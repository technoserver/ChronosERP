package com.chronos.erp.bo;

import com.chronos.erp.data.Mock;
import com.chronos.erp.modelo.entidades.NfeCabecalho;
import org.junit.Before;
import org.junit.Test;

public class NfeCabecalhoTest {

    private NfeCabecalho nfe;

    @Before
    public void init() {

        nfe = new NfeCabecalho();
        nfe.setListaNfeDetalhe(Mock.getListaNfeDetalhe());
    }

    @Test
    public void devemos_garantir_calcular_o_valor_total_dos_produtos() {

    }
}
