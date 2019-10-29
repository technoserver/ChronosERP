
package com.chronos.erp.modelo.view;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "view_sped_c425")
public class ViewSpedC425Id implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private ViewSpedC425 viewC425;

    /**
     * @return the viewC425
     */
    public ViewSpedC425 getViewC425() {
        return viewC425;
    }

    /**
     * @param viewC425 the viewC425Vo to set
     */
    public void setViewC425(ViewSpedC425 viewC425) {
        this.viewC425 = viewC425;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViewSpedC425Id)) return false;

        ViewSpedC425Id that = (ViewSpedC425Id) o;

        return getViewC425() != null ? getViewC425().equals(that.getViewC425()) : that.getViewC425() == null;
    }

    @Override
    public int hashCode() {
        return getViewC425() != null ? getViewC425().hashCode() : 0;
    }
}
