/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.Ibpt;
import com.chronos.util.jsf.Mensagem;
import org.apache.commons.io.FileUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 *
 * @author john
 */
@Named
@ViewScoped
public class IbptControll extends AbstractControll<Ibpt> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public void importaArquivo(FileUploadEvent event) {
        try {
            dao.excluir(Ibpt.class);
            UploadedFile arquivo = event.getFile();
            File file = File.createTempFile("file", ".tmp");
            FileUtils.copyInputStreamToFile(arquivo.getInputstream(), file);
            List<String> lines = FileUtils.readLines(file);
            String linhaArquivo[];

            List<Ibpt> tabela;
            for (int i = 1; i < lines.size(); i++) {

                linhaArquivo = lines.get(i).split("\\;");

                importarTabela(linhaArquivo);

            }
            Mensagem.addInfoMessage("Importação realizada com sucesso");
        } catch (Exception ex) {
            Mensagem.addErrorMessage("Erro ao realizar a importação", ex);
            throw new RuntimeException("Erro ao realizar a importação",ex);
            
        } 
    }

    public void importarTabela(String[] linha) throws ParseException, Exception {
      

            if (linha.length > 10) {
                Ibpt tb = new Ibpt();
                String ncm = linha[0].replaceAll("\\D","");      
                ncm = ncm.substring(0,(ncm.length() > 8?7:ncm.length()));
                tb.setNcm(ncm);
                tb.setEx(linha[1]);
                tb.setTipo('0');
                tb.setDescricao(linha[3]);
                tb.setNacionalFederal(new BigDecimal(linha[4]));
                tb.setImportadosFederal(new BigDecimal(linha[5]));
                tb.setEstadual(new BigDecimal(linha[6]));
                tb.setMunicipal(new BigDecimal(linha[7]));
                tb.setVigenciaInicio(sdf.parse(linha[8]));
                tb.setVigenciaFim(sdf.parse(linha[9]));
                dao.salvar(tb);
            }

     
    }

    @Override
    protected Class<Ibpt> getClazz() {
        return Ibpt.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "IBPT";
    }

}
