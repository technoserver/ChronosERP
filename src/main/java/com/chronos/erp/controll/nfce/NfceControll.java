package com.chronos.erp.controll.nfce;

import com.chronos.erp.controll.ERPLazyDataModel;
import com.chronos.erp.controll.nfe.NfeBaseControll;
import com.chronos.erp.modelo.entidades.NfeCabecalho;
import com.chronos.erp.modelo.entidades.NfeDetalhe;
import com.chronos.erp.modelo.entidades.NfeReferenciada;
import com.chronos.erp.modelo.entidades.TributOperacaoFiscal;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.NfeCabecalhoRepository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.service.comercial.NfeDetalheService;
import com.chronos.erp.service.comercial.NfeService;
import com.chronos.erp.util.jsf.FacesUtil;
import com.chronos.erp.util.jsf.Mensagem;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 04/10/17.
 */
@Named
@ViewScoped
public class NfceControll extends NfeBaseControll implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(NfceControll.class);

    private static final long serialVersionUID = 1L;

    @Inject
    private NfeCabecalhoRepository nfeRepositoy;
    @Inject
    private NfeService service;
    @Inject
    private NfeDetalheService nfeDetalheService;
    private List<NfeCabecalho> nfeSelecionadas;
    private TributOperacaoFiscal operacaoFiscal;


    @PostConstruct
    @Override
    public void init() {
        super.init();
        nfeSelecionadas = new ArrayList<>();
    }

    public ERPLazyDataModel<NfeCabecalho> getDataModel() {
        if (dataModel == null) {
            dataModel = new ERPLazyDataModel();
            dataModel.setDao(dao);
            dataModel.setClazz(NfeCabecalho.class);
        }
        dataModel.setAtributos(new Object[]{"serie", "numero", "dataHoraEmissao", "chaveAcesso", "digitoChaveAcesso", "valorTotal", "statusNota", "codigoModelo", "qrcode"});
        dataModel.getFiltros().clear();
        dataModel.addFiltro("tipoOperacao", 1, Filtro.IGUAL);
        dataModel.addFiltro("empresa.id", empresa.getId(), Filtro.IGUAL);
        dataModel.addFiltro("codigoModelo", "65", Filtro.IGUAL);
        return dataModel;
    }

    public void setDataModel(ERPLazyDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public void cancelarNfce() {

        try {
            NfeCabecalho nfe = dataModel.getRowData(getObjetoSelecionado().getId().toString());
            nfe.setJustificativaCancelamento(justificativa);

            boolean estoque = FacesUtil.isUserInRole("ESTOQUE");
            boolean cancelado = service.cancelarNFe(nfe, estoque);
            if (cancelado) {
                Mensagem.addInfoMessage("NFCe cancelada com sucesso");
            }

        } catch (Exception e) {
            if (e instanceof ChronosException) {
                Mensagem.addErrorMessage("Erro ao cancelar a NFCe");
            } else {
                throw new RuntimeException("Erro ao cancelar a NFCe", e);
            }
        }

    }

    public void danfe() {

        try {

            NfeCabecalho nfe = nfeRepositoy.getRemusoCupom(getObjetoSelecionado().getId());
            service.instanciarConfNfe(nfe.getEmpresa(), nfe.getModeloDocumento());
            service.danfe(nfe);

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Erro ao gerar o Cupom", ex);
            } else {
                throw new RuntimeException("Erro ao gerar o Cupom", ex);
            }
        }
    }

    public void instaciarDadosNfeReferenciada() {
        pessoaCliente = null;
        operacaoFiscal = null;
    }

    public void gerarNfeReferenciada() {

        try {

            if (nfeSelecionadas.isEmpty()) {
                throw new ChronosException("É preciso ao menos selecionar uma NFC-e");
            }

            NfeCabecalho nfe = service.dadosPadroes(empresa, ModeloDocumento.NFE);
            setObjeto(nfe);
            definirDestinatario(pessoaCliente);
            nfe.setTributOperacaoFiscal(operacaoFiscal);
            nfe.setNaturezaOperacao(operacaoFiscal.getDescricaoNaNf());
            int num = 1;
            String dadosAdicionais = "NFC-e referenciadas ";
            for (NfeCabecalho nfeSelecionada : nfeSelecionadas) {
                for (NfeDetalhe i : nfeSelecionada.getListaNfeDetalhe()) {
                    NfeDetalhe item = new NfeDetalhe();
                    item.setNfeCabecalho(nfe);
                    instanciarImpostos(item);
                    item.setProduto(i.getProduto());
                    item.setNumeroItem(num);
                    item.setQuantidadeComercial(i.getQuantidadeComercial());
                    item.setValorUnitarioComercial(i.getValorUnitarioComercial());
                    item.setValorOutrasDespesas(i.getValorOutrasDespesas());
                    item.setValorFrete(i.getValorFrete());
                    item.setValorSeguro(i.getValorSeguro());
                    item.setValorDesconto(i.getValorDesconto());
                    nfeDetalheService.addProduto(nfe, item);
                    num++;
                }
                NfeReferenciada nfeReferenciada = new NfeReferenciada();
                nfeReferenciada.setNfeCabecalho(nfe);
                nfeReferenciada.setChaveAcesso(nfeSelecionada.getChaveAcessoCompleta());

                nfe.getListaNfeReferenciada().add(nfeReferenciada);
                dadosAdicionais += nfeSelecionada.getNumero() + " ";
            }

            service.atualizarTotais(nfe);
            nfe.setInformacoesAddContribuinte(dadosAdicionais);
            transmitirNfe(true, false);

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().validationFailed();
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro ao gerar o nfe referenciada", ex);
            }
        }
    }

    public List<NfeCabecalho> getNfeSelecionadas() {
        return nfeSelecionadas;
    }

    public void setNfeSelecionadas(List<NfeCabecalho> nfeSelecionadas) {
        this.nfeSelecionadas = nfeSelecionadas;
    }

    public TributOperacaoFiscal getOperacaoFiscal() {
        return operacaoFiscal;
    }

    public void setOperacaoFiscal(TributOperacaoFiscal operacaoFiscal) {
        this.operacaoFiscal = operacaoFiscal;
    }
}
