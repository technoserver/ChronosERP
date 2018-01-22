
package com.chronos.modelo.entidades;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;


@Entity
@Table(name = "PDV_MOVIMENTO")
public class PdvMovimento implements Serializable {

    private static final long serialVersionUID = 2L;
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
//    @Column(name = "TOTAL_NAO_FISCAL")
    @Transient
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
    @JoinColumn(name = "ID_PDV_TURNO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private PdvTurno pdvTurno;
    @JoinColumn(name = "ID_PDV_OPERADOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private PdvOperador pdvOperador;
    @JoinColumn(name = "ID_PDV_CAIXA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private PdvCaixa pdvCaixa;

    public PdvMovimento() {
        this.totalRecebido = BigDecimal.ZERO;
        this.totalAcrescimo = BigDecimal.ZERO;
        this.totalCancelado = BigDecimal.ZERO;
        this.totalDesconto = BigDecimal.ZERO;
        this.totalFinal = BigDecimal.ZERO;
        this.totalSangria = BigDecimal.ZERO;
        this.totalSuprimento = BigDecimal.ZERO;
        this.totalTroco = BigDecimal.ZERO;
        this.totalVenda = BigDecimal.ZERO;
        this.statusMovimento = "A";
        this.pdvTurno = new PdvTurno(1);
        this.pdvCaixa = new PdvCaixa(1);
        this.pdvOperador = new PdvOperador(1);

    }

    public PdvMovimento(Integer id, Integer idGerenteSupervisor, Date dataAbertura, String horaAbertura, Date dataFechamento, String horaFechamento, BigDecimal totalSuprimento, BigDecimal totalSangria, BigDecimal totalVenda, BigDecimal totalDesconto, BigDecimal totalAcrescimo,  BigDecimal totalFinal,BigDecimal totalRecebido, BigDecimal totalTroco, BigDecimal totalCancelado, String statusMovimento,Integer idempresa) {
        this.id = id;
        this.idGerenteSupervisor = idGerenteSupervisor;
        this.dataAbertura = dataAbertura;
        this.horaAbertura = horaAbertura;
        this.dataFechamento = dataFechamento;
        this.horaFechamento = horaFechamento;
        this.totalSuprimento = totalSuprimento;
        this.totalSangria = totalSangria;
        this.totalFinal = totalFinal;
        this.totalVenda = totalVenda;
        this.totalDesconto = totalDesconto;
        this.totalAcrescimo = totalAcrescimo;
        this.totalRecebido = totalRecebido;
        this.totalTroco = totalTroco;
        this.totalCancelado = totalCancelado;
        this.statusMovimento = statusMovimento;
        this.empresa = new Empresa(1);
        this.pdvTurno = new PdvTurno(1);
        this.pdvOperador = new PdvOperador(1);
        this.pdvCaixa = new PdvCaixa(1);
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

    public PdvTurno getPdvTurno() {
        return pdvTurno;
    }

    public void setPdvTurno(PdvTurno pdvTurno) {
        this.pdvTurno = pdvTurno;
    }

    public PdvOperador getPdvOperador() {
        return pdvOperador;
    }

    public void setPdvOperador(PdvOperador pdvOperador) {
        this.pdvOperador = pdvOperador;
    }

    public PdvCaixa getPdvCaixa() {
        return pdvCaixa;
    }

    public void setPdvCaixa(PdvCaixa pdvCaixa) {
        this.pdvCaixa = pdvCaixa;
    }

    public BigDecimal calcularTotalFinal() {
        BigDecimal valor = BigDecimal.ZERO;

        totalFinal = valor
                .add(getTotalAcrescimo())
                .subtract(getTotalDesconto())
                .subtract(getTotalSangria())
                .add(getTotalSuprimento())
                .add(getTotalRecebido())
                .add(getTotalVenda())
                ;


        return totalFinal;
    }

}
