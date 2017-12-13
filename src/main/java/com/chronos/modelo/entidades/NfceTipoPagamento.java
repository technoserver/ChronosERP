
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "NFCE_TIPO_PAGAMENTO")
public class NfceTipoPagamento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CODIGO")
    private String codigo;
    @Column(name = "DESCRICAO")
    private String descricao;
    @Column(name = "PERMITE_TROCO")
    private String permiteTroco;
    @Column(name = "GERA_PARCELAS")
    private String geraParcelas;

    public NfceTipoPagamento() {
    }

    public NfceTipoPagamento(Integer id) {
        this.id = id;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPermiteTroco() {
        return permiteTroco;
    }

    public void setPermiteTroco(String permiteTroco) {
        this.permiteTroco = permiteTroco;
    }

    public String getGeraParcelas() {
        return geraParcelas;
    }

    public void setGeraParcelas(String geraParcelas) {
        this.geraParcelas = geraParcelas;
    }

    @Transient
    public NfceTipoPagamento buscarPorCodigo(String codigo) {
        NfceTipoPagamento tipo;
        switch (codigo) {
            case "01":
                tipo = new NfceTipoPagamento(1);
            case "02":
                tipo = new NfceTipoPagamento(2);
            case "03":
                tipo = new NfceTipoPagamento(3);
            case "04":
                tipo = new NfceTipoPagamento(4);
            default:
                tipo = new NfceTipoPagamento(1);

        }

        return tipo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final NfceTipoPagamento other = (NfceTipoPagamento) obj;
        return Objects.equals(this.id, other.id);
    }

}
