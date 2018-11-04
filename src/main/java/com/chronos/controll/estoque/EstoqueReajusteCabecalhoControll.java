package com.chronos.controll.estoque;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.*;
import com.chronos.repository.EstoqueRepository;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.Mensagem;
import org.springframework.util.StringUtils;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by john on 02/08/17.
 */
@Named
@ViewScoped
public class EstoqueReajusteCabecalhoControll extends AbstractControll<EstoqueReajusteCabecalho> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<Colaborador> colaboradores;
    @Inject
    private Repository<ProdutoSubGrupo> subgrupos;
    @Inject
    private Repository<EmpresaProduto> produtos;

    private ProdutoSubGrupo produtoSubgrupo;
    @Inject
    private EstoqueRepository estoqueRepository;

    private String codigo;
    private String nome;


    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setListaEstoqueReajusteDetalhe(new HashSet<>());
        getObjeto().setDataReajuste(new Date());
        getObjeto().setColaborador(new Colaborador(usuario.getIdcolaborador()));
        produtoSubgrupo = new ProdutoSubGrupo();
    }

    @Override
    public void doEdit() {
        super.doEdit();

        EstoqueReajusteCabecalho r = getDataModel().getRowData(getObjeto().getId().toString());
        setObjeto(r);
    }

    @Transactional
    @Override
    public void salvar() {
        try {
            if (getObjeto().getListaEstoqueReajusteDetalhe().isEmpty()) {
                Mensagem.addInfoMessage("Não existe itens a serem calculado");
                setTelaGrid(false);
            } else {
                efetuarCalculos();
                for (EstoqueReajusteDetalhe e : getObjeto().getListaEstoqueReajusteDetalhe()) {
                    estoqueRepository.ajustarEstoqueAndVerificadoEmpresa(empresa.getId(), e.getProduto().getId(), e.getQuantidadeReajuste());
                    estoqueRepository.atualizarPrecoProduto(e.getProduto().getId(), e.getValorReajuste());
                }
                super.salvar();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }

    }

    @Override
    public void remover() {
        Mensagem.addInfoMessage("Não é possivel realizar o procediment de exclusão. foram gerados movimento de ajuste");
    }

    public void buscaGrupoProdutos() {
        try {

            getObjeto().getListaEstoqueReajusteDetalhe().clear();
            atributos = new Object[]{"quantidadeEstoque", "produto.id", "produto.nome", "produto.valorVenda"};
            List<Filtro> filtros = new LinkedList<>();
            filtros.add(new Filtro("empresa.id", empresa.getId()));

            if (!StringUtils.isEmpty(codigo)) {
                if (codigo.length() > 9) {
                    filtros.add(new Filtro("produto.gtin", Filtro.IGUAL, codigo));
                } else {
                    codigo = "0" + codigo;
                    String str = codigo.replaceAll("\\D", "");
                    int cod = Integer.valueOf(str);
                    filtros.add(new Filtro("produto.id", cod));
                }


            } else {
                if (produtoSubgrupo.getId() != null) {
                    filtros.add(new Filtro("produto.produtoSubGrupo.id", produtoSubgrupo.getId()));
                }

                if (!StringUtils.isEmpty(nome)) {
                    filtros.add(new Filtro("produto.nome", Filtro.LIKE, nome));
                }
            }


            List<EmpresaProduto> listaProduto = produtos.getEntitys(EmpresaProduto.class, filtros, atributos);

            if (listaProduto.isEmpty()) {
                Mensagem.addInfoMessage("Nenhum produto encontrado");
            } else {
                listaProduto.stream().forEach((p) -> {
                    EstoqueReajusteDetalhe itemReajuste = new EstoqueReajusteDetalhe();
                    itemReajuste.setEstoqueReajusteCabecalho(getObjeto());
                    itemReajuste.setProduto(p.getProduto());
                    itemReajuste.setValorOriginal(p.getProduto().getValorVenda());
                    itemReajuste.setQuantidadeOriginal(p.getQuantidadeEstoque());
                    itemReajuste.setQuantidadeReajuste(p.getQuantidadeEstoque());
                    getObjeto().getListaEstoqueReajusteDetalhe().add(itemReajuste);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro.", e);
        }
    }

    public void efetuarCalculos() {
        try {
            if (getObjeto().getListaEstoqueReajusteDetalhe().isEmpty()) {
                throw new Exception("Nenhum item para calcular.");

            }
            if (getObjeto().getPorcentagem() == null) {
                throw new Exception("Percentual de reajuste não definido.");
            } else {
                String tipo = getObjeto().getTipoReajuste();
                getObjeto().getListaEstoqueReajusteDetalhe().stream().forEach(r -> {
                    r.setValorReajuste(calcularReajuste(r.getValorOriginal(), tipo));
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro.", e);
        }
    }

    private BigDecimal calcularReajuste(BigDecimal valorOriginal, String tipo) {
        if (!Optional.ofNullable(valorOriginal).isPresent()) {
            return BigDecimal.ZERO;
        }
        BigDecimal reajuste = tipo.equals("A")
                ? valorOriginal.multiply(BigDecimal.ONE.add(getObjeto().getPorcentagem().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_DOWN)))
                : valorOriginal.multiply(BigDecimal.ONE.subtract(getObjeto().getPorcentagem().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_DOWN)));

        return reajuste;
    }

    public List<Colaborador> getListaColaborador(String nome) {
        List<Colaborador> listaColaborador = new ArrayList<>();
        try {
            listaColaborador = colaboradores.getEntitys(Colaborador.class, "pessoa.nome", nome, atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaColaborador;
    }

    public List<ProdutoSubGrupo> getListaSubGrupo(String nome) {
        List<ProdutoSubGrupo> listaProdutoSubGrupo = new ArrayList<>();
        try {
            atributos = null;
            listaProdutoSubGrupo = subgrupos.getEntitys(ProdutoSubGrupo.class, "nome", nome, atributos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProdutoSubGrupo;
    }

    @Override
    protected Class<EstoqueReajusteCabecalho> getClazz() {
        return EstoqueReajusteCabecalho.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "ESTOQUE_REAJUSTE";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public ProdutoSubGrupo getProdutoSubgrupo() {
        return produtoSubgrupo;
    }

    public void setProdutoSubgrupo(ProdutoSubGrupo produtoSubgrupo) {
        this.produtoSubgrupo = produtoSubgrupo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
