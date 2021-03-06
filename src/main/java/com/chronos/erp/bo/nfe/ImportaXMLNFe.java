package com.chronos.erp.bo.nfe;

import br.inf.portalfiscal.nfe.schema_4.procNFe.TIpi;
import br.inf.portalfiscal.nfe.schema_4.procNFe.TNFe;
import br.inf.portalfiscal.nfe.schema_4.procNFe.TNfeProc;
import br.inf.portalfiscal.nfe.schema_4.procNFe.TProtNFe;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.modelo.enuns.TipoImportacaoXml;
import com.chronos.erp.service.comercial.DefinirCstService;
import com.chronos.erp.util.FormatValor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * Created by john on 02/08/17.
 */
public class ImportaXMLNFe {

    private String imp;

    private List<ConverterCst> cstList;
    private List<ConverterCfop> cfopList;
    private TipoImportacaoXml tipoImportacao;
    private DefinirCstService definirCstService;

    public ImportaXMLNFe(TipoImportacaoXml tipoImportacao) {
        this.tipoImportacao = tipoImportacao;
        this.cstList = new ArrayList<>();
        this.cfopList = new ArrayList<>();
        this.definirCstService = new DefinirCstService();
    }

    public ImportaXMLNFe(TipoImportacaoXml tipoImportacao, List<ConverterCst> cstList, List<ConverterCfop> cfopList) {
        this.tipoImportacao = tipoImportacao;
        this.cstList = cstList;
        this.cfopList = cfopList;
        this.definirCstService = new DefinirCstService();
    }

