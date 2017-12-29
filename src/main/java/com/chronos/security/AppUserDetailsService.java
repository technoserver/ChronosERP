/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.security;

import com.chronos.modelo.entidades.AdmModulo;
import com.chronos.modelo.entidades.tenant.UsuarioTenant;
import com.chronos.repository.Repository;
import com.chronos.repository.Usuarios;
import com.chronos.util.cdi.CDIServiceLocator;
import com.chronos.util.cdi.ManualCDILookup;
import com.chronos.util.tenant.TenantRegistry;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author john
 */
@Service
public class AppUserDetailsService extends ManualCDILookup implements UserDetailsService {

    private Usuarios dao;
    private List<GrantedAuthority> grantedAuths;
    private UsuarioTenant usr;
    private Repository<AdmModulo> admModuloRepository;


    private TenantRegistry tenantRegistry;

    @Override
    public UserDetails loadUserByUsername(String usuario) throws UsernameNotFoundException {
        try {
//            dao = CDIServiceLocator.getBean(Usuarios.class);
//
//            Optional<Usuario> userOptional = dao.getUser(usuario);
//            usr = userOptional.orElseThrow(() -> new UsernameNotFoundException("Usuário e/ou senha incorretos"));
//            usr = dao.getUsuario(usuario);
//          //  List<PapelFuncao> funcoes = dao.getPapelFuncao(usr);
//            grantedAuths = (List<GrantedAuthority>) getAutorizacoes(usr.getPapel().getListaPapelFuncao());
//

//


            tenantRegistry = CDIServiceLocator.getBean(TenantRegistry.class);
            Optional<UsuarioTenant> userOptional = tenantRegistry.getTenant(usuario);
            usr = userOptional.orElseThrow(() -> new UsernameNotFoundException("Usuário e/ou senha incorretos"));
//            PasswordEncoder passwordEnocder = new BCryptPasswordEncoder();
//            if (passwordEnocder.matches("admin", usr.getSenha())) {
//                // Senha válida
//            } else {
//                // Senha inválida
//            }

            return new UsuarioSistema(usr, new ArrayList<>());
        } catch (Exception e) {
            if (!(e instanceof NoResultException)) {
                e.printStackTrace();
            }
        }
        return null;

    }



}
