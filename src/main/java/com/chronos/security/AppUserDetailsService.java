/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.security;

import com.chronos.modelo.entidades.AdmModulo;
import com.chronos.modelo.view.ViewUsuarioTenant;
import com.chronos.repository.Repository;
import com.chronos.repository.UsuarioRepository;
import com.chronos.util.cdi.CDIServiceLocator;
import com.chronos.util.cdi.ManualCDILookup;
import com.chronos.util.tenant.TenantRegistry;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author john
 */
@Service
public class AppUserDetailsService extends ManualCDILookup implements UserDetailsService {

    private UsuarioRepository dao;
    private List<GrantedAuthority> grantedAuths;
    private ViewUsuarioTenant usr;
    private Repository<AdmModulo> admModuloRepository;


    private TenantRegistry tenantRegistry;

    @Override
    public UserDetails loadUserByUsername(String usuario) throws UsernameNotFoundException {
        try {

            tenantRegistry = CDIServiceLocator.getBean(TenantRegistry.class);
            dao = CDIServiceLocator.getBean(UsuarioRepository.class);

            Optional<ViewUsuarioTenant> userOptional = tenantRegistry.getTenant(usuario);
            usr = userOptional.orElseThrow(() -> new UsernameNotFoundException("Usuário e/ou senha incorretos"));
            return new UsuarioSistema(usr, new HashSet<>());
        } catch (Exception e) {
            if (!(e instanceof NoResultException)) {
                e.printStackTrace();
            }
        }
        return null;

    }


}
