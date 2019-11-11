package com.chronos.erp.modelo.converter;

import com.chronos.erp.util.Biblioteca;
import org.springframework.util.StringUtils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("cnpjCpfValidator")
public class CnpjCpfValidator implements Validator {

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object value) throws ValidatorException {
        String cnpjCpf = String.valueOf(value);
        cnpjCpf = cnpjCpf.replaceAll("\\D", "");

        if (!StringUtils.isEmpty(cnpjCpf) && cnpjCpf.length() <= 11) {
            if (!Biblioteca.cpfValido(cnpjCpf)) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "CPF inválido!", null));
            }
        } else if (!StringUtils.isEmpty(cnpjCpf) && !Biblioteca.cnpjValido(cnpjCpf)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "CNPJ inválido!", null));
        }

    }

}
