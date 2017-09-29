
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "PRODUTO_ALTERACAO_ITEM")
public class ProdutoAlteracaoItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CODIGO")
    private String codigo;
    @Column(name = "NOME")
    private String nome;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_INICIAL")
    private Date dataInicial;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_FINAL")
    private Date dataFinal;
    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Produto produto;

    public ProdutoAlteracaoItem() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * -- COD_ANT_ITEM [Código anterior do item com relação à última informação apresentada.]
     *
     * @return
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * -- COD_ANT_ITEM [Código anterior do item com relação à última informação apresentada.]
     *
     * @param codigo
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * -- DESCR_ANT_ITEM [Descrição anterior do item]
     *
     * @return
     */
    public String getNome() {
        return nome;
    }

    /**
     * -- DESCR_ANT_ITEM [Descrição anterior do item]
     *
     * @param nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * -- DT_INI [Data inicial de utilização da descrição do item]
     *
     * @return
     */
    public Date getDataInicial() {
        return dataInicial;
    }

    /**
     * -- DT_INI [Data inicial de utilização da descrição do item]
     *
     * @param dataInicial
     */
    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    /**
     * -- DT_FIM [Data final de utilização da descrição do item]
     *
     * @return
     */
    public Date getDataFinal() {
        return dataFinal;
    }

    /**
     * -- DT_FIM [Data final de utilização da descrição do item]
     *
     * @param dataFinal
     */
    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @Override
    public String toString() {
        return "com.t2tierp.model.bean.cadastros.ProdutoAlteracaoItem[id=" + id + "]";
    }

}
