/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.modelo.validator;

import com.chronos.util.Biblioteca;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * @author john
 */
@FacesValidator("renavamValidator")
public class RenavamValidator implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {
        String renavam = String.valueOf(value);
        renavam = renavam.replaceAll("\\D", "");
        if (value != null && !Biblioteca.renavamValido(renavam)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "RENAVAM inválido!", null));
        }
    }

}
