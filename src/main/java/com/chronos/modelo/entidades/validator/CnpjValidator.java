package com.chronos.modelo.entidades.validator;

import com.chronos.util.Biblioteca;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * Created by john on 25/07/17.
 */
@FacesValidator("cnpjValidator")
public class CnpjValidator implements Validator {

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object value) throws ValidatorException {
        String cnpj = String.valueOf(value);
        cnpj = cnpj.replaceAll("\\D", "");
        if (!Biblioteca.cnpjValido(cnpj)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "CNPJ inválido!!!", null));
        }
    }
}
