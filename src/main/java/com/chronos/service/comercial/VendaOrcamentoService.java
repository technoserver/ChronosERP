package com.chronos.service.comercial;

import com.chronos.modelo.entidades.VendaCabecalho;
import com.chronos.modelo.entidades.VendaOrcamentoCabecalho;
import com.chronos.modelo.enuns.SituacaoOrcamentoPedido;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.util.jpa.Transactional;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.io.Serializable;

public class VendaOrcamentoService implements Serializable {


    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<VendaOrcamentoCabecalho> repository;
    @Inject
    private Repository<VendaCabecalho> vendaRepository;

    @Inject
    private VendaService vendaService;


    @Transactional
    public VendaOrcamentoCabecalho salvar(VendaOrcamentoCabecalho orcamento) throws ChronosException {
        String situacao = orcamento.getSituacao();

        if (orcamento.getListaVendaOrcamentoDetalhe().isEmpty()) {
            throw new ChronosException("Itens não informado");
        }

        if (!situacao.equals(SituacaoOrcamentoPedido.PENDENTE.getCodigo())) {
            String mensagem = "Este orçamento não pode ser alterado.\n";
            if (situacao.equals(SituacaoOrcamentoPedido.CANCELADO.getCodigo())) {
                mensagem += "Situação: CANCELADO";
            }
            if (situacao.equals(SituacaoOrcamentoPedido.APROVADO.getCodigo())) {
                mensagem += "Situação: APROVADO";
            }
            throw new ChronosException(mensagem);
        }
        Integer id = orcamento.getId();
        orcamento = repository.atualizar(orcamento);

        if (id == null) {
            String codigo = orcamento.getTipo().equals("P") ? "#PE" : "#OE";
            codigo += StringUtils.leftPad(orcamento.getId().toString(), 3, "0");
            orcamento.setCodigo(codigo);
            orcamento = repository.atualizar(orcamento);
        }


        return orcamento;
    }

    @Transactional
    public VendaOrcamentoCabecalho conveterEmVenda(VendaOrcamentoCabecalho orcamento) {

        VendaCabecalho venda = vendaService.gerarVenaDoOrcamento(orcamento);
        venda = vendaRepository.atualizar(venda);
        venda = vendaService.faturarVenda(venda);
        orcamento = venda.getVendaOrcamentoCabecalho();
        orcamento.setSituacao(SituacaoOrcamentoPedido.APROVADO.getCodigo());
        orcamento = repository.saveAndFlush(orcamento);

        return orcamento;
    }

    @Transactional
    public void excluir(VendaOrcamentoCabecalho orcamento) {

    }
}
