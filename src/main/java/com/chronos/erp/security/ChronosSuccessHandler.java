package com.chronos.erp.security;


import com.chronos.erp.dto.UsuarioDTO;
import com.chronos.erp.modelo.entidades.AdmModulo;
import com.chronos.erp.modelo.entidades.AdmParametro;
import com.chronos.erp.modelo.entidades.Papel;
import com.chronos.erp.modelo.entidades.RestricaoSistema;
import com.chronos.erp.modelo.tenant.Tenant;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.repository.UsuarioRepository;
import com.chronos.erp.util.cdi.CDIServiceLocator;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChronosSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    private UsuarioSistema user;
    private RestricaoSistema restricaoSistema;
    private AdmParametro parametro;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            user = (UsuarioSistema) authentication.getPrincipal();
            Tenant tenant = new Tenant(user.getUsuario().getIdtenant(), user.getUsuario().getNomeTenant());
            request.getSession().setAttribute("tenantId", tenant);
            UsuarioDTO usuarioDTO = definirPermissoes(user);
            request.getSession().setAttribute("userChronosERP", usuarioDTO);
            request.getSession().setAttribute("restricoes", restricaoSistema);
            request.getSession().setAttribute("paramChronosERP", parametro);

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onAuthenticationSuccess(request, response, authentication);

    }


    private UsuarioDTO definirPermissoes(UsuarioSistema usuarioSistema) {

        try {
            Set<SimpleGrantedAuthority> authorities = new HashSet<>();

            UsuarioRepository dao = CDIServiceLocator.getBean(UsuarioRepository.class);

            UsuarioDTO usr = dao.getUsuarioDTO(usuarioSistema.getUsername());

            if (usr == null) {
                throw new UsernameNotFoundException("Usuário e/ou senha incorretos");
            }

            usr.setStatusTenant(usuarioSistema.getUsuario().getStatus());
            usr.setTenant(usuarioSistema.getUsuario().getNomeTenant());
            usr.setIdtenant(usr.getIdtenant());


            Papel papel = dao.getPapelFuncao(usr.getId());

            if (papel.getAcessoCompleto().equals("S") || usr.getAdministrador().equals("S")) {

                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }


            if (!usr.getStatusTenant().equals("A")) {
                authorities.add(new SimpleGrantedAuthority("ROLE_INADIPLENTE"));
            } else {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADIMPLENTE"));
            }


            papel.getListaPapelFuncao().forEach((p) -> {
                if (p.getPodeConsultar() != null && p.getPodeConsultar().equals("S")) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + p.getFuncao().getFormulario() + "_CONSULTAR"));
                }
                if (p.getPodeInserir() != null && p.getPodeInserir().equals("S")) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + p.getFuncao().getFormulario() + "_INSERIR"));
                }
                if (p.getPodeAlterar() != null && p.getPodeAlterar().equals("S")) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + p.getFuncao().getFormulario() + "_ALTERAR"));
                }
                if (p.getPodeExcluir() != null && p.getPodeExcluir().equals("S")) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + p.getFuncao().getFormulario() + "_EXCLUIR"));
                }
            });

            Repository<AdmModulo> admModuloRepository = CDIServiceLocator.getBean(Repository.class);
            List<AdmModulo> modulos = new ArrayList<>();
            if (admModuloRepository != null) {
                modulos = admModuloRepository.getEntitys(AdmModulo.class, "ativo", "S");
            }
            modulos.forEach(m -> authorities.add(new SimpleGrantedAuthority("ROLE_" + m.getNome())));


            Repository<RestricaoSistema> restricaoSistemaRepository = CDIServiceLocator.getBean(Repository.class);

            RestricaoSistema restricao = restricaoSistemaRepository.get(RestricaoSistema.class, "usuario.id", usr.getId());

            restricaoSistema = restricao != null ? restricao : new RestricaoSistema();

            Repository<AdmParametro> parametroRepository = CDIServiceLocator.getBean(Repository.class);

            parametro = parametroRepository.getEntitys(AdmParametro.class).stream().findFirst().orElse(null);

            Authentication authentication = new UsernamePasswordAuthenticationToken(usr.getLogin(), usr.getSenha(), authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);


            return usr;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("erro ao definir as permissões");
        }

    }
}
