package com.chronos.erp.modelo.entidades;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "ORCAMENTO_CABECALHO")
@DynamicUpdate
public class OrcamentoCabecalho implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @NotNull
    @Column(name = "TIPO")
    private String tipo;
    @Column(name = "CODIGO")
    private String codigo;
    @NotNull(message = "teste")
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_CADASTRO")
    private Date dataCadastro;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_ENTREGA")
    private Date dataEntrega;
    @Temporal(TemporalType.DATE)
    @Column(name = "VALIDADE")
    private Date validade;
    @Column(name = "TIPO_FRETE")
    private String tipoFrete;
    @Column(name = "VALOR_SUBTOTAL")
    @NotNull
    private BigDecimal valorSubtotal;
    @Column(name = "VALOR_FRETE")
    private BigDecimal valorFrete;
    @Column(name = "TAXA_COMISSAO")
    @DecimalMax(value = "100.0", message = "O deve deve ser igual ou menor que 100")
    private BigDecimal taxaComissao;
    @Column(name = "VALOR_COMISSAO")
    private BigDecimal valorComissao;
    @Column(name = "TAXA_DESCONTO")
    @DecimalMax(value = "100.0", message = "O deve deve ser igual ou menor que 100")
    private BigDecimal taxaDesconto;
    @Column(name = "VALOR_DESCONTO")
    private BigDecimal valorDesconto;
    @Column(name = "VALOR_TOTAL")
    @NotNull
    private BigDecimal valorTotal;
    @Column(name = "OBSERVACAO")
    private String observacao;
    @NotNull
    @NotBlank
    @Column(name = "SITUACAO")
    private String situacao;
    @JoinColumn(name = "ID_TRANSPORTADORA", referencedColumnName = "ID")
    @ManyToOne
    private Transportadora transportadora;
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private Cliente cliente;
    @JoinColumn(name = "ID_VENDEDOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private Vendedor vendedor;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orcamentoCabecalho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrcamentoDetalhe> listaOrcamentoDetalhe;
    @OneToMany(mappedBy = "orcamentoCabecalho", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<OrcamentoFormaPagamento> listaFormaPagamento;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private Empresa empresa;


    @Transient
    private CondicoesPagamento condicoesPagamento;

    public OrcamentoCabecalho() {

        this.valorComissao = BigDecimal.ZERO;
        this.valorDesconto = BigDecimal.ZERO;
        this.valorFrete = BigDecimal.ZERO;
        this.valorSubtotal = BigDecimal.ZERO;
        this.valorTotal = BigDecimal.ZERO;
        this.listaFormaPagamento = new HashSet<>();
        this.listaOrcamentoDetalhe = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * O=Orçamento | P=Pedido
     *
     * @return
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * O=Orçamento | P=Pedido
     *
     * @param tipo
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Código atribuído pelo usuário
     *
     * @return
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Código atribuído pelo usuário
     *
     * @param codigo
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Date getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(Date dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public Date getValidade() {
        return validade;
    }

    public void setValidade(Date validade) {
        this.validade = validade;
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

    public BigDecimal getValorSubtotal() {
        return valorSubtotal;
    }

    public void setValorSubtotal(BigDecimal valorSubtotal) {
        this.valorSubtotal = valorSubtotal;
    }

    public BigDecimal getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(BigDecimal valorFrete) {
        this.valorFrete = valorFrete;
    }

    public BigDecimal getTaxaComissao() {
        return taxaComissao;
    }

    public void setTaxaComissao(BigDecimal taxaComissao) {
        this.taxaComissao = taxaComissao;
    }

    public BigDecimal getValorComissao() {
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
        return valorDesconto;
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

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    /**
     * D=Digitacao | P=Producao | X=Expedicao | F=Faturado | E=Entregue
     *
     * @return
     */
    public String getSituacao() {
        return situacao;
    }

    /**
     * D=Digitacao | P=Producao | X=Expedicao | F=Faturado | E=Entregue
     *
     * @param situacao
     */
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public CondicoesPagamento getCondicoesPagamento() {
        return condicoesPagamento;
    }

    public void setCondicoesPagamento(CondicoesPagamento condicoesPagamento) {
        this.condicoesPagamento = condicoesPagamento;
    }

    public Transportadora getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(Transportadora transportadora) {
        this.transportadora = transportadora;
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

    public List<OrcamentoDetalhe> getListaOrcamentoDetalhe() {
        return Optional.ofNullable(listaOrcamentoDetalhe).orElse(new ArrayList<>());
    }

    public void setListaOrcamentoDetalhe(List<OrcamentoDetalhe> listaOrcamentoDetalhe) {
        this.listaOrcamentoDetalhe = listaOrcamentoDetalhe;
    }

    public Set<OrcamentoFormaPagamento> getListaFormaPagamento() {
        return listaFormaPagamento;
    }

    public void setListaFormaPagamento(Set<OrcamentoFormaPagamento> listaFormaPagamento) {
        this.listaFormaPagamento = listaFormaPagamento;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public BigDecimal calcularTotalDesconto() {
        valorDesconto = getListaOrcamentoDetalhe().stream()
                .map(OrcamentoDetalhe::getValorDesconto)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        return valorDesconto;
    }


    public BigDecimal calcularValorProdutos() {
        valorSubtotal = getListaOrcamentoDetalhe().stream()
                .map(OrcamentoDetalhe::getValorSubtotal)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        return valorSubtotal;
    }

    public BigDecimal calcularValorTotal() {
        valorTotal = calcularValorProdutos();
        valorTotal = valorTotal.add(getValorFrete())
                .subtract(calcularTotalDesconto());
        if (valorTotal == null) {
            System.out.println("");
        }
        return valorTotal;
    }


    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.id);
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
        final OrcamentoCabecalho other = (OrcamentoCabecalho) obj;
        return Objects.equals(this.id, other.id);
    }

}
