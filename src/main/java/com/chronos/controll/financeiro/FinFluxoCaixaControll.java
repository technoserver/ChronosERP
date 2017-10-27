package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.dto.MapDTO;
import com.chronos.modelo.entidades.ContaCaixa;
import com.chronos.modelo.entidades.view.ViewFinFluxoCaixaID;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.Biblioteca;
import com.chronos.util.jsf.Mensagem;
import com.google.gson.Gson;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by john on 14/08/17.
 */
@Named
@ViewScoped
public class FinFluxoCaixaControll extends AbstractControll<ViewFinFluxoCaixaID> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<ViewFinFluxoCaixaID> fluxos;
    @Inject
    private Repository<ContaCaixa> contaRepository;

    private Date periodo;
    private List<ViewFinFluxoCaixaID> listaFluxoCaixa;
    private List<ViewFinFluxoCaixaID> listaFluxoCaixaOld;
    private List<ViewFinFluxoCaixaID> listaFluxoCaixaDetalhe;
    private Map<String, Integer> contaCaixa;
    private Map<String, BigDecimal> tableDespesa;
    private Map<String, BigDecimal> tableReceita;
    private int idconta;
    private StringBuilder stringBuilder;
    private String jsonDespesas;
    private String jsonReceitas;
    private List<MapDTO> gfDespesas;
    private List<MapDTO> gfReceita;
    private BigDecimal entradas;
    private BigDecimal saidas;
    private BigDecimal resultado;
    private BigDecimal saldo;


    @PostConstruct
    @Override
    public void init() {
        super.init();
        contaCaixa = new LinkedHashMap<>();

        contaCaixa.put("Todas", 0);
        contaCaixa.putAll(contaRepository.getEntitys(ContaCaixa.class, new Object[]{"nome"}).stream()
                .collect(Collectors.toMap(ContaCaixa::getNome, ContaCaixa::getId)));

    }

    @Override
    public void doEdit() {
        super.doEdit();
        buscaDados();
    }

    public void buscaDados() {
        try {
            if (periodo == null) {
                Mensagem.addInfoMessage("Necessário informar o período!");

            } else {
                List<Filtro> filtros = new ArrayList<>();
                filtros.add(new Filtro(Filtro.AND, "viewFinFluxoCaixa.dataVencimento", Filtro.MAIOR_OU_IGUAL, Biblioteca.getDataInicial(periodo)));
                filtros.add(new Filtro(Filtro.AND, "viewFinFluxoCaixa.dataVencimento", Filtro.MENOR_OU_IGUAL, Biblioteca.ultimoDiaMes(periodo)));

                if (idconta > 0) {
                    filtros.add(new Filtro(Filtro.AND, "viewFinFluxoCaixa.idContaCaixa", Filtro.IGUAL, idconta));

                }
                listaFluxoCaixa = fluxos.getEntitys(ViewFinFluxoCaixaID.class, filtros);
//                filtros.clear();
//                filtros.add(new Filtro(Filtro.AND, "viewFinFluxoCaixa.dataVencimento", Filtro.MAIOR_OU_IGUAL, getDataInicialAnterior(periodo)));
//                filtros.add(new Filtro(Filtro.AND, "viewFinFluxoCaixa.dataVencimento", Filtro.MENOR_OU_IGUAL, ultimoDiaMes(periodo)));
//
//                if (idconta > 0) {
//                    filtros.add(new Filtro(Filtro.AND, "viewFinFluxoCaixa.idContaCaixa", Filtro.IGUAL,idconta));
//
//                }
//                listaFluxoCaixaOld = fluxos.getEntitys(ViewFinFluxoCaixaID.class, filtros);

                getTotais();


            }
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage( "Erro ao buscar os dados!",e);

        }
    }


    private Date getDataInicialAnterior(Date perido) {
        try {
            if (periodo == null) {
                return null;
            }
            Calendar dataValida = Calendar.getInstance();
            dataValida.setTime(periodo);
            dataValida.add(Calendar.MONTH, -1);
            dataValida.setLenient(false);
            dataValida.set(Calendar.DAY_OF_MONTH, 1);

            dataValida.getTime();

            return dataValida.getTime();
        } catch (Exception e) {
            return null;
        }
    }


    public void getTotais() {
        saidas = BigDecimal.ZERO;
        entradas = BigDecimal.ZERO;
        saldo = BigDecimal.ZERO;
        DecimalFormat decimalFormat = new DecimalFormat("#,###,##0.00");
        stringBuilder = new StringBuilder();
        gfDespesas = new ArrayList<>();
        gfReceita = new ArrayList<>();
        for (ViewFinFluxoCaixaID f : listaFluxoCaixa) {
            if (f.getViewFinFluxoCaixa().getOperacao().equals("E")) {
                entradas = entradas.add(f.getViewFinFluxoCaixa().getValor());

                gfReceita.add(new MapDTO(f.getViewFinFluxoCaixa().getDescricaoNatureza(), f.getViewFinFluxoCaixa().getValor()));
            } else {
                gfDespesas.add(new MapDTO(f.getViewFinFluxoCaixa().getDescricaoNatureza(), f.getViewFinFluxoCaixa().getValor()));
                saidas = saidas.add(f.getViewFinFluxoCaixa().getValor());
            }
        }
        Collections.sort(gfDespesas);
        Collections.sort(gfReceita);
        jsonDespesas = new Gson().toJson(gfDespesas);
        jsonReceitas = new Gson().toJson(gfReceita);

        stringBuilder = new StringBuilder();


        tableDespesa = Biblioteca.getMap(gfDespesas);
        tableDespesa.put("TOTAL", saidas);

        tableReceita = Biblioteca.getMap(gfReceita);
        tableReceita.put("TOTAL", entradas);
        saldo = entradas.subtract(saidas);
    }

    private void gerarArray(String atributo, double valor) {
        stringBuilder.append("[");
        stringBuilder.append("'");
        stringBuilder.append(atributo);
        stringBuilder.append("'");
        stringBuilder.append(",");
        stringBuilder.append(valor);
        stringBuilder.append("]");
        stringBuilder.append(",");
    }

    private BigDecimal getTotalMap(Map map) {
        BigDecimal valor = Optional.ofNullable(tableDespesa)
                .orElse(new LinkedHashMap<>())
                .values()
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return valor;
    }

    @Override
    protected Class<ViewFinFluxoCaixaID> getClazz() {
        return ViewFinFluxoCaixaID.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "FIN_FLUXO_CAIXA";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public Date getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Date periodo) {
        this.periodo = periodo;
    }

    public List<ViewFinFluxoCaixaID> getListaFluxoCaixa() {
        return listaFluxoCaixa;
    }

    public void setListaFluxoCaixa(List<ViewFinFluxoCaixaID> listaFluxoCaixa) {
        this.listaFluxoCaixa = listaFluxoCaixa;
    }

    public List<ViewFinFluxoCaixaID> getListaFluxoCaixaDetalhe() {
        return listaFluxoCaixaDetalhe;
    }

    public Map<String, Integer> getContaCaixa() {
        return contaCaixa;
    }

    public void setContaCaixa(Map<String, Integer> contaCaixa) {
        this.contaCaixa = contaCaixa;
    }

    public int getIdconta() {
        return idconta;
    }

    public void setIdconta(int idconta) {
        this.idconta = idconta;
    }

    public String getJsonDespesas() {
        return jsonDespesas;
    }

    public void setJsonDespesas(String jsonDespesas) {
        this.jsonDespesas = jsonDespesas;
    }

    public String getJsonReceitas() {
        return jsonReceitas;
    }

    public void setJsonReceitas(String jsonReceitas) {
        this.jsonReceitas = jsonReceitas;
    }

    public Map<String, BigDecimal> getTableDespesa() {
        return tableDespesa;
    }

    public void setTableDespesa(Map<String, BigDecimal> tableDespesa) {
        this.tableDespesa = tableDespesa;
    }

    public Map<String, BigDecimal> getTableReceita() {
        return tableReceita;
    }

    public void setTableReceita(Map<String, BigDecimal> tableReceita) {
        this.tableReceita = tableReceita;
    }

    public BigDecimal getEntradas() {
        return entradas;
    }

    public BigDecimal getSaidas() {
        return saidas;
    }

    public BigDecimal getResultado() {
        return resultado;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }
}
