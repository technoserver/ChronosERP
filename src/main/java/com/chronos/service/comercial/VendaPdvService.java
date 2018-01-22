package com.chronos.service.comercial;

import com.chronos.dto.ProdutoVendaDTO;
import com.chronos.modelo.entidades.FinLancamentoReceber;
import com.chronos.modelo.entidades.PdvFormaPagamento;
import com.chronos.modelo.entidades.PdvMovimento;
import com.chronos.modelo.entidades.PdvVendaCabecalho;
import com.chronos.modelo.entidades.enuns.Modulo;
import com.chronos.repository.EstoqueRepository;
import com.chronos.repository.Repository;
import com.chronos.service.financeiro.FinLancamentoReceberService;
import com.chronos.service.financeiro.MovimentoService;
import com.chronos.util.Constantes;
import com.chronos.util.jpa.Transactional;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 19/01/18.
 */
public class VendaPdvService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private FinLancamentoReceberService finLancamentoReceberService;

    @Inject
    private Repository<PdvVendaCabecalho> repository;
    @Inject
    private EstoqueRepository estoqueRepositoy;
    @Inject
    private MovimentoService movimentoService;


    @Transactional
    public PdvVendaCabecalho finalizarVenda(PdvVendaCabecalho venda){
        try {
            venda.setStatusVenda("F");
            List<PdvFormaPagamento> pagamentos = venda.getListaFormaPagamento();
            venda = repository.atualizar(venda);
            List<ProdutoVendaDTO> produtos = new ArrayList<>();
            venda.getListaPdvVendaDetalhe().forEach(p->{
                produtos.add(new ProdutoVendaDTO(p.getProduto().getId(),p.getQuantidade()));
            });
            estoqueRepositoy.atualizaEstoqueVerificado(venda.getEmpresa().getId(), produtos);
            for (PdvFormaPagamento p:pagamentos) {
                if(p.getPdvTipoPagamento().getGeraParcelas().equals("S")){
                    finLancamentoReceberService.gerarLancamento(venda.getId(), p.getValor(),venda.getCliente(),p.getCondicao(), Modulo.VENDA.getCodigo(), Constantes.FIN.NATUREZA_VENDA, venda.getEmpresa());
                }
            }
            movimentoService.lancaVenda(venda.getValorTotal(),venda.getValorDesconto(),venda.getTroco());
            return venda;
        }catch (Exception ex){
            ex.printStackTrace();
            throw new RuntimeException("Erro ao finalizar a venda",ex);
        }


    }
}
