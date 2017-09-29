package com.chronos.controll.nfe;

import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.NfeConfiguracao;
import com.chronos.modelo.entidades.NotaFiscalTipo;
import com.chronos.repository.Repository;
import com.chronos.service.comercial.vendas.nfe.NfeService;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.jsf.Mensagem;

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
    private Repository<NfeConfiguracao> configuracoes;
    @Inject
    private NfeService nfeService;

    private Empresa empresa;

    private HashMap<String, String> codigoModeloNfe;
    private String titulo;
    private String resultado;
    private String justificativa;
    private Integer numeroInicial;
    private Integer numeroFinal;
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
            Object atributos[] = new Object[]{"certificadoDigitalSenha", "webserviceAmbiente"};
            NfeConfiguracao configuracao = configuracoes.get(NfeConfiguracao.class, "empresa.id", 1, atributos);

            NotaFiscalTipo notaFiscalTipo = nfeService.getNotaFicalTipoByModelo(modelo);
            int serie = (notaFiscalTipo == null || notaFiscalTipo.getSerie() == null || notaFiscalTipo.getSerie() == null) ? 1 : Integer.valueOf(notaFiscalTipo.getSerie());
            resultado = nfeService.inutilizarNFe(configuracao, modelo, serie, numeroInicial, numeroFinal, justificativa);
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
}
