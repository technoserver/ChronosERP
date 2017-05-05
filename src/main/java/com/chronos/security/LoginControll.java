/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.security;

import com.chronos.modelo.entidades.PapelFuncao;
import com.chronos.modelo.entidades.Usuario;
import com.chronos.repository.Usuarios;
import com.chronos.util.cdi.CDIServiceLocator;
import com.chronos.util.cdi.ManualCDILookup;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.naming.NamingException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author john
 */
public class LoginControll extends ManualCDILookup implements AuthenticationProvider {

    private List<GrantedAuthority> grantedAuths;
    private Usuarios dao;
   

    @Override
    public Authentication authenticate(Authentication a) throws AuthenticationException {

        try {
            Usuario usr = null;
            UsuarioSistema user = null;
            String usuario = a.getName();
            String senha = a.getCredentials().toString();
            Md5PasswordEncoder enc = new Md5PasswordEncoder();
            senha = enc.encodePassword(usuario + senha, null);

            dao = CDIServiceLocator.getBean(Usuarios.class);
            usr = dao.getUsuario(usuario, senha);
            if (usr != null) {
                List<PapelFuncao> funcoes = dao.getPapelFuncao(usr); 
                grantedAuths =  (List<GrantedAuthority>) getAutorizacoes(funcoes);
                if (usr.getPapel().getAcessoCompleto().equals("S")) {
                    
                    grantedAuths.add(new SimpleGrantedAuthority("ADMIN"));
                } 
                user = new UsuarioSistema(usr, grantedAuths);
                Authentication auth = new UsernamePasswordAuthenticationToken(usuario, senha, grantedAuths);
               
                return auth;
            } else {
                throw new UsernameNotFoundException("Usuário não encontrado.");
            }

        } catch (NamingException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private Collection<? extends GrantedAuthority> getAutorizacoes(List<PapelFuncao> funcoes) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (PapelFuncao p : funcoes) {
            authorities.add(new SimpleGrantedAuthority(p.getFuncao().getNome()));

        }

        return authorities;
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(UsernamePasswordAuthenticationToken.class);
    }

}
