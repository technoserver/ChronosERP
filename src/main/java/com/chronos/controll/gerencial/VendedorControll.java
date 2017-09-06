package com.chronos.controll.gerencial;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.Colaborador;
import com.chronos.modelo.entidades.ComissaoPerfil;
import com.chronos.modelo.entidades.Vendedor;
import com.chronos.repository.Repository;

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
public class VendedorControll extends AbstractControll<Vendedor> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<Colaborador> colaboradores;
    @Inject
    private Repository<ComissaoPerfil> perfis;



    public List<Colaborador> getListColaborador(String nome){
        List<Colaborador> list = new ArrayList<>();
        try{
            list = colaboradores.getEntitys(Colaborador.class,"pessoa.nome",nome);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }

    public List<ComissaoPerfil> getListPerfil (String nome){
        List<ComissaoPerfil> list = new ArrayList<>();
        try{
            list = perfis.getEntitys(ComissaoPerfil.class,"nome",nome);
        }catch (Exception ex){
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
