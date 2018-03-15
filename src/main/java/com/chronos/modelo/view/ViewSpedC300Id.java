
package com.chronos.modelo.view;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "view_sped_c300")
public class ViewSpedC300Id implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private ViewSpedC300 viewSpedC300;

    public ViewSpedC300 getViewSpedC300() {
        return viewSpedC300;
    }

    public void setViewSpedC300(ViewSpedC300 viewSpedC300) {
        this.viewSpedC300 = viewSpedC300;
    }


}
