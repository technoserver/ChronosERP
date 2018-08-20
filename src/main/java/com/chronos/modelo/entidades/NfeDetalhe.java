package com.chronos.modelo.entidades;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "NFE_DETALHE")
public class NfeDetalhe implements Serializable {

    private static final long serialVersionUID = 2L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NUMERO_ITEM")
    private Integer numeroItem;
    @Column(name = "CODIGO_PRODUTO")
    private String codigoProduto;
    @Column(name = "GTIN")
    private String gtin;
    @Column(name = "NOME_PRODUTO")
    private String nomeProduto;
    @Column(name = "NCM")
    private String ncm;
    @Column(name = "CEST", length = 7)
    private String cest;
    @Column(name = "NVE")
    private String nve;
    @Column(name = "EX_TIPI")
    private Integer exTipi;
    @Column(name = "CFOP")
    private Integer cfop;
    @Column(name = "UNIDADE_COMERCIAL")
    private String unidadeComercial;
    @Column(name = "QUANTIDADE_COMERCIAL")
    private BigDecimal quantidadeComercial;
    @Column(name = "VALOR_UNITARIO_COMERCIAL")
    private BigDecimal valorUnitarioComercial;
    @Column(name = "VALOR_BRUTO_PRODUTO")
    private BigDecimal valorBrutoProduto;
    @Column(name = "GTIN_UNIDADE_TRIBUTAVEL")
    private String gtinUnidadeTributavel;
    @Column(name = "UNIDADE_TRIBUTAVEL")
    private String unidadeTributavel;
    @Column(name = "QUANTIDADE_TRIBUTAVEL")
    private BigDecimal quantidadeTributavel;
    @Column(name = "VALOR_UNITARIO_TRIBUTAVEL")
    private BigDecimal valorUnitarioTributavel;
    @Column(name = "VALOR_FRETE")
    private BigDecimal valorFrete;
    @Column(name = "VALOR_SEGURO")
    private BigDecimal valorSeguro;
    @Column(name = "VALOR_DESCONTO")
    private BigDecimal valorDesconto;
    @Column(name = "VALOR_OUTRAS_DESPESAS")
    private BigDecimal valorOutrasDespesas;
    @Column(name = "ENTRA_TOTAL")
    private Integer entraTotal;
    @Column(name = "VALOR_SUBTOTAL")
    private BigDecimal valorSubtotal;
    @Column(name = "VALOR_TOTAL")
    private BigDecimal valorTotal;
    @Column(name = "NUMERO_PEDIDO_COMPRA")
    private String numeroPedidoCompra;
    @Column(name = "ITEM_PEDIDO_COMPRA")
    private Integer itemPedidoCompra;
    @Column(name = "INFORMACOES_ADICIONAIS")
    private String informacoesAdicionais;
    @Column(name = "NUMERO_FCI")
    private String numeroFci;
    @Column(name = "NUMERO_RECOPI")
    private String numeroRecopi;
    @Column(name = "VALOR_TOTAL_TRIBUTOS")
    private BigDecimal valorTotalTributos;
    @Column(name = "VALOR_TOTAL_TRIBUTOS_FEDERAIS")
    @ColumnDefault(value = "0.00")
    private BigDecimal valorTotalTributosFederais;
    @Column(name = "VALOR_TOTAL_TRIBUTOS_ESTADUAIS")
    @ColumnDefault(value = "0.00")
    private BigDecimal valorTotalTributosEstaduais;
    @Column(name = "VALOR_TOTAL_TRIBUTOS_MUNICIPAIS")
    @ColumnDefault(value = "0.00")
    private BigDecimal valorTotalTributosMunicipais;
    @Column(name = "PERCENTUAL_DEVOLVIDO")
    private BigDecimal percentualDevolvido;
    @Column(name = "VALOR_IPI_DEVOLVIDO")
    private BigDecimal valorIpiDevolvido;
    @JoinColumn(name = "ID_NFE_CABECALHO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private NfeCabecalho nfeCabecalho;
    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @ManyToOne
    private Produto produto;
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "nfeDetalhe", cascade = CascadeType.ALL)
    private NfeDetalheImpostoIcms nfeDetalheImpostoIcms;
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "nfeDetalhe", cascade = CascadeType.ALL)
    private NfeDetalheImpostoIpi nfeDetalheImpostoIpi;
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "nfeDetalhe", cascade = CascadeType.ALL)
    private NfeDetalheImpostoIi nfeDetalheImpostoIi;
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "nfeDetalhe", cascade = CascadeType.ALL)
    private NfeDetalheImpostoPis nfeDetalheImpostoPis;
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "nfeDetalhe", cascade = CascadeType.ALL)
    private NfeDetalheImpostoCofins nfeDetalheImpostoCofins;
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "nfeDetalhe", cascade = CascadeType.ALL)
    private NfeDetalheImpostoIssqn nfeDetalheImpostoIssqn;
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "nfeDetalhe", cascade = CascadeType.ALL)
    private NfeDetEspecificoVeiculo veiculo;
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "nfeDetalhe", cascade = CascadeType.ALL)
    private NfeDetEspecificoCombustivel combustivel;
    //    @OneToMany(mappedBy = "nfeDetalhe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    private Set<NfeDetEspecificoMedicamento> listaMedicamento;