    public Map importarXmlNFe(File arquiXml) throws Exception {

        Map map = new HashMap();
        NfeCabecalho nfeCabecalho = new NfeCabecalho();

        nfeCabecalho.setListaNfeReferenciada(new HashSet<>());
        nfeCabecalho.setListaNfReferenciada(new HashSet<>());
        nfeCabecalho.setListaCteReferenciado(new HashSet<>());
        nfeCabecalho.setListaCupomFiscalReferenciado(new HashSet<>());
        nfeCabecalho.setListaProdRuralReferenciada(new HashSet<>());
        List<NfeReferenciada> listaNfeReferenciada = new ArrayList<>();
        NfeEmitente emitente;
        NfeDestinatario destinatario;

        List<NfeDetalhe> listaNfeDetalhe = new ArrayList();
        Set<NfeDuplicata> listaDuplicatas = new HashSet<>();

        JAXBContext jc = JAXBContext.newInstance("br.inf.portalfiscal.nfe.schema_4.procNFe");
        Unmarshaller unmarshaller = jc.createUnmarshaller();


        JAXBElement<TNfeProc> element = (JAXBElement) unmarshaller.unmarshal(arquiXml);
        TNfeProc nfeProc = element.getValue();

        TNFe nfe = nfeProc.getNFe();
        TNFe.InfNFe infNfe = nfe.getInfNFe();
        TNFe.InfNFe.Ide ide = infNfe.getIde();
        TNFe.InfNFe.Emit emit = infNfe.getEmit();
        TNFe.InfNFe.Dest dest = infNfe.getDest();
        List<TNFe.InfNFe.Det> listaDet = infNfe.getDet();
        TProtNFe protNfe = nfeProc.getProtNFe();


        //cabecalho
        nfeCabecalho.setCodigoNumerico(ide.getCNF());
        nfeCabecalho.setNaturezaOperacao(ide.getNatOp());
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

        if (tipoImportacao != TipoImportacaoXml.ENTRADA && protNfe.getInfProt().getCStat().equals("100")) {
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
            nfeCabecalho.setValorFcp(FormatValor.getInstance().formatarValorToBigDecimal(icmsTot.getVFCP()));
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
        nfeCabecalho.setEmitente(emitente);


        //destinatario
        destinatario = tipoImportacao == TipoImportacaoXml.ENTRADA ? getDestinatario(dest) : getDestinatario(emit);

        destinatario.setNfeCabecalho(nfeCabecalho);
        nfeCabecalho.setDestinatario(destinatario);

        //detalhe
        TNFe.InfNFe.Det det;
        NfeDetalhe nfeDetalhe;
        BigDecimal valorTotalProduto;

        for (TNFe.InfNFe.Det itemDet : listaDet) {
            nfeDetalhe = getNfeDetalhe(itemDet);
            nfeDetalhe.setNfeCabecalho(nfeCabecalho);
            listaNfeDetalhe.add(nfeDetalhe);
        }

        //Duplicatas
        TNFe.InfNFe.Cobr cobr = infNfe.getCobr();
        List<TNFe.InfNFe.Cobr.Dup> duplicatas = cobr != null ? cobr.getDup() : new ArrayList<>();
        for (TNFe.InfNFe.Cobr.Dup duplicata : duplicatas) {
            NfeDuplicata dup = new NfeDuplicata();
            dup.setNumero(duplicata.getNDup());
            dup.setDataVencimento(FormatValor.getInstance().formatarDataEUA(duplicata.getDVenc()));
            dup.setValor(FormatValor.getInstance().formatarValorToBigDecimal(duplicata.getVDup()));
            dup.setNfeCabecalho(nfeCabecalho);
            listaDuplicatas.add(dup);
        }


        if (tipoImportacao == TipoImportacaoXml.DEVOLUCAO) {
            NfeReferenciada nfeReferenciada = new NfeReferenciada();
            nfeReferenciada.setChaveAcesso(infNfe.getId().replaceAll("[NFCe]", ""));
            nfeReferenciada.setNfeCabecalho(nfeCabecalho);
            listaNfeReferenciada.add(nfeReferenciada);


            Set<NfeFormaPagamento> pagamentos = new HashSet<>();


            TipoPagamento tipo = new TipoPagamento().buscarPorCodigo("90");
            NfeFormaPagamento forma = new NfeFormaPagamento();
            forma.setValor(BigDecimal.ZERO);
            forma.setForma("90");
            forma.setTipoPagamento(tipo);
            forma.setNfeCabecalho(nfeCabecalho);
            pagamentos.add(forma);

            nfeCabecalho.setListaNfeFormaPagamento(pagamentos);
        }


        map.put("cabecalho", nfeCabecalho);
        map.put("detalhe", listaNfeDetalhe);
        map.put("emitente", emitente);
        map.put("duplicata", listaDuplicatas);
        map.put("destinatario", destinatario);
        map.put("nfeReferenciada", listaNfeReferenciada);

        return map;
    }


    private NfeEmitente getEmitente(TNFe.InfNFe.Emit emit) {
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
        emitente.setCodigoPais(1058);
        emitente.setNomePais("BRASIL");
        emitente.setTelefone(emit.getEnderEmit().getFone());
        emitente.setInscricaoEstadualSt(emit.getIEST());
        emitente.setInscricaoMunicipal(emit.getIM());
        emitente.setCrt(emit.getCRT());
        emitente.setCnae(emit.getCNAE());

        return emitente;
    }

    private NfeDestinatario getDestinatario(TNFe.InfNFe.Emit emit) {

        NfeDestinatario destinatario = new NfeDestinatario();


        destinatario.setCpfCnpj(emit.getCNPJ());
        destinatario.setInscricaoEstadual(emit.getIE());
        destinatario.setNome(emit.getXNome());
        destinatario.setLogradouro(emit.getEnderEmit().getXLgr());
        destinatario.setNumero(emit.getEnderEmit().getNro());
        destinatario.setComplemento(emit.getEnderEmit().getXCpl());
        destinatario.setBairro(emit.getEnderEmit().getXBairro());
        destinatario.setCodigoMunicipio(Integer.valueOf(emit.getEnderEmit().getCMun()));
        destinatario.setNomeMunicipio(emit.getEnderEmit().getXMun());
        destinatario.setUf(emit.getEnderEmit().getUF().value());
        destinatario.setCep(emit.getEnderEmit().getCEP());

        destinatario.setCodigoPais(
                Integer.valueOf(emit.getEnderEmit().getCPais() == null ? "1058" : emit.getEnderEmit().getCPais()));
        destinatario.setNomePais(emit.getEnderEmit().getXPais());
        destinatario.setTelefone(emit.getEnderEmit().getFone());
        destinatario.setInscricaoMunicipal(emit.getIM());

        return destinatario;
    }

    private NfeDestinatario getDestinatario(TNFe.InfNFe.Dest dest) {

        NfeDestinatario destinatario = new NfeDestinatario();

        destinatario.setCpfCnpj(dest.getCNPJ());
        destinatario.setInscricaoEstadual(dest.getIE());
        destinatario.setNome(dest.getXNome());
        destinatario.setLogradouro(dest.getEnderDest().getXLgr());
        destinatario.setNumero(dest.getEnderDest().getNro());
        destinatario.setComplemento(dest.getEnderDest().getXCpl());
        destinatario.setBairro(dest.getEnderDest().getXBairro());
        destinatario.setCodigoMunicipio(Integer.valueOf(dest.getEnderDest().getCMun()));
        destinatario.setNomeMunicipio(dest.getEnderDest().getXMun());
        destinatario.setUf(dest.getEnderDest().getUF().value());
        destinatario.setCep(dest.getEnderDest().getCEP());
        destinatario.setCodigoPais(1058);
        destinatario.setNomePais("BRASIL");
        destinatario.setTelefone(dest.getEnderDest().getFone());
        destinatario.setInscricaoMunicipal(dest.getIM());


        return destinatario;
    }

    private NfeDetalhe getNfeDetalhe(TNFe.InfNFe.Det det) throws ParseException {
        NfeDetalhe item = new NfeDetalhe();

        TNFe.InfNFe.Det.Prod prod = det.getProd();


        item.setNumeroItem(Integer.valueOf(det.getNItem()));
        item.setCodigoProduto(prod.getCProd());
        String gtin = prod.getCEAN() != null && !prod.getCEAN().equals("SEM GTIN") ? prod.getCEAN() : null;
        item.setGtin(gtin);
        item.setNomeProduto(prod.getXProd());
        item.setNcm(prod.getNCM());
        item.setCest(prod.getCEST());
        if (prod.getEXTIPI() != null) {
            item.setExTipi(Integer.valueOf(prod.getEXTIPI()));
        }
        item.setCfop(definirCfop(prod.getCFOP()));
        item.setUnidadeComercial(prod.getUCom());
        item.setQuantidadeComercial(FormatValor.getInstance().formatarQuantidadeToBigDecimal(prod.getQCom()));
        item.setValorUnitarioComercial(FormatValor.getInstance().formatarValorToBigDecimal(prod.getVUnCom()));
        item.setValorBrutoProduto(FormatValor.getInstance().formatarValorToBigDecimal(prod.getVProd()));
        item.setGtinUnidadeTributavel(gtin);
        item.setUnidadeTributavel(prod.getUTrib());
        item.setQuantidadeTributavel(FormatValor.getInstance().formatarQuantidadeToBigDecimal(prod.getQTrib()));
        item.setValorUnitarioTributavel(FormatValor.getInstance().formatarQuantidadeToBigDecimal(prod.getVUnTrib()));
//        item.setListaArmamento(new HashSet<>());
//        item.setListaMedicamento(new HashSet<>());
//        item.setListaDeclaracaoImportacao(new HashSet<>());
        item.setValorFrete(FormatValor.getInstance().formatarValorToBigDecimal(prod.getVFrete()));
        item.setValorSeguro(FormatValor.getInstance().formatarValorToBigDecimal(prod.getVSeg()));
        item.setValorDesconto(FormatValor.getInstance().formatarValorToBigDecimal(prod.getVDesc()));
        item.setValorOutrasDespesas(FormatValor.getInstance().formatarValorToBigDecimal(prod.getVOutro()));

        item.setEntraTotal(Integer.valueOf(prod.getIndTot()));
        item.setValorSubtotal(item.calcularSubTotalProduto());
        item.setValorTotal(item.calcularValorTotalProduto());
        item.setInformacoesAdicionais(det.getInfAdProd());

        //ICMS
        TNFe.InfNFe.Det.Imposto.ICMS icms = getTipoImposto(TNFe.InfNFe.Det.Imposto.ICMS.class, det.getImposto().getContent());
        NfeDetalheImpostoIcms impIcms = getIcms(icms);
        impIcms.setNfeDetalhe(item);
        item.setNfeDetalheImpostoIcms(impIcms);

        //IPI
        TIpi ipi = getTipoImposto(TIpi.class, det.getImposto().getContent());
        if (ipi != null) {
            NfeDetalheImpostoIpi impIpi = getIpi(ipi);
            impIpi.setNfeDetalhe(item);
            item.setNfeDetalheImpostoIpi(impIpi);
        }


        //PIS
        TNFe.InfNFe.Det.Imposto.PIS pis = getTipoImposto(TNFe.InfNFe.Det.Imposto.PIS.class, det.getImposto().getContent());
        NfeDetalheImpostoPis impPis = getPis(pis);
        impPis.setNfeDetalhe(item);
        item.setNfeDetalheImpostoPis(impPis);

        //COFINS
        TNFe.InfNFe.Det.Imposto.COFINS cofins = getTipoImposto(TNFe.InfNFe.Det.Imposto.COFINS.class, det.getImposto().getContent());
        NfeDetalheImpostoCofins impCofins = getCofins(cofins);
        impCofins.setNfeDetalhe(item);
        item.setNfeDetalheImpostoCofins(impCofins);


        return item;
    }


    private <T> T getTipoImposto(Class<T> imposto, List<JAXBElement<?>> classToCast) {


        imp = imposto.getSimpleName();
        imp = imp.equals("TIpi") ? "IPI" : imp;
        JAXBElement<?> element = classToCast.stream().filter(i -> i.getName().toString().contains(imp)).findFirst().orElse(null);

        return element != null ? (T) element.getValue() : null;
    }

    private NfeDetalheImpostoIcms getIcms(TNFe.InfNFe.Det.Imposto.ICMS icms) throws ParseException {
        NfeDetalheImpostoIcms impostoIcms = new NfeDetalheImpostoIcms();
        if (icms == null) {
            return impostoIcms;
        }
        if (icms.getICMS00() != null) {
            impostoIcms.setOrigemMercadoria(Integer.valueOf(icms.getICMS00().getOrig()));
            impostoIcms.setCstIcms(icms.getICMS00().getCST());
            impostoIcms.setModalidadeBcIcms(Integer.valueOf(icms.getICMS00().getModBC()));
            impostoIcms.setBaseCalculoIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS00().getVBC()));
            impostoIcms.setAliquotaIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS00().getPICMS()));
            impostoIcms.setValorIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS00().getVICMS()));

            if (icms.getICMS00().getVFCP() != null) {
                impostoIcms.setPercentualFcp(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS00().getPFCP()));
                impostoIcms.setValorFcp(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS00().getVFCP()));
            }

        }
        if (icms.getICMS10() != null) {
            impostoIcms.setOrigemMercadoria(Integer.valueOf(icms.getICMS10().getOrig()));
            impostoIcms.setCstIcms(icms.getICMS10().getCST());
            impostoIcms.setModalidadeBcIcms(Integer.valueOf(icms.getICMS10().getModBC()));
            impostoIcms.setBaseCalculoIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS10().getVBC()));
            impostoIcms.setAliquotaIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS10().getPICMS()));
            impostoIcms.setValorIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS10().getVICMS()));

            impostoIcms.setModalidadeBcIcmsSt(Integer.valueOf(icms.getICMS10().getModBCST()));
            impostoIcms.setPercentualMvaIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS10().getPMVAST()));
            impostoIcms.setAliquotaIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS10().getPICMSST()));
            impostoIcms.setValorBaseCalculoIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS10().getVBCST()));
            impostoIcms.setValorIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS10().getVICMSST()));
        }
        if (icms.getICMS20() != null) {
            impostoIcms.setOrigemMercadoria(Integer.valueOf(icms.getICMS20().getOrig()));
            impostoIcms.setCstIcms(icms.getICMS20().getCST());
            impostoIcms.setModalidadeBcIcms(Integer.valueOf(icms.getICMS20().getModBC()));
            impostoIcms.setBaseCalculoIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS20().getVBC()));
            impostoIcms.setAliquotaIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS20().getPICMS()));
            impostoIcms.setValorIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS20().getVICMS()));
            impostoIcms.setTaxaReducaoBcIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS20().getPRedBC()));
        }
        if (icms.getICMS30() != null) {
            impostoIcms.setOrigemMercadoria(Integer.valueOf(icms.getICMS30().getOrig()));
            impostoIcms.setCstIcms(icms.getICMS30().getCST());
            impostoIcms.setModalidadeBcIcmsSt(Integer.valueOf(icms.getICMS30().getModBCST()));
            impostoIcms.setPercentualMvaIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS30().getPMVAST()));
            impostoIcms.setPercentualReducaoBcIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS30().getPRedBCST()));
            impostoIcms.setValorBaseCalculoIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS30().getVBCST()));
            impostoIcms.setAliquotaIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS30().getPICMSST()));
            impostoIcms.setValorIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS30().getVICMSST()));
        }
        if (icms.getICMS40() != null) {
            impostoIcms.setOrigemMercadoria(Integer.valueOf(icms.getICMS40().getOrig()));
            impostoIcms.setCstIcms(icms.getICMS40().getCST());
            impostoIcms.setValorIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS40().getVICMSDeson()));
            if (icms.getICMS40().getMotDesICMS() != null) {
                impostoIcms.setMotivoDesoneracaoIcms(Integer.valueOf(icms.getICMS40().getMotDesICMS()));
            }

        }
        if (icms.getICMS51() != null) {
            impostoIcms.setOrigemMercadoria(Integer.valueOf(icms.getICMS51().getOrig()));
            impostoIcms.setCstIcms(icms.getICMS51().getCST());
            impostoIcms.setModalidadeBcIcms(Integer.valueOf(icms.getICMS51().getModBC()));
            impostoIcms.setBaseCalculoIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS51().getVBC()));
            impostoIcms.setAliquotaIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS51().getPICMS()));
            impostoIcms.setValorIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS51().getVICMS()));
            impostoIcms.setTaxaReducaoBcIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS51().getPRedBC()));
        }
        if (icms.getICMS60() != null) {
            impostoIcms.setOrigemMercadoria(Integer.valueOf(icms.getICMS60().getOrig()));
            impostoIcms.setCstIcms(icms.getICMS60().getCST());
            impostoIcms.setValorIcmsStRetido(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS60().getVICMSSTRet() == null ? "0,00" : icms.getICMS60().getVICMSSTRet()));
            impostoIcms.setValorBcIcmsStRetido(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS60().getVBCSTRet() == null ? "0,00" : icms.getICMS60().getVBCSTRet()));
        }
        if (icms.getICMS70() != null) {
            impostoIcms.setOrigemMercadoria(Integer.valueOf(icms.getICMS70().getOrig()));
            impostoIcms.setCstIcms(icms.getICMS70().getCST());
            impostoIcms.setModalidadeBcIcms(Integer.valueOf(icms.getICMS70().getModBCST()));
            impostoIcms.setTaxaReducaoBcIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS70().getPRedBC()));
            impostoIcms.setBaseCalculoIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS70().getVBC()));
            impostoIcms.setAliquotaIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS70().getPICMS()));
            impostoIcms.setValorIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS70().getVICMS()));
            impostoIcms.setModalidadeBcIcmsSt(Integer.valueOf(icms.getICMS70().getModBCST()));
            impostoIcms.setPercentualMvaIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS70().getPMVAST()));
            impostoIcms.setPercentualReducaoBcIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS70().getPRedBCST()));
            impostoIcms.setValorBaseCalculoIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS70().getVBCST()));
            impostoIcms.setAliquotaIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS70().getPICMSST()));
            impostoIcms.setValorIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS70().getVICMSST()));
        }
        if (icms.getICMS90() != null) {
            impostoIcms.setOrigemMercadoria(Integer.valueOf(icms.getICMS90().getOrig()));
            impostoIcms.setCstIcms(icms.getICMS90().getCST());
            if (icms.getICMS90().getModBCST() != null) {
                impostoIcms.setModalidadeBcIcms(Integer.valueOf(icms.getICMS90().getModBCST()));
            }

            impostoIcms.setTaxaReducaoBcIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS90().getPRedBC()));
            impostoIcms.setBaseCalculoIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS90().getVBC()));
            impostoIcms.setAliquotaIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS90().getPICMS()));
            impostoIcms.setValorIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS90().getVICMS()));
            if (icms.getICMS90().getModBCST() != null) {
                impostoIcms.setModalidadeBcIcmsSt(Integer.valueOf(icms.getICMS90().getModBCST()));
            }

            impostoIcms.setPercentualMvaIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS90().getPMVAST()));
            impostoIcms.setPercentualReducaoBcIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS90().getPRedBCST()));
            impostoIcms.setValorBaseCalculoIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS90().getVBCST()));
            impostoIcms.setAliquotaIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS90().getPICMSST()));
            impostoIcms.setValorIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMS90().getVICMSST()));
        }
        if (icms.getICMSPart() != null) {
            impostoIcms.setOrigemMercadoria(Integer.valueOf(icms.getICMSPart().getOrig()));
            impostoIcms.setCstIcms(icms.getICMSPart().getCST());
            impostoIcms.setModalidadeBcIcms((Integer.valueOf(icms.getICMSPart().getModBCST())));
            impostoIcms.setTaxaReducaoBcIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSPart().getPRedBC()));
            impostoIcms.setBaseCalculoIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSPart().getVBC()));
            impostoIcms.setAliquotaIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSPart().getPICMS()));
            impostoIcms.setValorIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSPart().getVICMS()));
            impostoIcms.setModalidadeBcIcmsSt(Integer.valueOf(icms.getICMSPart().getModBCST()));
            impostoIcms.setPercentualMvaIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSPart().getPMVAST()));
            impostoIcms.setPercentualReducaoBcIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSPart().getPRedBCST()));
            impostoIcms.setValorBaseCalculoIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSPart().getVBCST()));
            impostoIcms.setAliquotaIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSPart().getPICMSST()));
            impostoIcms.setValorIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSPart().getVICMSST()));
        }
        if (icms.getICMSST() != null) {
            impostoIcms.setOrigemMercadoria(Integer.valueOf(icms.getICMSST().getOrig()));
            impostoIcms.setCstIcms(icms.getICMSST().getCST());
            impostoIcms.setValorBcIcmsStRetido(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSST().getVBCSTRet()));
            impostoIcms.setValorIcmsStRetido(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSST().getVICMSSTRet()));
            impostoIcms.setValorBcIcmsStDestino(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSST().getVBCSTDest()));
            impostoIcms.setValorIcmsStDestino(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSST().getVICMSSTDest()));
        }
        if (icms.getICMSSN101() != null) {
            impostoIcms.setOrigemMercadoria(Integer.valueOf(icms.getICMSSN101().getOrig()));
            impostoIcms.setCsosn(icms.getICMSSN101().getCSOSN());
            impostoIcms.setAliquotaCreditoIcmsSn(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSSN101().getPCredSN()));
            impostoIcms.setValorCreditoIcmsSn(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSSN101().getVCredICMSSN()));
        }
        if (icms.getICMSSN102() != null) {
            impostoIcms.setOrigemMercadoria(Integer.valueOf(icms.getICMSSN102().getOrig()));
            impostoIcms.setCsosn(icms.getICMSSN102().getCSOSN());
        }
        if (icms.getICMSSN201() != null) {
            impostoIcms.setOrigemMercadoria(Integer.valueOf(icms.getICMSSN201().getOrig()));
            impostoIcms.setCsosn(icms.getICMSSN201().getCSOSN());
            impostoIcms.setModalidadeBcIcmsSt(Integer.valueOf(icms.getICMSSN201().getModBCST()));
            impostoIcms.setPercentualMvaIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSSN201().getPMVAST()));
            impostoIcms.setPercentualReducaoBcIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSSN201().getPRedBCST()));
            impostoIcms.setValorBaseCalculoIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSSN201().getVBCST()));
            impostoIcms.setAliquotaIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSSN201().getPICMSST()));
            impostoIcms.setValorIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSSN201().getVICMSST()));
            impostoIcms.setAliquotaCreditoIcmsSn(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSSN201().getPCredSN()));
            impostoIcms.setValorCreditoIcmsSn(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSSN201().getVCredICMSSN()));
        }
        if (icms.getICMSSN202() != null) {
            impostoIcms.setOrigemMercadoria(Integer.valueOf(icms.getICMSSN202().getOrig()));
            impostoIcms.setCsosn(icms.getICMSSN202().getCSOSN());
            impostoIcms.setModalidadeBcIcmsSt(Integer.valueOf(icms.getICMSSN202().getModBCST()));
            impostoIcms.setPercentualMvaIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSSN202().getPMVAST()));
            impostoIcms.setPercentualReducaoBcIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSSN202().getPRedBCST()));
            impostoIcms.setValorBaseCalculoIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSSN202().getVBCST()));
            impostoIcms.setAliquotaIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSSN202().getPICMSST()));
            impostoIcms.setValorIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSSN202().getVICMSST()));
        }
        if (icms.getICMSSN500() != null) {
            impostoIcms.setOrigemMercadoria(Integer.valueOf(icms.getICMSSN500().getOrig()));
            impostoIcms.setCsosn(icms.getICMSSN500().getCSOSN());
            impostoIcms.setValorBcIcmsStRetido(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSSN500().getVBCSTRet()));
            impostoIcms.setValorIcmsStRetido(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSSN500().getVICMSSTRet()));
        }
        if (icms.getICMSSN900() != null) {
            impostoIcms.setOrigemMercadoria(Integer.valueOf(icms.getICMSSN900().getOrig()));
            impostoIcms.setCsosn(icms.getICMSSN900().getCSOSN());
            impostoIcms.setModalidadeBcIcms(Integer.valueOf(icms.getICMSSN900().getModBCST()));
            impostoIcms.setTaxaReducaoBcIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSSN900().getPRedBC()));
            impostoIcms.setBaseCalculoIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSSN900().getVBC()));
            impostoIcms.setAliquotaIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSSN900().getPICMS()));
            impostoIcms.setValorIcms(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSSN900().getVICMS()));
            impostoIcms.setModalidadeBcIcmsSt(Integer.valueOf(icms.getICMSSN900().getModBCST()));
            impostoIcms.setPercentualMvaIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSSN900().getPMVAST()));
            impostoIcms.setPercentualReducaoBcIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSSN900().getPRedBCST()));
            impostoIcms.setValorBaseCalculoIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSSN900().getVBCST()));
            impostoIcms.setAliquotaIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSSN900().getPICMSST()));
            impostoIcms.setValorIcmsSt(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSSN900().getVICMSST()));
            impostoIcms.setAliquotaCreditoIcmsSn(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSSN900().getPCredSN()));
            impostoIcms.setValorCreditoIcmsSn(FormatValor.getInstance().formatarValorToBigDecimal(icms.getICMSSN900().getVCredICMSSN()));
        }

        return impostoIcms;

    }


    private NfeDetalheImpostoIpi getIpi(TIpi ipi) throws ParseException {
        NfeDetalheImpostoIpi impostoIpi = new NfeDetalheImpostoIpi();

        if (ipi == null) {
            return impostoIpi;
        }

        TIpi.IPITrib ipiTrib = ipi.getIPITrib();
        TIpi.IPINT ipInt = ipi.getIPINT();

        String cst = ipiTrib != null ? ipiTrib.getCST() : ipInt.getCST();
        impostoIpi.setEnquadramentoIpi(ipi.getCEnq());
        impostoIpi.setCstIpi(cst);
        if (ipiTrib != null) {
            impostoIpi.setValorBaseCalculoIpi(FormatValor.getInstance().formatarValorToBigDecimal(ipiTrib.getVBC()));
            impostoIpi.setAliquotaIpi(FormatValor.getInstance().formatarValorToBigDecimal(ipiTrib.getPIPI()));
            impostoIpi.setQuantidadeUnidadeTributavel(FormatValor.getInstance().formatarValorToBigDecimal(ipiTrib.getQUnid()));
            impostoIpi.setValorUnidadeTributavel(FormatValor.getInstance().formatarValorToBigDecimal(ipiTrib.getVUnid()));
            impostoIpi.setValorIpi(FormatValor.getInstance().formatarValorToBigDecimal(ipiTrib.getVIPI()));
        } else {
            impostoIpi.setValorBaseCalculoIpi(BigDecimal.ZERO);
            impostoIpi.setAliquotaIpi(BigDecimal.ZERO);
            impostoIpi.setQuantidadeUnidadeTributavel(BigDecimal.ZERO);
            impostoIpi.setValorUnidadeTributavel(BigDecimal.ZERO);
            impostoIpi.setValorIpi(BigDecimal.ZERO);
        }


        return impostoIpi;
    }

    private NfeDetalheImpostoPis getPis(TNFe.InfNFe.Det.Imposto.PIS pis) throws ParseException {
        NfeDetalheImpostoPis impostoPis = new NfeDetalheImpostoPis();

        if (pis == null) {
            return impostoPis;
        }

        if (pis.getPISAliq() != null) {
            impostoPis.setCstPis(pis.getPISAliq().getCST());
            impostoPis.setValorBaseCalculoPis(FormatValor.getInstance().formatarValorToBigDecimal(pis.getPISAliq().getVBC()));
            impostoPis.setAliquotaPisPercentual(FormatValor.getInstance().formatarValorToBigDecimal(pis.getPISAliq().getPPIS()));
            impostoPis.setValorPis(FormatValor.getInstance().formatarValorToBigDecimal(pis.getPISAliq().getVPIS()));
        }
        if (pis.getPISQtde() != null) {
            impostoPis.setCstPis(pis.getPISQtde().getCST());
            impostoPis.setQuantidadeVendida(FormatValor.getInstance().formatarValorToBigDecimal(pis.getPISQtde().getQBCProd()));
            impostoPis.setAliquotaPisReais(FormatValor.getInstance().formatarValorToBigDecimal(pis.getPISQtde().getVAliqProd()));
            impostoPis.setValorPis(FormatValor.getInstance().formatarValorToBigDecimal(pis.getPISQtde().getVPIS()));
        }
        if (pis.getPISNT() != null) {
            impostoPis.setCstPis(pis.getPISNT().getCST());
        }
        if (pis.getPISOutr() != null) {
            impostoPis.setCstPis(pis.getPISOutr().getCST());
            impostoPis.setValorBaseCalculoPis(FormatValor.getInstance().formatarValorToBigDecimal(pis.getPISOutr().getVBC()));
            impostoPis.setAliquotaPisPercentual(FormatValor.getInstance().formatarValorToBigDecimal(pis.getPISOutr().getPPIS()));
            impostoPis.setQuantidadeVendida(FormatValor.getInstance().formatarValorToBigDecimal(pis.getPISOutr().getQBCProd() == null ? "0" : pis.getPISOutr().getQBCProd()));
            impostoPis.setAliquotaPisReais(FormatValor.getInstance().formatarValorToBigDecimal(pis.getPISOutr().getVAliqProd()));
            impostoPis.setValorPis(FormatValor.getInstance().formatarValorToBigDecimal(pis.getPISOutr().getVPIS()));
        }

        return impostoPis;
    }

    private NfeDetalheImpostoCofins getCofins(TNFe.InfNFe.Det.Imposto.COFINS cofins) throws ParseException {
        NfeDetalheImpostoCofins impostoCofins = new NfeDetalheImpostoCofins();

        if (cofins == null) {
            return impostoCofins;
        }

        if (cofins.getCOFINSAliq() != null) {
            impostoCofins.setCstCofins(cofins.getCOFINSAliq().getCST());
            impostoCofins.setBaseCalculoCofins(FormatValor.getInstance().formatarValorToBigDecimal(cofins.getCOFINSAliq().getVBC()));
            impostoCofins.setAliquotaCofinsPercentual(FormatValor.getInstance().formatarValorToBigDecimal(cofins.getCOFINSAliq().getPCOFINS()));
            impostoCofins.setValorCofins(FormatValor.getInstance().formatarValorToBigDecimal(cofins.getCOFINSAliq().getVCOFINS()));
        }
        if (cofins.getCOFINSQtde() != null) {
            impostoCofins.setCstCofins(cofins.getCOFINSQtde().getCST());
            impostoCofins.setQuantidadeVendida(FormatValor.getInstance().formatarValorToBigDecimal(cofins.getCOFINSQtde().getQBCProd()));
            impostoCofins.setAliquotaCofinsReais(FormatValor.getInstance().formatarValorToBigDecimal(cofins.getCOFINSQtde().getVAliqProd()));
            impostoCofins.setValorCofins(FormatValor.getInstance().formatarValorToBigDecimal(cofins.getCOFINSQtde().getVCOFINS()));
        }
        if (cofins.getCOFINSNT() != null) {
            impostoCofins.setCstCofins(cofins.getCOFINSNT().getCST());
        }
        if (cofins.getCOFINSOutr() != null) {
            impostoCofins.setCstCofins(cofins.getCOFINSOutr().getCST());
            impostoCofins.setBaseCalculoCofins(FormatValor.getInstance().formatarValorToBigDecimal(cofins.getCOFINSOutr().getVBC()));
            impostoCofins.setAliquotaCofinsPercentual(FormatValor.getInstance().formatarValorToBigDecimal(cofins.getCOFINSOutr().getPCOFINS()));
            impostoCofins.setQuantidadeVendida(FormatValor.getInstance().formatarValorToBigDecimal(cofins.getCOFINSOutr().getQBCProd()));
            impostoCofins.setAliquotaCofinsReais(FormatValor.getInstance().formatarValorToBigDecimal(cofins.getCOFINSOutr().getVAliqProd()));
            impostoCofins.setValorCofins(FormatValor.getInstance().formatarValorToBigDecimal(cofins.getCOFINSOutr().getVCOFINS()));
        }

        return impostoCofins;
    }


    private Integer definirCfop(String cfop) {
        int cfopDetino = Integer.valueOf(cfop);
        for (ConverterCfop item : cfopList) {
            if (item.getCfopOrigem() == cfopDetino) {
                cfopDetino = item.getCfopDestino();
                break;
            }
        }

        return cfopDetino;
    }


}
