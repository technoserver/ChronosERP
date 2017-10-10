
package com.chronos.modelo.entidades.view;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "view_sped_c390")
public class ViewSpedC390Id implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private ViewSpedC390 viewC390;

    /**
     * @return the viewC390
     */
    public ViewSpedC390 getViewC390() {
        return viewC390;
    }

    /**
     * @param viewC390 the viewC390Vo to set
     */
    public void setViewC390(ViewSpedC390 viewC390) {
        this.viewC390 = viewC390;
    }

}
