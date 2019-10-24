package com.chronos.controll.cadastros;

import com.chronos.dto.ProdutoResumDTO;
import com.chronos.modelo.entidades.ProdutoGrupo;
import com.chronos.modelo.entidades.ProdutoSubGrupo;
import com.chronos.modelo.entidades.TributGrupoTributario;
import com.chronos.modelo.entidades.UnidadeProduto;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
