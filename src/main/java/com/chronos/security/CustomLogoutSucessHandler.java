package com.chronos.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomLogoutSucessHandler extends SimpleUrlLogoutSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomLogoutSucessHandler.class);

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        setDefaultTargetUrl("/login");
        logger.info("Endereço :"+request.getRemoteAddr());
        logger.info("Remote Host :"+request.getRemoteHost());
        logger.info("Brosew info :"+request.getHeader("User-Agent"));
        super.onLogoutSuccess(request, response, authentication);
    }
}
