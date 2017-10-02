package com.chronos.util;

import com.chronos.modelo.entidades.FinStatusParcela;
import com.chronos.modelo.entidades.FinTipoRecebimento;
import com.chronos.modelo.entidades.NaturezaFinanceira;
import com.chronos.modelo.entidades.NotaFiscalTipo;

/**
 * Created by john on 20/08/17.
 */
public interface Constantes {


    public static String DIRETORIO_SCHEMA_NFE = "/modulo/comercial/nfe/schemas/";
    public static final String JASPERNFE = "DanfeRetrato";
    public static final String JASPERNFCE = "danfeNfce";
    public static final String CAMINHODANFE = "/com/chronos/erplight/relatorios/comercial/nfe";

    interface FIN{
        public static FinStatusParcela STATUS_ABERTO = new FinStatusParcela(1);
        public static FinStatusParcela STATUS_QUITADO = new FinStatusParcela(2);
        public static FinTipoRecebimento CHEQUE = new FinTipoRecebimento(1);
        public static NaturezaFinanceira NATUREZA_VENDA = new NaturezaFinanceira(57);
    }

    interface COMERCIAL {
        public static NotaFiscalTipo TIPO_NFE = new NotaFiscalTipo();
    }
}
