
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "PDV_TIPO_PAGAMENTO")
public class PdvTipoPagamento implements Serializable {

    private static final long serialVersionUID = 2L;
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
    @Transient
    private String identificador;

    public PdvTipoPagamento() {
    }

    public PdvTipoPagamento(Integer id) {
        this.id = id;
    }

    public PdvTipoPagamento(Integer id, String codigo) {
        this.id = id;
        this.codigo = codigo;
    }

    public PdvTipoPagamento(Integer id, String codigo, String descricao, String permiteTroco, String geraParcelas) {
        this.id = id;
        this.codigo = codigo;
        this.descricao = descricao;
        this.permiteTroco = permiteTroco;
        this.geraParcelas = geraParcelas;
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

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    @Transient
    public PdvTipoPagamento buscarPorCodigo(String codigo) {
        PdvTipoPagamento tipo;
        switch (codigo) {
            case "01":
                tipo = new PdvTipoPagamento(1, "01");
                break;
            case "02":
                tipo = new PdvTipoPagamento(2, "02");
                break;
            case "03":
                tipo = new PdvTipoPagamento(3, "03");
                break;
            case "04":
                tipo = new PdvTipoPagamento(4, "04");
                break;

            case "05":
                tipo = new PdvTipoPagamento(5, "05");
                break;

            case "14":
                tipo = new PdvTipoPagamento(6, "14");
                break;
            case "10":
                tipo = new PdvTipoPagamento(7, "10");
                break;
            case "11":
                tipo = new PdvTipoPagamento(8, "11");
                break;
            case "12":
                tipo = new PdvTipoPagamento(9, "12");
                break;
            case "13":
                tipo = new PdvTipoPagamento(10, "13");
                break;
            case "15":
                tipo = new PdvTipoPagamento(11, "15");
                break;
            case "90":
                tipo = new PdvTipoPagamento(12, "90");
                break;

            case "99":
                tipo = new PdvTipoPagamento(13, "99");
                break;

            default:
                tipo = new PdvTipoPagamento(1, "01");

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
        final PdvTipoPagamento other = (PdvTipoPagamento) obj;
        return Objects.equals(this.id, other.id);
    }

}
