package com.chronos.erp.controll.vendas;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.controll.ERPLazyDataModel;
import com.chronos.erp.modelo.entidades.CondicoesPagamento;
import com.chronos.erp.modelo.entidades.CondicoesParcelas;
import com.chronos.erp.modelo.entidades.FinTipoRecebimento;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.util.jsf.Mensagem;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 16/08/17.
 */
@Named
@ViewScoped
public class VendaCondicoesPagamentoControll extends AbstractControll<CondicoesPagamento> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<FinTipoRecebimento> tipos;

    private CondicoesParcelas condicoesParcelas;

    private String nome;
    private Integer idmepresaFiltro;

    @PostConstruct
    @Override
    public void init() {
        super.init();
        idmepresaFiltro = empresa.getId();
        pesquisarEmpresas();
    }

    public ERPLazyDataModel<CondicoesPagamento> getDataModel() {

        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setDao(dao);
            dataModel.setClazz(CondicoesPagamento.class);
        }

        pesquisar();
        return dataModel;
    }


    public void pesquisar() {
        dataModel.getFiltros().clear();
        if (!StringUtils.isEmpty(nome)) {
            dataModel.addFiltro("nome", nome);
        }
        idmepresaFiltro = idmepresaFiltro == null || idmepresaFiltro == 0 ? empresa.getId() : idmepresaFiltro;
        dataModel.addFiltro("empresa.id", idmepresaFiltro, Filtro.IGUAL);
    }

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setEmpresa(empresa);
        getObjeto().setParcelas(new ArrayList<>());
        getObjeto().setVistaPrazo("0");
    }

    @Override
    public void doEdit() {
        super.doEdit();
        CondicoesPagamento condicoes = dataModel.getRowData(getObjeto().getId().toString());
        setObjeto(condicoes);

    }

    @Override
    public void salvar() {
        try {
            if (getObjeto().getVistaPrazo().equals("1")) {
                verificaParcelas();
            }
            super.salvar();
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao salvar o registro!", e);

        }
    }

    public void incluirVendaCondicoesParcelas() {
        condicoesParcelas = new CondicoesParcelas();
        condicoesParcelas.setCondicoesPagamento(getObjeto());
    }

    public void salvarVendaCondicoesParcelas() {
        getObjeto().getParcelas().add(condicoesParcelas);
    }

    private void verificaParcelas() throws Exception {
        if (getObjeto().getParcelas().isEmpty()) {
            throw new Exception("Para condições de pagamento a prazo é preciso informa as parcelas");
        }
        double prazoMedio = 0;
        BigDecimal totalPorcento = BigDecimal.ZERO;
        for (int i = 0; i < getObjeto().getParcelas().size(); i++) {
            totalPorcento = totalPorcento.add(getObjeto().getParcelas().get(i).getTaxa());
            if (i == 0) {
                prazoMedio = getObjeto().getParcelas().get(i).getDias();
            } else {
                prazoMedio += (getObjeto().getParcelas().get(i).getDias() - getObjeto().getParcelas().get(i - 1).getDias());
            }
        }
        if (totalPorcento.compareTo(BigDecimal.valueOf(100)) != 0) {
            throw new Exception("Os valores informados nas taxas não fecham em 100%.");
        }
        prazoMedio = prazoMedio / getObjeto().getParcelas().size();
        getObjeto().setPrazoMedio((int) prazoMedio);
    }

    public List<FinTipoRecebimento> getListaFinTipoRecebimento(String nome) {
        List<FinTipoRecebimento> listaFinTipoRecebimento = new ArrayList<>();
        try {
            listaFinTipoRecebimento = tipos.getEntitys(FinTipoRecebimento.class, "descricao", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaFinTipoRecebimento;
    }

    @Override
    protected Class<CondicoesPagamento> getClazz() {
        return CondicoesPagamento.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "CONDICOES_PAGAMENTO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public CondicoesParcelas getCondicoesParcelas() {
        return condicoesParcelas;
    }

    public void setCondicoesParcelas(CondicoesParcelas condicoesParcelas) {
        this.condicoesParcelas = condicoesParcelas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getIdmepresaFiltro() {
        return idmepresaFiltro;
    }

    public void setIdmepresaFiltro(Integer idmepresaFiltro) {
        this.idmepresaFiltro = idmepresaFiltro;
    }
}
