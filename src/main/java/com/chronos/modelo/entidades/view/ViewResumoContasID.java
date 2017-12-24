package com.chronos.modelo.entidades.view;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by john on 20/12/17.
 */
@Entity
@Table(name = "view_resumo_contas_mensal")
public class ViewResumoContasID implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private ViewResumoContas viewResumoContas;


    public ViewResumoContas getViewResumoContas() {
        return viewResumoContas;
    }

    public void setViewResumoContas(ViewResumoContas viewResumoContas) {
        this.viewResumoContas = viewResumoContas;
    }
}
