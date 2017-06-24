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
import java.util.Optional;
import javax.persistence.NoResultException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author john
 */
@Service
public class AppUserDetailsService extends ManualCDILookup implements UserDetailsService {

    private Usuarios dao;
    private List<GrantedAuthority> grantedAuths;
    private Usuario usr;

    @Override
    public UserDetails loadUserByUsername(String usuario) throws UsernameNotFoundException {
        try {
            dao = CDIServiceLocator.getBean(Usuarios.class);

            Optional<Usuario> userOptional = dao.getUser(usuario);
            usr = userOptional.orElseThrow(() -> new UsernameNotFoundException("Usuário e/ou senha incorretos"));
            usr = dao.getUsuario(usuario);
          //  List<PapelFuncao> funcoes = dao.getPapelFuncao(usr);
            grantedAuths = (List<GrantedAuthority>) getAutorizacoes(usr.getPapel().getListaPapelFuncao());

            if (usr.getPapel().getAcessoCompleto().equals("S") || usr.getAdministrador().equals("S")) {

                grantedAuths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
            return new UsuarioSistema(usr, grantedAuths);
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

}
