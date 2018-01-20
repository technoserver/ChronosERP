/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.util.jsf;

import com.chronos.modelo.entidades.*;
import com.chronos.modelo.entidades.tenant.Tenant;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.repository.Usuarios;
import com.chronos.security.UsuarioSistema;
import com.chronos.util.Biblioteca;
import com.chronos.util.cdi.CDIServiceLocator;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

/**
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


        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            Usuario usuario = (Usuario) session.getAttribute("userChronosERP");
            if (usuario == null) {
                getUsuarioSessao();
            }
            return FacesContext.getCurrentInstance().getExternalContext().isUserInRole(role);
        } catch (Exception ex) {

        }
        return false;
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

            Usuario usuario = (Usuario) session.getAttribute("userChronosERP");
            if (usuario == null) {
                Usuarios dao = CDIServiceLocator.getBean(Usuarios.class);
                String login = (String) session.getAttribute("usuarioSistema");
                usuario = dao.getUsuario(login);
                definirPermissoes(usuario);
                session.setAttribute("userChronosERP", usuario);
            }

            return usuario;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Tenant getTenantId() {
        try {

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            Tenant tenant = (Tenant) session.getAttribute("tenantId");
            return tenant;
        } catch (Exception e) {

        }
        return null;
    }

    public static Empresa getEmpresaUsuario() {
        Usuario user = getUsuarioSessao();
        return user.getColaborador().getPessoa().getListaEmpresa().stream()
                .findFirst()
                .orElse(null);
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


    private static void definirPermissoes(Usuario usuario) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        List<PapelFuncao> funcoes = usuario.getPapel().getListaPapelFuncao();

        if (usuario.getPapel().getAcessoCompleto().equals("S") || usuario.getAdministrador().equals("S")) {

            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        if (usuario.getLogin().equals("admin")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_SOFTHOUSE"));
        }

        funcoes.stream().forEach((p) -> {
            if (p.getPodeConsultar() != null && p.getPodeConsultar().equals("S")) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + p.getFuncao().getFormulario() + "_CONSULTAR"));
            }
            if (p.getPodeInserir() != null && p.getPodeInserir().equals("S")) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + p.getFuncao().getFormulario() + "_INSERIR"));
            }
            if (p.getPodeAlterar() != null && p.getPodeAlterar().equals("S")) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + p.getFuncao().getFormulario() + "_ALTERAR"));
            }
            if (p.getPodeExcluir() != null && p.getPodeExcluir().equals("S")) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + p.getFuncao().getFormulario() + "_EXCLUIR"));
            }
        });
        Repository<AdmModulo> admModuloRepository = CDIServiceLocator.getBean(Repository.class);
        List<AdmModulo> modulos = admModuloRepository.getEntitys(AdmModulo.class, "ativo", "S");
        modulos.forEach(m -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + m.getNome()));
        });
        Authentication authentication = new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getSenha(), authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static PdvMovimento getMovimento() {

        PdvMovimento movimento = null;
        try{
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            movimento = (PdvMovimento) session.getAttribute("caixaERP");
            if(movimento==null){
                Repository<PdvMovimento> repository = CDIServiceLocator.getBean(Repository.class);
                List<Filtro> filtros = new ArrayList<>();
                filtros.add(new Filtro("statusMovimento","A"));
                filtros.add(new Filtro("pdvTurno.id",1));
                filtros.add(new Filtro("pdvCaixa.id",1));
                filtros.add(new Filtro("pdvOperador.id",1));
                Object[] atributos;
                atributos = new Object[]{"idGerenteSupervisor","dataAbertura","horaAbertura","dataFechamento","horaFechamento","totalSuprimento","totalSangria","totalVenda","totalDesconto","totalAcrescimo","totalFinal","totalRecebido","totalTroco","totalCancelado","statusMovimento","empresa.id"};
                movimento = repository.get(PdvMovimento.class,filtros,atributos);
                session.setAttribute("caixaERP", movimento);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return movimento;
    }

    public static void setMovimento(PdvMovimento movimento) {
        try{
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            session.removeAttribute("caixaERP");
            session.setAttribute("caixaERP", movimento);
        }catch (Exception ex){

        }
    }
}
