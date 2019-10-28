/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.erp.modelo.validator;

import com.chronos.erp.util.Biblioteca;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * @author john
 */
@FacesValidator("chaveAcessoValidator")
public class ChaveAcessoValidator implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {
        String modelo = uic.getAttributes().get("dir").toString();
        if (value != null && !Biblioteca.isChaveAcesso(value.toString(), modelo)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Chave inválida!", null));
        }
    }

}
