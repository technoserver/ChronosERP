package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;



@Entity
@Table(name = "FUNCAO")
public class Funcao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "DESCRICAO_MENU")
    private String descricaoMenu;
    @Column(name = "IMAGEM_MENU")
    private String imagemMenu;
    @Column(name = "METODO")
    private String metodo;
    @Column(name = "NOME")
    private String nome;
    @Column(name = "FORMULARIO")
    private String formulario;

    public Funcao() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricaoMenu() {
        return descricaoMenu;
    }

    public void setDescricaoMenu(String descricaoMenu) {
        this.descricaoMenu = descricaoMenu;
    }

    public String getImagemMenu() {
        return imagemMenu;
    }

    public void setImagemMenu(String imagemMenu) {
        this.imagemMenu = imagemMenu;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFormulario() {
        return formulario;
    }

    public void setFormulario(String formulario) {
        this.formulario = formulario;
    }

}
