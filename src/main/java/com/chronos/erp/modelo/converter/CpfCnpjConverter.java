package com.chronos.erp.modelo.converter;

import com.chronos.erp.util.Biblioteca;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Created by john on 11/07/17.
 */
@FacesConverter(value = "cpfCnpjConverter")
public class CpfCnpjConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return s;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        return Biblioteca.cpfCnpjFormatado(o.toString());
    }
}
