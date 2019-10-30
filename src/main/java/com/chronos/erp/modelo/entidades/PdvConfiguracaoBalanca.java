
package com.chronos.erp.modelo.entidades;


import com.chronos.erp.modelo.enuns.ModeloBalanca;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "PDV_CONFIGURACAO_BALANCA")
public class PdvConfiguracaoBalanca implements Serializable {

    private static final long serialVersionUID = 4L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @NotNull
    private ModeloBalanca modelo;
    @Column(name = "tamanho_codigo_produto")
    @NotNull
    private Integer tamanhoCodigoProduto;
    @Column(name = "tamanho_identificador")
    @NotNull
    private Integer tamanhoIdentificador;
    @NotNull
    private String identificador;
    @Column(name = "tipo_configuracao")
    private String tipoConfiguracao;
    @MapsId
    @JoinColumn(name = "ID_PDV_CONFIGURACAO", referencedColumnName = "ID")
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @NotNull
    private PdvConfiguracao pdvConfiguracao;


    public PdvConfiguracaoBalanca() {

    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ModeloBalanca getModelo() {
        return modelo;
    }

    public void setModelo(ModeloBalanca modelo) {
        this.modelo = modelo;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getTipoConfiguracao() {
        return tipoConfiguracao;
    }

    public void setTipoConfiguracao(String tipoConfiguracao) {
        this.tipoConfiguracao = tipoConfiguracao;
    }

    public PdvConfiguracao getPdvConfiguracao() {
        return pdvConfiguracao;
    }

    public void setPdvConfiguracao(PdvConfiguracao pdvConfiguracao) {
        this.pdvConfiguracao = pdvConfiguracao;
    }

    public Integer getTamanhoCodigoProduto() {
        return tamanhoCodigoProduto;
    }

    public void setTamanhoCodigoProduto(Integer tamanhoCodigoProduto) {
        this.tamanhoCodigoProduto = tamanhoCodigoProduto;
    }

    public Integer getTamanhoIdentificador() {
        return tamanhoIdentificador;
    }

    public void setTamanhoIdentificador(Integer tamanhoIdentificador) {
        this.tamanhoIdentificador = tamanhoIdentificador;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.id);
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
        final PdvConfiguracaoBalanca other = (PdvConfiguracaoBalanca) obj;
        return Objects.equals(this.id, other.id);
    }

}
