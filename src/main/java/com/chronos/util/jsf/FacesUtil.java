/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.util.jsf;

import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.Usuario;
import com.chronos.security.UsuarioSistema;
import com.chronos.util.Biblioteca;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

/**
 *
 * @author john
 */
public class FacesUtil {

    private UsuarioSistema usuario;

    @Inject
    protected FacesContext facesContext;

    public static boolean isPostback() {
        return FacesContext.getCurrentInstance().isPostback();
    }


    public static void adicionaMensagem(FacesMessage.Severity severity, String mensagem, String msg2) {
        FacesMessage message = new FacesMessage(severity, mensagem, msg2);
        FacesContext.getCurrentInstance().addMessage(null, message);
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

    public static Usuario setUsuarioSessao(Usuario user) {
        try {

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            session.removeAttribute("usuarioSistema");
            session.setAttribute("usuarioSistema", user);
            return user;
        } catch (Exception e) {
            adicionaMensagem(FacesMessage.SEVERITY_ERROR, "Erro ao buscar os dados do usuario logado.", e.getMessage());
        }
        return null;
    }


    public static void setEmpresaUsuario(Empresa empresa) {
        Usuario user = getUsuarioSessao();
        List<Empresa> empresas = new ArrayList<>();
        empresas.addAll(user.getColaborador().getPessoa().getListaEmpresa());
        int indice = IntStream.range(0, empresas.size()).filter(i -> empresas.get(i).getId().equals(empresa.getId())).findAny()
                .getAsInt();
        empresas.remove(indice);
        empresas.add(empresa);
        user.getColaborador().getPessoa().setListaEmpresa(new HashSet<>(empresas));
        setUsuarioSessao(user);
    }

    public static Usuario getUsuarioSessao() {
        try {

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            return (Usuario) session.getAttribute("usuarioSistema");
        } catch (Exception e) {
            adicionaMensagem(FacesMessage.SEVERITY_ERROR, "Erro ao buscar os dados do usuario logado.", e.getMessage());
        }
        return null;
    }

    public static Empresa getEmpresaUsuario() {
        Usuario user = getUsuarioSessao();
        return user.getColaborador().getPessoa().getListaEmpresa().stream()
                .findFirst()
                .orElse(null);
    }

}
