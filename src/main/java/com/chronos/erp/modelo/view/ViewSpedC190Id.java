
package com.chronos.erp.modelo.view;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "view_sped_c190")
public class ViewSpedC190Id implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private ViewSpedC190 viewSpedC190;

    public ViewSpedC190 getViewSpedC190() {
        return viewSpedC190;
    }

    public void setViewSpedC190(ViewSpedC190 viewSpedC190) {
        this.viewSpedC190 = viewSpedC190;
    }

}
