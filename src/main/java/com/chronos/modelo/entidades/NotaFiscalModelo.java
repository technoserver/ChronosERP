
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "NOTA_FISCAL_MODELO")
public class NotaFiscalModelo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CODIGO")
    private String codigo;
    @Column(name = "DESCRICAO")
    private String descricao;
    @Column(name = "MODELO")
    private String modelo;

    public NotaFiscalModelo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

 

}
