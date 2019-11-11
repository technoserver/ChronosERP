package com.chronos.erp.controll.gerencial;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.ComissaoObjetivo;
import com.chronos.erp.modelo.entidades.ComissaoPerfil;
import com.chronos.erp.modelo.entidades.Produto;
import com.chronos.erp.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 23/08/17.
 */
@Named
@ViewScoped
public class ComissaoObjetivoControll extends AbstractControll<ComissaoObjetivo> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<Produto> produtos;
    @Inject
    private Repository<ComissaoPerfil> perfis;

    public List<Produto> getListaProduto(String nome) {
        List<Produto> listaProduto = new ArrayList<>();
        try {
            listaProduto = produtos.getEntitys(Produto.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaProduto;
    }

    public List<ComissaoPerfil> getListaComissaoPerfil(String nome) {
        List<ComissaoPerfil> listaComissaoPerfil = new ArrayList<>();
        try {
            listaComissaoPerfil = perfis.getEntitys(ComissaoPerfil.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaComissaoPerfil;
    }


    @Override
    protected Class<ComissaoObjetivo> getClazz() {
        return ComissaoObjetivo.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "COMISSAO_OBJETIVO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
