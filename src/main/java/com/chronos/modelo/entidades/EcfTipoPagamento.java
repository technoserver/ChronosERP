
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "ECF_TIPO_PAGAMENTO")
public class EcfTipoPagamento implements Serializable {

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
    @Column(name = "TEF")
    private String tef;
    @Column(name = "IMPRIME_VINCULADO")
    private String imprimeVinculado;
    @Column(name = "PERMITE_TROCO")
    private String permiteTroco;
    @Column(name = "TEF_TIPO_GP")
    private String tefTipoGp;
    @Column(name = "GERA_PARCELAS")
    private String geraParcelas;

    public EcfTipoPagamento() {
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

    public String getTef() {
        return tef;
    }

    public void setTef(String tef) {
        this.tef = tef;
    }

    public String getImprimeVinculado() {
        return imprimeVinculado;
    }

    public void setImprimeVinculado(String imprimeVinculado) {
        this.imprimeVinculado = imprimeVinculado;
    }

    public String getPermiteTroco() {
        return permiteTroco;
    }

    public void setPermiteTroco(String permiteTroco) {
        this.permiteTroco = permiteTroco;
    }

    public String getTefTipoGp() {
        return tefTipoGp;
    }

    public void setTefTipoGp(String tefTipoGp) {
        this.tefTipoGp = tefTipoGp;
    }

    public String getGeraParcelas() {
        return geraParcelas;
    }

    public void setGeraParcelas(String geraParcelas) {
        this.geraParcelas = geraParcelas;
    }


    @Override
    public String toString() {
        return "com.t2tierp.pafecf.java.EcfTipoPagamentoVO[id=" + id + "]";
    }

}
