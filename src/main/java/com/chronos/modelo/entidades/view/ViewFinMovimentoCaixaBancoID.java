
package com.chronos.modelo.entidades.view;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VIEW_FIN_MOVIMENTO_CAIXA_BANCO")
public class ViewFinMovimentoCaixaBancoID implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private ViewFinMovimentoCaixaBanco viewFinMovimentoCaixaBanco;

    public ViewFinMovimentoCaixaBancoID() {
    }

    public ViewFinMovimentoCaixaBanco getViewFinMovimentoCaixaBanco() {
        return viewFinMovimentoCaixaBanco;
    }

    public void setViewFinMovimentoCaixaBanco(ViewFinMovimentoCaixaBanco viewFinMovimentoCaixaBanco) {
        this.viewFinMovimentoCaixaBanco = viewFinMovimentoCaixaBanco;
    }

    public Integer getId() {
        return viewFinMovimentoCaixaBanco.hashCode();
    }

}
