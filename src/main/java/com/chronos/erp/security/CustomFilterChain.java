package com.chronos.erp.security;

import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.List;

public class CustomFilterChain extends FilterChainProxy {

    public CustomFilterChain() {
        super(filterChains());
    }

    private static List<SecurityFilterChain> filterChains() {
        List<SecurityFilterChain> filters = new ArrayList<>();

        LogoutFilter customLogout = new LogoutFilter(new CustomLogoutSucessHandler(), new SecurityContextLogoutHandler());
        customLogout.setFilterProcessesUrl("/customlogout");
        filters.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/customlogout**"), customLogout));
        return filters;
    }
}
