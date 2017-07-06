
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@Entity
@Table(name = "NFE_IMPORTACAO_DETALHE")
public class NfeImportacaoDetalhe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NUMERO_ADICAO")
    private Integer numeroAdicao;
    @Column(name = "NUMERO_SEQUENCIAL")
    private Integer numeroSequencial;
    @Column(name = "CODIGO_FABRICANTE_ESTRANGEIRO")
    private String codigoFabricanteEstrangeiro;
    @Column(name = "VALOR_DESCONTO")
    private BigDecimal valorDesconto;
    @Column(name = "DRAWBACK")
    private Integer drawback;
    @JoinColumn(name = "ID_NFE_DECLARACAO_IMPORTACAO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private NfeDeclaracaoImportacao nfeDeclaracaoImportacao;

    public NfeImportacaoDetalhe() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumeroAdicao() {
        return numeroAdicao;
    }

    public void setNumeroAdicao(Integer numeroAdicao) {
        this.numeroAdicao = numeroAdicao;
    }

    public Integer getNumeroSequencial() {
        return numeroSequencial;
    }

    public void setNumeroSequencial(Integer numeroSequencial) {
        this.numeroSequencial = numeroSequencial;
    }

    public String getCodigoFabricanteEstrangeiro() {
        return codigoFabricanteEstrangeiro;
    }

    public void setCodigoFabricanteEstrangeiro(String codigoFabricanteEstrangeiro) {
        this.codigoFabricanteEstrangeiro = codigoFabricanteEstrangeiro;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public Integer getDrawback() {
        return drawback;
    }

    public void setDrawback(Integer drawback) {
        this.drawback = drawback;
    }

    public NfeDeclaracaoImportacao getNfeDeclaracaoImportacao() {
        return nfeDeclaracaoImportacao;
    }

    public void setNfeDeclaracaoImportacao(NfeDeclaracaoImportacao nfeDeclaracaoImportacao) {
        this.nfeDeclaracaoImportacao = nfeDeclaracaoImportacao;
    }



}
