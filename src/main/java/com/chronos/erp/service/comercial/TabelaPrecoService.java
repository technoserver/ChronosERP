package com.chronos.erp.service.comercial;

import com.chronos.erp.modelo.entidades.TabelaPreco;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;

import javax.inject.Inject;
import java.io.Serializable;

public class TabelaPrecoService implements Serializable {


    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<TabelaPreco> repository;

    public TabelaPreco salvar(TabelaPreco tabelaPreco) throws ChronosException {

        if (tabelaPreco.getListaProduto().isEmpty()) {
            throw new ChronosException("è preciso definir produtos para tabela de preço");
        }


        if (tabelaPreco.getPrincipal().equals("S")) {
            TabelaPreco tabela = repository.get(TabelaPreco.class, "principal", "S");

            if (tabela != null && !tabela.equals(tabelaPreco)) {
                throw new ChronosException("Já foi definido uma tabela de preço principal");
            }
        }

        return repository.atualizar(tabelaPreco);
    }
}
