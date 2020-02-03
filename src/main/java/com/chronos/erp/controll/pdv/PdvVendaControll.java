package com.chronos.erp.controll.pdv;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.controll.ERPLazyDataModel;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.service.comercial.NfeService;
import com.chronos.erp.service.comercial.VendaPdvService;
import com.chronos.erp.util.Biblioteca;
import com.chronos.erp.util.jsf.FacesUtil;
import com.chronos.erp.util.jsf.Mensagem;
import com.chronos.transmissor.exception.EmissorException;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import org.apache.commons.lang3.time.DateUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CellEditEvent;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class PdvVendaControll extends AbstractControll<PdvVendaCabecalho> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<PdvCaixa> caixaRepository;
    @Inject
    private Repository<PdvOperador> operadorRepository;
    @Inject
    private Repository<NfeCabecalho> nfeRepository;
    @Inject
    private VendaPdvService service;
    @Inject
    private NfeService nfeService;

    private VendaDevolucao devolucao;
    private VendaDevolucaoItem vendaDevolucaoItemSelecionado;
    private PdvVendaCabecalho vendaDevolvida;

    private String justificativa;
    private String status;
    private String cliente;
    private Date dataInicial;
    private Date dataFinal;
    private int idcaixa;
    private int idoperador;

    private Map<String, String> statusDomain;
    private Map<String, Integer> operadorDomain;
    private Map<String, Integer> caixaDomain;


    @PostConstruct
    @Override
    public void init() {

        empresa = FacesUtil.getEmpresaUsuario();
        usuario = FacesUtil.getUsuarioSessao();


        List<PdvCaixa> Caixas = caixaRepository.getEntitys(PdvCaixa.class, "idempresa", empresa.getId(), new Object[]{"codigo", "nome"});

        caixaDomain = new LinkedHashMap<>();
        caixaDomain.put("Todos", 0);
        caixaDomain.putAll(Caixas.stream()
                .collect(Collectors.toMap(PdvCaixa::getNome, PdvCaixa::getId)));

        operadorDomain = new LinkedHashMap<>();
        operadorDomain.put("Todos", 0);
        operadorDomain.putAll(operadorRepository.getEntitys(PdvOperador.class, new Object[]{"login"}).stream()
                .collect(Collectors.toMap(PdvOperador::getLogin, PdvOperador::getId)));

        empresa = FacesUtil.getEmpresaUsuario();
        statusDomain = new LinkedHashMap<>();
        statusDomain.put("Todos", null);
        statusDomain.put("Encerrada", "E");
        statusDomain.put("Faturada", "F");
        statusDomain.put("Cancelada", "C");

    }

    public ERPLazyDataModel<PdvVendaCabecalho> getDataModel() {
        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setDao(dao);
            dataModel.setClazz(PdvVendaCabecalho.class);
        }
        Object[] atributos = new Object[]{"idnfe", "dataHoraVenda", "valorSubtotal", "valorDesconto", "valorTotal", "nomeCliente",
                "sicronizado", "statusVenda", "pdvMovimento.pdvCaixa.nome"};
        dataModel.setAtributos(atributos);
        pesquisar();
        return dataModel;
    }

    public void pesquisar() {
        dataModel.getFiltros().clear();
        dataModel.getFiltros().add(new Filtro("empresa.id", empresa.getId()));


        if (!StringUtils.isEmpty(cliente)) {
            dataModel.getFiltros().add(new Filtro("nomeCliente", Filtro.LIKE, cliente));
        }

        if (!StringUtils.isEmpty(status)) {
            dataModel.getFiltros().add(new Filtro("statusVenda", status));
        }

        if (dataInicial != null) {
            dataInicial = DateUtils.truncate(dataInicial, Calendar.DATE);
            dataModel.getFiltros().add(new Filtro("dataHoraVenda", Filtro.MAIOR_OU_IGUAL, dataInicial));
        }

        if (dataFinal != null) {
            dataFinal = DateUtils.truncate(dataFinal, Calendar.DATE);
            dataFinal = DateUtils.addSeconds(DateUtils.addMinutes(DateUtils.addHours(dataFinal, 23), 59), 59);
            dataModel.getFiltros().add(new Filtro("dataHoraVenda", Filtro.MENOR_OU_IGUAL, dataFinal));
        }

        if (idcaixa > 0) {
            dataModel.getFiltros().add(new Filtro("pdvMovimento.pdvCaixa.id", idcaixa));
        }

        if (usuario.getAdministrador().equals("N")) {
            int id = usuario.getOperador() != null ? usuario.getOperador().getId() : 0;
            dataModel.getFiltros().add(new Filtro("pdvMovimento.pdvOperador.id", id));
        } else {
            if (idoperador > 0) {
                dataModel.getFiltros().add(new Filtro("pdvMovimento.pdvOperador.id", idoperador));
            }
        }

    }

    public void exibirDevolucao() {
        vendaDevolvida = dataModel.getRowData(getObjetoSelecionado().getId().toString());
        devolucao = service.gerarDevolucao(vendaDevolvida);
    }

    public void calcularValorDevolucao(CellEditEvent event) {

        try {

            for (VendaDevolucaoItem item : devolucao.getListaVendaDevolucaoItem()) {
                if (!item.getQuantidade().equals(item.getQuantidadeVenda())) {
                    BigDecimal valorUn = Biblioteca.divide(item.getValor(), item.getQuantidadeVenda());
                    BigDecimal valor = Biblioteca.multiplica(item.getQuantidade(), valorUn);
                    item.setValor(valor);
                }
            }

            devolucao.calcularValorCredito();
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro ao calcular devolucao", ex);
            }
        }
    }

    public void removerItemDevolucao(int idx) {
        devolucao.getListaVendaDevolucaoItem().remove(idx);
    }


    public void confirmarDevolucao() {
        try {
            service.confirmarDevolucao(devolucao, vendaDevolvida);
            Mensagem.addInfoMessage("Devolução realizada com sucesso");
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("erro ao gerar a devolução", ex);
            }
        }
    }


    public void cancelarVenda() {
        try {

            boolean estoque = FacesUtil.isUserInRole("ESTOQUE");
            if (getObjetoSelecionado().getStatusVenda().equals("F")) {
                justificativa = "";
                PrimeFaces.current().executeScript("PF('dialogOutrasTelas4').show();");
            } else {
                service.cancelar(getObjetoSelecionado().getId(), estoque, null);
            }


        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Erro ao cancelar Cupom \n", ex);
            } else {
                throw new RuntimeException("Erro ao cancelar Cupom", ex);
            }
        }
    }

    public void gerarNfe() {


        try {

            boolean estoque = FacesUtil.isUserInRole("ESTOQUE");
            PdvVendaCabecalho venda = dataModel.getRowData(getObjetoSelecionado().getId().toString());
            if (!venda.getListaPdvVendaDetalhe().isEmpty() && !venda.getListaFormaPagamento().isEmpty()) {
                service.transmitirNFe(venda, estoque, ModeloDocumento.NFE);
            } else {
                Mensagem.addInfoMessage("não foram encotrado itens para essa venda");
            }

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Erro ao gera NFCe \n", ex);
            } else if (ex instanceof UnknownHostException) {
                Mensagem.addErrorMessage("Erro ao gerar conexao com \n", ex);
            } else if (ex instanceof EmissorException) {
                Mensagem.addErrorMessage("Erro ao gera NFCe  \n", ex);
            } else {
                throw new RuntimeException("Erro ao gerar NFce", ex);
            }


        }
    }

    public void gerarNfce() {


        try {

            boolean estoque = FacesUtil.isUserInRole("ESTOQUE");
            PdvVendaCabecalho venda = dataModel.getRowData(getObjetoSelecionado().getId().toString());
            if (!venda.getListaPdvVendaDetalhe().isEmpty() && !venda.getListaFormaPagamento().isEmpty()) {
                service.transmitirNFe(venda, estoque, ModeloDocumento.NFCE);
            } else {
                Mensagem.addInfoMessage("não foram encotrado itens para essa venda");
            }

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Erro ao gera NFCe \n", ex);
            } else if (ex instanceof UnknownHostException) {
                Mensagem.addErrorMessage("Erro ao gerar conexao com \n", ex);
            } else if (ex instanceof EmissorException) {
                Mensagem.addErrorMessage("Erro ao gera NFCe  \n", ex);
            } else {
                throw new RuntimeException("Erro ao gerar NFce", ex);
            }


        }
    }

    public void cancelarVendaNFCe() {
        try {

            boolean estoque = FacesUtil.isUserInRole("ESTOQUE");
            service.cancelar(getObjetoSelecionado().getId(), estoque, justificativa);

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Erro ao cancelar Cupom \n", ex);
            } else {
                throw new RuntimeException("Erro ao cancelar Cupom", ex);
            }
        }
    }

    public void danfe() {
        try {

            NfeCabecalho nfe = nfeRepository.get(getObjetoSelecionado().getIdnfe(), NfeCabecalho.class);
            nfeService.instanciarConfNfe(nfe.getEmpresa(), nfe.getModeloDocumento(), false);
            nfeService.danfe(nfe);
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Erro ao gera Cupom \n", ex);
            } else {
                throw new RuntimeException("Erro ao gerar Cupom", ex);
            }
        }
    }

    @Override
    protected Class<PdvVendaCabecalho> getClazz() {
        return PdvVendaCabecalho.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "PDV";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public Map<String, String> getStatusDomain() {
        return statusDomain;
    }

    public Map<String, Integer> getOperadorDomain() {
        return operadorDomain;
    }

    public Map<String, Integer> getCaixaDomain() {
        return caixaDomain;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
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

    public int getIdcaixa() {
        return idcaixa;
    }

    public void setIdcaixa(int idcaixa) {
        this.idcaixa = idcaixa;
    }

    public int getIdoperador() {
        return idoperador;
    }

    public void setIdoperador(int idoperador) {
        this.idoperador = idoperador;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public PdvVendaCabecalho getVendaDevolvida() {
        return vendaDevolvida;
    }

    public void setVendaDevolvida(PdvVendaCabecalho vendaDevolvida) {
        this.vendaDevolvida = vendaDevolvida;
    }

    public VendaDevolucao getDevolucao() {
        return devolucao;
    }

    public void setDevolucao(VendaDevolucao devolucao) {
        this.devolucao = devolucao;
    }

    public VendaDevolucaoItem getVendaDevolucaoItemSelecionado() {
        return vendaDevolucaoItemSelecionado;
    }

    public void setVendaDevolucaoItemSelecionado(VendaDevolucaoItem vendaDevolucaoItemSelecionado) {
        this.vendaDevolucaoItemSelecionado = vendaDevolucaoItemSelecionado;
    }
}
