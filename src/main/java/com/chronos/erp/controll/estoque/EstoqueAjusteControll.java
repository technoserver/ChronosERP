package com.chronos.erp.controll.estoque;

import com.chronos.erp.modelo.entidades.Empresa;
import com.chronos.erp.modelo.entidades.EmpresaProduto;
import com.chronos.erp.modelo.entidades.EstoqueGrade;
import com.chronos.erp.modelo.entidades.Produto;
import com.chronos.erp.modelo.enuns.AcaoLog;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.service.gerencial.AuditoriaService;
import com.chronos.erp.util.FormatValor;
import com.chronos.erp.util.jsf.FacesUtil;
import com.chronos.erp.util.jsf.Mensagem;
import org.primefaces.PrimeFaces;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Named
@ViewScoped
public class EstoqueAjusteControll implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<EmpresaProduto> repository;

    @Inject
    private Repository<Produto> produtoRepository;
    @Inject
    private AuditoriaService auditoriaService;
    @Inject
    private Repository<EstoqueGrade> estoqueGradeRepository;


    private Empresa empresa;
    private String codigo;
    private BigDecimal quantidade;
    private BigDecimal valorVenda;
    private Integer tipoEstoque;

    private List<EmpresaProduto> produtos;
    private EmpresaProduto produtoSelecionado;
    private List<EstoqueGrade> grades;
    private List<EstoqueGrade> gradesUpdate;
    private EmpresaProduto produtoGrade;


    @PostConstruct
    private void init() {
        empresa = FacesUtil.getEmpresaUsuario();
        produtos = new ArrayList<>();
        tipoEstoque = 1;
        gradesUpdate = new ArrayList<>();
    }

    public void addProduto() {
        if (!StringUtils.isEmpty(codigo)) {
            List<Filtro> filtros = new LinkedList<>();
            filtros.add(new Filtro("empresa.id", empresa.getId()));
            if (codigo.length() >= 8) {
                filtros.add(new Filtro("produto.gtin", Filtro.IGUAL, codigo));
            } else {
                String str = "0" + codigo.replaceAll("\\D", "");
                int cod = Integer.valueOf(str);
                filtros.add(new Filtro("produto.id", cod));
            }

            EmpresaProduto produto = repository.get(EmpresaProduto.class, filtros);

            if (produto == null) {
                Mensagem.addErrorMessage("Produto não localizado");
            } else {
                if (produtos.contains(produto)) {
                    Mensagem.addErrorMessage("Produto já informado");
                } else {
                    if (tipoEstoque == 1) {
                        produto.setEstoqueVerificado(Optional.ofNullable(quantidade).orElse(BigDecimal.ZERO));
                    } else {
                        produto.setQuantidadeEstoque(Optional.ofNullable(quantidade).orElse(BigDecimal.ZERO));
                    }


                    produto.setValorVenda(Optional.ofNullable(valorVenda).orElse(produto.getProduto().getValorVenda()));

                    if (produto.getProduto().getPossuiGrade()) {

                        filtros = new ArrayList<>();
                        filtros.add(new Filtro("idproduto", produto.getProduto().getId()));
                        filtros.add(new Filtro("idempresa", empresa.getId()));

                        grades = estoqueGradeRepository.getEntitys(EstoqueGrade.class, filtros);

                        if (grades.isEmpty()) {
                            produtos.add(produto);
                        } else {

                            PrimeFaces.current().executeScript("PF('dialogOutrasTelas').show();");
                            produtoGrade = produto;
                        }
                    } else {
                        produtos.add(produto);
                    }


                }

            }
        } else {
            Mensagem.addErrorMessage("Produto não localizado");
        }
    }

    public void remover() {
        produtos.remove(produtoSelecionado);
    }


    public void confirmarEstoqueGrade() {


        try {
            BigDecimal qtd = grades
                    .stream()
                    .map(i -> i.getQuantidadeEntrada())
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);

            if (quantidade == null || qtd.compareTo(quantidade) != 0) {
                throw new ChronosException("Quantidade de estoque informada para grade invalida ");
            }

            gradesUpdate.addAll(grades);
            produtos.add(produtoGrade);

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().validationFailed();
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Ocorreu um erro ao confirmar os valores da grade", ex);
            }
        }
    }


    public void salvar() {


        try {
            if (produtos.isEmpty()) {
                Mensagem.addErrorMessage("Não foram adicionado produtos a lista");
            } else {

                for (EmpresaProduto produto : produtos) {
                    repository.atualizarNamedQuery("EmpresaProduto.atualizarEstoque", produto.getQuantidadeEstoque(), produto.getId(), produto.getEstoqueVerificado());

                    auditoriaService.gerarLog(AcaoLog.AJUSTE, "alterado a quantidade em estoque do produto " + produto.getProduto().getNome()
                            + " para " + FormatValor.getInstance().formatarValor(produto.getQuantidadeEstoque()), "Estoque Ajuste");


                    if (produto.getValorVenda() != null) {

                        produtoRepository.atualizarNamedQuery("Produto.atualizarValorVenda", produto.getValorVenda(), produto.getProduto().getId());

                        auditoriaService.gerarLog(AcaoLog.AJUSTE, "alterado o valor de venda do produto " + produto.getProduto().getNome()
                                + " para " + FormatValor.getInstance().formatarValor(produto.getValorVenda()), "Estoque Ajuste");
                    }
                }

                for (EstoqueGrade g : gradesUpdate) {
                    if (tipoEstoque == 1) {
                        g.setVerificado(g.getQuantidadeEntrada());
                    } else {
                        g.setQuantidade(g.getQuantidadeEntrada());
                    }
                    estoqueGradeRepository.atualizar(g);
                }

                Mensagem.addInfoMessage("Estoque Reajustado");
                produtos.clear();
            }
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao atualizar o estoque", ex);
        }
    }


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public List<EmpresaProduto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<EmpresaProduto> produtos) {
        this.produtos = produtos;
    }

    public EmpresaProduto getProdutoSelecionado() {
        return produtoSelecionado;
    }

    public void setProdutoSelecionado(EmpresaProduto produtoSelecionado) {
        this.produtoSelecionado = produtoSelecionado;
    }

    public BigDecimal getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(BigDecimal valorVenda) {
        this.valorVenda = valorVenda;
    }

    public Integer getTipoEstoque() {
        return tipoEstoque;
    }

    public void setTipoEstoque(Integer tipoEstoque) {
        this.tipoEstoque = tipoEstoque;
    }

    public List<EstoqueGrade> getGrades() {
        return grades;
    }

    public void setGrades(List<EstoqueGrade> grades) {
        this.grades = grades;
    }
}
