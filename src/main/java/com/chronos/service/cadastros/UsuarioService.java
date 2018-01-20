package com.chronos.service.cadastros;

import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.PdvMovimento;
import com.chronos.modelo.entidades.Usuario;
import com.chronos.security.UsuarioLogado;
import com.chronos.security.UsuarioSistema;
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
        UsuarioSistema user = null;

        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
        Usuario usuario = null;
        if (auth != null && auth.getPrincipal() != null) {
            user = (UsuarioSistema) auth.getPrincipal();
            //usuario = user.getUsuario();
        }

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
