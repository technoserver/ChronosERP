package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.Cliente;
import com.chronos.modelo.entidades.FinCobranca;
import com.chronos.modelo.entidades.FinCobrancaParcelaReceber;
import com.chronos.modelo.entidades.FinParcelaReceber;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.Biblioteca;
import com.chronos.util.Constantes;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.event.RowEditEvent;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
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
@Named
@ViewScoped
public class FinCobrancaControll extends AbstractControll<FinCobranca> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<Cliente> clientes;
    @Inject
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
    public void salvar() {

        if(getObjeto().getListaFinCobrancaParcelaReceber().isEmpty()){
            Mensagem.addWarnMessage("Não foram definidas parcelas para cobrança");
        }else{
            super.salvar();
        }
    }

    @Override
    public void voltar() {
        parcelasVencidas = new ArrayList<>();
        super.voltar();
    }



    public void buscaParcelaVencida() {
        try {
            if(getObjeto().getCliente()==null){
                Mensagem.addWarnMessage("Cliente não informado!!!");
                return;
            }
            List<Filtro> filtros = new ArrayList<>();

            filtros.add(new Filtro(Filtro.AND, "empresa", Filtro.IGUAL, empresa));


            filtros = new ArrayList<>();
            filtros.add(new Filtro(Filtro.AND, "finStatusParcela.id", Filtro.IGUAL, Constantes.FIN.STATUS_ABERTO.getId()));

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
                Mensagem.addWarnMessage("Nenhuma parcela vencida!");

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
            if(getObjeto().getListaFinCobrancaParcelaReceber()==null || getObjeto().getListaFinCobrancaParcelaReceber().isEmpty()){
                Mensagem.addWarnMessage("Nenhuma parcela vencida!");
                return;
            }
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
            listaCliente = clientes.getEntitys(Cliente.class, "pessoa.nome", nome);
        } catch (Exception e) {
             e.printStackTrace();
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