//    @OneToMany(mappedBy = "nfeDetalhe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    private Set<NfeDetEspecificoArmamento> listaArmamento;
//    @OneToMany(mappedBy = "nfeDetalhe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    private Set<NfeDeclaracaoImportacao> listaDeclaracaoImportacao;
    @Transient
    private boolean produtoCadastrado;
    @Transient
    private BigDecimal impostoFederal;
    @Transient
    private BigDecimal impostoEstadual;
    @Transient
    private BigDecimal impostoMunicipal;

    public NfeDetalhe() {
    }

    public NfeDetalhe(Integer id, Produto produto, BigDecimal quantidadeComercial) {
        this.id = id;
        this.produto = produto;
        this.quantidadeComercial = quantidadeComercial;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumeroItem() {
        return numeroItem;
    }

    public void setNumeroItem(Integer numeroItem) {
        this.numeroItem = numeroItem;
    }

    public String getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getNcm() {
        return ncm;
    }

    public void setNcm(String ncm) {
        this.ncm = ncm;
    }

    public String getCest() {
        return cest;
    }

    public void setCest(String cest) {
        this.cest = cest;
    }

    public String getNve() {
        return nve;
    }

    public void setNve(String nve) {
        this.nve = nve;
    }

    public Integer getExTipi() {
        return exTipi;
    }

    public void setExTipi(Integer exTipi) {
        this.exTipi = exTipi;
    }

    public Integer getCfop() {
        return cfop;
    }

    public void setCfop(Integer cfop) {
        this.cfop = cfop;
    }

    public String getUnidadeComercial() {
        return unidadeComercial;
    }

    public void setUnidadeComercial(String unidadeComercial) {
        this.unidadeComercial = unidadeComercial;
    }

    public BigDecimal getQuantidadeComercial() {
        return Optional.ofNullable(quantidadeComercial).orElse(BigDecimal.ZERO);
    }

    public void setQuantidadeComercial(BigDecimal quantidadeComercial) {
        this.quantidadeComercial = quantidadeComercial;
    }

    public BigDecimal getValorUnitarioComercial() {
        return Optional.ofNullable(valorUnitarioComercial).orElse(BigDecimal.ZERO);
    }

    public void setValorUnitarioComercial(BigDecimal valorUnitarioComercial) {
        this.valorUnitarioComercial = valorUnitarioComercial;
    }

    public BigDecimal getValorBrutoProduto() {
        return valorBrutoProduto = getQuantidadeComercial().multiply(getValorUnitarioComercial());
    }

    public void setValorBrutoProduto(BigDecimal valorBrutoProduto) {
        this.valorBrutoProduto = valorBrutoProduto;
    }

    public String getGtinUnidadeTributavel() {
        return gtinUnidadeTributavel = getUnidadeComercial();
    }

    public void setGtinUnidadeTributavel(String gtinUnidadeTributavel) {
        this.gtinUnidadeTributavel = gtinUnidadeTributavel;
    }

    public String getUnidadeTributavel() {
        return unidadeTributavel;
    }

    public void setUnidadeTributavel(String unidadeTributavel) {
        this.unidadeTributavel = unidadeTributavel;
    }

    public BigDecimal getQuantidadeTributavel() {
        return quantidadeTributavel = getQuantidadeComercial();
    }

    public void setQuantidadeTributavel(BigDecimal quantidadeTributavel) {
        this.quantidadeTributavel = quantidadeTributavel;
    }

    public BigDecimal getValorUnitarioTributavel() {
        return valorUnitarioTributavel = getValorUnitarioComercial();
    }

    public void setValorUnitarioTributavel(BigDecimal valorUnitarioTributavel) {
        this.valorUnitarioTributavel = valorUnitarioTributavel;
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

    public BigDecimal getValorDesconto() {
        return Optional.ofNullable(valorDesconto).orElse(BigDecimal.ZERO);
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public BigDecimal getValorOutrasDespesas() {
        return Optional.ofNullable(valorOutrasDespesas).orElse(BigDecimal.ZERO);
    }

    public void setValorOutrasDespesas(BigDecimal valorOutrasDespesas) {
        this.valorOutrasDespesas = valorOutrasDespesas;
    }

    public Integer getEntraTotal() {
        return entraTotal;
    }

    public void setEntraTotal(Integer entraTotal) {
        this.entraTotal = entraTotal;
    }

    public BigDecimal getValorSubtotal() {
        return Optional.ofNullable(valorSubtotal).orElse(BigDecimal.ZERO);
    }

    public void setValorSubtotal(BigDecimal valorSubtotal) {
        this.valorSubtotal = valorSubtotal;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getNumeroPedidoCompra() {
        return numeroPedidoCompra;
    }

    public void setNumeroPedidoCompra(String numeroPedidoCompra) {
        this.numeroPedidoCompra = numeroPedidoCompra;
    }

    public Integer getItemPedidoCompra() {
        return itemPedidoCompra;
    }

    public void setItemPedidoCompra(Integer itemPedidoCompra) {
        this.itemPedidoCompra = itemPedidoCompra;
    }

    public String getInformacoesAdicionais() {
        return informacoesAdicionais;
    }

    public void setInformacoesAdicionais(String informacoesAdicionais) {
        this.informacoesAdicionais = informacoesAdicionais;
    }

    public String getNumeroFci() {
        return numeroFci;
    }

    public void setNumeroFci(String numeroFci) {
        this.numeroFci = numeroFci;
    }

    public String getNumeroRecopi() {
        return numeroRecopi;
    }

    public void setNumeroRecopi(String numeroRecopi) {
        this.numeroRecopi = numeroRecopi;
    }

    public BigDecimal getValorTotalTributos() {
        return valorTotalTributos;
    }

    public void setValorTotalTributos(BigDecimal valorTotalTributos) {
        this.valorTotalTributos = valorTotalTributos;
    }

    public BigDecimal getPercentualDevolvido() {
        return percentualDevolvido;
    }

    public void setPercentualDevolvido(BigDecimal percentualDevolvido) {
        this.percentualDevolvido = percentualDevolvido;
    }

    public BigDecimal getValorTotalTributosFederais() {
        return valorTotalTributosFederais;
    }

    public void setValorTotalTributosFederais(BigDecimal valorTotalTributosFederais) {
        this.valorTotalTributosFederais = valorTotalTributosFederais;
    }

    public BigDecimal getValorTotalTributosEstaduais() {
        return valorTotalTributosEstaduais;
    }

    public void setValorTotalTributosEstaduais(BigDecimal valorTotalTributosEstaduais) {
        this.valorTotalTributosEstaduais = valorTotalTributosEstaduais;
    }

    public BigDecimal getValorTotalTributosMunicipais() {
        return valorTotalTributosMunicipais;
    }

    public void setValorTotalTributosMunicipais(BigDecimal valorTotalTributosMunicipais) {
        this.valorTotalTributosMunicipais = valorTotalTributosMunicipais;
    }

    public BigDecimal getValorIpiDevolvido() {
        return valorIpiDevolvido;
    }

    public void setValorIpiDevolvido(BigDecimal valorIpiDevolvido) {
        this.valorIpiDevolvido = valorIpiDevolvido;
    }

    public NfeCabecalho getNfeCabecalho() {
        return nfeCabecalho;
    }

    public void setNfeCabecalho(NfeCabecalho nfeCabecalho) {
        this.nfeCabecalho = nfeCabecalho;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
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

    public NfeDetalheImpostoIi getNfeDetalheImpostoIi() {
        return nfeDetalheImpostoIi;
    }

    public void setNfeDetalheImpostoIi(NfeDetalheImpostoIi nfeDetalheImpostoIi) {
        this.nfeDetalheImpostoIi = nfeDetalheImpostoIi;
    }

    public NfeDetalheImpostoPis getNfeDetalheImpostoPis() {
        return nfeDetalheImpostoPis;
    }

    public void setNfeDetalheImpostoPis(NfeDetalheImpostoPis nfeDetalheImpostoPis) {
        this.nfeDetalheImpostoPis = nfeDetalheImpostoPis;
    }

    public NfeDetalheImpostoCofins getNfeDetalheImpostoCofins() {
        return nfeDetalheImpostoCofins;
    }

    public void setNfeDetalheImpostoCofins(NfeDetalheImpostoCofins nfeDetalheImpostoCofins) {
        this.nfeDetalheImpostoCofins = nfeDetalheImpostoCofins;
    }

    public NfeDetalheImpostoIssqn getNfeDetalheImpostoIssqn() {
        return nfeDetalheImpostoIssqn;
    }

    public void setNfeDetalheImpostoIssqn(NfeDetalheImpostoIssqn nfeDetalheImpostoIssqn) {
        this.nfeDetalheImpostoIssqn = nfeDetalheImpostoIssqn;
    }

    public NfeDetEspecificoVeiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(NfeDetEspecificoVeiculo veiculo) {
        this.veiculo = veiculo;
    }

    public NfeDetEspecificoCombustivel getCombustivel() {
        return combustivel;
    }

    public void setCombustivel(NfeDetEspecificoCombustivel combustivel) {
        this.combustivel = combustivel;
    }

//    public Set<NfeDetEspecificoMedicamento> getListaMedicamento() {
//        return listaMedicamento;
//    }
//
//    public void setListaMedicamento(Set<NfeDetEspecificoMedicamento> listaMedicamento) {
//        this.listaMedicamento = listaMedicamento;
//    }
//
//    public Set<NfeDetEspecificoArmamento> getListaArmamento() {
//        return listaArmamento;
//    }
//
//    public void setListaArmamento(Set<NfeDetEspecificoArmamento> listaArmamento) {
//        this.listaArmamento = listaArmamento;
//    }
//
//    public Set<NfeDeclaracaoImportacao> getListaDeclaracaoImportacao() {
//        return listaDeclaracaoImportacao;
//    }
//
//    public void setListaDeclaracaoImportacao(Set<NfeDeclaracaoImportacao> listaDeclaracaoImportacao) {
//        this.listaDeclaracaoImportacao = listaDeclaracaoImportacao;
//    }

    public boolean isProdutoCadastrado() {
        return produtoCadastrado;
    }

    public void setProdutoCadastrado(boolean produtoCadastrado) {
        this.produtoCadastrado = produtoCadastrado;
    }

    public BigDecimal getImpostoFederal() {
        return impostoFederal;
    }

    public void setImpostoFederal(BigDecimal impostoFederal) {
        this.impostoFederal = impostoFederal;
    }

    public BigDecimal getImpostoEstadual() {
        return impostoEstadual;
    }

    public void setImpostoEstadual(BigDecimal impostoEstadual) {
        this.impostoEstadual = impostoEstadual;
    }

    public BigDecimal getImpostoMunicipal() {
        return impostoMunicipal;
    }

    public void setImpostoMunicipal(BigDecimal impostoMunicipal) {
        this.impostoMunicipal = impostoMunicipal;
    }

    public void pegarInfoProduto() {
        if (produto != null) {
            nomeProduto = produto.getNome();
            ncm = produto.getNcm();
            exTipi = !StringUtils.isEmpty(produto.getExTipi()) ? !StringUtils.isEmpty(produto.getExTipi().trim()) ? Integer.valueOf(produto.getExTipi()) : exTipi : exTipi;
            cest = produto.getCest();
            gtin = produto.getGtin();
            unidadeComercial = produto.getUnidadeProduto().getSigla();
            unidadeTributavel = unidadeComercial;
            codigoProduto = StringUtils.isEmpty(gtin) ? produto.getId().toString() : gtin;
        }

    }

    public BigDecimal calcularSubTotalProduto(){
        this.valorSubtotal = getQuantidadeComercial().multiply(getValorUnitarioComercial());
        return valorSubtotal;
    }


    public BigDecimal calcularValorTotalProduto(){
        valorTotal = Optional.ofNullable(calcularSubTotalProduto()).orElse(BigDecimal.ZERO);
        valorTotal = valorTotal
                .add(Optional.ofNullable(this.valorFrete).orElse(BigDecimal.ZERO))
                .add(Optional.ofNullable(this.valorOutrasDespesas).orElse(BigDecimal.ZERO))
                .add(Optional.ofNullable(this.valorSeguro).orElse(BigDecimal.ZERO))
                .subtract(Optional.ofNullable(this.valorDesconto).orElse(BigDecimal.ZERO));

        return valorTotal;
    }

    public BigDecimal calcularTotal() {
        valorTotal = BigDecimal.ZERO;
        valorTotal = valorTotal.add(getValorSubtotal())
                .add(getValorFrete())
                .add(getValorSeguro())
                .add(getValorOutrasDespesas())
                .subtract(getValorDesconto());

        return valorTotal;
    }

    public boolean isProdutoValido() {
        boolean valido = true;
        if (produto == null) {
            valido = false;
        } else if (StringUtils.isEmpty(produto.getNcm())) {
            valido = false;
        }

        return valido;
    }



    @Override
    public int hashCode() {
        int hash = 7;

        hash = 61 * hash + Objects.hashCode(this.id);
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
        final NfeDetalhe other = (NfeDetalhe) obj;
        return Objects.equals(this.id, other.id);
    }

}
