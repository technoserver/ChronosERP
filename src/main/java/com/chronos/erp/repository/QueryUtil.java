package com.chronos.erp.repository;

import org.primefaces.model.SortOrder;

/**
 * Created by john on 29/09/17.
 */
public class QueryUtil {

    public String definirOrdenacao(String jpql, String sortField, SortOrder sortOrder) {
        if (sortField != null && sortOrder != null) {
            if (sortOrder.equals(SortOrder.ASCENDING)) {
                jpql += " ORDER BY o." + sortField + " ASC";
            } else if (sortOrder.equals(SortOrder.DESCENDING)) {
                jpql += " ORDER BY o." + sortField + " DESC";
            }
        }
        return jpql;
    }
}
