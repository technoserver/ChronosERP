package com.chronos.service.estoque;

import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.AcaoLog;
import com.chronos.repository.EstoqueRepository;
import com.chronos.repository.Repository;
import com.chronos.service.cadastros.FornecedorService;
import com.chronos.service.comercial.SyncPendentesService;
import com.chronos.service.financeiro.FinLancamentoPagarService;
import com.chronos.service.gerencial.AuditoriaService;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.FacesUtil;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Created by john on 02/08/17.
 */
public class EntradaNotaFiscalService implements Serializable {


    private static final long serialVersionUID = 1L;
    private Empresa empresa;


    @Inject
    private Repository<NfeCabecalho> repository;
    @Inject
    private EstoqueRepository estoqueRepository;
    @Inject
    private AuditoriaService auditoriaService;
    @Inject
    private FinLancamentoPagarService lancamentoPagarService;
    @Inject
    private Repository<Empresa> empresaRepository;

    @Inject
    private SyncPendentesService syncPendentesService;

    @Inject
    private FornecedorService fornecedorService;


    @Transactional
    public void salvar(NfeCabecalho nfe, ContaCaixa contaCaixa, NaturezaFinanceira naturezaFinanceira) throws Exception {


        boolean inclusao = false;

        Integer idempresa = nfe.getEmpresa().getId();
        AdmParametro parametro = FacesUtil.getParamentos();
        nfe.setNaturezaOperacao(nfe.getTributOperacaoFiscal().getDescricaoNaNf());
        if (nfe.getId() == null) {
            inclusao = true;
            for (NfeDetalhe detalhe : nfe.getListaNfeDetalhe()) {
                atualizarEstoque(nfe.getEmpresa(), nfe.getTributOperacaoFiscal(), detalhe);
                if (parametro != null && parametro.getFrenteCaixa()) {
                    syncPendentesService.gerarSyncPendetensEstoque(0, idempresa, detalhe.getProduto().getId());
                }
            }


        } else {
            List<NfeDetalhe> listaNfeDetOld = estoqueRepository.getItens(nfe);
            for (NfeDetalhe detalhe : listaNfeDetOld) {
                atualizarEstoque(nfe.getEmpresa(), nfe.getTributOperacaoFiscal(), detalhe);
                if (parametro != null && parametro.getFrenteCaixa()) {
                    syncPendentesService.gerarSyncPendetensEstoque(0, idempresa, detalhe.getProduto().getId());
                }
            }
            if (nfe.getTributOperacaoFiscal().getEstoqueVerificado() && nfe.getTributOperacaoFiscal().getEstoque()) {
                estoqueRepository.atualizaEstoqueEmpresaControleFiscal(empresa.getId(), nfe.getListaNfeDetalhe());
            } else if (nfe.getTributOperacaoFiscal().getEstoqueVerificado()) {
                estoqueRepository.atualizaEstoqueEmpresaControle(empresa.getId(), nfe.getListaNfeDetalhe());
            } else {
                estoqueRepository.atualizaEstoqueEmpresa(empresa.getId(), nfe.getListaNfeDetalhe());
            }
        }
        String descricao = "Entrada da NFe :" + nfe.getNumero() + " Fornecedor :" + nfe.getEmitente().getNome();
        nfe = repository.atualizar(nfe);
        if (!nfe.getListaDuplicata().isEmpty()) {
            lancamentoPagarService.gerarLancamento(nfe, contaCaixa, naturezaFinanceira);
        }
        if (inclusao) {
            auditoriaService.gerarLog(AcaoLog.INSERT, descricao, "Entrada de NF");
        }
    }

