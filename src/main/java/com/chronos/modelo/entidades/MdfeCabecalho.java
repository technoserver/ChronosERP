/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.modelo.entidades;

import com.chronos.modelo.enuns.StatusTransmissao;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author john
 */
@Entity
@Table(name = "mdfe_cabecalho")
@DynamicUpdate
public class MdfeCabecalho implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    private Integer uf;
    @Column(name = "tipo_ambiente")
    private Integer tipoAmbiente;
    @Column(name = "tipo_emitente")
    private Integer tipoEmitente;
    @Column(name = "tipo_transportadora")
    private Integer tipoTransportadora;
    @Column(length = 2)
    private String modelo;
    @Column(length = 3)
    private String serie;
    @Column(name = "numero_mdfe", length = 9)
    private String numeroMdfe;
    @Column(name = "codigo_numerico", length = 8)
    private String codigoNumerico;
    @Column(name = "chave_acesso", length = 44)
    private String chaveAcesso;
    @Column(name = "digito_verificador")
    private Integer digitoVerificador;
    private Integer modal;
    @Column(name = "data_hora_emissao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHoraEmissao;
    @Column(name = "tipo_emissao")
    private Integer tipoEmissao;
    @Column(name = "processo_emissao")
    private Integer processoEmissao;
    @Column(name = "versao_processo_emissao", length = 20)
    private String versaoProcessoEmissao;
    @Column(name = "uf_inicio", length = 2)
    private String ufInicio;
    @Column(name = "uf_fim", length = 2)
    private String ufFim;
    @Column(name = "data_hora_previsao_viagem")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHoraPrevisaoViagem;
    @Column(name = "quantidade_total_cte")
    private Integer quantidadeTotalCte;
    @Column(name = "quantidade_total_nfe")
    private Integer quantidadeTotalNfe;
    @Column(name = "quantidade_total_mdfe")
    private Integer quantidadeTotalMdfe;
    @Column(name = "codigo_unidade_medida", length = 2)
    private String codigoUnidadeMedida;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "peso_bruto_carga", precision = 18, scale = 6)
    private BigDecimal pesoBrutoCarga;
    @Column(name = "valor_carga", precision = 18, scale = 6)
    private BigDecimal valorCarga;
    @Column(name = "numero_protocolo", length = 15)
    private String numeroProtocolo;
    @Column(name = "status_mdfe")
    private Integer statusMdfe;
    @Column(name = "codigo_status_transmissao")
    private Integer codigoStatusTransmissao;
    @Column(name = "data_hora_processamento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHoraProcessamento;
    @Column(name = "justificativa_cancelamento", length = 255)
    private String justificativaCancelamento;
    @Column(name = "descricao_motivo_resposta", length = 255)
    private String descricaoMotivoResposta;
    @Column(name = "informacoes_add_contribuinte", length = 2147483647)
    private String informacoesAddContribuinte;
    @Column(name = "informacoes_add_fisco", length = 2147483647)
    private String informacoesAddFisco;


    @JoinColumn(name = "id_empresa", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Empresa empresa;

    @OneToOne(mappedBy = "mdfeCabecalho", cascade = CascadeType.ALL, orphanRemoval = true)
    private MdfeEmitente mdfeEmitente;

    @OneToOne(mappedBy = "mdfeCabecalho", cascade = CascadeType.ALL, orphanRemoval = true)
    private MdfeRodoviario mdfeRodoviario;


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "mdfeCabecalho", orphanRemoval = true)
    private Set<MdfeMunicipioDescarregamento> listaMdfeMunicipioDescarregamento;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "mdfeCabecalho", orphanRemoval = true)
    private Set<MdfeLacre> listaMdfeLacre;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "mdfeCabecalho", orphanRemoval = true)
    private Set<MdfeMunicipioCarregamento> listaMdfeMunicipioCarregamento;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "mdfeCabecalho", orphanRemoval = true)
    private Set<MdfeInformacaoSeguro> listaMdfeInformacaoSeguro;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "mdfeCabecalho", orphanRemoval = true)
    private Set<MdfePercurso> listaMdfePercurso;


    public MdfeCabecalho() {
        this.listaMdfePercurso = new HashSet<>();
        this.listaMdfeInformacaoSeguro = new HashSet<>();
        this.listaMdfeLacre = new HashSet<>();


        this.tipoEmitente = 2;
        this.statusMdfe = 0;
        this.modelo = "58";
        this.tipoEmissao = 1;
        this.modal = 1;
        this.quantidadeTotalCte = 0;
        this.quantidadeTotalMdfe = 0;
        this.quantidadeTotalNfe = 0;
        this.pesoBrutoCarga = BigDecimal.ZERO;
        this.valorCarga = BigDecimal.ZERO;
    }

    public MdfeCabecalho(Integer id) {
        this.id = id;
    }


    public MdfeCabecalho(Integer id, String numeroMdfe) {
        this.id = id;
        this.numeroMdfe = numeroMdfe;
    }

    public MdfeCabecalho(Integer id, String numeroMdfe, Date dataHoraEmissao, String chaveAcesso, Integer digitoVerificador, BigDecimal valorCarga, Integer statusMdfe) {
        this.id = id;
        this.numeroMdfe = numeroMdfe;
        this.dataHoraEmissao = dataHoraEmissao;
        this.chaveAcesso = chaveAcesso;
        this.digitoVerificador = digitoVerificador;
        this.valorCarga = valorCarga;
        this.statusMdfe = statusMdfe;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUf() {
        return uf;
    }

    public void setUf(Integer uf) {
        this.uf = uf;
    }

    public Integer getTipoAmbiente() {
        return tipoAmbiente;
    }

    public void setTipoAmbiente(Integer tipoAmbiente) {
        this.tipoAmbiente = tipoAmbiente;
    }

    public Integer getTipoEmitente() {
        return tipoEmitente;
    }

    public void setTipoEmitente(Integer tipoEmitente) {
        this.tipoEmitente = tipoEmitente;
    }

    public Integer getTipoTransportadora() {
        return tipoTransportadora;
    }

    public void setTipoTransportadora(Integer tipoTransportadora) {
        this.tipoTransportadora = tipoTransportadora;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getNumeroMdfe() {
        return numeroMdfe;
    }

    public void setNumeroMdfe(String numeroMdfe) {
        this.numeroMdfe = numeroMdfe;
    }

    public String getCodigoNumerico() {
        return codigoNumerico;
    }

    public void setCodigoNumerico(String codigoNumerico) {
        this.codigoNumerico = codigoNumerico;
    }

    public String getChaveAcesso() {
        return chaveAcesso;
    }

    public void setChaveAcesso(String chaveAcesso) {
        this.chaveAcesso = chaveAcesso;
    }

    public Integer getDigitoVerificador() {
        return digitoVerificador;
    }

    public void setDigitoVerificador(Integer digitoVerificador) {
        this.digitoVerificador = digitoVerificador;
    }

    public Integer getModal() {
        return modal;
    }

    public void setModal(Integer modal) {
        this.modal = modal;
    }

    public Date getDataHoraEmissao() {
        return dataHoraEmissao;
    }

    public void setDataHoraEmissao(Date dataHoraEmissao) {
        this.dataHoraEmissao = dataHoraEmissao;
    }

    public Integer getTipoEmissao() {
        return tipoEmissao;
    }

    public void setTipoEmissao(Integer tipoEmissao) {
        this.tipoEmissao = tipoEmissao;
    }

    public Integer getProcessoEmissao() {
        return processoEmissao;
    }

    public void setProcessoEmissao(Integer processoEmissao) {
        this.processoEmissao = processoEmissao;
    }

    public String getVersaoProcessoEmissao() {
        return versaoProcessoEmissao;
    }

    public void setVersaoProcessoEmissao(String versaoProcessoEmissao) {
        this.versaoProcessoEmissao = versaoProcessoEmissao;
    }

    public String getUfInicio() {
        return ufInicio;
    }

    public void setUfInicio(String ufInicio) {
        this.ufInicio = ufInicio;
    }

    public String getUfFim() {
        return ufFim;
    }

    public void setUfFim(String ufFim) {
        this.ufFim = ufFim;
    }

    public Date getDataHoraPrevisaoViagem() {
        return dataHoraPrevisaoViagem;
    }

    public void setDataHoraPrevisaoViagem(Date dataHoraPrevisaoViagem) {
        this.dataHoraPrevisaoViagem = dataHoraPrevisaoViagem;
    }

    public Integer getQuantidadeTotalCte() {
        return quantidadeTotalCte;
    }

    public void setQuantidadeTotalCte(Integer quantidadeTotalCte) {
        this.quantidadeTotalCte = quantidadeTotalCte;
    }

    public Integer getQuantidadeTotalNfe() {
        return quantidadeTotalNfe;
    }

    public void setQuantidadeTotalNfe(Integer quantidadeTotalNfe) {
        this.quantidadeTotalNfe = quantidadeTotalNfe;
    }

    public Integer getQuantidadeTotalMdfe() {
        return quantidadeTotalMdfe;
    }

    public void setQuantidadeTotalMdfe(Integer quantidadeTotalMdfe) {
        this.quantidadeTotalMdfe = quantidadeTotalMdfe;
    }

    public String getCodigoUnidadeMedida() {
        return codigoUnidadeMedida;
    }

    public void setCodigoUnidadeMedida(String codigoUnidadeMedida) {
        this.codigoUnidadeMedida = codigoUnidadeMedida;
    }

    public BigDecimal getPesoBrutoCarga() {
        return pesoBrutoCarga;
    }

    public void setPesoBrutoCarga(BigDecimal pesoBrutoCarga) {
        this.pesoBrutoCarga = pesoBrutoCarga;
    }

    public BigDecimal getValorCarga() {
        return valorCarga;
    }

    public void setValorCarga(BigDecimal valorCarga) {
        this.valorCarga = valorCarga;
    }

    public String getNumeroProtocolo() {
        return numeroProtocolo;
    }

    public void setNumeroProtocolo(String numeroProtocolo) {
        this.numeroProtocolo = numeroProtocolo;
    }

    public Integer getStatusMdfe() {
        return statusMdfe;
    }

    public void setStatusMdfe(Integer statusMdfe) {
        this.statusMdfe = statusMdfe;
    }

    public Integer getCodigoStatusTransmissao() {
        return codigoStatusTransmissao;
    }

    public void setCodigoStatusTransmissao(Integer codigoStatusTransmissao) {
        this.codigoStatusTransmissao = codigoStatusTransmissao;
    }

    public Date getDataHoraProcessamento() {
        return dataHoraProcessamento;
    }

    public void setDataHoraProcessamento(Date dataHoraProcessamento) {
        this.dataHoraProcessamento = dataHoraProcessamento;
    }

    public String getJustificativaCancelamento() {
        return justificativaCancelamento;
    }

    public void setJustificativaCancelamento(String justificativaCancelamento) {
        this.justificativaCancelamento = justificativaCancelamento;
    }

    public String getDescricaoMotivoResposta() {
        return descricaoMotivoResposta;
    }

    public void setDescricaoMotivoResposta(String descricaoMotivoResposta) {
        this.descricaoMotivoResposta = descricaoMotivoResposta;
    }

    public String getInformacoesAddContribuinte() {
        return informacoesAddContribuinte;
    }

    public void setInformacoesAddContribuinte(String informacoesAddContribuinte) {
        this.informacoesAddContribuinte = informacoesAddContribuinte;
    }

    public String getInformacoesAddFisco() {
        return informacoesAddFisco;
    }

    public void setInformacoesAddFisco(String informacoesAddFisco) {
        this.informacoesAddFisco = informacoesAddFisco;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public MdfeEmitente getMdfeEmitente() {
        return mdfeEmitente;
    }

    public void setMdfeEmitente(MdfeEmitente mdfeEmitente) {
        this.mdfeEmitente = mdfeEmitente;
    }

    public MdfeRodoviario getMdfeRodoviario() {
        return mdfeRodoviario;
    }

    public void setMdfeRodoviario(MdfeRodoviario mdfeRodoviario) {
        this.mdfeRodoviario = mdfeRodoviario;
    }

    public Set<MdfeMunicipioDescarregamento> getListaMdfeMunicipioDescarregamento() {
        return listaMdfeMunicipioDescarregamento;
    }

    public void setListaMdfeMunicipioDescarregamento(Set<MdfeMunicipioDescarregamento> listaMdfeMunicipioDescarregamento) {
        this.listaMdfeMunicipioDescarregamento = listaMdfeMunicipioDescarregamento;
    }

    public Set<MdfeLacre> getListaMdfeLacre() {
        return listaMdfeLacre;
    }

    public void setListaMdfeLacre(Set<MdfeLacre> listaMdfeLacre) {
        this.listaMdfeLacre = listaMdfeLacre;
    }

    public Set<MdfeMunicipioCarregamento> getListaMdfeMunicipioCarregamento() {
        return listaMdfeMunicipioCarregamento;
    }

    public void setListaMdfeMunicipioCarregamento(Set<MdfeMunicipioCarregamento> listaMdfeMunicipioCarregamento) {
        this.listaMdfeMunicipioCarregamento = listaMdfeMunicipioCarregamento;
    }

    public Set<MdfeInformacaoSeguro> getListaMdfeInformacaoSeguro() {
        return listaMdfeInformacaoSeguro;
    }

    public void setListaMdfeInformacaoSeguro(Set<MdfeInformacaoSeguro> listaMdfeInformacaoSeguro) {
        this.listaMdfeInformacaoSeguro = listaMdfeInformacaoSeguro;
    }

    public Set<MdfePercurso> getListaMdfePercurso() {
        return listaMdfePercurso;
    }

    public void setListaMdfePercurso(Set<MdfePercurso> listaMdfePercurso) {
        this.listaMdfePercurso = listaMdfePercurso;
    }

    public String getChaveAcessoCompleta() {
        return (this.chaveAcesso == null ? "" : this.chaveAcesso) + (this.digitoVerificador == null ? "" : this.digitoVerificador);
    }

    public String getNomeXml() {
        String nome = this.chaveAcesso + this.digitoVerificador;
        nome += StatusTransmissao.isAutorizado(this.statusMdfe) || StatusTransmissao.isEncerrada(this.statusMdfe) ? "-mdfeProc.xml" : "-mdfeCanc.xml";
        return nome;
    }

    public String getNomePdf() {
        String nome = this.chaveAcesso + this.digitoVerificador;
        nome += StatusTransmissao.isAutorizado(this.statusMdfe) || StatusTransmissao.isEncerrada(this.statusMdfe) ? "-mdfeProc.pdf" : "-mdfeCanc.pdf";
        return nome;
    }

    public boolean isPodeEnviar() {
        StatusTransmissao status = StatusTransmissao.valueOfCodigo(this.statusMdfe);
        return (status != StatusTransmissao.AUTORIZADA) && (status != StatusTransmissao.CANCELADA) && (status != StatusTransmissao.ENVIADA);
    }

    public boolean isAutorizado() {
        return StatusTransmissao.isAutorizado(this.statusMdfe);
    }

    public boolean isCancelado() {
        return StatusTransmissao.isAutorizado(this.statusMdfe);
    }

    public boolean isEncerrado() {
        return StatusTransmissao.isAutorizado(this.statusMdfe);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof MdfeCabecalho)) {
            return false;
        }
        MdfeCabecalho other = (MdfeCabecalho) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
