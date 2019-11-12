
package com.chronos.erp.modelo.entidades;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "PCP_SERVICO_COLABORADOR")
public class PcpServicoColaborador implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @JoinColumn(name = "ID_COLABORADOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Colaborador colaborador;
    @JoinColumn(name = "ID_PCP_SERVICO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private PcpServico pcpServico;

    public PcpServicoColaborador() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }

    public PcpServico getPcpServico() {
        return pcpServico;
    }

    public void setPcpServico(PcpServico pcpServico) {
        this.pcpServico = pcpServico;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final PcpServicoColaborador other = (PcpServicoColaborador) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }


}
