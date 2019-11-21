package com.chronos.erp.controll.pcp;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class PcpOpCabecalhoControll extends AbstractControll<PcpOpCabecalho> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<Produto> produtoRepository;
    @Inject
    private Repository<EstoqueGrade> estoqueGradeRepository;

    private PcpOpDetalhe pcpOpDetalhe;
    private PcpOpDetalhe pcpOpDetalheSelecionado = new PcpOpDetalhe();

    private boolean exibirGrade = false;

    private EstoqueCor cor;
    private EstoqueTamanho tamanho;
    private List<EstoqueCor> cores;
    private List<EstoqueTamanho> tamanhos;
    private List<EstoqueGrade> grades;

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setEmpresa(empresa);
        getObjeto().setListaPcpOpDetalhe(new HashSet<>());

        incluirPcpOpDetalhe();
    }

    @Override
    public void doEdit() {
        super.doEdit();
        PcpOpCabecalho of = dataModel.getRowData(getObjeto().getId().toString());
        setObjeto(of);

        incluirPcpOpDetalhe();

    }

    public void incluirPcpOpDetalhe() {
        pcpOpDetalhe = new PcpOpDetalhe();
        pcpOpDetalhe.setPcpOpCabecalho(getObjeto());
        pcpOpDetalhe.setCustoPrevisto(BigDecimal.ZERO);
        pcpOpDetalhe.setCustoRealizado(BigDecimal.ZERO);
        pcpOpDetalhe.setQuantidadeEntregue(BigDecimal.ZERO);
        pcpOpDetalhe.setQuantidadeProduzida(BigDecimal.ZERO);
        pcpOpDetalhe.setQuantidadeProduzir(BigDecimal.ONE);
    }

    public void buscarGrade() {


        exibirGrade = false;
        if (pcpOpDetalhe.getProduto() != null && pcpOpDetalhe.getProduto().getPossuiGrade() != null && pcpOpDetalhe.getProduto().getPossuiGrade()) {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("idproduto", pcpOpDetalhe.getProduto().getId()));
            filtros.add(new Filtro("idempresa", empresa.getId()));

            grades = estoqueGradeRepository.getEntitys(EstoqueGrade.class, filtros);

            if (!grades.isEmpty()) {
                cores = grades.stream().map(g -> g.getEstoqueCor()).collect(Collectors.toList());
                exibirGrade = true;
            }

        }

    }

    public void salvarPcpOpDetalhe() {
        if (pcpOpDetalhe.getId() == null) {
            getObjeto().getListaPcpOpDetalhe().add(pcpOpDetalhe);
        }
        salvar("Registro salvo com sucesso!");
    }

    public void excluirPcpOpDetalhe() {

        getObjeto().getListaPcpOpDetalhe().remove(pcpOpDetalheSelecionado);
        salvar("Registro excluído com sucesso!");

    }

    public List<Produto> getListaProduto(String nome) {
        List<Produto> listaProduto = new ArrayList<>();
        try {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("servico", "N"));
            filtros.add(new Filtro("nome", Filtro.LIKE, nome));
            filtros.add(new Filtro(Filtro.AND, "tipo", Filtro.IN, new Object[]{"V", "P"}));
            atributos = new Object[]{"nome"};
            listaProduto = produtoRepository.getEntitys(Produto.class, filtros, atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaProduto;
    }

    @Override
    protected Class<PcpOpCabecalho> getClazz() {
        return PcpOpCabecalho.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "PCP_OPERACAO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public PcpOpDetalhe getPcpOpDetalhe() {
        return pcpOpDetalhe;
    }

    public void setPcpOpDetalhe(PcpOpDetalhe pcpOpDetalhe) {
        this.pcpOpDetalhe = pcpOpDetalhe;
    }

    public PcpOpDetalhe getPcpOpDetalheSelecionado() {
        return pcpOpDetalheSelecionado;
    }

    public void setPcpOpDetalheSelecionado(PcpOpDetalhe pcpOpDetalheSelecionado) {
        this.pcpOpDetalheSelecionado = pcpOpDetalheSelecionado;
    }

    public EstoqueCor getCor() {
        return cor;
    }

    public void setCor(EstoqueCor cor) {
        this.cor = cor;
    }

    public EstoqueTamanho getTamanho() {
        return tamanho;
    }

    public void setTamanho(EstoqueTamanho tamanho) {
        this.tamanho = tamanho;
    }

    public List<EstoqueCor> getCores() {
        return cores;
    }

    public List<EstoqueTamanho> getTamanhos() {
        return tamanhos;
    }

    public boolean isExibirGrade() {
        return exibirGrade;
    }
}
