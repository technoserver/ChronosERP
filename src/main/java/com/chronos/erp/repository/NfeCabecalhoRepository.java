package com.chronos.erp.repository;

import com.chronos.erp.modelo.entidades.NfeCabecalho;

public class NfeCabecalhoRepository extends AbstractRepository {


    public NfeCabecalho getRemusoCupom(int id) {
        String jpql = "SELECT new NfeCabecalho(o.id,o.empresa.id,o.empresa.cnpj,o.digitoChaveAcesso,o.chaveAcesso,o.qrcode,o.codigoModelo,o.serie, o.statusNota)  FROM NfeCabecalho o WHERE o.id = ?1";
        NfeCabecalho nfe = get(NfeCabecalho.class, jpql, id);

        return nfe;
    }
}
