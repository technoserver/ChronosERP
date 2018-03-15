
package com.chronos.modelo.view;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "view_c370")
public class ViewSpedC370Id implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private ViewSpedC370 viewC370;

    /**
     * @return the viewC370
     */
    public ViewSpedC370 getViewC370() {
        return viewC370;
    }

    /**
     * @param viewC370 the viewC370Vo to set
     */
    public void setViewC370(ViewSpedC370 viewC370) {
        this.viewC370 = viewC370;
    }

}
