package com.chronos.controll.configuracao;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.PdvCaixa;
import com.chronos.modelo.entidades.PdvConfiguracao;
import com.chronos.modelo.entidades.PdvVendaCabecalho;
import com.chronos.modelo.enuns.TipoArquivo;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 04/10/17.
 */
@Named
@ViewScoped
public class NfceConfiguracaoControll extends AbstractControll<PdvConfiguracao> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<PdvCaixa> repositoryCaixa;
    @Inject
    private Repository<PdvVendaCabecalho> pdvVendaCabecalhoRepository;

    @Inject
    private ExternalContext context;
    @Inject
    private NfeService nfeService;

    private int idcaixa;

    @Override
    public ERPLazyDataModel<PdvConfiguracao> getDataModel() {

        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(PdvConfiguracao.class);
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
        getObjeto().setWebserviceUf(empresa.getCodigoIbgeUf().toString());

    }

    @Override
    public void doEdit() {
        super.doEdit();
        idcaixa = getObjeto().getPdvCaixa().getId();
    }

    @Override
    public void salvar() {


        try {
            boolean existeRegisro = pdvVendaCabecalhoRepository.existeRegisro(PdvVendaCabecalho.class, "pdvMovimento.pdvCaixa.id", idcaixa);

            if (existeRegisro && getObjeto().getPdvCaixa().getId() != idcaixa) {
                throw new ChronosException("Não é possivel altera o caixa, já foram realizadas vendas");
            }

            String schema = context.getRealPath(Constantes.DIRETORIO_SCHEMA_NFE);
            getObjeto().setCaminhoSchemas(schema);
            getObjeto().setWebserviceUf(empresa.getCodigoIbgeUf().toString());
            nfeService.cleanConf();
            super.salvar();
        } catch (Exception ex) {
            Mensagem.addErrorMessage("", ex);
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


    public List<PdvCaixa> getListaPdvCaixa(String nome) {
        List<PdvCaixa> caixas = new ArrayList<>();
        try {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro(Filtro.AND, "web", Filtro.IGUAL, "S"));
            filtros.add(new Filtro(Filtro.AND, "nome", Filtro.LIKE, nome));
            caixas = repositoryCaixa.getEntitys(PdvCaixa.class, filtros);
        } catch (Exception ex) {

        }
        return caixas;
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
