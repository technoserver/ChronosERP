package com.chronos.service.configuracao;

import com.chronos.modelo.entidades.PdvConfiguracaoBalanca;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.util.jpa.Transactional;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.io.Serializable;

public class BalancaService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<PdvConfiguracaoBalanca> repository;

    @Transactional
    private PdvConfiguracaoBalanca salvar(PdvConfiguracaoBalanca conf) throws ChronosException {

        String layout = montaLayout(conf);

        conf.setTipoConfiguracao(layout);

        return repository.atualizar(conf);
    }

    public String montaLayout(PdvConfiguracaoBalanca conf) throws ChronosException {

        if (conf.getTamanhoCodigoProduto() + conf.getTamanhoIdentificador() + 2 > 13) {
            throw new ChronosException("Configuração informada não pode utrapassar um total de 13 digitos");
        }

        String str = "";

        str += conf.getModelo().ordinal();

        String codigoProduto = StringUtils.rightPad("C", conf.getTamanhoCodigoProduto(), "C");
        str += codigoProduto;

        String tipo = StringUtils.rightPad(conf.getIdentificador(), conf.getTamanhoIdentificador(), conf.getIdentificador());

        int tam = 11 - (codigoProduto + tipo).length();
        String completar = "";
        completar = StringUtils.rightPad(completar, tam, "0");

        str += completar;

        str += tipo;

        str += "DV";
        return str;
    }

}
