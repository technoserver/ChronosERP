/*
* The MIT License
* 
* Copyright: Copyright (C) 2014 T2Ti.COM
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
* 
* The author may be contacted at: t2ti.com@gmail.com
*
* @author Claudio de Barros (T2Ti.com)
* @version 2.0
*/
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;


@Entity
@Table(name = "SIMPLES_NACIONAL_CABECALHO")
public class SimplesNacionalCabecalho implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "VIGENCIA_INICIAL")
    private Date vigenciaInicial;
    @Temporal(TemporalType.DATE)
    @Column(name = "VIGENCIA_FINAL")
    private Date vigenciaFinal;
    @Column(name = "ANEXO")
    private String anexo;
    @Column(name = "TABELA")
    private String tabela;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "simplesNacionalCabecalho", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SimplesNacionalDetalhe> listaSimplesNacionalDetalhe;

    public SimplesNacionalCabecalho() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getVigenciaInicial() {
        return vigenciaInicial;
    }

    public void setVigenciaInicial(Date vigenciaInicial) {
        this.vigenciaInicial = vigenciaInicial;
    }

    public Date getVigenciaFinal() {
        return vigenciaFinal;
    }

    public void setVigenciaFinal(Date vigenciaFinal) {
        this.vigenciaFinal = vigenciaFinal;
    }

    public String getAnexo() {
        return anexo;
    }

    public void setAnexo(String anexo) {
        this.anexo = anexo;
    }

    public String getTabela() {
        return tabela;
    }

    public void setTabela(String tabela) {
        this.tabela = tabela;
    }

    @Override
    public String toString() {
        return "com.t2tierp.model.bean.escritafiscal.SimplesNacionalCabecalho[id=" + id + "]";
    }

    public Set<SimplesNacionalDetalhe> getListaSimplesNacionalDetalhe() {
        return listaSimplesNacionalDetalhe;
    }

    public void setListaSimplesNacionalDetalhe(Set<SimplesNacionalDetalhe> listaSimplesNacionalDetalhe) {
        this.listaSimplesNacionalDetalhe = listaSimplesNacionalDetalhe;
    }

}
