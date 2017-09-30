/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.modelo.entidades.view;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author John Vanderson M Lim
 */
@Entity
@Table(name = "view_tributacao_cofins")
public class ViewTributacaoCofins implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "id_tribut_grupo_tributario")
    private Integer idTributGrupoTributario;
    @Column(name = "id_tribut_operacao_fiscal")
    private Integer idTributOperacaoFiscal;
    @Column(name = "cst_cofins", length = 2)
    private String cstCofins;
    @Column(name = "efd_tabela_435", length = 2)
    private String efdTabela435;
    @Column(name = "modalidade_base_calculo")
    private Character modalidadeBaseCalculo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "porcento_base_calculo", precision = 18, scale = 6)
    private BigDecimal porcentoBaseCalculo;
    @Column(name = "aliquota_porcento", precision = 18, scale = 6)
    private BigDecimal aliquotaPorcento;
    @Column(name = "aliquota_unidade", precision = 18, scale = 6)
    private BigDecimal aliquotaUnidade;
    @Column(name = "valor_preco_maximo", precision = 18, scale = 6)
    private BigDecimal valorPrecoMaximo;
    @Column(name = "valor_pauta_fiscal", precision = 18, scale = 6)
    private BigDecimal valorPautaFiscal;

    public ViewTributacaoCofins() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdTributGrupoTributario() {
        return idTributGrupoTributario;
    }

    public void setIdTributGrupoTributario(Integer idTributGrupoTributario) {
        this.idTributGrupoTributario = idTributGrupoTributario;
    }

    public Integer getIdTributOperacaoFiscal() {
        return idTributOperacaoFiscal;
    }

    public void setIdTributOperacaoFiscal(Integer idTributOperacaoFiscal) {
        this.idTributOperacaoFiscal = idTributOperacaoFiscal;
    }

    public String getCstCofins() {
        return cstCofins;
    }

    public void setCstCofins(String cstCofins) {
        this.cstCofins = cstCofins;
    }

    public String getEfdTabela435() {
        return efdTabela435;
    }

    public void setEfdTabela435(String efdTabela435) {
        this.efdTabela435 = efdTabela435;
    }

    public Character getModalidadeBaseCalculo() {
        return modalidadeBaseCalculo;
    }

    public void setModalidadeBaseCalculo(Character modalidadeBaseCalculo) {
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final ViewTributacaoCofins other = (ViewTributacaoCofins) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }


}
