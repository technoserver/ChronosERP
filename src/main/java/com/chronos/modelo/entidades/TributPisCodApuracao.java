package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "TRIBUT_PIS_COD_APURACAO")
public class TributPisCodApuracao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CST_PIS")
    private String cstPis;
    @Column(name = "EFD_TABELA_435")
    private String efdTabela435;
    @Column(name = "MODALIDADE_BASE_CALCULO")
    private String modalidadeBaseCalculo;
    @Column(name = "PORCENTO_BASE_CALCULO")
    private BigDecimal porcentoBaseCalculo;
    @Column(name = "ALIQUOTA_PORCENTO")
    private BigDecimal aliquotaPorcento;
    @Column(name = "ALIQUOTA_UNIDADE")
    private BigDecimal aliquotaUnidade;
    @Column(name = "VALOR_PRECO_MAXIMO")
    private BigDecimal valorPrecoMaximo;
    @Column(name = "VALOR_PAUTA_FISCAL")
    private BigDecimal valorPautaFiscal;
    @JoinColumn(name = "ID_TRIBUT_OPERACAO_FISCAL", referencedColumnName = "ID")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private TributOperacaoFiscal tributOperacaoFiscal;

    public TributPisCodApuracao() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCstPis() {
        return cstPis;
    }

    public void setCstPis(String cstPis) {
        this.cstPis = cstPis;
    }

    public String getEfdTabela435() {
        return efdTabela435;
    }

    public void setEfdTabela435(String efdTabela435) {
        this.efdTabela435 = efdTabela435;
    }

    public String getModalidadeBaseCalculo() {
        return modalidadeBaseCalculo;
    }

    public void setModalidadeBaseCalculo(String modalidadeBaseCalculo) {
        this.modalidadeBaseCalculo = modalidadeBaseCalculo;
    }

    public BigDecimal getPorcentoBaseCalculo() {
        return porcentoBaseCalculo;
    }

    public void setPorcentoBaseCalculo(BigDecimal porcentoBaseCalculo) {
        this.porcentoBaseCalculo = porcentoBaseCalculo;
    }

    public BigDecimal getAliquotaPorcento() {
        return aliquotaPorcento;
    }

    public void setAliquotaPorcento(BigDecimal aliquotaPorcento) {
        this.aliquotaPorcento = aliquotaPorcento;
    }

    public BigDecimal getAliquotaUnidade() {
        return aliquotaUnidade;
    }

    public void setAliquotaUnidade(BigDecimal aliquotaUnidade) {
        this.aliquotaUnidade = aliquotaUnidade;
    }

    public BigDecimal getValorPrecoMaximo() {
        return valorPrecoMaximo;
    }

    public void setValorPrecoMaximo(BigDecimal valorPrecoMaximo) {
        this.valorPrecoMaximo = valorPrecoMaximo;
    }

    public BigDecimal getValorPautaFiscal() {
        return valorPautaFiscal;
    }

    public void setValorPautaFiscal(BigDecimal valorPautaFiscal) {
        this.valorPautaFiscal = valorPautaFiscal;
    }

    public TributOperacaoFiscal getTributOperacaoFiscal() {
        return tributOperacaoFiscal;
    }

    public void setTributOperacaoFiscal(TributOperacaoFiscal tributOperacaoFiscal) {
        this.tributOperacaoFiscal = tributOperacaoFiscal;
    }

    @Override
    public String toString() {
        return "TributPisCodApuracao{" + "id=" + id + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
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
        final TributPisCodApuracao other = (TributPisCodApuracao) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
