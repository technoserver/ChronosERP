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
 *
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
}
