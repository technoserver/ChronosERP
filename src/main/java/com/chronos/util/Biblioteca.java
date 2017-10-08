/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * @author john
 */
public class Biblioteca {


    public String retiraAcentos(String string) {
        String aux = new String(string);
        aux = aux.replaceAll("[èëÈéêÉÊË]", "e");
        aux = aux.replaceAll("[ûùüúÛÚÙÜ]", "u");
        aux = aux.replaceAll("[ïîíìÏÎÍÌ]", "i");
        aux = aux.replaceAll("[àâáäãÁÀÂÄ]", "a");
        aux = aux.replaceAll("[óòôöõÓÒÔÖ]", "o");
        aux = aux.replaceAll("[çÇ]", "c");
        return aux;
    }

    public static String repete(String string, int quantidade) {
        return String.join("", Collections.nCopies(quantidade, string));
    }


    /**
     * Busca os bytes de um determinado arquivo
     *
     * @param file Arquivo
     * @return byte[]
     */
    public static byte[] getBytesFromFile(File file) throws Exception {
        // Create the byte array to hold the data
        byte[] documento;
        InputStream is = new FileInputStream(file);
        try {
            // Get the size of the file
            long length = file.length();
            documento = new byte[(int) length];
            // Read in the bytes
            int offset = 0;
            int numRead = 0;
            while (offset < documento.length && (numRead = is.read(documento, offset, documento.length - offset)) >= 0) {
                offset += numRead;
            }
            // Ensure all the bytes have been read in
            if (offset < documento.length) {
                throw new IOException("Could not completely read file " + file.getName());
            }
            // Close the input stream and return bytes
        } finally {
            is.close();
        }
        return documento;
    }

    /**
     * Formata o  CPF ou CNPJ dependendo do  tamanho
     *
     * @param cpfCnpj
     * @return
     */
    public static String cpfCnpjFormatado(String cpfCnpj) {
        if (cpfCnpj != null) {
            cpfCnpj = cpfCnpj.replaceAll("\\D", "");
            if (cpfCnpj.length() == 11) {
                String strFormatado = cpfCnpj.substring(0, 3) + "."
                        + cpfCnpj.substring(3, 6) + "."
                        + cpfCnpj.substring(6, 9) + "-"
                        + cpfCnpj.substring(9, 11);

                return strFormatado;
            }
            if (cpfCnpj.length() == 14) {
                String strFormatado = cpfCnpj.substring(0, 2) + "."
                        + cpfCnpj.substring(2, 5) + "."
                        + cpfCnpj.substring(5, 8) + "/"
                        + cpfCnpj.substring(8, 12) + "-"
                        + cpfCnpj.substring(12, 14);

                return strFormatado;
            }
        }
        return cpfCnpj;
    }

    /**
     * Verifica se o cnpj informado e valido
     *
     * @param cnpj CNPJ a validar
     * @return boolean
     */
    public static boolean cnpjValido(String cnpj) {
        try {
            if (cnpj == null) {
                throw new Exception("Valor nulo");
            }
            if (cnpj.equals("00000000000000") || cnpj.equals("11111111111111") || cnpj.equals("22222222222222") || cnpj.equals("33333333333333") || cnpj.equals("44444444444444") || cnpj.equals("55555555555555") || cnpj.equals("66666666666666") || cnpj.equals("77777777777777") || cnpj.equals("88888888888888") || cnpj.equals("99999999999999") || (cnpj.length() != 14)) {
                return (false);
            }

            char dig13, dig14;
            int sm, i, r, num, peso;
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 11; i >= 0; i--) {
                // converte o i-esimo caractere do CNPJ em um numero:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posicao de '0' na tabela ASCII)
                num = (int) (cnpj.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10) {
                    peso = 2;
                }
            }

            r = sm % 11;
            if ((r == 0) || (r == 1)) {
                dig13 = '0';
            } else {
                dig13 = (char) ((11 - r) + 48);
            }

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 12; i >= 0; i--) {
                num = (int) (cnpj.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10) {
                    peso = 2;
                }
            }

            r = sm % 11;
            if ((r == 0) || (r == 1)) {
                dig14 = '0';
            } else {
                dig14 = (char) ((11 - r) + 48);
            }

