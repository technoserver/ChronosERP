
package com.chronos.modelo.entidades;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "ADM_PARAMETRO")
public class AdmParametro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "FIN_PARCELA_ABERTO")
    private Integer finParcelaAberto;
    @Column(name = "FIN_PARCELA_QUITADO")
    private Integer finParcelaQuitado;
    @Column(name = "FIN_PARCELA_QUITADO_PARCIAL")
    private Integer finParcelaQuitadoParcial;
    @Column(name = "FIN_TIPO_RECEBIMENTO_EDI")
    private Integer finTipoRecebimentoEdi;
    @Column(name = "COMPRA_FIN_DOC_ORIGEM")
    private Integer compraFinDocOrigem;
    @Column(name = "COMPRA_CONTA_CAIXA")
    private Integer compraContaCaixa;
    @Column(name = "tribut_operacao_fiscal_padrao")
    private Integer tributOperacaoFiscalPadrao;
    @Column(name = "faturar_venda")
    private Boolean faturarVenda;
    @Column(name = "frente_caixa")
    private Boolean frenteCaixa;
    @Column(name = "os_gerar_movimento_caixa")
    private String osGerarMovimentoCaixa;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empresa empresa;

    @Transient
    private TributOperacaoFiscal operacaoFiscal;

    public AdmParametro() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFinParcelaAberto() {
        return finParcelaAberto;
    }

    public void setFinParcelaAberto(Integer finParcelaAberto) {
        this.finParcelaAberto = finParcelaAberto;
    }

    public Integer getFinParcelaQuitado() {
        return finParcelaQuitado;
    }

    public void setFinParcelaQuitado(Integer finParcelaQuitado) {
        this.finParcelaQuitado = finParcelaQuitado;
    }

    public Integer getFinParcelaQuitadoParcial() {
        return finParcelaQuitadoParcial;
    }

    public void setFinParcelaQuitadoParcial(Integer finParcelaQuitadoParcial) {
        this.finParcelaQuitadoParcial = finParcelaQuitadoParcial;
    }

    public Integer getFinTipoRecebimentoEdi() {
        return finTipoRecebimentoEdi;
    }

    public void setFinTipoRecebimentoEdi(Integer finTipoRecebimentoEdi) {
        this.finTipoRecebimentoEdi = finTipoRecebimentoEdi;
    }

    public Integer getCompraFinDocOrigem() {
        return compraFinDocOrigem;
    }

    public void setCompraFinDocOrigem(Integer compraFinDocOrigem) {
        this.compraFinDocOrigem = compraFinDocOrigem;
    }

    public Integer getCompraContaCaixa() {
        return compraContaCaixa;
    }

    public void setCompraContaCaixa(Integer compraContaCaixa) {
        this.compraContaCaixa = compraContaCaixa;
    }

    public Integer getTributOperacaoFiscalPadrao() {
        return tributOperacaoFiscalPadrao;
    }

    public void setTributOperacaoFiscalPadrao(Integer tributOperacaoFiscalPadrao) {
        this.tributOperacaoFiscalPadrao = tributOperacaoFiscalPadrao;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Boolean getFaturarVenda() {
        return faturarVenda;
    }

    public void setFaturarVenda(Boolean faturarVenda) {
        this.faturarVenda = faturarVenda;
    }

    public TributOperacaoFiscal getOperacaoFiscal() {
        return operacaoFiscal;
    }

    public void setOperacaoFiscal(TributOperacaoFiscal operacaoFiscal) {
        this.operacaoFiscal = operacaoFiscal;
    }

    public Boolean getFrenteCaixa() {
        return frenteCaixa;
    }

    public void setFrenteCaixa(Boolean frenteCaixa) {
        this.frenteCaixa = frenteCaixa;
    }

    public String getOsGerarMovimentoCaixa() {
        return osGerarMovimentoCaixa;
    }

    public void setOsGerarMovimentoCaixa(String osGerarMovimentoCaixa) {
        this.osGerarMovimentoCaixa = osGerarMovimentoCaixa;
    }

    @Override
    public String toString() {
        return id.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
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
        final AdmParametro other = (AdmParametro) obj;
        return Objects.equals(this.id, other.id);
    }

    
}
