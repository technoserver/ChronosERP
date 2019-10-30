package com.chronos.erp.repository;

import com.chronos.erp.dto.FornecedorDTO;
import org.primefaces.model.SortOrder;

import java.util.List;

public class FornecedorRepository extends AbstractRepository {


    public List<FornecedorDTO> getFonecedores(List<Filtro> filters, int first, int pageSize, String sortField, SortOrder sortOrder) {


        String jpql = "Select new com.chronos.dto() from Fornecedor f " +
                "join f.pesspa p " +
                "join ";

        return null;
    }
}
