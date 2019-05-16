package com.chronos.repository;

import com.chronos.dto.FatorGeradorDTO;

import java.io.Serializable;

/**
 * Created by john on 28/10/17.
 */
public class VendaRepository extends AbstractRepository implements Serializable {


    private static final long serialVersionUID = 3521212569157231099L;


    public FatorGeradorDTO getVendaToParcela(Integer id) {
        String psql = "SELECT new com.chronos.dto.FatorGeradorDTO(v.id,v.dataVenda,v.valorTotal,v.vendedor.colaborador.pessoa.nome,n.numero,n.dataHoraEmissao) FROM VendaCabecalho v " +
                "LEFT JOIN NfeCabecalho n on v.id = n.vendaCabecalho.id where v.id = ?1";
        FatorGeradorDTO entity = getEntity(FatorGeradorDTO.class, psql, 14).stream().findFirst().get();
        return entity;

    }

}
