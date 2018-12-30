package com.chronos.service;

import com.chronos.modelo.anotacoes.TaxaMaior;
import com.chronos.modelo.entidades.Usuario;
import com.chronos.repository.Repository;
import com.chronos.repository.UsuarioRepository;
import com.chronos.util.Biblioteca;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.jsf.Mensagem;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.inject.Inject;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;

/**
 * Created by john on 11/07/17.
 */
public abstract class AbstractService<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    protected Repository<T> repository;
    protected T objeto;
    protected boolean necessarioAutorizacaoSupervisor = false;
    protected boolean restricaoLiberada = false;
    @Inject
    private UsuarioRepository usuarioRepository;

    protected abstract Class<T> getClazz();

    protected boolean verificarRestricao() throws Exception {
        necessarioAutorizacaoSupervisor = false;
        if (!FacesUtil.getUsuarioSessao().getAdministrador().equals("S")) {
            Field fields[] = objeto.getClass().getDeclaredFields();
            for (Field f : fields) {
                if (f.isAnnotationPresent(TaxaMaior.class)) {
                    BigDecimal taxa = FacesUtil.getDescVenda();
                    if (taxa != null) {
                        Method metodo = objeto.getClass().getDeclaredMethod("get" + Biblioteca.primeiraMaiuscula(f.getName()));
                        BigDecimal valorCampo = (BigDecimal) metodo.invoke(objeto);
                        if (valorCampo != null && valorCampo.compareTo(taxa) > 0) {
                            necessarioAutorizacaoSupervisor = true;
                        }
                    }
                }

            }
        }

        return necessarioAutorizacaoSupervisor;

    }

    public boolean liberarRestricao(String usuario, String senha) {

        Usuario user = usuarioRepository.getUsuario(usuario);
        PasswordEncoder passwordEnocder = new BCryptPasswordEncoder();

        if (user == null || !passwordEnocder.matches(senha, user.getSenha())) {
            Mensagem.addInfoMessage("Login inválido ou usuário NÂO tem privilégio de supervisor.");
        } else if (user.getAdministrador() != null && user.getAdministrador().equals("S")) {
            restricaoLiberada = true;

        }

        return restricaoLiberada;
    }


    public boolean isNecessarioAutorizacaoSupervisor() {
        return necessarioAutorizacaoSupervisor;
    }

    public boolean isRestricaoLiberada() {
        return restricaoLiberada;
    }
}
