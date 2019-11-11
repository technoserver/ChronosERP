
package com.chronos.erp.modelo.entidades;

import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "PESSOA_JURIDICA")
public class PessoaJuridica implements Serializable {

    private static final long serialVersionUID = 2L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CNPJ", unique = true)
    private String cnpj;
    @Column(name = "FANTASIA")
    private String fantasia;
    @Column(name = "INSCRICAO_MUNICIPAL")
    private String inscricaoMunicipal;
    @Column(name = "INSCRICAO_ESTADUAL")
    private String inscricaoEstadual;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_CONSTITUICAO")
    private Date dataConstituicao;
    @Column(name = "TIPO_REGIME")
    private String tipoRegime;
    @Column(name = "CRT")
    private String crt;
    @Column(name = "SUFRAMA")
    private String suframa;
    @JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private Pessoa pessoa;

    public PessoaJuridica() {
    }

    public PessoaJuridica(Integer id, String nome) {
        this.id = id;
        this.pessoa = new Pessoa(nome);
    }

    @PreUpdate
    @PrePersist
    private void prePersit() {
        cnpj = StringUtils.isEmpty(cnpj) ? "" : cnpj.replaceAll("\\D", "");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getFantasia() {
        return fantasia;
    }

    public void setFantasia(String fantasia) {
        this.fantasia = fantasia;
    }

    public String getInscricaoMunicipal() {
        return inscricaoMunicipal;
    }

    public void setInscricaoMunicipal(String inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public Date getDataConstituicao() {
        return dataConstituicao;
    }

    public void setDataConstituicao(Date dataConstituicao) {
        this.dataConstituicao = dataConstituicao;
    }

    public String getTipoRegime() {
        return tipoRegime;
    }

    public void setTipoRegime(String tipoRegime) {
        this.tipoRegime = tipoRegime;
    }

    public String getCrt() {
        return crt;
    }

    public void setCrt(String crt) {
        this.crt = crt;
    }

    public String getSuframa() {
        return suframa;
    }

    public void setSuframa(String suframa) {
        this.suframa = suframa;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public String toString() {
        return cnpj;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PessoaJuridica other = (PessoaJuridica) obj;
        return Objects.equals(this.id, other.id);
    }
}
