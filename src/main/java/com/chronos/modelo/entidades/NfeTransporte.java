
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;




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
    //@OneToMany(mappedBy = "nfeTransporte", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Transient
    private Set<NfeTransporteReboque> listaTransporteReboque;
    @OneToMany(mappedBy = "nfeTransporte", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<NfeTransporteVolume> listaTransporteVolume;

    public NfeTransporte() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * modFrete - 0- Por conta do emitente;  1- Por conta do  destinatário/remetente;  2- Por conta de terceiros;  9- Sem frete. (V2.0)
     *
     * @return
     */
    public Integer getModalidadeFrete() {
        return modalidadeFrete;
    }

    /**
     * modFrete - 0- Por conta do emitente;  1- Por conta do  destinatário/remetente;  2- Por conta de terceiros;  9- Sem frete. (V2.0)
     *
     * @param modalidadeFrete
     */
    public void setModalidadeFrete(Integer modalidadeFrete) {
        this.modalidadeFrete = modalidadeFrete;
    }

    /**
     * Informar o CNPJ ou o CPF do  Transportador, preenchendo os  zeros não significativos
     *
     * @return
     */
    public String getCpfCnpj() {
        return cpfCnpj;
    }

    /**
     * Informar o CNPJ ou o CPF do  Transportador, preenchendo os  zeros não significativos
     *
     * @param cpfCnpj
     */
    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    /**
     * xNome - Razão Social ou nome
     *
     * @return
     */
    public String getNome() {
        return nome;
    }

    /**
     * xNome - Razão Social ou nome
     *
     * @param nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * IE
     *
     * @return
     */
    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    /**
     * IE
     *
     * @param inscricaoEstadual
     */
    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    /**
     * xEnder
     *
     * @return
     */
    public String getEmpresaEndereco() {
        return empresaEndereco;
    }

    /**
     * xEnder
     *
     * @param empresaEndereco
     */
    public void setEmpresaEndereco(String empresaEndereco) {
        this.empresaEndereco = empresaEndereco;
    }

    /**
     * xMun
     *
     * @return
     */
    public String getNomeMunicipio() {
        return nomeMunicipio;
    }

    /**
     * xMun
     *
     * @param nomeMunicipio
     */
    public void setNomeMunicipio(String nomeMunicipio) {
        this.nomeMunicipio = nomeMunicipio;
    }

    /**
     * UF
     *
     * @return
     */
    public String getUf() {
        return uf;
    }

    /**
     * UF
     *
     * @param uf
     */
    public void setUf(String uf) {
        this.uf = uf;
    }

    /**
     * vServ - Valor do Serviço
     *
     * @return
     */
    public BigDecimal getValorServico() {
        return valorServico;
    }

    /**
     * vServ - Valor do Serviço
     *
     * @param valorServico
     */
    public void setValorServico(BigDecimal valorServico) {
        this.valorServico = valorServico;
    }

    /**
     * vBCRet - BC da Retenção do ICMS
     *
     * @return
     */
    public BigDecimal getValorBcRetencaoIcms() {
        return valorBcRetencaoIcms;
    }

    /**
     * vBCRet - BC da Retenção do ICMS
     *
     * @param valorBcRetencaoIcms
     */
    public void setValorBcRetencaoIcms(BigDecimal valorBcRetencaoIcms) {
        this.valorBcRetencaoIcms = valorBcRetencaoIcms;
    }

    /**
     * pICMSRet - Alíquota da Retenção
     *
     * @return
     */
    public BigDecimal getAliquotaRetencaoIcms() {
        return aliquotaRetencaoIcms;
    }

    /**
     * pICMSRet - Alíquota da Retenção
     *
     * @param aliquotaRetencaoIcms
     */
    public void setAliquotaRetencaoIcms(BigDecimal aliquotaRetencaoIcms) {
        this.aliquotaRetencaoIcms = aliquotaRetencaoIcms;
    }

    /**
     * vICMSRet - Valor do ICMS Retido
     *
     * @return
     */
    public BigDecimal getValorIcmsRetido() {
        return valorIcmsRetido;
    }

    /**
     * vICMSRet - Valor do ICMS Retido
     *
     * @param valorIcmsRetido
     */
    public void setValorIcmsRetido(BigDecimal valorIcmsRetido) {
        this.valorIcmsRetido = valorIcmsRetido;
    }

    /**
     * CFOP - CFOP
     *
     * @return
     */
    public Integer getCfop() {
        return cfop;
    }

    /**
     * CFOP - CFOP
     *
     * @param cfop
     */
    public void setCfop(Integer cfop) {
        this.cfop = cfop;
    }

    /**
     * cMunFG - Informar o município de  ocorrência do fato gerador do  ICMS do transporte. Utilizar a  Tabela do IBGE (Anexo VII -  Tabela de UF, Município e  País)
     *
     * @return
     */
    public Integer getMunicipio() {
        return municipio;
    }

    /**
     * cMunFG - Informar o município de  ocorrência do fato gerador do  ICMS do transporte. Utilizar a  Tabela do IBGE (Anexo VII -  Tabela de UF, Município e  País)
     *
     * @param municipio
     */
    public void setMunicipio(Integer municipio) {
        this.municipio = municipio;
    }

    /**
     * placa - Placa do Veículo [Informar em um dos seguintes formatos: XXX9999, XXX999, XX9999 ou XXXX999. Informar a placa em informações complementares quando a placa do veículo tiver lei de formação diversa.]
     *
     * @return
     */
    public String getPlacaVeiculo() {
        return placaVeiculo;
    }

    /**
     * placa - Placa do Veículo [Informar em um dos seguintes formatos: XXX9999, XXX999, XX9999 ou XXXX999. Informar a placa em informações complementares quando a placa do veículo tiver lei de formação diversa.]
     *
     * @param placaVeiculo
     */
    public void setPlacaVeiculo(String placaVeiculo) {
        this.placaVeiculo = placaVeiculo;
    }

    /**
     * UF - Sigla da UF
     *
     * @return
     */
    public String getUfVeiculo() {
        return ufVeiculo;
    }

    /**
     * UF - Sigla da UF
     *
     * @param ufVeiculo
     */
    public void setUfVeiculo(String ufVeiculo) {
        this.ufVeiculo = ufVeiculo;
    }

    /**
     * RNTC - Registro Nacional de  Transportador de Carga  (ANTT)
     *
     * @return
     */
    public String getRntcVeiculo() {
        return rntcVeiculo;
    }

    /**
     * RNTC - Registro Nacional de  Transportador de Carga  (ANTT)
     *
     * @param rntcVeiculo
     */
    public void setRntcVeiculo(String rntcVeiculo) {
        this.rntcVeiculo = rntcVeiculo;
    }

    /**
     * vagao - Identificação do vagão
     *
     * @return
     */
    public String getVagao() {
        return vagao;
    }

    /**
     * vagao - Identificação do vagão
     *
     * @param vagao
     */
    public void setVagao(String vagao) {
        this.vagao = vagao;
    }

    /**
     * balsa - Identificação da balsa
     *
     * @return
     */
    public String getBalsa() {
        return balsa;
    }

    /**
     * balsa - Identificação da balsa
     *
     * @param balsa
     */
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NfeTransporte other = (NfeTransporte) obj;
        return Objects.equals(this.id, other.id);
    }
}