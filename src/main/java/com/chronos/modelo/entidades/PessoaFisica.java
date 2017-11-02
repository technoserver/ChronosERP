
package com.chronos.modelo.entidades;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "PESSOA_FISICA")
public class PessoaFisica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CPF", unique = true)
    private String cpf;
    @Column(name = "RG")
    private String rg;
    @Column(name = "ORGAO_RG")
    private String orgaoRg;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_EMISSAO_RG")
    private Date dataEmissaoRg;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_NASCIMENTO")
    private Date dataNascimento;
    @Column(name = "SEXO")
    private String sexo;
    @Column(name = "NATURALIDADE")
    private String naturalidade;
    @Column(name = "NACIONALIDADE")
    private String nacionalidade;
    @Column(name = "RACA")
    private String raca;
    @Column(name = "TIPO_SANGUE")
    private String tipoSangue;
    @Column(name = "CNH_NUMERO")
    private String cnhNumero;
    @Column(name = "CNH_CATEGORIA")
    private String cnhCategoria;
    @Temporal(TemporalType.DATE)
    @Column(name = "CNH_VENCIMENTO")
    private Date cnhVencimento;
    @Column(name = "TITULO_ELEITORAL_NUMERO")
    private String tituloEleitoralNumero;
    @Column(name = "TITULO_ELEITORAL_ZONA")
    private Integer tituloEleitoralZona;
    @Column(name = "TITULO_ELEITORAL_SECAO")
    private Integer tituloEleitoralSecao;
    @Column(name = "RESERVISTA_NUMERO")
    private String reservistaNumero;
    @Column(name = "RESERVISTA_CATEGORIA")
    private Integer reservistaCategoria;
    @Column(name = "NOME_MAE")
    private String nomeMae;
    @Column(name = "NOME_PAI")
    private String nomePai;
    @JoinColumn(name = "ID_ESTADO_CIVIL", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull(message = "Estado civil obrigatório")
    private EstadoCivil estadoCivil;
    @JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID")
    @OneToOne(optional = false)
    @NotNull(message = "Pessoa obrigatória")
    private Pessoa pessoa;

    public PessoaFisica() {
    }


    public PessoaFisica(Integer id, String nome) {
        this.id = id;
        this.pessoa = new Pessoa(nome);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getOrgaoRg() {
        return orgaoRg;
    }

    public void setOrgaoRg(String orgaoRg) {
        this.orgaoRg = orgaoRg;
    }

    public Date getDataEmissaoRg() {
        return dataEmissaoRg;
    }

    public void setDataEmissaoRg(Date dataEmissaoRg) {
        this.dataEmissaoRg = dataEmissaoRg;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNaturalidade() {
        return naturalidade;
    }

    public void setNaturalidade(String naturalidade) {
        this.naturalidade = naturalidade;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public String getTipoSangue() {
        return tipoSangue;
    }

    public void setTipoSangue(String tipoSangue) {
        this.tipoSangue = tipoSangue;
    }

    public String getCnhNumero() {
        return cnhNumero;
    }

    public void setCnhNumero(String cnhNumero) {
        this.cnhNumero = cnhNumero;
    }

    public String getCnhCategoria() {
        return cnhCategoria;
    }

    public void setCnhCategoria(String cnhCategoria) {
        this.cnhCategoria = cnhCategoria;
    }

    public Date getCnhVencimento() {
        return cnhVencimento;
    }

    public void setCnhVencimento(Date cnhVencimento) {
        this.cnhVencimento = cnhVencimento;
    }

    public String getTituloEleitoralNumero() {
        return tituloEleitoralNumero;
    }

    public void setTituloEleitoralNumero(String tituloEleitoralNumero) {
        this.tituloEleitoralNumero = tituloEleitoralNumero;
    }

    public Integer getTituloEleitoralZona() {
        return tituloEleitoralZona;
    }

    public void setTituloEleitoralZona(Integer tituloEleitoralZona) {
        this.tituloEleitoralZona = tituloEleitoralZona;
    }

    public Integer getTituloEleitoralSecao() {
        return tituloEleitoralSecao;
    }

    public void setTituloEleitoralSecao(Integer tituloEleitoralSecao) {
        this.tituloEleitoralSecao = tituloEleitoralSecao;
    }

    public String getReservistaNumero() {
        return reservistaNumero;
    }

    public void setReservistaNumero(String reservistaNumero) {
        this.reservistaNumero = reservistaNumero;
    }

    public Integer getReservistaCategoria() {
        return reservistaCategoria;
    }

    public void setReservistaCategoria(Integer reservistaCategoria) {
        this.reservistaCategoria = reservistaCategoria;
    }

    public String getNomeMae() {
        return nomeMae;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public String getNomePai() {
        return nomePai;
    }

    public void setNomePai(String nomePai) {
        this.nomePai = nomePai;
    }

    public EstadoCivil getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @Override
    public String toString() {
        return  cpf;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.cpf);
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
        final PessoaFisica other = (PessoaFisica) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.cpf, other.cpf)) {
            return false;
        }
        return true;
    }
    
    

}
