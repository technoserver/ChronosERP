package com.chronos.erp.service.comercial;

import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.modelo.enuns.Modulo;
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
    @Inject
    private Repository<PdvVendaCabecalho> pdvVendaCabecalhoRepository;

    @Inject
    private Repository<VendaCabecalho> vendaCabecalhoRepository;

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

    @Transactional
    public void gerarCredito(int id) throws ChronosException {
        VendaDevolucao devolucao = repository.get(id, VendaDevolucao.class);
        if (devolucao == null) {
            throw new ChronosException("Devolução não encontrada");
        }
        Cliente cliente;
        if (devolucao.getCodigoModulo().equals(Modulo.PDV.getCodigo())) {
            PdvVendaCabecalho venda = pdvVendaCabecalhoRepository.get(devolucao.getIdVenda(), PdvVendaCabecalho.class);

            if (venda == null) {
                throw new ChronosException("Venda para está devolução não encontrada");
            }

            if (venda.getCliente() == null) {
                throw new ChronosException("Cliente não vinculado a venda");
            }

            cliente = venda.getCliente();

        } else if (devolucao.getCodigoModulo().equals(Modulo.VENDA.getCodigo())) {
            VendaCabecalho venda = vendaCabecalhoRepository.get(devolucao.getIdVenda(), VendaCabecalho.class);

            if (venda == null) {
                throw new ChronosException("Venda para está devolução não encontrada");
            }

            if (venda.getCliente() == null) {
                throw new ChronosException("Cliente não vinculado a venda");
            }

            cliente = venda.getCliente();
        } else {
            throw new ChronosException("Venda para está devolução não encontrada");
        }


        contaPessoaService.lancarMovimentoDevolucao(cliente, devolucao);

        devolucao.setCreditoUtilizado("S");
        devolucao.setGeradoCredito("S");
        repository.salvar(devolucao);
    }
}
