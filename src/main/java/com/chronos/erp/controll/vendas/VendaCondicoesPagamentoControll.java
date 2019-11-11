package com.chronos.erp.controll.vendas;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.FinTipoRecebimento;
import com.chronos.erp.modelo.entidades.VendaCondicoesPagamento;
import com.chronos.erp.modelo.entidades.VendaCondicoesParcelas;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.util.jsf.Mensagem;
import org.springframework.util.StringUtils;

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
public class VendaCondicoesPagamentoControll extends AbstractControll<VendaCondicoesPagamento> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<FinTipoRecebimento> tipos;

    private VendaCondicoesParcelas vendaCondicoesParcelas;

    private String nome;


    public void pesquisar() {
        dataModel.getFiltros().clear();
        if (!StringUtils.isEmpty(nome)) {
            dataModel.addFiltro("nome", nome);
        }
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
        VendaCondicoesPagamento condicoes = dataModel.getRowData(getObjeto().getId().toString());
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
        vendaCondicoesParcelas = new VendaCondicoesParcelas();
        vendaCondicoesParcelas.setVendaCondicoesPagamento(getObjeto());
    }

    public void salvarVendaCondicoesParcelas() {
        getObjeto().getParcelas().add(vendaCondicoesParcelas);
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
    protected Class<VendaCondicoesPagamento> getClazz() {
        return VendaCondicoesPagamento.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "CONDICOES_PAGAMENTO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public VendaCondicoesParcelas getVendaCondicoesParcelas() {
        return vendaCondicoesParcelas;
    }

    public void setVendaCondicoesParcelas(VendaCondicoesParcelas vendaCondicoesParcelas) {
        this.vendaCondicoesParcelas = vendaCondicoesParcelas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}