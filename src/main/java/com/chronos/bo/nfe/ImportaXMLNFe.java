package com.chronos.bo.nfe;

import com.chronos.modelo.entidades.NfeCabecalho;
import com.chronos.modelo.entidades.NfeDetalhe;
import com.chronos.modelo.entidades.NfeEmitente;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;


import br.inf.portalfiscal.nfe.schema.procnfe.TIpi;
import br.inf.portalfiscal.nfe.schema.procnfe.TNFe;
import br.inf.portalfiscal.nfe.schema.procnfe.TNfeProc;
import br.inf.portalfiscal.nfe.schema.procnfe.TProtNFe;
import com.chronos.util.FormatValor;

import java.io.File;
import java.util.*;

/**
 * Created by john on 02/08/17.
 */
public class ImportaXMLNFe {


    public Map importarXmlNFe(File arquiXml) throws Exception {

        Map map = new HashMap();
        NfeCabecalho nfeCabecalho = new NfeCabecalho();
        nfeCabecalho.setListaNfeReferenciada(new HashSet<>());
        nfeCabecalho.setListaNfReferenciada(new HashSet<>());
        nfeCabecalho.setListaCteReferenciado(new HashSet<>());
        nfeCabecalho.setListaCupomFiscalReferenciado(new HashSet<>());
        nfeCabecalho.setListaProdRuralReferenciada(new HashSet<>());
        NfeEmitente emitente = new NfeEmitente();
        List<NfeDetalhe> listaNfeDetalhe = new ArrayList();

        JAXBContext jc = JAXBContext.newInstance("br.inf.portalfiscal.nfe.schema.procnfe");
        Unmarshaller unmarshaller = jc.createUnmarshaller();


        JAXBElement<TNfeProc> element = (JAXBElement) unmarshaller.unmarshal(arquiXml);
        TNfeProc nfeProc = element.getValue();

        TNFe nfe = nfeProc.getNFe();
        TNFe.InfNFe infNfe = nfe.getInfNFe();
        TNFe.InfNFe.Ide ide = infNfe.getIde();
        TNFe.InfNFe.Emit emit = infNfe.getEmit();
        List<TNFe.InfNFe.Det> listaDet = infNfe.getDet();
        TProtNFe protNfe = nfeProc.getProtNFe();


        //cabecalho
        nfeCabecalho.setCodigoNumerico(ide.getCNF());
        nfeCabecalho.setNaturezaOperacao(ide.getNatOp());
        nfeCabecalho.setIndicadorFormaPagamento(Integer.valueOf(ide.getIndPag()));
        nfeCabecalho.setCodigoModelo(ide.getMod());
        nfeCabecalho.setSerie(FormatValor.getInstance().formatarSerieToString(Integer.valueOf(ide.getSerie())));
        nfeCabecalho.setNumero(FormatValor.getInstance().formatarNumeroDocFiscalToString(Double.valueOf(ide.getNNF())));
        nfeCabecalho.setDataHoraEmissao(ide.getDhEmi() == null ? new Date() : FormatValor.getInstance().formatarDataNota(ide.getDhEmi()));
        if (ide.getDhSaiEnt() != null) {
            nfeCabecalho.setDataHoraEntradaSaida(FormatValor.getInstance().formatarDataNota(ide.getDhSaiEnt()));
        }
        nfeCabecalho.setTipoEmissao(Integer.valueOf(ide.getTpEmis()));
        nfeCabecalho.setVersaoProcessoEmissao(ide.getVerProc());
        nfeCabecalho.setCodigoMunicipio(Integer.valueOf(ide.getCMunFG()));
        nfeCabecalho.setFinalidadeEmissao(Integer.valueOf(ide.getFinNFe()));
        nfeCabecalho.setTipoOperacao(0);
        nfeCabecalho.setDigitoChaveAcesso(ide.getCDV());
        nfeCabecalho.setFormatoImpressaoDanfe(Integer.valueOf(ide.getTpImp()));
        nfeCabecalho.setChaveAcesso(protNfe.getInfProt().getChNFe());
        if (protNfe.getInfProt().getCStat().equals("100")) {
            nfeCabecalho.setStatusNota(5);
        }


        //cabecalho - totais icms
        TNFe.InfNFe.Total.ICMSTot icmsTot = nfe.getInfNFe().getTotal().getICMSTot();
        if (icmsTot != null) {
            nfeCabecalho.setBaseCalculoIcms(FormatValor.getInstance().formatarValorToBigDecimal(icmsTot.getVBC()));
            nfeCabecalho.setValorIcms(FormatValor.getInstance().formatarValorToBigDecimal(icmsTot.getVICMS()));
            nfeCabecalho.setBaseCalculoIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icmsTot.getVBCST()));
            nfeCabecalho.setValorIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icmsTot.getVST()));
            nfeCabecalho.setValorTotalProdutos(FormatValor.getInstance().formatarValorToBigDecimal(icmsTot.getVProd()));
            nfeCabecalho.setValorFrete(FormatValor.getInstance().formatarValorToBigDecimal(icmsTot.getVFrete()));
            nfeCabecalho.setValorSeguro(FormatValor.getInstance().formatarValorToBigDecimal(icmsTot.getVSeg()));
            nfeCabecalho.setValorDesconto(FormatValor.getInstance().formatarValorToBigDecimal(icmsTot.getVDesc()));
            nfeCabecalho.setValorImpostoImportacao(FormatValor.getInstance().formatarValorToBigDecimal(icmsTot.getVII()));
            nfeCabecalho.setValorIpi(FormatValor.getInstance().formatarValorToBigDecimal(icmsTot.getVIPI()));
            nfeCabecalho.setValorPis(FormatValor.getInstance().formatarValorToBigDecimal(icmsTot.getVPIS()));
            nfeCabecalho.setValorCofins(FormatValor.getInstance().formatarValorToBigDecimal(icmsTot.getVCOFINS()));
            nfeCabecalho.setValorDespesasAcessorias(FormatValor.getInstance().formatarValorToBigDecimal(icmsTot.getVOutro()));
            nfeCabecalho.setValorTotal(FormatValor.getInstance().formatarValorToBigDecimal(icmsTot.getVNF()));
        }

        //cabecalho - totais issqn
        TNFe.InfNFe.Total.ISSQNtot issqnTot = nfe.getInfNFe().getTotal().getISSQNtot();
        if (issqnTot != null) {
            nfeCabecalho.setValorServicos(FormatValor.getInstance().formatarValorToBigDecimal(issqnTot.getVServ()));
            nfeCabecalho.setBaseCalculoIssqn(FormatValor.getInstance().formatarValorToBigDecimal(issqnTot.getVBC()));
            nfeCabecalho.setValorIssqn(FormatValor.getInstance().formatarValorToBigDecimal(issqnTot.getVISS()));
            nfeCabecalho.setValorPisIssqn(FormatValor.getInstance().formatarValorToBigDecimal(issqnTot.getVPIS()));
            nfeCabecalho.setValorCofinsIssqn(FormatValor.getInstance().formatarValorToBigDecimal(issqnTot.getVCOFINS()));

        }

        //cabecalho - retenções
        TNFe.InfNFe.Total.RetTrib retTrib = nfe.getInfNFe().getTotal().getRetTrib();
        if (retTrib != null) {
            nfeCabecalho.setValorRetidoPis(FormatValor.getInstance().formatarValorToBigDecimal(retTrib.getVRetPIS()));
            nfeCabecalho.setValorRetidoCofins(FormatValor.getInstance().formatarValorToBigDecimal(retTrib.getVRetCOFINS()));
            nfeCabecalho.setValorRetidoCsll(FormatValor.getInstance().formatarValorToBigDecimal(retTrib.getVRetCSLL()));
            nfeCabecalho.setBaseCalculoIrrf(FormatValor.getInstance().formatarValorToBigDecimal(retTrib.getVBCIRRF()));
            nfeCabecalho.setValorRetidoIrrf(FormatValor.getInstance().formatarValorToBigDecimal(retTrib.getVIRRF()));
            nfeCabecalho.setBaseCalculoPrevidencia(FormatValor.getInstance().formatarValorToBigDecimal(retTrib.getVBCRetPrev()));
            nfeCabecalho.setValorRetidoPrevidencia(FormatValor.getInstance().formatarValorToBigDecimal(retTrib.getVRetPrev()));
        }

        //cabecalho - informações adicionais
        TNFe.InfNFe.Exporta exporta = infNfe.getExporta();
        if (exporta != null) {
            nfeCabecalho.setComexUfEmbarque(exporta.getXLocExporta());
            nfeCabecalho.setComexLocalEmbarque(exporta.getXLocDespacho());
        }

        TNFe.InfNFe.Compra compra = infNfe.getCompra();
        if (compra != null) {
            nfeCabecalho.setCompraNotaEmpenho(compra.getXNEmp());
            nfeCabecalho.setCompraPedido(compra.getXPed());
            nfeCabecalho.setCompraContrato(compra.getXCont());
        }

        TNFe.InfNFe.InfAdic infAdic = infNfe.getInfAdic();
        if (infAdic != null) {
            nfeCabecalho.setInformacoesAddFisco(infAdic.getInfAdFisco());
            nfeCabecalho.setInformacoesAddContribuinte(infAdic.getInfCpl());
        }

        //emitente
        emitente = getEmitente(emit);
        emitente.setNfeCabecalho(nfeCabecalho);



        return map;
    }

    private NfeEmitente getEmitente(TNFe.InfNFe.Emit emit){
        NfeEmitente emitente = new NfeEmitente();

        emitente.setCpfCnpj(emit.getCNPJ());
        emitente.setInscricaoEstadual(emit.getIE());
        emitente.setNome(emit.getXNome());
        emitente.setFantasia(emit.getXFant());
        emitente.setLogradouro(emit.getEnderEmit().getXLgr());
        emitente.setNumero(emit.getEnderEmit().getNro());
        emitente.setComplemento(emit.getEnderEmit().getXCpl());
        emitente.setBairro(emit.getEnderEmit().getXBairro());
        emitente.setCodigoMunicipio(Integer.valueOf(emit.getEnderEmit().getCMun()));
        emitente.setNomeMunicipio(emit.getEnderEmit().getXMun());
        emitente.setUf(emit.getEnderEmit().getUF().value());
        emitente.setCep(emit.getEnderEmit().getCEP());
        emitente.setCrt(emit.getCRT());
        emitente.setCodigoPais(Integer.valueOf(emit.getEnderEmit().getCPais()));
        emitente.setNomePais(emit.getEnderEmit().getXPais());
        emitente.setTelefone(emit.getEnderEmit().getFone());
        emitente.setInscricaoEstadualSt(emit.getIEST());
        emitente.setInscricaoMunicipal(emit.getIM());
        emitente.setCrt(emit.getCRT());
        emitente.setCnae(emit.getCNAE());

        return emitente;
    }




}
