package com.chronos.service.financeiro;

import com.chronos.dto.FatorGeradorDTO;
import com.chronos.modelo.entidades.VendaCabecalho;
import com.chronos.repository.Repository;

import javax.inject.Inject;
import java.io.Serializable;

public class FinRecebimentoService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<VendaCabecalho> repository;

    public FatorGeradorDTO buscarFator(Integer idvenda) {

        VendaCabecalho venda = repository.get(idvenda, VendaCabecalho.class);


        FatorGeradorDTO fator = new FatorGeradorDTO();


        return fator;
    }


}
