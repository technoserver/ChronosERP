package com.chronos.erp.modelo.converter;

import com.chronos.erp.data.DataDomain;
import com.chronos.erp.dto.MapIntegerDTO;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.io.Serializable;

@FacesConverter(value = "cfopMapConverter")
public class MapCfopConverter implements Converter, Serializable {

    private static final long serialVersionUID = 5629350597308157422L;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {

        if (value == null || value.isEmpty()) {
            return null;
        }

        MapIntegerDTO mapIntegerDTO = DataDomain.getCFOP().stream().filter(c -> c.getId() == Integer.parseInt(value)).findFirst().orElse(null);
        return mapIntegerDTO;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {
        if (object != null) {
            return String.valueOf(((MapIntegerDTO) object).getId());
        } else {
            return null;
        }
    }


}
