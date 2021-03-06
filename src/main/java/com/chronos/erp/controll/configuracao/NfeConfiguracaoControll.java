package com.chronos.erp.controll.configuracao;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.controll.ERPLazyDataModel;
import com.chronos.erp.modelo.entidades.NfeConfiguracao;
import com.chronos.erp.modelo.enuns.TipoArquivo;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.service.comercial.NfeService;
import com.chronos.erp.util.ArquivoUtil;
import com.chronos.erp.util.Constants;
import com.chronos.erp.util.jsf.Mensagem;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.util.StringUtils;

import javax.faces.context.ExternalContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

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


        try {

            if (empresa.getCodigoIbgeUf() == null) {
                throw new ChronosException("Código de IBGE não definido para a empresa");
            }

            super.doCreate();


            getObjeto().setEmpresa(empresa);
            getObjeto().setCaminhoArquivoDanfe("");
            getObjeto().setCaminhoSalvarPdf("S");
            getObjeto().setCaminhoSchemas("");
            getObjeto().setFormatoImpressaoDanfe(1);
            getObjeto().setProcessoEmissao(0);
            getObjeto().setSalvarXml("S");
            getObjeto().setWebserviceUf(empresa.getCodigoIbgeUf().toString());
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro ao tentar cria a configuração de NF-e", ex);
            }
        }

    }


    @Override
    public void salvar() {
        String schema = context.getRealPath(Constants.DIRETORIO_SCHEMA_NFE);
        getObjeto().setCaminhoSchemas(schema);
        getObjeto().setWebserviceUf(empresa.getCodigoIbgeUf().toString());
        nfeService.cleanConf();
        super.salvar();
    }

    public void uploadLogomarca(FileUploadEvent event) {
        try {

            UploadedFile arquivo = event.getFile();
            String nomeArquivo = "logo_transmissao_" + UUID.randomUUID().toString();
            String extensao = arquivo.getFileName().substring(arquivo.getFileName().lastIndexOf("."));
            nomeArquivo += extensao;

            if (!StringUtils.isEmpty(getObjeto().getCaminhoLogomarca())) {
                Files.deleteIfExists(Paths.get(getObjeto().getCaminhoLogomarca()));
            }

            getObjeto().setCaminhoLogomarca(ArquivoUtil.getInstance().escrever(TipoArquivo.LogoTransmissao, empresa.getCnpj(), arquivo.getInputstream(), nomeArquivo));
            salvar();
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
