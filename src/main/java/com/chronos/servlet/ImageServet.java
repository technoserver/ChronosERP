package com.chronos.servlet;

import com.chronos.dto.Image;
import com.chronos.util.ArquivoUtil;
import com.chronos.util.Biblioteca;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by john on 05/07/17.
 */
@WebServlet("/image/*")
public class ImageServet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String imagem = getParametro(request);

        Image img = null;

        if (!StringUtils.isEmpty(imagem)) {

            File file = new File(imagem);

            if (file.exists()) {
                img = new Image();
                byte[] bb;

                try {
                    bb = Biblioteca.getBytesFromFile(file);
                    img.setContent(bb);
                    img.setContentType("image/jpg");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
                    return;
                }
            }


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

    private String getParametro(HttpServletRequest request) {
        String url = request.getRequestURI();
        String parametro = null;
        Enumeration<String> colections = request.getParameterNames();
        List<String> paramentros = Collections.list(colections);

        if (url.contains("empresa")) {
            parametro = request.getParameter("logoEmp");
        } else if (url.contains("colaborador")) {
            parametro = request.getParameter("foto3x4");

        } else if (url.contains("perfil")) {
            parametro = request.getParameter("foto");
        } else if (url.contains("produtoTemp")) {
            parametro = request.getParameter("foto");
            if (!StringUtils.isEmpty(parametro)) {
                parametro = ArquivoUtil.getInstance().getFotoProdutoTemp(parametro);
            }
        } else if (url.contains("produto")) {
            parametro = request.getParameter("foto");
            if (!StringUtils.isEmpty(parametro)) {
                parametro = ArquivoUtil.getInstance().getFotoProduto(parametro);
            }
        }

        return parametro;
    }
}
