package com.chronos.security;


import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ChronosSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            UsuarioSistema user = (UsuarioSistema) authentication.getPrincipal();
            request.getSession().setAttribute("tenantId", user.getUsuario().getTenant());
            request.getSession().setAttribute("usuarioSistema", user.getUsuario().getLogin());

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }


}
