package com.chronos.service.cadastros;

import com.chronos.dto.UsuarioDTO;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.tenant.Tenant;
import com.chronos.modelo.tenant.UsuarioTenant;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.repository.TenantRepository;
import com.chronos.service.ChronosException;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.FacesUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 19/09/17.
 */
public class UsuarioService implements Serializable {

    private static final long serialVersionUID = 1L;

    private Empresa empresa;
    private PdvMovimento movimento;

    @Inject
    private Repository<Usuario> repository;
    @Inject
    private Repository<EmpresaPessoa> empresaPessoaRepository;


    @Inject
    private TenantRepository tenantRepository;


    @Transactional
    public Usuario salvar(Usuario user, String senha, List<Empresa> empresas) throws ChronosException {


        boolean existeColaborador = repository.existeRegisro(Usuario.class, "colaborador.id", user.getColaborador().getId());


        Integer id = user.getId();
        if (existeColaborador) {
            throw new ChronosException("já existe usuário definido para esse colaborador");

        }
        boolean existeUsuarioTenant = tenantRepository.existeUsuario(user.getLogin().toLowerCase());
        if (existeUsuarioTenant) {
            throw new ChronosException("já existe usuário definido com esse login");

        }
        boolean existeUsuario = repository.existeRegisro(Usuario.class, "login", user.getLogin().toLowerCase());
        if (existeUsuario) {
            throw new ChronosException("já existe usuário definido com esse login");

        }

        if (user.getPapel().getId() == 1) {
            user.setAdministrador("S");
        } else {
            user.setAdministrador("N");
        }
        if (user.getId() == null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setSenha(encoder.encode(senha));
        }

        definirEmpresa(user.getColaborador().getPessoa(), empresas);
        if (id == null) {
            Tenant tenant = FacesUtil.getTenantId();

            UsuarioTenant usertenat = new UsuarioTenant();
            usertenat.setLogin(user.getLogin());
            usertenat.setSenha(user.getSenha());

            usertenat.setTenant(tenant);

            tenantRepository.salvar(usertenat);
        }

        user = repository.atualizar(user);
        return user;
    }

    public UsuarioDTO getUsuarioLogado() {


        UsuarioDTO usuario = FacesUtil.getUsuarioSessao();

        return usuario;
    }

    public Empresa getEmpresaUsuario() {
        empresa = null;

        empresa = getUsuarioLogado().getEmpresa();
        return empresa;
    }

    private void definirEmpresa(Pessoa pessoa, List<Empresa> empresas) {
        List<EmpresaPessoa> empresaPessoas = new ArrayList<>();

        for (Empresa emp : empresas) {
            EmpresaPessoa empresaPessoa = new EmpresaPessoa();
            empresaPessoa.setEmpresa(emp);
            empresaPessoa.setPessoa(pessoa);
            empresaPessoa.setResponsavelLegal("N");
            empresaPessoa.setEmpresaPrincipal("N");

            empresaPessoas.add(empresaPessoa);

            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("empresa.id", emp.getId()));
            filtros.add(new Filtro("empresaPrincipal", "N"));
            filtros.add(new Filtro("pessoa.id", pessoa.getId()));

            empresaPessoaRepository.excluir(EmpresaPessoa.class, filtros);
        }

        empresaPessoaRepository.salvar(empresaPessoas);
    }
}
