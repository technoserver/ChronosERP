package com.chronos.modelo.entidades;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "OS_ABERTURA")
public class OsAbertura implements Serializable {

    private static final long serialVersionUID = 2L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NUMERO")
    private String numero;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_INICIO")
    private Date dataInicio;
    @Column(name = "HORA_INICIO")
    private String horaInicio;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_PREVISAO")
    private Date dataPrevisao;
    @Column(name = "HORA_PREVISAO")
    private String horaPrevisao;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_FIM")
    private Date dataFim;
    @Column(name = "HORA_FIM")
    private String horaFim;
    @Column(name = "NOME_CONTATO")
    private String nomeContato;
    @Column(name = "FONE_CONTATO")
    private String foneContato;
    @Column(name = "OBSERVACAO_CLIENTE")
    private String observacaoCliente;
    @Column(name = "OBSERVACAO_ABERTURA")
    private String observacaoAbertura;
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private Cliente cliente;
    @JoinColumn(name = "ID_COLABORADOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Colaborador colaborador;
    @JoinColumn(name = "ID_OS_STATUS", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private OsStatus osStatus;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "osAbertura", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OsEvolucao> listaOsEvolucao;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "osAbertura", cascade = CascadeType.ALL, orphanRemoval = true)     
    private Set<OsProdutoServico> listaOsProdutoServico;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "osAbertura", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OsAberturaEquipamento> listaOsAberturaEquipamento;

    public OsAbertura() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getDataPrevisao() {
        return dataPrevisao;
    }

    public void setDataPrevisao(Date dataPrevisao) {
        this.dataPrevisao = dataPrevisao;
    }

    public String getHoraPrevisao() {
        return horaPrevisao;
    }

    public void setHoraPrevisao(String horaPrevisao) {
        this.horaPrevisao = horaPrevisao;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    public String getNomeContato() {
        return nomeContato;
    }

    public void setNomeContato(String nomeContato) {
        this.nomeContato = nomeContato;
    }

    public String getFoneContato() {
        return foneContato;
    }

    public void setFoneContato(String foneContato) {
        this.foneContato = foneContato;
    }

    public String getObservacaoCliente() {
        return observacaoCliente;
    }

    public void setObservacaoCliente(String observacaoCliente) {
        this.observacaoCliente = observacaoCliente;
    }

    public String getObservacaoAbertura() {
        return observacaoAbertura;
    }

    public void setObservacaoAbertura(String observacaoAbertura) {
        this.observacaoAbertura = observacaoAbertura;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }

    public OsStatus getOsStatus() {
        return osStatus;
    }

    public void setOsStatus(OsStatus osStatus) {
        this.osStatus = osStatus;
    }

  
    public Set<OsEvolucao> getListaOsEvolucao() {
        return listaOsEvolucao;
    }

    public void setListaOsEvolucao(Set<OsEvolucao> listaOsEvolucao) {
        this.listaOsEvolucao = listaOsEvolucao;
    }

    public Set<OsProdutoServico> getListaOsProdutoServico() {
        return listaOsProdutoServico;
    }

    public void setListaOsProdutoServico(Set<OsProdutoServico> listaOsProdutoServico) {
        this.listaOsProdutoServico = listaOsProdutoServico;
    }

    public Set<OsAberturaEquipamento> getListaOsAberturaEquipamento() {
        return listaOsAberturaEquipamento;
    }

    public void setListaOsAberturaEquipamento(Set<OsAberturaEquipamento> listaOsAberturaEquipamento) {
        this.listaOsAberturaEquipamento = listaOsAberturaEquipamento;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final OsAbertura other = (OsAbertura) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "OsAbertura{" + "id=" + id + '}';
    }
    
    

}
