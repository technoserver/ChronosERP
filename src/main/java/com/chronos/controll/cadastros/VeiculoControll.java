package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.ProprietarioVeiculo;
import com.chronos.modelo.entidades.Veiculo;
import com.chronos.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 16/06/18.
 */
@Named
@ViewScoped
public class VeiculoControll extends AbstractControll<Veiculo> implements Serializable {


    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<ProprietarioVeiculo> proprietarioRepository;

    @Override
    public void salvar() {

        if (getObjeto().getProprietarioVeiculo() != null) {
            getObjeto().setTipoPropriedade("1");
        } else {
            getObjeto().setTipoPropriedade("0");
        }

        super.salvar();
    }

    public List<ProprietarioVeiculo> getListaProprietario(String nome) {
        List<ProprietarioVeiculo> list = new ArrayList<>();
        try {
            list = proprietarioRepository.getEntitys(ProprietarioVeiculo.class, "nome", nome, atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return list;
    }

    @Override
    protected Class<Veiculo> getClazz() {
        return Veiculo.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "VEICULO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
