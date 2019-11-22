package com.chronos.erp.controll.pcp;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.util.Biblioteca;
import com.chronos.erp.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        getObjeto().setListaPcpOpDetalhe(new ArrayList<>());
        getObjeto().setCustoTotalPrevisto(BigDecimal.ZERO);
        getObjeto().setCustoTotalRealizado(BigDecimal.ZERO);
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

        pcpOpDetalhe.setCustoPrevisto(Optional.ofNullable(pcpOpDetalhe.getProduto().getCustoProducao()).orElse(BigDecimal.ZERO));
        pcpOpDetalhe.setCustoRealizado(Optional.ofNullable(pcpOpDetalhe.getProduto().getCustoProducao()).orElse(BigDecimal.ZERO));
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

    public void definirTamanhos() {
        if (cor != null) {
            tamanhos = grades.stream()
                    .filter(g -> g.getEstoqueCor().getId().equals(cor.getId()))
                    .map(t -> t.getEstoqueTamanho()).collect(Collectors.toList());
        }
    }

    public void salvarPcpOpDetalhe() {


        try {
            if (exibirGrade) {
                Optional<EstoqueGrade> first = grades.stream()
                        .filter(g -> g.getEstoqueCor().getId().equals(cor.getId()) && g.getEstoqueTamanho().getId().equals(tamanho.getId()))
                        .findFirst();

                if (!first.isPresent()) {
                    throw new ChronosException("Grade não localizada");
                }

                String nome = pcpOpDetalhe.getProduto().getNome() + " COR " + cor.getNome() + " TAM " + tamanho.getNome();
                pcpOpDetalhe.getProduto().setNome(nome);
                pcpOpDetalhe.setIdgrade(first.get().getId());
            }

            getObjeto().getListaPcpOpDetalhe().add(pcpOpDetalhe);

            BigDecimal custoPrevisto = getObjeto().getCustoTotalPrevisto();
            BigDecimal custoTotalRealizado = getObjeto().getCustoTotalRealizado();

            custoPrevisto = Biblioteca.soma(custoPrevisto, pcpOpDetalhe.getCustoPrevisto());
            custoTotalRealizado = Biblioteca.soma(custoTotalRealizado, pcpOpDetalhe.getCustoRealizado());

            getObjeto().setCustoTotalPrevisto(custoPrevisto);
            getObjeto().setCustoTotalRealizado(custoTotalRealizado);

            incluirPcpOpDetalhe();

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("erro ao add o item da venda", ex);
            }
        }



    }

    public void excluirPcpOpDetalhe() {

        BigDecimal custoPrevisto = getObjeto().getCustoTotalPrevisto();
        BigDecimal custoTotalRealizado = getObjeto().getCustoTotalRealizado();

        custoPrevisto = Biblioteca.subtrai(custoPrevisto, pcpOpDetalhe.getCustoPrevisto());
        custoTotalRealizado = Biblioteca.subtrai(custoTotalRealizado, pcpOpDetalhe.getCustoRealizado());

        getObjeto().setCustoTotalPrevisto(custoPrevisto);
        getObjeto().setCustoTotalRealizado(custoTotalRealizado);

        getObjeto().getListaPcpOpDetalhe().remove(pcpOpDetalheSelecionado);


    }

    public List<Produto> getListaProduto(String nome) {
        List<Produto> listaProduto = new ArrayList<>();
        try {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("servico", "N"));
            filtros.add(new Filtro("nome", Filtro.LIKE, nome));
            filtros.add(new Filtro(Filtro.AND, "tipo", Filtro.IN, new Object[]{"V", "P"}));

            listaProduto = produtoRepository.getEntitys(Produto.class, filtros);
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
        return "PCP_ORDER_PRODUCAO";
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
