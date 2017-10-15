
package com.chronos.modelo.entidades;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;



@Entity
@Table(name = "PESSOA")
public class Pessoa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NOME")
    @NotNull(message = "Nome Obrigatório")
    private String nome;
    @Column(name = "TIPO")
    private String tipo;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "SITE")
    private String site;
    @Column(name = "CLIENTE")
    private String cliente;
    @Column(name = "FORNECEDOR")
    private String fornecedor;
    @Column(name = "COLABORADOR")
    private String colaborador;
    @Column(name = "TRANSPORTADORA")
    private String transportadora;
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "pessoa", cascade = CascadeType.ALL)
    private PessoaFisica pessoaFisica;
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "pessoa", cascade = CascadeType.ALL)
    private PessoaJuridica pessoaJuridica;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PessoaEndereco> listaPessoaEndereco;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PessoaContato> listaPessoaContato;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PessoaTelefone> listaPessoaTelefone;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "EMPRESA_PESSOA", joinColumns = {
        @JoinColumn(name = "ID_PESSOA")}, inverseJoinColumns = {
        @JoinColumn(name = "ID_EMPRESA")})
    private Set<Empresa> listaEmpresa;

    public Pessoa() {
    }

    public Pessoa(Integer id,String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Pessoa(String nome) {
        this.nome = nome;
    }

    public Pessoa(Integer id, String nome, String cliente, String fornecedor, String colaborador, String transportadora, PessoaFisica pessoaFisica) {
        this.id = id;
        this.nome = nome;
        this.cliente = cliente;
        this.fornecedor = fornecedor;
        this.colaborador = colaborador;
        this.transportadora = transportadora;
        this.pessoaFisica = pessoaFisica;
    }

    public Pessoa(Integer id, String nome, String tipo, String cliente, String fornecedor, String colaborador, String transportadora, PessoaJuridica pessoaJuridica) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.cliente = cliente;
        this.fornecedor = fornecedor;
        this.colaborador = colaborador;
        this.transportadora = transportadora;
        this.pessoaJuridica = pessoaJuridica;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getColaborador() {
        return colaborador;
    }

    public void setColaborador(String colaborador) {
        this.colaborador = colaborador;
    }

    public String getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(String transportadora) {
        this.transportadora = transportadora;
    }

    public PessoaFisica getPessoaFisica() {
        return pessoaFisica;
    }

    public void setPessoaFisica(PessoaFisica pessoaFisica) {
        this.pessoaFisica = pessoaFisica;
    }

    public PessoaJuridica getPessoaJuridica() {
        return pessoaJuridica;
    }

    public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
        this.pessoaJuridica = pessoaJuridica;
    }

    public Set<PessoaEndereco> getListaPessoaEndereco() {
        return listaPessoaEndereco;
    }

    public void setListaPessoaEndereco(Set<PessoaEndereco> listaPessoaEndereco) {
        this.listaPessoaEndereco = listaPessoaEndereco;
    }

    public Set<PessoaContato> getListaPessoaContato() {
        return listaPessoaContato;
    }

    public void setListaPessoaContato(Set<PessoaContato> listaPessoaContato) {
        this.listaPessoaContato = listaPessoaContato;
    }

    public Set<PessoaTelefone> getListaPessoaTelefone() {
        return listaPessoaTelefone;
    }

    public void setListaPessoaTelefone(Set<PessoaTelefone> listaPessoaTelefone) {
        this.listaPessoaTelefone = listaPessoaTelefone;
    }

    public Set<Empresa> getListaEmpresa() {
        return listaEmpresa;
    }

    public void setListaEmpresa(Set<Empresa> listaEmpresa) {
        this.listaEmpresa = listaEmpresa;
    }


    public String getIdentificador(){
        String identificador;
        identificador = this.tipo.equals("F")?this.pessoaFisica.getCpf():this.pessoaJuridica.getCnpj();
        return identificador!=null?identificador.replaceAll("\\D",""):"";
    }

    public String getRgOrIe() {
        String identificador;
        identificador = this.tipo.equals("F") ? this.pessoaFisica.getRg() : this.pessoaJuridica.getInscricaoEstadual();
        return identificador;
    }

    public PessoaEndereco buscarEnderecoPrincipal() {
        return listaPessoaEndereco.stream().filter(e -> e.getPrincipal().equals("S")).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.id);
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
        final Pessoa other = (Pessoa) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
