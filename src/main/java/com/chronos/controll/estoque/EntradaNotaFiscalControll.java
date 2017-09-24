package com.chronos.controll.estoque;

import com.chronos.bo.nfe.ImportaXMLNFe;
import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.*;
import com.chronos.repository.Filtro;
import com.chronos.service.cadastros.FornecedorService;
import com.chronos.service.cadastros.ProdutoFornecedorService;
import com.chronos.service.estoque.EntradaNotaFiscalService;
import com.chronos.util.Biblioteca;
import com.chronos.util.jsf.Mensagem;
import org.apache.commons.io.FileUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

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
    private NfeDuplicata duplicata;
    private NfeDuplicata duplicataSelecionada;

    private Produto produto;
    private Fornecedor fornecedor;
    private VendaCondicoesPagamento condicao;


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

    @Override
    public void doCreate() {
        setObjeto(entradaService.iniciar(empresa));
        setTelaGrid(false);
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
            valorTotalNF = Biblioteca.soma(valorTotalNF, item.getNfeDetalheImpostoIcms().getValorIcmsSt());
        }
        if (item.getNfeDetalheImpostoIpi() != null) {
            valorTotalIpi = Biblioteca.soma(valorTotalIpi, item.getNfeDetalheImpostoIpi().getValorIpi());
            valorTotalNF = Biblioteca.soma(valorTotalNF, item.getNfeDetalheImpostoIpi().getValorIpi());
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
        fornecedor = fornecedorService.cadastrarFornecedor(emitente, empresa);
        getObjeto().setFornecedor(fornecedor);
    }


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


    // <editor-fold defaultstate="collapsed" desc="Procedimentos para importação do xml">

    public void importaXML(FileUploadEvent event) {
        try {
            UploadedFile arquivoUpload = event.getFile();

            File file = File.createTempFile("nfe", ".xml");
            FileUtils.copyInputStreamToFile(arquivoUpload.getInputstream(), file);
            iniciarValorsValidacao();
            if (file != null) {
                ImportaXMLNFe importaXml = new ImportaXMLNFe();
                Map map = importaXml.importarXmlNFe(file);
                // tipoDoc = "NFE";
                setObjeto((NfeCabecalho) map.get("cabecalho"));
                getObjeto().setEmpresa(empresa);
                getObjeto().setEmitente((NfeEmitente) map.get("emitente"));

                getObjeto().setListaNfeDetalhe((ArrayList) map.get("detalhe"));

                getObjeto().setListaDuplicata((HashSet) map.get("duplicata"));
                verificarFornecedor();
                List<Filtro> filtro = new LinkedList<>();
                filtro.add(new Filtro(Filtro.AND, "fornecedor.id", Filtro.IGUAL, fornecedor.getId()));
                filtro.add(new Filtro(Filtro.AND, "numero", Filtro.IGUAL, getObjeto().getNumero()));
                NfeCabecalho nfe = dao.get(NfeCabecalho.class, filtro);
                if (nfe != null) {
                    doCreate();
                    throw new Exception("Essa nota já foi  digitada pra esse fornecedor !");
                }
                verificaProdutoNaoCadastrado(true);

                Mensagem.addInfoMessage("XML importados com sucesso!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }
    }


    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Crud Fornecedor">

    private void verificarFornecedor() throws Exception {


        String cpfCnpj = getObjeto().getEmitente().getCpfCnpj();
        fornecedor = fornecedorService.getFornecedor(cpfCnpj);
        if (fornecedor == null) {
            fornecedor = fornecedorService.getFornecedor(cpfCnpj);
            cadastrarFornecedor(getObjeto().getEmitente());
            getObjeto().setFornecedor(fornecedor);
        } else {
            getObjeto().setFornecedor(fornecedor);
        }

    }

    // </editor-fold>

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

    private boolean verificaProdutoNaoCadastrado(boolean importacao) {
        boolean produtoNaoCadastrado = false;

        try {
            if (importacao) {
                for (NfeDetalhe d : getObjeto().getListaNfeDetalhe()) {
                    boolean existe = produtoFornecedorService.existeProduto(d.getCodigoProduto());
                    if (existe) {
                        Produto produto = produtoFornecedorService.getProduto(d.getCodigoProduto());
                        d.setProduto(produto);
                        d.setProdutoCadastrado(true);
                    } else {
                        d.setProdutoCadastrado(false);
                    }

                    gerarValores(d);

                }
            } else {
                for (NfeDetalhe item : getObjeto().getListaNfeDetalhe()) {
                    int id = item.getProduto() == null || item.getProduto().getId() == null ? 0 : item.getProduto().getId();
                    produtoNaoCadastrado = true;//!produtoDao.existeRegisro(Produto.class, "id", id);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao buscar os dados do produto.", e);
        }
        return produtoNaoCadastrado;
    }

    // </editor-fold>








    private Map getCodigoEnquadramentoIPI(){
        Map<String,String> map = new HashMap<>();

        return map;
    }
    @Override
    protected Class<NfeCabecalho> getClazz() {
        return NfeCabecalho.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "NFE_CABECALHO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public VendaCondicoesPagamento getCondicao() {
        return condicao;
    }

    public void setCondicao(VendaCondicoesPagamento condicao) {
        this.condicao = condicao;
    }

    public NfeDuplicata getDuplicata() {
        return duplicata;
    }

    public void setDuplicata(NfeDuplicata duplicata) {
        this.duplicata = duplicata;
    }

    public NfeDuplicata getDuplicataSelecionada() {
        return duplicataSelecionada;
    }

    public void setDuplicataSelecionada(NfeDuplicata duplicataSelecionada) {
        this.duplicataSelecionada = duplicataSelecionada;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public BigDecimal getValorTotalFrete() {
        return valorTotalFrete;
    }

    public void setValorTotalFrete(BigDecimal valorTotalFrete) {
        this.valorTotalFrete = valorTotalFrete;
    }

    public BigDecimal getValorTotalDesconto() {
        return valorTotalDesconto;
    }

    public void setValorTotalDesconto(BigDecimal valorTotalDesconto) {
        this.valorTotalDesconto = valorTotalDesconto;
    }

    public BigDecimal getValorTotalProdutos() {
        return valorTotalProdutos;
    }

    public void setValorTotalProdutos(BigDecimal valorTotalProdutos) {
        this.valorTotalProdutos = valorTotalProdutos;
    }

    public BigDecimal getValorTotalBaseCalcIcms() {
        return valorTotalBaseCalcIcms;
    }

    public void setValorTotalBaseCalcIcms(BigDecimal valorTotalBaseCalcIcms) {
        this.valorTotalBaseCalcIcms = valorTotalBaseCalcIcms;
    }

    public BigDecimal getValorTotalIcms() {
        return valorTotalIcms;
    }

    public void setValorTotalIcms(BigDecimal valorTotalIcms) {
        this.valorTotalIcms = valorTotalIcms;
    }

    public BigDecimal getValorTotalBaseCalcIcmsST() {
        return valorTotalBaseCalcIcmsST;
    }

    public void setValorTotalBaseCalcIcmsST(BigDecimal valorTotalBaseCalcIcmsST) {
        this.valorTotalBaseCalcIcmsST = valorTotalBaseCalcIcmsST;
    }

    public BigDecimal getValorTotalIcmsST() {
        return valorTotalIcmsST;
    }

    public void setValorTotalIcmsST(BigDecimal valorTotalIcmsST) {
        this.valorTotalIcmsST = valorTotalIcmsST;
    }

    public BigDecimal getValorTotalBaseCalcPis() {
        return valorTotalBaseCalcPis;
    }

    public void setValorTotalBaseCalcPis(BigDecimal valorTotalBaseCalcPis) {
        this.valorTotalBaseCalcPis = valorTotalBaseCalcPis;
    }

    public BigDecimal getValorTotalPis() {
        return valorTotalPis;
    }

    public void setValorTotalPis(BigDecimal valorTotalPis) {
        this.valorTotalPis = valorTotalPis;
    }

    public BigDecimal getValorTotalBaseCalcCofins() {
        return valorTotalBaseCalcCofins;
    }

    public void setValorTotalBaseCalcCofins(BigDecimal valorTotalBaseCalcCofins) {
        this.valorTotalBaseCalcCofins = valorTotalBaseCalcCofins;
    }

    public BigDecimal getValorTotalCofins() {
        return valorTotalCofins;
    }

    public void setValorTotalCofins(BigDecimal valorTotalCofins) {
        this.valorTotalCofins = valorTotalCofins;
    }

    public BigDecimal getValorTotalIpi() {
        return valorTotalIpi;
    }

    public void setValorTotalIpi(BigDecimal valorTotalIpi) {
        this.valorTotalIpi = valorTotalIpi;
    }

    public BigDecimal getValorTotalNF() {
        return valorTotalNF;
    }

    public void setValorTotalNF(BigDecimal valorTotalNF) {
        this.valorTotalNF = valorTotalNF;
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

    public NfeDetalhe getNfeDetalhe() {
        return nfeDetalhe;
    }

    public void setNfeDetalhe(NfeDetalhe nfeDetalhe) {
        this.nfeDetalhe = nfeDetalhe;
    }

    public NfeDetalhe getNfeDetalheSelecionado() {
        return nfeDetalheSelecionado;
    }

    public void setNfeDetalheSelecionado(NfeDetalhe nfeDetalheSelecionado) {
        this.nfeDetalheSelecionado = nfeDetalheSelecionado;
    }

    public int getTipoCstIcms() {
        return tipoCstIcms;
    }

    public void setTipoCstIcms(int tipoCstIcms) {
        this.tipoCstIcms = tipoCstIcms;
    }
}
