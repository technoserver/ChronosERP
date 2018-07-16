package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by john on 19/06/18.
 */
@Entity
@Table(name = "MDFE_CONFIGURACAO")
public class MdfeConfiguracao implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "rntrc")
    private String rntrc;
    @Column(name = "CAMINHO_LOGOMARCA")
    private String caminhoLogomarca;
    @Column(name = "CAMINHO_SCHEMAS")
    private String caminhoSchemas;
    @Column(name = "WEBSERVICE_UF")
    private String webserviceUf;
    @Column(name = "WEBSERVICE_AMBIENTE")
    private Integer webserviceAmbiente;
    @Column(name = "observacao_padrao")
    private String observacaoPadrao;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empresa empresa;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRntrc() {
        return rntrc;
    }

    public void setRntrc(String rntrc) {
        this.rntrc = rntrc;
    }


    public String getCaminhoLogomarca() {
        return caminhoLogomarca;
    }

    public void setCaminhoLogomarca(String caminhoLogomarca) {
        this.caminhoLogomarca = caminhoLogomarca;
    }

    public String getCaminhoSchemas() {
        return caminhoSchemas;
    }

    public void setCaminhoSchemas(String caminhoSchemas) {
        this.caminhoSchemas = caminhoSchemas;
    }

    public String getWebserviceUf() {
        return webserviceUf;
    }

    public void setWebserviceUf(String webserviceUf) {
        this.webserviceUf = webserviceUf;
    }

    public Integer getWebserviceAmbiente() {
        return webserviceAmbiente;
    }

    public void setWebserviceAmbiente(Integer webserviceAmbiente) {
        this.webserviceAmbiente = webserviceAmbiente;
    }


    public String getObservacaoPadrao() {
        return observacaoPadrao;
    }

    public void setObservacaoPadrao(String observacaoPadrao) {
        this.observacaoPadrao = observacaoPadrao;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MdfeConfiguracao)) return false;

        MdfeConfiguracao that = (MdfeConfiguracao) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
