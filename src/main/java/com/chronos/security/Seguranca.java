/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.security;

import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.Usuario;
import com.chronos.util.ArquivoUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;

/**
 *
 * @author john
 */
@Named
@RequestScoped
public class Seguranca {

    @Inject
    private ExternalContext externalContext;
    private Empresa empresa;
    private UsuarioSistema usuario;

    public String getNomeUsuario() {
        String nome = null;

        UsuarioSistema usuarioLogado = getUsuarioLogado();

        if (usuarioLogado != null) {
            nome = usuarioLogado.getUsuario().getLogin();
        }

        return nome;
    }

    public String getFotoFuncionario() {
        String foto = "";
        usuario = getUsuarioLogado();
        if (usuario != null) {
            empresa = getEmpresaUsuario(getUsuarioLogado().getUsuario());
            foto = ArquivoUtil.getInstance().getFotoFuncionario(empresa.getCnpj(), usuario.getUsuario().getColaborador().getPessoa().getPessoaFisica().getCpf());
        }


        return new File(foto).exists() ? foto : null;
    }

    public String getNomeEmpresa() {
        usuario = getUsuarioLogado();
        String nomeEmpresa;
        nomeEmpresa = usuario != null ? getEmpresaUsuario(usuario.getUsuario()).getRazaoSocial() : "";

        return nomeEmpresa;
    }

    @Produces
    @UsuarioLogado
    public UsuarioSistema getUsuarioLogado() {
        usuario = null;

        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();

        if (usuario == null && auth != null && auth.getPrincipal() != null) {
            usuario = (UsuarioSistema) auth.getPrincipal();
        }

        return usuario;
    }

    public Empresa getEmpresaUsuario(Usuario usuario) {
        empresa = null;
        if (usuario != null) {
            usuario.getColaborador().getPessoa().getListaEmpresa().stream().forEach((e) -> {
                empresa = e;
            });
        }
        return empresa;
    }

}
