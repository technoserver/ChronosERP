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
import java.util.Optional;

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


        Usuario usuario = repository.get(Usuario.class, "colaborador.id", user.getColaborador().getId(), new Object[]{"login", "senha"});


        if (usuario != null && !usuario.equals(user)) {
            throw new ChronosException("já existe usuário definido para esse colaborador");
        }


        usuario = repository.get(Usuario.class, "login", user.getLogin(), new Object[]{"login", "senha"});


        if (usuario != null && !usuario.equals(user)) {
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

        if (empresas != null && !empresas.isEmpty()) {
            definirEmpresa(user.getColaborador().getPessoa(), empresas);
        }

        if (user.getId() == null) {
            definirUsuarioTenant(user);
        }

        user = repository.atualizar(user);
        return user;
    }

    public void atualizarSenha(Usuario user, String novaSenha) throws ChronosException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setSenha(encoder.encode(novaSenha));

        Optional<UsuarioTenant> tenantOptional = tenantRepository.getUserTenant(user.getLogin());

        if (!tenantOptional.isPresent()) {
            throw new ChronosException("Usuário cadastrado mais sem permissão de acesso ao sistema");
        }
        UsuarioTenant usuarioTenant = tenantOptional.get();
        usuarioTenant.setSenha(user.getSenha());
        tenantRepository.salvar(usuarioTenant);

        repository.atualizar(user);

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

    private void definirUsuarioTenant(Usuario user) {
        Tenant tenant = FacesUtil.getTenantId();

        UsuarioTenant usertenat = new UsuarioTenant();
        usertenat.setLogin(user.getLogin());
        usertenat.setSenha(user.getSenha());

        usertenat.setTenant(tenant);

        tenantRepository.salvar(usertenat);
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
