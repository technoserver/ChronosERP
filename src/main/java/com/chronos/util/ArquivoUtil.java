/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.util;

import com.chronos.modelo.entidades.enuns.TipoArquivo;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import static java.nio.file.FileSystems.getDefault;

/**
 * @author john
 */
public class ArquivoUtil {

    private Path local;
    private Path localTemporario;
    private Path localImagem;
    private Path localProduto;
    private static final Logger logger = LoggerFactory.getLogger(ArquivoUtil.class);

    private static ArquivoUtil instance;

    private ArquivoUtil() {
        this(getDefault().getPath(diretorioRaiz(),".chronos"));
    }

    private ArquivoUtil(Path path) {
        this.local = path;
        this.localTemporario = getDefault().getPath(local.toString(), "temp");
        this.localImagem = getDefault().getPath(local.toString(), "imagens");
        this.localProduto = getDefault().getPath(localImagem.toString(), "produto");
        criarPastas();
    }


    public String escrever(TipoArquivo tipoArquivo, String cnpj, InputStream origem, String nomeArquivo) throws IOException {

        String arquivo = getDestino(tipoArquivo, cnpj) + System.getProperty("file.separator") + nomeArquivo;
        FileOutputStream out = new FileOutputStream(arquivo);
        byte[] bb = new byte[origem.available()];
        origem.read(bb);
        out.write(bb);
        out.close();
        origem.close();
        return arquivo;
    }

    public String escrever(TipoArquivo tipoArquivo, String cnpj, byte[] file, String nomeArquivo) throws IOException {

        String arquivo = getDestino(tipoArquivo, cnpj) + System.getProperty("file.separator") + nomeArquivo;
        FileOutputStream out = new FileOutputStream(arquivo);

        out.write(file);
        out.close();

        return arquivo;
    }

    public String escrever(byte[] file, String nomeArquivo, String caminho) throws IOException {

        String arquivo = caminho + System.getProperty("file.separator") + nomeArquivo;
        FileOutputStream out = new FileOutputStream(arquivo);

        out.write(file);
        out.close();

        return arquivo;
    }

    public String escreverComCopia(TipoArquivo tipoArquivo, String cnpj, byte[] file, String nomeArquivo, Path copia) throws IOException {

        String arquivo = getDestino(tipoArquivo, cnpj) + System.getProperty("file.separator") + nomeArquivo;
        FileOutputStream out = new FileOutputStream(arquivo);

        out.write(file);
        out.close();
        Path localArquivo = getDefault().getPath(arquivo);
        Files.copy(localArquivo, copia, StandardCopyOption.REPLACE_EXISTING);
        return arquivo;
    }



    private static String diretorioRaiz() {

        String sistema = (String) System.getProperties().get("os.name");
        return sistema.startsWith("Windows") ? "c://" : System.getenv("HOME");
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

    public String getCertificado(String cnpj) {
        return getPastaCertificado(cnpj) + System.getProperty("file.separator") + cnpj + ".pfx";
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

    public String getPastaImagens() {
        File dir = new File(diretorio(), "imagens");
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

    public String getPastaXmlNfe(String cnpj) {
        File dir = new File(getDiretorioCnpj(cnpj) + System.getProperty("file.separator") + "xml", "nfe");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    public String getPastaXmlNfeProcessada(String cnpj) {
        File dir = new File(getPastaXmlNfe(cnpj), "processado");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    public String getPastaXmlNfePreProcessada(String cnpj) {
        File dir = new File(getPastaXmlNfe(cnpj), "preprocessado");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    public String getLogoEmpresa(String cnpj, String extensao) {
        return getPastaImagens(cnpj) + System.getProperty("file.separator") + "logo_empresa_" + cnpj + extensao;
    }

    public String getImagemEmpresa(String cnpj) {
        return getPastaImagens(cnpj) + System.getProperty("file.separator") + "logo_empresa_" + cnpj + ".png";
    }

    public String getImagemTransmissao(String cnpj) {
        return getPastaImagens(cnpj) + System.getProperty("file.separator") + "logo_transmissao_" + cnpj + ".png";
    }

    public String salvarFotoProdutoTemporariamente(UploadedFile arquivo) throws IOException {
        String novoNome = renomearArquivo( arquivo.getFileName());
        String caminho = this.localTemporario.toAbsolutePath().toString() + getDefault().getSeparator() + novoNome;
        logger.debug("Caminho temporario : " + caminho);
        Files.copy(arquivo.getInputstream(), Paths.get(new File(caminho).toURI()), StandardCopyOption.REPLACE_EXISTING);
        return novoNome;
    }

    public void salvarFotoProduto(String foto) {
        try {

            if (Files.exists(this.localTemporario.resolve(foto))) {
                Files.move(this.localTemporario.resolve(foto), this.localProduto.resolve(foto), StandardCopyOption.REPLACE_EXISTING);
                Files.deleteIfExists(this.localTemporario.resolve(foto));
            }

        } catch (IOException e) {
            throw new RuntimeException("Erro movendo a foto para destino final", e);
        }
    }

    public String getFotoProduto(String foto) {
        return localProduto.resolve(foto).toString();
    }

    public String getFotoProdutoTemp(String foto) {
        return localTemporario.resolve(foto).toString();
    }

    public void removerFoto(String foto) throws IOException {
        Path caminhoFoto = localProduto.resolve(foto);
        Path caminhoFotoTemp = localTemporario.resolve(foto);
        Files.deleteIfExists(caminhoFoto);
        Files.deleteIfExists(caminhoFotoTemp);
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


    private void criarPastas() {
        try {

            Files.createDirectories(this.local);
            this.localTemporario = getDefault().getPath(this.local.toString(), "temp");
            Files.createDirectories(this.localTemporario);
            Files.createDirectories(this.localImagem);
            Files.createDirectories(this.localProduto);

            if (logger.isDebugEnabled()) {
                logger.debug("Pastas criadas para salvar fotos.");
                logger.debug("Pasta default: " + this.local.toAbsolutePath());
                logger.debug("Pasta temporária: " + this.localTemporario.toAbsolutePath());
                logger.debug("Pasta imagens: " + this.localImagem.toAbsolutePath());
                logger.debug("Pasta iamgens/produto: " + this.localProduto.toAbsolutePath());
            }

        } catch (IOException e) {
            throw new RuntimeException("Erro criando pasta", e);
        }
    }

    private String getDestino(TipoArquivo tipoArquivo, String cnpj) {
        String destino = "";
        switch (tipoArquivo) {
            case LogoTransmissao:
                destino = getPastaImagens(cnpj);
                break;

            case Certificado:
                destino = getPastaCertificado(cnpj);
                break;
            case NFe:
                destino = getPastaXmlNfeProcessada(cnpj);
                break;
            case NFCe:
                destino = getPastaXmlNfeProcessada(cnpj);
                break;
            case NFePreProcessada:
                destino = getPastaXmlNfePreProcessada(cnpj);
                break;

            default:
                destino = local.toString();
        }

        return destino;
    }

    private String diretorio() {
        return getDefault().getPath(diretorioRaiz(), ".chronos").toString();
    }


    public String obterCaminhoCompleto(String path) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext contextoExterno = context.getExternalContext();
        ServletContext servletContext = (ServletContext) contextoExterno.getContext();
        return servletContext.getRealPath(path);
    }

}
