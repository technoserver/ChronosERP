package com.chronos.controll.configuracao;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.PdvCaixa;
import com.chronos.modelo.entidades.PdvConfiguracao;
import com.chronos.modelo.entidades.PdvConfiguracaoBalanca;
import com.chronos.modelo.entidades.PdvVendaCabecalho;
import com.chronos.modelo.enuns.ModeloBalanca;
import com.chronos.modelo.enuns.TipoArquivo;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.service.comercial.NfeService;
import com.chronos.service.configuracao.PdvConfiguracaoService;
import com.chronos.util.ArquivoUtil;
import com.chronos.util.Constants;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.faces.context.ExternalContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by john on 04/10/17.
 */
@Named
@ViewScoped
public class PdvConfiguracaoControll extends AbstractControll<PdvConfiguracao> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<PdvCaixa> repositoryCaixa;
    @Inject
    private Repository<PdvVendaCabecalho> pdvVendaCabecalhoRepository;

    @Inject
    private ExternalContext context;
    @Inject
    private NfeService nfeService;

    @Inject
    private PdvConfiguracaoService service;

    private PdvConfiguracaoBalanca balanca;


    private Map<String, String> tipos;

    private int tamanhoCodigoProduto = 4;

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
        balanca = new PdvConfiguracaoBalanca();
        balanca.setPdvConfiguracao(getObjeto());
        getObjeto().setEmpresa(empresa);
        getObjeto().setWebserviceUf(empresa.getCodigoIbgeUf().toString());

        preencherTipos();
    }

    @Override
    public void doEdit() {
        super.doEdit();
        preencherTipos();
        if (getObjeto().getPdvConfiguracaoBalanca() == null) {
            balanca = new PdvConfiguracaoBalanca();
            balanca.setPdvConfiguracao(getObjeto());
        } else {
            balanca = getObjeto().getPdvConfiguracaoBalanca();
        }

    }

    @Override
    public void remover() {
        dao.excluir(getObjetoSelecionado());
    }

    private void preencherTipos() {
        tipos = new HashMap<>();
        tipos.put("Valor Total", "T");
        tipos.put("Quantidade", "Q");
        tipos.put("Peso", "P");
    }

    @Override
    public void salvar() {


        try {

            String schema = context.getRealPath(Constants.DIRETORIO_SCHEMA_NFE);
            getObjeto().setCaminhoSchemas(schema);
            getObjeto().setWebserviceUf(empresa.getCodigoIbgeUf().toString());
            getObjeto().setPdvConfiguracaoBalanca(balanca);
            PdvConfiguracao result = service.salvar(getObjeto());
            nfeService.cleanConf();
            setObjeto(result);
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("erro salvar", ex);
            }
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
        return "PDV_CONFIGURACAO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public ModeloBalanca[] getModelos() {
        return ModeloBalanca.values();
    }

    public PdvConfiguracaoBalanca getBalanca() {
        return balanca;
    }

    public void setBalanca(PdvConfiguracaoBalanca balanca) {
        this.balanca = balanca;
    }

    public Map<String, String> getTipos() {
        return tipos;
    }

    public int getTamanhoCodigoProduto() {
        return tamanhoCodigoProduto;
    }

    public void setTamanhoCodigoProduto(int tamanhoCodigoProduto) {
        this.tamanhoCodigoProduto = tamanhoCodigoProduto;
    }
}
