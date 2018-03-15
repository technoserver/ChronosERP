
package com.chronos.modelo.view;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "VIEW_FIN_RESUMO_TESOURARIA")
public class ViewFinResumoTesourariaID implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private ViewFinResumoTesouraria viewFinResumoTesouraria;

    public ViewFinResumoTesourariaID() {
    }

    public ViewFinResumoTesouraria getViewFinResumoTesouraria() {
        return viewFinResumoTesouraria;
    }

    public void setViewFinResumoTesouraria(ViewFinResumoTesouraria viewFinResumoTesouraria) {
        this.viewFinResumoTesouraria = viewFinResumoTesouraria;
    }

    public Integer getId() {
        return viewFinResumoTesouraria.hashCode();
    }

}
