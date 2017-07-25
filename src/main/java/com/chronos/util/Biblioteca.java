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

/**
 * @author john
 */
public class Biblioteca {

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
                return (true);
            } else {
                return (false);
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
}
