/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.modelo.entidades.enuns;

/**
 * @author john
 */
public enum GtinFormat {

    /**
     * GTIN-8, EAN-8. a versão curta do EAN-13 para produtos extremamente pequenos.
     */
    GTIN_8(8),
    /**
     * GTIN-12, UPC-A, UPC-12. versão padrão do UPC.
     */
    GTIN_12(12),
    /**
     * GTIN-13, EAN-13. Principalmente utilizado nos supermercados para identificar produtos no ponto de venda
     */
    GTIN_13(13),
    /**
     * GTIN-14, EAN-14. geralmente usado pro produtos comercializado.
     */
    GTIN_14(14);

    private final int length;

    GtinFormat(final int length) {
        this.length = length;
    }

    public static GtinFormat forLength(final int gtinLength) {
        for (GtinFormat gtinFormat : GtinFormat.values()) {
            if (gtinLength == gtinFormat.length()) {
                return gtinFormat;
            }
        }
        throw new IllegalArgumentException("Length '" + gtinLength + "' does not match the "
                + "length of any known GTIN formats");
    }

    /**
     * @return O tamanho do formato GTIN.
     */
    public int length() {
        return length;
    }

    /**
     * @return o nome formatado, e.x. GTIN-12.
     */
    @Override
    public String toString() {
        return "GTIN-" + Integer.toString(length);
    }
}
