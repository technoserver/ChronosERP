
package com.chronos.erp.modelo.entidades;

import com.chronos.erp.bo.cadastro.ItemFilizola;
import com.chronos.erp.bo.cadastro.ItemToledo;
import com.chronos.erp.modelo.enuns.PrecoPrioritario;
import com.chronos.erp.service.ChronosException;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "PRODUTO")
@NamedQuery(name = "Produto.atualizarValorVenda"
        , query = "UPDATE Produto p SET p.valorVenda = ?1  where p.id =?2")
@DynamicUpdate
public class Produto implements Serializable {

    private static final long serialVersionUID = 2L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "GTIN", unique = true)
    private String gtin;
    @Column(name = "CODIGO_INTERNO")
    private String codigoInterno;
    @Column(name = "NCM")
    private String ncm;
    @Column(name = "CEST", length = 7)
    private String cest;
    @Column(name = "NOME")
    private String nome;
    @Column(name = "DESCRICAO")
    private String descricao;
    @Column(name = "DESCRICAO_PDV")
    private String descricaoPdv;
    @Column(name = "VALOR_COMPRA")
    private BigDecimal valorCompra;
    @Column(name = "VALOR_VENDA")
    @DecimalMin(value = "0.01", message = "O valor  deve ser maior que R$0,01")
    @DecimalMax(value = "9999999.99", message = "O valor  deve ser menor que R$9.999.999,99")
    private BigDecimal valorVenda;

    @Column(name = "VALOR_VENDA_ATACADO")
    @DecimalMin(value = "0.01", message = "O valor  deve ser maior que R$0,01")
    @DecimalMax(value = "9999999.99", message = "O valor  deve ser menor que R$9.999.999,99")
    private BigDecimal valorVendaAtacado;

    @Column(name = "QUANTIDADE_VENDA_ATACADO")
    @DecimalMin(value = "0.01", message = "A quantidade  deve ser maior que R$0,01")
    @DecimalMax(value = "9999999.99", message = "A quantidade  deve ser menor que 9.999.999,99")
    private BigDecimal quantidadeVendaAtacado;

    @Column(name = "PRECO_VENDA_MINIMO")
    private BigDecimal precoVendaMinimo;
    @Column(name = "PRECO_SUGERIDO")
    private BigDecimal precoSugerido;
    @Column(name = "CUSTO_UNITARIO")
    private BigDecimal custoUnitario;
    @Column(name = "CUSTO_PRODUCAO")
    private BigDecimal custoProducao;
    @Column(name = "CUSTO_MEDIO_LIQUIDO")
    private BigDecimal custoMedioLiquido;
    @Column(name = "PRECO_LUCRO_ZERO")
    private BigDecimal precoLucroZero;
    @Column(name = "PRECO_LUCRO_MINIMO")
    private BigDecimal precoLucroMinimo;
    @Column(name = "PRECO_LUCRO_MAXIMO")
    private BigDecimal precoLucroMaximo;
    @Column(name = "MARKUP")
    private BigDecimal markup;
    @Column(name = "QUANTIDADE_ESTOQUE")
    private BigDecimal quantidadeEstoque;
    @Column(name = "QUANTIDADE_ESTOQUE_ANTERIOR")
    private BigDecimal quantidadeEstoqueAnterior;
    @Column(name = "ESTOQUE_MINIMO")
    private BigDecimal estoqueMinimo;
    @Column(name = "ESTOQUE_MAXIMO")
    private BigDecimal estoqueMaximo;
    @Column(name = "ESTOQUE_IDEAL")
    private BigDecimal estoqueIdeal;
    @Column(name = "EXCLUIDO")
    private String excluido;
    @Column(name = "INATIVO")
    private String inativo;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_CADASTRO")
    private Date dataCadastro;
    @Column(name = "IMAGEM")
    private String imagem;
    @Column(name = "EX_TIPI")
    private String exTipi;
    @Column(name = "CODIGO_LST")
    private String codigoLst;
    @Column(name = "CLASSE_ABC")
    private String classeAbc;
    @Column(name = "IAT")
    private String iat;
    @Column(name = "IPPT")
    private String ippt;
    @Column(name = "TIPO_ITEM_SPED")
    private String tipoItemSped;
    @Column(name = "PESO")
    private BigDecimal peso;
    @Column(name = "PORCENTO_COMISSAO")
    private BigDecimal porcentoComissao;
    @Column(name = "PONTO_PEDIDO")
    private BigDecimal pontoPedido;
    @Column(name = "LOTE_ECONOMICO_COMPRA")
    private BigDecimal loteEconomicoCompra;
    @Column(name = "ALIQUOTA_ICMS_PAF")
    private BigDecimal aliquotaIcmsPaf;
    @Column(name = "ALIQUOTA_ISSQN_PAF")
    private BigDecimal aliquotaIssqnPaf;
    @Column(name = "TOTALIZADOR_PARCIAL")
    private String totalizadorParcial;
    @Column(name = "CODIGO_BALANCA")
    private Integer codigoBalanca;
    @Column(name = "DIAS_VALIDADE")
    private Integer diasValidade;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_ALTERACAO")
    private Date dataAlteracao;
    @Column(name = "TIPO")
    private String tipo;
    @Column(name = "SERVICO")
    private String servico;
    @Column(name = "preco_prioritario")
    private PrecoPrioritario precoPrioritario;
    @Column(name = "possui_grade")
    private Boolean possuiGrade;
    @JoinColumn(name = "ID_UNIDADE_PRODUTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UnidadeProduto unidadeProduto;
    @JoinColumn(name = "ID_SUBGRUPO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ProdutoSubGrupo produtoSubGrupo;
    @JoinColumn(name = "ID_MARCA_PRODUTO", referencedColumnName = "ID")
    @ManyToOne
    private ProdutoMarca produtoMarca;
    @JoinColumn(name = "ID_GRUPO_TRIBUTARIO", referencedColumnName = "ID")
    @ManyToOne
    private TributGrupoTributario tributGrupoTributario;
    @JoinColumn(name = "ID_ALMOXARIFADO", referencedColumnName = "ID")
    @ManyToOne
    private Almoxarifado almoxarifado;
    @JoinColumn(name = "ID_TRIBUT_ICMS_CUSTOM_CAB", referencedColumnName = "ID")
    @ManyToOne
    private TributIcmsCustomCab tributIcmsCustomCab;
    @JoinColumn(name = "id_tabela_nutricional_cabecalho", referencedColumnName = "ID")
    @ManyToOne
    private TabelaNutricionalCabecalho tabelaNutricional;
    @JoinColumn(name = "id_produto_grade", referencedColumnName = "ID")
    @ManyToOne
    private ProdutoGrade produtoGrade;


    //@OneToMany(fetch = FetchType.LAZY, mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "EMPRESA_PRODUTO", joinColumns = {
            @JoinColumn(name = "ID_EMPRESA")}, inverseJoinColumns = {
            @JoinColumn(name = "ID_PRODUTO")})
    private List<EmpresaProduto> produtosEmpresa;
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "produto", cascade = CascadeType.ALL)
    private UnidadeConversao unidadeConversao;
    @Transient
    private BigDecimal encargosVenda;
    @Transient
    private BigDecimal controle;
    @Transient
    private List<UnidadeConversao> conversoes;
    @Transient
    private PdvConfiguracaoBalanca balanca;

    public Produto() {
        this.valorCompra = BigDecimal.ZERO;

        this.precoLucroMaximo = BigDecimal.ZERO;
        this.precoLucroMinimo = BigDecimal.ZERO;
        this.precoLucroZero = BigDecimal.ZERO;
        this.precoSugerido = BigDecimal.ZERO;
        this.precoVendaMinimo = BigDecimal.ZERO;

        this.custoMedioLiquido = BigDecimal.ZERO;
        this.custoProducao = BigDecimal.ZERO;
        this.custoUnitario = BigDecimal.ZERO;


        this.estoqueIdeal = BigDecimal.ZERO;
        this.estoqueMaximo = BigDecimal.ZERO;
        this.estoqueMinimo = BigDecimal.ZERO;

        this.quantidadeEstoqueAnterior = BigDecimal.ZERO;
        this.estoqueIdeal = BigDecimal.ZERO;

        this.servico = "N";
        this.excluido = "N";
        this.inativo = "N";
        this.classeAbc = "A";
        this.iat = "A";
        this.ippt = "P";
        this.tipoItemSped = "00";
        this.tipo = "V";
        this.possuiGrade = false;

    }

    public Produto(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }


    public Produto(Integer id, String nome, String servico, BigDecimal valorVenda, BigDecimal quantidadeEstoque, String ncm, UnidadeProduto unidadeProduto) {
        this.id = id;
        this.nome = nome;
        this.valorVenda = valorVenda;
        this.quantidadeEstoque = quantidadeEstoque;
        this.ncm = ncm;
        this.unidadeProduto = unidadeProduto;
        this.servico = servico;
    }


    public Produto(Integer id, String nome, BigDecimal valorCompra, BigDecimal valorVenda, BigDecimal markup) {
        this.id = id;
        this.nome = nome;
        this.valorCompra = valorCompra;
        this.valorVenda = valorVenda;
        this.markup = markup;
    }

    public Produto(Integer id, String nome, BigDecimal valorVenda) {
        this.id = id;
        this.nome = nome;
        this.valorVenda = valorVenda;
    }

    public Produto(Integer id, String nome, BigDecimal valorVenda, Integer codigoBalanca) {
        this.id = id;
        this.nome = nome;
        this.valorVenda = valorVenda;
        this.codigoBalanca = codigoBalanca;

    }

    public Produto(Integer id, String nome, BigDecimal valorVenda, Integer codigoBalanca, Integer diasValidade, TabelaNutricionalCabecalho tabelaNutricional) {
        this.id = id;
        this.nome = nome;
        this.valorVenda = valorVenda;
        this.codigoBalanca = codigoBalanca;
        this.diasValidade = diasValidade;
        this.tabelaNutricional = tabelaNutricional;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public String getCodigoInterno() {
        return codigoInterno;
    }

    public void setCodigoInterno(String codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    public String getNcm() {
        return ncm;
    }

    public void setNcm(String ncm) {
        this.ncm = ncm;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricaoPdv() {
        return descricaoPdv;
    }

    public void setDescricaoPdv(String descricaoPdv) {
        this.descricaoPdv = descricaoPdv;
    }

    public BigDecimal getValorCompra() {
        return valorCompra;
    }

    public void setValorCompra(BigDecimal valorCompra) {
        this.valorCompra = valorCompra;
    }

    public BigDecimal getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(BigDecimal valorVenda) {
        this.valorVenda = valorVenda;
    }

    public BigDecimal getPrecoVendaMinimo() {
        return precoVendaMinimo;
    }

    public void setPrecoVendaMinimo(BigDecimal precoVendaMinimo) {
        this.precoVendaMinimo = precoVendaMinimo;
    }

    public BigDecimal getPrecoSugerido() {
        return precoSugerido;
    }

    public void setPrecoSugerido(BigDecimal precoSugerido) {
        this.precoSugerido = precoSugerido;
    }

    public BigDecimal getCustoMedioLiquido() {
        return custoMedioLiquido;
    }

    public void setCustoMedioLiquido(BigDecimal custoMedioLiquido) {
        this.custoMedioLiquido = custoMedioLiquido;
    }

    public BigDecimal getPrecoLucroZero() {
        return precoLucroZero;
    }

    public void setPrecoLucroZero(BigDecimal precoLucroZero) {
        this.precoLucroZero = precoLucroZero;
    }

    public BigDecimal getPrecoLucroMinimo() {
        return precoLucroMinimo;
    }

    public void setPrecoLucroMinimo(BigDecimal precoLucroMinimo) {
        this.precoLucroMinimo = precoLucroMinimo;
    }

    public BigDecimal getPrecoLucroMaximo() {
        return precoLucroMaximo;
    }

    public void setPrecoLucroMaximo(BigDecimal precoLucroMaximo) {
        this.precoLucroMaximo = precoLucroMaximo;
    }

    public BigDecimal getMarkup() {
        return markup;
    }

    public void setMarkup(BigDecimal markup) {
        this.markup = markup;
    }

    public BigDecimal getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(BigDecimal quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public BigDecimal getQuantidadeEstoqueAnterior() {
        return quantidadeEstoqueAnterior;
    }

    public void setQuantidadeEstoqueAnterior(BigDecimal quantidadeEstoqueAnterior) {
        this.quantidadeEstoqueAnterior = quantidadeEstoqueAnterior;
    }

    public BigDecimal getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(BigDecimal estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public BigDecimal getEstoqueMaximo() {
        return estoqueMaximo;
    }

    public void setEstoqueMaximo(BigDecimal estoqueMaximo) {
        this.estoqueMaximo = estoqueMaximo;
    }

    public BigDecimal getEstoqueIdeal() {
        return estoqueIdeal;
    }

    public void setEstoqueIdeal(BigDecimal estoqueIdeal) {
        this.estoqueIdeal = estoqueIdeal;
    }

    public String getExcluido() {
        return excluido;
    }

    public void setExcluido(String excluido) {
        this.excluido = excluido;
    }

    public String getInativo() {
        return inativo;
    }

    public void setInativo(String inativo) {
        this.inativo = inativo;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public BigDecimal getCustoUnitario() {
        return custoUnitario;
    }

    public void setCustoUnitario(BigDecimal custoUnitario) {
        this.custoUnitario = custoUnitario;
    }

    public BigDecimal getCustoProducao() {
        return custoProducao;
    }

    public void setCustoProducao(BigDecimal custoProducao) {
        this.custoProducao = custoProducao;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public String getExTipi() {
        return exTipi;
    }

    public void setExTipi(String exTipi) {
        this.exTipi = exTipi;
    }

    public String getCodigoLst() {
        return codigoLst;
    }

    public void setCodigoLst(String codigoLst) {
        this.codigoLst = codigoLst;
    }

    public String getClasseAbc() {
        return classeAbc;
    }

    public void setClasseAbc(String classeAbc) {
        this.classeAbc = classeAbc;
    }

    public String getIat() {
        return iat;
    }

    public void setIat(String iat) {
        this.iat = iat;
    }

    public String getIppt() {
        return ippt;
    }

    public void setIppt(String ippt) {
        this.ippt = ippt;
    }

    public String getTipoItemSped() {
        return tipoItemSped;
    }

    public void setTipoItemSped(String tipoItemSped) {
        this.tipoItemSped = tipoItemSped;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getPorcentoComissao() {
        return porcentoComissao;
    }

    public void setPorcentoComissao(BigDecimal porcentoComissao) {
        this.porcentoComissao = porcentoComissao;
    }

    public BigDecimal getPontoPedido() {
        return pontoPedido;
    }

    public void setPontoPedido(BigDecimal pontoPedido) {
        this.pontoPedido = pontoPedido;
    }

    public BigDecimal getLoteEconomicoCompra() {
        return loteEconomicoCompra;
    }

    public void setLoteEconomicoCompra(BigDecimal loteEconomicoCompra) {
        this.loteEconomicoCompra = loteEconomicoCompra;
    }

    public BigDecimal getAliquotaIcmsPaf() {
        return aliquotaIcmsPaf;
    }

    public void setAliquotaIcmsPaf(BigDecimal aliquotaIcmsPaf) {
        this.aliquotaIcmsPaf = aliquotaIcmsPaf;
    }

    public BigDecimal getAliquotaIssqnPaf() {
        return aliquotaIssqnPaf;
    }

    public void setAliquotaIssqnPaf(BigDecimal aliquotaIssqnPaf) {
        this.aliquotaIssqnPaf = aliquotaIssqnPaf;
    }

    public String getTotalizadorParcial() {
        return totalizadorParcial;
    }

    public void setTotalizadorParcial(String totalizadorParcial) {
        this.totalizadorParcial = totalizadorParcial;
    }

    public Integer getCodigoBalanca() {
        return codigoBalanca;
    }

    public void setCodigoBalanca(Integer codigoBalanca) {
        this.codigoBalanca = codigoBalanca;
    }

    public Date getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(Date dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public UnidadeProduto getUnidadeProduto() {
        return unidadeProduto;
    }

    public void setUnidadeProduto(UnidadeProduto unidadeProduto) {
        this.unidadeProduto = unidadeProduto;
    }

    public ProdutoSubGrupo getProdutoSubGrupo() {
        return produtoSubGrupo;
    }

    public void setProdutoSubGrupo(ProdutoSubGrupo produtoSubGrupo) {
        this.produtoSubGrupo = produtoSubGrupo;
    }

    public ProdutoMarca getProdutoMarca() {
        return produtoMarca;
    }

    public void setProdutoMarca(ProdutoMarca produtoMarca) {
        this.produtoMarca = produtoMarca;
    }

    public TributGrupoTributario getTributGrupoTributario() {
        return tributGrupoTributario;
    }

    public void setTributGrupoTributario(TributGrupoTributario tributGrupoTributario) {
        this.tributGrupoTributario = tributGrupoTributario;
    }

    public Almoxarifado getAlmoxarifado() {
        return almoxarifado;
    }

    public void setAlmoxarifado(Almoxarifado almoxarifado) {
        this.almoxarifado = almoxarifado;
    }

    public TributIcmsCustomCab getTributIcmsCustomCab() {
        return tributIcmsCustomCab;
    }

    public void setTributIcmsCustomCab(TributIcmsCustomCab tributIcmsCustomCab) {
        this.tributIcmsCustomCab = tributIcmsCustomCab;
    }

    public BigDecimal getEncargosVenda() {
        return encargosVenda;
    }

    public void setEncargosVenda(BigDecimal encargosVenda) {
        this.encargosVenda = encargosVenda;
    }

    public List<EmpresaProduto> getProdutosEmpresa() {
        return produtosEmpresa;
    }

    public void setProdutosEmpresa(List<EmpresaProduto> produtosEmpresa) {
        this.produtosEmpresa = produtosEmpresa;
    }

    public UnidadeConversao getUnidadeConversao() {
        return unidadeConversao;
    }

    public void setUnidadeConversao(UnidadeConversao unidadeConversao) {
        this.unidadeConversao = unidadeConversao;
    }

    public String getCest() {
        return cest;
    }

    public void setCest(String cest) {
        this.cest = cest;
    }

    public BigDecimal getControle() {
        return Optional.ofNullable(controle).orElse(BigDecimal.ZERO);
    }

    public void setControle(BigDecimal controle) {
        this.controle = controle;
    }

    public PrecoPrioritario getPrecoPrioritario() {
        return precoPrioritario;
    }

    public void setPrecoPrioritario(PrecoPrioritario precoPrioritario) {
        this.precoPrioritario = precoPrioritario;
    }

    public BigDecimal getValorVendaAtacado() {
        return valorVendaAtacado;
    }

    public void setValorVendaAtacado(BigDecimal valorVendaAtacado) {
        this.valorVendaAtacado = valorVendaAtacado;
    }

    public BigDecimal getQuantidadeVendaAtacado() {
        return quantidadeVendaAtacado;
    }

    public void setQuantidadeVendaAtacado(BigDecimal quantidadeVendaAtacado) {
        this.quantidadeVendaAtacado = quantidadeVendaAtacado;
    }

    public List<UnidadeConversao> getConversoes() {
        return Optional.ofNullable(conversoes).orElse(new ArrayList<>());
    }

    public void setConversoes(List<UnidadeConversao> conversoes) {
        this.conversoes = conversoes;
    }


    public Integer getDiasValidade() {
        return diasValidade;
    }

    public void setDiasValidade(Integer diasValidade) {
        this.diasValidade = diasValidade;
    }

    public TabelaNutricionalCabecalho getTabelaNutricional() {
        return tabelaNutricional;
    }

    public void setTabelaNutricional(TabelaNutricionalCabecalho tabelaNutricional) {
        this.tabelaNutricional = tabelaNutricional;
    }

    public Boolean getPossuiGrade() {
        return possuiGrade;
    }

    public void setPossuiGrade(Boolean possuiGrade) {
        this.possuiGrade = possuiGrade;
    }

    public ProdutoGrade getProdutoGrade() {
        return produtoGrade;
    }

    public void setProdutoGrade(ProdutoGrade produtoGrade) {
        this.produtoGrade = produtoGrade;
    }

    public PdvConfiguracaoBalanca getBalanca() {
        return balanca;
    }

    public void setBalanca(PdvConfiguracaoBalanca balanca) {
        this.balanca = balanca;
    }

    public String montarItemBalancaToledo() throws ChronosException {
        ItemToledo item = new ItemToledo(this);
        return item.montarItem();
    }

    public String montarItemBalancaToledoNutricao() {
        ItemToledo item = new ItemToledo(this);
        return item.montarValorNutricional();
    }

    public String montarItemBalancaFilizola() throws ChronosException {
        ItemFilizola item = new ItemFilizola(this);
        return item.montarItem();
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Produto other = (Produto) obj;
        return Objects.equals(this.id, other.id);
    }

}
