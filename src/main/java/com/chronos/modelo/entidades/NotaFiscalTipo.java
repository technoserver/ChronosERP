
package com.chronos.modelo.entidades;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Optional;


@Entity
@Table(name = "NOTA_FISCAL_TIPO")
public class NotaFiscalTipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NOME")
    private String nome;
    @Column(name = "DESCRICAO")
    private String descricao;
    @Column(name = "SERIE")
    private String serie;
    @Column(name = "SERIE_SCAN")
    private String serieScan;
    @Column(name = "ULTIMO_NUMERO")
    private Integer ultimoNumero;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empresa empresa;
    @JoinColumn(name = "ID_NOTA_FISCAL_MODELO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private NotaFiscalModelo notaFiscalModelo;

    public NotaFiscalTipo() {
    }

    public NotaFiscalTipo(Integer id, Integer ultimoNumero) {
        this.id = id;
        this.ultimoNumero = ultimoNumero;
    }

    public NotaFiscalTipo(Integer id, String serie, Integer ultimoNumero, Integer idNotaFiscalModelo) {
        this.id = id;
        this.serie = serie;
        this.ultimoNumero = ultimoNumero;
        this.notaFiscalModelo = new NotaFiscalModelo(idNotaFiscalModelo);
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getSerieScan() {
        return serieScan;
    }

    public void setSerieScan(String serieScan) {
        this.serieScan = serieScan;
    }

    public Integer getUltimoNumero() {
        return ultimoNumero;
    }

    public void setUltimoNumero(Integer ultimoNumero) {
        this.ultimoNumero = ultimoNumero;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public NotaFiscalModelo getNotaFiscalModelo() {
        return notaFiscalModelo;
    }

    public void setNotaFiscalModelo(NotaFiscalModelo notaFiscalModelo) {
        this.notaFiscalModelo = notaFiscalModelo;
    }

    public Integer proximoNumero() {
        return Optional.ofNullable(ultimoNumero).orElse(0) + 1;
    }

 
}
