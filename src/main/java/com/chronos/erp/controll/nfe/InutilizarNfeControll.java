package com.chronos.erp.controll.nfe;

import com.chronos.erp.modelo.entidades.Empresa;
import com.chronos.erp.modelo.entidades.NotaFiscalTipo;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.comercial.NfeService;
import com.chronos.erp.util.jsf.FacesUtil;
import com.chronos.erp.util.jsf.Mensagem;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by john on 26/09/17.
 */
@Named
@ViewScoped
public class InutilizarNfeControll implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private NfeService nfeService;
    @Inject
    private Repository<NotaFiscalTipo> notaFiscalTipoRepository;

    private Empresa empresa;

    private HashMap<String, String> codigoModeloNfe;
    private String titulo;
    private String resultado;
    private String justificativa;
    private Integer numeroInicial;
    private Integer numeroFinal;
    private Integer serie;
    private String modelo;

    @PostConstruct
    private void init() {
        titulo = "Inutilizar Número";
        empresa = FacesUtil.getEmpresaUsuario();
        codigoModeloNfe = new HashMap<>();
        codigoModeloNfe.put("Nota Fiscal Eletrônica - NFe", "55");
        codigoModeloNfe.put("Nota Fiscal Cupom Eletrônica - NFCe", "65");
    }

    public void inutilizaNumero() {


        try {

            ModeloDocumento modeloDocumento = ModeloDocumento.getByCodigo(Integer.valueOf(modelo));

            resultado = nfeService.inutilizarNFe(empresa, modeloDocumento, serie, numeroInicial, numeroFinal, justificativa);
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }
    }


    public HashMap<String, String> getCodigoModeloNfe() {
        return codigoModeloNfe;
    }

    public void setCodigoModeloNfe(HashMap<String, String> codigoModeloNfe) {
        this.codigoModeloNfe = codigoModeloNfe;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public Integer getNumeroInicial() {
        return numeroInicial;
    }

    public void setNumeroInicial(Integer numeroInicial) {
        this.numeroInicial = numeroInicial;
    }

    public Integer getNumeroFinal() {
        return numeroFinal;
    }

    public void setNumeroFinal(Integer numeroFinal) {
        this.numeroFinal = numeroFinal;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getSerie() {
        return serie;
    }

    public void setSerie(Integer serie) {
        this.serie = serie;
    }
}
