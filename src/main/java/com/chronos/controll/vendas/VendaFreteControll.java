package com.chronos.controll.vendas;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.Transportadora;
import com.chronos.modelo.entidades.VendaCabecalho;
import com.chronos.modelo.entidades.VendaFrete;
import com.chronos.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 16/08/17.
 */
@Named
@ViewScoped
public class VendaFreteControll extends AbstractControll<VendaFrete> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Repository<Transportadora> transportadoras;
    private Repository<VendaCabecalho> vendas;

    public List<Transportadora> getListaTransportadora(String nome) {
        List<Transportadora> listaTransportadora = new ArrayList<>();
        try {
            listaTransportadora = transportadoras.getEntitys(Transportadora.class,"pessoa.nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTransportadora;
    }

    public List<VendaCabecalho> getListaVendaCabecalho(String nome) {
        List<VendaCabecalho> listaVendaCabecalho = new ArrayList<>();
        try {
            listaVendaCabecalho = vendas.getEntitys(VendaCabecalho.class,"numeroFatura", Integer.valueOf(nome));
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaVendaCabecalho;
    }

    @Override
    protected Class<VendaFrete> getClazz() {
        return VendaFrete.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "VENDA_FRETE";
    }
}
