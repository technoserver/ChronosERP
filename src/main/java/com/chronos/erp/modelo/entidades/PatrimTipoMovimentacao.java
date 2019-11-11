package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PATRIM_TIPO_MOVIMENTACAO")
public class PatrimTipoMovimentacao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "TIPO")
    private String tipo;
    @Column(name = "NOME")
    private String nome;
    @Column(name = "DESCRICAO")
    private String descricao;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empresa empresa;
    @Transient
    private List<String> tiposPreCadastrados;

    public PatrimTipoMovimentacao() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public List<String> getTiposPreCadastrados() {
        if (tiposPreCadastrados == null) {
            tiposPreCadastrados = new ArrayList<>();
            tiposPreCadastrados.add("01");
            tiposPreCadastrados.add("02");
            tiposPreCadastrados.add("03");
            tiposPreCadastrados.add("04");
            tiposPreCadastrados.add("05");
            tiposPreCadastrados.add("06");
            tiposPreCadastrados.add("07");
        }
        return tiposPreCadastrados;
    }

    @Override
    public String toString() {
        return nome;
    }

}
