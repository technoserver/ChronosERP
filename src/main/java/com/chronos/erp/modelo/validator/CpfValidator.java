package com.chronos.erp.modelo.validator;

import com.chronos.erp.util.Biblioteca;
import org.springframework.util.StringUtils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * Created by john on 25/07/17.
 */
@FacesValidator("cpfValidator")
public class CpfValidator implements Validator {

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object value) throws ValidatorException {
        String cpf = String.valueOf(value);
        cpf = cpf.replaceAll("\\D", "");
        if (!StringUtils.isEmpty(cpf) && !Biblioteca.cpfValido(cpf)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "CPF inválido!", null));
        }
    }
}
