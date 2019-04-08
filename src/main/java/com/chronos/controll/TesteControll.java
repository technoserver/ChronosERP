package com.chronos.controll;

import br.inf.portalfiscal.nfe.schema_4.enviNFe.TEnviNFe;
import com.chronos.bo.nfe.NfeTransmissao;
import com.chronos.modelo.entidades.NfeCabecalho;
import com.chronos.repository.Repository;
import com.chronos.transmissor.util.XmlUtil;
import org.primefaces.PrimeFaces;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Map;

@Named
@ViewScoped
public class TesteControll implements Serializable {

    private static final long serialVersionUID = 1L;

    private String teste;

    private String xml;

    private int idnfe;

    @Inject
    private Repository<NfeCabecalho> nfeCabecalhoRepository;

    public void enviarMensage() {
        try {
            System.out.println("teste");

//            ConnectionFactory factory = new ConnectionFactory();
//            factory.setHost("localhost");
//            Connection connection = factory.newConnection();
//            Channel channel = connection.createChannel();
//
//            channel.queueDeclare("hello-queue", false, false, false, null);
//            String message = "Hello World!";
//            channel.basicPublish("", "hello-queue", null, message.getBytes("UTF-8"));
//            System.out.println(" [x] Sent '" + message + "'");
//
//            channel.close();
//            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public void setarValoresCEP() {
        Map<String, String> requestParamMap = FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap();

        String cep = requestParamMap.get("cep");
        String estado = requestParamMap.get("estado");
        String cidade = requestParamMap.get("cidade");
        String bairro = requestParamMap.get("bairro");
        String endereco = requestParamMap.get("endereco");

        System.out.println("cep = " + cep);
        System.out.println("estado = " + estado);
        System.out.println("cidade = " + cidade);
        System.out.println("bairro = " + bairro);
        System.out.println("endereco = " + endereco);


    }

    public void enviarNFe() throws Exception {

        NfeCabecalho nfeCabecalho = nfeCabecalhoRepository.get(idnfe, NfeCabecalho.class);

        TEnviNFe tEnviNFe = NfeTransmissao.getInstance().geraNFeEnv(nfeCabecalho);


        tEnviNFe.getNFe().get(0).setSignature(null);


        xml = XmlUtil.objectToXml(tEnviNFe);
        PrimeFaces.current().ajax().addCallbackParam("nfe", xml);
    }

    public void testResultPrint() {
        Map<String, String> requestParamMap = FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap();

        String cep = requestParamMap.get("sucesso");
        String estado = requestParamMap.get("mensagem");

        System.out.println("cep = " + cep);
        System.out.println("estado = " + estado);
    }

    public void imprimir() {
        xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><nfeProc versao=\"4.00\" xmlns=\"http://www.portalfiscal.inf.br/nfe\"><NFe><infNFe versao=\"4.00\" Id=\"NFe53180816925371000114650010000004541000004543\"><ide><cUF>53</cUF><cNF>00000454</cNF><natOp>VENDA</natOp><mod>65</mod><serie>1</serie><nNF>454</nNF><dhEmi>2018-08-15T01:29:50-03:00</dhEmi><tpNF>1</tpNF><idDest>1</idDest><cMunFG>5300108</cMunFG><tpImp>4</tpImp><tpEmis>1</tpEmis><cDV>3</cDV><tpAmb>2</tpAmb><finNFe>1</finNFe><indFinal>1</indFinal><indPres>1</indPres><procEmi>0</procEmi><verProc>4.0.0.3</verProc></ide><emit><CNPJ>16925371000114</CNPJ><xNome>TECHNOSEVER AUTOMACAO COMERCIAL</xNome><xFant>TECHNOSERVER</xFant><enderEmit><xLgr>QNA 05 LT 33 LJ01/02</xLgr><nro>01</nro><xCpl>TESETE</xCpl><xBairro>CEILANDIA</xBairro><cMun>5300108</cMun><xMun>BRASILIA</xMun><UF>DF</UF><CEP>73000000</CEP><cPais>1058</cPais><xPais>BRASIL</xPais></enderEmit><IE>0762315200183</IE><IM>5300108</IM><CRT>1</CRT></emit><det nItem=\"1\"><prod><cProd>7896065880069</cProd><cEAN>7896065880069</cEAN><xProd>NOTA FISCAL EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL</xProd><NCM>22011000</NCM><CFOP>5102</CFOP><uCom>UN</uCom><qCom>1.0000</qCom><vUnCom>2.500000</vUnCom><vProd>2.50</vProd><cEANTrib>7896065880069</cEANTrib><uTrib>UN</uTrib><qTrib>1.0000</qTrib><vUnTrib>2.500000</vUnTrib><indTot>1</indTot></prod><imposto><vTotTrib>1.73</vTotTrib><ICMS><ICMSSN102><orig>0</orig><CSOSN>102</CSOSN></ICMSSN102></ICMS><PIS><PISAliq><CST>01</CST><vBC>2.50</vBC><pPIS>0.65</pPIS><vPIS>0.01</vPIS></PISAliq></PIS><COFINS><COFINSAliq><CST>01</CST><vBC>2.50</vBC><pCOFINS>3.00</pCOFINS><vCOFINS>0.07</vCOFINS></COFINSAliq></COFINS></imposto></det><det nItem=\"2\"><prod><cProd>3</cProd><cEAN>SEM GTIN</cEAN><xProd>ALCATRA</xProd><NCM>02031900</NCM><CFOP>5102</CFOP><uCom>KG</uCom><qCom>1.0000</qCom><vUnCom>1.000000</vUnCom><vProd>1.00</vProd><cEANTrib>SEM GTIN</cEANTrib><uTrib>KG</uTrib><qTrib>1.0000</qTrib><vUnTrib>1.000000</vUnTrib><indTot>1</indTot></prod><imposto><vTotTrib>0.32</vTotTrib><ICMS><ICMSSN102><orig>0</orig><CSOSN>102</CSOSN></ICMSSN102></ICMS><PIS><PISAliq><CST>01</CST><vBC>1.00</vBC><pPIS>0.65</pPIS><vPIS>0.00</vPIS></PISAliq></PIS><COFINS><COFINSAliq><CST>01</CST><vBC>1.00</vBC><pCOFINS>3.00</pCOFINS><vCOFINS>0.03</vCOFINS></COFINSAliq></COFINS></imposto></det><det nItem=\"3\"><prod><cProd>4</cProd><cEAN>SEM GTIN</cEAN><xProd>SERVICO</xProd><NCM>00000000</NCM><CFOP>5933</CFOP><uCom>UN</uCom><qCom>1.0000</qCom><vUnCom>100.000000</vUnCom><vProd>100.00</vProd><cEANTrib>SEM GTIN</cEANTrib><uTrib>UN</uTrib><qTrib>1.0000</qTrib><vUnTrib>100.000000</vUnTrib><indTot>1</indTot></prod><imposto><vTotTrib>30.90</vTotTrib><ISSQN><vBC>100.00</vBC><vAliq>100.00</vAliq><vISSQN>100.00</vISSQN><cMunFG>5300108</cMunFG><cListServ>01.05</cListServ><indISS>5</indISS><indIncentivo>2</indIncentivo></ISSQN><PIS><PISAliq><CST>01</CST><vBC>100.00</vBC><pPIS>0.65</pPIS><vPIS>0.65</vPIS></PISAliq></PIS><COFINS><COFINSAliq><CST>01</CST><vBC>100.00</vBC><pCOFINS>3.00</pCOFINS><vCOFINS>3.00</vCOFINS></COFINSAliq></COFINS></imposto></det><total><ICMSTot><vBC>0.00</vBC><vICMS>0.00</vICMS><vICMSDeson>0.00</vICMSDeson><vFCP>0.00</vFCP><vBCST>0.00</vBCST><vST>0.00</vST><vFCPST>0.00</vFCPST><vFCPSTRet>0.00</vFCPSTRet><vProd>3.50</vProd><vFrete>0.00</vFrete><vSeg>0.00</vSeg><vDesc>0.00</vDesc><vII>0.00</vII><vIPI>0.00</vIPI><vIPIDevol>0.00</vIPIDevol><vPIS>0.01</vPIS><vCOFINS>0.10</vCOFINS><vOutro>0.00</vOutro><vNF>103.50</vNF><vTotTrib>32.95</vTotTrib></ICMSTot><ISSQNtot><vServ>100.00</vServ><vBC>100.00</vBC><vISS>100.00</vISS><vPIS>0.65</vPIS><vCOFINS>3.00</vCOFINS><dCompet>2018-08-15</dCompet></ISSQNtot></total><transp><modFrete>9</modFrete></transp><pag><detPag><tPag>03</tPag><vPag>103.50</vPag></detPag></pag><infAdic><infCpl>Trib. Aprox. Federal R$ 13,87 e R$ 0,96 Estadual e R$ 2,00 Municipal Fonte IBPT</infCpl></infAdic></infNFe><infNFeSupl><qrCode><![CDATA[http://dec.fazenda.df.gov.br/ConsultarNFCe.aspx?chNFe=53180816925371000114650010000004541000004543&nVersao=100&tpAmb=2&dhEmi=323031382d30382d31355430313a32393a35302d30333a3030&vNF=103.50&vICMS=0.00&digVal=2b6e4b5850727833362b615030687364334830573368575a6142453d&cIdToken=000001&cHashQRCode=79418228EAA9F21FBDE19FA8886B0E0362B4EA2E]]></qrCode><urlChave>http://dec.fazenda.df.gov.br/ConsultarNFCe.aspx</urlChave></infNFeSupl><Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/><SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/><Reference URI=\"#NFe53180816925371000114650010000004541000004543\"><Transforms><Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/><Transform Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/><DigestValue>+nKXPrx36+aP0hsd3H0W3hWZaBE=</DigestValue></Reference></SignedInfo><SignatureValue>SO/i0bS/472N3i9qFZIH/dfzGPuF1eLKUIB/X9rvAGs60nsLgLETmRMDek5LiJKUFll/qRxDUM3yYI4cIPxJFmUnE2n3EuTMN+mCl43TjJ1SZeCvzLGUDjIIQSI+gxaH0Da/VCbXyz5Xz9UAOpv13Y0LMFazW4L+e86b5x74OOg8H4DqM5P6E8VbVEaLsjRhCLjDBZxvKMMtR6s7p2/sBSU4LLE/KjYwqjbFfSoazem0UHKea6jzj9LpTJ2u2+zjGIOU+yOzQtm4Q99rzDSYqg1fn2QfFgmRkNeDd5rATvd068cKV9ENuNanwCC3BKMsdYb7EuvqqHPZsF9n9Tqb3Q==</SignatureValue><KeyInfo><X509Data><X509Certificate>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</X509Certificate></X509Data></KeyInfo></Signature></NFe><protNFe versao=\"4.00\"><infProt><tpAmb>2</tpAmb><verAplic>SVRSnfce201807191353</verAplic><chNFe>53180816925371000114650010000004541000004543</chNFe><dhRecbto>2018-08-15T01:29:51-03:00</dhRecbto><nProt>353180004095018</nProt><digVal>+nKXPrx36+aP0hsd3H0W3hWZaBE=</digVal><cStat>100</cStat><xMotivo>Autorizado o uso da NF-e</xMotivo></infProt></protNFe></nfeProc>";
//        TNfeProc nfeProc = new TNfeProc();
//        TNfeProc tNfeProc = XmlUtil.xmlToObject(xml, TNfeProc.class);
//        ObjectMapper mapper = new ObjectMapper();
//        xml = mapper.writeValueAsString(tNfeProc);
//
//        JAXBContext jc = JAXBContext.newInstance(TNfeProc.class);
//
//        StringWriter sw = new StringWriter();
//
//
//        Map<String, Object> properties = new HashMap<>();
//        properties.put(JAXBContextProperties.MEDIA_TYPE, "application/json");
//        properties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, true);
//
//
//        JAXBContext jaxbContext =
//                JAXBContextFactory.createContext(new Class[]  {
//                        TNfeProc.class,    ObjectFactory.class}, properties);
//        Marshaller marshaller = jaxbContext.createMarshaller();
//
////        Marshaller marshaller = jc.createMarshaller();
////        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
////        marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
//        marshaller.marshal(tNfeProc, sw);
//        xml = sw.toString();
        PrimeFaces.current().ajax().addCallbackParam("xml", xml);
    }

    public String getTeste() {
        return teste;
    }

    public void setTeste(String teste) {
        this.teste = teste;
    }


    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public int getIdnfe() {
        return idnfe;
    }

    public void setIdnfe(int idnfe) {
        this.idnfe = idnfe;
    }
}
