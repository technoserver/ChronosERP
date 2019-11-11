
package com.chronos.erp.modelo.entidades;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "PCP_SERVICO_EQUIPAMENTO")
public class PcpServicoEquipamento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @JoinColumn(name = "ID_PCP_SERVICO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private PcpServico pcpServico;
    @JoinColumn(name = "ID_PATRIM_BEM", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private PatrimBem patrimBem;

    public PcpServicoEquipamento() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PcpServico getPcpServico() {
        return pcpServico;
    }

    public void setPcpServico(PcpServico pcpServico) {
        this.pcpServico = pcpServico;
    }

    public PatrimBem getPatrimBem() {
        return patrimBem;
    }

    public void setPatrimBem(PatrimBem patrimBem) {
        this.patrimBem = patrimBem;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final PcpServicoEquipamento other = (PcpServicoEquipamento) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }


}
