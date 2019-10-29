package com.chronos.erp.util.tenant;

import com.chronos.erp.modelo.tenant.Tenant;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by john on 27/12/17.
 */
public class ConstantsTenant {

    public static final List<Tenant> TENANTS = new ArrayList<>();

    public static final Map<String, EntityManagerFactory> FACTORIES = new HashMap<>();
}
