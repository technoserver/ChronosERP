package com.chronos.erp.service;

import com.chronos.erp.modelo.anotacoes.TaxaMaior;
import com.chronos.erp.modelo.entidades.RestricaoSistema;
import com.chronos.erp.modelo.entidades.Usuario;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.repository.UsuarioRepository;
import com.chronos.erp.util.Biblioteca;
import com.chronos.erp.util.jsf.FacesUtil;
import com.chronos.erp.util.jsf.Mensagem;
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
                    RestricaoSistema restricao = FacesUtil.getRestricao();
                    BigDecimal taxa = restricao == null ? BigDecimal.ZERO : restricao.getDescontoVenda();
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

    public boolean podeAlteraPrecoVenda() {
        RestricaoSistema restricao = FacesUtil.getRestricao();

        boolean isAdministrador = FacesUtil.getUsuarioSessao().getAdministrador().equals("S");

        return isAdministrador || restricao == null ? true : restricao.getAlteraPrecoNaVenda().equals("S") ? true : false;
    }


    public boolean isNecessarioAutorizacaoSupervisor() {
        return necessarioAutorizacaoSupervisor;
    }

    public boolean isRestricaoLiberada() {
        return restricaoLiberada;
    }


}
