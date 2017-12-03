/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.util;


import com.chronos.execption.GtinFormatException;
import com.chronos.modelo.entidades.enuns.GtinFormat;

import java.io.Serializable;

/**
 * @author john
 */
public class GtinUtil implements Serializable {

    private static final long serialVersionUID = 7903029166267597077L;

    private final String gtin;
    private final GtinFormat format;

    private GtinUtil(final String gtin) {
        this.gtin = gtin;
        format = GtinFormat.forLength(gtin.length());
    }

    public static GtinUtil create(final String gtin) throws GtinFormatException {
        if (!isValid(gtin)) {
            throw new GtinFormatException("String '" + gtin + "' is not a valid gtin");
        }

        return new GtinUtil(gtin);
    }

    public static GtinUtil createWithCheckDigit(final String gtinWithoutCheckDigit) throws GtinFormatException {
        return new GtinUtil(withCheckDigit(gtinWithoutCheckDigit));
    }

    public static boolean isValid(final String gtin) {
        return isValid(gtin, null);
    }

    public static boolean isValid(final String gtin, final GtinFormat format) {
        // Check format of barcode for validity
        if (!matchesFormat(gtin)) {
            return false;
        }
        int gtinLength = gtin.length();
        if (format != null && gtinLength != format.length()) {
            return false;
        }
        int checkSum = 0;
        int weightBit = gtinLength % 2;
        for (int i = 0; i < gtinLength; i++) {
            int weight = i % 2 == weightBit ? 3 : 1;
            checkSum += Integer.parseInt(gtin.substring(i, i + 1)) * weight;
        }
        return checkSum % 10 == 0;
    }

    public static boolean isValid8(final String gtin) {
        return isValid(gtin, GtinFormat.GTIN_8);
    }

    public static boolean isValid12(final String gtin) {
        return isValid(gtin, GtinFormat.GTIN_12);
    }

    public static boolean isValid13(final String gtin) {
        return isValid(gtin, GtinFormat.GTIN_13);
    }

    public static boolean isValid14(final String gtin) {
        return isValid(gtin, GtinFormat.GTIN_14);
    }

    public static int calculateCheckDigit(final String gtinWithoutCheckDigit) {
        if (!matchesFormat(gtinWithoutCheckDigit, null, 1)) {
            throw new GtinFormatException("String '" + gtinWithoutCheckDigit + "' is not a valid partial gtin");
        }
        int checkSum = 0;
        int gtinLength = gtinWithoutCheckDigit.length();
        int weightBit = (gtinLength + 1) % 2;
        for (int i = 0; i < gtinLength; i++) {
            int weight = i % 2 == weightBit ? 3 : 1;
            checkSum += Integer.parseInt(gtinWithoutCheckDigit.substring(i, i + 1)) * weight;
        }
        return checkSum % 10 == 0 ? 0 : 10 - (checkSum % 10);
    }

    public static String withCheckDigit(final String gtinWithoutCheckDigit) {
        return gtinWithoutCheckDigit + Integer.toString(calculateCheckDigit(gtinWithoutCheckDigit));
    }

    public static boolean matchesFormat(final String gtin) {
        return matchesFormat(gtin, null, 0);
    }

    public static boolean matchesFormat(final String gtin, final GtinFormat format) {
        return matchesFormat(gtin, format, 0);
    }

    public static boolean matchesFormat8(final String gtin) {
        return matchesFormat(gtin, GtinFormat.GTIN_8, 0);
    }

    public static boolean matchesFormat12(final String gtin) {
        return matchesFormat(gtin, GtinFormat.GTIN_12, 0);
    }

    public static boolean matchesFormat13(final String gtin) {
        return matchesFormat(gtin, GtinFormat.GTIN_13, 0);
    }

    public static boolean matchesFormat14(final String gtin) {
        return matchesFormat(gtin, GtinFormat.GTIN_14, 0);
    }

    private static boolean matchesFormat(final String gtin, final GtinFormat format, final int offset) {
        if (gtin == null) {
            throw new IllegalArgumentException("gtin is null");
        }
        // Check length
        int gtinLength = gtin.length();
        boolean validLength = false;
        if (format != null) {
            validLength = gtinLength == format.length();
        } else {
            for (GtinFormat gtinFormat : GtinFormat.values()) {
                if (gtinLength == gtinFormat.length() - offset) {
                    validLength = true;
                    break;
                }
            }
        }
        if (!validLength) {
            return false;
        }
        // Check whether is a number
        return gtin.matches("^[0-9]+$");
    }

    public GtinFormat format() {
        return format;
    }

    public int length() {
        return format.length();
    }

    public int checkDigit() {
        return Integer.parseInt(gtin.substring(gtin.length() - 1));
    }

    public int digitAt(final int position) {
        return Integer.parseInt(gtin.substring(position, position + 1));
    }

    @Override
    public String toString() {
        return gtin;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj == this || obj instanceof GtinUtil && gtin.equals(((GtinUtil) obj).gtin);
    }

    @Override
    @SuppressWarnings("checkstyle:magicnumber")
    public int hashCode() {
        return 37 + gtin.hashCode();
    }

}
