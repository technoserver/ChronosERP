/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.erp.modelo.validator;

import com.chronos.erp.util.GtinUtil;
import org.springframework.util.StringUtils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * @author John Vanderson M Lim
 */
@FacesValidator("gtinValidator")
public class GtinValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String str = "";
        if (!StringUtils.isEmpty(value)) {

            String gtin = String.valueOf(value);
            boolean valido = GtinUtil.isValid(gtin);
            if (!valido) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Gtin inválido!", null));
            }
        }


    }

}
