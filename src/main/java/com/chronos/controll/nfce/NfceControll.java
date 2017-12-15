package com.chronos.controll.nfce;

import com.chronos.controll.ERPLazyDataModel;
import com.chronos.dto.ConfiguracaoEmissorDTO;
import com.chronos.dto.ProdutoDTO;
import com.chronos.exception.EmissorException;
import com.chronos.infra.enuns.ModeloDocumento;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.entidades.enuns.NivelAutorizacaoCaixa;
import com.chronos.modelo.entidades.enuns.StatusMovimentoCaixa;
import com.chronos.modelo.entidades.enuns.StatusTransmissao;
import com.chronos.modelo.entidades.view.ViewNfceCliente;
import com.chronos.repository.EstoqueRepository;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.comercial.NfeService;
import com.chronos.util.ArquivoUtil;
import com.chronos.util.Biblioteca;
import com.chronos.util.FormatValor;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.jsf.Mensagem;
import net.sf.jasperreports.engine.*;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.nio.file.FileSystems.getDefault;

/**
 * Created by john on 04/10/17.
 */
@Named
@ViewScoped
public class NfceControll implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<NfceMovimento> movimentos;
    @Inject
    private Repository<NfceTurno> turnos;
    @Inject
    private Repository<NfceOperador> operadores;
    @Inject
    private Repository<NfceSuprimento> suprimentos;
    @Inject
    private Repository<NfceConfiguracao> configuracoes;
    @Inject
    private Repository<ViewNfceCliente> clientes;
    @Inject
    private Repository<Vendedor> vendedores;
    @Inject
    private Repository<NfceTipoPagamento> nfceTipoPagamentoRepository;
    @Inject
    private Repository<NfceFechamento> nfceFechamentoRepository;
    @Inject
    private EstoqueRepository estoqueRepositoy;
    @Inject
    private Repository<NfeCabecalho> nfeRepositoy;
    @Inject
    private Repository<TributOperacaoFiscal> operacaoFiscalRepository;

    @Inject
    protected FacesContext facesContext;
    @Inject
    private NfeService nfeService;

    private NfceMovimento movimento;
    private List<NfceTurno> listTurno;


    private NfeDetalhe item;
    private NfeDetalhe itemSelecionado;
    private NfceTipoPagamento tipoPagamento;
    private List<NfceTipoPagamento> listTipoPagamento;
    private NfeFormaPagamento formaPagamentoSelecionado;
    private NfceTurno turno;
    private NfceOperador gerente;
    private NfceOperador operador;
    private NfceConfiguracao configuracao;
    private NfeCabecalho venda;
    private NfeCabecalho vendaSelecionada;
    private Empresa empresa;
    private Usuario usuario;
    private ViewNfceCliente cliente;
    private Vendedor vendedor;
    private List<Vendedor> listVendedores;
    private TributOperacaoFiscal operacao;


    private String tipoDesconto;

    private boolean telaVenda;
    private boolean telaPagamentos;
    private boolean telaImpressao;
    private boolean telaCaixa;
    private boolean telaGrid;
    private boolean encontro;

    private BigDecimal valorSuprimento;
    private BigDecimal desconto;



    private BigDecimal totalVenda;
    private BigDecimal acrescimo;
    private BigDecimal totalReceber;
    private BigDecimal troco;
    private BigDecimal totalRecebido;
    private BigDecimal saldoRestante;
    private BigDecimal valorPago;

    private String userGerente;
    private String senhaGerente;
    private String userOperador;
    private String senhaOperador;
    private StringBuilder linhasRelatorio;
    private String nomeImpressaoMovimento;
    private ExternalContext context;
    private String nomeCupom;

    private String justificativa;

    private ERPLazyDataModel dataModel;


    @PostConstruct
    private void init() {

        // verificarMovimento();
        linhasRelatorio = new StringBuilder();
        empresa = FacesUtil.getEmpresaUsuario();
        usuario = FacesUtil.getUsuarioSessao();
        vendedor = instanciarVendedor(usuario);
        context = facesContext.getExternalContext();
        listVendedores = new ArrayList<>();
        operacao = operacaoFiscalRepository.get(1, TributOperacaoFiscal.class);
        novaVenda();

    }

    public ERPLazyDataModel<NfeCabecalho> getDataModel() {
        if (dataModel == null) {
            dataModel = new ERPLazyDataModel();
            dataModel.setDao(nfeRepositoy);
            dataModel.setClazz(NfeCabecalho.class);
        }
        dataModel.setAtributos(new Object[]{"serie", "numero", "dataHoraEmissao", "chaveAcesso", "digitoChaveAcesso", "valorTotal", "statusNota", "codigoModelo", "qrcode"});
        dataModel.getFiltros().clear();
        dataModel.addFiltro("tipoOperacao", 1, Filtro.IGUAL);
        dataModel.addFiltro("empresa.id", empresa.getId(), Filtro.IGUAL);
        dataModel.addFiltro("codigoModelo", "65", Filtro.IGUAL);
        return dataModel;
    }

    // <editor-fold defaultstate="collapsed" desc="Procedimentos Caixa">

    public void verificarMovimento() {
        try {
            movimento = movimentos.get(NfceMovimento.class, "statusMovimento", "A");
            if (movimento == null) {
                telaCaixa = true;
                telaVenda = false;
                listTurno = turnos.getAll(NfceTurno.class);
                turno = new NfceTurno();
                movimento = new NfceMovimento();

                valorSuprimento = BigDecimal.ZERO;

                movimento.setStatusMovimento(StatusMovimentoCaixa.ABERTO.getCodigo());
                movimento.setTotalAcrescimo(BigDecimal.ZERO);
                movimento.setTotalCancelado(BigDecimal.ZERO);
                movimento.setTotalDesconto(BigDecimal.ZERO);
                movimento.setTotalFinal(BigDecimal.ZERO);
                movimento.setTotalNaoFiscal(BigDecimal.ZERO);
                movimento.setTotalRecebido(BigDecimal.ZERO);
                movimento.setTotalSangria(BigDecimal.ZERO);
                movimento.setTotalTroco(BigDecimal.ZERO);
                movimento.setTotalVenda(BigDecimal.ZERO);
            } else {
                telaVenda = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void confimarMovimento() {

        try {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("login", userOperador));
            filtros.add(new Filtro("senha", senhaOperador));
            operador = operadores.get(NfceOperador.class, filtros, new Object[]{});
            if (operador == null) {
                throw new Exception("Usuario ou senha invalido para o Operador");
            }
            filtros.clear();
            filtros.add(new Filtro("login", userGerente));
            filtros.add(new Filtro("senha", senhaGerente));
            filtros.add(new Filtro(Filtro.OR, "nivelAutorizacao", Filtro.IGUAL, NivelAutorizacaoCaixa.SUPERVISOR.getCodigo()));
            filtros.add(new Filtro(Filtro.OR, "nivelAutorizacao", Filtro.IGUAL, NivelAutorizacaoCaixa.GERENTE.getCodigo()));

            gerente = operadores.get(NfceOperador.class, filtros, new Object[]{});

            if (gerente == null) {
                throw new Exception("Usuario ou senha invalido para o Gerente ou Supervisor");
            }
            movimento.setNfceTurno(turno);
            movimento.setEmpresa(FacesUtil.getEmpresaUsuario());
            movimento.setNfceOperador(operador);
            movimento.setNfceCaixa(getConfiguraNfce().getNfceCaixa());
            movimento.setIdGerenteSupervisor(gerente.getId());
            movimento.setDataAbertura(new Date());
            movimento.setHoraAbertura(FormatValor.getInstance().formatarHora(new Date()));
            movimento.setTotalSuprimento(valorSuprimento);

            movimento = movimentos.atualizar(movimento);
            if (valorSuprimento != null && valorSuprimento.signum() > 0) {
                addSuprimento(valorSuprimento);
            }
            movimento.calcularTotalFinal();
            movimentos.atualizar(movimento);
            imprimeAbertura();

            telaCaixa = false;
            telaVenda = true;


        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }
    }

    private void addSuprimento(BigDecimal valor) {
        NfceSuprimento suprimento = new NfceSuprimento();
        suprimento.setNfceMovimento(movimento);
        suprimento.setDataSuprimento(new Date());
        suprimento.setObservacao("Abertura do Caixa");
        suprimento.setValor(valor);

        suprimentos.salvar(suprimento);
    }


    private void imprimeAbertura() {
        try {

            linhasRelatorio.setLength(0);
            append(Biblioteca.repete("=", 48));
            append("               ABERTURA DE CAIXA ");
            append("");
            append("DATA DE ABERTURA: " + FormatValor.getInstance().formatarData(movimento.getDataAbertura()));
            append("HORA DE ABERTURA: " + movimento.getHoraAbertura());
            append(movimento.getNfceCaixa().getNome() + "  OPERADOR: " + movimento.getNfceOperador().getLogin());
            append("MOVIMENTO: " + movimento.getId());
            append(Biblioteca.repete("=", 48));
            append("");
            append("SUPRIMENTO...: " + FormatValor.getInstance().formatoDecimal("V", movimento.getTotalSuprimento().doubleValue()));
            append("");
            append("");
            append("");
            append(" ________________________________________ ");
            append("             VISTO DO CAIXA ");
            append("");
            append("");
            append("");
            append(" ________________________________________ ");
            append("           VISTO DO SUPERVISOR ");

            Map map = new HashMap();
            File fileTemp = new File(context.getRealPath("/") + System.getProperty("file.separator") + "temp");
            if (!fileTemp.exists()) {
                fileTemp.mkdir();
            }
            map.put("CONTEUDO", linhasRelatorio.toString());
            String caminhoJasper = "/com/chronos/erplight/relatorios/comercial/nfce/relatorioMovimento.jasper";
            InputStream inputStream = this.getClass().getResourceAsStream(caminhoJasper);
            JasperPrint jp = JasperFillManager.fillReport(inputStream, map, new JREmptyDataSource());
            byte[] pdfFile = JasperExportManager.exportReportToPdf(jp);
            Path localPdf = getDefault().getPath(fileTemp.getPath());
            nomeImpressaoMovimento = "movimento" + movimento.getId() + ".pdf";
            ArquivoUtil.getInstance().escrever(pdfFile, nomeImpressaoMovimento, localPdf.toString());
            RequestContext.getCurrentInstance().addCallbackParam("movimentoIniciado", true);

        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao imprimir o relatório de abertura de movimento.\n.", ex);

        }
    }

    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Procedimentos Venda">

    public void novaVenda() {
        try {
            telaVenda = true;
            telaImpressao = false;
            telaCaixa = false;
            telaPagamentos = false;
            if (venda != null && venda.getId() != null) {
                nomeCupom = "cupom" + venda.getNumero() + ".pdf";
                String caminho = context.getRealPath("/") + System.getProperty("file.separator") + "temp";
                File fileTemp = new File(context.getRealPath("/") + System.getProperty("file.separator") + "temp");
                Files.deleteIfExists(getDefault().getPath(fileTemp.getPath(), nomeCupom));

            }
            venda = new NfeCabecalho();
            venda.setTributOperacaoFiscal(operacao);
            item = new NfeDetalhe();
            configuracao = getConfiguraNfce();
            nfeService.dadosPadroes(venda, ModeloDocumento.NFCE, empresa, new ConfiguracaoEmissorDTO(configuracao));
            desconto = BigDecimal.ZERO;

            File fileTemp = new File(context.getRealPath("/") + System.getProperty("file.separator") + "temp");
            if (!fileTemp.exists()) {
                fileTemp.mkdir();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public void cancelarVenda() {
        novaVenda();
        Mensagem.addInfoMessage("Venda cancelada");
    }


    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Procedimentos Pagamentos ">
    public void iniciarPagamentosVenda() {
        telaVenda = true;
        telaImpressao = false;
        telaCaixa = false;
        telaPagamentos = false;

        if (venda.getListaNfeDetalhe().isEmpty()) {
            Mensagem.addInfoMessage("Não foram informados itens nessa venda");
        } else {
            telaVenda = false;
            telaImpressao = false;
            telaCaixa = false;
            telaPagamentos = true;
            listTipoPagamento = nfceTipoPagamentoRepository.getAll(NfceTipoPagamento.class);
        }

        totalVenda = BigDecimal.ZERO;
        desconto = BigDecimal.ZERO;
        acrescimo = BigDecimal.ZERO;
        totalReceber = BigDecimal.ZERO;
        troco = BigDecimal.ZERO;
        totalRecebido = BigDecimal.ZERO;
        saldoRestante = BigDecimal.ZERO;

        //guarda valores para calculo
        totalVenda = venda.getValorTotalProdutos().add(venda.getValorServicos());
        desconto = venda.getValorDesconto();
        acrescimo = venda.getValorDespesasAcessorias();
        totalReceber = Biblioteca.soma(totalVenda, acrescimo);
        totalReceber = Biblioteca.subtrai(totalReceber, desconto);
        saldoRestante = totalReceber;

        valorPago = saldoRestante;
        venda.getListaNfeFormaPagamento().clear();
    }

    public void lancaPagamento() {
        try {
            if (saldoRestante.compareTo(BigDecimal.ZERO) <= 0) {
                Mensagem.addInfoMessage("Todos os valores já foram recebidos. Finalize a venda.");
            } else {
                if (tipoPagamento.getGeraParcelas().equals("S")) {

                } else {

                    incluiPagamento(tipoPagamento, valorPago);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }


    }

    private void incluiPagamento(NfceTipoPagamento tipoPagamento, BigDecimal valor) throws Exception {
        Optional<NfeFormaPagamento> formaPagamentoOpt = bucarTipoPagamento(tipoPagamento);
        if (formaPagamentoOpt.isPresent()) {
            Mensagem.addInfoMessage("Forma de pagamento " + tipoPagamento.getDescricao() + " já incluso");
        } else {
            NfeFormaPagamento formaPagamento = new NfeFormaPagamento();
            formaPagamento.setNfeCabecalho(venda);
            formaPagamento.setNfceTipoPagamento(tipoPagamento);
            formaPagamento.setValor(valor);
            formaPagamento.setForma(tipoPagamento.getCodigo());
            formaPagamento.setEstorno("N");

            venda.getListaNfeFormaPagamento().add(formaPagamento);


            totalRecebido = Biblioteca.soma(totalRecebido, valor);
            troco = Biblioteca.subtrai(totalRecebido, totalReceber);
            if (troco.compareTo(BigDecimal.ZERO) == -1) {
                troco = BigDecimal.ZERO;
            }
            verificaSaldoRestante();
        }

    }

    private Optional<NfeFormaPagamento> bucarTipoPagamento(NfceTipoPagamento tipoPagamento) {
        Optional<NfeFormaPagamento> formaPagamentoOpt = venda.getListaNfeFormaPagamento()
                .stream()
                .filter(fp -> fp.getNfceTipoPagamento().equals(tipoPagamento))
                .findAny();
        return formaPagamentoOpt;
    }


    public void excluirPagamento() {
        if (formaPagamentoSelecionado != null) {
            venda.getListaNfeFormaPagamento().remove(formaPagamentoSelecionado);
            verificaSaldoRestante();
        }
        Mensagem.addInfoMessage("Forma de pagamento removida");
    }

    public void finalizarVenda() {
        verificaSaldoRestante();
        if (saldoRestante.compareTo(BigDecimal.ZERO) <= 0) {
            venda.setTroco(troco);
            transmitirNfe();
        } else {
            Mensagem.addInfoMessage("Valores informados não são suficientes para finalizar a venda.");
        }
    }

    public void lancaMovimentos() {

        venda.getListaNfeFormaPagamento().stream()
                .forEach(p -> {
                    NfceFechamento fechamento = new NfceFechamento();
                    fechamento.setNfceMovimento(movimento);
                    fechamento.setTipoPagamento(p.getNfceTipoPagamento().getDescricao());
                    fechamento.setValor(p.getValor());
                    nfceFechamentoRepository.salvar(fechamento);

                });
        movimento.setTotalVenda(Biblioteca.soma(movimento.getTotalVenda(), totalReceber));
        movimento.calcularTotalFinal();
        movimentos.atualizar(movimento);
    }

    private void verificaSaldoRestante() {
        BigDecimal recebidoAteAgora = BigDecimal.ZERO;
        Set<NfeFormaPagamento> listaPagamento = venda.getListaNfeFormaPagamento();
        for (NfeFormaPagamento p : listaPagamento) {
            recebidoAteAgora = Biblioteca.soma(recebidoAteAgora, p.getValor());
        }

        saldoRestante = Biblioteca.subtrai(totalReceber, recebidoAteAgora);
        totalRecebido = recebidoAteAgora;
        valorPago = saldoRestante;
        if (valorPago.compareTo(BigDecimal.ZERO) == -1) {
            valorPago = BigDecimal.ZERO;
        }
        if (saldoRestante.compareTo(BigDecimal.ZERO) == -1) {
            saldoRestante = BigDecimal.ZERO;
        }
    }

    public void cancelarPagamento() {
        telaVenda = true;
        telaImpressao = false;
        telaCaixa = false;
        telaPagamentos = false;
    }

    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Procedimentos Venda item ">

    public void selecionarProduto(SelectEvent event) {
        Produto produto = (Produto) event.getObject();
        novoItem();
        item.setProduto(produto);
        item.setValorUnitarioComercial(Optional.ofNullable(produto.getValorVenda()).orElse(BigDecimal.ZERO));
//        item.setListaArmamento(new HashSet<>());
//        item.setListaDeclaracaoImportacao(new HashSet<>());
//        item.setListaMedicamento(new HashSet<>());

    }


    public void novoItem() {
        desconto = BigDecimal.ZERO;
        item = new NfeDetalhe();
        item.setNfeCabecalho(venda);
        item.setQuantidadeComercial(BigDecimal.ONE);
    }

    public void salvaProduto() {
        try {
            if (!validarItem()) {
                return;
            }
            realizaCalculosItem();

            Optional<NfeDetalhe> itemNfeOptional = buscarItemPorProduto(item.getProduto());
            NfeDetalhe it = null;
            if (itemNfeOptional.isPresent()) {
                it = itemNfeOptional.get();
                it = item;
            } else {
                venda.getListaNfeDetalhe().add(0, item);
            }
            venda = nfeService.atualizarTotais(venda);
            novoItem();
            Mensagem.addInfoMessage("Registro incluído!");

        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao salvar o produto\n", e);
        }

    }

    private Optional<NfeDetalhe> buscarItemPorProduto(Produto produto) {
        return venda.getListaNfeDetalhe().stream()
                .filter(i -> i.getProduto().equals(produto))
                .findAny();
    }

    public void excluirItem() {

        try {
            Produto produto = itemSelecionado.getProduto();
            int indice = IntStream.range(0, venda.getListaNfeDetalhe().size())
                    .filter(i -> venda.getListaNfeDetalhe().get(i).getProduto().equals(produto))
                    .findAny().getAsInt();
            venda.getListaNfeDetalhe().remove(indice);
            venda = nfeService.atualizarTotais(venda);
            Mensagem.addInfoMessage("Produto exluso com sucesso");
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }
    }

    /**
     * setar os valores do item que está sendo salvo
     *
     * @return
     * @throws Exception
     */
    private NfeDetalhe realizaCalculosItem() throws Exception {

        if (item.getProduto().getNcm() == null) {
            throw new Exception("Não existe NCM para o produto informado. Operação não realizada.");
        }

        BigDecimal valorVenda = item.getValorUnitarioComercial();
        valorVenda = valorVenda == null ? item.getProduto().getValorVenda() : valorVenda;
        item.setValorUnitarioComercial(valorVenda);
        item.setEntraTotal(1);
        item.pegarInfoProduto();
        item.calcularValorTotalProduto();
        item = nfeService.definirTributacao(item, venda.getTributOperacaoFiscal(), venda.getDestinatario());

        return item;
    }

    public boolean validarItem() {
        String msg = "";
        boolean valido = true;
        if (item == null) {
            msg = "Venda de produto não iniciada";
            valido = false;
        } else if (item.getProduto() == null) {
            msg = "Produto não definido";
            valido = false;
        } else if (item.getProduto().getValorVenda().doubleValue() <= 0) {
            msg = "Produto não pode ser vendido com valor zerado ou negativo.";
            valido = false;
        } else if (StringUtils.isEmpty(item.getProduto().getUnidadeProduto().getPodeFracionar())) {
            msg = "A unidade de medida possue valores não informado : 'Pode Fracionar'.";
            valido = false;
        } else if (item.getProduto().getUnidadeProduto().getPodeFracionar().equals("N") && (item.getQuantidadeComercial().doubleValue() % 1) != 0) {
            msg = "Este produto não pode ser vendido em quantidade fracionada.";
            valido = false;
        }
        if (!StringUtils.isEmpty(msg)) {
            Mensagem.addInfoMessage(msg);
        }

        return valido;
    }


    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Procedimentos NFce">

    public NfceConfiguracao getConfiguraNfce() throws Exception {
        List<Filtro> filtros = new LinkedList<>();
        filtros.add(new Filtro(Filtro.AND, "empresa.id", Filtro.IGUAL, empresa.getId()));
        configuracao = configuracao == null ? configuracoes.get(NfceConfiguracao.class, filtros) : configuracao;
        return configuracao;
    }

    private void definirNumeroItens() {

        int i = 0;
        for (NfeDetalhe item : venda.getListaNfeDetalhe()) {
            item.setNumeroItem(++i);
        }

    }

    @Transactional
    public void transmitirNfe() {
        try {


            configuracao = getConfiguraNfce();
            definirNumeroItens();
            venda.setCsc(configuracao.getCodigoCsc());
            boolean estoque = FacesUtil.isUserInRole("ESTOQUE");
            StatusTransmissao status = nfeService.transmitirNFe(venda, new ConfiguracaoEmissorDTO(configuracao), estoque);
            if (status == StatusTransmissao.AUTORIZADA) {
                // lancaMovimentos();
                gerarCupom();
                Mensagem.addInfoMessage("NFe transmitida com sucesso");
                RequestContext.getCurrentInstance().addCallbackParam("vendaFinalizada", true);
            } else if (status == StatusTransmissao.DUPLICIDADE) {

                // venda.setNumero(null);
                // transmitirNfe();
            }


        } catch (EmissorException ex) {
            if (ex.getMessage().contains("Read timed out")) {
                try {
                    venda.setStatusNota(StatusTransmissao.ENVIADA.getCodigo());
                    nfeRepositoy.atualizar(venda);
                } catch (Exception ex1) {

                }
            }
            ex.printStackTrace();
            Mensagem.addErrorMessage("Erro ao transmitir\n", ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("Erro ao transmitir\n", ex);
        }
    }

    public void cancelarNfce() {

        try {
            venda = nfeRepositoy.getJoinFetch(vendaSelecionada.getId(), NfeCabecalho.class);
            venda.setJustificativaCancelamento(justificativa);
            configuracao = getConfiguraNfce();
            boolean estoque = FacesUtil.isUserInRole("ESTOQUE");
            boolean cancelado = nfeService.cancelarNFe(venda, new ConfiguracaoEmissorDTO(configuracao), estoque);
            if (cancelado) {
                Mensagem.addInfoMessage("NFCe cancelada com sucesso");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao cancelar a NFC-e!\n", e);
        }

    }




    public void gerarCupom() throws JRException, IOException {

        try {
            configuracao = getConfiguraNfce();
            nfeService.gerarDanfe(venda, new ConfiguracaoEmissorDTO(configuracao));
            nomeCupom = "cupom" + venda.getNumero() + ".pdf";
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }
    }

    public void danfe() {


        try {
            configuracao = getConfiguraNfce();
            nfeService.danfe(vendaSelecionada, new ConfiguracaoEmissorDTO(configuracao));

        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }
    }


    //</editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Procedimentos Cliente">


    public void indentificarCliente() {
        cliente = new ViewNfceCliente();
    }

    public void vincularCliente() {
        localizarCliente();
        if (cliente.getId() != null) {
            Cliente c = new Cliente();
            c.setId(cliente.getId());
            venda.setCliente(c);

            preencheDadosDestinatario();
        } else {
            venda.setCliente(null);
            preencheDadosDestinatario();
        }
    }

    private void localizarCliente() {
        String nome = cliente.getNome();
        String email = cliente.getEmail();
        String cpf = cliente.getCpf();
        cliente = clientes.get(ViewNfceCliente.class, "cpf", cpf);
        if (cliente == null) {
            cliente = new ViewNfceCliente();
            cliente.setCpf(cpf);
            cliente.setNome(nome);
            cliente.setEmail(email);
        }
    }

    private void preencheDadosDestinatario() {
        NfeDestinatario destinatario = new NfeDestinatario();
        destinatario.setNfeCabecalho(venda);
        destinatario.setNome(cliente.getNome());
        destinatario.setCpfCnpj(cliente.getCpf());

        venda.setDestinatario(destinatario);
    }

    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Procedimentos Vendedor">

    public void buscarVendedores() {
        listVendedores = listVendedores.isEmpty() ? vendedores.getEntitys(Vendedor.class) : listVendedores;
    }

    private Vendedor instanciarVendedor(Usuario usuario) {
        Vendedor vendedor = vendedores.get(Vendedor.class, "colaborador.id", usuario.getColaborador().getId());

        if (vendedor == null) {
            vendedor = new Vendedor();
            vendedor.setGerente('N');
            vendedor.setComissao(BigDecimal.ZERO);
            vendedor.setMetaVendas(BigDecimal.ZERO);
            vendedor.setComissao(BigDecimal.ZERO);
            vendedor.setColaborador(usuario.getColaborador());
            vendedor = vendedores.atualizar(vendedor);
        }

        return vendedor;
    }

    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Procedimentos Pesquisas">

    public List<Produto> getListaProduto(String descricao) {
        List<Produto> listaProduto = new ArrayList<>();

        try {
            List<ProdutoDTO> list = nfeService.getListaProdutoDTO(descricao);
            listaProduto = list.stream().map(ProdutoDTO::getProduto).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProduto;
    }


    //</editor-fold>


    public String formatarValor(BigDecimal valor) {
        String valorFormatado = FormatValor.getInstance().formatoDecimal("V", valor.doubleValue());
        return valorFormatado;
    }

    public boolean podeConsultar() {
        // return false;
        boolean teste = FacesUtil.isUserInRole("NFCE_CONSULTA")
                || FacesUtil.isUserInRole("ADMIN");
        return teste;
    }

    public boolean isPodeLancaPagamento() {
        return valorPago.signum() == 0;
    }

    public boolean isPodeFinalzarVenda() {
        return saldoRestante.signum() == 0;
    }

    private void append(String texto) {
        linhasRelatorio.append(texto + "\n");
    }

    public boolean isTelaVenda() {
        return telaVenda;
    }

    public void setTelaVenda(boolean telaVenda) {
        this.telaVenda = telaVenda;
    }

    public boolean isTelaPagamentos() {
        return telaPagamentos;
    }

    public void setTelaPagamentos(boolean telaPagamentos) {
        this.telaPagamentos = telaPagamentos;
    }

    public boolean isTelaImpressao() {
        return telaImpressao;
    }

    public void setTelaImpressao(boolean telaImpressao) {
        this.telaImpressao = telaImpressao;
    }

    public boolean isTelaCaixa() {
        return telaCaixa;
    }

    public void setTelaCaixa(boolean telaCaixa) {
        this.telaCaixa = telaCaixa;
    }

    public List<NfceTurno> getListTurno() {
        return listTurno;
    }

    public void setListTurno(List<NfceTurno> listTurno) {
        this.listTurno = listTurno;
    }

    public NfceTurno getTurno() {
        return turno;
    }

    public void setTurno(NfceTurno turno) {
        this.turno = turno;
    }

    public BigDecimal getValorSuprimento() {
        return valorSuprimento;
    }

    public void setValorSuprimento(BigDecimal valorSuprimento) {
        this.valorSuprimento = valorSuprimento;
    }

    public String getUserGerente() {
        return userGerente;
    }

    public void setUserGerente(String userGerente) {
        this.userGerente = userGerente;
    }

    public String getSenhaGerente() {
        return senhaGerente;
    }

    public void setSenhaGerente(String senhaGerente) {
        this.senhaGerente = senhaGerente;
    }

    public String getUserOperador() {
        return userOperador;
    }

    public void setUserOperador(String userOperador) {
        this.userOperador = userOperador;
    }

    public String getSenhaOperador() {
        return senhaOperador;
    }

    public void setSenhaOperador(String senhaOperador) {
        this.senhaOperador = senhaOperador;
    }

    public String getNomeImpressaoMovimento() {
        return nomeImpressaoMovimento;
    }

    public ViewNfceCliente getCliente() {
        return cliente;
    }

    public void setCliente(ViewNfceCliente cliente) {
        this.cliente = cliente;
    }

    public List<Vendedor> getListVendedores() {
        return listVendedores;
    }

    public void setListVendedores(List<Vendedor> listVendedores) {
        this.listVendedores = listVendedores;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public NfeCabecalho getVenda() {
        return venda;
    }

    public void setVenda(NfeCabecalho venda) {
        this.venda = venda;
    }

    public NfeDetalhe getItem() {
        return item;
    }

    public void setItem(NfeDetalhe item) {
        this.item = item;
    }

    public String getTipoDesconto() {
        return tipoDesconto;
    }

    public void setTipoDesconto(String tipoDesconto) {
        this.tipoDesconto = tipoDesconto;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public boolean isTelaGrid() {
        return telaGrid;
    }

    public void setTelaGrid(boolean telaGrid) {
        this.telaGrid = telaGrid;
    }



    public List<NfceTipoPagamento> getListTipoPagamento() {
        return listTipoPagamento;
    }

    public void setListTipoPagamento(List<NfceTipoPagamento> listTipoPagamento) {
        this.listTipoPagamento = listTipoPagamento;
    }

    public NfceTipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(NfceTipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }


    public BigDecimal getTotalVenda() {
        return totalVenda;
    }

    public BigDecimal getAcrescimo() {
        return acrescimo;
    }

    public BigDecimal getTotalReceber() {
        return totalReceber;
    }

    public BigDecimal getTroco() {
        return troco;
    }

    public BigDecimal getTotalRecebido() {
        return totalRecebido;
    }


    public BigDecimal getSaldoRestante() {
        return saldoRestante;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public NfeFormaPagamento getFormaPagamentoSelecionado() {
        return formaPagamentoSelecionado;
    }

    public void setFormaPagamentoSelecionado(NfeFormaPagamento formaPagamentoSelecionado) {
        this.formaPagamentoSelecionado = formaPagamentoSelecionado;
    }

    public String getNomeCupom() {
        return nomeCupom;
    }

    public void setNomeCupom(String nomeCupom) {
        this.nomeCupom = nomeCupom;
    }

    public NfeCabecalho getVendaSelecionada() {
        return vendaSelecionada;
    }

    public void setVendaSelecionada(NfeCabecalho vendaSelecionada) {
        this.vendaSelecionada = vendaSelecionada;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }
}
