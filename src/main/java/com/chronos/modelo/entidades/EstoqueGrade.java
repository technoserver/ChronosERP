
package com.chronos.modelo.entidades;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;


@Entity
@Table(name = "ESTOQUE_GRADE")
public class EstoqueGrade implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @NotNull
    @Column(name = "id_produto")
    private Integer idproduto;
    @NotNull
    @Column(name = "id_empresa")
    private Integer idempresa;
    @Column(name = "CODIGO")
    private String codigo;
    @Column(name = "QUANTIDADE")
    private BigDecimal quantidade;
    @Column(name = "verificado")
    private BigDecimal verificado;
    @JoinColumn(name = "ID_ESTOQUE_COR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EstoqueCor estoqueCor;
    @JoinColumn(name = "ID_ESTOQUE_TAMANHO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EstoqueTamanho estoqueTamanho;

    @Transient
    private BigDecimal quantidadeEntrada;

    public EstoqueGrade() {
        this.quantidade = BigDecimal.ZERO;
        this.verificado = BigDecimal.ZERO;
        this.quantidadeEntrada = BigDecimal.ZERO;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public EstoqueCor getEstoqueCor() {
        return estoqueCor;
    }

    public void setEstoqueCor(EstoqueCor estoqueCor) {
        this.estoqueCor = estoqueCor;
    }

    public EstoqueTamanho getEstoqueTamanho() {
        return estoqueTamanho;
    }

    public void setEstoqueTamanho(EstoqueTamanho estoqueTamanho) {
        this.estoqueTamanho = estoqueTamanho;
    }

    public Integer getIdproduto() {
        return idproduto;
    }

    public void setIdproduto(Integer idproduto) {
        this.idproduto = idproduto;
    }

    public Integer getIdempresa() {
        return idempresa;
    }

    public void setIdempresa(Integer idempresa) {
        this.idempresa = idempresa;
    }

    public BigDecimal getVerificado() {
        return verificado;
    }

    public void setVerificado(BigDecimal verificado) {
        this.verificado = verificado;
    }

    public BigDecimal getQuantidadeEntrada() {
        return quantidadeEntrada;
    }

    public void setQuantidadeEntrada(BigDecimal quantidadeEntrada) {
        this.quantidadeEntrada = quantidadeEntrada;
    }

    public boolean isPodeRemover() {
        return (this.quantidade != null && this.quantidade.signum() <= 0) || (this.verificado != null && this.verificado.signum() <= 0);
    }

    @Override
   public String toString() {
      return "EstoqueGrade{" + "id=" + id + '}';
   }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EstoqueGrade that = (EstoqueGrade) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(idproduto, that.idproduto) &&
                Objects.equals(idempresa, that.idempresa) &&
                Objects.equals(codigo, that.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idproduto, idempresa, codigo);
    }
}
