package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.entidades.view.ViewFinLancamentoReceber;
import com.chronos.repository.Filtro;
import com.chronos.repository.ParcelaReceberRepository;
import com.chronos.repository.Repository;
import com.chronos.util.Constantes;
import com.chronos.util.FormatValor;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by john on 11/10/17.
 */
@Named
@ViewScoped
public class FinRecebimentoControll extends AbstractControll<FinParcelaReceber> implements Serializable {


    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<Cliente> clientes;
    @Inject
    private Repository<FinTipoRecebimento> tipoRecebimentoRepository;
    @Inject
    private Repository<ViewFinLancamentoReceber> parcelaRepository;
    @Inject
    private ParcelaReceberRepository parcelaReceberRepository;
    @Inject
    private Repository<FinParcelaRecebimento> finParcelaRecebimentoRepository;


    private Cliente cliente;

    private List<ViewFinLancamentoReceber> parcelas;
    private List<ViewFinLancamentoReceber> parcelasSelecionadas;
    private FinTipoRecebimento tipoRecebimento;

    private int qtdParcelasVencida;
    private int qtdParcelasAvencer;
    private BigDecimal multa;
    private BigDecimal juros;
    private BigDecimal totalVencidas;
    private BigDecimal totalAVencer;
    private BigDecimal totalReceber;
    private BigDecimal saldoDevedor;
    private String observacao;
    private boolean temParcelaVencida;

    private BigDecimal valorAPagar;

    public void buscarParcelas() {
        List<Filtro> filtros = new ArrayList<>();
        filtros.add(new Filtro("idStatusParcela", Filtro.DIFERENTE, 2));
        filtros.add(new Filtro("idCliente", Filtro.IGUAL, cliente.getId()));
        parcelas = parcelaRepository.getEntitys(ViewFinLancamentoReceber.class, filtros);
        multa = BigDecimal.ZERO;
        juros = BigDecimal.ZERO;
        totalVencidas = BigDecimal.ZERO;
        totalAVencer = BigDecimal.ZERO;
        qtdParcelasVencida = 0;
        qtdParcelasAvencer = 0;
        parcelas.stream().forEach(p -> {

          //  p.calcularValorJuros();
          //  p.calcularValorTotal();
          //  multa = multa.add(p.getValorMulta());
          // juros = juros.add(p.getValorJuro());

            if (p.isVencido()) {
                temParcelaVencida = true;
                totalVencidas = totalVencidas.add(p.getValorAPagar());
                qtdParcelasVencida++;
              //  parcelaReceberRepository.atualizarJuros(p.getIdParcelaReceber(), p.getValorJuro());
                if (p.getIdStatusParcela() != 4) {
                    parcelaReceberRepository.atualizarStatusParcela(p.getIdParcelaReceber(), 4);
                    p.setDescricaoSituacaoParcela("Vencido");
                }
            } else {
                totalAVencer = totalAVencer.add(p.getValorAPagar());
                qtdParcelasAvencer++;
            }


        });


    }

    public void baixaParcelas() {
        if (parcelasSelecionadas == null || parcelasSelecionadas.isEmpty()) {
            Mensagem.addInfoMessage("É preciso seleionar ao menos 1 parcela");
        } else {
            Collections.sort(parcelasSelecionadas);

            BigDecimal saldo = valorAPagar;
            if (tipoRecebimento.getTipo().equals("02")) {
                if (temParcelaVencida) {
                    Mensagem.addInfoMessage("Não é possivel realizar pagamento de parcela vencidas com cheque");
                } else {

                }
            } else {
                for (ViewFinLancamentoReceber p : parcelasSelecionadas) {
                    if (saldo.signum() > 0) {
                        if (p.getValorAPagar().compareTo(saldo) <= 0) {
                            fazerLancamento(p, p.getValorAPagar(), false);
                            saldo = saldo.subtract(p.getValorAPagar());
                        } else {
                            fazerLancamento(p, saldo, true);
                            saldo = BigDecimal.ZERO;
                        }
                    } else {
                        break;
                    }
                }

                buscarParcelas();
            }



        }
    }

