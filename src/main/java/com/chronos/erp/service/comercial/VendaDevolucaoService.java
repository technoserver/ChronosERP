package com.chronos.erp.service.comercial;

import com.chronos.erp.modelo.entidades.VendaDevolucao;
import com.chronos.erp.modelo.entidades.VendaDevolucaoItem;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.service.financeiro.ContaPessoaService;
import com.chronos.erp.util.jpa.Transactional;

import javax.inject.Inject;
import java.io.Serializable;

public class VendaDevolucaoService implements Serializable {

    private static final long serialVersionUID = 1L;


    @Inject
    private Repository<VendaDevolucao> repository;
    @Inject
    private ContaPessoaService contaPessoaService;

    @Transactional
    public VendaDevolucao gerarDevolucao(VendaDevolucao devolucao) throws ChronosException {


        if (devolucao.getListaVendaDevolucaoItem() == null || devolucao.getListaVendaDevolucaoItem().isEmpty()) {
            throw new ChronosException("Não foram informados item para devolução");
        }

        for (VendaDevolucaoItem item : devolucao.getListaVendaDevolucaoItem()) {
            if (item.getQuantidade().compareTo(item.getQuantidadeVenda()) > 0) {
                throw new ChronosException("Quantidade devolvida maior que a quantidade vendida");
            }
        }

        devolucao.setCreditoUtilizado(devolucao.getGeradoCredito());

        devolucao.calcularValorCredito();


        String totalParcial = devolucao.getValorVenda().compareTo(devolucao.getValorCredito()) != 0 ? "P" : "T";

        devolucao.setTotalParcial(totalParcial);

        devolucao = repository.atualizar(devolucao);
        return devolucao;
    }
}
