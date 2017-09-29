
package com.chronos.modelo.entidades.view;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "view_sped_c321")
public class ViewSpedC321Id implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private ViewSpedC321 viewC321;

    /**
     * @return the viewC321
     */
    public ViewSpedC321 getViewC321() {
        return viewC321;
    }

    /**
     * @param viewC321 the viewC321Vo to set
     */
    public void setViewC321(ViewSpedC321 viewC321) {
        this.viewC321 = viewC321;
    }
}
