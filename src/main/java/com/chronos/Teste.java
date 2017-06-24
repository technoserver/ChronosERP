/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author john
 */
public class Teste {
    
    public static void main(String[] args){
           BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String senha = "admin";   
        System.err.println(passwordEncoder.encode(senha));
        System.err.println(passwordEncoder.encode(senha));
        System.err.println(passwordEncoder.encode(senha));
        System.err.println(passwordEncoder.encode(senha));
    }
    
       private static String getSenha(String usuario, String senha) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String senhaCrip = (usuario + senha).replaceAll("a", "@").replaceAll("e", "3").replaceAll("i", "1").replaceAll("o", "0").replaceAll("u", "#");
        senhaCrip += senhaCrip.length();
        senhaCrip = passwordEncoder.encode(senha);
        return senhaCrip;
    }
}
