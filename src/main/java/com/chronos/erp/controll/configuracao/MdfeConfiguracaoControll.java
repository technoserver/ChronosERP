package com.chronos.erp.controll.configuracao;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.controll.ERPLazyDataModel;
import com.chronos.erp.modelo.entidades.MdfeConfiguracao;
import com.chronos.erp.modelo.enuns.TipoArquivo;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.util.ArquivoUtil;
import com.chronos.erp.util.Constants;
import com.chronos.erp.util.jsf.Mensagem;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.faces.context.ExternalContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

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
    public ERPLazyDataModel<MdfeConfiguracao> getDataModel() {

        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(MdfeConfiguracao.class);
            dataModel.setDao(dao);
        }

        dataModel.getFiltros().clear();
        dataModel.getFiltros().add(new Filtro("empresa.id", empresa.getId()));
        return dataModel;
    }

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setEmpresa(empresa);
        String schemas = context.getRealPath(Constants.DIRETORIO_SCHEMA_MDFE);
        getObjeto().setWebserviceUf(empresa.buscarEnderecoPrincipal().getMunicipioIbge().toString().substring(0, 2));
        getObjeto().setCaminhoSchemas(schemas);
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
