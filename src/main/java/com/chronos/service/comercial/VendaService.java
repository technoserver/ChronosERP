package com.chronos.service.comercial;

import com.chronos.modelo.entidades.VendaCabecalho;
import com.chronos.modelo.entidades.VendaComissao;
import com.chronos.repository.Repository;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by john on 06/09/17.
 */
public class VendaService implements Serializable {


    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<VendaComissao> comissoes;


    private void gerarComissao(VendaCabecalho venda) {
        VendaComissao comissao = new VendaComissao();
        comissao.setDataLancamento(new Date());
        comissao.setSituacao("A");
        comissao.setTipoContabil("C");
        comissao.setValorComissao(venda.getValorComissao());
        comissao.setValorVenda(venda.getValorTotal());
        comissao.setVendaCabecalho(venda);
        comissao.setVendedor(venda.getVendedor());
        comissoes.salvar(comissao);
    }
}
