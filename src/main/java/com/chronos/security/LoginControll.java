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
                
                if (usr.getPapel().getAcessoCompleto().equals("S") || usr.getAdministrador().equals("S")) {
                    
                    grantedAuths.add(new SimpleGrantedAuthority("ADMIN"));
                } 
                user = new UsuarioSistema(usr, grantedAuths);
                Authentication auth = new UsernamePasswordAuthenticationToken(usuario, senha, grantedAuths);
               
                return auth;
            } else {
                throw new UsernameNotFoundException("Usuário não encontrado.");
            }

        } catch (NamingException ex) {
            throw new RuntimeException(ex);            
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
      
    }

    private Collection<? extends GrantedAuthority> getAutorizacoes(List<PapelFuncao> funcoes) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        funcoes.stream().map((p) -> {
            if(p.getPodeConsultar().equals("S")){
                authorities.add(new SimpleGrantedAuthority(p.getFuncao().getNome()+"_CONSULTAR"));
            }
            return p;
        }).map((p) -> {
            if(p.getPodeInserir().equals("S")){
                authorities.add(new SimpleGrantedAuthority(p.getFuncao().getNome()+"_INSERIR"));
            }
            return p;
        }).map((p) -> {
            if(p.getPodeAlterar().equals("S")){
                authorities.add(new SimpleGrantedAuthority(p.getFuncao().getNome()+"_ALTERAR"));
            }
            return p;
        }).filter((p) -> (p.getPodeExcluir().equals("S"))).forEach((p) -> {
            authorities.add(new SimpleGrantedAuthority(p.getFuncao().getNome()+"_EXCLUIR"));
        });

        return authorities;
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(UsernamePasswordAuthenticationToken.class);
    }

}
