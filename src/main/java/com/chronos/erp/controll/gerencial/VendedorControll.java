package com.chronos.erp.controll.gerencial;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.Colaborador;
import com.chronos.erp.modelo.entidades.ComissaoPerfil;
import com.chronos.erp.modelo.entidades.Vendedor;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 23/08/17.
 */
@Named
@ViewScoped
public class VendedorControll extends AbstractControll<Vendedor> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<Colaborador> colaboradores;
    @Inject
    private Repository<ComissaoPerfil> perfis;


    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setGerente('N');
    }

    @Override
    public void salvar() {
        getObjeto().setMetaVendas(getObjeto().getMetaVendas() != null ? getObjeto().getMetaVendas() : BigDecimal.ZERO);
        getObjeto().setComissao(getObjeto().getComissao() != null ? getObjeto().getComissao() : BigDecimal.ZERO);
        boolean existeColaborador = getObjeto().getId() == null && dao.existeRegisro(Vendedor.class, "colaborador.id", getObjeto().getColaborador().getId());

        if (existeColaborador) {
            Mensagem.addInfoMessage("Colaborador já definido como vendendor");
        } else {
            super.salvar();
        }

    }

    public List<Colaborador> getListColaborador(String nome) {
        List<Colaborador> list = new ArrayList<>();
        try {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("pessoa.nome", Filtro.LIKE, nome));
            filtros.add(new Filtro("pessoa.id", Filtro.DIFERENTE, 1));
            filtros.add(new Filtro("pessoa.colaborador", "S"));
            list = colaboradores.getEntitys(Colaborador.class, filtros, new Object[]{"pessoa.id", "pessoa.nome"});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public List<ComissaoPerfil> getListPerfil(String nome) {
        List<ComissaoPerfil> list = new ArrayList<>();
        try {
            list = perfis.getEntitys(ComissaoPerfil.class, "nome", nome);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    protected Class<Vendedor> getClazz() {
        return Vendedor.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "VENDEDOR";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
