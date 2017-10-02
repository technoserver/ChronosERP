package com.chronos.bo.nfe;

/**
 * Created by john on 26/09/17.
 */
//pacotes para envio da nfe

import br.inf.portalfiscal.nfe.schema.consstatserv.TConsStatServ;
import br.inf.portalfiscal.nfe.schema.inutnfe.TInutNFe;
import br.inf.portalfiscal.nfe.schema.inutnfe.TInutNFe.InfInut;
import br.inf.portalfiscal.nfe.schema.retconsstatserv.TRetConsStatServ;
import com.chronos.nfe.Nfe;
import com.chronos.util.ConstantesNFe;
import com.chronos.util.FormatValor;

import java.util.Date;

//cancelar
//inutilizar

public class GeraXMLEnvio {


    public TInutNFe inutilizarNfe(String ambiente, String codigoUf, String modelo, int serie, int numInicial,
                                  int numFinal, String cnpj, String justificativa) {

        String id = "ID" + codigoUf + FormatValor.getInstance().formatarAno(new Date()) + cnpj + modelo + FormatValor.getInstance().formatarSerieToString(serie)
                + FormatValor.getInstance().formatarNumeroDocFiscalToString(Integer.valueOf(numInicial)) + FormatValor.getInstance().formatarNumeroDocFiscalToString(Integer.valueOf(numFinal));

        TInutNFe inutNFe = new TInutNFe();
        inutNFe.setVersao("3.10");

        InfInut infInut = new InfInut();
        infInut.setId(id);
        infInut.setTpAmb(ambiente);
        infInut.setXServ("INUTILIZAR");
        infInut.setCUF(codigoUf);
        infInut.setAno(FormatValor.getInstance().formatarAno(new Date()));

        infInut.setCNPJ(cnpj);
        infInut.setMod(modelo);
        infInut.setSerie(String.valueOf(serie));

        infInut.setNNFIni(String.valueOf(numInicial));
        infInut.setNNFFin(String.valueOf(numFinal));

        infInut.setXJust(justificativa);
        inutNFe.setInfInut(infInut);

        return inutNFe;
    }

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
