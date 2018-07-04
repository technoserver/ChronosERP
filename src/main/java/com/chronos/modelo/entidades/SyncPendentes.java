package com.chronos.modelo.entidades;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by john on 04/07/18.
 */
@Entity
@Table(name = "sync_pendentes")
public class SyncPendentes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "codigo_acao")
    @NotBlank
    @NotNull
    private String codigoAcao;
    @Column(name = "nome_acao")
    @NotBlank
    @NotNull
    private String nomeAcao;
    @Column(name = "id_pdv_caixa")
    @NotNull
    private Integer idpdvcaixa;
    @Column(name = "id_referente")
    @NotNull
    private Integer idreferente;


    public SyncPendentes() {
    }

    public SyncPendentes(String codigoAcao, String nomeAcao, Integer idpdvcaixa, Integer idreferente) {
        this.codigoAcao = codigoAcao;
        this.nomeAcao = nomeAcao;
        this.idpdvcaixa = idpdvcaixa;
        this.idreferente = idreferente;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoAcao() {
        return codigoAcao;
    }

    public void setCodigoAcao(String codigoAcao) {
        this.codigoAcao = codigoAcao;
    }

    public String getNomeAcao() {
        return nomeAcao;
    }

    public void setNomeAcao(String nomeAcao) {
        this.nomeAcao = nomeAcao;
    }

    public Integer getIdpdvcaixa() {
        return idpdvcaixa;
    }

    public void setIdpdvcaixa(Integer idpdvcaixa) {
        this.idpdvcaixa = idpdvcaixa;
    }

    public Integer getIdreferente() {
        return idreferente;
    }

    public void setIdreferente(Integer idreferente) {
        this.idreferente = idreferente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SyncPendentes)) return false;

        SyncPendentes that = (SyncPendentes) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
