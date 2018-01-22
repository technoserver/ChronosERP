package com.chronos.modelo.entidades;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "VENDA_CONDICOES_PAGAMENTO")
public class VendaCondicoesPagamento implements Serializable {

    private static final long serialVersionUID = 2L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NOME")
    private String nome;
    @Column(name = "DESCRICAO")
    private String descricao;
    @DecimalMin(value = "0.01", message = "O valor  deve ser maior que R$0,01")
    @DecimalMax(value = "9999999.99", message = "O valor  deve ser menor que R$9.999.999,99")
    @Column(name = "FATURAMENTO_MINIMO")
    private BigDecimal faturamentoMinimo;
    @DecimalMin(value = "0.01", message = "O valor  deve ser maior que R$0,01")
    @Column(name = "FATURAMENTO_MAXIMO")
    @DecimalMax(value = "9999999.99", message = "O valor  deve ser menor que R$9.999.999,99")
    private BigDecimal faturamentoMaximo;
    @Column(name = "INDICE_CORRECAO")
    @DecimalMin(value = "0.01", message = "O valor  deve ser maior que R$0,01")
    @DecimalMax(value = "99.99", message = "O valor  deve ser menor que 99,99")
    private BigDecimal indiceCorrecao;
    @Column(name = "DIAS_TOLERANCIA")
    private Integer diasTolerancia;
    @Column(name = "VALOR_TOLERANCIA")
    @DecimalMin(value = "0.01", message = "O valor  deve ser maior que R$0,01")
    @DecimalMax(value = "9999999.99", message = "O valor  deve ser menor que R$9.999.999,99")
    private BigDecimal valorTolerancia;
    @Column(name = "PRAZO_MEDIO")
    private Integer prazoMedio;
    @Column(name = "VISTA_PRAZO")
    private String vistaPrazo;
    @JoinColumn(name = "ID_FIN_TIPO_RECEBIMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private FinTipoRecebimento tipoRecebimento;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empresa empresa;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vendaCondicoesPagamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VendaCondicoesParcelas> parcelas;


    public VendaCondicoesPagamento() {

    }

    public VendaCondicoesPagamento(Integer id, String nome) {
        this.id = id;
        this.nome = nome;

    }

    public VendaCondicoesPagamento(Integer id, String nome, String vistaPrazo) {
        this.id = id;
        this.nome = nome;
        this.vistaPrazo = vistaPrazo;

    }

    public VendaCondicoesPagamento(Integer id, String nome, String vistaPrazo,FinTipoRecebimento tipoRecebimento) {
        this.id = id;
        this.nome = nome;
        this.vistaPrazo = vistaPrazo;
        this.tipoRecebimento = tipoRecebimento;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    /**
     * Valor mínimo para determinado tipo de pagamento. Exemplo: boleto: delimitar o menor valor de uma venda.
     *
     * @return
     */
    public BigDecimal getFaturamentoMinimo() {
        return faturamentoMinimo;
    }

    /**
     * Valor mínimo para determinado tipo de pagamento. Exemplo: boleto: delimitar o menor valor de uma venda.
     *
     *
     * @param faturamentoMinimo
     */
    public void setFaturamentoMinimo(BigDecimal faturamentoMinimo) {
        this.faturamentoMinimo = faturamentoMinimo;
    }

    /**
     * Valor máximo para determinado tipo de pagamento. Exemplo: boleto: delimitar o maior valor de uma venda.
     *
     * @return
     */
    public BigDecimal getFaturamentoMaximo() {
        return faturamentoMaximo;
    }

    /**
     * Valor máximo para determinado tipo de pagamento. Exemplo: boleto: delimitar o maior valor de uma venda.
     *
     * @param faturamentoMaximo
     */
    public void setFaturamentoMaximo(BigDecimal faturamentoMaximo) {
        this.faturamentoMaximo = faturamentoMaximo;
    }

    /**
     * Índice para acréscimo nas vendas a prazo sobre o preço de tabela no formato 1 + %/ 100. Exemplo: acréscimo de 5%
     * = 1,05. Padrão 1(=100%)
     *
     * @return
     */
    public BigDecimal getIndiceCorrecao() {
        return indiceCorrecao;
    }

    /**
     * Índice para acréscimo nas vendas a prazo sobre o preço de tabela no formato 1 + %/ 100. Exemplo: acréscimo de 5%
     * = 1,05. Padrão 1(=100%)
     *
     * @param indiceCorrecao
     */
    public void setIndiceCorrecao(BigDecimal indiceCorrecao) {
        this.indiceCorrecao = indiceCorrecao;
    }

    /**
     * Quantidade de dias de flexibilidade das datas de vencimento dos boletos que podem ser ajustados pelo vendedor
     * para antes ou depois da data definida. Exemplo: a 1ª parcela vence em 20 dias; se houver uma tolerância de 10
     * dias, o vendedor pode gerar um boleto para 10 até 30 dias.
     *
     * @return
     */
    public Integer getDiasTolerancia() {
        return diasTolerancia;
    }

    /**
     * Quantidade de dias de flexibilidade das datas de vencimento dos boletos que podem ser ajustados pelo vendedor
     * para antes ou depois da data definida. Exemplo: a 1ª parcela vence em 20 dias; se houver uma tolerância de 10
     * dias, o vendedor pode gerar um boleto para 10 até 30 dias.
     *
     * @param diasTolerancia
     */
    public void setDiasTolerancia(Integer diasTolerancia) {
        this.diasTolerancia = diasTolerancia;
    }

    /**
     * Permite o ajuste no valor das parcelas de acordo com a tolerância.
     *
     * @return
     */
    public BigDecimal getValorTolerancia() {
        return valorTolerancia;
    }

    /**
     * Permite o ajuste no valor das parcelas de acordo com a tolerância.
     *
     * @param valorTolerancia
     */
    public void setValorTolerancia(BigDecimal valorTolerancia) {
        this.valorTolerancia = valorTolerancia;
    }

    /**
     * Calculado pelo sistema de acordo com os prazos definidos para cada parcela.
     *
     * @return
     */
    public Integer getPrazoMedio() {
        return prazoMedio;
    }

    /**
     * Calculado pelo sistema de acordo com os prazos definidos para cada parcela.
     *
     * @param prazoMedio
     */
    public void setPrazoMedio(Integer prazoMedio) {
        this.prazoMedio = prazoMedio;
    }

    /**
     * V=VISTA | P=PRAZO
     *
     * @return
     */
    public String getVistaPrazo() {
        return vistaPrazo;
    }

    /**
     * V=VISTA | P=PRAZO
     *
     * @param vistaPrazo
     */
    public void setVistaPrazo(String vistaPrazo) {
        this.vistaPrazo = vistaPrazo;
    }

    public FinTipoRecebimento getTipoRecebimento() {
        return tipoRecebimento;
    }

    public void setTipoRecebimento(FinTipoRecebimento tipoRecebimento) {
        this.tipoRecebimento = tipoRecebimento;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public List<VendaCondicoesParcelas> getParcelas() {
        return parcelas;
    }

    public void setParcelas(List<VendaCondicoesParcelas> parcelas) {
        this.parcelas = parcelas;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.id);
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
        final VendaCondicoesPagamento other = (VendaCondicoesPagamento) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "VendaCondicoesPagamento{" + "id=" + id + '}';
    }

}
