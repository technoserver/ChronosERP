package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.*;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.Biblioteca;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.event.RowEditEvent;
import sun.plugin2.message.Message;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Created by john on 14/08/17.
 */
public class FinCobrancaControll extends AbstractControll<FinCobranca> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Repository<Cliente> clientes;
    private Repository<AdmParametro> parametros;
    private Repository<FinParcelaReceber> parcelas;

    private FinParcelaReceber finParcelaReceber;
    private FinParcelaReceber finParcelaReceberSelecionado;

    private FinCobrancaParcelaReceber finCobrancaParcelaReceber;
    private FinCobrancaParcelaReceber finCobrancaParcelaReceberSelecionado;

    private List<FinParcelaReceber> parcelasVencidas;


    @Override
    public void doCreate() {
        super.doCreate();
        Date dataContato = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        getObjeto().setDataContato(dataContato);
        getObjeto().setHoraContato(format.format(dataContato));
        getObjeto().setListaFinCobrancaParcelaReceber(new HashSet<>());

        parcelasVencidas = new ArrayList<>();
    }

    @Override
    public void voltar() {
        parcelasVencidas = new ArrayList<>();
        super.voltar();
    }

    public void buscaParcelaVencida() {
        try {
            List<Filtro> filtros = new ArrayList<>();

            filtros.add(new Filtro(Filtro.AND, "empresa", Filtro.IGUAL, empresa));
            AdmParametro admParametro = parametros.get(AdmParametro.class, filtros);
            if (admParametro == null) {
                throw new Exception("Parâmetros administrativos não cadastrados.\nEntre em contato com a Software House.");
            }

            filtros = new ArrayList<>();
            filtros.add(new Filtro(Filtro.AND, "finStatusParcela.id", Filtro.IGUAL, admParametro.getFinParcelaAberto()));

            Cliente cliente = getObjeto().getCliente();
            filtros.add(new Filtro(Filtro.AND, "finLancamentoReceber.cliente", Filtro.IGUAL, cliente));

            Date dataAtual = new Date();
            filtros.add(new Filtro(Filtro.AND, "dataVencimento", Filtro.MENOR, dataAtual));

            parcelasVencidas = parcelas .getEntitys(FinParcelaReceber.class, filtros);

            if (parcelasVencidas.isEmpty()) {
                Mensagem.addInfoMessage("Nenhuma parcela vencida para o cliente informado.!");

            }
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao salvar o registro!", e);


        }
    }

    public void alteraParcelaVencida(RowEditEvent event) {
        try {
            FinParcelaReceber parcela = (FinParcelaReceber) event.getObject();
            parcelas.atualizar(parcela);

            Mensagem.addInfoMessage("Dados salvos com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao salvar o registro!", e);

        }
    }

    public void alteraParcelaCobranca(RowEditEvent event) {
        try {
            FinCobrancaParcelaReceber parcela = (FinCobrancaParcelaReceber) event.getObject();
            getObjeto().getListaFinCobrancaParcelaReceber().remove(parcela);
            getObjeto().getListaFinCobrancaParcelaReceber().add(parcela);

            for (FinCobrancaParcelaReceber p : getObjeto().getListaFinCobrancaParcelaReceber()) {
                p.setValorReceberSimulado(p.getValorParcela().add(p.getValorJuroSimulado()).add(p.getValorMultaSimulado()));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao salvar o registro!", e);
        }
    }


    public void calcularJurosMulta() {
        try {
            if (parcelasVencidas == null || parcelasVencidas.isEmpty()) {
                Mensagem.addInfoMessage("Nenhuma parcela vencida!");

            } else {
                BigDecimal juros = BigDecimal.ZERO;
                BigDecimal multa = BigDecimal.ZERO;
                BigDecimal total = BigDecimal.ZERO;
                BigDecimal totalAtraso = BigDecimal.ZERO;

                BigDecimal valorJurosParcela;
                BigDecimal valorMultaParcela;

                getObjeto().setListaFinCobrancaParcelaReceber(new HashSet<>());

                for (FinParcelaReceber p : parcelasVencidas) {
                    p = (FinParcelaReceber) Biblioteca.nullToEmpty(p, false);

                    FinCobrancaParcelaReceber parcelaCobranca = new FinCobrancaParcelaReceber();
                    parcelaCobranca.setFinCobranca(getObjeto());
                    parcelaCobranca.setIdFinLancamentoReceber(p.getFinLancamentoReceber().getId());
                    parcelaCobranca.setIdFinParcelaReceber(p.getId());
                    parcelaCobranca.setDataVencimento(p.getDataVencimento());
                    parcelaCobranca.setValorParcela(p.getValor());

                    valorJurosParcela = p.getValor().multiply(p.getTaxaJuro().divide(BigDecimal.valueOf(100)));
                    valorMultaParcela = p.getValor().multiply(p.getTaxaMulta().divide(BigDecimal.valueOf(100)));

                    parcelaCobranca.setValorJuroSimulado(valorJurosParcela);
                    parcelaCobranca.setValorJuroAcordo(valorJurosParcela);
                    parcelaCobranca.setValorMultaSimulado(valorMultaParcela);
                    parcelaCobranca.setValorMultaAcordo(valorMultaParcela);
                    parcelaCobranca.setValorReceberSimulado(p.getValor().add(valorJurosParcela).add(valorMultaParcela));
                    parcelaCobranca.setValorReceberAcordo(parcelaCobranca.getValorReceberSimulado());

                    totalAtraso = totalAtraso.add(p.getValor());
                    total = total.add(parcelaCobranca.getValorReceberSimulado());
                    juros = juros.add(parcelaCobranca.getValorJuroSimulado());
                    multa = multa.add(parcelaCobranca.getValorMultaSimulado());

                    getObjeto().getListaFinCobrancaParcelaReceber().add(parcelaCobranca);
                }

                getObjeto().setTotalReceberNaData(total);
                getObjeto().setTotalJuros(juros);
                getObjeto().setTotalMulta(multa);
                getObjeto().setTotalAtrasado(totalAtraso);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao realizar os cálculos!", e);

        }
    }

    public void simulaValores() {
        try {
            BigDecimal juros = BigDecimal.ZERO;
            BigDecimal multa = BigDecimal.ZERO;
            BigDecimal total = BigDecimal.ZERO;

            for (FinCobrancaParcelaReceber p : getObjeto().getListaFinCobrancaParcelaReceber()) {
                p = (FinCobrancaParcelaReceber) Biblioteca.nullToEmpty(p, false);

                total = total.add(p.getValorReceberSimulado());
                juros = juros.add(p.getValorJuroSimulado());
                multa = multa.add(p.getValorMultaSimulado());
            }

            getObjeto().setTotalReceberNaData(total);
            getObjeto().setTotalJuros(juros);
            getObjeto().setTotalMulta(multa);
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao simular os valores!", e);

        }
    }

    public List<Cliente> getListaCliente(String nome) {
        List<Cliente> listaCliente = new ArrayList<>();
        try {
            listaCliente = clientes.getEntitys(Cliente.class, "pessoa.nome", nome,atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaCliente;
    }

    @Override
    protected Class<FinCobranca> getClazz() {
        return FinCobranca.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "FIN_COBRANCA";
    }

    public FinParcelaReceber getFinParcelaReceber() {
        return finParcelaReceber;
    }

    public void setFinParcelaReceber(FinParcelaReceber finParcelaReceber) {
        this.finParcelaReceber = finParcelaReceber;
    }

    public FinParcelaReceber getFinParcelaReceberSelecionado() {
        return finParcelaReceberSelecionado;
    }

    public void setFinParcelaReceberSelecionado(FinParcelaReceber finParcelaReceberSelecionado) {
        this.finParcelaReceberSelecionado = finParcelaReceberSelecionado;
    }

    public FinCobrancaParcelaReceber getFinCobrancaParcelaReceber() {
        return finCobrancaParcelaReceber;
    }

    public void setFinCobrancaParcelaReceber(FinCobrancaParcelaReceber finCobrancaParcelaReceber) {
        this.finCobrancaParcelaReceber = finCobrancaParcelaReceber;
    }

    public FinCobrancaParcelaReceber getFinCobrancaParcelaReceberSelecionado() {
        return finCobrancaParcelaReceberSelecionado;
    }

    public void setFinCobrancaParcelaReceberSelecionado(FinCobrancaParcelaReceber finCobrancaParcelaReceberSelecionado) {
        this.finCobrancaParcelaReceberSelecionado = finCobrancaParcelaReceberSelecionado;
    }

    public List<FinParcelaReceber> getParcelasVencidas() {
        return parcelasVencidas;
    }

    public void setParcelasVencidas(List<FinParcelaReceber> parcelasVencidas) {
        this.parcelasVencidas = parcelasVencidas;
    }
}