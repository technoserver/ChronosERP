
package com.chronos.erp.security;

import com.chronos.erp.modelo.view.ViewUsuarioTenant;
import com.chronos.erp.repository.UsuarioRepository;
import com.chronos.erp.util.cdi.CDIServiceLocator;
import com.chronos.erp.util.cdi.ManualCDILookup;
import com.chronos.erp.util.tenant.TenantRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.HashSet;
import java.util.Optional;

/**
 * @author john
 */
@Service
public class AppUserDetailsService extends ManualCDILookup implements UserDetailsService {


    private ViewUsuarioTenant usr;
    private TenantRegistry tenantRegistry;

    @Override
    public UserDetails loadUserByUsername(String usuario) throws UsernameNotFoundException {
        try {

            tenantRegistry = CDIServiceLocator.getBean(TenantRegistry.class);
            UsuarioRepository repository = CDIServiceLocator.getBean(UsuarioRepository.class);

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
