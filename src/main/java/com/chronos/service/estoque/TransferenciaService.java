package com.chronos.service.estoque;

import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.EstoqueTransferenciaCabecalho;
import com.chronos.modelo.entidades.EstoqueTransferenciaDetalhe;
import com.chronos.modelo.entidades.TributOperacaoFiscal;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.service.cadastros.EmpresaService;
import com.chronos.service.cadastros.ProdutoService;
import com.chronos.util.jpa.Transactional;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransferenciaService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EmpresaService empresaService;
    @Inject
    private Repository<EstoqueTransferenciaCabecalho> repository;
    @Inject
    private EntradaNotaFiscalService entradaService;


    @Inject
    private ProdutoService produtoService;


    public List<Empresa> popularFiliais(int idmatriz, int idempresa, String tipo) throws ChronosException {
        List<Empresa> empresas = empresaService.getFiliais(idmatriz, idempresa, tipo);
        if (empresas.isEmpty()) {
            throw new ChronosException("Não existe filiais");
        }
        return empresas;
    }

    public List<TributOperacaoFiscal> getListOperacaoFiscal() throws ChronosException {
        List<TributOperacaoFiscal> operacoes = new ArrayList<>();

        if (operacoes.isEmpty()) {
            throw new ChronosException("Não existe Operações Fiscais");
        }

        return operacoes;
    }

    @Transactional
    public void salvar(EstoqueTransferenciaCabecalho objeto) throws Exception {
        validar(objeto);
        BigDecimal total = objeto.getListEstoqueTransferenciaDetalhe()
                .stream()
                .map(EstoqueTransferenciaDetalhe::getValorTotal)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        objeto.setValorTotal(total);
        if (objeto.getTributOperacaoFiscal().getObrigacaoFiscal()) {

        } else {
            produtoService.transferenciaEstoque(objeto, objeto.getListEstoqueTransferenciaDetalhe());

            objeto = repository.atualizar(objeto);
            entradaService.gerarEntrada(objeto);
        }

    }


    private void validar(EstoqueTransferenciaCabecalho obj) throws ChronosException {

        if (obj.getTributOperacaoFiscal() == null) {
            throw new ChronosException("Operação fiscal não informada");
        } else if (obj.getTributOperacaoFiscal().getObrigacaoFiscal() && obj.getTributGrupoTributario() == null) {
            throw new ChronosException("Grupo tributário não informado");
        } else if (obj.getListEstoqueTransferenciaDetalhe().isEmpty()) {
            throw new ChronosException("Itens não informado");
        }
    }



}
