package com.chronos.modelo.entidades;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TRIBUT_CONFIGURA_OF_GT")
public class TributConfiguraOfGt implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @JoinColumn(name = "ID_TRIBUT_OPERACAO_FISCAL", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TributOperacaoFiscal tributOperacaoFiscal;
    @JoinColumn(name = "ID_TRIBUT_GRUPO_TRIBUTARIO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TributGrupoTributario tributGrupoTributario;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tributConfiguraOfGt", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TributIcmsUf> listaTributIcmsUf;
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "tributConfiguraOfGt", cascade = CascadeType.ALL)
    private TributPisCodApuracao tributPisCodApuracao;
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "tributConfiguraOfGt", cascade = CascadeType.ALL)
    private TributCofinsCodApuracao tributCofinsCodApuracao;
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "tributConfiguraOfGt", cascade = CascadeType.ALL)
    private TributIpiDipi tributIpiDipi;   
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "tributConfiguraOfGt", cascade = CascadeType.ALL)
    private TributIss tributIss;
    

    public TributConfiguraOfGt() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TributOperacaoFiscal getTributOperacaoFiscal() {
        return tributOperacaoFiscal;
    }

    public void setTributOperacaoFiscal(TributOperacaoFiscal tributOperacaoFiscal) {
        this.tributOperacaoFiscal = tributOperacaoFiscal;
    }

    public TributGrupoTributario getTributGrupoTributario() {
        return tributGrupoTributario;
    }

    public void setTributGrupoTributario(TributGrupoTributario tributGrupoTributario) {
        this.tributGrupoTributario = tributGrupoTributario;
    }

 
    public Set<TributIcmsUf> getListaTributIcmsUf() {
        return listaTributIcmsUf;
    }

    public void setListaTributIcmsUf(Set<TributIcmsUf> listaTributIcmsUf) {
        this.listaTributIcmsUf = listaTributIcmsUf;
    }

    public TributPisCodApuracao getTributPisCodApuracao() {
        return tributPisCodApuracao;
    }

    public void setTributPisCodApuracao(TributPisCodApuracao tributPisCodApuracao) {
        this.tributPisCodApuracao = tributPisCodApuracao;
    }

    public TributCofinsCodApuracao getTributCofinsCodApuracao() {
        return tributCofinsCodApuracao;
    }

    public void setTributCofinsCodApuracao(TributCofinsCodApuracao tributCofinsCodApuracao) {
        this.tributCofinsCodApuracao = tributCofinsCodApuracao;
    }

    public TributIpiDipi getTributIpiDipi() {
        return tributIpiDipi;
    }

    public void setTributIpiDipi(TributIpiDipi tributIpiDipi) {
        this.tributIpiDipi = tributIpiDipi;
    }

    public TributIss getTributIss() {
        return tributIss;
    }

    public void setTributIss(TributIss tributIss) {
        this.tributIss = tributIss;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final TributConfiguraOfGt other = (TributConfiguraOfGt) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TributConfiguraOfGt{" + "id=" + id + '}';
    }
    
    

}
