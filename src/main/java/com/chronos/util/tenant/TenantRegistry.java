package com.chronos.util.tenant;

import com.chronos.modelo.entidades.tenant.Tenant;
import com.chronos.modelo.entidades.tenant.UsuarioTenant;
import com.chronos.repository.TenantRepository;
import org.hibernate.MultiTenancyStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Created by john on 26/12/17.
 */

public class TenantRegistry implements Serializable {

    private static final long serialVersionUID = 1L;


    @Inject
    private TenantRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(TenantRegistry.class);


    public Optional<UsuarioTenant> getTenant(final String login) {
        Optional<UsuarioTenant> tenantUsuario = repository.getUser(login);
        ;

        if (tenantUsuario.isPresent() && !ConstantsTenant.TENANTS.contains(tenantUsuario.get().getTenant())) {
            ConstantsTenant.TENANTS.add(tenantUsuario.get().getTenant());
            final EntityManagerFactory emf = createEntityManagerFactory(tenantUsuario.get().getTenant());
            ConstantsTenant.FACTORIES.put(tenantUsuario.get().getTenant().getNome(), emf);
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
        final Map<String, String> props = new TreeMap<>();
        logger.debug("Criando entity manager factory para schema '" + tenant.getNome() + "' for tenant '" + tenant.getNome() + "'.");
//        props.put("javax.persistence.jdbc.url", "jdbc:postgresql://localhost:5432/chronosLight?currentSchema=" + tenant.getNome());
//        props.put("hibernate.multiTenancy", MultiTenancyStrategy.SCHEMA.name());
//        props.put("hibernate.multi_tenant_connection_provider", dsProvider);
//        props.put("hibernate.tenant_identifier_resolver", tenantResolver);;
        props.put("hibernate.default_schema", tenant.getNome());
        return Persistence.createEntityManagerFactory("ChronosLightUP", props);
    }

    public EntityManagerFactory getEntityManagerFactory(final String tenantName) {
        return ConstantsTenant.FACTORIES.get(tenantName);
    }
}
