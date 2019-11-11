package com.chronos.erp.controll.configuracao;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.ConverterCst;
import com.chronos.erp.modelo.enuns.TipoCst;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

@Named
@ViewScoped
public class CstConverterControll extends AbstractControll<ConverterCst> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, String> tipo;

    private HashMap cstOrigem;
    private HashMap cstDestino;


    @Override
    public void doCreate() {
        super.doCreate();
        popularMap();
    }

    @Override
    public void doEdit() {
        super.doEdit();
        popularMap();
        definiCst();
    }

    @Override
    public void salvar() {

        try {
            validar();
            super.salvar();
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro ao salvar o registro", ex);
            }
        }
    }

    public void definiCst() {

        switch (getObjeto().getTipoCst()) {
            case ICMS:
                cstOrigem = getListaCst();
                cstOrigem.putAll(getListaCsosnB());
                break;
            case IPI:
                cstOrigem = getListaCstIpi();
                break;
            case PIS:
                cstOrigem = getListaCstPis();
                break;

            case COFINS:
                cstOrigem = getListaCstPis();

        }

        cstDestino = cstOrigem;
    }

    public HashMap getListaCst() {
        HashMap<String, String> cst = new LinkedHashMap<>();

        cst.put("00 - Tributada Integralmente", "00");
        cst.put("10 - Tributada e com Cobrança do ICMS  por Substituicao Tributária", "10");
        cst.put("20 - Com redução de Base de Calculo", "20");
        cst.put("30 - Isenta ou Não Tributada e com cobrança do ICMS por Substituição tributária", "30");
        cst.put("40 - Isenta", "40");
        cst.put("41 - Não Tributada", "41");
        cst.put("50 - Suspensão", "50");
        cst.put("51 - Direrimento", "51");
        cst.put("60 - ICMS cobrado anteriormente por substituição tributária", "60");
        cst.put("70 - Com redução de base de cálculo e cobrança do ICMS por substituicão tributária", "70");
        cst.put("90 - Outros", "90");
        cst.put("101 - Tributada pelo Simples Nacional com permissão de crédito", "101");
        cst.put("102 - Tributada pelo Simples Nacional sem permissão de crédito", "102");
        cst.put("103 - Isenção do ICMS no Simples Nacional para faixa de receita bruta", "103");
        cst.put("201 - Tributada pelo Simples Nacional com permissão de crédito e com cobrança do ICMS por substituição tributária", "201");
        cst.put("202 - Tributada pelo Simples Nacional sem permissão de crédito e com cobrança do ICMS por substituição tributária", "202");
        cst.put("203 - Isenção do ICMS no Simples Nacional para faixa de receita bruta e com cobrança do ICMS por substituição tributária", "203");
        cst.put("300 - Imune", "300");
        cst.put("400 - Não tributada pelo Simples Nacional", "400");
        cst.put("500 - ICMS cobrado anteriormente por substituição tributária (substituído) ou por antecipação", "500");
        cst.put("900 - Outros", "900");

        return cst;
    }

    private void popularMap() {
        tipo = new LinkedHashMap<>();
        tipo.put("Entrada", "E");
        tipo.put("Saída", "S");
    }

    private void validar() throws ChronosException {
        List<Filtro> filtros = new ArrayList<>();
        filtros.add(new Filtro("tipoCst", getObjeto().getTipoCst()));
        filtros.add(new Filtro("cstDestino", getObjeto().getCstDestino()));
        filtros.add(new Filtro("cstOrigem", getObjeto().getCstOrigem()));

        ConverterCst converterCst = dao.get(ConverterCst.class, filtros);

        if (converterCst != null && !converterCst.equals(getObjeto())) {
            throw new ChronosException("Combinação já definida");
        }

    }


    @Override
    protected Class<ConverterCst> getClazz() {
        return ConverterCst.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "CONVERTER_CST";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public Map<String, String> getTipo() {
        return tipo;
    }

    public HashMap getCstOrigem() {
        return cstOrigem;
    }

    public HashMap getCstDestino() {
        return cstDestino;
    }

    public TipoCst[] getTiposCst() {
        return TipoCst.values();
    }
}
