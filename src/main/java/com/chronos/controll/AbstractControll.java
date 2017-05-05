/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll;

import com.chronos.repository.Repository;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.jsf.Mensagem;
import java.io.Serializable;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;
import org.springframework.util.StringUtils;

/**
 *
 * @author john
 */


public abstract class AbstractControll<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private T objetoSelecionado;
    private T objeto;
    private ERPLazyDataModel<T> dataModel;
    private boolean telaGrid = true;
    @Inject
    protected Repository<T> dao;
    private String titulo;
    private int activeTabIndex;

    protected abstract Class<T> getClazz();

    protected abstract String getFuncaoBase();

    protected abstract String getPagina();
    
    @PostConstruct
    public void init() {
        dataModel = new ERPLazyDataModel<>();
        dataModel.setClazz(getClazz());
        dataModel.setDao(dao);
       
    }

    public String doView(){
        return getPagina();
    }
    
    public void voltar() {
        titulo = "Consultar";
        telaGrid = true;
    }

    public String keyFromValue(HashMap map, Object value) {
        for (Object o : map.keySet()) {
            if (map.get(o).equals(value)) {
                return String.valueOf(o);
            }
        }
        return null;
    }

    public void onTabChange(final TabChangeEvent event) {
        TabView tv = (TabView) event.getComponent();
        this.activeTabIndex = tv.getActiveIndex();
    }

    public String doIndex() {
        return "/privado/index?faces-redirect=true";
    }

    public void doCreate() {
        try {
            objeto = (T) getClazz().newInstance();
            telaGrid = true;
            titulo = "Cadastrar";
            activeTabIndex = 0;
        } catch (InstantiationException | IllegalAccessException e) {
            Mensagem.addErrorMessage("Ocorreu um erro ao iniciar a inclusão do registro!", e);
        }

    }

    public void doEdit() {
        objeto = objetoSelecionado;
        titulo = "Alteração";
        telaGrid = false;
        activeTabIndex = 0;
    }

    public void salvar() {
        salvar(null);
    }

    public void salvar(String mensagem) {
        try {
            objeto = dao.atualizar(objeto);
            Mensagem.addInfoMessage( mensagem != null ? mensagem : "Registro salvo com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao salvar o registro!", e);
        }
    }

    public boolean podeConsultar() {
        // return false;
        boolean teste = FacesUtil.isUserInRole(getFuncaoBase() + "_CONSULTA")
                || FacesUtil.isUserInRole("ADMIN");
        return teste;
    }

    public boolean podeInserir() {
        return FacesUtil.isUserInRole(getFuncaoBase() + "_INSERE") || FacesUtil.isUserInRole("ADMIN");
    }

    public boolean podeAlterar() {
        return FacesUtil.isUserInRole(getFuncaoBase() + "_ALTERA") || FacesUtil.isUserInRole("ADMIN");
    }

    public boolean podeExcluir() {
        return FacesUtil.isUserInRole(getFuncaoBase() + "_EXCLUI") || FacesUtil.isUserInRole("ADMIN");
    }

    public T getObjetoSelecionado() {
        return objetoSelecionado;
    }

    public void setObjetoSelecionado(T objetoSelecionado) {
        this.objetoSelecionado = objetoSelecionado;
    }

    public T getObjeto() {
        return objeto;
    }

    public void setObjeto(T objeto) {
        this.objeto = objeto;
    }

    public ERPLazyDataModel<T> getDataModel() {
        return dataModel;
    }

    public void setDataModel(ERPLazyDataModel<T> dataModel) {
        this.dataModel = dataModel;
    }

    public boolean isTelaGrid() {
        return telaGrid;
    }

    public void setTelaGrid(boolean telaGrid) {
        this.telaGrid = telaGrid;
    }

    public Repository<T> getDao() {
        return dao;
    }

    public void setDao(Repository<T> dao) {
        this.dao = dao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = StringUtils.isEmpty(titulo) ? "Consultar" : titulo;
    }

    public int getActiveTabIndex() {
        return activeTabIndex;
    }

    public void setActiveTabIndex(int activeTabIndex) {
        this.activeTabIndex = activeTabIndex;
    }

}
