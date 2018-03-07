package com.chronos.service.comercial;

import com.chronos.modelo.entidades.Colaborador;
import com.chronos.modelo.entidades.Vendedor;
import com.chronos.repository.Repository;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;

public class VendedorService implements Serializable {


    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<Vendedor> repository;

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
}
