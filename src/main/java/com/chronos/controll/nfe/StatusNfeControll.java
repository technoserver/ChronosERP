package com.chronos.controll.nfe;

import com.chronos.infra.enuns.ModeloDocumento;
import com.chronos.service.cadastros.UsuarioService;
import com.chronos.service.comercial.NfeService;
import com.chronos.util.jsf.Mensagem;

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
