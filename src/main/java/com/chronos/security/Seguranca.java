/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.security;

import com.chronos.dto.UsuarioDTO;
import com.chronos.modelo.entidades.Empresa;
import com.chronos.util.jsf.FacesUtil;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.File;
import java.io.Serializable;
import java.util.Optional;

/**
 * @author john
 */
@Named
@RequestScoped
public class Seguranca implements Serializable {




    public String getNomeUsuario() {
        return getUsuarioLogado().getNome();
    }

    //TODO verificar
    public String getFotoFuncionario() {
        String foto = "";
        if (getUsuarioLogado() != null) {
            foto = Optional.ofNullable(getUsuarioLogado().getFoto()).orElse("");
        }
        return new File(foto).exists() ? foto : null;
    }

    //TODO verificar
    public String getNomeEmpresa() {
        String nomeEmpresa = null;
        nomeEmpresa = getEmpresa().getRazaoSocial();

        return nomeEmpresa;
    }


    public String getCargo() {
        return getUsuarioLogado().getCargo();
    }

    public boolean isTemAcessoEpresa() {
        return getUsuarioLogado().getAdministrador().equals("S");
    }


    public boolean isTemAcesso(String modulo) {
        return FacesUtil.isUserInRole(modulo + "_CONSULTAR") || FacesUtil.isUserInRole("ADMIN");

    }

    private UsuarioDTO getUsuarioLogado() {
        return FacesUtil.getUsuarioSessao();
    }

    private Empresa getEmpresa() {
        return FacesUtil.getEmpresaUsuario();
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


    public boolean isAdministrador() {
        return FacesUtil.isUserInRole("ADMIN");
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
