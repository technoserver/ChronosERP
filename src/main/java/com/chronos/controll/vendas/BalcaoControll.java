package com.chronos.controll.vendas;

import com.chronos.controll.ERPLazyDataModel;
import com.chronos.dto.ConfiguracaoEmissorDTO;
import com.chronos.dto.ProdutoDTO;
import com.chronos.infra.enuns.ModeloDocumento;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.entidades.enuns.SituacaoVenda;
import com.chronos.repository.Repository;
import com.chronos.service.cadastros.UsuarioService;
import com.chronos.service.comercial.NfeService;
import com.chronos.service.comercial.VendaPdvService;
import com.chronos.service.comercial.VendaService;
import com.chronos.service.financeiro.FinLancamentoReceberService;
import com.chronos.util.Biblioteca;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Created by john on 19/09/17.
 */
@Named
@ViewScoped
public class BalcaoControll implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(BalcaoControll.class);
    @Inject
    protected FacesContext facesContext;
    @Inject
    private Repository<PdvVendaCabecalho> vendas;
    @Inject
    private Repository<EmpresaProduto> produtos;
    @Inject
    private Repository<Vendedor> vendedores;
    @Inject
    private Repository<Cliente> clientes;
    @Inject
    private Repository<VendaComissao> comissoes;
    @Inject
    private Repository<VendaCondicoesPagamento> condicoes;
    @Inject
    private Repository<NfeCabecalho> nfeRepository;
    @Inject
    private Repository<TributOperacaoFiscal> operacaoRepository;
    @Inject
    private Repository<PdvTipoPagamento> tipoPagamentoRepository;
    @Inject
    private UsuarioService userService;
    @Inject
    private FinLancamentoReceberService recebimentoService;
    @Inject
    private NfeService nfeService;
    @Inject
    private VendaPdvService service;
    @Inject
    private VendaService vendaService;
    private ERPLazyDataModel<PdvVendaCabecalho> dataModel;

    private PdvVendaCabecalho venda;
    private PdvVendaDetalhe item;
    private PdvTipoPagamento tipoPagamento;
    private List<PdvTipoPagamento> listTipoPagamento;
    private PdvFormaPagamento formaPagamentoSelecionado;
    private Empresa empresa;
    private Usuario usuario;
    private Vendedor vendedor;
    private Cliente cliente;
    private ProdutoDTO produto;
    private List<Vendedor> listVendedores;
    private VendaCondicoesPagamento condicaoPagamento;
    private List<VendaCondicoesPagamento> condicoesPagamentos;
    private BigDecimal desconto;


    private String tipoDesconto;

    private boolean telaVenda;
    private boolean telaPagamentos;
    private boolean telaImpressao;
    private boolean telaCaixa;
    private boolean exibirCondicoes;
    private boolean telaGrid = true;
    private BigDecimal totalVenda;
    private BigDecimal acrescimo;
    private BigDecimal totalReceber;
    private BigDecimal troco;
    private BigDecimal totalRecebido;
    private BigDecimal saldoRestante;
    private BigDecimal valorPago;
    private int id;




    @PostConstruct
    private void init() {

        empresa = FacesUtil.getEmpresaUsuario();
        usuario = FacesUtil.getUsuarioSessao();

    }


    public ERPLazyDataModel<PdvVendaCabecalho> getDataModel(){
        if(dataModel == null){
            dataModel = new ERPLazyDataModel<>();
            dataModel.setDao(vendas);
            dataModel.setClazz(PdvVendaCabecalho.class);
        }
        return dataModel;
    }

    public void novaVenda() {
        telaGrid = false;
        telaVenda = true;
        telaPagamentos = false;
        venda = new PdvVendaCabecalho();
        venda.setEmpresa(empresa);
        venda.setListaPdvVendaDetalhe(new ArrayList<>());
        venda.setDataHoraVenda(new Date());
        venda.setStatusVenda(SituacaoVenda.Digitacao.getCodigo());
        vendedor = instanciarVendedor(usuario);
        venda.setVendedor(vendedor);
        item = new PdvVendaDetalhe();
        desconto = BigDecimal.ZERO;
        telaPagamentos = false;
        telaVenda = true;
        telaImpressao = false;
        exibirCondicoes = false;
    }



    public String verificarMovimento(){
        try{
            PdvMovimento movimento = FacesUtil.getMovimento();
            if(movimento == null ){
                return "/modulo/comercial/caixa/movimentos.xhtml";
            }else{
                return "";
            }
        }catch (Exception ex){
            ex.printStackTrace();
            throw new RuntimeException("Não foi possivel fazer o redirecionamento para o abertura de caixa");
        }

    }


    public void cancelar() {
        telaVenda = true;
        telaPagamentos = false;
        novaVenda();
    }

    public void gerarNfce() {


        try {
            ModeloDocumento modelo = ModeloDocumento.NFCE;
            if (cliente.getId() == 1) {
                TributOperacaoFiscal operacao = operacaoRepository.get(1, TributOperacaoFiscal.class);
                venda.getCliente().setTributOperacaoFiscal(operacao);
            }
            boolean estoque = FacesUtil.isUserInRole("ESTOQUE");
          //  vendaService.transmitirNFe(venda, modelo, estoque);
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }
    }

    public void danfe() {
        try {

            NfeCabecalho nfe = null;//nfeRepository.get(idnfe, NfeCabecalho.class);
            ModeloDocumento modelo = ModeloDocumento.getByCodigo(Integer.valueOf(nfe.getCodigoModelo()));
            ConfiguracaoEmissorDTO configuracao = nfeService.getConfEmisor(empresa, modelo);
            nfeService.danfe(nfe, configuracao);
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }
    }


    // <editor-fold defaultstate="collapsed" desc="Procedimentos Produto">
    public List<ProdutoDTO> getListProduto(String nome) {
        List<ProdutoDTO> listaProduto = new ArrayList<>();

        try {

            listaProduto = nfeService.getListaProdutoDTO(nome);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProduto;
    }

    public void selecionarProduto(SelectEvent event) {
        ProdutoDTO produtoSelecionado = (ProdutoDTO) event.getObject();
        desconto = BigDecimal.ZERO;
        item = new PdvVendaDetalhe();
        item.setPdvVendaCabecalho(venda);
        item.setValorUnitario(produtoSelecionado.getProduto().getValorVenda());
        item.setQuantidade(BigDecimal.ONE);
        item.setProduto(produtoSelecionado.getProduto());
        item.setTaxaComissao(vendedor.getComissao());
        item.setTaxaDesconto(BigDecimal.ZERO);
        item.setValorDesconto(BigDecimal.ZERO);
    }

    public void calcularDesconto() {
        desconto = desconto == null ? BigDecimal.ZERO : desconto;
        if (tipoDesconto.equals("P")) {
            item.setTaxaDesconto(desconto);
        } else {
            BigDecimal subTotal = item.getValorSubtotal();
            BigDecimal razao = subTotal.subtract(desconto);
            razao = razao.divide(subTotal, MathContext.DECIMAL64);
            razao = razao.multiply(BigDecimal.valueOf(100));
            BigDecimal cem = BigDecimal.valueOf(100);
            BigDecimal percentual = cem.subtract(razao);

            item.setTaxaDesconto(percentual);
        }
    }

    public void addProduto() {
        Optional<PdvVendaDetalhe> itemOpt = getItemVenda(item.getProduto());
        BigDecimal quantidade = item.getQuantidade();
        if (itemOpt.isPresent()) {
            item = itemOpt.get();
            item.setQuantidade(quantidade);
        } else {
            venda.getListaPdvVendaDetalhe().add(0, item);
        }

        venda.calcularValorTotal();
        item = new PdvVendaDetalhe();
        produto = new ProdutoDTO();

    }

    public void alterarQuantidade(Produto produto, BigDecimal quantidade) {
        Optional<PdvVendaDetalhe> itemOpt = getItemVenda(produto);
        if (itemOpt.isPresent()) {
            itemOpt.get().setQuantidade(quantidade);
        }
    }

    public void excluir(Produto produto) {

        int indice = IntStream.range(0, venda.getListaPdvVendaDetalhe().size()).filter(i -> venda.getListaPdvVendaDetalhe().get(i).getProduto().equals(produto)).findAny()
                .getAsInt();
        venda.getListaPdvVendaDetalhe().remove(indice);
    }

    private Optional<PdvVendaDetalhe> getItemVenda(Produto produto) {
        return venda.getListaPdvVendaDetalhe().stream().filter(i -> i.getProduto().equals(produto)).findAny();

    }


    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Procedimentos cliente">
    public List<Cliente> getListCliente(String nome) {
        List<Cliente> list = new ArrayList<>();
        try {

            list = clientes.getEntitys(Cliente.class, "pessoa.nome", nome, new Object[]{"pessoa.nome"});
        } catch (Exception ex) {

        }
        return list;
    }

    public void buscarCliente() {
        cliente = new Cliente();
    }

    public void selecionarCliente() {
        if(cliente.getId()!=null){
            venda.setCliente(cliente);
            venda.setNomeCliente(cliente.getPessoa().getNome());

        }else{

            if(StringUtils.isEmpty(venda.getCpfCnpjCliente())){
                Mensagem.addErrorMessage("CPF/CNPJ obrigatorio");
                FacesContext.getCurrentInstance().validationFailed();
            }else if(StringUtils.isEmpty(venda.getNomeCliente())){
                Mensagem.addErrorMessage("Nome obrigatorio");
                FacesContext.getCurrentInstance().validationFailed();
            }else{
                boolean cpfValido = true;
                if(venda.getCpfCnpjCliente().length() == 14){
                    cpfValido = Biblioteca.cnpjValido(venda.getCpfCnpjCliente());
                    if(!cpfValido){
                        Mensagem.addErrorMessage("CNPJ invalido");
                        FacesContext.getCurrentInstance().validationFailed();
                    }

                }else{
                    cpfValido = Biblioteca.cpfValido(venda.getCpfCnpjCliente());
                    if(!cpfValido){
                        Mensagem.addErrorMessage("CPF invalido");
                        FacesContext.getCurrentInstance().validationFailed();
                    }


                }
            }
            cliente = null;

        }

    }

    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Procedimentos vendedor">
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

    public void buscarVendedores() {
        listVendedores = vendedores.getEntitys(Vendedor.class);
    }
    // </editor-fold>




    // <editor-fold defaultstate="collapsed" desc="Procedimentos Pagamentos ">
    public void iniciarPagamentosVenda() {
        telaVenda = true;
        telaImpressao = false;
        telaCaixa = false;
        telaPagamentos = false;
        exibirCondicoes = false;

        if (venda.getListaPdvVendaDetalhe().isEmpty()) {
            Mensagem.addInfoMessage("Não foram informados itens nessa venda");
        } else {
            telaVenda = false;
            telaImpressao = false;
            telaCaixa = false;
            telaPagamentos = true;
            listTipoPagamento = tipoPagamentoRepository.getAll(PdvTipoPagamento.class);
        }

        totalVenda = BigDecimal.ZERO;
        desconto = BigDecimal.ZERO;
        acrescimo = BigDecimal.ZERO;
        totalReceber = BigDecimal.ZERO;
        troco = BigDecimal.ZERO;
        totalRecebido = BigDecimal.ZERO;
        saldoRestante = BigDecimal.ZERO;

        //guarda valores para calculo
        totalVenda = venda.getValorTotal();
        desconto = venda.getValorDesconto();
        acrescimo = BigDecimal.ZERO;
        totalReceber = Biblioteca.soma(totalVenda, acrescimo);
        saldoRestante = totalReceber;

        valorPago = saldoRestante;
        venda.setListaFormaPagamento(new ArrayList<>());
    }

    public void lancaPagamento() {
        try {

            if (saldoRestante.compareTo(BigDecimal.ZERO) <= 0) {
                Mensagem.addErrorMessage("Todos os valores já foram recebidos. Finalize a venda.");
            } else {
                if (cliente==null && tipoPagamento.getGeraParcelas().equals("S")) {
                    Mensagem.addErrorMessage("Para gera contas a receber é preciso informar um cliente");
                } else {

                    incluiPagamento(tipoPagamento, valorPago);
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            Mensagem.addErrorMessage("", ex);
        }


    }

    private void incluiPagamento(PdvTipoPagamento tipoPagamento, BigDecimal valor) throws Exception {
        Optional<PdvFormaPagamento> formaPagamentoOpt = bucarTipoPagamento(tipoPagamento);
        if (formaPagamentoOpt.isPresent()) {
            Mensagem.addInfoMessage("Forma de pagamento " + tipoPagamento.getDescricao() + " já incluso");
        } else {
            if(totalReceber.compareTo(valorPago)<0 && tipoPagamento.getPermiteTroco().equals("N")){
                Mensagem.addInfoMessage("Forma de pagamento " + tipoPagamento.getDescricao() + " não permite troco");
            }else {
                PdvFormaPagamento formaPagamento = new PdvFormaPagamento();
                formaPagamento.setPdvVendaCabecalho(venda);
                formaPagamento.setPdvTipoPagamento(tipoPagamento);
                formaPagamento.setValor(valor);
                formaPagamento.setForma(tipoPagamento.getCodigo());
                formaPagamento.setEstorno("N");

                if(tipoPagamento.getGeraParcelas().equals("S")){
                    formaPagamento.setCondicao(condicaoPagamento);
                }

                venda.getListaFormaPagamento().add(formaPagamento);


                totalRecebido = Biblioteca.soma(totalRecebido, valor);
                troco = Biblioteca.subtrai(totalRecebido, totalReceber);
                if (troco.compareTo(BigDecimal.ZERO) == -1) {
                    troco = BigDecimal.ZERO;
                }
                verificaSaldoRestante();
            }

        }

    }

    private Optional<PdvFormaPagamento> bucarTipoPagamento(PdvTipoPagamento tipoPagamento) {
        Optional<PdvFormaPagamento> formaPagamentoOpt = venda.getListaFormaPagamento()
                .stream()
                .filter(fp -> fp.getPdvTipoPagamento().equals(tipoPagamento))
                .findAny();
        return formaPagamentoOpt;
    }


    public void excluirPagamento() {
        if (formaPagamentoSelecionado != null) {
            venda.getListaFormaPagamento().remove(formaPagamentoSelecionado);
            verificaSaldoRestante();
        }
        Mensagem.addInfoMessage("Forma de pagamento removida");
    }

    public void finalizarVenda() {


        try{
            verificaSaldoRestante();
            if (saldoRestante.compareTo(BigDecimal.ZERO) <= 0) {
                venda.setTroco(troco);
               venda =  service.finalizarVenda(venda);
                telaVenda = false;
                telaPagamentos = false;
                telaImpressao = true;
                id = venda.getId();
            } else {
                Mensagem.addInfoMessage("Valores informados não são suficientes para finalizar a venda.");
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
            Mensagem.addErrorMessage("Erro ao finalziar venda",ex);
        }
    }



    private void verificaSaldoRestante() {
        BigDecimal recebidoAteAgora = BigDecimal.ZERO;
        List<PdvFormaPagamento> listaPagamento = venda.getListaFormaPagamento();
        for (PdvFormaPagamento p : listaPagamento) {
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
        tipoPagamento = null;
    }

    public void definirCondicoess(){

        exibirCondicoes = tipoPagamento.getGeraParcelas().equals("S") && !tipoPagamento.getCodigo().equals("02");
        if(exibirCondicoes){
            condicoesPagamentos = condicoes.getEntitys(VendaCondicoesPagamento.class,new Object[]{"nome","vistaPrazo","tipoRecebimento"});
        }

    }

    //</editor-fold>



    public void selecionarVendedor() {
        venda.setVendedor(vendedor);
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public List<Vendedor> getListVendedores() {
        return listVendedores;
    }


    @NotNull
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }


    public PdvVendaDetalhe getItem() {
        return item;
    }

    public void setItem(PdvVendaDetalhe item) {
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

    public PdvVendaCabecalho getVenda() {
        return venda;
    }

    public void setVenda(PdvVendaCabecalho venda) {
        this.venda = venda;
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

    public List<VendaCondicoesPagamento> getCondicoesPagamentos() {
        return condicoesPagamentos;
    }

    public void setCondicoesPagamentos(List<VendaCondicoesPagamento> condicoesPagamentos) {
        this.condicoesPagamentos = condicoesPagamentos;
    }

    public ProdutoDTO getProduto() {
        return produto;
    }

    public void setProduto(ProdutoDTO produto) {
        this.produto = produto;
    }

    public PdvTipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(PdvTipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public List<PdvTipoPagamento> getListTipoPagamento() {
        return listTipoPagamento;
    }

    public void setListTipoPagamento(List<PdvTipoPagamento> listTipoPagamento) {
        this.listTipoPagamento = listTipoPagamento;
    }

    public boolean isTelaCaixa() {
        return telaCaixa;
    }

    public void setTelaCaixa(boolean telaCaixa) {
        this.telaCaixa = telaCaixa;
    }

    public BigDecimal getTotalVenda() {
        return totalVenda;
    }

    public void setTotalVenda(BigDecimal totalVenda) {
        this.totalVenda = totalVenda;
    }

    public BigDecimal getAcrescimo() {
        return acrescimo;
    }

    public void setAcrescimo(BigDecimal acrescimo) {
        this.acrescimo = acrescimo;
    }

    public BigDecimal getTotalReceber() {
        return totalReceber;
    }

    public void setTotalReceber(BigDecimal totalReceber) {
        this.totalReceber = totalReceber;
    }

    public BigDecimal getTroco() {
        return troco;
    }

    public void setTroco(BigDecimal troco) {
        this.troco = troco;
    }

    public BigDecimal getTotalRecebido() {
        return totalRecebido;
    }

    public void setTotalRecebido(BigDecimal totalRecebido) {
        this.totalRecebido = totalRecebido;
    }

    public BigDecimal getSaldoRestante() {
        return saldoRestante;
    }

    public void setSaldoRestante(BigDecimal saldoRestante) {
        this.saldoRestante = saldoRestante;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public PdvFormaPagamento getFormaPagamentoSelecionado() {
        return formaPagamentoSelecionado;
    }

    public void setFormaPagamentoSelecionado(PdvFormaPagamento formaPagamentoSelecionado) {
        this.formaPagamentoSelecionado = formaPagamentoSelecionado;
    }

    public boolean isPodeFinalzarVenda() {
        return saldoRestante.signum() == 0;
    }
    public boolean isPodeLancaPagamento() {
        return valorPago.signum() == 0;
    }

    public boolean isExibirCondicoes() {
        return exibirCondicoes;
    }

    public void setExibirCondicoes(boolean exibirCondicoes) {
        this.exibirCondicoes = exibirCondicoes;
    }

    public VendaCondicoesPagamento getCondicaoPagamento() {
        return condicaoPagamento;
    }

    public void setCondicaoPagamento(VendaCondicoesPagamento condicaoPagamento) {
        this.condicaoPagamento = condicaoPagamento;
    }

    public boolean isTelaGrid() {
        return telaGrid;
    }

    public void setTelaGrid(boolean telaGrid) {
        this.telaGrid = telaGrid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