    @Transactional
    public void gerarEntrada(EstoqueTransferenciaCabecalho objeto) throws Exception {
        NfeCabecalho nfe = new NfeCabecalho();
        iniciar(objeto.getEmpresaOrigem());

        nfe.setDataHoraEntradaSaida(new Date());
        nfe.setNumero(StringUtils.leftPad(objeto.getId().toString(), 9, "0"));
        nfe.setValorTotal(objeto.getValorTotal());
        nfe.setValorTotalProdutos(nfe.getValorTotal());

        nfe.setTributOperacaoFiscal(objeto.getTributOperacaoFiscal());
        nfe.setNaturezaOperacao(objeto.getTributOperacaoFiscal().getDescricaoNaNf());

        definirDestinatario(nfe, objeto);
        definirEmitente(nfe, objeto);

        if (!fornecedorService.existeFornecedorByCpfCnj(nfe.getEmitente().getCpfCnpj())) {
            Fornecedor fornecedor = fornecedorService.cadastrarFornecedor(nfe.getEmitente(), objeto.getEmpresaOrigem());
            nfe.setFornecedor(fornecedor);
        }

        int i = 0;
        for (EstoqueTransferenciaDetalhe item : objeto.getListEstoqueTransferenciaDetalhe()) {
            NfeDetalhe itemNfe = new NfeDetalhe();
            itemNfe.setNfeCabecalho(nfe);
            itemNfe.setNomeProduto(item.getProduto().getNome());
            itemNfe.setValorTotalTributos(BigDecimal.ZERO);
            itemNfe.setValorTotal(item.getValorTotal());
            itemNfe.setCfop(0);
            itemNfe.setValorUnitarioComercial(item.getValorCusto());
            itemNfe.setQuantidadeComercial(item.getQuantidade());
            itemNfe.setCodigoProduto(item.getProduto().getId().toString());
            itemNfe.setNumeroItem(i++);
            itemNfe.setProduto(item.getProduto());
            nfe.getListaNfeDetalhe().add(itemNfe);
        }


        salvar(nfe, null, null);
    }

    public NfeCabecalho iniciar(Empresa empresa){
        this.empresa = empresa;
        NfeCabecalho nfe = new NfeCabecalho();
        nfe.setEmpresa(empresa);
        nfe.setEmitente(new NfeEmitente());
        nfe.getEmitente().setNfeCabecalho(nfe);
        nfe.setLocalEntrega(new NfeLocalEntrega());
        nfe.getLocalEntrega().setNfeCabecalho(nfe);
        nfe.setLocalRetirada(new NfeLocalRetirada());
        nfe.getLocalRetirada().setNfeCabecalho(nfe);
        nfe.setTransporte(new NfeTransporte());
        nfe.getTransporte().setNfeCabecalho(nfe);
        nfe.setFatura(new NfeFatura());
        nfe.getFatura().setNfeCabecalho(nfe);

        nfe.setListaNfeReferenciada(new HashSet<>());
        nfe.setListaNfReferenciada(new HashSet<>());
        nfe.setListaCteReferenciado(new HashSet<>());
        nfe.setListaProdRuralReferenciada(new HashSet<>());
        nfe.setListaCupomFiscalReferenciado(new HashSet<>());
        nfe.getTransporte().setListaTransporteReboque(new HashSet<>());
        nfe.getTransporte().setListaTransporteVolume(new HashSet<>());
        nfe.setListaDuplicata(new HashSet<>());
        nfe.setListaNfeDetalhe(new ArrayList<>());
        valoresPadrao(nfe);

        return nfe;
    }


    private void valoresPadrao(NfeCabecalho nfe) {
        nfe.setTipoOperacao(0);
        nfe.setStatusNota(0);
        nfe.setFormatoImpressaoDanfe(1);
        nfe.setConsumidorOperacao(1);
        nfe.setTipoEmissao(1);
        nfe.setFinalidadeEmissao(1);
        nfe.setLocalDestino(1);
        nfe.getTransporte().setModalidadeFrete(0);

        Date dataAtual = new Date();
        nfe.setUfEmitente(empresa.getCodigoIbgeUf());
        nfe.setDataHoraEmissao(dataAtual);
        nfe.setDataHoraEntradaSaida(dataAtual);

        nfe.setBaseCalculoIcms(BigDecimal.ZERO);
        nfe.setValorIcms(BigDecimal.ZERO);
        nfe.setValorTotalProdutos(BigDecimal.ZERO);
        nfe.setBaseCalculoIcmsSt(BigDecimal.ZERO);
        nfe.setValorIcmsSt(BigDecimal.ZERO);
        nfe.setValorIpi(BigDecimal.ZERO);
        nfe.setValorPis(BigDecimal.ZERO);
        nfe.setValorCofins(BigDecimal.ZERO);
        nfe.setValorFrete(BigDecimal.ZERO);
        nfe.setValorSeguro(BigDecimal.ZERO);
        nfe.setValorDespesasAcessorias(BigDecimal.ZERO);
        nfe.setValorDesconto(BigDecimal.ZERO);
        nfe.setValorTotal(BigDecimal.ZERO);
        nfe.setValorImpostoImportacao(BigDecimal.ZERO);
        nfe.setBaseCalculoIssqn(BigDecimal.ZERO);
        nfe.setValorIssqn(BigDecimal.ZERO);
        nfe.setValorPisIssqn(BigDecimal.ZERO);
        nfe.setValorCofinsIssqn(BigDecimal.ZERO);
        nfe.setValorServicos(BigDecimal.ZERO);
        nfe.setValorRetidoPis(BigDecimal.ZERO);
        nfe.setValorRetidoCofins(BigDecimal.ZERO);
        nfe.setValorRetidoCsll(BigDecimal.ZERO);
        nfe.setBaseCalculoIrrf(BigDecimal.ZERO);
        nfe.setValorRetidoIrrf(BigDecimal.ZERO);
        nfe.setBaseCalculoPrevidencia(BigDecimal.ZERO);
        nfe.setValorRetidoPrevidencia(BigDecimal.ZERO);
        nfe.setValorIcmsDesonerado(BigDecimal.ZERO);
    }


