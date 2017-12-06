package com.chronos.controll.nfce;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.NfceFechamento;
import com.chronos.modelo.entidades.NfceMovimento;
import com.chronos.modelo.entidades.NfceSangria;
import com.chronos.modelo.entidades.NfceSuprimento;
import com.chronos.modelo.entidades.enuns.StatusMovimentoCaixa;
import com.chronos.repository.Repository;
import com.chronos.util.Biblioteca;
import com.chronos.util.FormatValor;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by john on 04/10/17.
 */
@Named
@ViewScoped
public class NfceMovimentoControll extends AbstractControll<NfceMovimento> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<NfceSangria> sangriaRepository;
    @Inject
    private Repository<NfceSuprimento> suprimentoRepository;
    @Inject
    private Repository<NfceFechamento> fechamentoRepository;
    @Inject
    protected FacesContext facesContext;

    private List<NfceFechamento> fechamentos;
    private BigDecimal valor;
    private String observacao;
    private StringBuilder linhasRelatorio;
    private String nomeImpressaoMovimento;
    private ExternalContext context;
    private Map<String, BigDecimal> detalhe;

    @PostConstruct
    @Override
    public void init() {
        super.init();
        context = facesContext.getExternalContext();
        nomeImpressaoMovimento = "";
        File fileTemp = new File(context.getRealPath("/") + System.getProperty("file.separator") + "temp");

        if (!fileTemp.exists()) {
            fileTemp.mkdir();
        }
    }

    @Override
    public ERPLazyDataModel<NfceMovimento> getDataModel() {

        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setDao(dao);
            dataModel.setClazz(NfceMovimento.class);
        }
        dataModel.addFiltro("statusMovimento", StatusMovimentoCaixa.ABERTO.getCodigo());

        return dataModel;
    }

    @Override
    public void doEdit() {
        super.doEdit();
        fechamentos = fechamentoRepository.getEntitys(NfceFechamento.class, "nfceMovimento.id", getObjeto().getId());
        BigDecimal valor = BigDecimal.ZERO;
        String pagamento = "";
        String pagamentoAux = "";
        detalhe = new LinkedHashMap<>();
        Collections.sort(fechamentos);
        int i = 0;
        for (NfceFechamento fechamento : fechamentos) {
            i++;
            pagamento = fechamento.getTipoPagamento();

            if (pagamentoAux.equals("")) {
                pagamentoAux = pagamento;
            }
            if (pagamento.equals(pagamentoAux)) {
                valor = valor.add(fechamento.getValor());
                if (i == fechamentos.size()) {
                    detalhe.put(pagamento, valor);
                }
            } else {
                detalhe.put(pagamentoAux, valor);
                valor = fechamento.getValor();
                pagamentoAux = pagamento;
                if (i == fechamentos.size()) {
                    detalhe.put(pagamento, valor);
                }
            }

        }
    }

    public void encenrarMovimento() {


        try {
            linhasRelatorio = new StringBuilder();
            setObjeto(getObjetoSelecionado());
            getObjeto().setDataFechamento(new Date());
            getObjeto().setHoraFechamento(FormatValor.getInstance().formatarHora(new Date()));
            getObjeto().setStatusMovimento("F");
            getObjeto().calcularTotalFinal();
            fechamentos = fechamentoRepository.getEntitys(NfceFechamento.class, "nfceMovimento.id", getObjeto().getId());
            imprimirFechamento();
            Mensagem.addInfoMessage("Movimento encerrado com sucesso");
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }
    }

    public void gerarImpressao() {

        try {
            linhasRelatorio = new StringBuilder();
            setObjeto(getObjetoSelecionado());
            fechamentos = fechamentoRepository.getEntitys(NfceFechamento.class, "nfceMovimento.id", getObjeto().getId());
            imprimirFechamento();
            Mensagem.addInfoMessage("Impressão gerada com sucesso");
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }
    }

    public void novoLancamento() {
        observacao = "";
        valor = BigDecimal.ZERO;
        setObjeto(getObjetoSelecionado());
    }

    public void lancarSuprimentos() {
        try {
            NfceSuprimento suprimento = new NfceSuprimento();
            suprimento.setNfceMovimento(getObjeto());
            suprimento.setDataSuprimento(new Date());
            suprimento.setObservacao(observacao.trim());
            suprimento.setValor(valor);
            suprimentoRepository.salvar(suprimento);

            getObjetoSelecionado().setTotalSuprimento(Biblioteca.soma(getObjeto().getTotalSuprimento(), suprimento.getValor()));
            dao.atualizar(getObjetoSelecionado());


            Mensagem.addInfoMessage("Suprimento inserido com sucesso.");

        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }
    }

    public void lancarSangria() {
        try {
            NfceSangria sangria = new NfceSangria();
            sangria.setNfceMovimento(getObjetoSelecionado());
            sangria.setDataSangria(new Date());
            sangria.setValor(valor);
            sangria.setObservacao(observacao.trim());
            sangriaRepository.salvar(sangria);

            getObjetoSelecionado().setTotalSangria(Biblioteca.soma(getObjetoSelecionado().getTotalSangria(), sangria.getValor()));
            dao.atualizar(getObjetoSelecionado());
            Mensagem.addInfoMessage("Sangria realizada com sucesso.");

        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }
    }

    private void imprimirFechamento() {
        try {
//            String declarado, meio;
//            BigDecimal valorDeclarado, totDeclarado;
//            String suprimento, sangria, naoFiscal, totalVenda, desconto, acrescimo, recebido, troco, cancelado, totalFinal;
//
//            linhasRelatorio.setLength(0);
//
//            append(Biblioteca.repete("=", 48));
//            append("             FECHAMENTO DE CAIXA                ");
//            append(Biblioteca.repete("=", 48));
//            append("");
//            append("DATA DE ABERTURA  : " + FormatValor.getInstance().formatarData(getObjeto().getDataAbertura()));
//            append("HORA DE ABERTURA  : " + getObjeto().getHoraAbertura());
//            append("DATA DE FECHAMENTO: " + FormatValor.getInstance().formatarData(getObjeto().getDataFechamento()));
//            append("HORA DE FECHAMENTO: " + getObjeto().getHoraFechamento());
//            append(getObjeto().getNfceCaixa().getNome() + "  OPERADOR: " + getObjeto().getNfceOperador().getLogin());
//            append("MOVIMENTO: " + getObjeto().getId());
//            append(Biblioteca.repete("=", 48));
//            append("");
//
//            suprimento = FormatValor.getInstance().formatoDecimal("V", getObjeto().getTotalSuprimento().doubleValue());
//            suprimento = Biblioteca.repete("", 33 - suprimento.length()) + suprimento;
//            sangria = FormatValor.getInstance().formatoDecimal("V", getObjeto().getTotalSangria().doubleValue());
//            sangria = Biblioteca.repete("", 33 - sangria.length()) + sangria;
//            naoFiscal = FormatValor.getInstance().formatoDecimal("V", getObjeto().getTotalNaoFiscal().doubleValue());
//            naoFiscal = Biblioteca.repete("", 33 - naoFiscal.length()) + naoFiscal;
//            totalVenda = FormatValor.getInstance().formatoDecimal("V", getObjeto().getTotalVenda().doubleValue());
//            totalVenda = Biblioteca.repete("", 33 - totalVenda.length()) + totalVenda;
//            desconto = FormatValor.getInstance().formatoDecimal("V", getObjeto().getTotalDesconto().doubleValue());
//            desconto = Biblioteca.repete("", 33 - desconto.length()) + desconto;
//            acrescimo = FormatValor.getInstance().formatoDecimal("V", getObjeto().getTotalAcrescimo().doubleValue());
//            acrescimo = Biblioteca.repete("", 33 - acrescimo.length()) + acrescimo;
//            recebido = FormatValor.getInstance().formatoDecimal("V", getObjeto().getTotalRecebido().doubleValue());
//            recebido = Biblioteca.repete("", 33 - recebido.length()) + recebido;
//            troco = FormatValor.getInstance().formatoDecimal("V", getObjeto().getTotalTroco().doubleValue());
//            troco = Biblioteca.repete("", 33 - troco.length()) + troco;
//            cancelado = FormatValor.getInstance().formatoDecimal("V", getObjeto().getTotalCancelado().doubleValue());
//            cancelado = Biblioteca.repete("", 33 - cancelado.length()) + cancelado;
//            totalFinal = FormatValor.getInstance().formatoDecimal("V", getObjeto().getTotalFinal().doubleValue());
//            totalFinal = Biblioteca.repete("", 33 - totalFinal.length()) + totalFinal;
//
//            append("SUPRIMENTO...: " + suprimento);
//            append("SANGRIA......: " + sangria);
//            append("NAO FISCAL...: " + naoFiscal);
//            append("TOTAL VENDA..: " + totalVenda);
//            append("DESCONTO.....: " + desconto);
//            append("ACRESCIMO....: " + acrescimo);
//            append("RECEBIDO.....: " + recebido);
//            append("TROCO........: " + troco);
//            append("CANCELADO....: " + cancelado);
//            append("TOTAL FINAL..: " + totalFinal);
//            append("");
//            append("");
//            append("");
//            append(Biblioteca.repete("=", 48));
//            append("       VALORES DECLARADOS PARA FECHAMENTO");
//            append(Biblioteca.repete("=", 48));
//
//            totDeclarado = BigDecimal.ZERO;
//
//            for (NfceFechamento f : fechamentos) {
//
//                valorDeclarado = f.getValor();
//                declarado = FormatValor.getInstance().formatoDecimal("V", valorDeclarado.doubleValue());
//                declarado = Biblioteca.repete(" ", 28 - declarado.length()) + declarado;
//
//                meio = f.getTipoPagamento();
//                meio = Biblioteca.repete(" ", 20 - meio.length()) + meio;
//
//                totDeclarado = Biblioteca.soma(totDeclarado, valorDeclarado);
//
//                append(meio + declarado);
//            }
//
//            append(Biblioteca.repete("-", 48));
//
//            declarado = FormatValor.getInstance().formatoDecimal("V", totDeclarado.doubleValue());
//            declarado = Biblioteca.repete("", 11 - declarado.length()) + declarado;
//
//            append("TOTAL.........:" + declarado);
//            append("");
//            append("");
//            append("");
//            append("");
//            append("    ________________________________________    ");
//            append("                 VISTO DO CAIXA                 ");
//            append("");
//            append("");
//            append("");
//            append("    ________________________________________    ");
//            append("               VISTO DO SUPERVISOR              ");
//
//            Map map = new HashMap();
//            map.put("CONTEUDO", linhasRelatorio.toString());
//            File fileTemp = new File(context.getRealPath("/") + System.getProperty("file.separator") + "temp");
//            if (!fileTemp.exists()) {
//                fileTemp.mkdir();
//            }
//
//
//            String caminhoJasper = "/com/chronos/erplight/relatorios/comercial/nfce/relatorioMovimento.jasper";
//            InputStream inputStream = this.getClass().getResourceAsStream(caminhoJasper);
//            JasperPrint jp = JasperFillManager.fillReport(inputStream, map, new JREmptyDataSource());
//            byte[] pdfFile = JasperExportManager.exportReportToPdf(jp);
//            Path localPdf = getDefault().getPath(fileTemp.getPath());
            nomeImpressaoMovimento = "movimento" + getObjeto().getId() + ".pdf";
//            ArquivoUtil.getInstance().escrever(pdfFile, nomeImpressaoMovimento, localPdf.toString());
            RequestContext.getCurrentInstance().addCallbackParam("movimentoIniciado", true);

        } catch (Exception ex) {
            Mensagem.addErrorMessage("Ocorreu um erro ao imprimir o relatório de encerramento de movimento.\n." + ex);

        }

    }

    private void append(String texto) {
        linhasRelatorio.append(texto + "\n");
    }

    @Override
    protected Class<NfceMovimento> getClazz() {
        return NfceMovimento.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "NFCE_MOVIMENTO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }


    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getNomeImpressaoMovimento() {
        return nomeImpressaoMovimento;
    }

    public void setNomeImpressaoMovimento(String nomeImpressaoMovimento) {
        this.nomeImpressaoMovimento = nomeImpressaoMovimento;
    }
}
