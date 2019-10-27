package com.chronos.service.comercial;

import com.chronos.modelo.entidades.Colaborador;
import com.chronos.modelo.entidades.Comissao;
import com.chronos.modelo.enuns.Modulo;
import com.chronos.repository.VendaComissaoRepository;
import com.chronos.util.jpa.Transactional;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ComissaoService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private VendaComissaoRepository repository;

    @Transactional
    public void gerarComissao(String situacao, String tipoContabil, BigDecimal valorComissao, BigDecimal valorVenda,
                              String numdoc, Colaborador colaborador, Modulo modulo) {
        Comissao comissao = new Comissao();
        comissao.setDataLancamento(new Date());
        comissao.setSituacao(situacao);
        comissao.setTipoContabil(tipoContabil);
        comissao.setValorComissao(valorComissao);
        comissao.setValorVenda(valorVenda);
        comissao.setNumeroDocumento(numdoc);
        comissao.setColaborador(colaborador);
        comissao.setCodigoModulo(modulo.getCodigo());
        repository.salvar(comissao);
    }
}