    private void definirDestinatario(NfeCabecalho nfe, EstoqueTransferenciaCabecalho objeto) {
        Empresa empresa = empresaRepository.get(objeto.getEmpresaDestino().getId(), Empresa.class);
        nfe.setEmpresa(empresa);
        NfeDestinatario destinatario = new NfeDestinatario();
        nfe.setDestinatario(destinatario);
        destinatario.setNfeCabecalho(nfe);

        EmpresaEndereco endereco = empresa.buscarEnderecoPrincipal();

        destinatario.setCpfCnpj(empresa.getCnpj());
        destinatario.setInscricaoEstadual(empresa.getInscricaoEstadual());
        destinatario.setNome(empresa.getRazaoSocial());

        destinatario.setLogradouro(endereco.getLogradouro());
        destinatario.setNumero(endereco.getNumero());
        destinatario.setComplemento(endereco.getComplemento());
        destinatario.setBairro(endereco.getBairro());
        destinatario.setCodigoMunicipio(endereco.getMunicipioIbge());
        destinatario.setNomeMunicipio(endereco.getCidade());
        destinatario.setUf(endereco.getUf());
        destinatario.setCep(endereco.getCep());
        destinatario.setCodigoPais(1058);
        destinatario.setNomePais("BRASIL");
        destinatario.setTelefone(endereco.getFone());

        destinatario.setInscricaoMunicipal(empresa.getInscricaoMunicipal());


    }

    private void definirEmitente(NfeCabecalho nfe, EstoqueTransferenciaCabecalho objeto) {
        Empresa empresa = objeto.getEmpresaOrigem();
        NfeEmitente emitente = new NfeEmitente();

        EmpresaEndereco endereco = empresa.buscarEnderecoPrincipal();

        emitente.setCpfCnpj(empresa.getCnpj());
        emitente.setInscricaoEstadual(empresa.getInscricaoEstadual());
        emitente.setNome(empresa.getRazaoSocial());
        emitente.setFantasia(empresa.getNomeFantasia());
        emitente.setLogradouro(endereco.getLogradouro());
        emitente.setNumero(endereco.getNumero());
        emitente.setComplemento(endereco.getComplemento());
        emitente.setBairro(endereco.getBairro());
        emitente.setCodigoMunicipio(endereco.getMunicipioIbge());
        emitente.setNomeMunicipio(endereco.getCidade());
        emitente.setUf(endereco.getUf());
        emitente.setCep(endereco.getCep());
        emitente.setCrt(empresa.getCrt());
        emitente.setCodigoPais(1058);
        emitente.setNomePais("BRASIL");
        emitente.setTelefone(endereco.getFone());
        emitente.setInscricaoEstadualSt(empresa.getInscricaoEstadualSt());
        emitente.setInscricaoMunicipal(empresa.getInscricaoMunicipal());
        emitente.setCnae(empresa.getCodigoCnaePrincipal());

        emitente.setNfeCabecalho(nfe);
        nfe.setEmitente(emitente);

    }

    private void atualizarEstoque(Empresa empresa, TributOperacaoFiscal operacaoFiscal, NfeDetalhe detalhe) throws Exception {
        if (operacaoFiscal.getEstoqueVerificado() && operacaoFiscal.getEstoque()) {
            estoqueRepository.atualizaEstoqueEmpresaControleFiscal(empresa.getId(), detalhe.getProduto().getId(), detalhe.getQuantidadeComercial());
        } else if (operacaoFiscal.getEstoqueVerificado()) {
            estoqueRepository.atualizaEstoqueEmpresaControle(empresa.getId(), detalhe.getProduto().getId(), detalhe.getQuantidadeComercial());
        } else {
            estoqueRepository.atualizaEstoqueEmpresa(empresa.getId(), detalhe.getProduto().getId(), detalhe.getQuantidadeComercial());
        }
    }
}
