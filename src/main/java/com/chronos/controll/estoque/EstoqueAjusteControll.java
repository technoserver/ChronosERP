package com.chronos.controll.estoque;

import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.EmpresaProduto;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.jsf.Mensagem;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
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

    private Empresa empresa;
    private String codigo;
    private BigDecimal quantidade;

    private List<EmpresaProduto> produtos;
    private EmpresaProduto produtoSelecionado;

    @PostConstruct
    private void init() {
        empresa = FacesUtil.getEmpresaUsuario();
        produtos = new ArrayList<>();
    }

    public void addProduto() {
        if (!StringUtils.isEmpty(codigo)) {
            List<Filtro> filtros = new LinkedList<>();
            filtros.add(new Filtro("empresa.id", empresa.getId()));
            if (codigo.length() > 9) {
                filtros.add(new Filtro("produto.gtin", Filtro.IGUAL, codigo));
            } else {
                String str = "0" + codigo.replaceAll("\\D", "");
                int cod = Integer.valueOf(str);
                filtros.add(new Filtro("produto.id", cod));
            }
            Object[] atributos = new Object[]{"produto.id", "produto.nome"};
            EmpresaProduto produto = repository.get(EmpresaProduto.class, filtros, atributos);

            if (produto == null) {
                Mensagem.addErrorMessage("Produto não localizado");
            } else {
                if (produtos.contains(produto)) {
                    Mensagem.addErrorMessage("Produto já informado");
                } else {
                    produto.setEstoqueVerificado(Optional.ofNullable(quantidade).orElse(BigDecimal.ZERO));
                    produto.setQuantidadeEstoque(Optional.ofNullable(quantidade).orElse(BigDecimal.ZERO));
                    produtos.add(produto);
                }

            }
        } else {
            Mensagem.addErrorMessage("Produto não localizado");
        }
    }

    public void remover() {
        produtos.remove(produtoSelecionado);
    }

    public void salvar() {


        try {
            if (produtos.isEmpty()) {
                Mensagem.addErrorMessage("Não foram adicionado produtos a lista");
            } else {

                for (EmpresaProduto produto : produtos) {
                    repository.atualizarNamedQuery("EmpresaProduto.atualizarEstoque", produto.getQuantidadeEstoque(), produto.getId());
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
}
