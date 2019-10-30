package com.chronos.erp.controll.nfe;

import com.chronos.erp.service.cadastros.UsuarioService;
import com.chronos.erp.service.comercial.NfeService;
import com.chronos.erp.util.jsf.Mensagem;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 26/09/17.
 */
@Named
@ViewScoped
public class StatusNfeControll implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    protected FacesContext facesContext;
    private String titulo;
    private String status;
    @Inject
    private UsuarioService userService;
    @Inject
    private NfeService service;

    @PostConstruct
    private void initid() {
        titulo = "Consulta";

    }

    public void consultaStatus() {


        try {
            status = service.consultarStatusNfe(ModeloDocumento.NFE);
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("", e);
        }
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
