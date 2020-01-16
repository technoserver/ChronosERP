package com.chronos.erp.service.estoque;

import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.modelo.enuns.AcaoLog;
import com.chronos.erp.modelo.enuns.Modulo;
import com.chronos.erp.modelo.enuns.StatusTransmissao;
import com.chronos.erp.repository.EstoqueRepository;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.service.cadastros.FornecedorService;
import com.chronos.erp.service.cadastros.ProdutoFornecedorService;
import com.chronos.erp.service.comercial.SyncPendentesService;
import com.chronos.erp.service.financeiro.FinLancamentoPagarService;
import com.chronos.erp.service.gerencial.AuditoriaService;
import com.chronos.erp.util.jpa.Transactional;
import com.chronos.erp.util.jsf.FacesUtil;
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
    @Inject
    private ProdutoFornecedorService produtoFornecedorService;


    @Transactional
    public void finalizar(NfeCabecalho nfe, ContaCaixa contaCaixa, NaturezaFinanceira naturezaFinanceira) throws Exception {
        boolean inclusao = false;


        for (NfeDetalhe item : nfe.getListaNfeDetalhe()) {

            if (item.getListaGrade() != null && !item.getListaGrade().isEmpty()) {
                BigDecimal qtd = item.getListaGrade()
                        .stream()
                        .map(i -> i.getQuantidade())
                        .reduce(BigDecimal::add)
                        .orElse(BigDecimal.ZERO);

                if (qtd.compareTo(item.getQuantidadeComercial()) != 0) {
                    throw new ChronosException("Quantidade de estoque informada para grade invalida ");
                }
            }
        }
        if (!nfe.getListaDuplicata().isEmpty()) {

            if (naturezaFinanceira == null) {
                throw new ChronosException("Natureza financeira não informada");
            }

            if (contaCaixa == null) {
                throw new ChronosException("Conta caixa não informada");
            }

        }

        Integer idempresa = nfe.getEmpresa().getId();
        AdmParametro parametro = FacesUtil.getParamentos();
        nfe.setNaturezaOperacao(nfe.getTributOperacaoFiscal().getDescricaoNaNf());


        if (nfe.getId() == null || !nfe.getStatusNota().equals(StatusTransmissao.ENCERRADO.getCodigo())) {
            nfe = repository.atualizar(nfe);
            inclusao = true;
            for (NfeDetalhe detalhe : nfe.getListaNfeDetalhe()) {
                atualizarEstoque(nfe.getEmpresa(), nfe.getTributOperacaoFiscal(), detalhe);
                if (parametro != null && parametro.getFrenteCaixa()) {
                    syncPendentesService.gerarSyncPendetensEstoque(0, idempresa, detalhe.getProduto().getId());
                }

                if (detalhe.getListaGrade() != null && !detalhe.getListaGrade().isEmpty()) {

                    for (NfeDetEspecificoGrade eg : detalhe.getListaGrade()) {
                        EstoqueGrade g = eg.getEstoqueGrade();
                        if (eg.getQuantidade() != null && eg.getQuantidade().signum() > 0) {
                            if (nfe.getTributOperacaoFiscal().getEstoqueVerificado() && nfe.getTributOperacaoFiscal().getEstoque()) {
                                estoqueRepository.atualizarGradeQuantidaAndVerificado(idempresa, g.getIdproduto(), g.getEstoqueCor().getId(), g.getEstoqueTamanho().getId(), eg.getQuantidade());
                            } else if (nfe.getTributOperacaoFiscal().getEstoqueVerificado()) {
                                estoqueRepository.atualizarGradeVerificado(idempresa, g.getIdproduto(), g.getEstoqueCor().getId(), g.getEstoqueTamanho().getId(), eg.getQuantidade());
                            } else {
                                estoqueRepository.atualizarGradeQuantidade(idempresa, g.getIdproduto(), g.getEstoqueCor().getId(), g.getEstoqueTamanho().getId(), eg.getQuantidade());
                            }
                        }

                    }


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
                estoqueRepository.atualizaEstoqueEmpresaControleFiscal(idempresa, nfe.getListaNfeDetalhe());
            } else if (nfe.getTributOperacaoFiscal().getEstoqueVerificado()) {
                estoqueRepository.atualizaEstoqueEmpresaControle(idempresa, nfe.getListaNfeDetalhe());
            } else {
                estoqueRepository.atualizaEstoqueEmpresa(idempresa, nfe.getListaNfeDetalhe());
            }


        }
        String descricao = "Entrada da NFe :" + nfe.getNumero() + " Fornecedor :" + nfe.getEmitente().getNome();
        nfe.setStatusNota(StatusTransmissao.ENCERRADO.getCodigo());
        nfe = repository.atualizar(nfe);

        if (!nfe.getListaDuplicata().isEmpty()) {

            lancamentoPagarService.gerarLancamento(nfe, contaCaixa, naturezaFinanceira);
        }

        produtoFornecedorService.atualizarUtimaCompra(nfe);


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


        finalizar(nfe, null, null);
    }

    public NfeCabecalho iniciar(Empresa empresa) {
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
        nfe.setStatusNota(StatusTransmissao.EDICAO.getCodigo());
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

    private void atualizarEstoque(Empresa empresa, TributOperacaoFiscal operacaoFiscal, NfeDetalhe detalhe) {
        if (operacaoFiscal.getEstoqueVerificado() && operacaoFiscal.getEstoque()) {
            estoqueRepository.atualizarEstoqueMovimento(detalhe.getProduto().getId(), empresa.getId(), detalhe.getQuantidadeComercial(),
                    Modulo.ENTRADA.getCodigo(), detalhe.getNfeCabecalho().getId().toString(), "F", "E");
            estoqueRepository.atualizarEstoqueMovimento(detalhe.getProduto().getId(), empresa.getId(), detalhe.getQuantidadeComercial(),
                    Modulo.ENTRADA.getCodigo(), detalhe.getNfeCabecalho().getId().toString(), "V", "E");
            estoqueRepository.atualizaEstoqueEmpresaControleFiscal(empresa.getId(), detalhe.getProduto().getId(), detalhe.getQuantidadeComercial());

        } else if (operacaoFiscal.getEstoqueVerificado()) {
            estoqueRepository.atualizarEstoqueMovimento(detalhe.getProduto().getId(), empresa.getId(), detalhe.getQuantidadeComercial(),
                    Modulo.ENTRADA.getCodigo(), detalhe.getNfeCabecalho().getId().toString(), "V", "E");
            estoqueRepository.atualizaEstoqueEmpresaControle(empresa.getId(), detalhe.getProduto().getId(), detalhe.getQuantidadeComercial());

        } else {
            estoqueRepository.atualizarEstoqueMovimento(detalhe.getProduto().getId(), empresa.getId(), detalhe.getQuantidadeComercial(),
                    Modulo.ENTRADA.getCodigo(), detalhe.getNfeCabecalho().getId().toString(), "F", "E");
            estoqueRepository.atualizaEstoqueEmpresa(empresa.getId(), detalhe.getProduto().getId(), detalhe.getQuantidadeComercial());

        }
    }

    @Transactional
    public void estornarEstoque(NfeCabecalho nfe) {
        Empresa emp = nfe.getEmpresa();


        if (nfe.getTributOperacaoFiscal().getEstoqueVerificado() && nfe.getTributOperacaoFiscal().getEstoque()) {
            estoqueRepository.atualizaEstoqueEmpresaControleFiscal(emp.getId(), nfe.getListaNfeDetalhe());
        } else if (nfe.getTributOperacaoFiscal().getEstoqueVerificado()) {
            estoqueRepository.atualizaEstoqueEmpresaControle(emp.getId(), nfe.getListaNfeDetalhe());
        } else {
            estoqueRepository.atualizaEstoqueEmpresa(emp.getId(), nfe.getListaNfeDetalhe());
        }

        for (NfeDetalhe item : nfe.getListaNfeDetalhe()) {
            if (nfe.getTributOperacaoFiscal().getEstoqueVerificado() && nfe.getTributOperacaoFiscal().getEstoque()) {
                estoqueRepository.atualizarEstoqueMovimento(item.getProduto().getId(), emp.getId(), item.getQuantidadeComercial(),
                        Modulo.ENTRADA.getCodigo(), item.getNfeCabecalho().getId().toString(), "F", "S");
                estoqueRepository.atualizarEstoqueMovimento(item.getProduto().getId(), emp.getId(), item.getQuantidadeComercial(),
                        Modulo.ENTRADA.getCodigo(), item.getNfeCabecalho().getId().toString(), "V", "S");


            } else if (nfe.getTributOperacaoFiscal().getEstoqueVerificado()) {
                estoqueRepository.atualizarEstoqueMovimento(item.getProduto().getId(), emp.getId(), item.getQuantidadeComercial(),
                        Modulo.ENTRADA.getCodigo(), item.getNfeCabecalho().getId().toString(), "V", "S");

            } else {
                estoqueRepository.atualizarEstoqueMovimento(item.getProduto().getId(), emp.getId(), item.getQuantidadeComercial(),
                        Modulo.ENTRADA.getCodigo(), item.getNfeCabecalho().getId().toString(), "F", "S");

            }
        }

        repository.excluir(nfe, nfe.getId());
        auditoriaService.gerarLog(AcaoLog.DELETE, "Exclusão de Nota fiscal de entrada já encerrada", "Entrada de NF");
    }
}
