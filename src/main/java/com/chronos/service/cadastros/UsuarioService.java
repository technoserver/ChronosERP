package com.chronos.service.cadastros;

import com.chronos.dto.UsuarioDTO;
import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.PdvMovimento;
import com.chronos.modelo.entidades.Usuario;
import com.chronos.modelo.tenant.Tenant;
import com.chronos.modelo.tenant.UsuarioTenant;
import com.chronos.repository.Repository;
import com.chronos.repository.TenantRepository;
import com.chronos.service.ChronosException;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.FacesUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.inject.Inject;
import java.io.Serializable;

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
    private TenantRepository tenantRepository;


    @Transactional
    public Usuario salvar(Usuario user, String senha) throws ChronosException {


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
}
