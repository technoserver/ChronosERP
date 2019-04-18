package com.chronos.service.comercial;

import com.chronos.modelo.entidades.VendaComissao;
import com.chronos.modelo.entidades.Vendedor;
import com.chronos.repository.VendaComissaoRepository;
import com.chronos.util.jpa.Transactional;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class VendaComissaoService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private VendaComissaoRepository repository;

    @Transactional
    public void gerarComissao(String situacao, String tipoContabil, BigDecimal valorComissao, BigDecimal valorVenda, String numdoc, Vendedor vendedor) {
        VendaComissao comissao = new VendaComissao();
        comissao.setDataLancamento(new Date());
        comissao.setSituacao(situacao);
        comissao.setTipoContabil(tipoContabil);
        comissao.setValorComissao(valorComissao);
        comissao.setValorVenda(valorVenda);
        comissao.setNumeroDocumento(numdoc);
        comissao.setVendedor(vendedor);
        repository.salvar(comissao);
    }
}
