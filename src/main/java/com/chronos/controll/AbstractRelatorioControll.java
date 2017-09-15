package com.chronos.controll;

import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.Usuario;
import com.chronos.modelo.entidades.enuns.Estados;
import com.chronos.security.UsuarioLogado;
import com.chronos.security.UsuarioSistema;
import com.chronos.util.jsf.Mensagem;
import com.chronos.util.report.ExecutorRelatorio;
import org.hibernate.Session;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.StringUtils;

import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by john on 14/09/17.
 */
public class AbstractRelatorioControll implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    protected FacesContext facesContext;

    @Inject
    protected HttpServletResponse response;

    @Inject
    protected EntityManager em;

    protected Map<String, Object> parametros;

    protected Empresa empresa;


    public List<String> getEstado() {
        List<String> estados = new ArrayList<>();
        List<Estados> ufs = Arrays.asList(Estados.values());
        ufs.stream().forEach(e -> {
            estados.add(e.getCodigo());
        });
        estados.add(0, "Selecione");
        return estados;
    }

    protected void executarRelatorio(String caminhoRelatorio, String nomeArquivoSaida) {

        ExecutorRelatorio executor = new ExecutorRelatorio(caminhoRelatorio, response, parametros, nomeArquivoSaida);

        Session session = em.unwrap(Session.class);
        session.doWork(executor);

        if (executor.isRelatorioGerado()) {
            facesContext.responseComplete();
        } else {
            Mensagem.addErrorMessage("A execução do relatório não retornou dados.");
        }
    }

    protected Empresa getEmpresaUsuario() {
        empresa = null;

        getUsuarioLogado().getColaborador().getPessoa().getListaEmpresa().stream().forEach((e) -> {
            empresa = e;
        });
        return empresa;
    }

    @Produces
    @UsuarioLogado
    private Usuario getUsuarioLogado() {
        UsuarioSistema user = null;

        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
        Usuario usuario = null;
        if (auth != null && auth.getPrincipal() != null) {
            user = (UsuarioSistema) auth.getPrincipal();
            usuario = user.getUsuario();
        }

        return usuario;
    }

    protected String retornaValorPadrao(String valor) {
        return StringUtils.isEmpty(valor) ? "%" : "%" + valor + "%";
    }

}
