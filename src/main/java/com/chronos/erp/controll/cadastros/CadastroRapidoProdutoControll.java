package com.chronos.erp.controll.cadastros;

import com.chronos.erp.dto.ProdutoResumDTO;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.util.Biblioteca;
import com.chronos.erp.util.jsf.Mensagem;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

@Named
@ViewScoped
public class CadastroRapidoProdutoControll implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<ProdutoGrupo> grupoRepository;
    @Inject
    private Repository<ProdutoSubGrupo> subGrupoRepository;
    @Inject
    private Repository<UnidadeProduto> unidadeProdutoRepository;
    @Inject
    private Repository<TributGrupoTributario> grupoTributarioRepository;
    @Inject
    private Repository<ProdutoMarca> produtoMarcaRepository;

    private ProdutoResumDTO produto;

    private ProdutoGrupo grupo;


    @PostConstruct
    private void init() {
        produto = new ProdutoResumDTO();
    }

    public void abrirDialog() {
        Map<String, Object> opcoes = new HashMap<>();
        opcoes.put("modal", true);
        opcoes.put("resizable", false);
        //  opcoes.put("contentHeight", 470);


        PrimeFaces.current().dialog().openDynamic("/modulo/cadastros/produto/cadastroRapidoProduto", opcoes, null);
    }

    public void closerDialog() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }


    public void calcularValorVenda() {


        try {
            BigDecimal encargos = Optional.ofNullable(produto.getEncargos()).orElse(BigDecimal.ZERO);
            BigDecimal custo = Optional.ofNullable(produto.getCusto()).orElse(BigDecimal.ZERO);
            BigDecimal margem = Optional.ofNullable(produto.getMargemLucro()).orElse(BigDecimal.ZERO);
            BigDecimal custoTotal = Biblioteca.soma(encargos, custo);
            BigDecimal valorSugerido = Biblioteca.calcularValorPercentual(custoTotal, margem);
            BigDecimal valorVenda = Biblioteca.soma(custoTotal, valorSugerido);
            produto.setValorVenda(valorVenda);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().validationFailed();
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage(ex.getMessage());
            }

        }
    }

    public void salvar() {

        PrimeFaces.current().dialog().closeDynamic(produto);
    }

    public List<ProdutoSubGrupo> getListaSubgrupo(String nome) {
        List<ProdutoSubGrupo> listaSubgrupo = new ArrayList<>();
        try {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("nome", Filtro.LIKE, nome));
            if (grupo.getId() != null) {
                filtros.add(new Filtro("produtoGrupo.id", grupo.getId()));
            }
            listaSubgrupo = subGrupoRepository.getEntitys(ProdutoSubGrupo.class, filtros);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaSubgrupo;
    }

    public List<ProdutoGrupo> getListaGrupo(String nome) {
        List<ProdutoGrupo> listaGrupo = new ArrayList<>();
        try {

            listaGrupo = grupoRepository.getEntitys(ProdutoGrupo.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaGrupo;
    }

    public List<UnidadeProduto> getListaUnidadeProduto(String nome) {
        List<UnidadeProduto> listaUnidadeProduto = new ArrayList<>();
        try {

            listaUnidadeProduto = unidadeProdutoRepository.getEntitys(UnidadeProduto.class, "descricao", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaUnidadeProduto;
    }

    public List<TributGrupoTributario> getListaGrupoTributario(String nome) {
        List<TributGrupoTributario> listaGrupoTributario = new ArrayList<>();
        try {

            listaGrupoTributario = grupoTributarioRepository.getEntitys(TributGrupoTributario.class, "descricao", nome, new Object[]{"descricao"});
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaGrupoTributario;
    }

    public List<ProdutoMarca> getListaMarca(String nome) {
        List<ProdutoMarca> lista = new ArrayList<>();
        try {

            lista = produtoMarcaRepository.getEntitys(ProdutoMarca.class, "nome", nome, new Object[]{"nome"});
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return lista;
    }

    public ProdutoResumDTO getProduto() {
        return produto;
    }

    public void setProduto(ProdutoResumDTO produto) {
        this.produto = produto;
    }

    public ProdutoGrupo getGrupo() {
        return grupo;
    }

    public void setGrupo(ProdutoGrupo grupo) {
        this.grupo = grupo;
    }
}
