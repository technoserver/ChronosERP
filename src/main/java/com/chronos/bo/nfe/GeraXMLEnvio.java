package com.chronos.bo.nfe;

/**
 * Created by john on 26/09/17.
 */
//pacotes para envio da nfe

import br.inf.portalfiscal.nfe.schema.consstatserv.TConsStatServ;
import br.inf.portalfiscal.nfe.schema.retconsstatserv.TRetConsStatServ;
import com.chronos.nfe.Nfe;
import com.chronos.util.ConstantesNFe;

//cancelar
//inutilizar


public class GeraXMLEnvio {

    public String consultarStatus(String ambiente, String codIbge, String versaoNfe) throws Exception {
        TConsStatServ consStatServ = new TConsStatServ();
        consStatServ.setTpAmb(ambiente);
        consStatServ.setCUF(codIbge);
        consStatServ.setVersao(versaoNfe);
        consStatServ.setXServ("STATUS");

        TRetConsStatServ retorno = Nfe.statusServico(consStatServ, false, ConstantesNFe.NFE);
        String status;
        status = "Status:" + retorno.getCStat() + "\n";
        status += "Motivo:" + retorno.getXMotivo() + "\n";
        status += "Data:" + retorno.getDhRecbto() + "\n";

        return status;
    }
}
