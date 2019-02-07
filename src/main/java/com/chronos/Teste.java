/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos;

import com.chronos.util.Biblioteca;

import java.util.ArrayList;
import java.util.List;

/**
 * @author john
 */
public class Teste {

    public static void main(String[] args) {


        List<List<String>> teste = new ArrayList<>();

        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();

        list1.add("A");
        list1.add("B");
        list1.add("C");

        list2.add("D");
        list2.add("E");
        list2.add("F");

        teste.add(list1);
        teste.add(list2);
        List<String> result = new ArrayList<>();

        Biblioteca.generateCombination(teste, result, 0, "");


    }


}
