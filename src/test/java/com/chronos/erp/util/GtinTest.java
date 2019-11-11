package com.chronos.erp.util;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class GtinTest {

    @Test
    public void gtin8DeveSerValido() {
        String gtin8 = "78936683";
        boolean valido = GtinUtil.isValid(gtin8);
        assertTrue(valido);
    }
}
