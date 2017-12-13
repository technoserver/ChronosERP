
package com.chronos.modelo.entidades;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;


@Entity
@Table(name = "COMISSAO_OBJETIVO")
public class ComissaoObjetivo implements Serializable {

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
    @Column(name = "DESCRICAO")
    private String descricao;
    @Column(name = "FORMA_PAGAMENTO")
    private String formaPagamento;
    @Column(name = "TAXA_PAGAMENTO")
    private BigDecimal taxaPagamento;
    @Column(name = "VALOR_PAGAMENTO")
    private BigDecimal valorPagamento;
    @Column(name = "VALOR_META")
    private BigDecimal valorMeta;
    @Column(name = "QUANTIDADE")
    private BigDecimal quantidade;
    @JoinColumn(name = "ID_COMISSAO_PERFIL", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ComissaoPerfil comissaoPerfil;
    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @ManyToOne
    private Produto produto;

    public ComissaoObjetivo() {
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    /**
     * DESCRICAO COM EXPLICAÇÃO DETALHADA DO OBJETIVO
     * @return 
     */
    public String getDescricao() {
        return descricao;
    }
    /**
     * DESCRICAO COM EXPLICAÇÃO DETALHADA DO OBJETIVO
     * @param descricao 
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    /**
     * 0=VALOR FIXO | 1=PERCENTUAL
     * @return 
     */
    public String getFormaPagamento() {
        return formaPagamento;
    }
    /**
     * 0=VALOR FIXO | 1=PERCENTUAL
     * @param formaPagamento 
     */
    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
    /**
     * TAXA A SER UTILIZADA PARA O PAGAMENTO POR PERCENTUAL
     * @return 
     */
    public BigDecimal getTaxaPagamento() {
        return taxaPagamento;
    }
    /**
     * TAXA A SER UTILIZADA PARA O PAGAMENTO POR PERCENTUAL
     * @param taxaPagamento 
     */
    public void setTaxaPagamento(BigDecimal taxaPagamento) {
        this.taxaPagamento = taxaPagamento;
    }
    /**
     *  VALOR FIXO A SER PAGO CASO SE ATINJA A META
     * @return 
     */
    public BigDecimal getValorPagamento() {
        return valorPagamento;
    }
    /**
     *  VALOR FIXO A SER PAGO CASO SE ATINJA A META
     * @param valorPagamento 
     */
    public void setValorPagamento(BigDecimal valorPagamento) {
        this.valorPagamento = valorPagamento;
    }
    /**
     * VALOR MONETARIO DA META A SER ATINGIDA
     * @return 
     */
    public BigDecimal getValorMeta() {
        return valorMeta;
    }
    /**
     * VALOR MONETARIO DA META A SER ATINGIDA
     * @param valorMeta 
     */
    public void setValorMeta(BigDecimal valorMeta) {
        this.valorMeta = valorMeta;
    }
    /**
     * UTILIZADO QUANDO A META FOR A QUANTIDADE DE DETERMINADO PRODUTO
     * @return 
     */
    public BigDecimal getQuantidade() {
        return quantidade;
    }
    /**
     * UTILIZADO QUANDO A META FOR A QUANTIDADE DE DETERMINADO PRODUTO
     * @param quantidade 
     */
    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public ComissaoPerfil getComissaoPerfil() {
        return comissaoPerfil;
    }

    public void setComissaoPerfil(ComissaoPerfil comissaoPerfil) {
        this.comissaoPerfil = comissaoPerfil;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id);
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
        final ComissaoObjetivo other = (ComissaoObjetivo) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return  nome ;
    }

   
}
