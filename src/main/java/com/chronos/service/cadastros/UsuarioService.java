package com.chronos.service.cadastros;

import com.chronos.dto.UsuarioDTO;
import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.PdvMovimento;
import com.chronos.util.jsf.FacesUtil;

import java.io.Serializable;

/**
 * Created by john on 19/09/17.
 */
public class UsuarioService implements Serializable {

    private static final long serialVersionUID = 1L;

    private Empresa empresa;
    private PdvMovimento movimento;


    public UsuarioDTO getUsuarioLogado() {


        UsuarioDTO usuario = FacesUtil.getUsuarioSessao();

        return usuario;
    }

    public Empresa getEmpresaUsuario() {
        empresa = null;

        empresa = getUsuarioLogado().getEmpresa();
        return empresa;
    }
}
