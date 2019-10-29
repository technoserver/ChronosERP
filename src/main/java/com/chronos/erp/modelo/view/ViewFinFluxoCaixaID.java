
package com.chronos.erp.modelo.view;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "VIEW_FIN_FLUXO_CAIXA")
public class ViewFinFluxoCaixaID implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private ViewFinFluxoCaixa viewFinFluxoCaixa;

    public ViewFinFluxoCaixaID() {
    }

    public ViewFinFluxoCaixa getViewFinFluxoCaixa() {
        return viewFinFluxoCaixa;
    }

    public void setViewFinFluxoCaixa(ViewFinFluxoCaixa viewFinFluxoCaixa) {
        this.viewFinFluxoCaixa = viewFinFluxoCaixa;
    }

    public Integer getId() {
        return viewFinFluxoCaixa.hashCode();
    }

}
