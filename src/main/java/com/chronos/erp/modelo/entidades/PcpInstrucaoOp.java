
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "PCP_INSTRUCAO_OP")
public class PcpInstrucaoOp implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @JoinColumn(name = "ID_PCP_OP_CABECALHO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private PcpOpCabecalho pcpOpCabecalho;
    @JoinColumn(name = "ID_PCP_INSTRUCAO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private PcpInstrucao pcpInstrucao;

    public PcpInstrucaoOp() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PcpOpCabecalho getPcpOpCabecalho() {
        return pcpOpCabecalho;
    }

    public void setPcpOpCabecalho(PcpOpCabecalho pcpOpCabecalho) {
        this.pcpOpCabecalho = pcpOpCabecalho;
    }

    public PcpInstrucao getPcpInstrucao() {
        return pcpInstrucao;
    }

    public void setPcpInstrucao(PcpInstrucao pcpInstrucao) {
        this.pcpInstrucao = pcpInstrucao;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.id);
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
        final PcpInstrucaoOp other = (PcpInstrucaoOp) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }


}
