/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.erp.util.jsf;

import com.chronos.erp.dto.UsuarioDTO;
import com.chronos.erp.modelo.entidades.AdmParametro;
import com.chronos.erp.modelo.entidades.Empresa;
import com.chronos.erp.modelo.entidades.PdvMovimento;
import com.chronos.erp.modelo.entidades.RestricaoSistema;
import com.chronos.erp.modelo.tenant.Tenant;
import com.chronos.erp.security.UsuarioSistema;
import com.chronos.erp.util.Biblioteca;
import com.chronos.transmissor.init.Configuracoes;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.math.BigDecimal;

/**
 * @author john
 */
public class FacesUtil {

    @Inject
    protected FacesContext facesContext;
    private UsuarioSistema usuario;

    public static boolean isPostback() {
        return FacesContext.getCurrentInstance().isPostback();
    }


    public static void adicionaMensagem(FacesMessage.Severity severity, String mensagem, String msg2) {
        FacesMessage message = new FacesMessage(severity, mensagem, msg2);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }


    public static void downloadArquivo(File file, String nomeArquivo, boolean baixar) throws Exception {
        String content = baixar ? "attachment" : "inline";
        String contentType = baixar ? "application/octet-stream" : "application/pdf";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseHeader("Content-Type", contentType);
        externalContext.setResponseHeader("Content-Length", String.valueOf(file.length()));
        externalContext.setResponseHeader("Content-Disposition", content + "; filename=\"" + nomeArquivo + "\"");
        externalContext.getResponseOutputStream().write(Biblioteca.getBytesFromFile(file));
        facesContext.responseComplete();
    }


    public static boolean isNotPostback() {
        return !isPostback();
    }

    public static boolean isUserInRole(String role) {
        try {

            return FacesContext.getCurrentInstance().getExternalContext().isUserInRole(role);
        } catch (Exception ex) {

        }
        return false;
    }

    public static UsuarioDTO setUsuarioSessao(UsuarioDTO user) {
        try {

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            session.removeAttribute("userChronosERP");
            session.setAttribute("userChronosERP", user);
            return user;
        } catch (Exception e) {
            adicionaMensagem(FacesMessage.SEVERITY_ERROR, "Erro ao buscar os dados do usuario logado.", e.getMessage());
        }
        return null;
    }

    public static UsuarioDTO getUsuarioSessao() {
        try {

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("userChronosERP");

            return usuario;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static AdmParametro getParamentos() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        AdmParametro parametro = null;
        try {
            parametro = (AdmParametro) session.getAttribute("paramChronosERP");
        } catch (Exception ex) {

        }
        return parametro;
    }

    public static void setParamtro(AdmParametro paramtro) {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        session.removeAttribute("paramChronosERP");
        session.setAttribute("paramChronosERP", paramtro);
    }

    public static Tenant getTenantId() {
        try {
            Tenant tenant = null;
            SecurityContext contexto = SecurityContextHolder.getContext();
            if (contexto != null) {
                Authentication auth = contexto.getAuthentication();
                if (auth != null) {
                    if (auth.getPrincipal() instanceof UsuarioSistema) {
                        UsuarioSistema user = (UsuarioSistema) auth.getPrincipal();
                        tenant = new Tenant(user.getUsuario().getId(), user.getUsuario().getNomeTenant());
                    } else if (auth.getPrincipal() instanceof String) {
                        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                        tenant = (Tenant) session.getAttribute("tenantId");
                    }
                }

            }

            return tenant;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Empresa getEmpresaUsuario() {
        UsuarioDTO user = getUsuarioSessao();
        return user.getEmpresa();
    }

    public static void setEmpresaUsuario(Empresa empresa) {
        UsuarioDTO user = getUsuarioSessao();
        user.setEmpresa(empresa);
        setUsuarioSessao(user);
    }

    public static Configuracoes getConfEmissor() {
        Configuracoes conf = null;
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            conf = (Configuracoes) session.getAttribute("confEm");
            return conf;
        } catch (Exception ex) {
        }
        return conf;
    }

    public static void setConfEmissor(Configuracoes conf) {

        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            session.removeAttribute("confEm");
            session.setAttribute("confEm", conf);
        } catch (Exception ex) {
        }

    }

    public static RestricaoSistema getRestricao() {

        RestricaoSistema restricao = null;
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            restricao = (RestricaoSistema) session.getAttribute("restricoes");

        } catch (Exception ex) {
        }
        return restricao;
    }

    private static HttpSession getHttpSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }

    public static BigDecimal getRestricaoTaxaMaior() {
        return (BigDecimal) getHttpSession().getAttribute("restricaoTaxaMaior");
    }

    public static Integer getRestricaoDataMaior() {
        return (Integer) getHttpSession().getAttribute("restricaoDataMaior");
    }


    public static PdvMovimento getMovimento() {

        PdvMovimento movimento = null;
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            movimento = (PdvMovimento) session.getAttribute("caixaERP");


        } catch (Exception ex) {

        }
        return movimento;
    }

    public static void setMovimento(PdvMovimento movimento) {
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            session.removeAttribute("caixaERP");
            session.setAttribute("caixaERP", movimento);
        } catch (Exception ex) {

        }
    }
}
