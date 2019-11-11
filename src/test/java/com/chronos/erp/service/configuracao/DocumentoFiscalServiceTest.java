package com.chronos.erp.service.configuracao;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

public class DocumentoFiscalServiceTest {

    private DocumentoFiscalService service;

    @Before
    public void before() {
        service = new DocumentoFiscalService(null);
    }

    @Test
    public void devemo_garantir_a_geracao_de_um_codigo_numerico_valido() {
        String codigo = service.gerarCodigoNFe();


        assertNotEquals("00000000", codigo);
        assertNotEquals("11111111", codigo);
        assertNotEquals("22222222", codigo);
        assertNotEquals("33333333", codigo);
        assertNotEquals("44444444", codigo);
        assertNotEquals("55555555", codigo);
        assertNotEquals("66666666", codigo);
        assertNotEquals("77777777", codigo);
        assertNotEquals("88888888", codigo);
        assertNotEquals("99999999", codigo);
        assertNotEquals("12345678", codigo);
        assertNotEquals("23456789", codigo);
        assertNotEquals("34567890", codigo);
        assertNotEquals("45678901", codigo);
        assertNotEquals("56789012", codigo);
        assertNotEquals("67890123", codigo);
        assertNotEquals("78901234", codigo);
        assertNotEquals("89012345", codigo);
        assertNotEquals("90123456", codigo);
        assertNotEquals("01234567", codigo);
    }
}
