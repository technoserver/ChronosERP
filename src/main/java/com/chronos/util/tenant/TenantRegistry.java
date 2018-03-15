package com.chronos.util.tenant;

import com.chronos.modelo.tenant.Tenant;
import com.chronos.modelo.view.ViewUsuarioTenant;
import com.chronos.repository.TenantRepository;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.Serializable;
import java.util.Optional;

/**
 * Created by john on 26/12/17.
 */

public class TenantRegistry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private TenantRepository repository;

    public Optional<ViewUsuarioTenant> getTenant(final String login) {
        Optional<ViewUsuarioTenant> tenantUsuario = repository.getUser(login);


        if (tenantUsuario.isPresent()) {

            Tenant tenant = new Tenant(tenantUsuario.get().getId(), tenantUsuario.get().getNomeTenant());
            if (ConstantsTenant.FACTORIES.isEmpty()) {
                final EntityManagerFactory emf = createEntityManagerFactory(tenant);
                ConstantsTenant.FACTORIES.put("ChronosLightUP", emf);
            }


        }
        return tenantUsuario;
    }

    @PreDestroy
    public void shutdownTenants() {
        ConstantsTenant.FACTORIES.forEach((tenantName, entityManagerFactory) -> entityManagerFactory.close());
        ConstantsTenant.FACTORIES.clear();
        ConstantsTenant.TENANTS.clear();
    }

    private EntityManagerFactory createEntityManagerFactory(final Tenant tenant) {
        return Persistence.createEntityManagerFactory("ChronosLightUP");
    }

    public EntityManagerFactory getEntityManagerFactory(final String tenantName) {
        return ConstantsTenant.FACTORIES.get(tenantName);
    }
}
