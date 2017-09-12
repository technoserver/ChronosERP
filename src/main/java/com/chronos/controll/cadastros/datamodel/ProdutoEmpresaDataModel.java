package com.chronos.controll.cadastros.datamodel;

import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.view.ViewProdutoEmpresa;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by john on 12/09/17.
 */
public class ProdutoEmpresaDataModel extends ERPLazyDataModel<ViewProdutoEmpresa> {

    private static final Logger logger = LoggerFactory.getLogger(ViewProdutoEmpresa.class);

    @Override
    public List<ViewProdutoEmpresa> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

        try {

            List<ViewProdutoEmpresa> produtos = repository.getEntitys(getClazz(), filtros, first, pageSize, sortField, sortOrder, joinFetch, atributos);
            Long totalRegistros = repository.getTotalRegistros(getClazz(), filtros);
            this.setRowCount(totalRegistros.intValue());
            filtros.clear();
            return produtos;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Erro ao carregar os clientes", ex.getMessage());
        }

        return new ArrayList<>();
    }
}
