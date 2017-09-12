package com.chronos.util;

import com.chronos.modelo.entidades.FinStatusParcela;
import com.chronos.modelo.entidades.FinTipoRecebimento;
import com.chronos.modelo.entidades.NaturezaFinanceira;
import com.chronos.modelo.entidades.NotaFiscalTipo;

/**
 * Created by john on 20/08/17.
 */
public interface Constantes {

    interface FIN{
        public static FinStatusParcela STATUS_ABERTO = new FinStatusParcela(1);
        public static FinStatusParcela STATUS_QUITADO = new FinStatusParcela(2);
        public static FinTipoRecebimento CHEQUE = new FinTipoRecebimento(1);
        public static NaturezaFinanceira NATUREZA_VENDA = new NaturezaFinanceira(2);
    }

    interface COMERCIAL {
        public static NotaFiscalTipo TIPO_NFE = new NotaFiscalTipo();
    }
}
