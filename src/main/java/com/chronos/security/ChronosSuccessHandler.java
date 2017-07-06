package com.chronos.security;

import com.chronos.modelo.entidades.Usuario;
import com.chronos.repository.Usuarios;
import com.chronos.util.cdi.CDIServiceLocator;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ChronosSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private Usuarios dao;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            UsuarioSistema user = (UsuarioSistema) authentication.getPrincipal();
            Usuario usuario = null;
            usuario = user != null ? usuario = user.getUsuario() : getUsuario(authentication.getName());         
            request.getSession().setAttribute("usuarioSistema", usuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }

    public Usuario getUsuario(String usuario) throws Exception {
        dao = CDIServiceLocator.getBean(Usuarios.class);
        return dao.getUsuario(usuario);
    }
}
