package com.chronos.controll.mdfe;

import br.com.samuelweb.certificado.Certificado;
import br.com.samuelweb.certificado.CertificadoService;
import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.MdfeConfiguracao;
import com.chronos.modelo.enuns.TipoArquivo;
import com.chronos.util.ArquivoUtil;
import com.chronos.util.Constantes;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.util.StringUtils;

import javax.faces.context.ExternalContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by john on 19/06/18.
 */
@Named
@ViewScoped
public class MdfeConfiguracaoControll extends AbstractControll<MdfeConfiguracao> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private ExternalContext context;

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setEmpresa(empresa);
        String schemas = context.getRealPath(Constantes.DIRETORIO_SCHEMA_MDFE);
        getObjeto().setCaminhoSchemas(schemas);
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
    protected Class<MdfeConfiguracao> getClazz() {
        return MdfeConfiguracao.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "MDFE_CONFIGURACAO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
