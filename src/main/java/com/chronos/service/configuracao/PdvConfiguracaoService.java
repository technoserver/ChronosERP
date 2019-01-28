package com.chronos.service.configuracao;

import com.chronos.modelo.entidades.PdvConfiguracao;
import com.chronos.modelo.enuns.ModeloBalanca;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.util.jpa.Transactional;

import javax.inject.Inject;
import java.io.Serializable;

public class PdvConfiguracaoService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<PdvConfiguracao> repository;

    @Inject
    private BalancaService balancaService;

    @Transactional
    public PdvConfiguracao salvar(PdvConfiguracao configuracao) throws ChronosException {


        PdvConfiguracao pdvConfiguracao = repository.get(PdvConfiguracao.class, "pdvCaixa.id", configuracao.getPdvCaixa().getId(), new Object[]{"mensagemCupom"});

        if (pdvConfiguracao != null && !pdvConfiguracao.equals(configuracao)) {
            throw new ChronosException("Já foram definidas as configurações pra esse caixa");
        }


        if (configuracao.getPdvConfiguracaoBalanca().getModelo() == ModeloBalanca.NENHUM) {
            configuracao.setPdvConfiguracaoBalanca(null);
        } else {
            String layout = balancaService.montaLayout(configuracao.getPdvConfiguracaoBalanca());
            configuracao.getPdvConfiguracaoBalanca().setTipoConfiguracao(layout);
        }


        configuracao = repository.atualizar(configuracao);
        return configuracao;
    }
}
