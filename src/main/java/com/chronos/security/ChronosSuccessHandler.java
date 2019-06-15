package com.chronos.security;


import com.chronos.dto.UsuarioDTO;
import com.chronos.modelo.entidades.AdmModulo;
import com.chronos.modelo.entidades.Papel;
import com.chronos.modelo.entidades.RestricaoSistema;
import com.chronos.modelo.tenant.Tenant;
import com.chronos.repository.Repository;
import com.chronos.repository.UsuarioRepository;
import com.chronos.util.cdi.CDIServiceLocator;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChronosSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {



    private UsuarioSistema user;
    private BigDecimal desconto;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            user = (UsuarioSistema) authentication.getPrincipal();
            Tenant tenant = new Tenant(user.getUsuario().getIdtenant(), user.getUsuario().getNomeTenant());
            request.getSession().setAttribute("tenantId", tenant);
            UsuarioDTO usuarioDTO = definirPermissoes(user);
            request.getSession().setAttribute("userChronosERP", usuarioDTO);
            request.getSession().setAttribute("desc", desconto);

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onAuthenticationSuccess(request, response, authentication);

    }


    private UsuarioDTO definirPermissoes(UsuarioSistema usuarioSistema) {

        try {
            Set<SimpleGrantedAuthority> authorities = new HashSet<>();

            UsuarioRepository dao = CDIServiceLocator.getBean(UsuarioRepository.class);
            assert dao != null;
            UsuarioDTO user = dao.getUsuarioDTO(usuarioSistema.getUsername());
            Papel papel = dao.getPapelFuncao(user.getId());

            if (papel.getAcessoCompleto().equals("S") || user.getAdministrador().equals("S")) {

                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }


            if (!usuarioSistema.getUsuario().getStatus().equals("A")) {
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

            RestricaoSistema restricao = restricaoSistemaRepository.get(RestricaoSistema.class, "usuario.id", user.getId());

            desconto = restricao != null && restricao.getDescontoVenda() != null ? restricao.getDescontoVenda() : BigDecimal.ZERO;

            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getLogin(), user.getSenha(), authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);


            return user;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("erro ao definir as permissões");
        }

    }
}
