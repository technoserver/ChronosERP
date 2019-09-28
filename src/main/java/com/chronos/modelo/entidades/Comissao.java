
package com.chronos.modelo.entidades;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Entity
@Table(name = "comissao")
public class Comissao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "VALOR_VENDA")
    private BigDecimal valorVenda;
    @Column(name = "TIPO_CONTABIL")
    private String tipoContabil;
    @Column(name = "VALOR_COMISSAO")
    private BigDecimal valorComissao;
    @Column(name = "SITUACAO")
    private String situacao;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_LANCAMENTO")
    private Date dataLancamento;
    @Column(name = "numero_documento")
    private String numeroDocumento;
    @Column(name = "codigo_modulo")
    private String codigoModulo;
    @JoinColumn(name = "id_colaborador", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Colaborador colaborador;

    public Comissao() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(BigDecimal valorVenda) {
        this.valorVenda = valorVenda;
    }

    /**
     * -- Indicar se este lanlçamento é a DEBITO ou a CREDITO. Ex. se houver uma devolucao de venda haverá um lançamento a DEBITO
     *
     * @return
     */
    public String getTipoContabil() {
        return tipoContabil;
    }

    /**
     * -- Indicar se este lanlçamento é a DEBITO ou a CREDITO. Ex. se houver uma devolucao de venda haverá um lançamento a DEBITO
     *
     * @param tipoContabil
     */
    public void setTipoContabil(String tipoContabil) {
        this.tipoContabil = tipoContabil;
    }

    public BigDecimal getValorComissao() {
        return valorComissao;
    }

    public void setValorComissao(BigDecimal valorComissao) {
        this.valorComissao = valorComissao;
    }

    /**
     * Q=QUITADA quando a comisão foi efetivamente paga ao vendedor | A= ABERTO, a comissão ainda não foi paga ao vendedor
     *
     * @return
     */
    public String getSituacao() {
        return situacao;
    }

    /**
     * Q=QUITADA quando a comisão foi efetivamente paga ao vendedor | A= ABERTO, a comissão ainda não foi paga ao vendedor
     *
     * @param situacao
     */
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getCodigoModulo() {
        return codigoModulo;
    }

    public void setCodigoModulo(String codigoModulo) {
        this.codigoModulo = codigoModulo;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comissao)) return false;

        Comissao that = (Comissao) o;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
