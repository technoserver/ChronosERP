package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;


/**
* <p>Title: T2Ti ERP</p>
* <p>Description:  VO relacionado a tabela [PAPEL_FUNCAO]</p>
*
* <p>The MIT License</p>
*
* <p>Copyright: Copyright (C) 2010 T2Ti.COM
*
* Permission is hereby granted, free of charge, to any person
* obtaining a copy of this software and associated documentation
* files (the "Software"), to deal in the Software without
* restriction, including without limitation the rights to use,
* copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the
* Software is furnished to do so, subject to the following
* conditions:
*
* The above copyright notice and this permission notice shall be
* included in all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
* EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
* OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
* NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
* HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
* WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
* OTHER DEALINGS IN THE SOFTWARE.
*
*        The author may be contacted at:
*            t2ti.com@gmail.com</p>
*
* @author Claudio de Barros (t2ti.com@gmail.com)
* @version 1.0
*/
@Entity
@Table(name = "PAPEL_FUNCAO")
public class PapelFuncao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "PODE_CONSULTAR")
    private String podeConsultar;
    @Column(name = "PODE_INSERIR")
    private String podeInserir;
    @Column(name = "PODE_ALTERAR")
    private String podeAlterar;
    @Column(name = "PODE_EXCLUIR")
    private String podeExcluir;
    @Column(name = "HABILITADO")
    private String habilitado;
    @JoinColumn(name = "ID_PAPEL", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Papel papel;
    @JoinColumn(name = "ID_FUNCAO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Funcao funcao;

    public PapelFuncao() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPodeConsultar() {
        return podeConsultar;
    }

    public void setPodeConsultar(String podeConsultar) {
        this.podeConsultar = podeConsultar;
    }

    public String getPodeInserir() {
        return podeInserir;
    }

    public void setPodeInserir(String podeInserir) {
        this.podeInserir = podeInserir;
    }

    public String getPodeAlterar() {
        return podeAlterar;
    }

    public void setPodeAlterar(String podeAlterar) {
        this.podeAlterar = podeAlterar;
    }

    public String getPodeExcluir() {
        return podeExcluir;
    }

    public void setPodeExcluir(String podeExcluir) {
        this.podeExcluir = podeExcluir;
    }

    public String getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(String habilitado) {
        this.habilitado = habilitado;
    }

    public Papel getPapel() {
        return papel;
    }

    public void setPapel(Papel papel) {
        this.papel = papel;
    }

    public Funcao getFuncao() {
        return funcao;
    }

    public void setFuncao(Funcao funcao) {
        this.funcao = funcao;
    }

}
