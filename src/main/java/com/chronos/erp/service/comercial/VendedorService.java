package com.chronos.erp.service.comercial;

import com.chronos.erp.modelo.entidades.Colaborador;
import com.chronos.erp.modelo.entidades.Tecnico;
import com.chronos.erp.modelo.entidades.Vendedor;
import com.chronos.erp.repository.Repository;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VendedorService implements Serializable {


    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<Vendedor> repository;
    @Inject
    private Repository<Tecnico> tecnicoRepository;

    public Vendedor instaciarVendedor(Integer idcolaborador) {

        Vendedor vendedor = repository.get(Vendedor.class, "colaborador.id", idcolaborador);

        if (vendedor == null) {
            vendedor = new Vendedor();
            vendedor.setGerente('N');
            vendedor.setComissao(BigDecimal.ZERO);
            vendedor.setMetaVendas(BigDecimal.ZERO);
            vendedor.setComissao(BigDecimal.ZERO);
            vendedor.setColaborador(new Colaborador(idcolaborador));
            vendedor = repository.atualizar(vendedor);
        }

        return vendedor;
    }

    public Map<String, Integer> getMapVendedores() {
        List<Vendedor> list = repository.getEntitys(Vendedor.class, new ArrayList<>(), new Object[]{"colaborador.pessoa.nome"});
        list.add(0, new Vendedor(0, "TODOS"));
        Map<String, Integer> listaVendedor = new LinkedHashMap<>();
        listaVendedor.putAll(list.stream()
                .collect(Collectors.toMap((Vendedor::getNome), Vendedor::getId)));

        return listaVendedor;
    }

    public Map<String, Integer> getMapColaboradorComissionado() {
        List<Vendedor> list = repository.getEntitys(Vendedor.class, new ArrayList<>(), new Object[]{"colaborador.id", "colaborador.pessoa.nome"});
        list.add(0, new Vendedor(0, 0, "TODOS"));
        Map<String, Integer> listaVendedor = new LinkedHashMap<>();
        listaVendedor.putAll(list.stream()
                .collect(Collectors.toMap(v -> (v.getColaborador().getNome()), v -> v.getColaborador().getId())));

        List<Tecnico> tecnicos = tecnicoRepository.getEntitys(Tecnico.class, new ArrayList<>(), new Object[]{"colaborador.id", "colaborador.pessoa.nome"});
        listaVendedor.putAll(tecnicos.stream()
                .collect(Collectors.toMap(t -> (t.getColaborador().getNome()), t -> t.getColaborador().getId())));
        return listaVendedor;
    }
}
