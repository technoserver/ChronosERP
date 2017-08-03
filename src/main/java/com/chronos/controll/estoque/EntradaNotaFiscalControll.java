package com.chronos.controll.estoque;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.*;
import com.chronos.service.cadastros.FornecedorService;
import com.chronos.service.cadastros.ProdutoFornecedorService;
import com.chronos.service.estoque.EntradaNotaFiscalService;
import com.chronos.util.Biblioteca;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

/**
 * Created by john on 02/08/17.
 */
@Named
@ViewScoped
public class EntradaNotaFiscalControll extends AbstractControll<NfeCabecalho> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EntradaNotaFiscalService entradaService;
    @Inject
    private FornecedorService fornecedorService;
    @Inject
    private ProdutoFornecedorService produtoFornecedorService;


    private NfeDetalhe nfeDetalhe;
    private NfeDetalhe nfeDetalheSelecionado;
    private NfeDetalheImpostoIcms nfeDetalheImpostoIcms;
    private NfeDetalheImpostoIpi nfeDetalheImpostoIpi;
    private NfeDetalheImpostoCofins nfeDetalheImpostoCofins;
    private NfeDetalheImpostoPis nfeDetalheImpostoPis;

    private Produto produto;
    private Fornecedor fornecedor;


    private BigDecimal valorTotalFrete;
    private BigDecimal valorTotalDesconto;
    private BigDecimal valorTotalProdutos;
    private BigDecimal valorTotalBaseCalcIcms;
    private BigDecimal valorTotalIcms;
    private BigDecimal valorTotalBaseCalcIcmsST;
    private BigDecimal valorTotalIcmsST;
    private BigDecimal valorTotalBaseCalcPis;
    private BigDecimal valorTotalPis;
    private BigDecimal valorTotalBaseCalcCofins;
    private BigDecimal valorTotalCofins;
    private BigDecimal valorTotalIpi;
    private BigDecimal valorTotalNF;

    private int tipoCstIcms;


    @Override
    public ERPLazyDataModel<NfeCabecalho> getDataModel() {
        if (dataModel == null) {
            Object[] atribut = new Object[]{"fornecedor", "serie", "numero", "dataHoraEmissao", "chaveAcesso", "digitoChaveAcesso", "valorTotal", "statusNota"};
            dataModel = new ERPLazyDataModel();
            dataModel.setClazz(getClazz());
            dataModel.addFiltro("tipoOperacao", 0);
            dataModel.setDao(dao);
            dataModel.setAtributos(atribut);


        }
        return dataModel;
    }


    public void gerarValores(NfeDetalhe item) {
        valorTotalFrete = Biblioteca.soma(valorTotalFrete, item.getValorFrete());
        valorTotalDesconto = Biblioteca.soma(valorTotalDesconto, item.getValorDesconto());
        valorTotalProdutos = Biblioteca.soma(valorTotalProdutos, item.getValorSubtotal());

        valorTotalNF = Biblioteca.soma(valorTotalNF, item.getValorTotal());

        if (item.getNfeDetalheImpostoIcms() != null) {
            valorTotalBaseCalcIcms = Biblioteca.soma(valorTotalBaseCalcIcms, item.getNfeDetalheImpostoIcms().getBaseCalculoIcms());
            valorTotalIcms = Biblioteca.soma(valorTotalIcms, item.getNfeDetalheImpostoIcms().getValorIcms());
            valorTotalBaseCalcIcmsST = Biblioteca.soma(valorTotalBaseCalcIcmsST, item.getNfeDetalheImpostoIcms().getValorBaseCalculoIcmsSt());
            valorTotalIcmsST = Biblioteca.soma(valorTotalIcmsST, item.getNfeDetalheImpostoIcms().getValorIcmsSt());
        }
        if (item.getNfeDetalheImpostoIpi() != null) {
            valorTotalIpi = Biblioteca.soma(valorTotalIpi, item.getNfeDetalheImpostoIpi().getValorIpi());
            valorTotalNF = Biblioteca.soma(valorTotalNF, valorTotalIpi);
        }
        if (item.getNfeDetalheImpostoPis() != null) {
            valorTotalBaseCalcPis = Biblioteca.soma(valorTotalBaseCalcPis, item.getNfeDetalheImpostoPis().getValorBaseCalculoPis());
            valorTotalPis = Biblioteca.soma(valorTotalPis, item.getNfeDetalheImpostoPis().getValorPis());
        }
        if (item.getNfeDetalheImpostoPis() != null) {
            valorTotalBaseCalcCofins = Biblioteca.soma(valorTotalBaseCalcCofins, item.getNfeDetalheImpostoCofins().getBaseCalculoCofins());
            valorTotalCofins = Biblioteca.soma(valorTotalCofins, item.getNfeDetalheImpostoCofins().getValorCofins());
        }
    }

    private void cadastrarFornecedor(NfeEmitente emitente) throws Exception {
        Fornecedor fornecedor = fornecedorService.cadastrarFornecedor(emitente, empresa);
        getObjeto().setFornecedor(fornecedor);
    }


    // <editor-fold defaultstate="collapsed" desc="Crud Produto">
    public void incluiProduto() {
        tipoCstIcms = Integer.valueOf(empresa.getCrt());
        nfeDetalhe = new NfeDetalhe();

        nfeDetalhe.setNfeCabecalho(getObjeto());
        nfeDetalhe.setListaArmamento(new HashSet<>());
        nfeDetalhe.setListaMedicamento(new HashSet<>());
        nfeDetalhe.setListaDeclaracaoImportacao(new HashSet<>());

        nfeDetalheImpostoIcms = new NfeDetalheImpostoIcms();
        nfeDetalheImpostoIpi = new NfeDetalheImpostoIpi();
        nfeDetalheImpostoCofins = new NfeDetalheImpostoCofins();
        nfeDetalheImpostoPis = new NfeDetalheImpostoPis();

        nfeDetalhe.setNfeDetalheImpostoCofins(nfeDetalheImpostoCofins);
        nfeDetalhe.setNfeDetalheImpostoIcms(nfeDetalheImpostoIcms);
        nfeDetalhe.setNfeDetalheImpostoIpi(nfeDetalheImpostoIpi);
        nfeDetalhe.setNfeDetalheImpostoPis(nfeDetalheImpostoPis);

        nfeDetalheImpostoIcms.setNfeDetalhe(nfeDetalhe);
        nfeDetalheImpostoIcms.setBaseCalculoIcms(BigDecimal.ZERO);
        nfeDetalheImpostoIcms.setValorIcms(BigDecimal.ZERO);
        nfeDetalheImpostoIcms.setAliquotaIcms(BigDecimal.ZERO);
        nfeDetalheImpostoIcms.setValorBaseCalculoIcmsSt(BigDecimal.ZERO);
        nfeDetalheImpostoIcms.setValorIcmsSt(BigDecimal.ZERO);
        nfeDetalheImpostoIcms.setAliquotaIcmsSt(BigDecimal.ZERO);

        nfeDetalheImpostoIpi.setNfeDetalhe(nfeDetalhe);
        nfeDetalheImpostoIpi.setAliquotaIpi(BigDecimal.ZERO);
        nfeDetalheImpostoIpi.setEnquadramentoIpi("99");
        nfeDetalheImpostoIpi.setValorBaseCalculoIpi(BigDecimal.ZERO);
        nfeDetalheImpostoIpi.setValorIpi(BigDecimal.ZERO);

        nfeDetalheImpostoPis.setNfeDetalhe(nfeDetalhe);
        nfeDetalheImpostoPis.setAliquotaPisPercentual(BigDecimal.ZERO);
        nfeDetalheImpostoPis.setValorBaseCalculoPis(BigDecimal.ZERO);
        nfeDetalheImpostoPis.setValorPis(BigDecimal.ZERO);

        nfeDetalheImpostoCofins.setNfeDetalhe(nfeDetalhe);
        nfeDetalheImpostoCofins.setAliquotaCofinsPercentual(BigDecimal.ZERO);
        nfeDetalheImpostoCofins.setBaseCalculoCofins(BigDecimal.ZERO);
        nfeDetalheImpostoCofins.setValorCofins(BigDecimal.ZERO);

    }

    public void alteraProduto() {
        nfeDetalhe = nfeDetalheSelecionado;
        nfeDetalheImpostoIcms = nfeDetalhe.getNfeDetalheImpostoIcms();
        nfeDetalheImpostoIpi = nfeDetalhe.getNfeDetalheImpostoIpi();
        nfeDetalheImpostoCofins = nfeDetalhe.getNfeDetalheImpostoCofins();
        nfeDetalheImpostoPis = nfeDetalhe.getNfeDetalheImpostoPis();
    }

    public void excluirProduto() {

        getObjeto().getListaNfeDetalhe().remove(nfeDetalheSelecionado);
        iniciarValorsValidacao();
        getObjeto().getListaNfeDetalhe().stream().forEach(item -> {
            gerarValores(item);
        });
        Mensagem.addInfoMessage("Registro excluido!");

    }

    public void salvaProduto() {
        try {
            if (!getObjeto().getListaNfeDetalhe().contains(nfeDetalhe)) {
                nfeDetalhe.setValorSubtotal(nfeDetalhe.calcularSubTotalProduto());
                nfeDetalhe.setValorTotal(nfeDetalhe.calcularValorTotalProduto());
                nfeDetalhe.setCodigoProduto(nfeDetalhe.getProduto().getId().toString());
                nfeDetalhe.setNomeProduto(nfeDetalhe.getProduto().getNome());
                nfeDetalhe.setNcm(nfeDetalhe.getProduto().getNcm());
                nfeDetalhe.setUnidadeComercial(nfeDetalhe.getProduto().getUnidadeProduto().getSigla());
                nfeDetalhe.setValorBrutoProduto(nfeDetalhe.getValorUnitarioComercial());
                nfeDetalhe.setUnidadeTributavel(nfeDetalhe.getUnidadeComercial());
                nfeDetalhe.setValorUnitarioTributavel(nfeDetalhe.getValorUnitarioComercial());
                nfeDetalhe.setProdutoCadastrado(true);
                getObjeto().getListaNfeDetalhe().add(nfeDetalhe);
                gerarValores(nfeDetalhe);

                Mensagem.addInfoMessage("Registro incluído!");
            } else {
                Mensagem.addInfoMessage("Registro alterado!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao salvar o registro", e);
        }
    }

    public void cadastrarProduto() {
        produto = new Produto();
        nfeDetalhe = nfeDetalheSelecionado;
        produto.setGtin(nfeDetalhe.getGtin());
        produto.setDataCadastro(new Date());
        produto.setCustoUnitario(nfeDetalhe.getValorUnitarioComercial());
        produto.setDescricao(nfeDetalhe.getNomeProduto());
        produto.setNcm(nfeDetalhe.getNcm());
        produto.setNome(nfeDetalhe.getNomeProduto());
        produto.setValorCompra(nfeDetalhe.getValorUnitarioComercial());

    }

    public void exibirVinculoProduto() {
        produto = new Produto();
        nfeDetalhe = nfeDetalheSelecionado;
    }

    public void vincularProduto() {
        try {
            salvarNovoProduto();
            Mensagem.addInfoMessage("Produto vinculado com sucesso");
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("Erro ao vincular o produto", ex);
        }
    }

    public void salvarNovoProduto() {
        try {

            FornecedorProduto forProd = produtoFornecedorService.salvar(produto,fornecedor,empresa,nfeDetalhe.getValorUnitarioComercial(),nfeDetalhe.getCodigoProduto());
            nfeDetalhe.setProduto(forProd.getProduto());
            nfeDetalhe.setProdutoCadastrado(true);
            Mensagem.addInfoMessage("Produto cadastro e vinculado com sucesso");
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("Erro ao vincular o produto", ex);
        }
    }

    // </editor-fold>



    private void iniciarValorsValidacao() {
        valorTotalFrete = BigDecimal.ZERO;
        valorTotalDesconto = BigDecimal.ZERO;
        valorTotalProdutos = BigDecimal.ZERO;
        valorTotalBaseCalcIcms = BigDecimal.ZERO;
        valorTotalIcms = BigDecimal.ZERO;
        valorTotalBaseCalcIcmsST = BigDecimal.ZERO;
        valorTotalIcmsST = BigDecimal.ZERO;
        valorTotalBaseCalcPis = BigDecimal.ZERO;
        valorTotalPis = BigDecimal.ZERO;
        valorTotalBaseCalcCofins = BigDecimal.ZERO;
        valorTotalCofins = BigDecimal.ZERO;
        valorTotalIpi = BigDecimal.ZERO;
        valorTotalNF = BigDecimal.ZERO;

    }

    @Override
    public void doCreate() {
        setObjeto(entradaService.iniciar());
        getObjeto().setEmpresa(empresa);
    }

    @Override
    protected Class<NfeCabecalho> getClazz() {
        return NfeCabecalho.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "NFE_CABECALHO";
    }
}
