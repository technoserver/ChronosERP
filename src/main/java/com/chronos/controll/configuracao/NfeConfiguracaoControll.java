package com.chronos.controll.configuracao;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.NfeConfiguracao;
import com.chronos.modelo.enuns.TipoArquivo;
import com.chronos.repository.Filtro;
import com.chronos.service.comercial.NfeService;
import com.chronos.util.ArquivoUtil;
import com.chronos.util.Constantes;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.faces.context.ExternalContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 26/09/17.
 */
@Named
@ViewScoped
public class NfeConfiguracaoControll extends AbstractControll<NfeConfiguracao> implements Serializable {

    @Inject
    private ExternalContext context;
    @Inject
    private NfeService nfeService;

    @Override
    public ERPLazyDataModel<NfeConfiguracao> getDataModel() {

        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(NfeConfiguracao.class);
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
        getObjeto().setCaminhoArquivoDanfe("");
        getObjeto().setCaminhoSalvarPdf("S");
        getObjeto().setCaminhoSchemas("");
        getObjeto().setFormatoImpressaoDanfe(1);
        getObjeto().setProcessoEmissao(0);
        getObjeto().setSalvarXml("S");
        getObjeto().setWebserviceUf(empresa.getCodigoIbgeUf().toString());

    }


    @Override
    public void salvar() {
        String schema = context.getRealPath(Constantes.DIRETORIO_SCHEMA_NFE);
        getObjeto().setCaminhoSchemas(schema);
        getObjeto().setWebserviceUf(empresa.getCodigoIbgeUf().toString());
        nfeService.cleanConf();
        super.salvar();
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
