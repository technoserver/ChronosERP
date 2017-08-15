
package com.chronos.modelo.entidades.view;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VIEW_FIN_CHEQUE_NAO_COMPENSADO")
public class ViewFinChequeNaoCompensadoID implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private ViewFinChequeNaoCompensado viewFinChequeNaoCompensado;

    public ViewFinChequeNaoCompensadoID() {
    }

    public ViewFinChequeNaoCompensado getViewFinChequeNaoCompensado() {
        return viewFinChequeNaoCompensado;
    }

    public void setViewFinChequeNaoCompensado(ViewFinChequeNaoCompensado viewFinChequeNaoCompensado) {
        this.viewFinChequeNaoCompensado = viewFinChequeNaoCompensado;
    }

}
