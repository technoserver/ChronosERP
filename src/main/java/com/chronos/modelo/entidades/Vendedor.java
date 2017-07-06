package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "VENDEDOR")
public class Vendedor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "COMISSAO")
    private BigDecimal comissao;
    @Column(name = "META_VENDAS")
    private BigDecimal metaVendas;
    @Column(name = "taxa_gerente", precision = 18, scale = 6)
    private BigDecimal taxaGerente;
    @Column(name = "gerente")
    private Character gerente;
    @JoinColumn(name = "ID_COLABORADOR", referencedColumnName = "ID")
    @ManyToOne
    private Colaborador colaborador;
    @JoinColumn(name = "id_comissao_perfil", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ComissaoPerfil comissaoPerfil;

    public Vendedor() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getComissao() {
        return comissao;
    }

    public void setComissao(BigDecimal comissao) {
        this.comissao = comissao;
    }

    public BigDecimal getMetaVendas() {
        return metaVendas;
    }

    public void setMetaVendas(BigDecimal metaVendas) {
        this.metaVendas = metaVendas;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }
    /**
     * CASO SEJA GERENTE, A TAXA É INFORMADA NESSA TABELA. CADA GERENTE PODE TER UMA TAXA DIFERENCIADA.
     * @return 
     */
    public BigDecimal getTaxaGerente() {
        return taxaGerente;
    }
    /**
     * CASO SEJA GERENTE, A TAXA É INFORMADA NESSA TABELA. CADA GERENTE PODE TER UMA TAXA DIFERENCIADA.
     * @param taxaGerente 
     */
    public void setTaxaGerente(BigDecimal taxaGerente) {
        this.taxaGerente = taxaGerente;
    }
    /**
     * S=SIM | N=NÃO - MARCA SE O COLABORADOR É UM GERENTE
     * @return 
     */
    public Character getGerente() {
        return gerente;
    }
    /**
     * S=SIM | N=NÃO - MARCA SE O COLABORADOR É UM GERENTE
     * @param gerente 
     */
    public void setGerente(Character gerente) {
        this.gerente = gerente;
    }

    public ComissaoPerfil getComissaoPerfil() {
        return comissaoPerfil;
    }

    public void setComissaoPerfil(ComissaoPerfil comissaoPerfil) {
        this.comissaoPerfil = comissaoPerfil;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final Vendedor other = (Vendedor) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Vendedor{" + "id=" + id + '}';
    }
    
    

}
