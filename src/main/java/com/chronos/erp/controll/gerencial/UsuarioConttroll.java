package com.chronos.erp.controll.gerencial;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.controll.ERPLazyDataModel;
import com.chronos.erp.dto.UsuarioDTO;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.repository.ColaboradorRepository;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.service.cadastros.UsuarioService;
import com.chronos.erp.util.jsf.FacesUtil;
import com.chronos.erp.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by john on 04/12/17.
 */
@Named
@ViewScoped
public class UsuarioConttroll extends AbstractControll<Usuario> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<Papel> papelRepository;
    @Inject
    private ColaboradorRepository colaboradorRepository;

    @Inject
    private Repository<EmpresaPessoa> empresaPessoaRepository;

    @Inject
    private Repository<Empresa> empresaRepository;

    private List<Empresa> empresas;
    private List<Empresa> empresasSelecionada;

    private List<EmpresaPessoa> listEmpresaPessoa;

    private String senha;

    private EmpresaPessoa empresaPessoa;

    @Inject
    private UsuarioService service;

    private String novaSenha;

    @Override
    public ERPLazyDataModel<Usuario> getDataModel() {
        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(Usuario.class);
            dataModel.setDao(dao);
        }
        dataModel.getFiltros().clear();
        return dataModel;
    }

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setDataCadastro(new Date());
        empresas = new ArrayList<>();

    }

    @Override
    public void doEdit() {
        super.doEdit();
        List<Filtro> filtros = new ArrayList<>();

    }

    @Override
    public void salvar() {

        try {
            if (getObjeto().getId() == null) {
                service.salvar(getObjeto(), senha, empresasSelecionada);
                Mensagem.addInfoMessage("Dados salvo com sucesso");
            } else {
                String adm = getObjeto().getPapel().getAcessoCompleto() != null && getObjeto().getPapel().getAcessoCompleto().equals("S")
                        ? "S" : "N";
                getObjeto().setAdministrador(adm);
                super.salvar();
            }

            setTelaGrid(true);

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("erro ao salvar o usuário", ex);
            }
        }

    }

    public void atualizarSenha() {
        try {
            service.atualizarSenha(getObjetoSelecionado(), novaSenha);
            Mensagem.addInfoMessage("senha atualizada");
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("erro ao atualizar senha", ex);
            }
        }
    }

    @Override
    public void remover() {
        UsuarioDTO user = FacesUtil.getUsuarioSessao();
        if (user.getId() == getObjetoSelecionado().getId()) {
            Mensagem.addInfoMessage("Exclusão do usuario logado não permitida");
            return;
        }
        super.remover();
    }

    public List<Papel> getListPapel(String nome) {
        List<Papel> papeis = new ArrayList<>();

        try {
            papeis = papelRepository.getEntitys(Papel.class, "nome", nome, new Object[]{"nome", "acessoCompleto"});
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return papeis;
    }

    public List<Colaborador> getListColaborador(String nome) {
        List<Colaborador> list = new ArrayList<>();
        try {

            list = colaboradorRepository.getColaboradoresEmpresaByNome(nome);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public void definirEmpresas() {
        empresas = getListEmpresas();
    }

    public List<Empresa> getListEmpresas() {
        List<Filtro> filtros = new ArrayList<>();

        empresas = new ArrayList<>();


        filtros.add(new Filtro("empresaPrincipal", "S"));
        filtros.add(new Filtro("pessoa.id", getObjeto().getColaborador().getPessoa().getId()));
        empresaPessoa = empresaPessoaRepository.get(EmpresaPessoa.class, filtros, new Object[]{"empresa.id", "empresa.razaoSocial"});
        filtros.clear();
        filtros.add(new Filtro("id", Filtro.DIFERENTE, empresaPessoa.getEmpresa().getId()));


        empresas = empresaRepository.getEntitys(Empresa.class, filtros, new Object[]{"razaoSocial"});
        return empresas;
    }

    public boolean getPodeAlterarSenha() {
        UsuarioDTO user = FacesUtil.getUsuarioSessao();
        return getObjeto().getId() == null || user.getId().equals(getObjeto().getId());
    }

    @Override
    protected Class<Usuario> getClazz() {
        return Usuario.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "USUARIO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<Empresa> getEmpresas() {
        return empresas;
    }


    public List<Empresa> getEmpresasSelecionada() {
        return empresasSelecionada;
    }

    public void setEmpresasSelecionada(List<Empresa> empresasSelecionada) {
        this.empresasSelecionada = empresasSelecionada;
    }

    public boolean isExisteEmpresa() {
        return empresas != null && !empresas.isEmpty();
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }
}
