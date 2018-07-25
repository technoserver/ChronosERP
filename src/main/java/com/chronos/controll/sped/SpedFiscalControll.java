package com.chronos.controll.sped;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.Contador;
import com.chronos.service.ChronosException;
import com.chronos.service.fiscal.SpedIcmsIpiService;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.jsf.Mensagem;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.Serializable;
import java.util.*;

/**
 * Created by john on 08/10/17.
 */
@Named
@ViewScoped
public class SpedFiscalControll extends AbstractControll<Contador> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private SpedIcmsIpiService icmsIpiService;

    private Date dataInicial;
    private Date dataFinal;
    private String versao;
    private String finalidadeArquivo;
    private String perfil;
    private Integer inventario;
    private Integer idContador;
    private Map<String, Integer> contadores;

    @PostConstruct
    @Override
    public void init() {
        super.init();
        contadores = new LinkedHashMap<>();
        try {
            List<Contador> listaContador = dao.getEntitys(Contador.class);
            for (Contador c : listaContador) {
                contadores.put(c.getNome(), c.getId());
            }
            if (contadores.isEmpty()) {
                contadores.put("Nenhum contador cadastrado", 0);
            }
        } catch (Exception e) {
            //e.printStackTrace();
            contadores.put("Ocorreu um erro ao buscar os dados de contadores", 0);
        }
    }

    public void geraSpedFiscal() {
        try {
            Calendar d1 = Calendar.getInstance();
            Calendar d2 = Calendar.getInstance();
            d1.setTime(dataInicial);
            d2.setTime(dataFinal);
            File arquivo = icmsIpiService.geraArquivo(versao, finalidadeArquivo, perfil, inventario, dataInicial, dataFinal, idContador);
            FacesUtil.downloadArquivo(arquivo, "spedfiscal.txt");
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Ocorreu um erro ao gerar o arquivo.", ex);
            } else {
                throw new RuntimeException(ex);

            }
        }
    }

    @Override
    protected Class<Contador> getClazz() {
        return Contador.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "SPED_FISCAL";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getVersao() {
        return versao;
    }

    public void setVersao(String versao) {
        this.versao = versao;
    }

    public String getFinalidadeArquivo() {
        return finalidadeArquivo;
    }

    public void setFinalidadeArquivo(String finalidadeArquivo) {
        this.finalidadeArquivo = finalidadeArquivo;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public Integer getInventario() {
        return inventario;
    }

    public void setInventario(Integer inventario) {
        this.inventario = inventario;
    }

    public Integer getIdContador() {
        return idContador;
    }

    public void setIdContador(Integer idContador) {
        this.idContador = idContador;
    }

    public Map<String, Integer> getContadores() {
        return contadores;
    }

    public void setContadores(Map<String, Integer> contadores) {
        this.contadores = contadores;
    }
}
