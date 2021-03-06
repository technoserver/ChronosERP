package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "NFE_DECLARACAO_IMPORTACAO")
public class NfeDeclaracaoImportacao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NUMERO_DOCUMENTO")
    private String numeroDocumento;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_REGISTRO")
    private Date dataRegistro;
    @Column(name = "LOCAL_DESEMBARACO")
    private String localDesembaraco;
    @Column(name = "UF_DESEMBARACO")
    private String ufDesembaraco;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_DESEMBARACO")
    private Date dataDesembaraco;
    @Column(name = "CODIGO_EXPORTADOR")
    private String codigoExportador;
    @Column(name = "VIA_TRANSPORTE")
    private Integer viaTransporte;
    @Column(name = "VALOR_AFRMM")
    private BigDecimal valorAfrmm;
    @Column(name = "FORMA_INTERMEDIACAO")
    private Integer formaIntermediacao;
    @Column(name = "CNPJ")
    private String cnpj;
    @Column(name = "UF_TERCEIRO")
    private String ufTerceiro;
    @JoinColumn(name = "ID_NFE_DETALHE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private NfeDetalhe nfeDetalhe;
    @OneToMany(mappedBy = "nfeDeclaracaoImportacao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<NfeImportacaoDetalhe> listaImportacaoDetalhe;

    public NfeDeclaracaoImportacao() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public Date getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(Date dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public String getLocalDesembaraco() {
        return localDesembaraco;
    }

    public void setLocalDesembaraco(String localDesembaraco) {
        this.localDesembaraco = localDesembaraco;
    }

    public String getUfDesembaraco() {
        return ufDesembaraco;
    }

    public void setUfDesembaraco(String ufDesembaraco) {
        this.ufDesembaraco = ufDesembaraco;
    }

    public Date getDataDesembaraco() {
        return dataDesembaraco;
    }

    public void setDataDesembaraco(Date dataDesembaraco) {
        this.dataDesembaraco = dataDesembaraco;
    }

    public String getCodigoExportador() {
        return codigoExportador;
    }

    public void setCodigoExportador(String codigoExportador) {
        this.codigoExportador = codigoExportador;
    }

    public Integer getViaTransporte() {
        return viaTransporte;
    }

    public void setViaTransporte(Integer viaTransporte) {
        this.viaTransporte = viaTransporte;
    }

    public BigDecimal getValorAfrmm() {
        return valorAfrmm;
    }

    public void setValorAfrmm(BigDecimal valorAfrmm) {
        this.valorAfrmm = valorAfrmm;
    }

    public Integer getFormaIntermediacao() {
        return formaIntermediacao;
    }

    public void setFormaIntermediacao(Integer formaIntermediacao) {
        this.formaIntermediacao = formaIntermediacao;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getUfTerceiro() {
        return ufTerceiro;
    }

    public void setUfTerceiro(String ufTerceiro) {
        this.ufTerceiro = ufTerceiro;
    }

    public NfeDetalhe getNfeDetalhe() {
        return nfeDetalhe;
    }

    public void setNfeDetalhe(NfeDetalhe nfeDetalhe) {
        this.nfeDetalhe = nfeDetalhe;
    }

    public Set<NfeImportacaoDetalhe> getListaImportacaoDetalhe() {
        return listaImportacaoDetalhe;
    }

    public void setListaImportacaoDetalhe(Set<NfeImportacaoDetalhe> listaImportacaoDetalhe) {
        this.listaImportacaoDetalhe = listaImportacaoDetalhe;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final NfeDeclaracaoImportacao other = (NfeDeclaracaoImportacao) obj;
        return Objects.equals(this.id, other.id);
    }

}
