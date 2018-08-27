package com.chronos.controll.mdfe;

import com.chronos.modelo.entidades.Empresa;
import com.chronos.service.ChronosException;
import com.chronos.service.comercial.MdfeService;
import com.chronos.transmissor.exception.EmissorException;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.jsf.Mensagem;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class MdfeEnventosControll implements Serializable {


    private static final long serialVersionUID = 1L;

    @Inject
    private MdfeService service;
    private Empresa empresa;

    private String retorno;

    @PostConstruct
    private void init() {
        empresa = FacesUtil.getEmpresaUsuario();
    }

    public void localizarMdfeNaoEncerrados() {
        try {
            retorno = service.consultarNaoEncerrados(empresa.getCnpj());
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Erro ao buscar MDF-e não encerrados", ex);
            } else if (ex instanceof EmissorException) {
                Mensagem.addErrorMessage("Erro ao buscar MDF-e não encerrados", ex);
            } else {
                throw new RuntimeException("", ex);
            }
        }
    }

    public String getRetorno() {
        return retorno;
    }

    public void setRetorno(String retorno) {
        this.retorno = retorno;
    }
}
