package com.chronos.service.configuracao;

import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.NotaFiscalTipo;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.jsf.FacesUtil;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by john on 20/06/18.
 */
public class NotaFiscalService implements Serializable {

    private static final long serialVersionUID = 1L;

    private Empresa empresa;
    @Inject
    private Repository<NotaFiscalTipo> repository;

    @PostConstruct
    private void init() {
        empresa = FacesUtil.getEmpresaUsuario();

    }

    public String definirChaveAcessoNfe() {

        return "";
    }


    public NotaFiscalTipo getNotaFicalTipo(String modelo) throws Exception {

        List<Filtro> filtros = new LinkedList<>();
        filtros.add(new Filtro(Filtro.AND, "empresa.id", Filtro.IGUAL, empresa.getId()));
        filtros.add(new Filtro(Filtro.AND, "notaFiscalModelo.codigo", Filtro.IGUAL, modelo));
        Object[] atributos = new String[]{"serie", "ultimoNumero", "notaFiscalModelo.id"};
        NotaFiscalTipo notaFiscalTipo = repository.get(NotaFiscalTipo.class, filtros, atributos);
        if (notaFiscalTipo == null) {
            throw new Exception("Configuração de numero fiscal para o modelo :" + modelo + " não definida");
        }

        notaFiscalTipo.setUltimoNumero(notaFiscalTipo.proximoNumero());

        return notaFiscalTipo;
    }
}
