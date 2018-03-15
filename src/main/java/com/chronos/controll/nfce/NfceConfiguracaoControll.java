package com.chronos.controll.nfce;

import br.com.samuelweb.certificado.Certificado;
import br.com.samuelweb.certificado.CertificadoService;
import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.PdvCaixa;
import com.chronos.modelo.entidades.PdvConfiguracao;
import com.chronos.modelo.enuns.TipoArquivo;
import com.chronos.util.ArquivoUtil;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.util.StringUtils;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by john on 04/10/17.
 */
@Named
@ViewScoped
public class NfceConfiguracaoControll extends AbstractControll<PdvConfiguracao> implements Serializable {

    private static final long serialVersionUID = 1L;


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
        getObjeto().setPdvCaixa(new PdvCaixa(1));

    }

    public void uploadCertificado(FileUploadEvent event) {
        try {
            if (StringUtils.isEmpty(getObjeto().getCertificadoDigitalSenha())) {
                throw new Exception("É preciso definir a senha do certificado primeiramente");
            }
            UploadedFile arquivo = event.getFile();
            String nomeArquivo = empresa.getCnpj();
            String extensao = arquivo.getFileName().substring(arquivo.getFileName().lastIndexOf("."));
            nomeArquivo += extensao;
            String caminhoCertificado = ArquivoUtil.getInstance().escrever(TipoArquivo.Certificado, empresa.getCnpj(), arquivo.getInputstream(), nomeArquivo);
            Certificado certificado = CertificadoService.certificadoPfx(caminhoCertificado, getObjeto().getCertificadoDigitalSenha());
            if (!certificado.isValido()) {
                Path local = FileSystems.getDefault().getPath(caminhoCertificado);
                Files.deleteIfExists(local);
                throw new Exception("Certificado não é valido data de validade " + certificado.getVencimento());
            }
            getObjeto().setCertificadoDigitalCaminho(caminhoCertificado);
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
    protected Class<PdvConfiguracao> getClazz() {
        return PdvConfiguracao.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "NFCE_CONFIGURACAO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
