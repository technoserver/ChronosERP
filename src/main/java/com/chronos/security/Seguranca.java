/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.security;

import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.Usuario;
import com.chronos.util.ArquivoUtil;
import com.chronos.util.jsf.FacesUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.StringUtils;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;

/**
 * @author john
 */
@Named
@RequestScoped
public class Seguranca {

    @Inject
    private ExternalContext externalContext;
    private Empresa empresa;


    private Usuario usuario;



    public String getNomeUsuario() {
        String nome = null;


        usuario = getUsuarioLogado();

        if (usuario != null) {
            nome = usuario.getLogin();
        }


        return nome;
    }

    //TODO verificar
    public String getFotoFuncionario() {
        String foto = "";
        usuario = getUsuarioLogado();
        if (usuario != null) {
            empresa = FacesUtil.getEmpresaUsuario();

            if(usuario.getColaborador().getPessoa().getTipo().equals("F") && !StringUtils.isEmpty(usuario.getColaborador().getPessoa().getPessoaFisica().getCpf())){
                foto = ArquivoUtil.getInstance().getFotoFuncionario(empresa.getCnpj(), usuario.getColaborador().getPessoa().getPessoaFisica().getCpf());
            }

        }


        return new File(foto).exists() ? foto : null;
    }

    //TODO verificar
    public String getNomeEmpresa() {
        String nomeEmpresa = null;
        nomeEmpresa = FacesUtil.getEmpresaUsuario().getRazaoSocial();

        return nomeEmpresa;
    }

    @Produces
    @UsuarioLogado
    public Usuario getUsuarioLogado() {


        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken)
                FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();

        if (auth != null && auth.getPrincipal() != null && usuario == null) {
            usuario = FacesUtil.getUsuarioSessao();
        }

        return usuario;
    }


    public boolean isTemAcessoEpresa() {
        return getUsuarioLogado().getAdministrador().equals("S");
    }

    public boolean isTemAcesso(String modulo) {
        return FacesUtil.isUserInRole(modulo + "_CONSULTAR") || FacesUtil.isUserInRole("ADMIN");

    }

    // <editor-fold defaultstate="collapsed" desc="Modulo Comercial">
    public boolean isComercial() {
        return isUserNfe() || isUserNfce() || isUserCte() || isUserNFSe() || isUserComissoes() || isUserOs();
    }

    public boolean isUserVendas() {
        return FacesUtil.isUserInRole("VENDA");
    }

    public boolean isUserNfe() {
        return FacesUtil.isUserInRole("NFE");
    }

    public boolean isUserNfce() {
        return FacesUtil.isUserInRole("NFCE");
    }

    public boolean isUserCte() {
        return FacesUtil.isUserInRole("CTE");
    }

    public boolean isUserNFSe() {
        return FacesUtil.isUserInRole("NFSE");
    }

    public boolean isUserComissoes() {
        return FacesUtil.isUserInRole("COMISSOES");
    }

    public boolean isUserOs() {
        return FacesUtil.isUserInRole("OS");
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Modulo Administrativo">
    public boolean isAdministrativo() {
        return isUserGed() || isUserEtiquetas() || isUserAgenda() || isUserBI();
    }

    public boolean isUserGed() {
        return FacesUtil.isUserInRole("GED");
    }

    public boolean isUserEtiquetas() {
        return FacesUtil.isUserInRole("ETIQUETAS");
    }

    public boolean isUserAgenda() {
        return FacesUtil.isUserInRole("AGENDA");
    }

    public boolean isUserBI() {
        return FacesUtil.isUserInRole("BI");
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Modulo Financeiro">
    public boolean isFinanceiro() {
        return FacesUtil.isUserInRole("FINANCEIRO");
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Modulo Recursos Humanos">
    public boolean isRecursosHumano() {
        return isUserPonto() || isUserFolha() || isUserEsocial();
    }

    public boolean isUserPonto() {
        return FacesUtil.isUserInRole("PONTO");
    }

    public boolean isUserFolha() {
        return FacesUtil.isUserInRole("FOLHA");
    }

    public boolean isUserEsocial() {
        return FacesUtil.isUserInRole("ESOCIAL");
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Modulo Suplimentos">
    public boolean isSuprimentos() {
        return isUserEstoque() || isUserCompras();
    }

    public boolean isUserCompras() {
        return FacesUtil.isUserInRole("COMPRAS");
    }

    public boolean isUserEstoque() {
        return FacesUtil.isUserInRole("ESTOQUE");
    }

    public boolean isUserContratos() {
        return FacesUtil.isUserInRole("CONTRATOS");
    }

    public boolean isUserInventario() {
        return FacesUtil.isUserInRole("INVENTARIO");
    }

    public boolean isUserPCP() {
        return FacesUtil.isUserInRole("PCP");
    }

    public boolean isUserWMS() {
        return FacesUtil.isUserInRole("WMS");
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Modulo Fiscal">
    public boolean isFiscal() {
        return isUserEscritaFiscal() || isUserSpedFiscal() || isUserSpedContribuicoes() || isUserTributacao();
    }

    public boolean isUserEscritaFiscal() {
        return FacesUtil.isUserInRole("ESCRITA_FISCAL");
    }

    public boolean isUserSpedFiscal() {
        return FacesUtil.isUserInRole("SPED");
    }

    public boolean isUserSpedContribuicoes() {
        return FacesUtil.isUserInRole("SPED_CONTRIBUICOES");
    }

    public boolean isUserTributacao() {
        return FacesUtil.isUserInRole("TRIBUTACAO");
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Modulo Contabil">
    public boolean isContabil() {
        return isUserContabil() || isUserSpedContabil() || isUserOrcamento();
    }

    public boolean isUserContabil() {
        return FacesUtil.isUserInRole("CONTABIL");
    }

    public boolean isUserSpedContabil() {
        return FacesUtil.isUserInRole("SPED_CONTABIL");
    }

    public boolean isUserOrcamento() {
        return FacesUtil.isUserInRole("ORCAMENTO");
    }

    // </editor-fold>

}
