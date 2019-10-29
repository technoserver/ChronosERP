package com.chronos.erp.controll.mdfe;

import br.inf.portalfiscal.mdfe.schema_300.evEncMDFe.TRetEvento;
import com.chronos.erp.modelo.entidades.Empresa;
import com.chronos.erp.modelo.entidades.Municipio;
import com.chronos.erp.modelo.enuns.Estados;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.service.comercial.MdfeService;
import com.chronos.erp.util.jsf.FacesUtil;
import com.chronos.erp.util.jsf.Mensagem;
import com.chronos.transmissor.exception.EmissorException;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Named
@ViewScoped
public class MdfeEncerrar implements Serializable {


    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<Municipio> municipioRepository;

    @Inject
    private MdfeService service;
    private Empresa empresa;
    private String chave;
    private String protocolo;
    private String uf = "AL";
    private Municipio municipio;

    @PostConstruct
    private void init() {
        empresa = FacesUtil.getEmpresaUsuario();
    }

    public void encerrarMdfe() {

        try {


            TRetEvento retorno = service.encerrar(chave, empresa.getCnpj(), protocolo,
                    municipio.getCodigoIbge().toString().substring(0, 2), municipio.getCodigoIbge().toString());

            if (retorno.getInfEvento().getCStat().equals("135")) {
                Mensagem.addInfoMessage("MDF-e Encerrado com sucesso");
            } else {
                Mensagem.addErrorMessage(retorno.getInfEvento().getXMotivo());
            }

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Erro ao buscar MDF-e não encerrados", ex);
            } else if (ex instanceof EmissorException) {
                Mensagem.addErrorMessage("Erro ao buscar MDF-e não encerrados", ex);
            } else {
                throw new RuntimeException("", ex);
            }
        }
    }

    public List<Estados> getEstado() {
        List<Estados> estados = new ArrayList<>();
        if (estados.isEmpty()) {
            estados = new LinkedList<>();
            estados.addAll(Arrays.asList(Estados.values()));
        }
        return estados;
    }

    public List<Municipio> getMunicipios(String nome) {
        List<Municipio> cidades = new LinkedList<>();
        try {
            List<Filtro> filtros = new LinkedList<>();
            filtros.add(new Filtro("uf.sigla", uf));
            filtros.add(new Filtro("nome", Filtro.LIKE, nome));
            cidades = municipioRepository.getEntitys(Municipio.class, filtros, new Object[]{"nome", "codigoIbge"});
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cidades;
    }

    public void instanciaCidade() {
        municipio = null;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }
}
