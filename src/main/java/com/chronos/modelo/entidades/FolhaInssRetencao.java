
package com.chronos.modelo.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "FOLHA_INSS_RETENCAO")
public class FolhaInssRetencao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "VALOR_MENSAL")
    private BigDecimal valorMensal;
    @Column(name = "VALOR_13")
    private BigDecimal valor13;
    @JoinColumn(name = "ID_FOLHA_INSS_SERVICO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FolhaInssServico folhaInssServico;
    @JoinColumn(name = "ID_FOLHA_INSS", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FolhaInss folhaInss;

    public FolhaInssRetencao() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getValorMensal() {
        return valorMensal;
    }

    public void setValorMensal(BigDecimal valorMensal) {
        this.valorMensal = valorMensal;
    }

    public BigDecimal getValor13() {
        return valor13;
    }

    public void setValor13(BigDecimal valor13) {
        this.valor13 = valor13;
    }

    public FolhaInssServico getFolhaInssServico() {
        return folhaInssServico;
    }

    public void setFolhaInssServico(FolhaInssServico folhaInssServico) {
        this.folhaInssServico = folhaInssServico;
    }

    public FolhaInss getFolhaInss() {
        return folhaInss;
    }

    public void setFolhaInss(FolhaInss folhaInss) {
        this.folhaInss = folhaInss;
    }

   @Override
   public String toString() {
      return "FolhaInssRetencao{" + "id=" + id + '}';
   }

 
}
