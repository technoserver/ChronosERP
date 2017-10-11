
package com.chronos.modelo.entidades;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;


@Entity
@Table(name = "NFCE_MOVIMENTO")
public class NfceMovimento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "ID_GERENTE_SUPERVISOR")
    private Integer idGerenteSupervisor;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_ABERTURA")
    private Date dataAbertura;
    @Column(name = "HORA_ABERTURA")
    private String horaAbertura;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_FECHAMENTO")
    private Date dataFechamento;
    @Column(name = "HORA_FECHAMENTO")
    private String horaFechamento;
    @Column(name = "TOTAL_SUPRIMENTO")
    private BigDecimal totalSuprimento;
    @Column(name = "TOTAL_SANGRIA")
    private BigDecimal totalSangria;
    @Column(name = "TOTAL_NAO_FISCAL")
    private BigDecimal totalNaoFiscal;
    @Column(name = "TOTAL_VENDA")
    private BigDecimal totalVenda;
    @Column(name = "TOTAL_DESCONTO")
    private BigDecimal totalDesconto;
    @Column(name = "TOTAL_ACRESCIMO")
    private BigDecimal totalAcrescimo;
    @Column(name = "TOTAL_FINAL")
    private BigDecimal totalFinal;
    @Column(name = "TOTAL_RECEBIDO")
    private BigDecimal totalRecebido;
    @Column(name = "TOTAL_TROCO")
    private BigDecimal totalTroco;
    @Column(name = "TOTAL_CANCELADO")
    private BigDecimal totalCancelado;
    @Column(name = "STATUS_MOVIMENTO")
    private String statusMovimento;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empresa empresa;
    @JoinColumn(name = "ID_NFCE_TURNO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private NfceTurno nfceTurno;
    @JoinColumn(name = "ID_NFCE_OPERADOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private NfceOperador nfceOperador;
    @JoinColumn(name = "ID_NFCE_CAIXA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private NfceCaixa nfceCaixa;

    public NfceMovimento() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdGerenteSupervisor() {
        return idGerenteSupervisor;
    }

    public void setIdGerenteSupervisor(Integer idGerenteSupervisor) {
        this.idGerenteSupervisor = idGerenteSupervisor;
    }

    public Date getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(Date dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public String getHoraAbertura() {
        return horaAbertura;
    }

    public void setHoraAbertura(String horaAbertura) {
        this.horaAbertura = horaAbertura;
    }

    public Date getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(Date dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public String getHoraFechamento() {
        return horaFechamento;
    }

    public void setHoraFechamento(String horaFechamento) {
        this.horaFechamento = horaFechamento;
    }

    public BigDecimal getTotalSuprimento() {
        return Optional.ofNullable(totalSuprimento).orElse(BigDecimal.ZERO);
    }

    public void setTotalSuprimento(BigDecimal totalSuprimento) {
        this.totalSuprimento = totalSuprimento;
    }

    public BigDecimal getTotalSangria() {
        return Optional.ofNullable(totalSangria).orElse(BigDecimal.ZERO);
    }

    public void setTotalSangria(BigDecimal totalSangria) {
        this.totalSangria = totalSangria;
    }

    public BigDecimal getTotalNaoFiscal() {
        return totalNaoFiscal;
    }

    public void setTotalNaoFiscal(BigDecimal totalNaoFiscal) {
        this.totalNaoFiscal = totalNaoFiscal;
    }

    public BigDecimal getTotalVenda() {
        return Optional.ofNullable(totalVenda).orElse(BigDecimal.ZERO);
    }

    public void setTotalVenda(BigDecimal totalVenda) {
        this.totalVenda = totalVenda;
    }

    public BigDecimal getTotalDesconto() {
        return Optional.ofNullable(totalDesconto).orElse(BigDecimal.ZERO);
    }

    public void setTotalDesconto(BigDecimal totalDesconto) {
        this.totalDesconto = totalDesconto;
    }

    public BigDecimal getTotalAcrescimo() {
        return Optional.ofNullable(totalAcrescimo).orElse(BigDecimal.ZERO);
    }

    public void setTotalAcrescimo(BigDecimal totalAcrescimo) {
        this.totalAcrescimo = totalAcrescimo;
    }

    public BigDecimal getTotalFinal() {
        return totalFinal;
    }

    public void setTotalFinal(BigDecimal totalFinal) {
        this.totalFinal = totalFinal;
    }

    public BigDecimal getTotalRecebido() {
        return Optional.ofNullable(totalRecebido).orElse(BigDecimal.ZERO);
    }

    public void setTotalRecebido(BigDecimal totalRecebido) {
        this.totalRecebido = totalRecebido;
    }

    public BigDecimal getTotalTroco() {
        return totalTroco;
    }

    public void setTotalTroco(BigDecimal totalTroco) {
        this.totalTroco = totalTroco;
    }

    public BigDecimal getTotalCancelado() {
        return totalCancelado;
    }

    public void setTotalCancelado(BigDecimal totalCancelado) {
        this.totalCancelado = totalCancelado;
    }

    /**
     * -- A=ABERTO | F=FECHADO | T=FECHADO TEMPORARIAMENTE
     *
     * @return
     */
    public String getStatusMovimento() {
        return statusMovimento;
    }

    /**
     * -- A=ABERTO | F=FECHADO | T=FECHADO TEMPORARIAMENTE
     *
     * @param statusMovimento
     */
    public void setStatusMovimento(String statusMovimento) {
        this.statusMovimento = statusMovimento;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public NfceTurno getNfceTurno() {
        return nfceTurno;
    }

    public void setNfceTurno(NfceTurno nfceTurno) {
        this.nfceTurno = nfceTurno;
    }

    public NfceOperador getNfceOperador() {
        return nfceOperador;
    }

    public void setNfceOperador(NfceOperador nfceOperador) {
        this.nfceOperador = nfceOperador;
    }

    public NfceCaixa getNfceCaixa() {
        return nfceCaixa;
    }

    public void setNfceCaixa(NfceCaixa nfceCaixa) {
        this.nfceCaixa = nfceCaixa;
    }


    public BigDecimal calcularTotalFinal() {
        totalFinal = BigDecimal.ZERO;

        totalFinal = totalFinal
                .add(getTotalAcrescimo())
                .subtract(getTotalDesconto())
                .subtract(getTotalSangria())
                .add(getTotalSuprimento())
                .add(getTotalRecebido())
                .add(getTotalVenda());


        return totalFinal;
    }

}
