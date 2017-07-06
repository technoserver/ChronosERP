package com.chronos.modelo.entidades;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "descricao", nullable = false, length = 50)
    private String descricao;
    @OneToMany(mappedBy = "menu",fetch = FetchType.EAGER)   
    private List<Menu> subMenus;
    @JoinColumn(name = "id_menu",referencedColumnName = "id")
    @ManyToOne

    private Menu menu;

    public Menu() {
    }

    public Menu(Integer id) {
        this.id = id;
    }

    public Menu(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    public List<Menu> getSubMenus() {
        return subMenus;
    }

    public void setMenuList(List<Menu> subMenus) {
        this.subMenus = subMenus;
    }

    public Menu getIdMenu() {
        return menu;
    }

    public void setIdMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof Menu)) {
            return false;
        }
        Menu other = (Menu) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.chronosweb.entidades.Menu[ id=" + id + " ]";
    }

}
