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
import java.util.*;

@Named
@ViewScoped
public class TabelaNutricionalControll extends AbstractControll<TabelaNutricionalCabecalho> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<Nutriente> nutrienteRepository;

    private Map<String, String> unidades;
    private Map<String, String> medidaCaseria;
    private Map<String, Integer> parteDecimalMedida;

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
        unidades.put("UNDIADE", "UN");

        parteDecimalMedida = new LinkedHashMap<>();

        parteDecimalMedida.put("Para 0", 0);
        parteDecimalMedida.put("Para 1/4", 1);
        parteDecimalMedida.put("Para 1/3", 2);
        parteDecimalMedida.put("Para 1/2", 3);
        parteDecimalMedida.put("Para 2/3", 4);
        parteDecimalMedida.put("Para 3/4", 5);

        medidaCaseria = new LinkedHashMap<>();
        medidaCaseria.put("Colher(es) de Sopa", "00");
        medidaCaseria.put("Colher(es) de Café", "01");
        medidaCaseria.put("Colher(es) de Chá", "02");
        medidaCaseria.put("Xícara(s)", "03");
        medidaCaseria.put("De Xícara(s)", "04");
        medidaCaseria.put("Unidade(s)", "05");
        medidaCaseria.put("Pacote(s)", "06");
        medidaCaseria.put("Fatia(s)", "07");
        medidaCaseria.put("Fatia(s) Fina(s)", "08");
        medidaCaseria.put("Pedaço(s)", "09");
        medidaCaseria.put("Folha(s)", "10");
        medidaCaseria.put("Pão(es)", "11");
        medidaCaseria.put("Biscoito(s)", "12");
        medidaCaseria.put("Bisnaguinha(s)", "13");
        medidaCaseria.put("Disco(s)", "14");
        medidaCaseria.put("Copo(s)", "15");
        medidaCaseria.put("Porção(ões)", "16");
        medidaCaseria.put("Tablete(s)", "17");
        medidaCaseria.put("Sache(s)", "18");
        medidaCaseria.put("Almôndega(s)", "19");
        medidaCaseria.put("Bife(s)", "20");
        medidaCaseria.put("Filé(s)", "21");
        medidaCaseria.put("Concha(s)", "22");
        medidaCaseria.put("Bala(s)", "23");
        medidaCaseria.put("Prato(s) Fundo(s)", "24");
        medidaCaseria.put("Pitada(s)", "25");
        medidaCaseria.put("Lata(s)", "26");
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

    public Map<String, String> getMedidaCaseria() {
        return medidaCaseria;
    }

    public Map<String, Integer> getParteDecimalMedida() {
        return parteDecimalMedida;
    }
}
