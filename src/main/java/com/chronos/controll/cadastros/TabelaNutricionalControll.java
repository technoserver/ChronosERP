package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.Nutriente;
import com.chronos.modelo.entidades.TabelaNutricionalCabecalho;
import com.chronos.modelo.entidades.TabelaNutricionalDetalhe;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class TabelaNutricionalControll extends AbstractControll<TabelaNutricionalCabecalho> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<Nutriente> nutrienteRepository;

    private Map<String, String> unidades;

    private TabelaNutricionalDetalhe item;
    private TabelaNutricionalDetalhe itemSelecionado;


    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setNutrientes(new ArrayList<>());
        iniciarValores();
    }


    @Override
    public void doEdit() {
        super.doEdit();
        TabelaNutricionalCabecalho tabelaNutricional = dataModel.getRowData(getObjetoSelecionado().getId().toString());
        setObjeto(tabelaNutricional);
        iniciarValores();
        setTelaGrid(false);
    }

    @Override
    public void salvar() {


        try {

            if (getObjeto().getNutrientes().isEmpty()) {
                throw new ChronosException("É preciso informa os nutrientes");
            }
            super.salvar();
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("erro ao salvar tabela nutricional", ex);
            }
        }
    }

    public void incluirItem() {
        item = new TabelaNutricionalDetalhe();
        item.setTabelaNutricional(getObjeto());

    }

    public void alterarItem() {
        item = itemSelecionado;
    }

    public void salvarItem() {
        if (item.getId() == null) {
            getObjeto().getNutrientes().add(item);
        }
    }

    public List<Nutriente> getListaNutrientes(String nome) {
        List<Nutriente> nutrientes = new ArrayList<>();
        try {
            nutrientes = nutrienteRepository.getEntitys(Nutriente.class, "nome", nome);
        } catch (Exception ex) {

        }

        return nutrientes;

    }

    private void iniciarValores() {
        unidades = new HashMap<>();
        unidades.put("GRAMA", "GR");
        unidades.put("MILILITRO", "ML");
    }

    @Override
    protected Class<TabelaNutricionalCabecalho> getClazz() {
        return TabelaNutricionalCabecalho.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "TABELA_NUTRICIONAL";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public Map<String, String> getUnidades() {
        return unidades;
    }

    public void setUnidades(Map<String, String> unidades) {
        this.unidades = unidades;
    }

    public TabelaNutricionalDetalhe getItem() {
        return item;
    }

    public void setItem(TabelaNutricionalDetalhe item) {
        this.item = item;
    }

    public TabelaNutricionalDetalhe getItemSelecionado() {
        return itemSelecionado;
    }

    public void setItemSelecionado(TabelaNutricionalDetalhe itemSelecionado) {
        this.itemSelecionado = itemSelecionado;
    }
}
