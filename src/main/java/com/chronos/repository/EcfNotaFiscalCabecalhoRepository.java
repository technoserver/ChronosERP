package com.chronos.repository;

import com.chronos.modelo.entidades.EcfNotaFiscalCabecalho;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Created by john on 28/09/17.
 */
public class EcfNotaFiscalCabecalhoRepository extends AbstractRepository {


//    public List<EcfNotaFiscalCabecalho> getEntity(Date dataInicio, Date dataFim, String serie, String subserie) throws Exception {
//        try {
//            abrirConexao();
//            Query q = em.createQuery("SELECT o FROM EcfNotaFiscalCabecalho o WHERE o.dataEmissao BETWEEN :dataInicial AND :dataFinal AND o.serie = :serie AND o.subserie = :subserie ORDER BY o.numero");
//            q.setParameter("dataInicial", dataInicio);
//            q.setParameter("dataFinal", dataFim);
//            q.setParameter("serie", serie);
//            q.setParameter("subserie", subserie);
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
