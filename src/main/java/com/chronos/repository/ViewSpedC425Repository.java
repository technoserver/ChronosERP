package com.chronos.repository;

import com.chronos.modelo.entidades.view.ViewSpedC425Id;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Created by john on 28/09/17.
 */
public class ViewSpedC425Repository extends AbstractRepository {

//    public List<ViewSpedC425Id> getEntitys(Date dataInicio, Date dataFim, String totalizadorParcial1, String totalizadorParcial2) throws Exception {
//        try {
//            abrirConexao();
//            Query q = em.createQuery("SELECT o FROM ViewSpedC425Id o WHERE o.dataEmissao BETWEEN :dataInicial AND :dataFinal AND o.totalizadorParcial NOT LIKE :totalizadorParcial1 AND o.totalizadorParcial NOT LIKE :totalizadorParcial2");
//            q.setParameter("dataInicial", dataInicio);
//            q.setParameter("dataFinal", dataFim);
//            q.setParameter("totalizadorParcial1", totalizadorParcial1);
//            q.setParameter("totalizadorParcial2", totalizadorParcial2);
//            return q.getResultList();
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            if (isAutoCommit()) {
//                fecharConexao();
//            }
//        }
//    }
}
