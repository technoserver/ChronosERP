package com.chronos.service.cadastros;

import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.PdvMovimento;
import com.chronos.modelo.entidades.Usuario;
import com.chronos.security.UsuarioLogado;
import com.chronos.security.UsuarioSistema;
import com.chronos.util.jsf.FacesUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * Created by john on 19/09/17.
 */
public class UsuarioService implements Serializable {

    private static final long serialVersionUID = 1L;

    private Empresa empresa;
    private PdvMovimento movimento;

    @Produces
    @UsuarioLogado
    public Usuario getUsuarioLogado() {



        Usuario usuario = FacesUtil.getUsuarioSessao();

        return usuario;
    }

    public Empresa getEmpresaUsuario() {
        empresa = null;

        getUsuarioLogado().getColaborador().getPessoa().getListaEmpresa().stream().forEach((e) -> {
            empresa = e;
        });
        return empresa;
    }
}
