package com.chronos.controll.tributacao;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.*;
import com.chronos.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by john on 16/08/17.
 */
@Named
@ViewScoped
public class TributConfiguraOfGtControll extends AbstractControll<TributConfiguraOfGt> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<TributGrupoTributario> grupos;
    @Inject
    private Repository<TributOperacaoFiscal> operacoes;
    @Inject
    private Repository<TipoReceitaDipi> ipis;

    private TributIcmsUf tributIcmsUf;
    private TributIcmsUf tributIcmsUfSelecionado;

    private boolean simplesNascional;

    @Override
    public void doCreate() {
        super.doCreate();

        TributPisCodApuracao pis = new TributPisCodApuracao();

        getObjeto().setTributPisCodApuracao(pis);
        pis.setAliquotaUnidade(BigDecimal.ZERO);
        pis.setEfdTabela435("01");
        pis.setModalidadeBaseCalculo("1");
        pis.setPorcentoBaseCalculo(BigDecimal.ZERO);
        pis.setValorPautaFiscal(BigDecimal.ZERO);
        pis.setValorPrecoMaximo(BigDecimal.ZERO);
        pis.setAliquotaPorcento(BigDecimal.ZERO);


        TributCofinsCodApuracao cofins = new TributCofinsCodApuracao();
        cofins.setAliquotaUnidade(BigDecimal.ZERO);
        cofins.setEfdTabela435("01");
        cofins.setModalidadeBaseCalculo("1");
        cofins.setPorcentoBaseCalculo(BigDecimal.ZERO);
        cofins.setValorPautaFiscal(BigDecimal.ZERO);
        cofins.setValorPrecoMaximo(BigDecimal.ZERO);

        cofins.setAliquotaPorcento(BigDecimal.ZERO);
        getObjeto().setTributCofinsCodApuracao(cofins);

        TributIpiDipi ipi = new TributIpiDipi();

        getObjeto().setTributIpiDipi(ipi);
        ipi.setAliquotaPorcento(BigDecimal.ZERO);
        ipi.setAliquotaUnidade(BigDecimal.ZERO);
        ipi.setModalidadeBaseCalculo("0");
        ipi.setPorcentoBaseCalculo(BigDecimal.ZERO);
        ipi.setValorPautaFiscal(BigDecimal.ZERO);
        ipi.setValorPrecoMaximo(BigDecimal.ZERO);

        getObjeto().setListaTributIcmsUf(new HashSet<>());
    }

    @Override
    public void doEdit() {

        TributConfiguraOfGt trib = dataModel.getRowData(getObjetoSelecionado().getId().toString());
        setObjeto(trib);
        setTelaGrid(false);

    }

    public void incluirTributIcmsUf() {
        tributIcmsUf = new TributIcmsUf();
        tributIcmsUf.setModalidadeBc("3");
        tributIcmsUf.setModalidadeBcSt("4");
        tributIcmsUf.setTributConfiguraOfGt(getObjeto());
    }

    public void alterarTributIcmsUf() {
        tributIcmsUf = tributIcmsUfSelecionado;
        tributIcmsUf.setModalidadeBc("3");
        tributIcmsUf.setModalidadeBcSt("4");
    }

    public void salvarTributIcmsUf() {
        if (tributIcmsUf.getId() == null) {
            getObjeto().getListaTributIcmsUf().add(tributIcmsUf);
        }
        salvar("Registro salvo com sucesso!");
    }

    public void excluirTributIcmsUf() {

        getObjeto().getListaTributIcmsUf().remove(tributIcmsUfSelecionado);
        salvar("Registro excluído com sucesso!");

    }

    public List<TributGrupoTributario> getListaTributGrupoTributario(String nome) {
        List<TributGrupoTributario> listaTributGrupoTributario = new ArrayList<>();
        try {
            listaTributGrupoTributario = grupos.getEntitys(TributGrupoTributario.class, "descricao", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTributGrupoTributario;
    }

    public List<TributOperacaoFiscal> getListaTributOperacaoFiscal(String nome) {
        List<TributOperacaoFiscal> listaTributOperacaoFiscal = new ArrayList<>();
        try {
            listaTributOperacaoFiscal = operacoes.getEntitys(TributOperacaoFiscal.class, "descricao", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTributOperacaoFiscal;
    }

    public List<TipoReceitaDipi> getListaTipoReceitaDipi(String nome) {
        List<TipoReceitaDipi> listaTipoReceitaDipi = new ArrayList<>();
        try {
            listaTipoReceitaDipi = ipis.getEntitys(TipoReceitaDipi.class, "descricao", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTipoReceitaDipi;
    }


    public HashMap getListaCstPis() {
        HashMap<String, String> cst = new LinkedHashMap<>();

        cst.put("01 - Operacao Tributavel com Aliquota Basica", "01");
        cst.put("02 - Operacao Tributavel com Aliquota Diferenciada", "02");
        cst.put("03 - Operacao Tributavel com Aliquota por Unidade de Medida de Produto", "03");
        cst.put("04 - Operacao Tributavel Monofasica - Revenda a Aliquota Zero", "04");
        cst.put("05 - Operacao Tributavel por Substituicao Tributaria", "05");
        cst.put("06 - Operacao Tributavel a Aliquota Zero", "06");
        cst.put("07 - Operacao Isenta da Contribuicao", "07");
        cst.put("08 - Operacao sem Incidencia da Contribuicao", "08");
        cst.put("09 - Operacao com Suspensao da Contribuicao", "09");
        cst.put("49 - Outras Operacões de Saida", "49");
        cst.put("50 - Operacao com Direito a Credito - Vinculada Exclusivamente a Receita Tributada no Mercado Interno", "50");
//        cst.put("51 - Operacao com Direito a Credito – Vinculada Exclusivamente a Receita Nao Tributada no Mercado Interno", "51");
//        cst.put("52 - Operacao com Direito a Credito - Vinculada Exclusivamente a Receita de Exportacao", "52");
//        cst.put("53 - Operacao com Direito a Credito - Vinculada a Receitas Tributadas e Nao-Tributadas no Mercado Interno", "53");
//        cst.put("54 - Operacao com Direito a Credito - Vinculada a Receitas Tributadas no Mercado Interno e de Exportacao", "54");
//        cst.put("55 - Operacao com Direito a Credito - Vinculada a Receitas Nao-Tributadas no Mercado Interno e de Exportacao", "55");
//        cst.put("56 - Operacao com Direito a Credito - Vinculada a Receitas Tributadas e Nao-Tributadas no Mercado Interno, e de Exportacao", "56");
//        cst.put("60 - Credito Presumido - Operacao de Aquisicao Vinculada Exclusivamente a Receita Tributada no Mercado Interno", "60");
//        cst.put("61 - Credito Presumido - Operacao de Aquisicao Vinculada Exclusivamente a Receita Nao-Tributada no Mercado Interno", "61");
//        cst.put("62 - Credito Presumido - Operacao de Aquisicao Vinculada Exclusivamente a Receita de Exportacao", "62");
//        cst.put("63 - Credito Presumido - Operacao de Aquisicao Vinculada a Receitas Tributadas e Nao-Tributadas no Mercado Interno", "63");
//        cst.put("64 - Credito Presumido - Operacao de Aquisicao Vinculada a Receitas Tributadas no Mercado Interno e de Exportacao", "64");
//        cst.put("65 - Credito Presumido - Operacao de Aquisicao Vinculada a Receitas Nao-Tributadas no Mercado Interno e de Exportacao", "65");
//        cst.put("66 - Credito Presumido - Operacao de Aquisicao Vinculada a Receitas Tributadas e Nao-Tributadas no Mercado Interno, e de Exportacao", "66");
//        cst.put("67 - Credito Presumido - Outras Operacões", "67");
//        cst.put("70 - Operacao de Aquisicao sem Direito a Credito", "70");
//        cst.put("71 - Operacao de Aquisicao com Isencao", "71");
//        cst.put("72 - Operacao de Aquisicao com Suspensao", "72");
//        cst.put("73 - Operacao de Aquisicao a Aliquota Zero", "73");
//        cst.put("74 - Operacao de Aquisicao sem Incidencia da Contribuicao", "74");
//        cst.put("75 - Operacao de Aquisicao por Substituicao Tributaria", "75");
//        cst.put("98 - Outras Operacões de Entrada", "98");
        cst.put("99 - Outras Operacões", "99");

        return cst;
    }

    public HashMap getListaCodApuracaoEfd() {

        HashMap<String, String> cod = new HashMap<>();
        cod.put("01 - Contribuição não-cumulativa apurada a alíquota básica", "01");
        cod.put("02 - Contribuição não-cumulativa apurada a alíquotas diferenciadas", "02");
        cod.put("03 - Contribuição não-cumulativa apurada a alíquota por unidade de medida de produto", "03");
        cod.put("04 - Contribuição não-cumulativa apurada a alíquota básica – Atividade Imobiliária", "04");
        cod.put("31 - Contribuição apurada por substituição tributária", "31");
        cod.put("32 - Contribuição apurada por substituição tributária – Vendas à Zona Franca de Manaus", "32");
        cod.put("51 - Contribuição cumulativa apurada a alíquota básica", "51");
        cod.put("52 - Contribuição cumulativa apurada a alíquotas diferenciadas", "52");
        cod.put("53 - Contribuição cumulativa apurada a alíquota por unidade de medida de produto", "53");
        cod.put("54 - Contribuição cumulativa apurada a alíquota básica – Atividade Imobiliária", "54");
        cod.put("70 - Contribuição apurada da Atividade Imobiliária - RET", "70");
        cod.put("71 - Contribuição apurada de SCP – Incidência Não Cumulativa", "71");
        cod.put("72 - Contribuição apurada de SCP – Incidência Cumulativa", "72");
        cod.put("99 - Contribuição para o PIS/Pasep – Folha de Salários", "99");

        return cod;
    }

    public HashMap getListaCstIpi() {
        HashMap<String, String> cst = new LinkedHashMap<>();
//        cst.put("00 - Entrada com Recuperação de Crédito", "00");
//        cst.put("01 - Entrada Tributável com Aliquota Zero", "01");
//        cst.put("02 - Entrada Isenta", "02");
//        cst.put("03 - Entrada Não-Tributada", "03");
//        cst.put("04 - Entrada Imune", "04");
//        cst.put("05 - Entrada com Suspensão", "05");
//        cst.put("49 - Outras Entradas", "49");
        cst.put("50 - Saída Tributada", "50");
        cst.put("51 - Saída Tributável com aliquota Zero", "51");
        cst.put("52 - Saida Isenta", "52");
        cst.put("53 - Saida Não-Tributada", "53");
        cst.put("54 - Saida Imune", "54");
        cst.put("55 - Saida com Suspensão", "55");
        cst.put("99 - Outras Saídas", "99");

        return cst;
    }

    public HashMap getListaMotivoDesoneracao() {
        HashMap<String, Integer> cst = new HashMap<>();

        cst.put("1 - Táxi", 1);
        cst.put("2 - Deficiente Físico", 2);
        cst.put("3 - Produtor Agropecuário", 3);
        cst.put("4 - Frotista/Locadora", 4);
        cst.put("5 - Diplomático/Consular", 5);
        cst.put("6 - Utilitários e Motocicletas da Amazônia Ocidental e Áreas de Livre Comércio (Resolução 714/88 e 790/94 - CONTRAN e suas alterações)", 6);
        cst.put("7 - Venda a Órgãos Públicos", 7);
        cst.put("8 - SUFRAMA", 8);
        cst.put("9 - Outros", 9);

        return cst;
    }


    @Override
    protected Class<TributConfiguraOfGt> getClazz() {
        return TributConfiguraOfGt.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "TRIBUT_CONFIGURA_OF_GT";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public TributIcmsUf getTributIcmsUf() {
        return tributIcmsUf;
    }

    public void setTributIcmsUf(TributIcmsUf tributIcmsUf) {
        this.tributIcmsUf = tributIcmsUf;
    }

    public TributIcmsUf getTributIcmsUfSelecionado() {
        return tributIcmsUfSelecionado;
    }

    public void setTributIcmsUfSelecionado(TributIcmsUf tributIcmsUfSelecionado) {
        this.tributIcmsUfSelecionado = tributIcmsUfSelecionado;
    }

    public boolean isSimplesNascional() {
        return simplesNascional = empresa.getCrt() != null && empresa.getCrt().equals("1");
    }
}
