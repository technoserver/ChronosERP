/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.execption;

/**
 * @author john
 */
public class GtinFormatException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public GtinFormatException(String mensagem) {
        super(mensagem);
    }

}
