
package com.chronos.modelo.entidades.view;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "VIEW_PESSOA_CLIENTE")
public class PessoaCliente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private int id;
    @Column(name = "ID_OPERACAO_FISCAL")
    private Integer idOperacaoFiscal;
    @Basic(optional = false)
    @Column(name = "ID_PESSOA")
    private int idPessoa;
    @Basic(optional = false)
    @Column(name = "ID_ATIVIDADE_FOR_CLI")
    private int idAtividadeForCli;
    @Basic(optional = false)
    @Column(name = "ID_SITUACAO_FOR_CLI")
    private int idSituacaoForCli;    
    @Column(name = "DESDE")
    @Temporal(TemporalType.DATE)
    private Date desde;
    @Column(name = "DATA_CADASTRO")
    @Temporal(TemporalType.DATE)
    private Date dataCadastro;
    @Column(name = "OBSERVACAO",length = 2147483647)
    private String observacao;
    @Column(name = "CONTA_TOMADOR")
    private String contaTomador;
    @Column(name = "GERA_FINANCEIRO")
    private Character geraFinanceiro;
    @Column(name = "INDICADOR_PRECO")
    private Character indicadorPreco;
    @Column(name = "PORCENTO_DESCONTO")
    private BigDecimal porcentoDesconto;
    @Column(name = "FORMA_DESCONTO")
    private Character formaDesconto;
    @Column(name = "LIMITE_CREDITO")
    private BigDecimal limiteCredito;
    @Column(name = "TIPO_FRETE")
    private Character tipoFrete;
    @Column(name = "LOGRADOURO")
    private String logradouro;
    @Column(name = "NUMERO")
    private String numero;
    @Column(name = "COMPLEMENTO")
    private String complemento;
    @Column(name = "BAIRRO")
    private String bairro;
    @Column(name = "CIDADE")
    private String cidade;
    @Column(name = "CEP")
    private String cep;
    @Column(name = "MUNICIPIO_IBGE")
    private Integer municipioIbge;
    @Column(name = "UF")
    private String uf;
    @Column(name = "FONE")
    private String fone;
    @Basic(optional = false)
    @Column(name = "NOME")
    private String nome;
    @Basic(optional = false)
    @Column(name = "TIPO")
    private String tipo;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "SITE")
    private String site;
    @Column(name = "CPF_CNPJ")
    private String cpfCnpj;
    @Column(name = "RG_IE")
    private String rgIe;

    public PessoaCliente() {
    }

    public PessoaCliente(Integer id, String nome,String cpfCnpj,String logradouro, String cidade, String uf, String fone,String tipo) {
        this.id = id;
        this.logradouro = logradouro;
        this.cidade = cidade;
        this.uf = uf;
        this.fone = fone;
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getIdOperacaoFiscal() {
        return idOperacaoFiscal;
    }

    public void setIdOperacaoFiscal(Integer idOperacaoFiscal) {
        this.idOperacaoFiscal = idOperacaoFiscal;
    }

    public int getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(int idPessoa) {
        this.idPessoa = idPessoa;
    }

    public int getIdAtividadeForCli() {
        return idAtividadeForCli;
    }

    public void setIdAtividadeForCli(int idAtividadeForCli) {
        this.idAtividadeForCli = idAtividadeForCli;
    }

    public int getIdSituacaoForCli() {
        return idSituacaoForCli;
    }

    public void setIdSituacaoForCli(int idSituacaoForCli) {
        this.idSituacaoForCli = idSituacaoForCli;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getContaTomador() {
        return contaTomador;
    }

    public void setContaTomador(String contaTomador) {
        this.contaTomador = contaTomador;
    }

    public Character getGeraFinanceiro() {
        return geraFinanceiro;
    }

    public void setGeraFinanceiro(Character geraFinanceiro) {
        this.geraFinanceiro = geraFinanceiro;
    }

    public Character getIndicadorPreco() {
        return indicadorPreco;
    }

    public void setIndicadorPreco(Character indicadorPreco) {
        this.indicadorPreco = indicadorPreco;
    }

    public BigDecimal getPorcentoDesconto() {
        return porcentoDesconto;
    }

    public void setPorcentoDesconto(BigDecimal porcentoDesconto) {
        this.porcentoDesconto = porcentoDesconto;
    }

    public Character getFormaDesconto() {
        return formaDesconto;
    }

    public void setFormaDesconto(Character formaDesconto) {
        this.formaDesconto = formaDesconto;
    }

    public BigDecimal getLimiteCredito() {
        return limiteCredito;
    }

    public void setLimiteCredito(BigDecimal limiteCredito) {
        this.limiteCredito = limiteCredito;
    }

    public Character getTipoFrete() {
        return tipoFrete;
    }

    public void setTipoFrete(Character tipoFrete) {
        this.tipoFrete = tipoFrete;
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

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Integer getMunicipioIbge() {
        return municipioIbge;
    }

    public void setMunicipioIbge(Integer municipioIbge) {
        this.municipioIbge = municipioIbge;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
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

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getRgIe() {
        return rgIe;
    }

    public void setRgIe(String rgIe) {
        this.rgIe = rgIe;
    }
 
    
    @Override
    public String toString() {
        return nome;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + this.id;
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
        final PessoaCliente other = (PessoaCliente) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

   
}
