package com.chronos.service.estoque;

import com.chronos.bo.nfe.TransferenciaToNfe;
import com.chronos.dto.ConfiguracaoEmissorDTO;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.StatusTransmissao;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.service.cadastros.EmpresaProdutoService;
import com.chronos.service.cadastros.EmpresaService;
import com.chronos.service.cadastros.ProdutoService;
import com.chronos.service.comercial.NfeService;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.Mensagem;

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
    private Repository<Empresa> empresaRepository;
    @Inject
    private EntradaNotaFiscalService entradaService;
    @Inject
    private NfeService nfeService;
    @Inject
    private EmpresaProdutoService empresaProdutoService;


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
        verificarProdutoNaoCadastrado(objeto);
        if (objeto.getTributOperacaoFiscal().getObrigacaoFiscal()) {
            Empresa empresaDestino = empresaRepository.get(objeto.getEmpresaDestino().getId(), Empresa.class);
            objeto.setEmpresaDestino(empresaDestino);


            TransferenciaToNfe transferenciaToNfe = new TransferenciaToNfe(objeto);

            NfeCabecalho nfe = transferenciaToNfe.gerarNFe();
            nfe.setTransferencia(objeto);
            ConfiguracaoEmissorDTO configuracaoEmissorDTO = nfeService.instanciarConfNfe(nfe.getEmpresa(), nfe.getModeloDocumento(), true);
            nfe.setAmbiente(configuracaoEmissorDTO.getWebserviceAmbiente());
            StatusTransmissao status = nfeService.transmitirNFe(nfe, true);

            if (status == StatusTransmissao.AUTORIZADA) {

                objeto = nfe.getTransferencia();
                objeto.setStatus('F');
                repository.atualizar(objeto);

                Mensagem.addInfoMessage("NFe transmitida com sucesso");
            }

        } else {
            objeto.setStatus('E');
            produtoService.transferenciaEstoque(objeto, objeto.getListEstoqueTransferenciaDetalhe());

            objeto = repository.atualizar(objeto);
            entradaService.gerarEntrada(objeto);
        }

    }

    private void verificarProdutoNaoCadastrado(EstoqueTransferenciaCabecalho objeto) {

        for (EstoqueTransferenciaDetalhe item : objeto.getListEstoqueTransferenciaDetalhe()) {
            empresaProdutoService.novoProdutoQuandoNaoExiste(objeto.getEmpresaDestino(), item.getProduto());
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
