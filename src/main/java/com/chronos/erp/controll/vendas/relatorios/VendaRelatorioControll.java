package com.chronos.erp.controll.vendas.relatorios;

import com.chronos.erp.controll.AbstractRelatorioControll;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.modelo.view.PessoaCliente;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.repository.VendaRepository;
import com.chronos.erp.service.comercial.VendedorService;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by john on 18/09/17.
 */
@Named
@RequestScoped
public class VendaRelatorioControll extends AbstractRelatorioControll implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private VendedorService vendedorService;
    @Inject
    private Repository<PdvVendaCabecalho> pdvRepository;
    @Inject
    private Repository<VendaCabecalho> vendaCabecalhoRepository;
    @Inject
    private Repository<PdvMovimento> movimentoRepository;
    @Inject
    private VendaRepository vendaRepository;

    @Inject
    private Repository<VendaConsignadaCabecalho> vendaConsignadaRepository;

    @Inject
    private Repository<ProdutoGrupo> produtoGrupoRepository;

    @Inject
    private Repository<PessoaCliente> pessoaClienteRepository;

    private Date dataInicial;
    private Date dataFinal;
    private Integer idvendedor = 0;
    private int idcupom;
    private int idvendaConsignada;
    private int idgrupo;
    private PdvVendaCabecalho vendaCupom;
    private VendaCabecalho pedidoVenda;
    private VendaConsignadaCabecalho vendaConsignada;
    private PessoaCliente cliente;
    private Map<String, Integer> listaVendedor;
    private Map<String, Integer> listaColaborador;
    private String statusVendas;
    private Map<String, Integer> listaGrupo;

    private Map<String, String> status;

    private Map<String, Boolean> naoSim;



    private boolean agrupar;

    @PostConstruct
    @Override
    protected void init() {
        super.init();

        listaColaborador = vendedorService.getMapColaboradorComissionado();

        listaVendedor = vendedorService.getMapVendedores();

        listaGrupo = new LinkedHashMap<>();
        listaGrupo.put("TODOS", 0);
        listaGrupo.putAll(produtoGrupoRepository.getEntitys(ProdutoGrupo.class, new Object[]{"nome"}).stream()
                .collect(Collectors.toMap((ProdutoGrupo::getNome), ProdutoGrupo::getId)));


        status = new LinkedHashMap<>();
        status.put("Encerrado/Faturado", "");
        status.put("Encerrado/Digitacao", "ED");
        status.put("Digitacao", "D");
        status.put("Encerrada", "E");
        status.put("Faturada", "F");
        status.put("Devolvida", "DV");
        status.put("Devolvida Parcilamente", "DP");
        status.put("Cancelado", "C");


        naoSim = new LinkedHashMap<>();
        naoSim.put("NÂO", false);
        naoSim.put("SIM ", true);

        if (idcupom > 0) {

        }

        dataFinal = new Date();
        dataInicial = new Date();
    }

    public void buscarPedido() {
        try {
            if (idcupom > 0) {
                vendaCupom = pdvRepository.get(idcupom, PdvVendaCabecalho.class);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void buscarPedidoVenda() {
        try {
            if (idcupom > 0) {
                pedidoVenda = vendaCabecalhoRepository.get(idcupom, VendaCabecalho.class);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void buscarVendaConsignada() {
        try {
            if (idvendaConsignada > 0) {
                vendaConsignada = vendaConsignadaRepository.get(idvendaConsignada, VendaConsignadaCabecalho.class);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void imprimirTabelaPreco(int id) {
        parametros = new HashMap<>();
        parametros.put("id", id);
        parametros.put("idempresa", empresa.getId());

        String caminhoRelatorio = "/relatorios/vendas";
        String nomeRelatorio = "tabelaPreco.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "tabela_preco.pdf");
    }

    public void imprimirPedido(int id) {
        parametros = new HashMap<>();
        parametros.put("idvenda", id);

        String caminhoRelatorio = "/relatorios/vendas";
        String nomeRelatorio = "venda.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "pedidoVenda.pdf");
    }

    public void imprimirPedidoPdv(int id) {
        parametros = new HashMap<>();
        parametros.put("idvenda", id);

        String caminhoRelatorio = "/relatorios/vendas";
        String nomeRelatorio = "venda_pdv.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "pedidoVenda.pdf");
    }

    public void imprimirRomaneio(int id) {
        parametros = new HashMap<>();
        parametros.put("idvenda", id);

        String caminhoRelatorio = "/relatorios/vendas";
        String nomeRelatorio = "romaneioEntrega.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "romaneio.pdf");
    }

    public void imprimirPedidoCupom(int id) {
        parametros = new HashMap<>();
        parametros.put("idvenda", id);
        parametros.put("usuario", userService.getUsuarioLogado().getNome());

        String caminhoRelatorio = "/relatorios/vendas";
        String nomeRelatorio = "vendaCupom.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "pedidoVenda.pdf");
    }

    public void imprimirOrcamento(int id) {
        parametros = new HashMap<>();
        parametros.put("id", id);

        String caminhoRelatorio = "/relatorios/vendas";
        String nomeRelatorio = "orcamento.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "orcamento.pdf");
    }

    public void imprimirOrcamentoCupom(int id) {
        parametros = new HashMap<>();
        parametros.put("id", id);

        String caminhoRelatorio = "/relatorios/vendas";
        String nomeRelatorio = "orcamentoCupom.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "orcamento.pdf");
    }

    public void imprimirRelacaoVendas() {
        parametros = new HashMap<>();
        parametros.put("dataPedidoInicial", dataInicial);
        parametros.put("dataPedidoFinal", dataFinal);
        parametros.put("idempresa", empresa.getId());

        if (idvendedor > 0) {
            parametros.put("idvendedor", idvendedor);
        }

        if (cliente != null) {
            parametros.put("idcliente", cliente.getId());
        }

        if (!StringUtils.isEmpty(statusVendas)) {

            switch (statusVendas) {
                case "ED":
                    parametros.put("situacao", Arrays.asList(new String[]{"D", "E"}));
                case "D":
                    parametros.put("situacao", Arrays.asList(new String[]{"D"}));
                    break;
                case "F":
                    parametros.put("situacao", Arrays.asList(new String[]{"F"}));
                    break;
                case "E":
                    parametros.put("situacao", Arrays.asList(new String[]{"E"}));
                    break;
                case "C":
                    parametros.put("situacao", Arrays.asList(new String[]{"C"}));
                    break;
                case "DV":
                    parametros.put("situacao", Arrays.asList(new String[]{"D"}));
                    break;
                case "DP":
                    parametros.put("situacao", Arrays.asList(new String[]{"DP"}));
                    break;
            }
        }


        String caminhoRelatorio = "/relatorios/vendas";
        String nomeRelatorio = "relacaoVendas.jasper";

        String nome = "relacaoVendas." + tipoRelatorio;


        executarRelatorio(caminhoRelatorio, nomeRelatorio, nome);
    }

    public void imprimirLucratividade() {
        parametros = new HashMap<>();
        parametros.put("peridoInicial", dataInicial);
        parametros.put("periodoFinal", dataFinal);
        parametros.put("idempresa", empresa.getId());


        String caminhoRelatorio = "/relatorios/vendas";
        String nomeRelatorio = "lucratividade.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "lucratividade.pdf");
    }

    public void imprimirRanckProdutos() {
        parametros = new HashMap<>();
        parametros.put("dataInicial", dataInicial);
        parametros.put("dataFinal", dataFinal);
        parametros.put("idempresa", empresa.getId());
        String caminhoRelatorio = "/relatorios/vendas";
        String nomeRelatorio = "rankProdutos.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "ranckProdutos.pdf");
    }

    public void imprimirComissoes() {
        parametros = new HashMap<>();
        parametros.put("peridoInicial", dataInicial);
        parametros.put("peridoFinal", dataFinal);
        parametros.put("idempresa", empresa.getId());
        if (idvendedor > 0) {
            parametros.put("idvendedor", idvendedor);
        }

        String caminhoRelatorio = "/relatorios/vendas";
        String nomeRelatorio = "relacaoComissoes.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "comissoes.pdf");
    }

    public void imprimirProdutoVendido() {
        parametros = new HashMap<>();
        parametros.put("dataInicial", dataInicial);
        parametros.put("dataFinal", dataFinal);
        parametros.put("idempresa", empresa.getId());

        if (idgrupo > 0) {
            parametros.put("idgrupo", idgrupo);
        }

        String caminhoRelatorio = "/relatorios/vendas";
        String nomeRelatorio = agrupar ? "relacaoProdutosVendidoGrupo.jasper" : "relacaoProdutosVendido.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "produtosMaisVendido.pdf");
    }

    public void imprimirProdutoVendido2() {
        parametros = new HashMap<>();
        parametros.put("dataInicial", dataInicial);
        parametros.put("dataFinal", dataFinal);
        parametros.put("idempresa", empresa.getId());


        String caminhoRelatorio = "/relatorios/vendas";
        String nomeRelatorio = "relacaoProdutosVendido2.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "produtosMaisVendido.pdf");
    }

    public void imprimirClienteCompra() {
        parametros = new HashMap<>();
        parametros.put("peridoInicial", dataInicial);
        parametros.put("peridoFinal", dataFinal);
        parametros.put("idempresa", empresa.getId());

        String caminhoRelatorio = "/relatorios/vendas";
        String nomeRelatorio = "relacaoClientesVenda.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "clientesMaisCompram.pdf");
    }

    public void imprimirRelacaoProdutosEnradaXValorVenda() {
        parametros = new HashMap<>();
        parametros.put("dataInicial", dataInicial);
        parametros.put("dataFinal", dataFinal);

        String caminhoRelatorio = "/relatorios/vendas";
        String nomeRelatorio = "relacaoProdutosValorEntradaXValorVenda.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "relacaoProdutos.pdf");
    }

    public List<PessoaCliente> getListaCliente(String nome) {
        List<PessoaCliente> listaCliente = new ArrayList<>();
        try {
            listaCliente = pessoaClienteRepository.getEntitys(PessoaCliente.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaCliente;
    }


    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Integer getIdvendedor() {
        return idvendedor;
    }

    public void setIdvendedor(Integer idvendedor) {
        this.idvendedor = idvendedor;
    }

    public Map<String, Integer> getListaVendedor() {
        return listaVendedor;
    }

    public void setListaVendedor(Map<String, Integer> listaVendedor) {
        this.listaVendedor = listaVendedor;
    }

    public int getIdcupom() {
        return idcupom;
    }

    public void setIdcupom(int idcupom) {
        this.idcupom = idcupom;
    }

    public PdvVendaCabecalho getVendaCupom() {
        return vendaCupom;
    }

    public void setVendaCupom(PdvVendaCabecalho vendaCupom) {
        this.vendaCupom = vendaCupom;
    }

    public PessoaCliente getCliente() {
        return cliente;
    }

    public void setCliente(PessoaCliente cliente) {
        this.cliente = cliente;
    }

    public String getStatusVendas() {
        return statusVendas;
    }

    public void setStatusVendas(String statusVendas) {
        this.statusVendas = statusVendas;
    }

    public Map<String, String> getStatus() {
        return status;
    }

    public void setStatus(Map<String, String> status) {
        this.status = status;
    }

    public boolean isAgrupar() {
        return agrupar;
    }

    public void setAgrupar(boolean agrupar) {
        this.agrupar = agrupar;
    }

    public Map<String, Boolean> getNaoSim() {
        return naoSim;
    }

    public Map<String, Integer> getListaGrupo() {
        return listaGrupo;
    }

    public int getIdgrupo() {
        return idgrupo;
    }

    public void setIdgrupo(int idgrupo) {
        this.idgrupo = idgrupo;
    }

    public int getIdvendaConsignada() {
        return idvendaConsignada;
    }

    public void setIdvendaConsignada(int idvendaConsignada) {
        this.idvendaConsignada = idvendaConsignada;
    }

    public VendaConsignadaCabecalho getVendaConsignada() {
        return vendaConsignada;
    }

    public Map<String, Integer> getListaColaborador() {
        return listaColaborador;
    }

    public VendaCabecalho getPedidoVenda() {
        return pedidoVenda;
    }
}
