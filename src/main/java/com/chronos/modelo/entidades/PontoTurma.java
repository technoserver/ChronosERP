
package com.chronos.modelo.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "PONTO_TURMA")
public class PontoTurma implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CODIGO")
    private String codigo;
    @Column(name = "NOME")
    private String nome;
    @JoinColumn(name = "ID_PONTO_ESCALA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private PontoEscala pontoEscala;

    public PontoTurma() {
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public PontoEscala getPontoEscala() {
        return pontoEscala;
    }

    public void setPontoEscala(PontoEscala pontoEscala) {
        this.pontoEscala = pontoEscala;
    }

   @Override
   public String toString() {
      return nome ;
   }



}
