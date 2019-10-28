
package com.chronos.erp.security;


import com.chronos.erp.modelo.view.ViewUsuarioTenant;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author john
 */
public class UsuarioSistema extends User {

    private static final long serialVersionUID = 1L;

    private ViewUsuarioTenant usuario;

    public UsuarioSistema(ViewUsuarioTenant usuario, Collection<? extends GrantedAuthority> authorities) {
        super(usuario.getLogin(), usuario.getSenha(), authorities);
        this.usuario = usuario;
    }

    public ViewUsuarioTenant getUsuario() {
        return usuario;
    }
}
