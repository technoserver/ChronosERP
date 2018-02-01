package com.chronos.util.tenant;

import com.chronos.modelo.entidades.tenant.Tenant;
import com.chronos.modelo.entidades.view.ViewUsuarioTenant;
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
            if (!ConstantsTenant.TENANTS.contains(tenant)) {
                ConstantsTenant.TENANTS.add(tenant);
                final EntityManagerFactory emf = createEntityManagerFactory(tenant);
                ConstantsTenant.FACTORIES.put(tenant.getNome(), emf);
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

//
//        logger.debug("Criando entity manager factory para schema '" + tenant.getNome() + "' for tenant '" + tenant.getNome() + "'.");
//        props.put("javax.persistence.jdbc.url", "jdbc:postgresql://localhost:5432/chronosLight?currentSchema=" + tenant.getNome());
//       // props.put("hibernate.multiTenancy", MultiTenancyStrategy.SCHEMA.name());
//      //  props.put("hibernate.multi_tenant_connection_provider", dsProvider);
//      //  props.put("hibernate.tenant_identifier_resolver", tenant.getNome());;
//       // props.put("hibernate.default_schema", tenant.getNome());
        return Persistence.createEntityManagerFactory("ChronosLightUP");
    }

    public EntityManagerFactory getEntityManagerFactory(final String tenantName) {
        return ConstantsTenant.FACTORIES.get(tenantName);
    }
}
