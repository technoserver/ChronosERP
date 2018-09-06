package com.chronos.controll.compras;

import com.chronos.bo.nfe.ImportaXMLNFe;
import com.chronos.bo.nfe.NfeUtil;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.controll.nfe.NfeBaseControll;
import com.chronos.dto.ImpostoDTO;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.FinalidadeEmissao;
import com.chronos.modelo.enuns.StatusTransmissao;
import com.chronos.modelo.enuns.TipoImportacaoXml;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.service.comercial.DefinirCstService;
import com.chronos.service.comercial.NfeService;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import com.chronos.util.Biblioteca;
import com.chronos.util.jsf.Mensagem;
import org.apache.commons.io.FileUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.UploadedFile;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Named
@ViewScoped
public class DevolucaoCompraControll extends NfeBaseControll implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private NfeService service;
    @Inject
    private DefinirCstService definirCstService;
    @Inject
    private Repository<ConverterCfop> converterCfopRepository;
    @Inject
    private Repository<ConverterCst> converterCstRepository;
    @Inject
    private Repository<FornecedorProduto> fornecedorProdutoRepository;

    private NfeDetalhe nfeDetalheSelecionado;
    private NfeDetalheImpostoCofins nfeDetalheImpostoCofins;
    private NfeDetalheImpostoPis nfeDetalheImpostoPis;
    private NfeDetalheImpostoIcms nfeDetalheImpostoIcms;
    private NfeDetalheImpostoIpi nfeDetalheImpostoIpi;

    private boolean dadosSalvos;
    private BigDecimal vlrAux;
    private BigDecimal qtdAux;
    private BigDecimal qtdOld;

    private static BigDecimal valorPorItem(BigDecimal qtd, BigDecimal qtdAtual, BigDecimal valor) {
        try {
            BigDecimal valorPorItem = valor.divide(qtd);
            valorPorItem = valorPorItem.setScale(2, RoundingMode.HALF_DOWN);
            return valorPorItem.multiply(qtdAtual);
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public ERPLazyDataModel<NfeCabecalho> getDataModel() {

        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(NfeCabecalho.class);
            dataModel.setDao(dao);
        }
        dataModel.getFiltros().clear();
        dataModel.getFiltros().add(new Filtro("finalidadeEmissao", 4));
        dataModel.getFiltros().add(new Filtro("empresa.id", empresa.getId()));

        return dataModel;
    }

    @Override
    public void doEdit() {
        NfeCabecalho nfe = dataModel.getRowData(getObjetoSelecionado().getId().toString());
        Empresa emp = nfe.getEmpresa();
        setObjeto(nfe);
        setTelaGrid(false);
        dadosSalvos = true;
    }

    public void importarXml(FileUploadEvent event) {
        try {
            UploadedFile arquivoUpload = event.getFile();

            File file = File.createTempFile("nfe", ".xml");
            FileUtils.copyInputStreamToFile(arquivoUpload.getInputstream(), file);


            List<ConverterCfop> cfops = converterCfopRepository.getEntitys(ConverterCfop.class, "tipo", "S");
            List<ConverterCst> csts = converterCstRepository.getEntitys(ConverterCst.class, "tipo", "S");

            ImportaXMLNFe importaXml = new ImportaXMLNFe(TipoImportacaoXml.DEVOLUCAO, csts, cfops);
            Map map = importaXml.importarXmlNFe(file);

            List<NfeReferenciada> listNfeReferenciada = (ArrayList) map.get("nfeReferenciada");

            String chave = listNfeReferenciada.get(0).getChaveAcesso();
            verificarEntrada(chave);
            super.doCreate();
            setObjeto((NfeCabecalho) map.get("cabecalho"));
            getObjeto().setEmpresa(empresa);
            getObjeto().setTipoOperacao(1);
            getObjeto().setNaturezaOperacao("DEVOLUCAO");
            getObjeto().setNumero("");
            getObjeto().setChaveAcesso("");
            getObjeto().setDigitoChaveAcesso("");
            getObjeto().setSerie("");
            getObjeto().setCodigoNumerico("");
            getObjeto().setDataHoraEmissao(null);
            getObjeto().setDataHoraEntradaSaida(null);
            getObjeto().setStatusNota(StatusTransmissao.EDICAO.getCodigo());
            getObjeto().setCodigoModelo(ModeloDocumento.NFE.getCodigo().toString());
            getObjeto().setDestinatario((NfeDestinatario) map.get("destinatario"));
            getObjeto().setFinalidadeEmissao(FinalidadeEmissao.DEVOLUCAO.getCodigo());
            getObjeto().setListaNfeDetalhe((ArrayList) map.get("detalhe"));
            getObjeto().setListaNfeReferenciada(new HashSet<>(listNfeReferenciada));



            for (NfeDetalhe item : getObjeto().getListaNfeDetalhe()) {
                ImpostoDTO impostoDTO = new ImpostoDTO(item.getNfeDetalheImpostoIcms());
                String cst = definirCstService.definirCst(csts, impostoDTO, empresa.getCrt());
                impostoDTO.setCst(cst);
                NfeDetalheImpostoIcms icms = definirCstService.definirNfeDetalheIcms(cst, empresa.getCrt(), impostoDTO);
                icms.setNfeDetalhe(item);
                item.setNfeDetalheImpostoIcms(icms);
                verificaProdutoNaoCadastrado(item);
            }

            atualizaTotais();

            NfeUtil nfeUtil = new NfeUtil();
            NfeEmitente emitente = nfeUtil.getEmitente(empresa);
            emitente.setNfeCabecalho(getObjeto());
            getObjeto().setEmitente(emitente);

            service.instanciarDadosConfiguracoes(getObjeto());

            service.definirIndicadorIe(getObjeto().getDestinatario(), ModeloDocumento.NFE);

            super.salvar();

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro ao importa XML", ex);
            }
        }
    }

    public void verificarEntrada(String chave) throws ChronosException {

        List<Filtro> filtro = new LinkedList<>();
        filtro.add(new Filtro("chaveAcesso", chave));
        filtro.add(new Filtro("tipoOperacao", 0));

        NfeCabecalho nfe = dao.get(NfeCabecalho.class, filtro, new Object[]{"numero"});

        if (nfe == null) {
            throw new ChronosException("Entrada para esse fornecedor não localizada");
        }
    }

    public void excluirProduto() {
        if (nfeDetalheSelecionado == null) {
            Mensagem.addErrorMessage("Nenhum registro selecionado!");
        } else {
            getObjeto().getListaNfeDetalhe().remove(nfeDetalheSelecionado);
            atualizaTotais();
            dadosSalvos = false;
            Mensagem.addInfoMessage("Registro excluído!");
        }
    }

    public void alterarProduto(RowEditEvent event) {
        try {
            if (event != null) {

                vlrAux = BigDecimal.ZERO;
                qtdAux = BigDecimal.ZERO;

                NfeDetalhe nfeDetalheAtual = (NfeDetalhe) event.getObject();
                qtdAux = nfeDetalheAtual.getQuantidadeComercial();

                nfeDetalheSelecionado.setQuantidadeComercial(qtdAux);
                nfeDetalheSelecionado.setQuantidadeTributavel(qtdAux);
                BigDecimal descontoUnitario = nfeDetalheSelecionado.getValorDesconto() != null ? nfeDetalheSelecionado.getValorDesconto().divide(qtdOld) : BigDecimal.ZERO;

                nfeDetalheSelecionado
                        .setValorBrutoProduto(qtdAux.multiply(nfeDetalheAtual.getValorUnitarioComercial()));
                nfeDetalheSelecionado.setValorDesconto(descontoUnitario.multiply(qtdAux));
                nfeDetalheSelecionado.setValorSubtotal(nfeDetalheSelecionado.getValorBrutoProduto());
                nfeDetalheSelecionado.setValorTotal(nfeDetalheSelecionado.getValorBrutoProduto()
                        .subtract(nfeDetalheSelecionado.getValorDesconto()));

                // icms
                if (nfeDetalheSelecionado.getNfeDetalheImpostoIcms() != null) {
                    if (nfeDetalheSelecionado.getNfeDetalheImpostoIcms().getBaseCalculoIcms() != null) {
                        vlrAux = valorPorItem(qtdOld, qtdAux,
                                nfeDetalheSelecionado.getNfeDetalheImpostoIcms().getBaseCalculoIcms());
                        nfeDetalheSelecionado.getNfeDetalheImpostoIcms().setBaseCalculoIcms(vlrAux);
                        vlrAux = valorPorItem(qtdOld, qtdAux,
                                nfeDetalheSelecionado.getNfeDetalheImpostoIcms().getValorIcms());

                        nfeDetalheSelecionado.getNfeDetalheImpostoIcms().setValorIcms(vlrAux);
                    }

                    if (nfeDetalheSelecionado.getNfeDetalheImpostoIcms().getValorBaseCalculoIcmsSt() != null) {
                        vlrAux = valorPorItem(qtdOld, qtdAux,
                                nfeDetalheSelecionado.getNfeDetalheImpostoIcms().getValorBaseCalculoIcmsSt());
                        nfeDetalheSelecionado.getNfeDetalheImpostoIcms().setValorBaseCalculoIcmsSt(vlrAux);

                        vlrAux = valorPorItem(qtdOld, qtdAux,
                                nfeDetalheSelecionado.getNfeDetalheImpostoIcms().getValorIcmsSt());
                        nfeDetalheSelecionado.getNfeDetalheImpostoIcms().setValorIcmsSt(vlrAux);
                    }
                }
                // IPI

                if (nfeDetalheSelecionado.getNfeDetalheImpostoIpi() != null) {
                    if (nfeDetalheSelecionado.getNfeDetalheImpostoIpi().getValorBaseCalculoIpi() != null) {
                        vlrAux = valorPorItem(qtdOld, qtdAux,
                                nfeDetalheSelecionado.getNfeDetalheImpostoIpi().getValorBaseCalculoIpi());
                        nfeDetalheSelecionado.getNfeDetalheImpostoIpi().setValorBaseCalculoIpi(vlrAux);
                        vlrAux = valorPorItem(qtdOld, qtdAux,
                                nfeDetalheSelecionado.getNfeDetalheImpostoIpi().getValorIpi());
                        nfeDetalheSelecionado.getNfeDetalheImpostoIpi().setValorIpi(vlrAux);
                    }
                }

                // PIS
                if (nfeDetalheSelecionado.getNfeDetalheImpostoPis() != null) {
                    vlrAux = valorPorItem(qtdOld, qtdAux,
                            nfeDetalheSelecionado.getNfeDetalheImpostoPis().getValorPis());
                    nfeDetalheSelecionado.getNfeDetalheImpostoPis().setValorPis(vlrAux);
                }

                // COFINS
                if (nfeDetalheSelecionado.getNfeDetalheImpostoCofins() != null) {
                    vlrAux = valorPorItem(qtdOld, qtdAux,
                            nfeDetalheSelecionado.getNfeDetalheImpostoCofins().getValorCofins());
                    nfeDetalheSelecionado.getNfeDetalheImpostoCofins().setValorCofins(vlrAux);
                }

                atualizaTotais();

            }
            dadosSalvos = false;
            Mensagem.addInfoMessage("Dados atualizado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao salvar o registro!", e);
        }
    }

    public void selecionarItem() {
        try {
            if (nfeDetalheSelecionado != null) {
                qtdOld = nfeDetalheSelecionado.getQuantidadeComercial() != null
                        ? nfeDetalheSelecionado.getQuantidadeComercial() : BigDecimal.ZERO;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void alterarImpostos() {
        if (nfeDetalheSelecionado == null) {
            Mensagem.addErrorMessage("Nenhum registro selecionado!");
        } else {
            nfeDetalheImpostoCofins = nfeDetalheSelecionado.getNfeDetalheImpostoCofins() == null ? new NfeDetalheImpostoCofins() : nfeDetalheSelecionado.getNfeDetalheImpostoCofins();
            nfeDetalheImpostoIcms = nfeDetalheSelecionado.getNfeDetalheImpostoIcms() == null ? new NfeDetalheImpostoIcms() : nfeDetalheSelecionado.getNfeDetalheImpostoIcms();
            nfeDetalheImpostoIpi = nfeDetalheSelecionado.getNfeDetalheImpostoIpi() == null ? new NfeDetalheImpostoIpi() : nfeDetalheSelecionado.getNfeDetalheImpostoIpi();
            nfeDetalheImpostoPis = nfeDetalheSelecionado.getNfeDetalheImpostoPis() == null ? new NfeDetalheImpostoPis() : nfeDetalheSelecionado.getNfeDetalheImpostoPis();
            dadosSalvos = false;
            RequestContext.getCurrentInstance().execute("PF('dialogOutrasTelas2').show()");
        }
    }

    public void salvarImpostos() {
        realizarCalculoItem();
        atualizaTotais();
        Mensagem.addInfoMessage("Impostos atualizado!");
        dadosSalvos = false;
    }

    private void realizarCalculoItem() {
        nfeDetalheSelecionado.setValorUnitarioTributavel(nfeDetalheSelecionado.getValorUnitarioComercial());
        nfeDetalheSelecionado.setValorBrutoProduto(
                Biblioteca.multiplica(nfeDetalheSelecionado.getValorUnitarioComercial(), nfeDetalheSelecionado.getQuantidadeComercial()));
        nfeDetalheSelecionado.setQuantidadeTributavel(nfeDetalheSelecionado.getQuantidadeComercial());
        nfeDetalheSelecionado.setValorSubtotal(nfeDetalheSelecionado.getValorBrutoProduto());
        nfeDetalheSelecionado.setEntraTotal(1);
        if (nfeDetalheSelecionado.getValorFrete() == null) {
            nfeDetalheSelecionado.setValorFrete(BigDecimal.ZERO);
        }
        if (nfeDetalheSelecionado.getValorOutrasDespesas() == null) {
            nfeDetalheSelecionado.setValorOutrasDespesas(BigDecimal.ZERO);
        }
        if (nfeDetalheSelecionado.getValorSeguro() == null) {
            nfeDetalheSelecionado.setValorSeguro(BigDecimal.ZERO);
        }
        if (nfeDetalheSelecionado.getValorDesconto() == null) {
            nfeDetalheSelecionado.setValorDesconto(BigDecimal.ZERO);
        }
        // nfeDetalhe.setValorTotal(nfeDetalhe.getValorBrutoProduto().subtract(nfeDetalhe.getValorDesconto()));
        nfeDetalheSelecionado.setValorTotal(nfeDetalheSelecionado.getValorBrutoProduto());
        realizarCalculoImposto();
    }

    public void transmitir() {

        try {
            if (dadosSalvos) {
                getObjeto().setDataHoraEmissao(new Date());
                getObjeto().setDataHoraEntradaSaida(new Date());
                transmitirNfe(true);
            } else {
                Mensagem.addInfoMessage("Antes de enviar a NF-e é necessário salvar as informações!");
            }

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("", ex);
            }
        }

    }

    private void realizarCalculoImposto() {
        if (nfeDetalheImpostoIcms != null) {
            BigDecimal valorCreditoSN;
            BigDecimal aliquotaCreditoIcms = nfeDetalheImpostoIcms.getAliquotaCreditoIcmsSn() == null ? BigDecimal.ZERO : nfeDetalheImpostoIcms.getAliquotaCreditoIcmsSn();
            valorCreditoSN = Biblioteca.multiplica(nfeDetalheSelecionado.getValorTotal(), aliquotaCreditoIcms).divide(BigDecimal.valueOf(100), RoundingMode.DOWN);
            nfeDetalheImpostoIcms.setValorCreditoIcmsSn(valorCreditoSN);
        }
    }

    private void atualizaTotais() {
        BigDecimal totalProdutos = BigDecimal.ZERO;
        BigDecimal valorFrete = BigDecimal.ZERO;
        BigDecimal valorSeguro = BigDecimal.ZERO;
        BigDecimal valorOutrasDespesas = BigDecimal.ZERO;
        BigDecimal desconto = BigDecimal.ZERO;
        BigDecimal baseCalculoIcms = BigDecimal.ZERO;
        BigDecimal valorIcms = BigDecimal.ZERO;
        BigDecimal baseCalculoIcmsSt = BigDecimal.ZERO;
        BigDecimal valorIcmsSt = BigDecimal.ZERO;
        BigDecimal valorIpi = BigDecimal.ZERO;
        BigDecimal valorPis = BigDecimal.ZERO;
        BigDecimal valorCofins = BigDecimal.ZERO;
        BigDecimal valorNotaFiscal;
        BigDecimal valorIcmsDesonerado = BigDecimal.ZERO;
        BigDecimal totalServicos = BigDecimal.ZERO;
        BigDecimal baseCalculoIssqn = BigDecimal.ZERO;
        BigDecimal valorIssqn = BigDecimal.ZERO;
        BigDecimal valorPisIssqn = BigDecimal.ZERO;
        BigDecimal valorCofinsIssqn = BigDecimal.ZERO;

        for (NfeDetalhe itensNfe : getObjeto().getListaNfeDetalhe()) {
            totalProdutos = totalProdutos.add(itensNfe.getValorBrutoProduto());
            valorFrete = valorFrete.add(itensNfe.getValorFrete() != null ? itensNfe.getValorFrete() : BigDecimal.ZERO);
            valorSeguro = valorSeguro.add(itensNfe.getValorSeguro() != null ? itensNfe.getValorSeguro() : BigDecimal.ZERO);
            valorOutrasDespesas = valorOutrasDespesas.add(itensNfe.getValorOutrasDespesas() != null ? itensNfe.getValorOutrasDespesas() : BigDecimal.ZERO);
            desconto = desconto.add(itensNfe.getValorDesconto() != null ? itensNfe.getValorDesconto() : BigDecimal.ZERO);

            if (itensNfe.getNfeDetalheImpostoIcms().getBaseCalculoIcms() != null) {
                baseCalculoIcms = baseCalculoIcms.add(itensNfe.getNfeDetalheImpostoIcms().getBaseCalculoIcms());
            }
            if (itensNfe.getNfeDetalheImpostoIcms().getValorIcms() != null) {
                valorIcms = valorIcms.add(itensNfe.getNfeDetalheImpostoIcms().getValorIcms());
            }
            if (itensNfe.getNfeDetalheImpostoIcms().getValorIcmsDesonerado() != null) {
                valorIcmsDesonerado = valorIcmsDesonerado
                        .add(itensNfe.getNfeDetalheImpostoIcms().getValorIcmsDesonerado());
            }
            if (itensNfe.getNfeDetalheImpostoIcms().getValorBaseCalculoIcmsSt() != null) {
                baseCalculoIcmsSt = baseCalculoIcmsSt
                        .add(itensNfe.getNfeDetalheImpostoIcms().getValorBaseCalculoIcmsSt());
            }
            if (itensNfe.getNfeDetalheImpostoIcms().getValorIcmsSt() != null) {
                valorIcmsSt = valorIcmsSt.add(itensNfe.getNfeDetalheImpostoIcms().getValorIcmsSt());
            }
            if (itensNfe.getNfeDetalheImpostoIpi() != null
                    && itensNfe.getNfeDetalheImpostoIpi().getValorIpi() != null) {
                valorIpi = valorIpi.add(itensNfe.getNfeDetalheImpostoIpi().getValorIpi());
            }
            if (itensNfe.getNfeDetalheImpostoPis() != null
                    && itensNfe.getNfeDetalheImpostoPis().getValorPis() != null) {
                valorPis = valorPis.add(itensNfe.getNfeDetalheImpostoPis().getValorPis());
            }
            if (itensNfe.getNfeDetalheImpostoCofins() != null
                    && itensNfe.getNfeDetalheImpostoCofins().getValorCofins() != null) {
                valorCofins = valorCofins.add(itensNfe.getNfeDetalheImpostoCofins().getValorCofins());
            }
        }

        valorNotaFiscal = totalProdutos.add(valorIcmsSt).add(valorFrete).add(valorSeguro).add(valorOutrasDespesas)
                .add(valorIpi).subtract(desconto);


        getObjeto().setValorFrete(valorFrete);
        getObjeto().setValorDespesasAcessorias(valorOutrasDespesas);
        getObjeto().setValorSeguro(valorSeguro);
        getObjeto().setValorDesconto(desconto);

        getObjeto().setValorServicos(totalServicos);
        getObjeto().setBaseCalculoIssqn(baseCalculoIssqn);
        getObjeto().setValorIssqn(valorIssqn);
        getObjeto().setValorPisIssqn(valorPisIssqn);
        getObjeto().setValorCofinsIssqn(valorCofinsIssqn);
        getObjeto().setValorIcmsDesonerado(valorIcmsDesonerado);

        getObjeto().setValorTotalProdutos(totalProdutos);
        getObjeto().setBaseCalculoIcms(baseCalculoIcms);
        getObjeto().setValorIcms(valorIcms);
        getObjeto().setBaseCalculoIcmsSt(baseCalculoIcmsSt);
        getObjeto().setValorIcmsSt(valorIcmsSt);
        getObjeto().setValorIpi(valorIpi);
        getObjeto().setValorPis(valorPis);
        getObjeto().setValorCofins(valorCofins);

        getObjeto().setValorTotal(valorNotaFiscal);
    }

    private void verificaProdutoNaoCadastrado(NfeDetalhe item) throws Exception {


        FornecedorProduto fornecedorProduto = fornecedorProdutoRepository.get(FornecedorProduto.class, "codigoFornecedorProduto", item.getCodigoProduto(), new Object[]{"produto.id", "produto.nome"});

        if (fornecedorProduto == null) {
            throw new ChronosException("Produo não vinculado ao fornacedor");
        } else {
            item.setProduto(fornecedorProduto.getProduto());
        }


    }

    @Override
    protected Class<NfeCabecalho> getClazz() {
        return NfeCabecalho.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "DEVOLUCAO_COMPRA";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public NfeDetalhe getNfeDetalheSelecionado() {
        return nfeDetalheSelecionado;
    }

    public void setNfeDetalheSelecionado(NfeDetalhe nfeDetalheSelecionado) {
        this.nfeDetalheSelecionado = nfeDetalheSelecionado;
    }


    public NfeDetalheImpostoCofins getNfeDetalheImpostoCofins() {
        return nfeDetalheImpostoCofins;
    }

    public void setNfeDetalheImpostoCofins(NfeDetalheImpostoCofins nfeDetalheImpostoCofins) {
        this.nfeDetalheImpostoCofins = nfeDetalheImpostoCofins;
    }

    public NfeDetalheImpostoPis getNfeDetalheImpostoPis() {
        return nfeDetalheImpostoPis;
    }

    public void setNfeDetalheImpostoPis(NfeDetalheImpostoPis nfeDetalheImpostoPis) {
        this.nfeDetalheImpostoPis = nfeDetalheImpostoPis;
    }

    public NfeDetalheImpostoIcms getNfeDetalheImpostoIcms() {
        return nfeDetalheImpostoIcms;
    }

    public void setNfeDetalheImpostoIcms(NfeDetalheImpostoIcms nfeDetalheImpostoIcms) {
        this.nfeDetalheImpostoIcms = nfeDetalheImpostoIcms;
    }

    public NfeDetalheImpostoIpi getNfeDetalheImpostoIpi() {
        return nfeDetalheImpostoIpi;
    }

    public void setNfeDetalheImpostoIpi(NfeDetalheImpostoIpi nfeDetalheImpostoIpi) {
        this.nfeDetalheImpostoIpi = nfeDetalheImpostoIpi;
    }

    public Empresa getEmpresa() {
        return empresa;
    }
}
