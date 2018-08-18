package com.chronos.service.financeiro;

import com.chronos.modelo.entidades.FinParcelaReceberCartao;
import com.chronos.modelo.entidades.FinParcelaRecebimentoCartao;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.util.jpa.Transactional;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FinRecebimentoCartaoService implements Serializable {


    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<FinParcelaReceberCartao> repository;
    @Inject
    private Repository<FinParcelaRecebimentoCartao> repositoryRecebimento;


    @Transactional
    public void receber(List<FinParcelaReceberCartao> parcelasSelecionada) throws ChronosException {
        if (parcelasSelecionada == null || parcelasSelecionada.isEmpty()) {
            throw new ChronosException("è preciso selecionar ao menos 1 parcela");
        }
        List<FinParcelaRecebimentoCartao> recebimentos = new ArrayList<>();

        for (FinParcelaReceberCartao p : parcelasSelecionada) {
            p.setPago(true);
            p.setDataRecebimento(new Date());
            FinParcelaRecebimentoCartao recebimento = new FinParcelaRecebimentoCartao();
            recebimento.setDataLancamento(new Date());
            recebimento.setDataPrevisao(p.getDataVencimento());
            recebimento.setFinParcelaReceberCartao(p);
            recebimento.setDataRecebimento(new Date());
            recebimento.setValorBruto(p.getValorBruto());
            recebimento.setValorEncargos(p.getValorEncargos());
            recebimento.setValorLiquido(p.getValorLiquido());
            recebimento.setContaCaixa(p.getContaCaixa());
            repository.atualizarNamedQuery("FinParcelaReceberCartao.UpdatePagamento", new Date(), p.getId());
            recebimentos.add(recebimento);
        }

        repositoryRecebimento.salvar(recebimentos);
    }


}
