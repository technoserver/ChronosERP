package com.chronos.erp.controll;

import com.chronos.erp.modelo.entidades.Empresa;
import com.chronos.erp.modelo.enuns.Estados;
import com.chronos.erp.service.cadastros.UsuarioService;
import com.chronos.erp.util.jsf.FacesUtil;
import com.chronos.erp.util.jsf.Mensagem;
import com.chronos.erp.util.report.ExecutorRelatorio;
import org.hibernate.Session;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.*;

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
    protected Map<String, String> tiposRelatorio;
    protected Empresa empresa;
    @Inject
    protected UsuarioService userService;

    protected String tipoRelatorio = "PDF";

    @PostConstruct
    protected void init() {
        this.empresa = FacesUtil.getEmpresaUsuario();
        this.tiposRelatorio = new HashMap<>();
        tiposRelatorio.put("PDF", "PDF");
        tiposRelatorio.put("XLS", "XLS");
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

        ExecutorRelatorio executor = new ExecutorRelatorio(diretorioRelatorio, nomeRelatorio, response, parametros, nomeArquivoSaida, tipoRelatorio);

        Session session = em.unwrap(Session.class);
        session.doWork(executor);

        if (executor.isRelatorioGerado()) {
            facesContext.responseComplete();
        } else {
            Mensagem.addErrorMessage("A execução do relatório não retornou dados.");
        }
    }


    protected String retornaValorPadrao(String valor) {
        return StringUtils.isEmpty(valor) ? "%" : "%" + valor + "%";
    }


    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getTipoRelatorio() {
        return tipoRelatorio;
    }

    public void setTipoRelatorio(String tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }

    public Map<String, String> getTiposRelatorio() {
        return tiposRelatorio;
    }
}
