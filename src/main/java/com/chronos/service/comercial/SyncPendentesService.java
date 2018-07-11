package com.chronos.service.comercial;

import com.chronos.modelo.entidades.EmpresaProduto;
import com.chronos.modelo.entidades.PdvCaixa;
import com.chronos.modelo.entidades.SyncPendentes;
import com.chronos.modelo.enuns.AcaoSync;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.jpa.Transactional;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 04/07/18.
 */
public class SyncPendentesService implements Serializable {


    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<PdvCaixa> repositoryCaixa;
    @Inject
    private Repository<EmpresaProduto> produtoRepository;
    @Inject
    private Repository<SyncPendentes> repository;

    @Transactional
    public void gerarSyncPendetensEstoque(Integer idcaixa, Integer idempresa, Integer idproduto) {

        List<Filtro> filtros = new ArrayList<>();
        filtros.add(new Filtro("id", Filtro.DIFERENTE, idcaixa));

        List<PdvCaixa> caixas = repositoryCaixa.getEntitys(PdvCaixa.class, filtros);

        filtros.clear();

        filtros.add(new Filtro("empresa.id", idempresa));
        filtros.add(new Filtro("produto.id", idproduto));

        EmpresaProduto empresaProduto = produtoRepository.get(EmpresaProduto.class, filtros);

        for (PdvCaixa caixa : caixas) {
            SyncPendentes sync = new SyncPendentes(AcaoSync.ESTOQUE.getCodigo(), AcaoSync.ESTOQUE.getNome(), caixa.getId(), empresaProduto.getProduto().getId());
            repository.salvar(sync);
        }

    }

}