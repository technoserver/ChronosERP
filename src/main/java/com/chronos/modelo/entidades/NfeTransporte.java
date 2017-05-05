
package com.chronos.modelo.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "NFE_TRANSPORTE")
public class NfeTransporte implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "MODALIDADE_FRETE")
    private Integer modalidadeFrete;
    @Column(name = "CPF_CNPJ")
    private String cpfCnpj;
    @Column(name = "NOME")
    private String nome;
    @Column(name = "INSCRICAO_ESTADUAL")
    private String inscricaoEstadual;
    @Column(name = "EMPRESA_ENDERECO")
    private String empresaEndereco;
    @Column(name = "NOME_MUNICIPIO")
    private String nomeMunicipio;
    @Column(name = "UF")
    private String uf;
    @Column(name = "VALOR_SERVICO")
    private BigDecimal valorServico;
    @Column(name = "VALOR_BC_RETENCAO_ICMS")
    private BigDecimal valorBcRetencaoIcms;
    @Column(name = "ALIQUOTA_RETENCAO_ICMS")
    private BigDecimal aliquotaRetencaoIcms;
    @Column(name = "VALOR_ICMS_RETIDO")
    private BigDecimal valorIcmsRetido;
    @Column(name = "CFOP")
    private Integer cfop;
    @Column(name = "MUNICIPIO")
    private Integer municipio;
    @Column(name = "PLACA_VEICULO")
    private String placaVeiculo;
    @Column(name = "UF_VEICULO")
    private String ufVeiculo;
    @Column(name = "RNTC_VEICULO")
    private String rntcVeiculo;
    @Column(name = "VAGAO")
    private String vagao;
    @Column(name = "BALSA")
    private String balsa;
    @JoinColumn(name = "ID_NFE_CABECALHO", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private NfeCabecalho nfeCabecalho;
    @JoinColumn(name = "ID_TRANSPORTADORA", referencedColumnName = "ID")
    @ManyToOne
    private Transportadora transportadora;
    @OneToMany(mappedBy="nfeTransporte", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<NfeTransporteReboque> listaTransporteReboque;
    @OneToMany(mappedBy="nfeTransporte", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<NfeTransporteVolume> listaTransporteVolume;

    public NfeTransporte() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getModalidadeFrete() {
        return modalidadeFrete;
    }

    public void setModalidadeFrete(Integer modalidadeFrete) {
        this.modalidadeFrete = modalidadeFrete;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getEmpresaEndereco() {
        return empresaEndereco;
    }

    public void setEmpresaEndereco(String empresaEndereco) {
        this.empresaEndereco = empresaEndereco;
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

    public BigDecimal getValorServico() {
        return valorServico;
    }

    public void setValorServico(BigDecimal valorServico) {
        this.valorServico = valorServico;
    }

    public BigDecimal getValorBcRetencaoIcms() {
        return valorBcRetencaoIcms;
    }

    public void setValorBcRetencaoIcms(BigDecimal valorBcRetencaoIcms) {
        this.valorBcRetencaoIcms = valorBcRetencaoIcms;
    }

    public BigDecimal getAliquotaRetencaoIcms() {
        return aliquotaRetencaoIcms;
    }

    public void setAliquotaRetencaoIcms(BigDecimal aliquotaRetencaoIcms) {
        this.aliquotaRetencaoIcms = aliquotaRetencaoIcms;
    }

    public BigDecimal getValorIcmsRetido() {
        return valorIcmsRetido;
    }

    public void setValorIcmsRetido(BigDecimal valorIcmsRetido) {
        this.valorIcmsRetido = valorIcmsRetido;
    }

    public Integer getCfop() {
        return cfop;
    }

    public void setCfop(Integer cfop) {
        this.cfop = cfop;
    }

    public Integer getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Integer municipio) {
        this.municipio = municipio;
    }

    public String getPlacaVeiculo() {
        return placaVeiculo;
    }

    public void setPlacaVeiculo(String placaVeiculo) {
        this.placaVeiculo = placaVeiculo;
    }

    public String getUfVeiculo() {
        return ufVeiculo;
    }

    public void setUfVeiculo(String ufVeiculo) {
        this.ufVeiculo = ufVeiculo;
    }

    public String getRntcVeiculo() {
        return rntcVeiculo;
    }

    public void setRntcVeiculo(String rntcVeiculo) {
        this.rntcVeiculo = rntcVeiculo;
    }

    public String getVagao() {
        return vagao;
    }

    public void setVagao(String vagao) {
        this.vagao = vagao;
    }

    public String getBalsa() {
        return balsa;
    }

    public void setBalsa(String balsa) {
        this.balsa = balsa;
    }

    public NfeCabecalho getNfeCabecalho() {
        return nfeCabecalho;
    }

    public void setNfeCabecalho(NfeCabecalho nfeCabecalho) {
        this.nfeCabecalho = nfeCabecalho;
    }

    public Transportadora getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(Transportadora transportadora) {
        this.transportadora = transportadora;
    }

    public Set<NfeTransporteReboque> getListaTransporteReboque() {
		return listaTransporteReboque;
	}

	public void setListaTransporteReboque(Set<NfeTransporteReboque> listaTransporteReboque) {
		this.listaTransporteReboque = listaTransporteReboque;
	}

	public Set<NfeTransporteVolume> getListaTransporteVolume() {
		return listaTransporteVolume;
	}

	public void setListaTransporteVolume(Set<NfeTransporteVolume> listaTransporteVolume) {
		this.listaTransporteVolume = listaTransporteVolume;
	}



}
