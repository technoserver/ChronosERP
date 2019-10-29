package com.chronos.erp.controll.configuracao;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.controll.ERPLazyDataModel;
import com.chronos.erp.modelo.entidades.NotaFiscalModelo;
import com.chronos.erp.modelo.entidades.NotaFiscalTipo;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 26/09/17.
 */
@Named
@ViewScoped
public class NotaFiscalTipoControll extends AbstractControll<NotaFiscalTipo> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<NotaFiscalModelo> modelos;


    @Override
    public ERPLazyDataModel<NotaFiscalTipo> getDataModel() {

        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(NotaFiscalTipo.class);
            dataModel.setDao(dao);
        }

        dataModel.getFiltros().clear();
        dataModel.getFiltros().add(new Filtro("empresa.id", empresa.getId()));
        return dataModel;
    }

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setEmpresa(empresa);
    }

    @Override
    public void salvar() {
        getObjeto().setSerie(String.format("%3s", getObjeto().getSerie() != null ? getObjeto().getSerie().trim() : "1").replace(' ', '0'));
        getObjeto().setSerieScan(String.format("%3s", getObjeto().getSerieScan() != null ? getObjeto().getSerieScan().trim() : "1").replace(' ', '0'));
        super.salvar();
    }

    public List<NotaFiscalModelo> getListaNotaFiscalModelo(String nome) {
        List<NotaFiscalModelo> listaNotaFiscalModelo = new ArrayList<>();
        try {
            listaNotaFiscalModelo = modelos.getEntitys(NotaFiscalModelo.class, "descricao", nome, new Object[]{"descricao"});
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaNotaFiscalModelo;
    }

    @Override
    protected Class<NotaFiscalTipo> getClazz() {
        return NotaFiscalTipo.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "NOTA_FISCAL_TIPO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }


}
