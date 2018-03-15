
package com.chronos.modelo.view;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "view_sped_c490")
public class ViewSpedC490Id implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private ViewSpedC490 viewC490;

    /**
     * @return the viewC490
     */
    public ViewSpedC490 getViewC490() {
        return viewC490;
    }

    /**
     * @param viewC490 the viewC490Vo to set
     */
    public void setViewC490(ViewSpedC490 viewC490) {
        this.viewC490 = viewC490;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViewSpedC490Id)) return false;

        ViewSpedC490Id that = (ViewSpedC490Id) o;

        return getViewC490() != null ? getViewC490().equals(that.getViewC490()) : that.getViewC490() == null;
    }

    @Override
    public int hashCode() {
        return getViewC490() != null ? getViewC490().hashCode() : 0;
    }
}
