package com.chronos.erp.repository;

import com.chronos.erp.dto.FatorGeradorDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.MonthDay;
import java.time.Year;
import java.util.Optional;

/**
 * Created by john on 28/10/17.
 */
public class VendaRepository extends AbstractRepository implements Serializable {


    private static final long serialVersionUID = 3521212569157231099L;


    public FatorGeradorDTO getVendaToParcela(Integer id) {
        String psql = "SELECT new com.chronos.erp.dto.FatorGeradorDTO(v.id, v.dataVenda, v.valorTotal, v.vendedor.colaborador.pessoa.nome, n.numero, n.dataHoraEmissao) FROM VendaCabecalho v " +
                "LEFT JOIN NfeCabecalho n on v.id = n.vendaCabecalho.id where v.id = ?1";
        FatorGeradorDTO entity = getEntity(FatorGeradorDTO.class, psql, 14).stream().findFirst().get();
        return entity;

    }


    public BigDecimal valorTotalNoAno(Integer idmperesa) {
        Optional<BigDecimal> optional = Optional.ofNullable(
                em.createQuery("select sum(valorTotal) from PdvVendaCabecalho v where v.empresa.id =:empresa and year (v.dataHoraVenda) = :ano and (statusVenda = 'E' or  statusVenda = 'F')", BigDecimal.class)
                        .setParameter("ano", Year.now().getValue())
                        .setParameter("empresa", idmperesa)
                        .getSingleResult());
        return optional.orElse(BigDecimal.ZERO);
    }


    public BigDecimal valorTotalNoMes(Integer idmperesa) {
        Optional<BigDecimal> optional = Optional.ofNullable(
                em.createQuery("select sum(valorTotal) from PdvVendaCabecalho v where v.empresa.id =:empresa and month(v.dataHoraVenda) = :mes and (statusVenda = 'E' or  statusVenda = 'F')", BigDecimal.class)
                        .setParameter("mes", MonthDay.now().getMonthValue())
                        .setParameter("empresa", idmperesa)
                        .getSingleResult());
        return optional.orElse(BigDecimal.ZERO);
    }


    public BigDecimal valorVendaMedioNoAno(Integer idmperesa) {
        Optional<BigDecimal> optional = Optional.ofNullable(
                em.createQuery("select sum(valorTotal)/count(*) from PdvVendaCabecalho v where v.empresa.id =:empresa and year(v.dataHoraVenda) = :ano and (statusVenda = 'E' or  statusVenda = 'F')", BigDecimal.class)
                        .setParameter("ano", Year.now().getValue())
                        .setParameter("empresa", idmperesa)
                        .getSingleResult());
        return optional.orElse(BigDecimal.ZERO);
    }


}
