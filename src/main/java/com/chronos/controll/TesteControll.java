/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll;

import com.chronos.modelo.entidades.Banco;
import com.chronos.repository.Repository;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author john
 */
@Named
@ViewScoped
public class TesteControll implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<Banco> bancos;
    @Inject
    private Calculadora calc;
    private String teste;
    private List<Banco> listaBancos;
    public TesteControll() {
    }

    
    
    @PostConstruct
    public void init() {
        teste = String.valueOf(calc.calcular());
           try {
            listaBancos =  bancos.getAll(Banco.class);
        } catch (Exception ex) {
            Logger.getLogger(TesteControll.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getTeste() {
        return teste;
    }

    public void setTeste(String teste) {
        this.teste = teste;
    }

    public int getPreco() {
        return calc.calcular();
    }
    
    private List<Banco> getListaBanco(){
        try {
            return bancos.getAll(Banco.class);
        } catch (Exception ex) {
            Logger.getLogger(TesteControll.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Banco> getListaBancos() {
        return listaBancos;
    }

    public void setListaBancos(List<Banco> listaBancos) {
        this.listaBancos = listaBancos;
    }
    
    

}
