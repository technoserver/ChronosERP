package com.chronos.modelo.entidades;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

@Entity
@Table(name = "VENDA_ORCAMENTO_CABECALHO")
public class VendaOrcamentoCabecalho implements Serializable {

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
    private BigDecimal valorTotal;
    @Column(name = "OBSERVACAO")
    private String observacao;
    @NotNull
    @NotBlank
    @Column(name = "SITUACAO")
    private String situacao;
    @JoinColumn(name = "ID_VENDA_CONDICOES_PAGAMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private VendaCondicoesPagamento condicoesPagamento;
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
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vendaOrcamentoCabecalho", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VendaOrcamentoDetalhe> listaVendaOrcamentoDetalhe;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private Empresa empresa;


    public VendaOrcamentoCabecalho() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    /**
     * O=Orçamento | P=Pedido
     * @return 
     */
    public String getTipo() {
        return tipo;
    }
    /**
     * O=Orçamento | P=Pedido
     * @param tipo 
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    /**
     * Código atribuído pelo usuário
     * @return 
     */
    public String getCodigo() {
        return codigo;
    }
    /**
     * Código atribuído pelo usuário
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
     * @return 
     */
    public String getTipoFrete() {
        return tipoFrete;
    }
    /**
     * C=CIF | F=FOB
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
     * @return 
     */
    public String getSituacao() {
        return situacao;
    }
    /**
     * D=Digitacao | P=Producao | X=Expedicao | F=Faturado | E=Entregue
     * @param situacao 
     */
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public VendaCondicoesPagamento getCondicoesPagamento() {
        return condicoesPagamento;
    }

    public void setCondicoesPagamento(VendaCondicoesPagamento condicoesPagamento) {
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

    public Set<VendaOrcamentoDetalhe> getListaVendaOrcamentoDetalhe() {
        return Optional.ofNullable(listaVendaOrcamentoDetalhe).orElse(new HashSet<>());
    }

    public void setListaVendaOrcamentoDetalhe(Set<VendaOrcamentoDetalhe> listaVendaOrcamentoDetalhe) {
        this.listaVendaOrcamentoDetalhe = listaVendaOrcamentoDetalhe;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public BigDecimal calcularValorTotal(){
        valorTotal = getListaVendaOrcamentoDetalhe().stream()
                          .map(VendaOrcamentoDetalhe::getValorTotal)
                          .reduce(BigDecimal::add)
                          .orElse(BigDecimal.ZERO);

        return  valorTotal;
    }

    public BigDecimal calcularValorProdutos(){
        valorSubtotal = getListaVendaOrcamentoDetalhe().stream()
                .map(VendaOrcamentoDetalhe::getValorSubtotal)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        return valorSubtotal;
    }

    public BigDecimal calcularTotalDesconto() {
        valorDesconto = getListaVendaOrcamentoDetalhe().stream()
                .map(VendaOrcamentoDetalhe::getValorDesconto)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        return valorDesconto;
    }

    public String valorSubTotalFormatado(){
        return formatarValor(calcularValorProdutos());
    }

    public String valorDescontoFormatado() {
        return formatarValor(calcularTotalDesconto());
    }

    public String valorTotalFormatado(){
        return formatarValor(calcularValorTotal());
    }

    private String formatarValor(BigDecimal valor) {
        DecimalFormatSymbols simboloDecimal = DecimalFormatSymbols.getInstance();
        simboloDecimal.setDecimalSeparator('.');
        DecimalFormat formatar = new DecimalFormat("0.00", simboloDecimal);

        return formatar.format(Optional.ofNullable(valor).orElse(BigDecimal.ZERO));
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
        final VendaOrcamentoCabecalho other = (VendaOrcamentoCabecalho) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
