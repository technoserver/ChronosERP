package com.chronos.service.estoque;

import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.TributOperacaoFiscal;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.service.cadastros.EmpresaService;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TransferenciaService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EmpresaService empresaService;
    @Inject
    private Repository<TributOperacaoFiscal> operacaoFiscalRepository;

    public List<Empresa> popularFiliais(int idmatriz, int idempresa, String tipo) throws ChronosException {
        List<Empresa> empresas = empresaService.getFiliais(idmatriz, idempresa, tipo);
        if (empresas.isEmpty()) {
            throw new ChronosException("Não existe filiais");
        }
        return empresas;
    }

    public List<TributOperacaoFiscal> getListOperacaoFiscal() throws ChronosException {
        List<TributOperacaoFiscal> operacoes = new ArrayList<>();

        if (operacoes.isEmpty()) {
            throw new ChronosException("Não existe Operações Fiscais");
        }

        return operacoes;
    }


}
