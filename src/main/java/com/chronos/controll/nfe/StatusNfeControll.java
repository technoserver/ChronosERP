package com.chronos.controll.nfe;

import com.chronos.bo.nfe.NfeTransmissao;
import com.chronos.dto.ConfiguracaoEmissorDTO;
import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.NfeConfiguracao;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.cadastros.UsuarioService;
import com.chronos.util.Constantes;
import com.chronos.util.jsf.Mensagem;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 26/09/17.
 */
@Named
@ViewScoped
public class StatusNfeControll implements Serializable {

    private static final long serialVersionUID = 1L;

    private String titulo;
    private String status;
    @Inject
    private Repository<NfeConfiguracao> configuracoes;
    @Inject
    private UsuarioService userService;

    private ExternalContext context;

    @Inject
    protected FacesContext facesContext;

    @PostConstruct
    private void initid() {
        titulo = "Consulta";
        context = facesContext.getCurrentInstance().getExternalContext();
    }

    public void consultaStatus() {


        try {
            Empresa empresa = userService.getEmpresaUsuario();
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("empresa.id", empresa.getId()));

            NfeConfiguracao configuracao = configuracoes.get(NfeConfiguracao.class, filtros, new Object[]{"certificadoDigitalSenha", "webserviceAmbiente"});
            String schemas = StringUtils.isEmpty(configuracao.getCaminhoSchemas()) ? context.getRealPath(Constantes.DIRETORIO_SCHEMA_NFE) : configuracao.getCaminhoSchemas();
            configuracao.setCaminhoSchemas(schemas);

            NfeTransmissao transmissao = new NfeTransmissao(empresa);
            status = transmissao.statusServico(new ConfiguracaoEmissorDTO(configuracao));
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("", e);
        }
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
