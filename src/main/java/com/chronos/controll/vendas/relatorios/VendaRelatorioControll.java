package com.chronos.controll.vendas.relatorios;

import com.chronos.controll.AbstractRelatorioControll;
import com.chronos.modelo.entidades.PdvVendaCabecalho;
import com.chronos.modelo.entidades.Vendedor;
import com.chronos.repository.Repository;

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
    private Repository<Vendedor> vendedorRepository;
    @Inject
    private Repository<PdvVendaCabecalho> pdvRepository;

    private Date dataInicial;
    private Date dataFinal;
    private Integer idvendedor;
    private int idcupom;
    private PdvVendaCabecalho vendaCupom;
    private Map<String, Integer> listaVendedor;

    @PostConstruct
    @Override
    protected void init() {
        super.init();
        List<Vendedor> list = vendedorRepository.getEntitys(Vendedor.class, new ArrayList<>(), new Object[]{"colaborador.pessoa.nome"});
        list.add(0, new Vendedor(0, "TODOS"));
        listaVendedor = new LinkedHashMap<>();
        listaVendedor.putAll(list.stream()
                .collect(Collectors.toMap((Vendedor::getNome), Vendedor::getId)));
        if(idcupom > 0){

        }
    }
    public void buscarPedido(){
        try{
            if(idcupom>0){
                vendaCupom = pdvRepository.get(idcupom,PdvVendaCabecalho.class);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
    public void imprimirPedido(int id) {
        parametros = new HashMap<>();
        parametros.put("idvenda", id);

        String caminhoRelatorio = "/relatorios/vendas";
        String nomeRelatorio = "venda.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "pedidoVenda.pdf");
    }

    public void imprimirPedidoCupom(int id) {
        parametros = new HashMap<>();
        parametros.put("idvenda", id);

        String caminhoRelatorio = "/relatorios/vendas";
        String nomeRelatorio = "vendaCupom.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "pedidoVenda.pdf");
    }

    public void imprimirRelacaoVendas() {
        parametros = new HashMap<>();
        parametros.put("dataPedidoInicial", dataInicial);
        parametros.put("dataPedidoFinal", dataFinal);
        parametros.put("idempresa", empresa.getId());
        String caminhoRelatorio = "/relatorios/vendas";
        String nomeRelatorio = "relacaoVendas.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "realcaoVendas.pdf");
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
        parametros.put("peridoInicial", dataInicial);
        parametros.put("peridoFinal", dataFinal);
        parametros.put("idempresa", empresa.getId());
        String caminhoRelatorio = "/relatorios/vendas";
        String nomeRelatorio = "relacaoProdutosVendido.jasper";

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
}
