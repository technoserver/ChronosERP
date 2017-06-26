/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.OperadoraPlanoSaude;
import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author john
 */
@Named
@ViewScoped
public class OperadoraPlanoSaudeControll extends AbstractControll<OperadoraPlanoSaude> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    protected Class<OperadoraPlanoSaude> getClazz() {
        return OperadoraPlanoSaude.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "OPERADORA_PLANO_SAUDE";
    }
    
}
