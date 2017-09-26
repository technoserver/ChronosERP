package com.chronos.controll.nfe;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.NfeConfiguracao;
import com.chronos.modelo.entidades.enuns.TipoArquivo;
import com.chronos.util.ArquivoUtil;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 26/09/17.
 */
@Named
@ViewScoped
public class NfeConfiguracaoControll extends AbstractControll<NfeConfiguracao> implements Serializable {


    @Override
    public void doCreate() {
        super.doCreate();

        getObjeto().setEmpresa(empresa);
        getObjeto().setCaminhoArquivoDanfe("");
        getObjeto().setCaminhoSalvarPdf("S");
        getObjeto().setCaminhoSchemas("");
        getObjeto().setFormatoImpressaoDanfe(1);
        getObjeto().setProcessoEmissao(0);
        getObjeto().setSalvarXml("S");

    }


    public void uploadCertificado(FileUploadEvent event) {
        try {

            UploadedFile arquivo = event.getFile();
            String nomeArquivo = empresa.getCnpj();
            String extensao = arquivo.getFileName().substring(arquivo.getFileName().lastIndexOf("."));
            nomeArquivo += extensao;
            getObjeto().setCertificadoDigitalCaminho(ArquivoUtil.getInstance().escrever(TipoArquivo.Certificado, empresa.getCnpj(), arquivo.getInputstream(), nomeArquivo));
        } catch (Exception e) {
            Mensagem.addErrorMessage("", e);
            e.printStackTrace();
        }
    }

    public void uploadLogomarca(FileUploadEvent event) {
        try {

            UploadedFile arquivo = event.getFile();
            String nomeArquivo = "logo_transmissao_" + empresa.getCnpj();
            String extensao = arquivo.getFileName().substring(arquivo.getFileName().lastIndexOf("."));
            nomeArquivo += extensao;
            getObjeto().setCaminhoLogomarca(ArquivoUtil.getInstance().escrever(TipoArquivo.LogoTransmissao, empresa.getCnpj(), arquivo.getInputstream(), nomeArquivo));

        } catch (Exception e) {
            Mensagem.addErrorMessage("", e);
            e.printStackTrace();
        }
    }


    @Override
    protected Class<NfeConfiguracao> getClazz() {
        return NfeConfiguracao.class;
    }

    @Override
    protected String getFuncaoBase() {
        return null;
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
