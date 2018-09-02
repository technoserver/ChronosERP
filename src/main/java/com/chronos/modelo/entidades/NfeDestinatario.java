package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "NFE_DESTINATARIO")
public class NfeDestinatario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CPF_CNPJ")
    private String cpfCnpj;
    @Column(name = "ESTRANGEIRO_IDENTIFICACAO")
    private String estrangeiroIdentificacao;
    @Column(name = "NOME")
    private String nome;
    @Column(name = "LOGRADOURO")
    private String logradouro;
    @Column(name = "NUMERO")
    private String numero;
    @Column(name = "COMPLEMENTO")
    private String complemento;
    @Column(name = "BAIRRO")
    private String bairro;
    @Column(name = "CODIGO_MUNICIPIO")
    private Integer codigoMunicipio;
    @Column(name = "NOME_MUNICIPIO")
    private String nomeMunicipio;
    @Column(name = "UF")
    private String uf;
    @Column(name = "CEP")
    private String cep;
    @Column(name = "CODIGO_PAIS")
    private Integer codigoPais;
    @Column(name = "NOME_PAIS")
    private String nomePais;
    @Column(name = "TELEFONE")
    private String telefone;
    @Column(name = "INDICADOR_IE")
    private Integer indicadorIe;
    @Column(name = "INSCRICAO_ESTADUAL")
    private String inscricaoEstadual;
    @Column(name = "INSCRICAO_MUNICIPAL")
    private String inscricaoMunicipal;
    @Column(name = "SUFRAMA")
    private Integer suframa;
    @Column(name = "EMAIL")
    private String email;
    @JoinColumn(name = "ID_NFE_CABECALHO", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private NfeCabecalho nfeCabecalho;

    public NfeDestinatario() {
    }

    public NfeDestinatario(String nome) {
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getEstrangeiroIdentificacao() {
        return estrangeiroIdentificacao;
    }

    public void setEstrangeiroIdentificacao(String estrangeiroIdentificacao) {
        this.estrangeiroIdentificacao = estrangeiroIdentificacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public Integer getCodigoMunicipio() {
        return codigoMunicipio;
    }

    public void setCodigoMunicipio(Integer codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
    }

    public String getNomeMunicipio() {
        return nomeMunicipio;
    }

    public void setNomeMunicipio(String nomeMunicipio) {
        this.nomeMunicipio = nomeMunicipio;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Integer getCodigoPais() {
        return codigoPais;
    }

    public void setCodigoPais(Integer codigoPais) {
        this.codigoPais = codigoPais;
    }

    public String getNomePais() {
        return nomePais;
    }

    public void setNomePais(String nomePais) {
        this.nomePais = nomePais;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * indIEDest - Indicador da IE do Destinatário -
     * 1=Contribuinte ICMS (informar a IE do destinatário);
     * 2=Contribuinte isento de Inscrição no cadastro de   Contribuintes do ICMS;
     * 9=Não Contribuinte, que pode ou não possuir Inscrição   Estadual no Cadastro de Contribuintes do ICMS;
     * Nota 1: No caso de NFC-e informar indIEDest=9 e não   informar a tag IE do destinatário;
     * Nota 2: No caso de operação com o Exterior informar   indIEDest=9 e não informar a tag IE do destinatário;
     * Nota 3: No caso de Contribuinte Isento de Inscrição   (indIEDest=2), não informar a tag IE do destinatário.
     *
     * @return
     */
    public Integer getIndicadorIe() {
        return indicadorIe;
    }

    /**
     * indIEDest - Indicador da IE do Destinatário - 1=Contribuinte ICMS (informar a IE do destinatário);
     * 2=Contribuinte isento de Inscrição no cadastro de   Contribuintes do ICMS;
     * 9=Não Contribuinte, que pode ou não possuir Inscrição   Estadual no Cadastro de Contribuintes do ICMS;
     * Nota 1: No caso de NFC-e informar indIEDest=9 e não   informar a tag IE do destinatário;
     * Nota 2: No caso de operação com o Exterior informar   indIEDest=9 e não informar a tag IE do destinatário;
     * Nota 3: No caso de Contribuinte Isento de Inscrição   (indIEDest=2), não informar a tag IE do destinatário.
     * @param indicadorIe
     */

    public void setIndicadorIe(Integer indicadorIe) {
        this.indicadorIe = indicadorIe;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getInscricaoMunicipal() {
        return inscricaoMunicipal;
    }

    public void setInscricaoMunicipal(String inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

    /**
     * ISUF - Obrigatório, nas operações que se beneficiam de incentivos fiscais existentes nas áreas sob controle da SUFRAMA.  A omissão da Inscrição SUFRAMA impede o processamento da operação pelo Sistema de Mercadoria Nacional da SUFRAMA e a liberação da Declaração de Ingresso, prejudicando a comprovação do ingresso/internamento da mercadoria nas áreas sob controle da SUFRAMA. (v2.0)
     * @return
     */
    public Integer getSuframa() {
        return suframa;
    }

    /**
     * ISUF - Obrigatório, nas operações que se beneficiam de incentivos fiscais existentes nas áreas sob controle da SUFRAMA.  A omissão da Inscrição SUFRAMA impede o processamento da operação pelo Sistema de Mercadoria Nacional da SUFRAMA e a liberação da Declaração de Ingresso, prejudicando a comprovação do ingresso/internamento da mercadoria nas áreas sob controle da SUFRAMA. (v2.0)
     * @param suframa
     */

    public void setSuframa(Integer suframa) {
        this.suframa = suframa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public NfeCabecalho getNfeCabecalho() {
        return nfeCabecalho;
    }

    public void setNfeCabecalho(NfeCabecalho nfeCabecalho) {
        this.nfeCabecalho = nfeCabecalho;
    }

}
