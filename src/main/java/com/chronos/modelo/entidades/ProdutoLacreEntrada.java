
package com.chronos.modelo.entidades;


import java.io.Serializable;
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
@Table(name = "PRODUTO_LACRE_ENTRADA")
public class ProdutoLacreEntrada implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @JoinColumn(name = "ID_LACRE_PRODUTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ProdutoLacre produtoLacre;
    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Produto produto;

    public ProdutoLacreEntrada() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProdutoLacre getProdutoLacre() {
        return produtoLacre;
    }

    public void setProdutoLacre(ProdutoLacre produtoLacre) {
        this.produtoLacre = produtoLacre;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

   @Override
   public String toString() {
      return "ProdutoLacreEntrada{" + "id=" + id + '}';
   }

   

}
