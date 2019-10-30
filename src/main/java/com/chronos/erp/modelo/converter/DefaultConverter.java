package com.chronos.erp.modelo.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@FacesConverter(value = "defaultConverter")
public class DefaultConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {

        if (value == null || value.isEmpty()) {
            return null;
        }
        return uiComponent.getAttributes().get(value);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        try {
            if (value != null) {

                Method method = value.getClass().getDeclaredMethod("getId");
                Integer id = (Integer) method.invoke(value);
                uiComponent.getAttributes().put(String.valueOf(id), value);
                return String.valueOf(id);


            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
