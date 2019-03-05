
package com.chronos.security;


import com.chronos.modelo.tenant.UsuarioTenant;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 *
 * @author john
 */
public class UsuarioSistema extends User {

    private static final long serialVersionUID = 1L;

    private UsuarioTenant usuario;

    public UsuarioSistema(UsuarioTenant usuario, Collection<? extends GrantedAuthority> authorities) {
         super(usuario.getLogin(), usuario.getSenha(), authorities);
        this.usuario = usuario;
    }

    public UsuarioTenant getUsuario() {
        return usuario;
    }
}
