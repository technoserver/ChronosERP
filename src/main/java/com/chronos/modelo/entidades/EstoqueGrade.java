
package com.chronos.modelo.entidades;



import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "ESTOQUE_GRADE")
public class EstoqueGrade implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CODIGO")
    private String codigo;
    @Column(name = "QUANTIDADE")
    private BigDecimal quantidade;
    @JoinColumn(name = "ID_ESTOQUE_COR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EstoqueCor estoqueCor;
    @JoinColumn(name = "ID_ESTOQUE_TAMANHO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EstoqueTamanho estoqueTamanho;
    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Produto produto;
    @JoinColumn(name = "ID_ESTOQUE_SABOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EstoqueSabor estoqueSabor;
    @JoinColumn(name = "ID_ESTOQUE_MARCA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EstoqueMarca estoqueMarca;

    public EstoqueGrade() {
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

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public EstoqueSabor getEstoqueSabor() {
        return estoqueSabor;
    }

    public void setEstoqueSabor(EstoqueSabor estoqueSabor) {
        this.estoqueSabor = estoqueSabor;
    }

    public EstoqueMarca getEstoqueMarca() {
        return estoqueMarca;
    }

    public void setEstoqueMarca(EstoqueMarca estoqueMarca) {
        this.estoqueMarca = estoqueMarca;
    }

   @Override
   public String toString() {
      return "EstoqueGrade{" + "id=" + id + '}';
   }

   

}
