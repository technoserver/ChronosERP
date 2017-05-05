/*
* The MIT License
* 
* Copyright: Copyright (C) 2014 T2Ti.COM
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
* 
* The author may be contacted at: t2ti.com@gmail.com
*
* @author Claudio de Barros (T2Ti.com)
* @version 2.0
*/
package com.chronos.modelo.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


@Entity
@Table(name = "EMPRESA")
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "RAZAO_SOCIAL")
    private String razaoSocial;
    @Column(name = "NOME_FANTASIA")
    private String nomeFantasia;
    @Column(name = "CNPJ")
    private String cnpj;
    @Column(name = "INSCRICAO_ESTADUAL")
    private String inscricaoEstadual;
    @Column(name = "INSCRICAO_ESTADUAL_ST")
    private String inscricaoEstadualSt;
    @Column(name = "INSCRICAO_MUNICIPAL")
    private String inscricaoMunicipal;
    @Column(name = "INSCRICAO_JUNTA_COMERCIAL")
    private String inscricaoJuntaComercial;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_INSC_JUNTA_COMERCIAL")
    private Date dataInscJuntaComercial;
    @Column(name = "TIPO")
    private String tipo;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_CADASTRO")
    private Date dataCadastro;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_INICIO_ATIVIDADES")
    private Date dataInicioAtividades;
    @Column(name = "SUFRAMA")
    private String suframa;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "IMAGEM_LOGOTIPO")
    private String imagemLogotipo;
    @Column(name = "CRT")
    private String crt;
    @Column(name = "TIPO_REGIME")
    private String tipoRegime;
    @Column(name = "ALIQUOTA_PIS")
    private BigDecimal aliquotaPis;
    @Column(name = "CONTATO")
    private String Contato;
    @Column(name = "ALIQUOTA_COFINS")
    private BigDecimal aliquotaCofins;
    @Column(name = "CODIGO_IBGE_CIDADE")
    private Integer codigoIbgeCidade;
    @Column(name = "CODIGO_IBGE_UF")
    private Integer codigoIbgeUf;
    @Column(name = "CODIGO_TERCEIROS")
    private Integer codigoTerceiros;
    @Column(name = "CODIGO_GPS")
    private Integer codigoGps;
    @Column(name = "ALIQUOTA_SAT")
    private BigDecimal aliquotaSat;
    @Column(name = "CEI")
    private String cei;
    @Column(name = "CODIGO_CNAE_PRINCIPAL")
    private String codigoCnaePrincipal;
    @Column(name = "TIPO_CONTROLE_ESTOQUE")
    private String tipoControleEstoque;
    @JoinColumn(name = "ID_CONTADOR", referencedColumnName = "ID")
    @ManyToOne
    private Contador contador;
    @JoinColumn(name = "ID_FPAS", referencedColumnName = "ID")
    @ManyToOne
    private Fpas fpas;
    @JoinColumn(name = "ID_SINDICATO_PATRONAL", referencedColumnName = "ID")
    @ManyToOne
    private Sindicato sindicato;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne
    private Empresa empresa;
    @OneToMany(mappedBy="empresa", fetch = FetchType.EAGER)
    private Set<EmpresaEndereco> listaEndereco;
    @ManyToMany(fetch=FetchType.LAZY, mappedBy = "listaEmpresa")
    private Set<Pessoa> listaPessoa;
    @Transient
    private byte[] imagem;

    public Empresa() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getInscricaoEstadualSt() {
        return inscricaoEstadualSt;
    }

    public void setInscricaoEstadualSt(String inscricaoEstadualSt) {
        this.inscricaoEstadualSt = inscricaoEstadualSt;
    }

    public String getInscricaoMunicipal() {
        return inscricaoMunicipal;
    }

    public void setInscricaoMunicipal(String inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

    public String getInscricaoJuntaComercial() {
        return inscricaoJuntaComercial;
    }

    public void setInscricaoJuntaComercial(String inscricaoJuntaComercial) {
        this.inscricaoJuntaComercial = inscricaoJuntaComercial;
    }

    public Date getDataInscJuntaComercial() {
        return dataInscJuntaComercial;
    }

    public void setDataInscJuntaComercial(Date dataInscJuntaComercial) {
        this.dataInscJuntaComercial = dataInscJuntaComercial;
    }
    /**
     * 
     * @return
     * Valores possíveis: M-Matriz | F-Filial | D=Depósito
     */
    public String getTipo() {
        return tipo;
    }
    /**
     * Valores possíveis: M-Matriz | F-Filial | D=Depósito
     * @param tipo 
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Date getDataInicioAtividades() {
        return dataInicioAtividades;
    }

    public void setDataInicioAtividades(Date dataInicioAtividades) {
        this.dataInicioAtividades = dataInicioAtividades;
    }

    public String getSuframa() {
        return suframa;
    }

    public void setSuframa(String suframa) {
        this.suframa = suframa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImagemLogotipo() {
        return imagemLogotipo;
    }

    public void setImagemLogotipo(String imagemLogotipo) {
        this.imagemLogotipo = imagemLogotipo;
    }
    /**
     * 
     * @return 
     * Código Regime Tributário [1=Simples Nacional | 2=Simples Nacional - excesso de sublimite da receita bruta  | 3=Regime Normal
     */
    public String getCrt() {
        return crt;
    }
    /**
     * 
     * @param crt 
     * Código Regime Tributário [1=Simples Nacional | 2=Simples Nacional - excesso de sublimite da receita bruta  | 3=Regime Normal
     */
    public void setCrt(String crt) {
        this.crt = crt;
    }
    /**
     * 
     * @return 
     * 1-Lucro REAL;2-Lucro presumido;3-Simples nacional;
     */
    public String getTipoRegime() {
        return tipoRegime;
    }
    /**
     * 
     * @param tipoRegime 
     * 1-Lucro REAL;2-Lucro presumido;3-Simples nacional;
     */
    public void setTipoRegime(String tipoRegime) {
        this.tipoRegime = tipoRegime;
    }
    /**
     * Aliquota do PIS padrao para a empresa
     * @return 
     */
    public BigDecimal getAliquotaPis() {
        return aliquotaPis;
    }
    /**
     * Aliquota do PIS padrao para a empresa
     * @param aliquotaPis 
     */
    public void setAliquotaPis(BigDecimal aliquotaPis) {
        this.aliquotaPis = aliquotaPis;
    }

    public String getContato() {
        return Contato;
    }

    public void setContato(String Contato) {
        this.Contato = Contato;
    }

 
    /**
     * Aliquota padrao do Confins
     * @return 
     */
    public BigDecimal getAliquotaCofins() {
        return aliquotaCofins;
    }
    /**
     * Aliquota padrao do Confins
     * @param aliquotaCofins 
     */
    public void setAliquotaCofins(BigDecimal aliquotaCofins) {
        this.aliquotaCofins = aliquotaCofins;
    }

    public Integer getCodigoIbgeCidade() {
        return codigoIbgeCidade;
    }

    public void setCodigoIbgeCidade(Integer codigoIbgeCidade) {
        this.codigoIbgeCidade = codigoIbgeCidade;
    }

    public Integer getCodigoIbgeUf() {
        return codigoIbgeUf;
    }

    public void setCodigoIbgeUf(Integer codigoIbgeUf) {
        this.codigoIbgeUf = codigoIbgeUf;
    }
    /**
     * Codigo das contribuicoes para terceiros incidentes sobre a folha de pagamento (os terceiros sao SESI, SENAI, SEBRAE, INCRA, etc) esta tabela consta na IN 971/2009  da RFB
     * @return 
     */
    public Integer getCodigoTerceiros() {
        return codigoTerceiros;
    }
    /**
     * Codigo das contribuicoes para terceiros incidentes sobre a folha de pagamento (os terceiros sao SESI, SENAI, SEBRAE, INCRA, etc) esta tabela consta na IN 971/2009  da RFB
     * @param codigoTerceiros 
     */
    public void setCodigoTerceiros(Integer codigoTerceiros) {
        this.codigoTerceiros = codigoTerceiros;
    }
    /**
     * Codigo de pagamento da GPS, trata-se de tabale encontrada no site da Previdencia Social
     * @return 
     */
    public Integer getCodigoGps() {
        return codigoGps;
    }
    /**
     * Codigo de pagamento da GPS, trata-se de tabale encontrada no site da Previdencia Social
     * @param codigoGps 
     */
    public void setCodigoGps(Integer codigoGps) {
        this.codigoGps = codigoGps;
    }
    /**
     * Aliquota do Seguro contra Acidente de trabalho incidente sobre a folha de pagametno dos empregados
     * @return 
     */
    public BigDecimal getAliquotaSat() {
        return aliquotaSat;
    }
    /**
     * Aliquota do Seguro contra Acidente de trabalho incidente sobre a folha de pagametno dos empregados
     * @param aliquotaSat 
     */
    public void setAliquotaSat(BigDecimal aliquotaSat) {
        this.aliquotaSat = aliquotaSat;
    }
    /**
     * Número de inscrição do estabelecimento no Cadastro Específico do INSS
     * @return 
     */
    public String getCei() {
        return cei;
    }
    /**
     * Número de inscrição do estabelecimento no Cadastro Específico do INSS
     * @param cei 
     */
    public void setCei(String cei) {
        this.cei = cei;
    }
    /**
     * Demais códigos da empresa devem ser informados na tabela EMPRESA_CNAE
     * @return 
     */
    public String getCodigoCnaePrincipal() {
        return codigoCnaePrincipal;
    }
    /**
     * Demais códigos da empresa devem ser informados na tabela EMPRESA_CNAE
     * @param codigoCnaePrincipal 
     */ 
    public void setCodigoCnaePrincipal(String codigoCnaePrincipal) {
        this.codigoCnaePrincipal = codigoCnaePrincipal;
    }
    /**
     * C=Centralizado [incremento/decremento ocorre na tabela PRODUTO] | D=Descentralizado [incremento/decremento ocorre na tabela EMPRESA_PRODUTO]
     * @return 
     */
    public String getTipoControleEstoque() {
        return tipoControleEstoque;
    }
    /**
     * C=Centralizado [incremento/decremento ocorre na tabela PRODUTO] | D=Descentralizado [incremento/decremento ocorre na tabela EMPRESA_PRODUTO]
     * @param tipoControleEstoque 
     */
    public void setTipoControleEstoque(String tipoControleEstoque) {
        this.tipoControleEstoque = tipoControleEstoque;
    }

    public Contador getContador() {
        return contador;
    }

    public void setContador(Contador contador) {
        this.contador = contador;
    }

    public Fpas getFpas() {
        return fpas;
    }

    public void setFpas(Fpas fpas) {
        this.fpas = fpas;
    }

    public Sindicato getSindicato() {
        return sindicato;
    }

    public void setSindicato(Sindicato sindicato) {
        this.sindicato = sindicato;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

	public Set<EmpresaEndereco> getListaEndereco() {
		return listaEndereco;
	}

	public void setListaEndereco(Set<EmpresaEndereco> listaEndereco) {
		this.listaEndereco = listaEndereco;
	}

	public byte[] getImagem() {
		return imagem;
	}

	public void setImagem(byte[] imagem) {
		this.imagem = imagem;
	}

	public Set<Pessoa> getListaPessoa() {
		return listaPessoa;
	}

	public void setListaPessoa(Set<Pessoa> listaPessoa) {
		this.listaPessoa = listaPessoa;
	}

	@Override
    public String toString() {
        return razaoSocial;
    }

}
