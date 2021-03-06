package com.chronos.erp.controll.estoque;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.service.cadastros.ProdutoService;
import com.chronos.erp.service.comercial.NfeService;
import com.chronos.erp.service.estoque.TransferenciaService;
import com.chronos.erp.util.Biblioteca;
import com.chronos.erp.util.jpa.Transactional;
import com.chronos.erp.util.jsf.Mensagem;
import com.chronos.transmissor.exception.EmissorException;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class TransferenciaControll extends AbstractControll<EstoqueTransferenciaCabecalho> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<TributOperacaoFiscal> operacaoFiscalRepository;
    @Inject
    private Repository<TributGrupoTributario> grupoTributarioRepository;
    @Inject
    private Repository<NfeCabecalho> nfeCabecalhoRepository;

    @Inject
    private ProdutoService produtoService;
    @Inject
    private NfeService nfeService;

    @Inject
    private TransferenciaService service;
    private List<Empresa> empresas;
    private List<TributOperacaoFiscal> operacoesFiscais;

    private Map<String, Integer> listaEmps;
    private int idempresaDestino;

    private EstoqueTransferenciaDetalhe itemSelecionado;
    private Produto produto;
    private BigDecimal quantidade;

    @Override
    public void doCreate() {


        try {
            super.doCreate();
            int id = empresa.getTipo().equals("M") ? empresa.getId() : empresa.getIdempresa();
            empresas = service.popularFiliais(empresa.getId(), id, empresa.getTipo());
            listaEmps = new LinkedHashMap<>();
            listaEmps.putAll(empresas.stream()
                    .collect(Collectors.toMap((Empresa::getRazaoSocial), Empresa::getId)));
            getObjeto().setColaborador(new Colaborador(usuario.getIdcolaborador()));
            getObjeto().setEmpresaOrigem(empresa);
            setTelaGrid(false);
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro ao criar uma nova transferencia", ex);
            }
        }

    }


    @Transactional
    @Override
    public void salvar() {
        try {
            getObjeto().setEmpresaDestino(new Empresa(idempresaDestino));
            service.salvar(getObjeto());
            Mensagem.addInfoMessage("Transferencia finalizada");
            setTelaGrid(true);
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else if (ex instanceof EmissorException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro ao salvar a transferencia", ex);
            }
        }
    }

    public void addItem() {

        if (getObjeto().getTributOperacaoFiscal() == null) {
            FacesContext.getCurrentInstance().validationFailed();

            Mensagem.addErrorMessage("É preciso informar a Operação fiscal");
        } else {
            produto = new Produto();
            quantidade = BigDecimal.ONE;
        }
    }

    public void salvarItem() {
        Optional<EstoqueTransferenciaDetalhe> detalheOptional = getObjeto().getListEstoqueTransferenciaDetalhe().stream()
                .filter(i -> i.getProduto().equals(produto))
                .findAny();
        if (detalheOptional.isPresent()) {
            Mensagem.addErrorMessage("produto já adicionado");
        } else {
            EstoqueTransferenciaDetalhe item = new EstoqueTransferenciaDetalhe();
            item.setEstoqueTransferenciaCabecalho(getObjeto());
            item.setProduto(produto);
            item.setQuantidade(quantidade);
            item.setValorCusto(produto.getCustoUnitario());
            item.setValorTotal(Biblioteca.multiplica(quantidade, item.getValorCusto()));
            item.setUnidade(produto.getUnidadeProduto().getSigla());
            getObjeto().getListEstoqueTransferenciaDetalhe().add(item);
        }
    }

    public void removerItem() {
        getObjeto().getListEstoqueTransferenciaDetalhe().remove(itemSelecionado);
    }


    public void danfe() {
        try {

            NfeCabecalho nfe = nfeCabecalhoRepository.get(getObjetoSelecionado().getIdnfecabeclaho(), NfeCabecalho.class);
            nfeService.instanciarConfNfe(nfe.getEmpresa(), nfe.getModeloDocumento(), nfe.getSerie(), false);
            nfeService.danfe(nfe);
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Erro ao gera Cupom \n", ex);
            } else {
                throw new RuntimeException("Erro ao gerar Cupom", ex);
            }
        }
    }

    public List<TributOperacaoFiscal> getListaTributOperacaoFiscal(String descricao) {
        List<TributOperacaoFiscal> listaTributOperacaoFiscal = new ArrayList<>();

        try {
            listaTributOperacaoFiscal = operacaoFiscalRepository.getEntitys(TributOperacaoFiscal.class, "descricao", descricao, new Object[]{"descricao", "descricaoNaNf", "estoque", "estoqueVerificado", "obrigacaoFiscal", "destacaIpi", "destacaPisCofins"});
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTributOperacaoFiscal;
    }

    public List<TributGrupoTributario> getListaTributGrupoTributario(String nome) {
        List<TributGrupoTributario> grupos = new ArrayList<>();
        try {
            grupos = grupoTributarioRepository.getEntitys(TributGrupoTributario.class, "descricao", nome, new Object[]{"descricao"});
        } catch (Exception ex) {

        }
        return grupos;
    }

    public List<Produto> getListaProduto(String filtro) {
        List<Produto> produtos = new ArrayList<>();

        try {
            produtos = produtoService.getProdutosTransferencia(empresa.getId(), idempresaDestino, filtro);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return produtos;
    }


    @Override
    protected Class<EstoqueTransferenciaCabecalho> getClazz() {
        return EstoqueTransferenciaCabecalho.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "TRANSFERENCIA";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public Map<String, Integer> getListaEmps() {
        return listaEmps;
    }

    public void setListaEmps(Map<String, Integer> listaEmps) {
        this.listaEmps = listaEmps;
    }

    public int getIdempresaDestino() {
        return idempresaDestino;
    }

    public void setIdempresaDestino(int idempresaDestino) {
        this.idempresaDestino = idempresaDestino;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public EstoqueTransferenciaDetalhe getItemSelecionado() {
        return itemSelecionado;
    }

    public void setItemSelecionado(EstoqueTransferenciaDetalhe itemSelecionado) {
        this.itemSelecionado = itemSelecionado;
    }
}
