package com.chronos.util.tenant;

import com.chronos.modelo.entidades.tenant.Tenant;
import com.chronos.util.jsf.FacesUtil;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Created by john on 22/01/18.
 */
public class MultiTenantSchemaResolver implements CurrentTenantIdentifierResolver {

    private static final String DEFAULT_SCHEMA = "public";


    private Tenant tenant;


    @Override
    public String resolveCurrentTenantIdentifier() {

        tenant = FacesUtil.getTenantId();
        String schema = tenant != null ? tenant.getNome() : null;

        if (schema != null) {
            return schema;
        }
        return DEFAULT_SCHEMA;
    }


    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }


    public String resolveTenantByHttpSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();


        return "";
    }
}
