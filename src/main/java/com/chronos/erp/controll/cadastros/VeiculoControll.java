package com.chronos.erp.controll.cadastros;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.Transportadora;
import com.chronos.erp.modelo.entidades.Veiculo;
import com.chronos.erp.repository.Repository;

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
    private Repository<Transportadora> transportadoraRepository;

    @Override
    public void salvar() {

        if (getObjeto().getTransportadora() != null) {
            getObjeto().setTipoPropriedade("1");
        } else {
            getObjeto().setTipoPropriedade("0");
        }


        super.salvar();
    }

    public List<Transportadora> getListaTransportadora(String nome) {
        List<Transportadora> list = new ArrayList<>();
        try {
            list = transportadoraRepository.getEntitys(Transportadora.class, "pessoa.nome", nome, new Object[]{"pessoa.nome"});
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
