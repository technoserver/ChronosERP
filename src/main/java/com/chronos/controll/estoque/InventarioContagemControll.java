package com.chronos.controll.estoque;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.*;
import com.chronos.repository.EstoqueRepository;
import com.chronos.repository.Repository;
import com.chronos.service.gerencial.AuditoriaService;
import com.chronos.util.Biblioteca;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Named
@ViewScoped
public class InventarioContagemControll extends AbstractControll<InventarioContagemCab> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<Produto> produtoRepository;
    @Inject
    private Repository<EmpresaProduto> empresaProdutoRepository;
    @Inject
    private EstoqueRepository estoqueRepository;
    @Inject
    private AuditoriaService auditoriaService;
    @Inject
    private Repository<ProdutoSubGrupo> produtoSubGrupoRepository;


    private InventarioContagemDet inventarioContagemDet;
    private InventarioContagemDet inventarioContagemDetSelecionado;

    private ProdutoSubGrupo produtoSubgrupo;
    private HashMap<String, String> tipoContagem;
    private HashMap<String, Integer> contagens;


    @PostConstruct
    @Override
    public void init() {
        super.init();

        tipoContagem = new HashMap<>();
        tipoContagem.put("Geral", "G");
        tipoContagem.put("Dinâmico", "D");
        tipoContagem.put("Rotativo", "R");
        tipoContagem.put("Amostragem", "A");

        contagens = new HashMap<>();
        contagens.put("01", 1);
        contagens.put("02", 2);
        contagens.put("03", 3);
    }

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setEmpresa(empresa);
        getObjeto().setListaInventarioContagemDet(new ArrayList<>());
        produtoSubgrupo = new ProdutoSubGrupo();

        Mensagem.addInfoMessage("Para definir a quantidade contado é preciso clicar na grid. após ter realizado a contagem escolhar qual contagem irá atualizar o estoque");
    }

    @Transactional
    @Override
    public void salvar() {
        try {

            efetuarCalculos();
            super.salvar();

            if (getObjeto().getEstoqueAtualizado().equals("S")) {

                for (InventarioContagemDet det : getObjeto().getListaInventarioContagemDet()) {

                    det.getProduto().setQuantidadeEstoque(det.getContagem01());

                    BigDecimal quantidade;

                    switch (det.getContagem()) {

                        case 2:
                            quantidade = det.getContagem02();
                            break;

                        case 3:
                            quantidade = det.getContagem03();
                            break;
                        default:
                            quantidade = det.getContagem01();
                            break;
                    }

                    estoqueRepository.ajustarEstoqueEmpresa(empresa.getId(), det.getProduto().getId(), quantidade);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void efetuarCalculos() {
        try {
            if (getObjeto().getListaInventarioContagemDet().isEmpty()) {
                Mensagem.addInfoMessage("Nenhum item para calcular.");
            } else {
                for (InventarioContagemDet item : getObjeto().getListaInventarioContagemDet()) {


                    BigDecimal quantidade;

                    switch (item.getContagem()) {

                        case 2:
                            quantidade = item.getContagem02();
                            break;

                        case 3:
                            quantidade = item.getContagem03();
                            break;
                        default:
                            quantidade = item.getContagem01();
                            break;
                    }

                    if (quantidade != null && item.getQuantidadeSistema() != null) {
                        //acuracidade dos registros = (registros sistema / registros contados) X 100 }
                        if (quantidade.compareTo(BigDecimal.ZERO) != 0) {
                            item.setAcuracidade(Biblioteca.multiplica(Biblioteca.divide(item.getQuantidadeSistema(), quantidade), BigDecimal.valueOf(100)));
                        } else {
                            item.setAcuracidade(BigDecimal.ZERO);
                        }
                        //divergencia dos registros = ((registros contados - registros sistema) / registros sistema) X 100 }
                        if (item.getQuantidadeSistema().compareTo(BigDecimal.ZERO) != 0) {
                            item.setDivergencia(Biblioteca.multiplica(
                                    Biblioteca.divide(Biblioteca.subtrai(quantidade, item.getQuantidadeSistema()), item.getQuantidadeSistema()),
                                    BigDecimal.valueOf(100)));
                        } else {
                            item.setDivergencia(BigDecimal.valueOf(100));
                        }

                    }

                    if (item.getQuantidadeSistema() != null) {
                        if (item.getContagem01() != null && (item.getContagem01().compareTo(item.getQuantidadeSistema()) == 0)) {
                            item.setFechadoContagem("01");
                        } else if (item.getContagem02() != null && (item.getContagem02().compareTo(item.getQuantidadeSistema()) == 0)) {
                            item.setFechadoContagem("02");
                        } else if (item.getContagem03() != null && (item.getContagem03().compareTo(item.getQuantidadeSistema()) == 0)) {
                            item.setFechadoContagem("03");
                        } else {
                            item.setFechadoContagem("XX");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro.", e);
        }
    }

    public void buscaGrupoProdutos(SelectEvent event) {
        try {
            ProdutoSubGrupo subGrupo = (ProdutoSubGrupo) event.getObject();
            List<EmpresaProduto> produtos = empresaProdutoRepository.getEntitys(EmpresaProduto.class, "produto.produtoSubGrupo", subGrupo);
            for (int i = 0; i < produtos.size(); i++) {
                InventarioContagemDet item = new InventarioContagemDet();
                item.setInventarioContagemCab(getObjeto());
                item.setProduto(produtos.get(i).getProduto());
                item.setQuantidadeSistema(produtos.get(i).getQuantidadeEstoque());
                item.setContagem(1);
                getObjeto().getListaInventarioContagemDet().add(item);
            }
            if (produtos.isEmpty()) {
                Mensagem.addInfoMessage("Nenhum produto encontrado para o grupo selecionado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro.", e);
        }
    }

    public void alterarInventarioContagem() {
        inventarioContagemDet = inventarioContagemDetSelecionado;
    }

    public void salvarInventarioContagem() {

        efetuarCalculos();
    }

    public List<ProdutoSubGrupo> getListaSubGrupo(String nome) {
        List<ProdutoSubGrupo> listaProdutoSubGrupo = new ArrayList<>();
        try {
            listaProdutoSubGrupo = produtoSubGrupoRepository.getEntitys(ProdutoSubGrupo.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaProdutoSubGrupo;
    }

    @Override
    protected Class<InventarioContagemCab> getClazz() {
        return InventarioContagemCab.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "BALANCO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    @Override
    public HashMap<String, String> getTipoContagem() {
        return tipoContagem;
    }

    public ProdutoSubGrupo getProdutoSubgrupo() {
        return produtoSubgrupo;
    }

    public void setProdutoSubgrupo(ProdutoSubGrupo produtoSubgrupo) {
        this.produtoSubgrupo = produtoSubgrupo;
    }

    public HashMap<String, Integer> getContagens() {
        return contagens;
    }
}
