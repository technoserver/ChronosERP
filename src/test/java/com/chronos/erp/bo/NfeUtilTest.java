package com.chronos.erp.bo;

import com.chronos.erp.bo.nfe.NfeUtil;
import com.chronos.erp.data.Mock;
import com.chronos.erp.modelo.entidades.NfeCabecalho;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class NfeUtilTest {

    private NfeUtil nfeUtil;
    private NfeCabecalho nfe;

    @Before
    public void init() {
        nfeUtil = new NfeUtil();
        nfe = new NfeCabecalho();

    }

    @Test
    public void devemos_garantir_que_os_avalors_totais_sejam_calculado() {

        nfe.setListaNfeDetalhe(Mock.getListaNfeDetalhe());
        nfeUtil.calcularTotalNFe(nfe);

        BigDecimal valorEsperado = new BigDecimal("389.46");

        Assert.assertEquals(valorEsperado, nfe.getValorTotal());
    }
}
