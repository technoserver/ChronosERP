package com.chronos.modelo.entidades;

import com.chronos.modelo.anotacoes.TaxaMaior;
import com.chronos.modelo.enuns.SituacaoVenda;
import com.chronos.modelo.enuns.TipoFrete;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

@Entity
@Table(name = "VENDA_CABECALHO")
@NamedQuery(name = "VendaCabecalho.UpdateSituacao", query = "UPDATE VendaCabecalho v SET v.situacao = ?1 where v.id = ?2")
@DynamicUpdate
public class VendaCabecalho implements Serializable {

    private static final long serialVersionUID = 2L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_VENDA")
    @NotNull
    private Date dataVenda;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_SAIDA")
    private Date dataSaida;
    @Column(name = "HORA_SAIDA")
    private String horaSaida;
    @Column(name = "NUMERO_FATURA")
    private Integer numeroFatura;
    @Column(name = "LOCAL_ENTREGA")
    private String localEntrega;
    @Column(name = "LOCAL_COBRANCA")
    private String localCobranca;
    @Column(name = "VALOR_SUBTOTAL")
    private BigDecimal valorSubtotal;
    @Column(name = "TAXA_COMISSAO")
    private BigDecimal taxaComissao;
    @Column(name = "VALOR_COMISSAO")
    private BigDecimal valorComissao;
    @Column(name = "TAXA_DESCONTO")
    @TaxaMaior()
    private BigDecimal taxaDesconto;
    @Column(name = "VALOR_DESCONTO")
    private BigDecimal valorDesconto;
    @Column(name = "VALOR_TOTAL")
    private BigDecimal valorTotal;
    @NotNull
    @Column(name = "TIPO_FRETE")
    private String tipoFrete;
    @NotNull
    @Column(name = "FORMA_PAGAMENTO")
    private String formaPagamento;
    @Column(name = "VALOR_FRETE")
    private BigDecimal valorFrete;
    @Column(name = "VALOR_SEGURO")
    private BigDecimal valorSeguro;
    @Column(name = "OBSERVACAO")
    private String observacao;
    @NotNull
    @Column(name = "SITUACAO")
    private String situacao;
    @JoinColumn(name = "ID_VENDA_ORCAMENTO_CABECALHO", referencedColumnName = "ID")
    @ManyToOne
    private VendaOrcamentoCabecalho vendaOrcamentoCabecalho;
    @JoinColumn(name = "ID_VENDA_CONDICOES_PAGAMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private VendaCondicoesPagamento condicoesPagamento;
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private Cliente cliente;
    @JoinColumn(name = "ID_VENDEDOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private Vendedor vendedor;
    @JoinColumn(name = "ID_TIPO_NOTA_FISCAL", referencedColumnName = "ID")
    @ManyToOne(optional = true)
    private NotaFiscalTipo notaFiscalTipo;
    @JoinColumn(name = "ID_TRANSPORTADORA", referencedColumnName = "ID")
    @ManyToOne
    private Transportadora transportadora;
    @JoinColumn(name = "ID_VENDA_ROMANEIO_ENTREGA", referencedColumnName = "ID")
    @ManyToOne
    private VendaRomaneioEntrega vendaRomaneioEntrega;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empresa empresa;
    @OneToMany(mappedBy = "vendaCabecalho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VendaDetalhe> listaVendaDetalhe;
    @Transient
    private boolean excludoItem;

    public VendaCabecalho() {
        this.listaVendaDetalhe = new ArrayList<>();
        this.dataVenda = new Date();
        this.situacao = SituacaoVenda.Digitacao.getCodigo();
        this.tipoFrete = TipoFrete.CIF.getCodigo();
    }

    public VendaCabecalho(Integer id) {
        this.id = id;
    }

    public VendaCabecalho(Integer id, Date dataVenda, Integer numeroFatura, BigDecimal valorTotal, String situacao,String cliente) {
        this.id = id;
        this.dataVenda = dataVenda;
        this.numeroFatura = numeroFatura;
        this.valorTotal = valorTotal;
        this.situacao = situacao;
        this.cliente = new Cliente(0,cliente);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    public Date getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(Date dataSaida) {
        this.dataSaida = dataSaida;
    }

    public String getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(String horaSaida) {
        this.horaSaida = horaSaida;
    }

    public Integer getNumeroFatura() {
        return numeroFatura;
    }

    public void setNumeroFatura(Integer numeroFatura) {
        this.numeroFatura = numeroFatura;
    }

    public String getLocalEntrega() {
        return localEntrega;
    }

    public void setLocalEntrega(String localEntrega) {
        this.localEntrega = localEntrega;
    }

    public String getLocalCobranca() {
        return localCobranca;
    }

    public void setLocalCobranca(String localCobranca) {
        this.localCobranca = localCobranca;
    }

    public BigDecimal getValorSubtotal() {
        return valorSubtotal;
    }

    public void setValorSubtotal(BigDecimal valorSubtotal) {
        this.valorSubtotal = valorSubtotal;
    }

    public BigDecimal getTaxaComissao() {
        return Optional.ofNullable(taxaComissao).orElse(BigDecimal.ZERO);
    }

    public void setTaxaComissao(BigDecimal taxaComissao) {
        this.taxaComissao = taxaComissao;
    }

    public BigDecimal getValorComissao() {
        valorComissao = getTaxaComissao().multiply(getValorTotal()).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
        return valorComissao;
    }

    public void setValorComissao(BigDecimal valorComissao) {
        this.valorComissao = valorComissao;
    }

    public BigDecimal getTaxaDesconto() {
        return taxaDesconto;
    }

    public void setTaxaDesconto(BigDecimal taxaDesconto) {
        this.taxaDesconto = taxaDesconto;
    }

    public BigDecimal getValorDesconto() {
        return Optional.ofNullable(valorDesconto).orElse(BigDecimal.ZERO);
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    /**
     * C=CIF | F=FOB
     *
     * @return
     */
    public String getTipoFrete() {
        return tipoFrete;
    }

    /**
     * C=CIF | F=FOB
     *
     * @param tipoFrete
     */
    public void setTipoFrete(String tipoFrete) {
        this.tipoFrete = tipoFrete;
    }

    /**
     * 0=pagamento à vista | 1=pagamento à prazo | 2=outros. (Campo indPag da NF-e)
     *
     * @return
     */
    public String getFormaPagamento() {
        return formaPagamento;
    }

    /**
     * 0=pagamento à vista | 1=pagamento à prazo | 2=outros. (Campo indPag da NF-e)
     *
     * @param formaPagamento
     */
    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public BigDecimal getValorFrete() {
        return Optional.ofNullable(valorFrete).orElse(BigDecimal.ZERO);
    }

    public void setValorFrete(BigDecimal valorFrete) {
        this.valorFrete = valorFrete;
    }

    public BigDecimal getValorSeguro() {
        return Optional.ofNullable(valorSeguro).orElse(BigDecimal.ZERO);
    }

    public void setValorSeguro(BigDecimal valorSeguro) {
        this.valorSeguro = valorSeguro;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    /**
     * D=Digitacao | P=Producao | X=Expedicao | F=Faturado | E=Entregue | V=Devolução N =Nota Fiscal
     *
     * @return
     */
    public String getSituacao() {
        return situacao;
    }

    /**
     * D=Digitacao | P=Producao | X=Expedicao | F=Faturado | E=Entregue | V=Devolução |N =Nota Fiscal
     *
     * @param situacao
     */
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public VendaOrcamentoCabecalho getVendaOrcamentoCabecalho() {
        return vendaOrcamentoCabecalho;
    }

    public void setVendaOrcamentoCabecalho(VendaOrcamentoCabecalho vendaOrcamentoCabecalho) {
        this.vendaOrcamentoCabecalho = vendaOrcamentoCabecalho;
    }

    public VendaCondicoesPagamento getCondicoesPagamento() {
        return condicoesPagamento;
    }

    public void setCondicoesPagamento(VendaCondicoesPagamento condicoesPagamento) {
        this.condicoesPagamento = condicoesPagamento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public NotaFiscalTipo getNotaFiscalTipo() {
        return notaFiscalTipo;
    }

    public void setNotaFiscalTipo(NotaFiscalTipo notaFiscalTipo) {
        this.notaFiscalTipo = notaFiscalTipo;
    }

    public Transportadora getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(Transportadora transportadora) {
        this.transportadora = transportadora;
    }

    public VendaRomaneioEntrega getVendaRomaneioEntrega() {
        return vendaRomaneioEntrega;
    }

    public void setVendaRomaneioEntrega(VendaRomaneioEntrega vendaRomaneioEntrega) {
        this.vendaRomaneioEntrega = vendaRomaneioEntrega;
    }

    public List<VendaDetalhe> getListaVendaDetalhe() {
        return Optional.ofNullable(listaVendaDetalhe).orElse(new ArrayList<>());
    }

    public void setListaVendaDetalhe(List<VendaDetalhe> listaVendaDetalhe) {
        this.listaVendaDetalhe = listaVendaDetalhe;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public boolean isExcludoItem() {
        return excludoItem;
    }

    public void setExcludoItem(boolean excludoItem) {
        this.excludoItem = excludoItem;
    }

    public boolean isEncerrado() {
        return situacao != null && situacao.equals("E");
    }

    public boolean isFaturado() {
        return situacao != null && situacao.equals("F");
    }

    public boolean isCancelado() {
        return situacao != null && situacao.equals("C");
    }


    public BigDecimal calcularTotalDesconto() {
        valorDesconto = getListaVendaDetalhe().stream()
                .map(VendaDetalhe::getValorDesconto)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        return valorDesconto;
    }


    public BigDecimal calcularValorProdutos() {
        valorSubtotal = getListaVendaDetalhe().stream()
                .map(VendaDetalhe::getValorSubtotal)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        return valorSubtotal;
    }

    public BigDecimal calcularValorTotal() {
        valorTotal = calcularValorProdutos();
        valorTotal = valorTotal.add(getValorFrete())
                .add(getValorSeguro())
                .subtract(calcularTotalDesconto());
        return valorTotal;
    }

    public BigDecimal calcularValorDevolucao() {

        return getListaVendaDetalhe().stream()
                .map(VendaDetalhe::getValorTotalDevolvido)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public String valorSubTotalFormatado() {
        return formatarValor(calcularValorProdutos());
    }

    public String valorDescontoFormatado() {
        return formatarValor(calcularTotalDesconto());
    }

    public String valorTotalFormatado() {
        return formatarValor(calcularValorTotal());
    }

    private String formatarValor(BigDecimal valor) {
        DecimalFormatSymbols simboloDecimal = DecimalFormatSymbols.getInstance();
        simboloDecimal.setDecimalSeparator('.');
        DecimalFormat formatar = new DecimalFormat("0.00", simboloDecimal);

        return formatar.format(Optional.ofNullable(valor).orElse(BigDecimal.ZERO));
    }

    public boolean temProduto() {
        return getListaVendaDetalhe().size() > 0;
    }

    @Transient
    public boolean isPodeExcluir() {
        return situacao.equals("D");
    }

    @Transient
    public boolean isPodeCancelar() {
        boolean podeCacelar = situacao.equals("E");
        return podeCacelar;
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VendaCabecalho other = (VendaCabecalho) obj;
        return Objects.equals(this.id, other.id);
    }

}
