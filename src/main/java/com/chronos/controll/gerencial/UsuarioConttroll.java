package com.chronos.controll.gerencial;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.Colaborador;
import com.chronos.modelo.entidades.Papel;
import com.chronos.modelo.entidades.Usuario;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.jsf.Mensagem;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    private Repository<Colaborador> colaboradores;
    private String senha;


    @Override
    public ERPLazyDataModel<Usuario> getDataModel() {
        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(Usuario.class);
            dataModel.setDao(dao);
        }
        dataModel.getFiltros().clear();
        dataModel.addFiltro("id", 1, Filtro.DIFERENTE);
        return dataModel;
    }

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setDataCadastro(new Date());

    }

    @Override
    public void salvar() {

        boolean existeColaborador = dao.existeRegisro(Usuario.class, "colaborador.id", getObjeto().getColaborador().getId());
        boolean existeUsuario = dao.existeRegisro(Usuario.class, "login", getObjeto().getLogin().toLowerCase());
        if (existeColaborador) {
            Mensagem.addInfoMessage("já existe usuário definido para esse colaborador");
        } else if (existeUsuario) {
            Mensagem.addInfoMessage("já existe usuário definido com esse login");
        } else {
            if (getObjeto().getPapel().getId() == 1) {
                getObjeto().setAdministrador("S");
            } else {
                getObjeto().setAdministrador("N");
            }
            if (getObjeto().getId() == null) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                getObjeto().setSenha(encoder.encode(senha));
            }
            super.salvar();
        }

    }

    public List<Papel> getListPapel(String nome) {
        List<Papel> papeis = new ArrayList<>();

        try {
            papeis = papelRepository.getEntitys(Papel.class, "nome", nome, new Object[]{"nome"});
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return papeis;
    }

    public List<Colaborador> getListColaborador(String nome) {
        List<Colaborador> list = new ArrayList<>();
        try {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("pessoa.nome", Filtro.LIKE, nome));
            filtros.add(new Filtro("pessoa.id", Filtro.DIFERENTE, 1));
            filtros.add(new Filtro("pessoa.id", Filtro.DIFERENTE, 2));
            filtros.add(new Filtro("pessoa.colaborador", "S"));
            list = colaboradores.getEntitys(Colaborador.class, filtros, new Object[]{"pessoa.id", "pessoa.nome"});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
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
}
