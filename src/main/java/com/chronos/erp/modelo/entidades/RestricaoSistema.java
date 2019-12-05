
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "RESTRICAO_SISTEMA")
public class RestricaoSistema implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "desconto_venda")
    @DecimalMin(value = "0.01", message = "O valor  do desconto deve ser maior que 0,1")
    @DecimalMax(value = "99.99", message = "O valor  deve ser menor que 99.99")
    private BigDecimal descontoVenda;
    @Column(name = "altera_preco_na_venda")
    private String alteraPrecoNaVenda;
    @Column(name = "bloquear_venda_por_limite_credito")
    private String bloquearVendaPorLimiteCredito;

    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private Usuario usuario;

    public RestricaoSistema() {
        this.alteraPrecoNaVenda = "S";
        this.bloquearVendaPorLimiteCredito = "N";
        this.descontoVenda = BigDecimal.ZERO;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getDescontoVenda() {
        return descontoVenda;
    }

    public void setDescontoVenda(BigDecimal descontoVenda) {
        this.descontoVenda = descontoVenda;
    }

    public String getAlteraPrecoNaVenda() {
        return alteraPrecoNaVenda;
    }

    public void setAlteraPrecoNaVenda(String alteraPrecoNaVenda) {
        this.alteraPrecoNaVenda = alteraPrecoNaVenda;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getBloquearVendaPorLimiteCredito() {
        return bloquearVendaPorLimiteCredito;
    }

    public void setBloquearVendaPorLimiteCredito(String bloquearVendaPorLimiteCredito) {
        this.bloquearVendaPorLimiteCredito = bloquearVendaPorLimiteCredito;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RestricaoSistema)) return false;

        RestricaoSistema that = (RestricaoSistema) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
