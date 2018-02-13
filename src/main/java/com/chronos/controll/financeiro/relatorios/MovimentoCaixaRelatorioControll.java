package com.chronos.controll.financeiro.relatorios;

import com.chronos.controll.AbstractRelatorioControll;
import com.chronos.dto.MapDTO;
import com.chronos.modelo.entidades.PdvMovimento;
import com.chronos.modelo.entidades.view.ViewMovimentoCaixa;
import com.chronos.repository.Repository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
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
        cartaoDebito= BigDecimal.ZERO;
        cheque= BigDecimal.ZERO;
        cartaoCredito= BigDecimal.ZERO;
        ticket= BigDecimal.ZERO;
        credito= BigDecimal.ZERO;
        duplicata= BigDecimal.ZERO;

        for(ViewMovimentoCaixa v : movimentos){

            switch (v.getCodigoFormaPagamento()){

                case "01" :
                    dinheiro = dinheiro.add(v.getValor());
                    break;
                case "02" :
                    cheque = cheque.add(v.getValor());
                    break;
                case "03" :
                    cartaoCredito = cartaoCredito.add(v.getValor());
                    break;
                case "04" :
                    cartaoDebito = cartaoDebito.add(v.getValor());
                    break;
                case "05" :
                    ticket = ticket.add(v.getValor());
                    break;
                case "06" :
                    duplicata = duplicata.add(v.getValor());
                    break;
                case "07" :
                    credito = credito.add(v.getValor());
                    break;
            }
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
}
