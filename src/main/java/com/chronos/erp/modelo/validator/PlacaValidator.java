
package com.chronos.erp.modelo.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author john
 */
@FacesValidator("placaValidator")
public class PlacaValidator implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {
        if (value != null) {
            String placa = String.valueOf(value);
            Pattern pattern = Pattern.compile("[A-Z]{3}-[0-9]{4}");
            Matcher matcher = pattern.matcher(placa.toUpperCase());
            if (!matcher.matches()) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Placa inválida!", null));
            }
        }

    }

}
