package com.chronos.controll.configuracao;

import br.com.samuelweb.certificado.Certificado;
import br.com.samuelweb.certificado.CertificadoService;
import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.CertificadoConfiguracao;
import com.chronos.modelo.enuns.TipoArquivo;
import com.chronos.repository.Filtro;
import com.chronos.util.ArquivoUtil;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by john on 16/07/18.
 */
@Named
@ViewScoped
public class CertificadoConfiguracaoControll extends AbstractControll<CertificadoConfiguracao> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, String> tipos;


    @PostConstruct
    @Override
    public void init() {
        super.init();
        tipos = new LinkedHashMap<>();
        tipos.put("A1", "A1");
        tipos.put("A3", "A3");
    }

    @Override
    public ERPLazyDataModel<CertificadoConfiguracao> getDataModel() {

        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(CertificadoConfiguracao.class);
            dataModel.setDao(dao);
        }

        dataModel.getFiltros().clear();
        dataModel.getFiltros().add(new Filtro("empresa.id", empresa.getId()));
        return dataModel;
    }

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setTipo("A1");
        getObjeto().setEmpresa(empresa);
    }

    @Override
    public void salvar() {
        if (getObjeto().getTipo().equals("A1")) {
            getObjeto().setCaminho("");
            getObjeto().setSenha("01");
            getObjeto().setSerie("");
        }
        super.salvar();
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

    public Map<String, String> getTipos() {
        return tipos;
    }

    public void setTipos(Map<String, String> tipos) {
        this.tipos = tipos;
    }
}
