package com.chronos.controll.financeiro.relatorios;

import com.chronos.controll.AbstractRelatorioControll;
import com.chronos.dto.MapDTO;
import com.chronos.dto.PdvMovimentoDTO;
import com.chronos.modelo.entidades.PdvMovimento;
import com.chronos.modelo.view.ViewMovimentoCaixa;
import com.chronos.repository.Repository;
import com.chronos.util.jsf.Mensagem;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Named
@RequestScoped
public class MovimentoCaixaRelatorioControll extends AbstractRelatorioControll implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<ViewMovimentoCaixa> repository;
    @Inject
    private Repository<PdvMovimento> movimentoRepository;
    private List<ViewMovimentoCaixa> movimentos;
    private List<PdvMovimentoDTO> vendas;
    private List<PdvMovimentoDTO> sangrias;
    private List<PdvMovimentoDTO> suprimentos;
    private PdvMovimento movimento;

    private int idmovimento;
    private List<MapDTO> pagamentos;

    private BigDecimal dinheiro;
    private BigDecimal cartaoDebito;
    private BigDecimal cheque;
    private BigDecimal cartaoCredito;
    private BigDecimal ticket;
    private BigDecimal credito;
    private BigDecimal duplicata;


    public void buscarMovimentos() {
        movimentos = repository.getEntitys(ViewMovimentoCaixa.class, "idmovimento", idmovimento);
        movimento = movimentoRepository.get(idmovimento, PdvMovimento.class);
        dinheiro = BigDecimal.ZERO;
        cartaoDebito = BigDecimal.ZERO;
        cheque = BigDecimal.ZERO;
        cartaoCredito = BigDecimal.ZERO;
        ticket = BigDecimal.ZERO;
        credito = BigDecimal.ZERO;
        duplicata = BigDecimal.ZERO;

        for (ViewMovimentoCaixa v : movimentos) {

            switch (v.getCodigoFormaPagamento()) {

                case "01":
                    dinheiro = dinheiro.add(v.getValor());
                    break;
                case "02":
                    cheque = cheque.add(v.getValor());
                    break;
                case "03":
                    cartaoCredito = cartaoCredito.add(v.getValor());
                    break;
                case "04":
                    cartaoDebito = cartaoDebito.add(v.getValor());
                    break;
                case "05":
                    ticket = ticket.add(v.getValor());
                    break;
                case "06":
                    duplicata = duplicata.add(v.getValor());
                    break;
                case "07":
                    credito = credito.add(v.getValor());
                    break;
            }
        }
    }


    public void relacaoVendaPorMovimento() {


        try {
            Date dataInicial = new SimpleDateFormat("yyyy-MM-dd").parse("0001-01-01");
            Date dataFinal = new Date();
            List<PdvMovimentoDTO> movimentos = movimentoRepository.getEntitysNamedQuery(PdvMovimentoDTO.class, "Pdv.movimento", idmovimento, dataInicial, dataFinal);
            vendas = new ArrayList<>();
            suprimentos = new ArrayList<>();
            sangrias = new ArrayList<>();
            for (PdvMovimentoDTO mov : movimentos) {
                if (mov.getOrigem().equals("VENDA")) {
                    vendas.add(mov);

                }

                if (mov.getOrigem().equals("Suprimentos")) {
                    suprimentos.add(mov);
                }

                if (mov.getOrigem().equals("Sangria")) {
                    sangrias.add(mov);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void imprimirMovimento(int id) {


        try {
            Date dataInicial = new SimpleDateFormat("yyyy-MM-dd").parse("0001-01-01");
            Date dataFinal = new Date();
            parametros = new HashMap<>();
            parametros.put("idmovimento", id);

            String caminhoRelatorio = "/relatorios/financeiro";
            String nomeRelatorio = "movimentoDetalhado.jasper";

            executarRelatorio(caminhoRelatorio, nomeRelatorio, "movimento.pdf");
        } catch (Exception ex) {
            Mensagem.addErrorMessage("", ex);
        }
    }


    public int getIdmovimento() {
        return idmovimento;
    }

    public void setIdmovimento(int idmovimento) {
        this.idmovimento = idmovimento;
    }

    public PdvMovimento getMovimento() {
        return movimento;
    }

    public BigDecimal getDinheiro() {
        return dinheiro;
    }

    public BigDecimal getCartaoDebito() {
        return cartaoDebito;
    }

    public BigDecimal getCheque() {
        return cheque;
    }

    public BigDecimal getCartaoCredito() {
        return cartaoCredito;
    }

    public BigDecimal getTicket() {
        return ticket;
    }

    public BigDecimal getCredito() {
        return credito;
    }

    public BigDecimal getDuplicata() {
        return duplicata;
    }

    public List<PdvMovimentoDTO> getVendas() {
        return vendas;
    }

    public List<PdvMovimentoDTO> getSangrias() {
        return sangrias;
    }

    public List<PdvMovimentoDTO> getSuprimentos() {
        return suprimentos;
    }
}
