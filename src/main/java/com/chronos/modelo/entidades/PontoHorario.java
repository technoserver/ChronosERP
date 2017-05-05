
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
@Table(name = "PONTO_HORARIO")
public class PontoHorario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "TIPO")
    private String tipo;
    @Column(name = "CODIGO")
    private String codigo;
    @Column(name = "NOME")
    private String nome;
    @Column(name = "TIPO_TRABALHO")
    private String tipoTrabalho;
    @Column(name = "CARGA_HORARIA")
    private String cargaHoraria;
    @Column(name = "ENTRADA01")
    private String entrada01;
    @Column(name = "SAIDA01")
    private String saida01;
    @Column(name = "ENTRADA02")
    private String entrada02;
    @Column(name = "SAIDA02")
    private String saida02;
    @Column(name = "ENTRADA03")
    private String entrada03;
    @Column(name = "SAIDA03")
    private String saida03;
    @Column(name = "ENTRADA04")
    private String entrada04;
    @Column(name = "SAIDA04")
    private String saida04;
    @Column(name = "ENTRADA05")
    private String entrada05;
    @Column(name = "SAIDA05")
    private String saida05;
    @Column(name = "HORA_INICIO_JORNADA")
    private String horaInicioJornada;
    @Column(name = "HORA_FIM_JORNADA")
    private String horaFimJornada;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empresa empresa;

    public PontoHorario() {
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

    public String getTipoTrabalho() {
        return tipoTrabalho;
    }

    public void setTipoTrabalho(String tipoTrabalho) {
        this.tipoTrabalho = tipoTrabalho;
    }

    public String getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(String cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public String getEntrada01() {
        return entrada01;
    }

    public void setEntrada01(String entrada01) {
        this.entrada01 = entrada01;
    }

    public String getSaida01() {
        return saida01;
    }

    public void setSaida01(String saida01) {
        this.saida01 = saida01;
    }

    public String getEntrada02() {
        return entrada02;
    }

    public void setEntrada02(String entrada02) {
        this.entrada02 = entrada02;
    }

    public String getSaida02() {
        return saida02;
    }

    public void setSaida02(String saida02) {
        this.saida02 = saida02;
    }

    public String getEntrada03() {
        return entrada03;
    }

    public void setEntrada03(String entrada03) {
        this.entrada03 = entrada03;
    }

    public String getSaida03() {
        return saida03;
    }

    public void setSaida03(String saida03) {
        this.saida03 = saida03;
    }

    public String getEntrada04() {
        return entrada04;
    }

    public void setEntrada04(String entrada04) {
        this.entrada04 = entrada04;
    }

    public String getSaida04() {
        return saida04;
    }

    public void setSaida04(String saida04) {
        this.saida04 = saida04;
    }

    public String getEntrada05() {
        return entrada05;
    }

    public void setEntrada05(String entrada05) {
        this.entrada05 = entrada05;
    }

    public String getSaida05() {
        return saida05;
    }

    public void setSaida05(String saida05) {
        this.saida05 = saida05;
    }

    public String getHoraInicioJornada() {
        return horaInicioJornada;
    }

    public void setHoraInicioJornada(String horaInicioJornada) {
        this.horaInicioJornada = horaInicioJornada;
    }

    public String getHoraFimJornada() {
        return horaFimJornada;
    }

    public void setHoraFimJornada(String horaFimJornada) {
        this.horaFimJornada = horaFimJornada;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

   @Override
   public String toString() {
      return nome ;
   }

  

}
