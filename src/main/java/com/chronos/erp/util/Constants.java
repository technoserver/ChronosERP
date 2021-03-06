package com.chronos.erp.util;

import com.chronos.erp.modelo.entidades.*;

/**
 * Created by john on 20/08/17.
 */
public interface Constants {


    String DIRETORIO_SCHEMA_NFE = "/modulo/comercial/nfe/schemas";
    String DIRETORIO_SCHEMA_MDFE = "/modulo/comercial/transporte/mdfe/schemas/";
    String DIRETORIO_SCHEMA_CTE = "/modulo/comercial/transporte/cte/schemas/";
    String JASPERNFE = "DanfeRetrato";
    String JASPERNFCE = "danfeNfce";
    String CAMINHODANFE = "/relatorios/nfe";
    int DECIMAIS_QUANTIDADE = 3;
    int DECIMAIS_VALOR = 2;
    boolean DESENVOLVIMENTO = false;


    interface FIN {
        FinStatusParcela STATUS_ABERTO = new FinStatusParcela(1);
        FinStatusParcela STATUS_QUITADO = new FinStatusParcela(2);
        FinStatusParcela STATUS_PARCIAL = new FinStatusParcela(3);
        FinTipoRecebimento CHEQUE = new FinTipoRecebimento(1);
        NaturezaFinanceira NATUREZA_VENDA = new NaturezaFinanceira(57);
    }

    interface COMERCIAL {
        NotaFiscalTipo TIPO_NFE = new NotaFiscalTipo();
    }

    interface OS {

        OsStatus STATUS_ABERTO = new OsStatus(1, "ABERTA");
        OsStatus STATUS_FINALIZADO = new OsStatus(4, "FINALIZADO");
        OsStatus STATUS_FATURADO = new OsStatus(5, "FINALIZADO E FATURADO");
        OsStatus STATUS_EMITIDO = new OsStatus(6, "FINALZIADO E EMITIDO NF");
        OsStatus STATUS_CANCELADO = new OsStatus(7, "CANCELADO");
    }

    interface MODULO {
        String NFE = "230";
    }

}