    @Transactional
    private void fazerLancamento(ViewFinLancamentoReceber p, BigDecimal valorPagar, boolean parcial) {
        FinParcelaRecebimento recebimento = new FinParcelaRecebimento();
        recebimento.setFinTipoRecebimento(tipoRecebimento);
        BigDecimal valorJuros = BigDecimal.ZERO;

        if (valorPagar.compareTo(p.getSaldo()) > 0) {
        //    valorJuros = valorPagar.subtract(p.getValorJuro());
        }

        recebimento.setContaCaixa(tipoRecebimento.getContaCaixa());
        recebimento.setDataRecebimento(new Date());
        recebimento.setHistorico(observacao);
        recebimento.setTaxaDesconto(p.getTaxaDesconto());
        recebimento.setTaxaJuro(p.getTaxaJuro());
        recebimento.setTaxaMulta(p.getTaxaMulta());
        recebimento.setValorDesconto(p.getValorDesconto());
        recebimento.setValorJuro(valorJuros);
        recebimento.setValorMulta(p.getValorMulta());
        recebimento.setValorRecebido(valorPagar);

        FinStatusParcela status = parcial ? Constantes.FIN.STATUS_PARCIAL : Constantes.FIN.STATUS_QUITADO;
        FinParcelaReceber pr = new FinParcelaReceber(p.getIdParcelaReceber());
        recebimento.setFinParcelaReceber(pr);

        finParcelaRecebimentoRepository.salvar(recebimento);
        parcelaReceberRepository.atualizarStatusParcela(recebimento.getFinParcelaReceber().getId(), status.getId());
    }

    public List<Cliente> getListaCliente(String nome) {
        List<Cliente> listaCliente = new ArrayList<>();
        try {

            listaCliente = clientes.getEntitys(Cliente.class, "pessoa.nome", nome, new Object[]{"pessoa.nome"});
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaCliente;
    }

    public List<FinTipoRecebimento> getListaFinTipoRecebimento(String nome) {
        List<FinTipoRecebimento> listaFinTipoRecebimento = new ArrayList<>();
        try {
            listaFinTipoRecebimento = tipoRecebimentoRepository.getEntitys(FinTipoRecebimento.class, "descricao", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaFinTipoRecebimento;
    }


    public String formatarValor(BigDecimal valor) {
        return FormatValor.getInstance().formatoDecimal("V", valor.doubleValue());
    }

    @Override
    protected Class<FinParcelaReceber> getClazz() {
        return null;
    }

    @Override
    protected String getFuncaoBase() {
        return null;
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ViewFinLancamentoReceber> getParcelas() {
        return parcelas;
    }

    public void setParcelas(List<ViewFinLancamentoReceber> parcelas) {
        this.parcelas = parcelas;
    }

    public int getQtdParcelasVencida() {
        return qtdParcelasVencida;
    }

    public void setQtdParcelasVencida(int qtdParcelasVencida) {
        this.qtdParcelasVencida = qtdParcelasVencida;
    }

    public int getQtdParcelasAvencer() {
        return qtdParcelasAvencer;
    }

    public void setQtdParcelasAvencer(int qtdParcelasAvencer) {
        this.qtdParcelasAvencer = qtdParcelasAvencer;
    }

    public BigDecimal getMulta() {
        return Optional.ofNullable(multa).orElse(BigDecimal.ZERO);
    }

    public void setMulta(BigDecimal multa) {
        this.multa = multa;
    }

    public BigDecimal getJuros() {
        return Optional.ofNullable(juros).orElse(BigDecimal.ZERO);
    }

    public void setJuros(BigDecimal juros) {
        this.juros = juros;
    }

    public BigDecimal getTotalVencidas() {
        return Optional.ofNullable(totalVencidas).orElse(BigDecimal.ZERO);
    }

    public void setTotalVencidas(BigDecimal totalVencidas) {
        this.totalVencidas = totalVencidas;
    }

    public BigDecimal getTotalAVencer() {
        return Optional.ofNullable(totalAVencer).orElse(BigDecimal.ZERO);
    }

    public void setTotalAVencer(BigDecimal totalAVencer) {
        this.totalAVencer = totalAVencer;
    }

    public List<ViewFinLancamentoReceber> getParcelasSelecionadas() {
        return Optional.ofNullable(parcelasSelecionadas).orElse(new ArrayList<>());
    }

    public void setParcelasSelecionadas(List<ViewFinLancamentoReceber> parcelasSelecionadas) {
        this.parcelasSelecionadas = parcelasSelecionadas;
    }

    public BigDecimal getValorAPagar() {

        return valorAPagar;
    }

    public void setValorAPagar(BigDecimal valorAPagar) {
        this.valorAPagar = valorAPagar;
    }

    public FinTipoRecebimento getTipoRecebimento() {
        return tipoRecebimento;
    }

    public void setTipoRecebimento(FinTipoRecebimento tipoRecebimento) {
        this.tipoRecebimento = tipoRecebimento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public BigDecimal getTotalReceber() {
        totalReceber = getJuros().add(getMulta());
        return totalReceber;
    }

    public BigDecimal getSaldoDevedor() {
        saldoDevedor = getTotalVencidas().add(getTotalAVencer());
        return saldoDevedor;
    }
}
