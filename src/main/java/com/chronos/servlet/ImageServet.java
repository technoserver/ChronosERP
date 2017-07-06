package com.chronos.servlet;

import com.chronos.dto.Image;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by john on 05/07/17.
 */
@WebServlet("/image/*")
public class ImageServet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String imagem = request.getParameter("img");

        Image img = null;

        if(!StringUtils.isEmpty(imagem)){

        }

        if (img == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }

        response.reset();
        response.setContentType(img.getContentType());
        response.setContentLength(img.getContent().length);

        response.getOutputStream().write(img.getContent());

    }
}
