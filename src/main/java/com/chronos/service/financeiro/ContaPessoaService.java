package com.chronos.service.financeiro;

import com.chronos.modelo.entidades.ContaPessoa;
import com.chronos.modelo.entidades.MovimentoContaPessoa;
import com.chronos.modelo.enuns.AcaoLog;
import com.chronos.modelo.enuns.TipoLancamento;
import com.chronos.repository.Repository;
import com.chronos.service.gerencial.AuditoriaService;
import com.chronos.util.FormatValor;
import com.chronos.util.jpa.Transactional;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by john on 01/02/18.
 */
public class ContaPessoaService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<ContaPessoa> contaPessoaRepository;

    @Inject
    private Repository<MovimentoContaPessoa> movimentoRepository;
    @Inject
    private AuditoriaService auditoriaService;


    @Transactional
    public ContaPessoa salvar(ContaPessoa conta) {
        conta = contaPessoaRepository.atualizar(conta);
        return conta;
    }

    @Transactional
    public void lancaMovimento(ContaPessoa conta, BigDecimal valor, TipoLancamento tipoLancamento, String codigo, String numDocumento) {
        MovimentoContaPessoa movimento = new MovimentoContaPessoa();
        movimento.setCodigoModulo(codigo);
        movimento.setNumeroDocumento(numDocumento);
        movimento.setDataMovimento(new Date());
        movimento.setValor(valor);
        movimento.setTipoMovimento(tipoLancamento.getCodigo());
        movimento.setContaPessoa(conta);
        movimentoRepository.salvar(movimento);
        atualizarSaldoConta(conta, tipoLancamento, valor);
        String conteudo = "Lançamento de ";
        conteudo += tipoLancamento == TipoLancamento.DEBITO ? "Debito " : "Crédito ";
        conteudo += "na conta de " + conta.getPessoa().getNome() + " no valor de " + FormatValor.getInstance().formatarValor(valor);
        auditoriaService.gerarLog(AcaoLog.INSERT, conteudo, "LANCAMENTO DE CREDITO CLIENTE");
    }

    private void atualizarSaldoConta(ContaPessoa conta, TipoLancamento tipo, BigDecimal valor) {
        valor = tipo == TipoLancamento.CREDITO ? valor : valor.negate();
        conta.setSaldo(conta.getSaldo().add(valor));
        contaPessoaRepository.atualizar(conta);
    }


}