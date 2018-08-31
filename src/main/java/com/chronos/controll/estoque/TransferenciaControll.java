package com.chronos.controll.estoque;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.*;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.service.estoque.TransferenciaService;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
    private TransferenciaService service;
    private List<Empresa> empresas;
    private List<TributOperacaoFiscal> operacoesFiscais;
    private Map<String, Integer> listaEmpresas;
    private int idempresaDestino;

    private Produto produto;
    private BigDecimal quantidade;

    @Override
    public void doCreate() {


        try {
            super.doCreate();
            int id = empresa.getTipo().equals("M") ? empresa.getId() : empresa.getIdempresa();
            empresas = service.popularFiliais(empresa.getId(), id, empresa.getTipo());
            listaEmpresas = new LinkedHashMap<>();
            listaEmpresas.putAll(empresas.stream()
                    .collect(Collectors.toMap((Empresa::getRazaoSocial), Empresa::getId)));
            getObjeto().setColaborador(usuario.getColaborador());
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


    public void addItem() {
        produto = new Produto();
        quantidade = BigDecimal.ZERO;
    }

    public void salvarItem() {
        getObjeto().getListEstoqueTransferenciaDetalhe().stream()
                .filter(i -> i.getProduto().equals(produto))
                .findAny();
    }

    public List<TributOperacaoFiscal> getListaTributOperacaoFiscal(String descricao) {
        List<TributOperacaoFiscal> listaTributOperacaoFiscal = new ArrayList<>();

        try {
            listaTributOperacaoFiscal = operacaoFiscalRepository.getEntitys(TributOperacaoFiscal.class, "descricao", descricao, new Object[]{"descricao", "cfop", "obrigacaoFiscal", "destacaIpi", "destacaPisCofins", "calculoIssqn"});
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

    public Map<String, Integer> getListaEmpresas() {
        return listaEmpresas;
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
}
