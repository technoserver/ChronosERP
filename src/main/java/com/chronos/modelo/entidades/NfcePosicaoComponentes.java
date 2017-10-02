
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "NFCE_POSICAO_COMPONENTES")
public class NfcePosicaoComponentes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NOME")
    private String nome;
    @Column(name = "ALTURA")
    private Integer altura;
    @Column(name = "LARGURA")
    private Integer largura;
    @Column(name = "TOPO")
    private Integer topo;
    @Column(name = "ESQUERDA")
    private Integer esquerda;
    @Column(name = "TAMANHO_FONTE")
    private Integer tamanhoFonte;
    @Column(name = "TEXTO")
    private String texto;
    @JoinColumn(name = "ID_NFCE_RESOLUCAO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private NfceResolucao nfceResolucao;

    public NfcePosicaoComponentes() {
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

    public Integer getAltura() {
        return altura;
    }

    public void setAltura(Integer altura) {
        this.altura = altura;
    }

    public Integer getLargura() {
        return largura;
    }

    public void setLargura(Integer largura) {
        this.largura = largura;
    }

    public Integer getTopo() {
        return topo;
    }

    public void setTopo(Integer topo) {
        this.topo = topo;
    }

    public Integer getEsquerda() {
        return esquerda;
    }

    public void setEsquerda(Integer esquerda) {
        this.esquerda = esquerda;
    }

    public Integer getTamanhoFonte() {
        return tamanhoFonte;
    }

    public void setTamanhoFonte(Integer tamanhoFonte) {
        this.tamanhoFonte = tamanhoFonte;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public NfceResolucao getNfceResolucao() {
        return nfceResolucao;
    }

    public void setNfceResolucao(NfceResolucao nfceResolucao) {
        this.nfceResolucao = nfceResolucao;
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
        final NfcePosicaoComponentes other = (NfcePosicaoComponentes) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
