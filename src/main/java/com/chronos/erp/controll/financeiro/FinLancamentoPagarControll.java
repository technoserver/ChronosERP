package com.chronos.erp.controll.financeiro;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.controll.ERPLazyDataModel;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.util.Constants;
import com.chronos.erp.util.jsf.Mensagem;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by john on 15/08/17.
 */
@Named
@ViewScoped
public class FinLancamentoPagarControll extends AbstractControll<FinLancamentoPagar> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<FinDocumentoOrigem> documentos;
    @Inject
    private Repository<Fornecedor> fornecedores;
    @Inject
    private Repository<ContaCaixa> contas;
    @Inject
    private Repository<NaturezaFinanceira> naturezas;
    @Inject
    private Repository<AdmParametro> parametros;
    @Inject
    private Repository<FinStatusParcela> status;
    @Inject
    private Repository<FinPagamentoFixo> pagamentos;
    @Inject
    private Repository<FinParcelaPagar> parcelas;

    private List<FinLancamentoPagar> lancamentosSelecionados;
    private List<FinLancamentoPagar> lancamentosFiltrados;

    private FinParcelaPagar finParcelaPagar;
    private FinParcelaPagar finParcelaPagarSelecionado;

    private FinLctoPagarNtFinanceira finLctoPagarNtFinanceira;
    private FinLctoPagarNtFinanceira finLctoPagarNtFinanceiraSelecionado;

    //atributos utilizados para geração das parcelas
    private ContaCaixa contaCaixa;
    private NaturezaFinanceira naturezaFinanceira;

    private Integer idmepresaFiltro;
    private String fornecedor;


    @PostConstruct
    @Override
    public void init() {
        super.init();
        idmepresaFiltro = empresa.getId();
        pesquisarEmpresas();
    }

    @Override
    public ERPLazyDataModel<FinLancamentoPagar> getDataModel() {
        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setDao(dao);
            dataModel.setClazz(FinLancamentoPagar.class);
        }

        pesquisar();
        return dataModel;
    }

    public void pesquisar() {
        dataModel.getFiltros().clear();
        if (!StringUtils.isEmpty(fornecedor)) {
            dataModel.getFiltros().add(new Filtro("fornecedor.pessoa.nome", Filtro.LIKE, fornecedor));
        }

        idmepresaFiltro = idmepresaFiltro == null || idmepresaFiltro == 0 ? empresa.getId() : idmepresaFiltro;
        dataModel.addFiltro("empresa.id", idmepresaFiltro, Filtro.IGUAL);
    }

    @Override
    public void doCreate() {
        super.doCreate();

        getObjeto().setListaFinParcelaPagar(new ArrayList<>());
        getObjeto().setListaFinLctoPagarNtFinanceira(new HashSet<>());
        getObjeto().setEmpresa(empresa);
        getObjeto().setDataLancamento(new Date());
    }

    @Override
    public void doEdit() {
        super.doEdit();
        FinLancamentoPagar pagar = dataModel.getRowData(getObjeto().getId().toString());
        setObjeto(pagar);
    }

    @Override
    public void salvar() {

        try {
            if (getObjeto().getId() == null) {
                getObjeto().setValorAPagar(getObjeto().getValorTotal());
                gerarParcelas();
                geraNaturezaFinanceira();

            }
            getObjeto().setValorAPagar(getObjeto().getValorTotal());
            super.salvar();
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao salvar o registro!", e);

        }
    }

    public void gerarParcelas() throws Exception {

        FinStatusParcela statusParcela = Constants.FIN.STATUS_ABERTO;


        if (contaCaixa == null || contaCaixa.getId() == null) {
            throw new Exception("É necessário informar a conta caixa para previsão das parcelas.");
        }

        Date dataEmissão = new Date();
        Calendar primeiroVencimento = Calendar.getInstance();
        primeiroVencimento.setTime(getObjeto().getPrimeiroVencimento());
        BigDecimal valorParcela = getObjeto().getValorAPagar().divide(BigDecimal.valueOf(getObjeto().getQuantidadeParcela()), RoundingMode.HALF_DOWN);
        BigDecimal somaParcelas = BigDecimal.ZERO;
        BigDecimal residuo = BigDecimal.ZERO;
        for (int i = 0; i < getObjeto().getQuantidadeParcela(); i++) {
            FinParcelaPagar parcelaPagar = new FinParcelaPagar();
            parcelaPagar.setFinLancamentoPagar(getObjeto());
            parcelaPagar.setFinStatusParcela(statusParcela);
            parcelaPagar.setContaCaixa(contaCaixa);
            parcelaPagar.setNumeroParcela(i + 1);
            parcelaPagar.setDataEmissao(dataEmissão);
            if (i > 0) {
                primeiroVencimento.add(Calendar.DAY_OF_MONTH, getObjeto().getIntervaloEntreParcelas());
            }
            parcelaPagar.setDataVencimento(primeiroVencimento.getTime());
            parcelaPagar.setSofreRetencao(getObjeto().getFornecedor().getSofreRetencao());
            parcelaPagar.setValor(valorParcela);

            somaParcelas = somaParcelas.add(valorParcela);
            if (i == (getObjeto().getQuantidadeParcela() - 1)) {
                residuo = getObjeto().getValorAPagar().subtract(somaParcelas);
                valorParcela = valorParcela.add(residuo);
                parcelaPagar.setValor(valorParcela);
            }
            getObjeto().getListaFinParcelaPagar().add(parcelaPagar);
        }
    }

    private void geraNaturezaFinanceira() {
        FinLctoPagarNtFinanceira finLctoPagarNaturezaFinancaeira = new FinLctoPagarNtFinanceira();
        finLctoPagarNaturezaFinancaeira.setFinLancamentoPagar(getObjeto());
        finLctoPagarNaturezaFinancaeira.setNaturezaFinanceira(naturezaFinanceira);
        finLctoPagarNaturezaFinancaeira.setDataInclusao(new Date());
        finLctoPagarNaturezaFinancaeira.setValor(getObjeto().getValorAPagar());

        getObjeto().getListaFinLctoPagarNtFinanceira().add(finLctoPagarNaturezaFinancaeira);
    }

    public void geraPagamentoFixo() {
        try {
            FinPagamentoFixo pagamentoFixo = new FinPagamentoFixo();
            pagamentoFixo.setFornecedor(getObjeto().getFornecedor());
            pagamentoFixo.setFinDocumentoOrigem(getObjeto().getFinDocumentoOrigem());
            pagamentoFixo.setPagamentoCompartilhado(getObjeto().getPagamentoCompartilhado());
            pagamentoFixo.setQuantidadeParcela(getObjeto().getQuantidadeParcela());
            pagamentoFixo.setValorTotal(getObjeto().getValorTotal());
            pagamentoFixo.setValorAPagar(getObjeto().getValorAPagar());
            pagamentoFixo.setDataLancamento(getObjeto().getDataLancamento());
            pagamentoFixo.setNumeroDocumento(getObjeto().getNumeroDocumento());
            pagamentoFixo.setPrimeiroVencimento(getObjeto().getPrimeiroVencimento());
            pagamentoFixo.setIntervaloEntreParcelas(getObjeto().getIntervaloEntreParcelas());
            pagamentoFixo.setImagemDocumento(getObjeto().getImagemDocumento());

            pagamentos.salvar(pagamentoFixo);
            Mensagem.addInfoMessage("Pagamento fixo/recorrente gerado com sucesso.");

        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao salvar o registro!", ex);
        }
    }

    public void mesclarLancamentos() {
        try {
            if (lancamentosSelecionados.size() <= 1) {
                Mensagem.addInfoMessage("Necessário selecionar 2 ou mais lançamentos!");

            } else if (lancamentosSelecionados.size() > 1) {
                BigDecimal valorTotal = BigDecimal.ZERO;
                int quantidadeParcelas = 0;
                for (FinLancamentoPagar l : lancamentosSelecionados) {
                    if (l.getMescladoPara() != null) {
                        throw new Exception("Lançamento selecionado já mesclado: " + l.getId());
                    }
                    if (l.getValorTotal() != null) {
                        valorTotal = valorTotal.add(l.getValorTotal());
                    }
                    quantidadeParcelas += l.getQuantidadeParcela();
                }

                List<Filtro> filtros = new ArrayList<>();
                filtros.add(new Filtro(Filtro.AND, "finLancamentoPagar", " IN ", lancamentosSelecionados));

                List<FinParcelaPagar> listParcelas = parcelas.getEntitys(FinParcelaPagar.class, filtros);

                FinLancamentoPagar lancamentoMesclado = new FinLancamentoPagar();
                lancamentoMesclado.setFornecedor(lancamentosSelecionados.get(0).getFornecedor());
                lancamentoMesclado.setFinDocumentoOrigem(lancamentosSelecionados.get(0).getFinDocumentoOrigem());
                lancamentoMesclado.setDataLancamento(lancamentosSelecionados.get(0).getDataLancamento());
                lancamentoMesclado.setIntervaloEntreParcelas(lancamentosSelecionados.get(0).getIntervaloEntreParcelas());
                lancamentoMesclado.setNumeroDocumento(lancamentosSelecionados.get(0).getNumeroDocumento());
                lancamentoMesclado.setPagamentoCompartilhado(lancamentosSelecionados.get(0).getPagamentoCompartilhado());
                lancamentoMesclado.setPrimeiroVencimento(lancamentosSelecionados.get(0).getPrimeiroVencimento());
                lancamentoMesclado.setQuantidadeParcela(quantidadeParcelas);
                lancamentoMesclado.setValorAPagar(valorTotal);
                lancamentoMesclado.setValorTotal(valorTotal);

                for (FinParcelaPagar p : listParcelas) {
                    p.setId(null);
                    p.setFinLancamentoPagar(lancamentoMesclado);
                    p.setListaFinParcelaPagamento(null);
                }
                lancamentoMesclado.setListaFinParcelaPagar(new ArrayList<>());

                dao.salvar(lancamentoMesclado);

                for (FinLancamentoPagar l : lancamentosSelecionados) {
                    l.setMescladoPara(lancamentoMesclado.getId());
                    l = dao.atualizar(l);
                }
                Mensagem.addInfoMessage("Lançamentos Mesclados!");

            }
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao mesclar os lançamentos!", e);

        }
    }

    public void incluirFinParcelaPagar() {
        finParcelaPagar = new FinParcelaPagar();
        finParcelaPagar.setFinLancamentoPagar(getObjeto());
    }

    public void alterarFinParcelaPagar() {
        finParcelaPagar = finParcelaPagarSelecionado;
    }

    public void salvarFinParcelaPagar() {

        salvar("Registro salvo com sucesso!");
    }

    public void excluirFinParcelaPagar() {

        getObjeto().getListaFinParcelaPagar().remove(finParcelaPagarSelecionado);
        salvar("Registro excluído com sucesso!");

    }

    public void incluirFinLctoPagarNtFinanceira() {
        finLctoPagarNtFinanceira = new FinLctoPagarNtFinanceira();
        finLctoPagarNtFinanceira.setFinLancamentoPagar(getObjeto());
    }

    public void alterarFinLctoPagarNtFinanceira() {
        finLctoPagarNtFinanceira = finLctoPagarNtFinanceiraSelecionado;
    }

    public void salvarFinLctoPagarNtFinanceira() {
        if (finLctoPagarNtFinanceira.getId() == null) {
            getObjeto().getListaFinLctoPagarNtFinanceira().add(finLctoPagarNtFinanceira);
        }
        salvar("Registro salvo com sucesso!");
    }

    public void excluirFinLctoPagarNtFinanceira() {

        getObjeto().getListaFinLctoPagarNtFinanceira().remove(finLctoPagarNtFinanceiraSelecionado);
        salvar("Registro excluído com sucesso!");

    }

    public List<FinDocumentoOrigem> getListaFinDocumentoOrigem(String nome) {
        List<FinDocumentoOrigem> listaFinDocumentoOrigem = new ArrayList<>();
        try {
            atributos = new Object[]{"descricao"};
            listaFinDocumentoOrigem = documentos.getEntitys(FinDocumentoOrigem.class, "siglaDocumento", nome, atributos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaFinDocumentoOrigem;
    }

    public List<Fornecedor> getListaFornecedor(String nome) {
        List<Fornecedor> listaFornecedor = new ArrayList<>();
        try {
            atributos = new Object[]{"pessoa.nome"};
            listaFornecedor = fornecedores.getEntitys(Fornecedor.class, "pessoa.nome", nome, atributos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaFornecedor;
    }

    public List<ContaCaixa> getListaContaCaixa(String nome) {
        List<ContaCaixa> listaContaCaixa = new ArrayList<>();
        try {
            atributos = new Object[]{"nome"};
            listaContaCaixa = contas.getEntitys(ContaCaixa.class, "descricao", nome, atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaContaCaixa;
    }

    public List<NaturezaFinanceira> getListaNaturezaFinanceira(String nome) {
        List<NaturezaFinanceira> listaNaturezaFinanceira = new ArrayList<>();
        try {
            atributos = new Object[]{"descricao"};
            listaNaturezaFinanceira = naturezas.getEntitys(NaturezaFinanceira.class, "descricao", nome, atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaNaturezaFinanceira;
    }

    @Override
    protected Class<FinLancamentoPagar> getClazz() {
        return FinLancamentoPagar.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "LANCAMENTO_PAGAR";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public FinParcelaPagar getFinParcelaPagar() {
        return finParcelaPagar;
    }

    public void setFinParcelaPagar(FinParcelaPagar finParcelaPagar) {
        this.finParcelaPagar = finParcelaPagar;
    }

    public FinLctoPagarNtFinanceira getFinLctoPagarNtFinanceira() {
        return finLctoPagarNtFinanceira;
    }

    public void setFinLctoPagarNtFinanceira(FinLctoPagarNtFinanceira finLctoPagarNtFinanceira) {
        this.finLctoPagarNtFinanceira = finLctoPagarNtFinanceira;
    }

    public List<FinLancamentoPagar> getLancamentosSelecionados() {
        return lancamentosSelecionados;
    }

    public void setLancamentosSelecionados(List<FinLancamentoPagar> lancamentosSelecionados) {
        this.lancamentosSelecionados = lancamentosSelecionados;
    }

    public ContaCaixa getContaCaixa() {
        return contaCaixa;
    }

    public void setContaCaixa(ContaCaixa contaCaixa) {
        this.contaCaixa = contaCaixa;
    }

    public NaturezaFinanceira getNaturezaFinanceira() {
        return naturezaFinanceira;
    }

    public void setNaturezaFinanceira(NaturezaFinanceira naturezaFinanceira) {
        this.naturezaFinanceira = naturezaFinanceira;
    }

    public FinParcelaPagar getFinParcelaPagarSelecionado() {
        return finParcelaPagarSelecionado;
    }

    public void setFinParcelaPagarSelecionado(FinParcelaPagar finParcelaPagarSelecionado) {
        this.finParcelaPagarSelecionado = finParcelaPagarSelecionado;
    }

    public FinLctoPagarNtFinanceira getFinLctoPagarNtFinanceiraSelecionado() {
        return finLctoPagarNtFinanceiraSelecionado;
    }

    public void setFinLctoPagarNtFinanceiraSelecionado(FinLctoPagarNtFinanceira finLctoPagarNtFinanceiraSelecionado) {
        this.finLctoPagarNtFinanceiraSelecionado = finLctoPagarNtFinanceiraSelecionado;
    }

    public List<FinLancamentoPagar> getLancamentosFiltrados() {
        return lancamentosFiltrados;
    }

    public void setLancamentosFiltrados(List<FinLancamentoPagar> lancamentosFiltrados) {
        this.lancamentosFiltrados = lancamentosFiltrados;
    }

    public Integer getIdmepresaFiltro() {
        return idmepresaFiltro;
    }

    public void setIdmepresaFiltro(Integer idmepresaFiltro) {
        this.idmepresaFiltro = idmepresaFiltro;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }
}
