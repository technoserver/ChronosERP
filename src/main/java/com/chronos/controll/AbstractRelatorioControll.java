package com.chronos.controll;

import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.enuns.Estados;
import com.chronos.service.cadastros.UsuarioService;
import com.chronos.util.jsf.Mensagem;
import com.chronos.util.report.ExecutorRelatorio;
import org.hibernate.Session;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
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
    @Inject
    private UsuarioService userService;

    protected Map<String, Object> parametros;

    protected Empresa empresa;

    @PostConstruct
    protected void init() {
        this.empresa = getEmpresaUsuario();
    }


    public List<String> getEstado() {
        List<String> estados = new ArrayList<>();
        List<Estados> ufs = Arrays.asList(Estados.values());
        ufs.stream().forEach(e -> {
            estados.add(e.getCodigo());
        });
        estados.add(0, "Selecione");
        return estados;
    }

    protected void executarRelatorio(String diretorioRelatorio, String nomeRelatorio, String nomeArquivoSaida) {

        ExecutorRelatorio executor = new ExecutorRelatorio(diretorioRelatorio, nomeRelatorio, response, parametros, nomeArquivoSaida);

        Session session = em.unwrap(Session.class);
        session.doWork(executor);

        if (executor.isRelatorioGerado()) {
            facesContext.responseComplete();
        } else {
            Mensagem.addErrorMessage("A execução do relatório não retornou dados.");
        }
    }

    protected Empresa getEmpresaUsuario() {

        return userService.getEmpresaUsuario();
    }



    protected String retornaValorPadrao(String valor) {
        return StringUtils.isEmpty(valor) ? "%" : "%" + valor + "%";
    }

}
