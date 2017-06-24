/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.util;

import java.io.File;
import java.io.IOException;
import static java.nio.file.FileSystems.getDefault;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author john
 */
public class ArquivoUtil {

    private Path local;
    private static ArquivoUtil instance;

    private ArquivoUtil() {
        this(getDefault().getPath(diretorioRaiz(), ".chronos"));
    }

    private ArquivoUtil(Path path) {
        this.local = path;
        criarPastas();
    }

    private void criarPastas() {
        try {

            Files.createDirectories(this.local);

        } catch (IOException e) {
            throw new RuntimeException("Erro criando pasta", e);
        }
    }

    private static String diretorioRaiz() {

        String sistema = (String) System.getProperties().get("os.name");
        return sistema.startsWith("Windows") ? "c://" : System.getenv("HOME");
    }

    private  String diretorio() {
        return getDefault().getPath(diretorioRaiz(), ".chronos").toString();
    }

    public static ArquivoUtil getInstance() {
        if (instance == null) {
            instance = new ArquivoUtil();
            return instance;
        }
        return instance;
    }

    public  String getDiretorioCnpj(String cnpj) {

        return diretorio() + System.getProperty("file.separator") + cnpj;
    }

    public  String getPastaCertificado(String cnpj) {
        File dir = new File(getDiretorioCnpj(cnpj), "certificado");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    public  String getPastaImagens(String cnpj) {
        File dir = new File(getDiretorioCnpj(cnpj), "imagens");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    public  String getPastaFotoFuncionario(String cnpj) {
        File dir = new File(getDiretorioCnpj(cnpj), "fotosFuncionario");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    public  String getImagemEmpresa(String cnpj) {
        return getPastaImagens(cnpj) + System.getProperty("file.separator") + "logo_empresa_" + cnpj + ".png";
    }
    
     public  String getFotoFuncionario(String cnpj,String cpf) {
        cpf = cpf.replaceAll("\\D","");
        return getPastaFotoFuncionario(cnpj) + System.getProperty("file.separator") + "foto_funcionario_" + cpf + ".png";
    }

}
