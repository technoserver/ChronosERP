package com.chronos.erp.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

public class ChronosAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = 3798723588865329956L;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        User authUser = (User) SecurityContextHolder.getContext().getAuthentication();
        if (authUser != null) {
            redirectStrategy.sendRedirect(request, response, "/error/acessoNaoPermitido.xhtml");
        } else {
            redirectStrategy.sendRedirect(request, response, "/login.xhtml");
            ;
        }
    }
}
