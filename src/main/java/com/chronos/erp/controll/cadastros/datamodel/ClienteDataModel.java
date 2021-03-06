package com.chronos.erp.controll.cadastros.datamodel;

import com.chronos.erp.controll.ERPLazyDataModel;
import com.chronos.erp.modelo.view.PessoaCliente;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by john on 12/07/17.
 */
public class ClienteDataModel extends ERPLazyDataModel<PessoaCliente> {

    private static final Logger logger = LoggerFactory.getLogger(ClienteDataModel.class);

    @Override
    public List<PessoaCliente> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        try {
            atributos = new Object[]{"nome", "cpfCnpj", "logradouro", "cidade", "uf", "fone", "tipo"};
            List<PessoaCliente> clietes = repository.getEntitys(getClazz(), filtros, first, pageSize, sortField, sortOrder, joinFetch, atributos);
            Long totalRegistros = repository.getTotalRegistros(getClazz(), filtros);
            this.setRowCount(totalRegistros.intValue());
            filtros.clear();
            return clietes;

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Erro ao carregar os clientes", ex.getMessage());
        }

        return new LinkedList<>();
    }
}
