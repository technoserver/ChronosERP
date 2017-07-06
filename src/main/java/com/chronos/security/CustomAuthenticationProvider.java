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
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author john
 */
@Service
public class CustomAuthenticationProvider extends ManualCDILookup implements AuthenticationProvider {

    private List<GrantedAuthority> grantedAuths;
    private Usuarios dao;
    private Usuario usr;

    @Override
    public Authentication authenticate(Authentication a) throws AuthenticationException {

        try {
            String usuario = a.getName();
            String senha = a.getCredentials().toString();
            senha = getSenha(usuario, senha);
            dao = CDIServiceLocator.getBean(Usuarios.class);

            Optional<Usuario> userOptional = dao.getUser(usuario);
            usr = userOptional.orElseThrow(() -> new UsernameNotFoundException("Usuário e/ou senha incorretos"));

            List<PapelFuncao> funcoes = dao.getPapelFuncao(usr);
            grantedAuths = (List<GrantedAuthority>) getAutorizacoes(funcoes);

            if (usr.getPapel().getAcessoCompleto().equals("S") || usr.getAdministrador().equals("S")) {

                grantedAuths.add(new SimpleGrantedAuthority("ADMINISTRADORES"));
            }

            Authentication auth = new UsernamePasswordAuthenticationToken(usuario, senha, grantedAuths);

            return auth;

        } catch (Exception e) {
            if (!(e instanceof NoResultException)) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Collection<? extends GrantedAuthority> getAutorizacoes(List<PapelFuncao> funcoes) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        funcoes.stream().map((p) -> {
            if (p.getPodeConsultar() != null && p.getPodeConsultar().equals("S")) {
                authorities.add(new SimpleGrantedAuthority(p.getFuncao().getNome() + "_CONSULTAR"));
            }
            return p;
        }).map((p) -> {
            if (p.getPodeInserir() != null && p.getPodeInserir().equals("S")) {
                authorities.add(new SimpleGrantedAuthority(p.getFuncao().getNome() + "_INSERIR"));
            }
            return p;
        }).map((p) -> {
            if (p.getPodeAlterar() != null && p.getPodeAlterar().equals("S")) {
                authorities.add(new SimpleGrantedAuthority(p.getFuncao().getNome() + "_ALTERAR"));
            }
            return p;
        }).filter((p) -> (p.getPodeExcluir() != null && p.getPodeExcluir().equals("S"))).forEach((p) -> {
            authorities.add(new SimpleGrantedAuthority(p.getFuncao().getNome() + "_EXCLUIR"));
        });

        return authorities;
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(UsernamePasswordAuthenticationToken.class);
    }

    private String getSenha(String usuario, String senha) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String senhaCrip = (usuario + senha).replaceAll("a", "@").replaceAll("e", "3").replaceAll("i", "1").replaceAll("o", "0").replaceAll("u", "#");
        senhaCrip += senhaCrip.length();
        senhaCrip = passwordEncoder.encode(senhaCrip);
        return senhaCrip;
    }

}
