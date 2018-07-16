package com.chronos.controll.configuracao;

import br.com.samuelweb.certificado.Certificado;
import br.com.samuelweb.certificado.CertificadoService;
import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.CertificadoConfiguracao;
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
 * Created by john on 16/07/18.
 */
@Named
@ViewScoped
public class CertificadoConfiguracaoControll extends AbstractControll<CertificadoConfiguracao> implements Serializable {

    private static final long serialVersionUID = 1L;


    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setTipo("A1");
        getObjeto().setEmpresa(empresa);
    }

    public void uploadCertificado(FileUploadEvent event) {
        try {
            if (StringUtils.isEmpty(getObjeto().getSenha())) {
                throw new Exception("É preciso definir a senha do certificado primeiramente");
            }
            UploadedFile arquivo = event.getFile();
            String nomeArquivo = empresa.getCnpj();
            String extensao = arquivo.getFileName().substring(arquivo.getFileName().lastIndexOf("."));
            nomeArquivo += extensao;
            String caminhoCertificado = ArquivoUtil.getInstance().escrever(TipoArquivo.Certificado, empresa.getCnpj(), arquivo.getInputstream(), nomeArquivo);
            Certificado certificado = CertificadoService.certificadoPfx(caminhoCertificado, getObjeto().getSenha());
            if (!certificado.isValido()) {
                Path local = FileSystems.getDefault().getPath(caminhoCertificado);
                Files.deleteIfExists(local);
                throw new Exception("Certificado não é valido data de validade " + certificado.getVencimento());
            }
            getObjeto().setCaminho(caminhoCertificado);
        } catch (Exception e) {
            Mensagem.addErrorMessage("", e);
            e.printStackTrace();
        }
    }

    @Override
    protected Class<CertificadoConfiguracao> getClazz() {
        return CertificadoConfiguracao.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "CERTIFICADO_CONFIGURACAO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
