package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "VENDA_ROMANEIO_ENTREGA")
public class VendaRomaneioEntrega implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "DESCRICAO")
    private String descricao;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_EMISSAO")
    private Date dataEmissao;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_PREVISTA")
    private Date dataPrevista;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_SAIDA")
    private Date dataSaida;
    @Column(name = "SITUACAO")
    private String situacao;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_ENCERRAMENTO")
    private Date dataEncerramento;
    @Column(name = "OBSERVACAO")
    private String observacao;
    @JoinColumn(name = "ID_COLABORADOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Colaborador colaborador;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vendaRomaneioEntrega", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VendaCabecalho> listaVendaCabecalho;

    public VendaRomaneioEntrega() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public Date getDataPrevista() {
        return dataPrevista;
    }

    public void setDataPrevista(Date dataPrevista) {
        this.dataPrevista = dataPrevista;
    }

    public Date getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(Date dataSaida) {
        this.dataSaida = dataSaida;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Date getDataEncerramento() {
        return dataEncerramento;
    }

    public void setDataEncerramento(Date dataEncerramento) {
        this.dataEncerramento = dataEncerramento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }

    public Set<VendaCabecalho> getListaVendaCabecalho() {
        return listaVendaCabecalho;
    }

    public void setListaVendaCabecalho(Set<VendaCabecalho> listaVendaCabecalho) {
        this.listaVendaCabecalho = listaVendaCabecalho;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id);
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
        final VendaRomaneioEntrega other = (VendaRomaneioEntrega) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    

}
