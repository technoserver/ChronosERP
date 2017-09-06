package com.chronos.controll.vendas;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.Colaborador;
import com.chronos.modelo.entidades.VendaCabecalho;
import com.chronos.modelo.entidades.VendaRomaneioEntrega;
import com.chronos.repository.Repository;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by john on 16/08/17.
 */
@Named
@ViewScoped
public class VendaRomaneioEntregaControll extends AbstractControll<VendaRomaneioEntrega> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Repository<Colaborador> colaboradores;
    private Repository<VendaCabecalho> vendas;

    private Integer numeroFatura;

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setSituacao("A");
        getObjeto().setListaVendaCabecalho(new HashSet<>());
    }

    public void incluiVenda() {
        try {
            if (numeroFatura != null) {
                VendaCabecalho venda = vendas.get(VendaCabecalho.class, "numeroFatura", numeroFatura);
                if (venda == null) {
                    Mensagem.addWarnMessage("Venda não localizada!");

                } else {
                    venda.setVendaRomaneioEntrega(getObjeto());
                    getObjeto().getListaVendaCabecalho().add(venda);
                }
            }
        } catch (Exception e) {
            Mensagem.addErrorMessage("Ocorreu um erro!", e);

        }
    }

    public List<Colaborador> getListaColaborador(String nome) {
        List<Colaborador> listaColaborador = new ArrayList<>();
        try {
            listaColaborador = colaboradores.getEntitys(Colaborador.class, "pessoa.nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaColaborador;
    }

    @Override
    protected Class<VendaRomaneioEntrega> getClazz() {
        return VendaRomaneioEntrega.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "VENDA_ROMANEIO_ENTREGA";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public Integer getNumeroFatura() {
        return numeroFatura;
    }

    public void setNumeroFatura(Integer numeroFatura) {
        this.numeroFatura = numeroFatura;
    }
}
