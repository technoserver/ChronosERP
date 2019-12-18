package com.chronos.erp.service.financeiro;

import com.chronos.erp.modelo.entidades.Cliente;
import com.chronos.erp.modelo.entidades.ContaPessoa;
import com.chronos.erp.modelo.entidades.MovimentoContaPessoa;
import com.chronos.erp.modelo.entidades.VendaDevolucao;
import com.chronos.erp.modelo.enuns.AcaoLog;
import com.chronos.erp.modelo.enuns.Modulo;
import com.chronos.erp.modelo.enuns.TipoLancamento;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.gerencial.AuditoriaService;
import com.chronos.erp.util.FormatValor;
import com.chronos.erp.util.jpa.Transactional;

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

    public void lancarMovimentoDevolucaoPdv(Cliente cliente, VendaDevolucao devolucao) {

        ContaPessoa contaPessoa = contaPessoaRepository.get(ContaPessoa.class, "pessoa.id", cliente.getPessoa().getId());

        if (contaPessoa == null) {
            contaPessoa = new ContaPessoa();
            contaPessoa.setSaldo(BigDecimal.ZERO);
            contaPessoa.setPessoa(cliente.getPessoa());

            contaPessoa = contaPessoaRepository.atualizar(contaPessoa);

        }

        lancaMovimento(contaPessoa, devolucao.getValorCredito(), TipoLancamento.CREDITO, Modulo.DEVOLUCAO_PDV.getCodigo(), devolucao.getId().toString());
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

    @Transactional
    public void excluirMovimento(ContaPessoa conta, MovimentoContaPessoa movimento) {
        movimentoRepository.excluir(movimento);
        atualizarSaldoConta(conta, TipoLancamento.DEBITO, movimento.getValor());
        String conteudo = "Exclusão de Crédito";
        conteudo += "na conta de " + conta.getPessoa().getNome() + " no valor de " + FormatValor.getInstance().formatarValor(movimento.getValor());
        auditoriaService.gerarLog(AcaoLog.DELETE, conteudo, "LANCAMENTO DE CREDITO CLIENTE");
    }

    private void atualizarSaldoConta(ContaPessoa conta, TipoLancamento tipo, BigDecimal valor) {
        valor = tipo == TipoLancamento.CREDITO ? valor : valor.negate();
        conta.setSaldo(conta.getSaldo().add(valor));
        contaPessoaRepository.atualizar(conta);
    }


}
