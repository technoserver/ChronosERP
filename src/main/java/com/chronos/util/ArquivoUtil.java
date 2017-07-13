/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static java.nio.file.FileSystems.getDefault;

import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author john
 */
public class ArquivoUtil {

    private Path local;
    private Path localTemporario;
    private static final Logger logger = LoggerFactory.getLogger(ArquivoUtil.class);

    private static ArquivoUtil instance;

    private ArquivoUtil() {
        this(getDefault().getPath(diretorioRaiz(),".chronos"));
    }

    private ArquivoUtil(Path path) {
        this.local = path;
        criarPastas();
    }

    private void criarPastas() {
        try {

            Files.createDirectories(this.local);
            this.localTemporario = getDefault().getPath(this.local.toString(), "temp");
            Files.createDirectories(this.localTemporario);

            if (logger.isDebugEnabled()) {
                logger.debug("Pastas criadas para salvar fotos.");
                logger.debug("Pasta default: " + this.local.toAbsolutePath());
                logger.debug("Pasta temporária: " + this.localTemporario.toAbsolutePath());
            }

        } catch (IOException e) {
            throw new RuntimeException("Erro criando pasta", e);
        }
    }

    private static String diretorioRaiz() {

        String sistema = (String) System.getProperties().get("os.name");
        return sistema.startsWith("Windows") ? "c://" : System.getenv("HOME");
    }



    private String diretorio() {
        return getDefault().getPath(diretorioRaiz(), ".chronos").toString();
    }

    public static ArquivoUtil getInstance() {
        if (instance == null) {
            instance = new ArquivoUtil();
            return instance;
        }
        return instance;
    }

    public String getDiretorioCnpj(String cnpj) {

        return diretorio() + System.getProperty("file.separator") + cnpj;
    }

    public String getPastaCertificado(String cnpj) {
        File dir = new File(getDiretorioCnpj(cnpj), "certificado");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    public String getPastaImagens(String cnpj) {
        File dir = new File(getDiretorioCnpj(cnpj), "imagens");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    public String getPastaFotoFuncionario(String cnpj) {
        File dir = new File(getDiretorioCnpj(cnpj), "fotosFuncionario");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    public String getImagemEmpresa(String cnpj) {
        return getPastaImagens(cnpj) + System.getProperty("file.separator") + "logo_empresa_" + cnpj + ".png";
    }

    public void salvarFotoProdutoTemporariamente(UploadedFile arquivo){
        String novoNome = renomearArquivo( arquivo.getFileName());
      //  String pastaFotoProduto =
      //  Files.copy(arquivo.getInputstream(), Paths.get(new File(foto3x4).toURI()), StandardCopyOption.REPLACE_EXISTING);

    }

    public void salvarFotoProduto(String foto) {
        try {
            Files.move(this.localTemporario.resolve(foto), this.local.resolve(foto));
        } catch (IOException e) {
            throw new RuntimeException("Erro movendo a foto para destino final", e);
        }
    }

    public String getFotoFuncionario(String cnpj, String cpf) {
        cpf = cpf.replaceAll("\\D", "");
        return getPastaFotoFuncionario(cnpj) + System.getProperty("file.separator") + "foto_funcionario_" + cpf + ".png";
    }



    public byte[] recuperarImagemTemporaria(String nome) {
        try {
            return Files.readAllBytes(this.localTemporario.resolve(nome));
        } catch (IOException e) {
            throw new RuntimeException("Erro lendo a imagem temporária", e);
        }
    }

    private String renomearArquivo(String nomeOriginal) {
        String novoNome = UUID.randomUUID().toString() + "_" + nomeOriginal;

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Nome original: %s, novo nome: %s", nomeOriginal, novoNome));
        }

        return novoNome;

    }

}
