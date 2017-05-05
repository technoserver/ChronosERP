/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.util.jsf;

import com.chronos.util.Biblioteca;
import java.io.File;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author john
 */
public class FacesUtil {

    public static boolean isPostback() {
        return FacesContext.getCurrentInstance().isPostback();
    }

    public static void downloadArquivo(File file, String nomeArquivo) throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseHeader("Content-Type", "text/plain");
        externalContext.setResponseHeader("Content-Length", String.valueOf(file.length()));
        externalContext.setResponseHeader("Content-Disposition", "attachment;filename=\"" + nomeArquivo + "\"");
        externalContext.getResponseOutputStream().write(Biblioteca.getBytesFromFile(file));
        facesContext.responseComplete();
    }

    public static boolean isNotPostback() {
        return !isPostback();
    }

    public static boolean isUserInRole(String role) {

        return FacesContext.getCurrentInstance().getExternalContext().isUserInRole(role);
    }

}
