package com.chronos.erp.controll.financeiro;

import com.chronos.erp.controll.ERPLazyDataModel;
import com.chronos.erp.modelo.view.ViewLancamentoReceberResum;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by john on 15/08/17.
 */
public class FinLancamentoReceberDataModel extends ERPLazyDataModel<ViewLancamentoReceberResum> {

    private static final Logger logger = LoggerFactory.getLogger(FinLancamentoReceberDataModel.class);

    @Override
    public List<ViewLancamentoReceberResum> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        try {

            List<ViewLancamentoReceberResum> recebimentos = repository.getEntitys(getClazz(), filtros, first, pageSize, sortField, sortOrder, joinFetch, atributos);
            Long totalRegistros = repository.getTotalRegistros(getClazz(), filtros);
            this.setRowCount(totalRegistros.intValue());
            filtros.clear();
            return recebimentos;

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Erro ao carregar os recebimentos", ex.getMessage());
        }

        return new LinkedList<>();
    }
}
