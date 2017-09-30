
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "NFCE_RESOLUCAO")
public class NfceResolucao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "RESOLUCAO_TELA")
    private String resolucaoTela;
    @Column(name = "LARGURA")
    private Integer largura;
    @Column(name = "ALTURA")
    private Integer altura;
    @Column(name = "IMAGEM_TELA")
    private String imagemTela;
    @Column(name = "IMAGEM_MENU")
    private String imagemMenu;
    @Column(name = "IMAGEM_SUBMENU")
    private String imagemSubmenu;
    @OneToMany(mappedBy = "nfceResolucao", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<NfcePosicaoComponentes> listaNfcePosicaoComponentes;

    public NfceResolucao() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResolucaoTela() {
        return resolucaoTela;
    }

    public void setResolucaoTela(String resolucaoTela) {
        this.resolucaoTela = resolucaoTela;
    }

    public Integer getLargura() {
        return largura;
    }

    public void setLargura(Integer largura) {
        this.largura = largura;
    }

    public Integer getAltura() {
        return altura;
    }

    public void setAltura(Integer altura) {
        this.altura = altura;
    }

    public String getImagemTela() {
        return imagemTela;
    }

    public void setImagemTela(String imagemTela) {
        this.imagemTela = imagemTela;
    }

    public String getImagemMenu() {
        return imagemMenu;
    }

    public void setImagemMenu(String imagemMenu) {
        this.imagemMenu = imagemMenu;
    }

    public String getImagemSubmenu() {
        return imagemSubmenu;
    }

    public void setImagemSubmenu(String imagemSubmenu) {
        this.imagemSubmenu = imagemSubmenu;
    }

    @Override
    public String toString() {
        return "com.t2tierp.model.bean.nfce.NfceResolucao[id=" + id + "]";
    }

    public Set<NfcePosicaoComponentes> getListaNfcePosicaoComponentes() {
        return listaNfcePosicaoComponentes;
    }

    public void setListaNfcePosicaoComponentes(Set<NfcePosicaoComponentes> listaNfcePosicaoComponentes) {
        this.listaNfcePosicaoComponentes = listaNfcePosicaoComponentes;
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
        final NfceResolucao other = (NfceResolucao) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
