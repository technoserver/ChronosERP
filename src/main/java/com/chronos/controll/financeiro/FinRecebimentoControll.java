package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.dto.FatorGeradorDTO;
import com.chronos.dto.ReciboPagamentoDTO;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.AcaoLog;
import com.chronos.modelo.enuns.Modulo;
import com.chronos.modelo.view.PessoaCliente;
import com.chronos.modelo.view.ViewFinLancamentoReceber;
import com.chronos.repository.Filtro;
import com.chronos.repository.ParcelaReceberRepository;
import com.chronos.repository.Repository;
import com.chronos.repository.VendaRepository;
import com.chronos.service.ChronosException;
import com.chronos.service.financeiro.MovimentoService;
import com.chronos.util.Constants;
import com.chronos.util.FormatValor;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
    @Inject
    private Repository<PessoaCliente> pessoaClienteRepository;
    @Inject
    private Repository<FinChequeRecebido> chequesRecebidos;
    @Inject
    private VendaRepository vendaRepository;
    @Inject
    private Repository<VendaDetalhe> vendaDetalheRepository;

    @Inject
    private Repository<PdvVendaCabecalho> pdvVendaCabecalhoRepository;

    @Inject
    private MovimentoService movimentoService;

    private Cliente cliente;

    private List<ViewFinLancamentoReceber> parcelas;
    private List<ViewFinLancamentoReceber> parcelasSelecionadas;
    private FinTipoRecebimento tipoRecebimento;
    private FinChequeRecebido finChequeRecebido;

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
    private ReciboPagamentoDTO recibo;

    private BigDecimal valorAPagar;

    private FatorGeradorDTO fatorGerador;


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

    public void finalizaRecebimentoCheque() throws ChronosException {

        finChequeRecebido.setContaCaixa(tipoRecebimento.getContaCaixa());
        chequesRecebidos.salvar(finChequeRecebido);
        finalizarRecebimento(valorAPagar);
    }

    public void baixaParcelas() {


        try {
            if (parcelasSelecionadas == null || parcelasSelecionadas.isEmpty()) {
                Mensagem.addInfoMessage("É preciso selecionar ao menos 1 parcela");
            } else {
                Collections.sort(parcelasSelecionadas);


                if (tipoRecebimento.getTipo().equals("02")) {
                    temParcelaVencida = parcelasSelecionadas.stream().filter(p -> p.isVencido()).findAny().isPresent();
                    if (temParcelaVencida) {
                        Mensagem.addInfoMessage("Não é possivel realizar pagamento de parcela vencidas com cheque");
                    } else {
                        finChequeRecebido = new FinChequeRecebido();
                        PrimeFaces.current().executeScript("PF('dialogFinChequeRecebido').show()");
                        //RequestContext.getCurrentInstance().update("formOutrasTelas:dialogFinChequeRecebido");
                    }
                } else {
                    finalizarRecebimento(valorAPagar);
                }


            }
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage(ex.getMessage());
            } else {
                throw new RuntimeException("Erro ao baixa as parcelas", ex);
            }

        }
    }

    public void calcularValorAPagar() {
        valorAPagar = parcelasSelecionadas.stream().map(p -> p.getValorAPagar()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    public void localizarFatorGerador(String numDoc) {

        if (fatorGerador != null && numDoc.equals(fatorGerador.getDoc())) {
            return;
        }

        Integer id;

        String idempresa = "E" + empresa.getId();
        String str = numDoc.substring(6 + 1, numDoc.lastIndexOf("C"));
        if (numDoc.contains(idempresa + "M" + Modulo.VENDA.getCodigo())) {


            boolean isNumber = str.chars().allMatch(Character::isDigit);
            if (isNumber) {
                id = Integer.parseInt(str);
                fatorGerador = vendaRepository.getVendaToParcela(id);
                fatorGerador.setDoc(numDoc);
                fatorGerador.setItens(new ArrayList<>());
                List<VendaDetalhe> itens = vendaDetalheRepository.getEntitys(VendaDetalhe.class, "vendaCabecalho.id", id);

                itens.forEach(i -> {
                    fatorGerador.addItem(i.getProduto().getNome(), i.getQuantidade(), i.getValorTotal());
                });


            }
        } else if (numDoc.contains("M" + Modulo.PDV.getCodigo())) {
            boolean isNumber = str.chars().allMatch(Character::isDigit);
            if (isNumber) {
                id = Integer.parseInt(str);
                PdvVendaCabecalho venda = pdvVendaCabecalhoRepository.get(id, PdvVendaCabecalho.class);

                if (venda != null) {
                    fatorGerador = new FatorGeradorDTO(venda.getId(), venda.getDataHoraVenda(),
                            venda.getValorTotal(), venda.getVendedor().getColaborador().getPessoa().getNome(), "", null);
                    fatorGerador.setDoc(numDoc);
                    fatorGerador.setItens(new ArrayList<>());


                    venda.getListaPdvVendaDetalhe().forEach(i -> {
                        fatorGerador.addItem(i.getProduto().getNome(), i.getQuantidade(), i.getValorTotal());
                    });
                }


            }
        } else if (numDoc.contains("M" + Modulo.OS.getCodigo())) {

        }
    }

    private void finalizarRecebimento(BigDecimal valorAPagar) throws ChronosException {

        String documentos = "";
        int idcliente = 0;
        List<Integer> ids = new ArrayList<>();
        BigDecimal saldo = valorAPagar;


        PdvMovimento movimento = movimentoService.verificarMovimento(empresa);

        for (ViewFinLancamentoReceber p : parcelasSelecionadas) {

            if (saldo.signum() > 0) {
                if (p.getValorAPagar().compareTo(saldo) <= 0) {
                    fazerLancamento(p, p.getValorAPagar(), false, movimento);
                    saldo = saldo.subtract(p.getValorAPagar());
                } else {
                    fazerLancamento(p, saldo, true, movimento);
                    saldo = BigDecimal.ZERO;
                }

                ids.add(p.getId());
                idcliente = p.getIdCliente();
                documentos += " " + p.getNumeroDocumento() + "/" + p.getNumeroParcela();

            } else {
                break;
            }
        }


        if (movimento != null) {

            movimentoService.lancaRecebimento(valorAPagar);

        }


        gerarLog(AcaoLog.BAIXA_PARCELA, "Recebimento da(s) parcela(s) de Nº " + documentos, "Tela Recebimento");
        recibo = new ReciboPagamentoDTO();
        recibo.setIdcliente(idcliente);
        recibo.setIdtipoRecebimento(tipoRecebimento.getId());
        recibo.setValorPago(valorAPagar);
        recibo.setIdsrecebimento(ids);

        buscarParcelas();
        PrimeFaces.current().executeScript("PF('recibo').show()");
    }

    @Transactional
    private void fazerLancamento(ViewFinLancamentoReceber p, BigDecimal valorPagar, boolean parcial, PdvMovimento movimento) {
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
        recebimento.setPdvMovimento(movimento);


        FinStatusParcela status = parcial ? Constants.FIN.STATUS_PARCIAL : Constants.FIN.STATUS_QUITADO;
        FinParcelaReceber pr = new FinParcelaReceber(p.getIdParcelaReceber());
        recebimento.setFinParcelaReceber(pr);

        finParcelaRecebimentoRepository.salvar(recebimento);
        parcelaReceberRepository.atualizarStatusParcela(recebimento.getFinParcelaReceber().getId(), status.getId());
    }

    public List<Cliente> getListaCliente(String nome) {
        List<Cliente> listaCliente = new ArrayList<>();
        try {

            List<Filtro> filtros = new ArrayList<>();

            filtros.add(new Filtro("pessoa.nome", Filtro.LIKE, nome));

            int id = org.apache.commons.lang3.StringUtils.isNumeric(nome) ? Integer.parseInt(nome) : 0;

            filtros.add(new Filtro(Filtro.OR, "id", Filtro.IGUAL, id));

            listaCliente = clientes.getEntitys(Cliente.class, filtros, new Object[]{"pessoa.id", "pessoa.nome"});
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaCliente;
    }

    public List<Pessoa> getListaPessoa(String nome) {
        List<Pessoa> listaPessoa = new ArrayList<>();
        try {
            List<PessoaCliente> listaCliente = pessoaClienteRepository.getEntitys(PessoaCliente.class, "nome", nome, new Object[]{"idPessoa", "nome", "cpfCnpj", "tipo"});
            listaPessoa.addAll(listaCliente.stream().map(PessoaCliente::getPessoa).collect(Collectors.toList()));
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaPessoa;
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

    public void onSelecionaPessoa(SelectEvent event) {
        finChequeRecebido.setNome(finChequeRecebido.getPessoa().getNome());
        if (finChequeRecebido.getPessoa().getPessoaFisica() != null) {
            finChequeRecebido.setCpfCnpj(finChequeRecebido.getPessoa().getPessoaFisica().getCpf());
        }
        if (finChequeRecebido.getPessoa().getPessoaJuridica() != null) {
            finChequeRecebido.setCpfCnpj(finChequeRecebido.getPessoa().getPessoaJuridica().getCnpj());
        }
    }


    public String formatarValor(BigDecimal valor) {
        return FormatValor.getInstance().formatoDecimal("V", valor.doubleValue());
    }

    @Override
    protected Class<FinParcelaReceber> getClazz() {
        return FinParcelaReceber.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "RECEBIMENTO";
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

    public ReciboPagamentoDTO getRecibo() {
        return recibo;
    }

    public void setRecibo(ReciboPagamentoDTO recibo) {
        this.recibo = recibo;
    }


    public FinChequeRecebido getFinChequeRecebido() {
        return finChequeRecebido;
    }

    public void setFinChequeRecebido(FinChequeRecebido finChequeRecebido) {
        this.finChequeRecebido = finChequeRecebido;
    }

    public FatorGeradorDTO getFatorGerador() {
        return fatorGerador;
    }
}