            // Verifica se os digitos calculados conferem com os digitos
            // informados.
            if ((dig13 == cnpj.charAt(12)) && (dig14 == cnpj.charAt(13))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return (false);
        }
    }

    /**
     * Verifica se o cpf informado e valido
     *
     * @param cpf CPF a validar
     * @return boolean
     */
    public static boolean cpfValido(String cpf) {
        try {
            if (cpf == null) {
                throw new Exception("Valor nulo");
            }
            if (cpf.equals("00000000000") || cpf.equals("11111111111") || cpf.equals("22222222222") || cpf.equals("33333333333") || cpf.equals("44444444444") || cpf.equals("55555555555") || cpf.equals("66666666666") || cpf.equals("77777777777") || cpf.equals("88888888888") || cpf.equals("99999999999") || (cpf.length() != 11)) {
                return (false);
            }

            char dig10, dig11;
            int sm, i, r, num, peso;

            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                // converte o i-esimo caractere do CPF em um numero:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posicao de '0' na tabela ASCII)
                num = (int) (cpf.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig10 = '0';
            } else {
                dig10 = (char) (r + 48); // converte no respectivo caractere
                // numerico
            }
            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (cpf.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig11 = '0';
            } else {
                dig11 = (char) (r + 48);
            }

            // Verifica se os digitos calculados conferem com os digitos
            // informados.
            if ((dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static Object nullToEmpty(Object objeto, boolean relacionamentos) {
        Object atributo;
        try {
            Field fields[] = objeto.getClass().getDeclaredFields();
            for (Field f : fields) {
                if (!(f.getName().equals("serialVersionUID") || f.getName().equals("bag"))) {
                    if (f.getType() == String.class || f.getType() == Integer.class || f.getType() == BigDecimal.class || f.getType() == Double.class || f.getType() == Date.class || f.getType() == Long.class) {

                        Method metodo = objeto.getClass().getDeclaredMethod("get" + primeiraMaiuscula(f.getName()));
                        atributo = metodo.invoke(objeto);
                        if (atributo == null) {
                            if (f.getType() == String.class) {
                                metodo = objeto.getClass().getDeclaredMethod("set" + primeiraMaiuscula(f.getName()), String.class);
                                metodo.invoke(objeto, "");
                            }
                            if (f.getType() == Integer.class) {
                                metodo = objeto.getClass().getDeclaredMethod("set" + primeiraMaiuscula(f.getName()), Integer.class);
                                metodo.invoke(objeto, 0);
                            }
                            if (f.getType() == BigDecimal.class) {
                                metodo = objeto.getClass().getDeclaredMethod("set" + primeiraMaiuscula(f.getName()), BigDecimal.class);
                                metodo.invoke(objeto, BigDecimal.ZERO);
                            }
                            if (f.getType() == Double.class) {
                                metodo = objeto.getClass().getDeclaredMethod("set" + primeiraMaiuscula(f.getName()), Double.class);
                                metodo.invoke(objeto, 0.0);
                            }
                            if (f.getType() == Date.class) {
                                metodo = objeto.getClass().getDeclaredMethod("set" + primeiraMaiuscula(f.getName()), Date.class);
                                metodo.invoke(objeto, new Date(0l));
                            }
                            if (f.getType() == Long.class) {
                                metodo = objeto.getClass().getDeclaredMethod("set" + primeiraMaiuscula(f.getName()), Long.class);
                                metodo.invoke(objeto, 0l);
                            }
                        }
                    } else if (relacionamentos) {
                        if (!objeto.getClass().getName().equals(f.getType().getName())) {
                            Method metodo = objeto.getClass().getDeclaredMethod("get" + primeiraMaiuscula(f.getName()));
                            atributo = metodo.invoke(objeto);
                            if (atributo != null) {
                                if (atributo.getClass() != ArrayList.class) {
                                    nullToEmpty(atributo, relacionamentos);
                                }
                            } else {
                                if (f.getType() != ArrayList.class || !(f.getType().isArray())) {
                                    metodo = objeto.getClass().getDeclaredMethod("set" + primeiraMaiuscula(f.getName()), f.getType());
                                    metodo.invoke(objeto, Class.forName(f.getType().getName()).newInstance());

                                    metodo = objeto.getClass().getDeclaredMethod("get" + primeiraMaiuscula(f.getName()));
                                    atributo = metodo.invoke(objeto);

                                    nullToEmpty(atributo, relacionamentos);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            // ex.printStackTrace();
        }
        return objeto;
    }

    public static String primeiraMaiuscula(String texto) {
        return Character.toUpperCase(texto.charAt(0)) + texto.substring(1);
    }

    public static BigDecimal soma(BigDecimal valor1, BigDecimal valor2) {
        BigDecimal resultado = valor1.add(valor2, MathContext.DECIMAL64);
        resultado = resultado.setScale(2, RoundingMode.HALF_UP);
        return resultado;
    }

    public static BigDecimal subtrai(BigDecimal valor1, BigDecimal valor2) {
        BigDecimal resultado = valor1.subtract(valor2, MathContext.DECIMAL64);
        resultado = resultado.setScale(2, RoundingMode.HALF_UP);
        return resultado;
    }

    public static BigDecimal multiplica(BigDecimal valor1, BigDecimal valor2) {
        BigDecimal resultado = valor1.multiply(valor2, MathContext.DECIMAL64);
        resultado = resultado.setScale(2, RoundingMode.HALF_UP);
        return resultado;
    }

    public static BigDecimal divide(BigDecimal valor1, BigDecimal valor2) {
        BigDecimal resultado = valor1.divide(valor2, MathContext.DECIMAL64);
        resultado = resultado.setScale(2, RoundingMode.HALF_UP);
        return resultado;
    }

    public static Date addDay(Date dt, int numberDay) {
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, numberDay);
        return c.getTime();
    }

    public static BigDecimal calcTaxa(BigDecimal total, BigDecimal taxa) {
        BigDecimal valor = total.multiply(taxa.divide(new BigDecimal(100)));
        return valor;
    }

    public static Integer modulo11(String codigo) {
        int total = 0;
        int peso = 2;

        for (int i = 0; i < codigo.length(); i++) {
            total += (codigo.charAt((codigo.length() - 1) - i) - '0') * peso;
            peso++;
            if (peso == 10) {
                peso = 2;
            }
        }
        int resto = total % 11;
        return (resto == 0 || resto == 1) ? 0 : (11 - resto);
    }
}
