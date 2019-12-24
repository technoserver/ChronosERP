package com.chronos.erp.controll.estoque;

import com.chronos.erp.dto.UsuarioDTO;
import com.chronos.erp.modelo.entidades.Empresa;
import com.chronos.erp.modelo.entidades.EmpresaPessoa;
import com.chronos.erp.modelo.entidades.EstoqueProdutoMovimentacao;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.util.jsf.FacesUtil;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named
@ViewScoped
public class MovimentoProdutoControll implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<EstoqueProdutoMovimentacao> repository;
    @Inject
    private Repository<Empresa> empresaRepository;
    @Inject
    private Repository<EmpresaPessoa> empresaPessoaRepository;

    private List<EstoqueProdutoMovimentacao> movimentos;
    private List<Empresa> listaEmpresas;
    private List<Empresa> empresasSelecionada;
    private Empresa empresa;
    private UsuarioDTO usuario;
    private Date dataInicial;
    private Date dataFinal;
    private String tipo;
    private String entradaSaida;
    private String produto;
    private int idempresa;

    @PostConstruct
    public void init() {

        empresa = FacesUtil.getEmpresaUsuario();
        usuario = FacesUtil.getUsuarioSessao();
        pesquisarEmpresas();
    }

    public void pesquisarEmpresas() {


        listaEmpresas = new ArrayList<>();
        empresasSelecionada = new ArrayList<>();

        if (usuario.getAdministrador().equals("S")) {
            List<Empresa> empresas = empresaRepository.getEntitys(Empresa.class, new Object[]{"razaoSocial"});

            if (!empresas.isEmpty() && empresas.size() > 1) {
                empresas.forEach(e -> {
                    listaEmpresas.add(e);
                });
            }


        } else {
            List<EmpresaPessoa> empresaPessoas = empresaPessoaRepository.getEntitys(EmpresaPessoa.class, "pessoa.id", usuario.getIdpessoa(), new Object[]{"empresa.id, empresa.razaoSocial"});

            if (!empresaPessoas.isEmpty() && empresaPessoas.size() > 1) {

                for (EmpresaPessoa emp : empresaPessoas) {
                    listaEmpresas.add(emp.getEmpresa());
                }

            }
        }
    }


    public void pesquisar() {
        List<Filtro> filtros = new ArrayList<>();

        if (dataInicial != null) {
            filtros.add(new Filtro("dataMovimento", Filtro.MAIOR_OU_IGUAL, dataInicial));
        }

        if (dataFinal != null) {
            filtros.add(new Filtro("dataMovimento", Filtro.MENOR_OU_IGUAL, dataFinal));
        }

        if (!StringUtils.isEmpty(produto)) {
            filtros.add(new Filtro("empresaProduto.produto.nome", Filtro.LIKE, produto));
        }

        idempresa = idempresa == 0 ? empresa.getId() : idempresa;
        filtros.add(new Filtro("empresaProduto.empresa.id", idempresa));
        movimentos = repository.getEntitys(EstoqueProdutoMovimentacao.class, filtros);

    }


    public List<EstoqueProdutoMovimentacao> getMovimentos() {
        return movimentos;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEntradaSaida() {
        return entradaSaida;
    }

    public void setEntradaSaida(String entradaSaida) {
        this.entradaSaida = entradaSaida;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public int getIdempresa() {
        return idempresa;
    }

    public void setIdempresa(int idempresa) {
        this.idempresa = idempresa;
    }

    public List<Empresa> getListaEmpresas() {
        return listaEmpresas;
    }
}
