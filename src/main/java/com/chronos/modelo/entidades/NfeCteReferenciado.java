
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "NFE_CTE_REFERENCIADO")
public class NfeCteReferenciado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CHAVE_ACESSO")
    private String chaveAcesso;
    @JoinColumn(name = "ID_NFE_CABECALHO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private NfeCabecalho nfeCabecalho;

    public NfeCteReferenciado() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChaveAcesso() {
        return chaveAcesso;
    }

    public void setChaveAcesso(String chaveAcesso) {
        this.chaveAcesso = chaveAcesso;
    }

    public NfeCabecalho getNfeCabecalho() {
        return nfeCabecalho;
    }

    public void setNfeCabecalho(NfeCabecalho nfeCabecalho) {
        this.nfeCabecalho = nfeCabecalho;
    }



}
