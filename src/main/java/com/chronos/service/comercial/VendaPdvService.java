package com.chronos.service.comercial;

import com.chronos.dto.ProdutoVendaDTO;
import com.chronos.modelo.entidades.FinLancamentoReceber;
import com.chronos.modelo.entidades.PdvFormaPagamento;
import com.chronos.modelo.entidades.PdvVendaCabecalho;
import com.chronos.modelo.entidades.enuns.Modulo;
import com.chronos.repository.EstoqueRepository;
import com.chronos.repository.Repository;
import com.chronos.service.financeiro.FinLancamentoReceberService;
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


    @Transactional
    public void finalizarVenda(PdvVendaCabecalho venda){
        try {
            venda.setStatusVenda("F");
            venda = repository.atualizar(venda);
            List<ProdutoVendaDTO> produtos = new ArrayList<>();
            venda.getListaPdvVendaDetalhe().forEach(p->{
                produtos.add(new ProdutoVendaDTO(p.getId(),p.getQuantidade()));
            });
            estoqueRepositoy.atualizaEstoqueVerificado(venda.getEmpresa().getId(), produtos);
            for (PdvFormaPagamento p:venda.getListaFormaPagamento()) {
                if(p.getPdvTipoPagamento().getGeraParcelas().equals("S")){
                    finLancamentoReceberService.gerarLancamento(venda.getId(), p.getValor(),venda.getCliente(),p.getCondicao(), Modulo.VENDA.getCodigo(), Constantes.FIN.NATUREZA_VENDA, venda.getEmpresa());
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }


    }
}
