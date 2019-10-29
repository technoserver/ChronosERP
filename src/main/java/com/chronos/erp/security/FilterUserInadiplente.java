package com.chronos.erp.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class FilterUserInadiplente extends GenericFilterBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilterUserInadiplente.class);
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private AuthenticationManager authenticationManager;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (attemptAuthentication(request) == AuthenticationStates.REDIRECT) {
            LOGGER.debug("redirecionando para pagia de acesso não permitido");
            this.redirectStrategy.sendRedirect((HttpServletRequest) request, (HttpServletResponse) response, "/error/acessoNaoPermitido.xhtml");
        } else {

            filterChain.doFilter(request, response);
        }
    }

    private AuthenticationStates attemptAuthentication(ServletRequest request) {
        AuthenticationStates state = AuthenticationStates.CONTINUE;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> authorities;
        if ((authentication != null && authentication.isAuthenticated())) {
            authorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
            boolean inadiplemente = authorities.stream().filter(a -> a.equals("ROLE_INADIPLENTE")).findFirst().isPresent();

            if (inadiplemente) {
                state = AuthenticationStates.REDIRECT;

            }
        }
        return state;
    }

    private enum AuthenticationStates {
        REDIRECT, CONTINUE;
    }
}
