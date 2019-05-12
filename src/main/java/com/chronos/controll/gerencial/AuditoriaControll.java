package com.chronos.controll.gerencial;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.Auditoria;
import com.chronos.repository.Filtro;
import org.springframework.util.StringUtils;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;

@Named
@ViewScoped
public class AuditoriaControll extends AbstractControll<Auditoria> implements Serializable {


    private String usuario;
    private Date dataInicial;
    private Date dataFinal;
    private String janela;
    private String descricao;

    @Override
    public ERPLazyDataModel<Auditoria> getDataModel() {

        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setDao(dao);
            dataModel.setClazz(Auditoria.class);
        }

        Object[] atributos = new Object[]{"dataRegistro", "horaRegistro", "janelaController", "acao", "conteudo", "usuario.login"};

        dataModel.getFiltros().clear();
        pesquisar();

        dataModel.setAtributos(atributos);
        return dataModel;
    }

    public void pesquisar() {

        if (dataFinal != null) {
            dataModel.getFiltros().add(new Filtro("dataRegistro", Filtro.MENOR_OU_IGUAL, dataFinal));
        }

        if (dataInicial != null) {
            dataModel.getFiltros().add(new Filtro("dataRegistro", Filtro.MAIOR_OU_IGUAL, dataInicial));
        }

        if (!StringUtils.isEmpty(usuario)) {
            dataModel.getFiltros().add(new Filtro("usuario.login", usuario));
        }

        if (!StringUtils.isEmpty(janela)) {
            dataModel.getFiltros().add(new Filtro("janelaController", Filtro.LIKE, janela));
        }

        if (!StringUtils.isEmpty(descricao)) {
            dataModel.getFiltros().add(new Filtro("acao", Filtro.LIKE, descricao.toLowerCase()));
        }

    }

    @Override
    protected Class<Auditoria> getClazz() {
        return Auditoria.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "AUDITORIA";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
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

    public String getJanela() {
        return janela;
    }

    public void setJanela(String janela) {
        this.janela = janela;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
