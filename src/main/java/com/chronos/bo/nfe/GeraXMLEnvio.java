package com.chronos.bo.nfe;

/**
 * Created by john on 26/09/17.
 */

//pacotes para envio da nfe

import br.inf.portalfiscal.nfe.schema.consstatserv.TConsStatServ;
import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TEnvEvento;
import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TEvento;
import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TEvento.InfEvento;
import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TEvento.InfEvento.DetEvento;
import br.inf.portalfiscal.nfe.schema.envinfe.*;
import br.inf.portalfiscal.nfe.schema.envinfe.TNFe.InfNFe;
import br.inf.portalfiscal.nfe.schema.envinfe.TNFe.InfNFe.*;
import br.inf.portalfiscal.nfe.schema.envinfe.TNFe.InfNFe.Det.Imposto;
import br.inf.portalfiscal.nfe.schema.envinfe.TNFe.InfNFe.Det.Imposto.*;
import br.inf.portalfiscal.nfe.schema.envinfe.TNFe.InfNFe.Det.Prod;
import br.inf.portalfiscal.nfe.schema.envinfe.TNFe.InfNFeSupl;
import br.inf.portalfiscal.nfe.schema.inutnfe.TInutNFe;
import br.inf.portalfiscal.nfe.schema.inutnfe.TInutNFe.InfInut;
import br.inf.portalfiscal.nfe.schema.retconsstatserv.TRetConsStatServ;
import com.chronos.calc.enuns.OrigemMercadoria;
import com.chronos.infra.enuns.AmbienteEmissao;
import com.chronos.infra.enuns.IndicadorIe;
import com.chronos.infra.enuns.ModalidadeFrete;
import com.chronos.infra.enuns.ModeloDocumento;
import com.chronos.modelo.entidades.*;
import com.chronos.nfe.Nfe;
import com.chronos.util.ConstantesNFe;
import com.chronos.util.FormatValor;
import com.chronos.util.NFCeUtil;
import com.chronos.util.WebServiceUtil;
import org.springframework.util.StringUtils;

import javax.swing.text.MaskFormatter;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.Set;

//cancelar
//inutilizar

public class GeraXMLEnvio {


    private ModeloDocumento modelo;
    private Empresa empresa;
    private EmpresaEndereco endereco;
    private NfeCabecalho nfeCabecalho;
    private AmbienteEmissao ambienteEmissao;
    private IndicadorIe tipoContribuinte;
    private boolean temProduto;
    private BigDecimal valorTotalTributos;

    public TEnviNFe gerarXmlEnvio(Empresa empresa, NfeCabecalho nfeCabecalho) throws Exception {
        this.empresa = empresa;
        this.nfeCabecalho = nfeCabecalho;
        endereco = empresa.buscarEnderecoPrincipal();
        modelo = ModeloDocumento.getByCodigo(Integer.valueOf(nfeCabecalho.getCodigoModelo()));
        ambienteEmissao = AmbienteEmissao.getByCodigo(nfeCabecalho.getAmbiente());
        valorTotalTributos = BigDecimal.ZERO;
        TNFe nfe = new TNFe();
        InfNFe infNfe = getInfNFe();
        nfe.setInfNFe(infNfe);

        // cabecalho
        TNFe.InfNFe.Ide ide = getIde();
        nfe.getInfNFe().setIde(ide);

        // emitente
        TNFe.InfNFe.Emit emit = getEmit(nfeCabecalho.getEmitente());
        nfe.getInfNFe().setEmit(emit);

        // Info Add
        TNFe.InfNFe.InfAdic infAdic = getInfAdic();
        nfe.getInfNFe().setInfAdic(infAdic);


        // destinatario
        TNFe.InfNFe.Dest dest = getDest(nfeCabecalho.getDestinatario());
        nfe.getInfNFe().setDest(dest);

        // Grupo de informacao das NF/NF-e referenciadas
        // NF-e Referenciadas
        if (modelo == ModeloDocumento.NFE && !nfeCabecalho.getListaNfeReferenciada().isEmpty()) {
            addNFref(nfeCabecalho.getListaNfeReferenciada(), ide);
        }

        // Transporte
        TNFe.InfNFe.Transp transp = getTransp(nfeCabecalho.getTransporte());
        infNfe.setTransp(transp);

        // Fatura
        TNFe.InfNFe.Cobr cobr = getCobr(nfeCabecalho.getFatura(), nfeCabecalho.getListaDuplicata());
        infNfe.setCobr(cobr);

        // detalhes
        for (NfeDetalhe nfeDetalhe : nfeCabecalho.getListaNfeDetalhe()) {
            boolean servico = nfeDetalhe.getProduto().getServico() != null && nfeDetalhe.getProduto().getServico().equals("S");
            if (!servico && !temProduto) {
                temProduto = true;
            }
            Det det = addDetalheNfe(nfeDetalhe, servico);
            nfe.getInfNFe().getDet().add(det);

            if (nfeDetalhe.getValorTotalTributos() != null && nfeDetalhe.getValorTotalTributos().signum() >= 0) {
                valorTotalTributos = valorTotalTributos.add(nfeDetalhe.getValorTotalTributos());
            }
        }
        if (ambienteEmissao == AmbienteEmissao.HOMOLOCACAO) {
            nfe.getInfNFe().getDet().get(0).getProd()
                    .setXProd("NOTA FISCAL EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL");
        }

        // NFe Cabeçalho -- Totais
        Total total = getTotais(nfeCabecalho);
        infNfe.setTotal(total);


        // Monta EnviNfe
        TEnviNFe enviNFe = new TEnviNFe();
        enviNFe.setVersao("3.10");
        enviNFe.setIdLote("1");
        enviNFe.setIndSinc("1");
        enviNFe.getNFe().add(nfe);

        // Monta e Assina o XML
        enviNFe = Nfe.montaNfe(enviNFe, false);

        if (modelo == ModeloDocumento.NFCE) {
            String url = WebServiceUtil.getUrl(ConstantesNFe.NFCE, ConstantesNFe.SERVICOS.URL_QRCODE);
            // QRCODE
            String qrCode = NFCeUtil.getCodeQRCode(infNfe.getId().substring(3), "100",
                    nfeCabecalho.getAmbiente().toString(), nfeCabecalho.getDestinatario() == null ? "" : nfeCabecalho.getDestinatario().getCpfCnpj(),
                    ide.getDhEmi(), total.getICMSTot().getVNF(), total.getICMSTot().getVICMS(),
                    Base64.getEncoder().encodeToString(
                            enviNFe.getNFe().get(0).getSignature().getSignedInfo().getReference().getDigestValue()),
                    "000001", nfeCabecalho.getCsc(), url);
            InfNFeSupl infNFeSupl = new InfNFeSupl();
            infNFeSupl.setQrCode(qrCode);
            enviNFe.getNFe().get(0).setInfNFeSupl(infNFeSupl);

            Pag pag = new Pag();
            pag.setTPag("01");
            pag.setVPag(FormatValor.getInstance().formatarValor(nfeCabecalho.getValorTotal()));
            infNfe.getPag().add(pag);
        }


        return enviNFe;
    }

    public TEnvEvento cancelarNfe(String chave, String protocolo, String ambiente, String uf, String cnpj,
                                  String justificativa) throws ParseException {


        String id = "ID110111" + chave + "01";
        Date data = new Date();
        TEnvEvento enviEvento = new TEnvEvento();
        enviEvento.setVersao("1.00");
        enviEvento.setIdLote("1");

        TEvento eventoCancela = new TEvento();
        eventoCancela.setVersao("1.00");

        InfEvento infoEvento = new InfEvento();
        infoEvento.setId(id);
        infoEvento.setChNFe(chave);
        infoEvento.setCOrgao(uf);
        infoEvento.setTpAmb(ambiente);

        infoEvento.setCNPJ(cnpj);

        infoEvento.setDhEvento(FormatValor.getInstance().formatarDataNota(data));
        infoEvento.setTpEvento("110111");
        infoEvento.setNSeqEvento("1");
        infoEvento.setVerEvento("1.00");

        DetEvento detEvento = new DetEvento();
        detEvento.setVersao("1.00");
        detEvento.setDescEvento("Cancelamento");
        detEvento.setNProt(protocolo);
        detEvento.setXJust(justificativa);
        infoEvento.setDetEvento(detEvento);
        eventoCancela.setInfEvento(infoEvento);
        enviEvento.getEvento().add(eventoCancela);

        return enviEvento;
    }

    public br.inf.portalfiscal.nfe.schema.envcce.TEnvEvento cartaCorrecao(String chave, String ambiente,
                                                                          String codigoUf, String cnpj, String correcao) throws ParseException {

        String id = "ID110110" + chave + "01";

        br.inf.portalfiscal.nfe.schema.envcce.TEnvEvento envEvento = new br.inf.portalfiscal.nfe.schema.envcce.TEnvEvento();
        envEvento.setVersao("1.00");
        envEvento.setIdLote("1");

        br.inf.portalfiscal.nfe.schema.envcce.TEvento evento = new br.inf.portalfiscal.nfe.schema.envcce.TEvento();
        evento.setVersao("1.00");

        br.inf.portalfiscal.nfe.schema.envcce.TEvento.InfEvento infEvento = new br.inf.portalfiscal.nfe.schema.envcce.TEvento.InfEvento();
        infEvento.setId(id);
        infEvento.setCOrgao(codigoUf);
        infEvento.setTpAmb(ambiente);

        infEvento.setCNPJ(cnpj);
        infEvento.setChNFe(chave);

        infEvento.setDhEvento(FormatValor.getInstance().formatarDataNota(new Date()));
        infEvento.setTpEvento("110110");
        infEvento.setNSeqEvento("1");
        infEvento.setVerEvento("1.00");

        br.inf.portalfiscal.nfe.schema.envcce.TEvento.InfEvento.DetEvento detEvento = new br.inf.portalfiscal.nfe.schema.envcce.TEvento.InfEvento.DetEvento();
        detEvento.setVersao("1.00");
        detEvento.setDescEvento("Carta de Correcao");

        detEvento.setXCorrecao(correcao);
        detEvento.setXCondUso(
                "A Carta de Correcao e disciplinada pelo paragrafo 1o-A do art. 7o do Convenio S/N, de 15 de dezembro de 1970 e pode ser utilizada para regularizacao de erro ocorrido na emissao de documento fiscal, desde que o erro nao esteja relacionado com: I - as variaveis que determinam o valor do imposto tais como: base de calculo, aliquota, diferenca de preco, quantidade, valor da operacao ou da prestacao; II - a correcao de dados cadastrais que implique mudanca do remetente ou do destinatario; III - a data de emissao ou de saida.");
        infEvento.setDetEvento(detEvento);
        evento.setInfEvento(infEvento);
        envEvento.getEvento().add(evento);

        return envEvento;
    }


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

    private InfNFe getInfNFe() {
        InfNFe infNfe = new InfNFe();
        infNfe.setId("NFe" + nfeCabecalho.getChaveAcessoCompleta());
        infNfe.setVersao("3.10");

        return infNfe;
    }

    private Ide getIde() throws ParseException {
        TNFe.InfNFe.Ide ide = new TNFe.InfNFe.Ide();
        ide.setCUF(empresa.getCodigoIbgeUf().toString());
        ide.setCNF(nfeCabecalho.getCodigoNumerico());
        ide.setNatOp(nfeCabecalho.getNaturezaOperacao());
        ide.setIndPag(String.valueOf(nfeCabecalho.getIndicadorFormaPagamento()));
        ide.setMod(nfeCabecalho.getCodigoModelo());
        ide.setSerie(Integer.valueOf(nfeCabecalho.getSerie()).toString());
        ide.setNNF(Integer.valueOf(nfeCabecalho.getNumero()).toString());
        ide.setDhEmi(FormatValor.getInstance().formatarDataNota(nfeCabecalho.getDataHoraEmissao()));

        ide.setDhSaiEnt(modelo == ModeloDocumento.NFE
                ? FormatValor.getInstance().formatarDataNota(nfeCabecalho.getDataHoraEntradaSaida()) : null);
        ide.setTpNF(String.valueOf(nfeCabecalho.getTipoOperacao()));
        ide.setCMunFG(nfeCabecalho.getCodigoMunicipio().toString());
        ide.setTpImp(String.valueOf(nfeCabecalho.getFormatoImpressaoDanfe()));
        ide.setTpEmis(String.valueOf(nfeCabecalho.getTipoEmissao()));
        ide.setVerProc(nfeCabecalho.getVersaoProcessoEmissao());
        ide.setTpAmb(String.valueOf(nfeCabecalho.getAmbiente()));
        ide.setFinNFe(String.valueOf(nfeCabecalho.getFinalidadeEmissao()));
        ide.setProcEmi(String.valueOf(nfeCabecalho.getProcessoEmissao()));
        ide.setCDV(nfeCabecalho.getDigitoChaveAcesso());
        ide.setIdDest(String.valueOf(nfeCabecalho.getLocalDestino()));
        ide.setIndFinal(String.valueOf(nfeCabecalho.getConsumidorOperacao()));
        ide.setIndPres("1");

        return ide;
    }

    private Emit getEmit(NfeEmitente emitente) {
        TNFe.InfNFe.Emit emit = new Emit();
        String razao = emitente.getNome();
        emit.setCNPJ(emitente.getCpfCnpj());
        emit.setXNome((razao.length() >= 60) ? razao.substring(0, 59) : razao);
        emit.setXFant(emitente.getFantasia());
        emit.setEnderEmit(getEnderEmi(emitente));
        emit.setIE(emitente.getInscricaoEstadual());
        emit.setIEST(StringUtils.isEmpty(emitente.getInscricaoEstadualSt()) ? null : emitente.getInscricaoEstadualSt());
        emit.setIM(StringUtils.isEmpty(emitente.getInscricaoMunicipal()) ? null : emitente.getInscricaoMunicipal());
        emit.setCRT(emitente.getCrt());
        emit.setCNAE(StringUtils.isEmpty(emitente.getCnae()) ? null : emitente.getCnae());

        return emit;
    }

    private TEnderEmi getEnderEmi(NfeEmitente emitente) {
        TEnderEmi enderecoEmi = new TEnderEmi();
        enderecoEmi.setXLgr(emitente.getLogradouro());
        enderecoEmi.setNro(emitente.getNumero());
        enderecoEmi.setXCpl(emitente.getComplemento());
        enderecoEmi.setXBairro(emitente.getBairro());
        enderecoEmi.setCMun(emitente.getCodigoMunicipio().toString());
        enderecoEmi.setXMun(emitente.getNomeMunicipio());
        enderecoEmi.setUF(TUfEmi.fromValue(emitente.getUf()));
        enderecoEmi.setCEP(emitente.getCep());
        enderecoEmi.setCPais("1058");
        enderecoEmi.setXPais("BRASIL");
        enderecoEmi.setFone(emitente.getTelefone());
        return enderecoEmi;
    }

    private Dest getDest(NfeDestinatario destinatario) {
        Dest dest = null;
        if (destinatario != null && destinatario.getCpfCnpj() != null) {
            dest = new Dest();
            if (destinatario.getCpfCnpj().length() == 14) {
                dest.setCNPJ(destinatario.getCpfCnpj());
            } else {
                dest.setCPF(destinatario.getCpfCnpj());
            }
            if (ambienteEmissao == AmbienteEmissao.HOMOLOCACAO) {
                dest.setXNome("NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL");
            } else {
                dest.setXNome(destinatario.getNome());
            }
            if (destinatario.getLogradouro() != null) {
                dest.setEnderDest(getTEndereco(destinatario));
            }
            // NFC-e não pode ser emitida para contribuinte do ICMS
            if (modelo == ModeloDocumento.NFCE) {
                dest.setIndIEDest(IndicadorIe.NAO_CONTRIBUINTE.getCodigo().toString());
            } else if (destinatario.getCpfCnpj().length() == 14) {
                if (destinatario.getInscricaoEstadual() == null || destinatario.getInscricaoEstadual().equals("")) {
                    dest.setIndIEDest(IndicadorIe.NAO_CONTRIBUINTE.getCodigo().toString());

                } else if (destinatario.getInscricaoEstadual().equalsIgnoreCase("ISENTO")) {
                    dest.setIndIEDest(IndicadorIe.CONTRIBUINTE_ISENTO.getCodigo().toString());
                } else {
                    dest.setIndIEDest(IndicadorIe.CONTRIBUINTE_ICMS.getCodigo().toString());
                    dest.setIE(destinatario.getInscricaoEstadual());
                }

            } else if (nfeCabecalho.getValorIcmsSt().compareTo(BigDecimal.ZERO) > 0) {
                dest.setIndIEDest("2");
            } else if (destinatario.getUf().equals("AM") || destinatario.getUf().equals("BA")
                    || destinatario.getUf().equals("CE") || destinatario.getUf().equals("GO")
                    || destinatario.getUf().equals("MG") || destinatario.getUf().equals("MS")
                    || destinatario.getUf().equals("MT") || destinatario.getUf().equals("PE")
                    || destinatario.getUf().equals("RN") || destinatario.getUf().equals("SE")
                    || destinatario.getUf().equals("SP")) {
                dest.setIndIEDest(IndicadorIe.NAO_CONTRIBUINTE.getCodigo().toString());
            } else {
                dest.setIndIEDest(IndicadorIe.CONTRIBUINTE_ISENTO.getCodigo().toString());
            }
            dest.setISUF(destinatario.getSuframa() == null ? null : String.valueOf(destinatario.getSuframa()));
            dest.setEmail(destinatario.getEmail() == null ? null
                    : destinatario.getEmail().equals("") ? null : destinatario.getEmail());
            tipoContribuinte = IndicadorIe.getByCodigo(Integer.valueOf(dest.getIndIEDest()));
        } else if (destinatario == null && modelo == ModeloDocumento.NFCE) {
            tipoContribuinte = IndicadorIe.NAO_CONTRIBUINTE;
            return null;

        }

        return dest;
    }

    private TEndereco getTEndereco(NfeDestinatario destinatario) {
        TEndereco enderecoDest = new TEndereco();
        enderecoDest.setXLgr(destinatario.getLogradouro());
        enderecoDest.setNro(destinatario.getNumero());
        if (destinatario.getComplemento() != null) {
            enderecoDest.setXCpl(destinatario.getComplemento().equals("") ? null : destinatario.getComplemento());
        }
        enderecoDest.setXBairro(destinatario.getBairro());
        enderecoDest.setCMun(
                destinatario.getCodigoMunicipio() == null ? null : destinatario.getCodigoMunicipio().toString());
        enderecoDest.setXMun(destinatario.getNomeMunicipio());
        enderecoDest.setUF(destinatario.getUf() == null ? null : TUf.fromValue(destinatario.getUf()));
        enderecoDest.setCEP(destinatario.getCep());
        enderecoDest.setCPais(destinatario.getCodigoPais() == null ? null : destinatario.getCodigoPais().toString());
        enderecoDest.setXPais(destinatario.getNomePais());
        if (destinatario.getTelefone() != null) {
            enderecoDest.setFone(destinatario.getTelefone().equals("") ? null : destinatario.getTelefone());
        }

        return enderecoDest;
    }


    private Transp getTransp(NfeTransporte transporte) {
        Transp transp = new Transp();


        if (modelo == ModeloDocumento.NFCE) {
            transp.setModFrete(ModalidadeFrete.SEM_FRETE.getCodigo().toString());
        } else {
            if (transporte == null) {
                transp.setModFrete(ModalidadeFrete.POR_CONTA_EMITENTE.getCodigo().toString());
            } else {
                transp.setVagao(StringUtils.isEmpty(transporte.getVagao()) ? null : transporte.getVagao());
                transp.setBalsa(StringUtils.isEmpty(transporte.getBalsa()) ? null : transporte.getBalsa());

                if (transporte.getTransportadora() != null && transporte.getTransportadora().getId() != null) {
                    TNFe.InfNFe.Transp.Transporta transporta = new TNFe.InfNFe.Transp.Transporta();
                    transp.setTransporta(transporta);
                    if (transporte.getCpfCnpj().length() == 11) {
                        transp.getTransporta().setCPF(transporte.getCpfCnpj());
                    } else {
                        transp.getTransporta().setCNPJ(transporte.getCpfCnpj());
                    }
                    transp.getTransporta().setXNome(transporte.getNome());
                    transp.getTransporta().setIE(transporte.getInscricaoEstadual());
                    transp.getTransporta().setXEnder(transporte.getEmpresaEndereco());
                    transp.getTransporta().setXMun(transporte.getNomeMunicipio());
                    transp.getTransporta().setUF(TUf.valueOf(transporte.getUf()));
                }

                if (transporte.getValorServico() != null) {
                    TNFe.InfNFe.Transp.RetTransp retTransp = new TNFe.InfNFe.Transp.RetTransp();
                    transp.setRetTransp(retTransp);

                    transp.getRetTransp().setVServ(FormatValor.getInstance().formatarValor(transporte.getValorServico()));
                    transp.getRetTransp().setVBCRet(FormatValor.getInstance().formatarValor(transporte.getValorBcRetencaoIcms()));
                    transp.getRetTransp().setPICMSRet(FormatValor.getInstance().formatarValor(transporte.getAliquotaRetencaoIcms()));
                    transp.getRetTransp().setVICMSRet(FormatValor.getInstance().formatarValor(transporte.getValorIcmsRetido()));
                    transp.getRetTransp().setCFOP(transporte.getCfop().toString());
                    transp.getRetTransp().setCMunFG(transporte.getMunicipio().toString());
                }

                if (!StringUtils.isEmpty(transporte.getPlacaVeiculo())) {
                    TVeiculo veiculo = new TVeiculo();
                    transp.setVeicTransp(veiculo);

                    transp.getVeicTransp().setPlaca(transporte.getPlacaVeiculo());
                    transp.getVeicTransp().setUF(TUf.valueOf(transporte.getUf()));
                    transp.getVeicTransp().setRNTC(transporte.getRntcVeiculo());
                }

                transporte.getListaTransporteReboque().stream()
                        .forEach(r -> {
                            TVeiculo reboque = new TVeiculo();
                            reboque.setPlaca(r.getPlaca());
                            reboque.setUF(TUf.valueOf(r.getUf()));
                            reboque.setRNTC(r.getRntc());
                            transp.getReboque().add(reboque);
                        });

                transporte.getListaTransporteVolume().stream()
                        .forEach(v -> {
                            TNFe.InfNFe.Transp.Vol volume = new TNFe.InfNFe.Transp.Vol();
                            volume.setQVol(v.getQuantidade().toString());
                            volume.setEsp(v.getEspecie());
                            volume.setMarca(v.getMarca());
                            volume.setPesoL(FormatValor.getInstance().formatarQuantidade(v.getPesoLiquido()));
                            volume.setPesoB(FormatValor.getInstance().formatarQuantidade(v.getPesoBruto()));
                            v.getListaTransporteVolumeLacre().stream()
                                    .forEach(l -> {
                                        TNFe.InfNFe.Transp.Vol.Lacres lacre = new TNFe.InfNFe.Transp.Vol.Lacres();
                                        volume.getLacres().add(lacre);

                                        lacre.setNLacre(l.getNumero());
                                    });
                            transp.getVol().add(volume);
                        });


            }
        }
        return transp;
    }


    private void addNFref(Set<NfeReferenciada> listaNfeReferenciada, Ide ide) {

        listaNfeReferenciada.stream().forEach(nfeRef -> {
            TNFe.InfNFe.Ide.NFref nFref = new TNFe.InfNFe.Ide.NFref();
            nFref.setRefNFe(nfeRef.getChaveAcesso());
            ide.getNFref().add(nFref);
        });

    }

    private Det addDetalheNfe(NfeDetalhe nfeDetalhe, boolean servico) throws Exception {
        Det det = new Det();
        det.setNItem(nfeDetalhe.getNumeroItem().toString());
        det.setProd(getProd(nfeDetalhe));
        if (nfeDetalhe.getInformacoesAdicionais() != null
                && nfeDetalhe.getInformacoesAdicionais().trim().length() > 0) {
            String infoProd = nfeDetalhe.getInformacoesAdicionais();
            infoProd = infoProd.replaceAll("\r", "");
            infoProd = infoProd.replaceAll("\t", "");
            infoProd = infoProd.replaceAll("\n", "");
            infoProd = Normalizer.normalize(infoProd, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
            det.setInfAdProd(infoProd);
        }
        det.setImposto(getImposto(nfeDetalhe, servico));

        return det;
    }


    private Prod getProd(NfeDetalhe nfeDetalhe) {
        Prod prod = new Prod();

        String ean = (nfeDetalhe.getGtin() == null) ? "" : nfeDetalhe.getGtin();
        prod.setCProd(nfeDetalhe.getCodigoProduto());
        prod.setCEAN(ean);
        prod.setXProd(nfeDetalhe.getNomeProduto());
        prod.setNCM(nfeDetalhe.getNcm());
        if (!StringUtils.isEmpty(nfeDetalhe.getCest())) {
            prod.setCEST(nfeDetalhe.getCest());
        }
        prod.setCFOP(nfeDetalhe.getCfop().toString());
        prod.setUCom(nfeDetalhe.getUnidadeComercial());
        prod.setQCom(FormatValor.getInstance().formatarQuantidade(nfeDetalhe.getQuantidadeComercial()));
        prod.setVUnCom(nfeDetalhe.getValorUnitarioComercial().toPlainString());
        prod.setVProd(FormatValor.getInstance().formatarValor(nfeDetalhe.getValorSubtotal()));

        prod.setCEANTrib(ean);
        prod.setUTrib(nfeDetalhe.getUnidadeTributavel());
        prod.setQTrib(FormatValor.getInstance().formatarQuantidade(nfeDetalhe.getQuantidadeTributavel()));
        prod.setVUnTrib(nfeDetalhe.getValorUnitarioTributavel().toPlainString());

        prod.setVDesc(FormatValor.getInstance().formatarValorToNull(nfeDetalhe.getValorDesconto()));
        prod.setVFrete(FormatValor.getInstance().formatarValorToNull(nfeDetalhe.getValorFrete()));
        prod.setVSeg(FormatValor.getInstance().formatarValorToNull(nfeDetalhe.getValorSeguro()));
        prod.setVOutro(FormatValor.getInstance().formatarValorToNull(nfeDetalhe.getValorOutrasDespesas()));

        prod.setIndTot(String.valueOf(nfeDetalhe.getEntraTotal()));
        return prod;
    }

    private Det.Imposto getImposto(NfeDetalhe nfeDetalhe, boolean servico) throws Exception {
        Imposto imposto = new Imposto();

        if (nfeDetalhe.getValorTotalTributos() != null && nfeDetalhe.getValorTotalTributos().signum() >= 0) {
            imposto.getContent().add(new ObjectFactory()
                    .createTNFeInfNFeDetImpostoVTotTrib(FormatValor.getInstance().formatarValor(nfeDetalhe.getValorTotalTributos())));
        }
        if (servico) {
            TNFe.InfNFe.Det.Imposto.ISSQN issqn = getISSQN(nfeDetalhe.getNfeDetalheImpostoIssqn());
            JAXBElement<ISSQN> issqnElement = new JAXBElement<>(new QName("ISSQN"), ISSQN.class, issqn);
            imposto.getContent().add(issqnElement);

        } else {
            ICMS icms = getICMS(nfeDetalhe.getNfeDetalheImpostoIcms());
            JAXBElement<ICMS> icmsElement = new JAXBElement<>(new QName("ICMS"), ICMS.class, icms);
            imposto.getContent().add(icmsElement);

            if (nfeDetalhe.getNfeDetalheImpostoIpi() != null) {
                TIpi ipi = getTIpi(nfeDetalhe.getNfeDetalheImpostoIpi());
                JAXBElement<TIpi> ipiElement = new JAXBElement<>(new QName("IPI"), TIpi.class, ipi);
                imposto.getContent().add(ipiElement);

            } else if (nfeDetalhe.getNfeDetalheImpostoIpi() != null) {
                TIpi ipi = getTIpi(nfeDetalhe.getNfeDetalheImpostoIpi());
                if (ipi != null) {
                    JAXBElement<TIpi> ipiElement = new JAXBElement<>(new QName("IPI"), TIpi.class, ipi);
                    imposto.getContent().add(ipiElement);
                }

            }
        }

        PIS pis = getPis(nfeDetalhe.getNfeDetalheImpostoPis());
        if (pis != null) {
            JAXBElement<PIS> pisElement = new JAXBElement<>(new QName("PIS"), PIS.class, pis);
            imposto.getContent().add(pisElement);
        }

        COFINS cofins = getCofins(nfeDetalhe.getNfeDetalheImpostoCofins());
        if (cofins != null) {
            JAXBElement<COFINS> cofinsElement = new JAXBElement<>(new QName("COFINS"), COFINS.class, cofins);
            imposto.getContent().add(cofinsElement);
        }

        if (!servico) {
            String cst = empresa.getCrt().equals("1") ? nfeDetalhe.getNfeDetalheImpostoIcms().getCsosn() : nfeDetalhe.getNfeDetalheImpostoIcms().getCstIcms();
            ICMSUFDest icmsDest = getICMSDest(nfeDetalhe.getNfeDetalheImpostoIcms(), cst);
            if (icmsDest != null) {
                JAXBElement<ICMSUFDest> icmsDestElement = new JAXBElement<>(new QName("ICMSUFDest"), ICMSUFDest.class,
                        icmsDest);
                imposto.getContent().add(icmsDestElement);
            }
        }
        return imposto;
    }

    private ICMSUFDest getICMSDest(NfeDetalheImpostoIcms difal, String cst) {
        if (tipoContribuinte != null && tipoContribuinte == IndicadorIe.NAO_CONTRIBUINTE && nfeCabecalho.getLocalDestino() == 2 && modelo == ModeloDocumento.NFE) {
            if (cst.equals("00") || cst.equals("20") || cst.equals("40") || cst.equals("41") || cst.equals("60")) {

                Imposto.ICMSUFDest icmsDest = new Imposto.ICMSUFDest();

                if (difal.getValorIcmsInterUfDestino() == null || difal.getValorIcmsInterUfRemetente() == null) {
                    return null;
                }
                icmsDest.setPFCPUFDest(FormatValor.getInstance().formatarValor(difal.getPercentualFcpUfDestino()));
                icmsDest.setPICMSInter(FormatValor.getInstance().formatarValor(difal.getAliquotaInterestadualUfs()));
                icmsDest.setPICMSInterPart(FormatValor.getInstance().formatarValor(difal.getPercentualProvisorioPartilha()));
                icmsDest.setPICMSUFDest(FormatValor.getInstance().formatarValor(difal.getAliquotaInternaUfDestino()));
                icmsDest.setVFCPUFDest(FormatValor.getInstance().formatarValor(difal.getValorIcmsFcpUfDestino()));
                icmsDest.setVBCUFDest(FormatValor.getInstance().formatarValor(difal.getValorBsIcmsUfDestino()));
                icmsDest.setVICMSUFDest(FormatValor.getInstance().formatarValor(difal.getValorIcmsInterUfDestino()));
                icmsDest.setVICMSUFRemet(FormatValor.getInstance().formatarValor(difal.getValorIcmsInterUfRemetente()));

                return icmsDest;
            }

        }

        return null;
    }

    private COFINS getCofins(NfeDetalheImpostoCofins impCofins) {
        COFINS cofins = null;
        if (impCofins != null) {
            cofins = new COFINS();
            if (impCofins.getCstCofins().equals("01")
                    || impCofins.getCstCofins().equals("02")) {
                TNFe.InfNFe.Det.Imposto.COFINS.COFINSAliq cofinsAliq = new TNFe.InfNFe.Det.Imposto.COFINS.COFINSAliq();
                cofins.setCOFINSAliq(cofinsAliq);

                cofinsAliq.setCST(impCofins.getCstCofins());
                cofinsAliq.setVBC(FormatValor.getInstance().formatarValor(impCofins.getBaseCalculoCofins()));
                if (impCofins.getCstCofins().equals("01")) {
                    cofinsAliq.setPCOFINS(FormatValor.getInstance().formatarValor(impCofins.getAliquotaCofinsPercentual()));
                } else {
                    cofinsAliq.setPCOFINS(FormatValor.getInstance().formatarValor(impCofins.getAliquotaCofinsReais()));
                }
                cofinsAliq.setVCOFINS(FormatValor.getInstance().formatarValor(impCofins.getValorCofins()));
            } else if (impCofins.getCstCofins().equals("03")) {
                TNFe.InfNFe.Det.Imposto.COFINS.COFINSQtde cofinsQtd = new COFINS.COFINSQtde();
                cofinsQtd.setCST("03");
                cofinsQtd.setQBCProd(FormatValor.getInstance().formatarValor(impCofins.getQuantidadeVendida()));
                cofinsQtd.setVAliqProd(FormatValor.getInstance().formatarValor(impCofins.getAliquotaCofinsReais()));
                cofinsQtd.setVCOFINS(FormatValor.getInstance().formatarValor(impCofins.getValorCofins()));
            } else if (impCofins.getCstCofins().equals("04")
                    || impCofins.getCstCofins().equals("05")
                    || impCofins.getCstCofins().equals("06")
                    || impCofins.getCstCofins().equals("07")
                    || impCofins.getCstCofins().equals("08")
                    || impCofins.getCstCofins().equals("09")) {
                TNFe.InfNFe.Det.Imposto.COFINS.COFINSNT cofinsNT = new COFINS.COFINSNT();
                cofinsNT.setCST(impCofins.getCstCofins());
                cofins.setCOFINSNT(cofinsNT);

            } else {
                TNFe.InfNFe.Det.Imposto.COFINS.COFINSOutr cofinsOutr = new TNFe.InfNFe.Det.Imposto.COFINS.COFINSOutr();
                cofins.setCOFINSOutr(cofinsOutr);
                cofinsOutr.setCST(impCofins.getCstCofins());
                cofinsOutr.setQBCProd(FormatValor.getInstance().formatarValor(impCofins.getQuantidadeVendida()));
                cofinsOutr.setVAliqProd(FormatValor.getInstance().formatarValor(impCofins.getAliquotaCofinsReais()));
                cofinsOutr.setVCOFINS(FormatValor.getInstance().formatarValor(impCofins.getValorCofins()));
            }
        }
        return cofins;
    }

    private TIpi getTIpi(NfeDetalheImpostoIpi impIpi) {
        TIpi ipi = null;
        // IPI
        if (impIpi != null && impIpi.getValorIpi() != null) {

            ipi = new TIpi();
            TIpi.IPITrib ipiTrib = new TIpi.IPITrib();
            ipi.setIPITrib(ipiTrib);
            ipi.setCEnq(impIpi.getEnquadramentoIpi() == null ? "999"
                    : impIpi.getEnquadramentoIpi());
            ipiTrib.setCST(impIpi.getCstIpi());
            ipiTrib.setVBC(FormatValor.getInstance().formatarValor(impIpi.getValorBaseCalculoIpi()));
            ipiTrib.setPIPI(FormatValor.getInstance().formatarValor(impIpi.getAliquotaIpi()));
            ipiTrib.setVIPI(FormatValor.getInstance().formatarValor(impIpi.getValorIpi()));

        }
        return ipi;
    }

    private PIS getPis(NfeDetalheImpostoPis impPis) {
        PIS pis = null;

        if (impPis != null) {
            pis = new PIS();
            if (impPis.getCstPis().equals("01")
                    || impPis.getCstPis().equals("02")) {
                TNFe.InfNFe.Det.Imposto.PIS.PISAliq pisAliq = new TNFe.InfNFe.Det.Imposto.PIS.PISAliq();
                pis.setPISAliq(pisAliq);

                pisAliq.setCST(impPis.getCstPis());
                pisAliq.setVBC(FormatValor.getInstance().formatarValor(impPis.getValorBaseCalculoPis()));
                if (impPis.getCstPis().equals("01")) {
                    pisAliq.setPPIS(
                            FormatValor.getInstance().formatarValor(impPis.getAliquotaPisPercentual()));
                } else {
                    pisAliq.setPPIS(FormatValor.getInstance().formatarValor(impPis.getAliquotaPisReais()));
                }
                pisAliq.setVPIS(FormatValor.getInstance().formatarValor(impPis.getValorPis()));
            } else if (impPis.getCstPis().equals("03")) {
                TNFe.InfNFe.Det.Imposto.COFINS.COFINSQtde cofinsQtd = new COFINS.COFINSQtde();
                cofinsQtd.setCST("03");
                cofinsQtd.setQBCProd(FormatValor.getInstance().formatarValor(impPis.getQuantidadeVendida()));
                cofinsQtd.setVAliqProd(FormatValor.getInstance().formatarValor(impPis.getAliquotaPisReais()));
                cofinsQtd.setVCOFINS(FormatValor.getInstance().formatarValor(impPis.getValorPis()));
            } else if (impPis.getCstPis().equals("04")
                    || impPis.getCstPis().equals("05")
                    || impPis.getCstPis().equals("06")
                    || impPis.getCstPis().equals("07")
                    || impPis.getCstPis().equals("08")
                    || impPis.getCstPis().equals("09")) {
                TNFe.InfNFe.Det.Imposto.PIS.PISNT pisNT = new PIS.PISNT();
                pisNT.setCST(impPis.getCstPis());
                pis.setPISNT(pisNT);

            } else {
                TNFe.InfNFe.Det.Imposto.PIS.PISOutr pisOutr = new PIS.PISOutr();
                pis.setPISOutr(pisOutr);
                pisOutr.setCST(impPis.getCstPis());
                pisOutr.setQBCProd(FormatValor.getInstance().formatarValor(impPis.getQuantidadeVendida()));
                pisOutr.setVAliqProd(FormatValor.getInstance().formatarValor(impPis.getAliquotaPisReais()));
                pisOutr.setVPIS(FormatValor.getInstance().formatarValor(impPis.getValorPis()));
            }
        }

        return pis;
    }

    private ICMS getICMS(NfeDetalheImpostoIcms impIcms) throws Exception {

        TNFe.InfNFe.Det.Imposto.ICMS icms = new TNFe.InfNFe.Det.Imposto.ICMS();

        String origemMercadoria = impIcms.getOrigemMercadoria() == null ? String.valueOf(OrigemMercadoria.Nacional.getCodigo()) : String.valueOf(impIcms.getOrigemMercadoria());
        String nomeProduto = impIcms.getNfeDetalhe().getNomeProduto();
        if (empresa.getCrt().equals("1")) {
            String csosn = impIcms.getCsosn();
            if (csosn == null) {
                throw new Exception("csosn para o produto \n" + nomeProduto + "\n não informado !!!");
            }
            if (csosn.equals("101")) {
                TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN101 icms101 = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN101();
                icms.setICMSSN101(icms101);
                icms101.setCSOSN(csosn);
                icms101.setOrig(origemMercadoria);
                icms101.setPCredSN(FormatValor.getInstance().formatarValor(impIcms.getAliquotaCreditoIcmsSn()));
                icms101.setVCredICMSSN(FormatValor.getInstance().formatarValor(impIcms.getValorCreditoIcmsSn()));
            }
            if (csosn.equals("102") || csosn.equals("103") || csosn.equals("300") || csosn.equals("400")) {
                TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102 icms102 = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102();
                icms.setICMSSN102(icms102);

                icms102.setCSOSN(csosn);
                icms102.setOrig(origemMercadoria);
            }
            if (csosn.equals("201")) {
                TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN201 icms201 = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN201();
                icms.setICMSSN201(icms201);

                icms201.setCSOSN(csosn);
                icms201.setOrig(origemMercadoria);
                icms201.setModBCST(String.valueOf(impIcms.getModalidadeBcIcmsSt()));
                icms201.setPMVAST(FormatValor.getInstance().formatarValor(impIcms.getPercentualMvaIcmsSt()));
                icms201.setPRedBCST(FormatValor.getInstance().formatarValor(impIcms.getPercentualReducaoBcIcmsSt()));
                icms201.setVBCST(FormatValor.getInstance().formatarValor(impIcms.getValorBaseCalculoIcmsSt()));
                icms201.setPICMSST(FormatValor.getInstance().formatarValor(impIcms.getAliquotaIcmsSt()));
                icms201.setVICMSST(FormatValor.getInstance().formatarValor(impIcms.getValorIcmsSt()));
                icms201.setPCredSN(FormatValor.getInstance().formatarValor(impIcms.getAliquotaCreditoIcmsSn()));
                icms201.setVCredICMSSN(FormatValor.getInstance().formatarValor(impIcms.getValorCreditoIcmsSn()));
            }
            if (csosn.equals("202") || csosn.equals("203")) {
                TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN202 icms202 = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN202();
                icms.setICMSSN202(icms202);

                icms202.setCSOSN(csosn);
                icms202.setOrig(origemMercadoria);
                icms202.setModBCST(String.valueOf(impIcms.getModalidadeBcIcmsSt()));
                icms202.setPMVAST(FormatValor.getInstance().formatarValor(impIcms.getPercentualMvaIcmsSt()));
                icms202.setPRedBCST(FormatValor.getInstance().formatarValor(impIcms.getPercentualReducaoBcIcmsSt()));
                icms202.setVBCST(FormatValor.getInstance().formatarValor(impIcms.getValorBaseCalculoIcmsSt()));
                icms202.setPICMSST(FormatValor.getInstance().formatarValor(impIcms.getAliquotaIcmsSt()));
                icms202.setVICMSST(FormatValor.getInstance().formatarValor(impIcms.getValorIcmsSt()));
            }
            if (csosn.equals("500")) {


                TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN500 icms500 = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN500();
                icms.setICMSSN500(icms500);

                icms500.setCSOSN(csosn);
                icms500.setOrig(origemMercadoria);


                icms500.setVBCSTRet(FormatValor.getInstance().formatarValor(BigDecimal.ZERO));
                icms500.setVICMSSTRet(FormatValor.getInstance().formatarValor(BigDecimal.ZERO));
            }
            if (csosn.equals("900")) {
                TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN900 icms900 = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN900();
                icms.setICMSSN900(icms900);

                icms900.setCSOSN(csosn);
                icms900.setOrig(origemMercadoria);
                if (impIcms.getModalidadeBcIcms() != null) {
                    icms900.setModBC(String.valueOf(impIcms.getModalidadeBcIcms()));
                }
                icms900.setVBC(impIcms.getBaseCalculoIcms() == null ? null : FormatValor.getInstance().formatarValor(impIcms.getBaseCalculoIcms()));

                icms900.setPICMS(impIcms.getAliquotaIcms() == null ? null : FormatValor.getInstance().formatarValor(impIcms.getAliquotaIcms()));
                icms900.setVICMS(impIcms.getValorIcms() == null ? null : FormatValor.getInstance().formatarValor(impIcms.getValorIcms()));
                if (impIcms.getValorIcmsSt() != null) {
                    icms900.setModBCST(String.valueOf(impIcms.getModalidadeBcIcmsSt()));
                    icms900.setPMVAST(FormatValor.getInstance().formatarValor(
                            impIcms.getPercentualMvaIcmsSt() == null ? BigDecimal.ZERO
                                    : impIcms.getPercentualMvaIcmsSt()));
                    icms900.setPRedBCST(FormatValor.getInstance().formatarValor(impIcms.getPercentualReducaoBcIcmsSt()));
                    icms900.setVBCST(FormatValor.getInstance().formatarValor(impIcms.getValorBaseCalculoIcmsSt()));
                    icms900.setPICMSST(FormatValor.getInstance().formatarValor(impIcms.getAliquotaIcmsSt()));
                    icms900.setVICMSST(FormatValor.getInstance().formatarValor(impIcms.getValorIcmsSt()));
                }

            }
        } else {
            String cst = impIcms.getCstIcms();
            if (cst == null) {
                throw new Exception("CST para o produto \n" + nomeProduto + "\n não informado !!!");
            }
            if (cst.equals("00")) {
                TNFe.InfNFe.Det.Imposto.ICMS.ICMS00 icms00 = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS00();
                icms.setICMS00(icms00);

                icms00.setCST(impIcms.getCstIcms());
                icms00.setOrig(origemMercadoria);
                icms00.setModBC(String.valueOf(impIcms.getModalidadeBcIcms()));
                icms00.setVBC(FormatValor.getInstance().formatarValor(impIcms.getBaseCalculoIcms() == null
                        ? BigDecimal.ZERO : impIcms.getBaseCalculoIcms()));
                icms00.setPICMS(FormatValor.getInstance().formatarValor(impIcms.getAliquotaIcms()));
                icms00.setVICMS(FormatValor.getInstance().formatarValor(impIcms.getValorIcms()));
            }
            if (cst.equals("10")) {
                TNFe.InfNFe.Det.Imposto.ICMS.ICMS10 icms10 = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS10();
                icms.setICMS10(icms10);

                icms10.setCST(impIcms.getCstIcms());
                icms10.setOrig(origemMercadoria);
                icms10.setModBC(String.valueOf(impIcms.getModalidadeBcIcms()));
                icms10.setVBC(FormatValor.getInstance().formatarValor(impIcms.getBaseCalculoIcms()));
                icms10.setPICMS(FormatValor.getInstance().formatarValor(impIcms.getAliquotaIcms()));
                icms10.setVICMS(FormatValor.getInstance().formatarValor(impIcms.getValorIcms()));
                icms10.setModBCST(String.valueOf(impIcms.getModalidadeBcIcmsSt()));
                icms10.setPMVAST(FormatValor.getInstance().formatarValor(impIcms.getPercentualMvaIcmsSt()));
                icms10.setPRedBCST(FormatValor.getInstance().formatarValor(impIcms.getPercentualReducaoBcIcmsSt()));
                icms10.setVBCST(FormatValor.getInstance().formatarValor(impIcms.getValorBaseCalculoIcmsSt()));
                icms10.setPICMSST(FormatValor.getInstance().formatarValor(impIcms.getAliquotaIcmsSt()));
                icms10.setVICMSST(FormatValor.getInstance().formatarValor(impIcms.getValorIcmsSt()));
            }
            if (cst.equals("20")) {
                TNFe.InfNFe.Det.Imposto.ICMS.ICMS20 icms20 = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS20();
                icms.setICMS20(icms20);

                icms20.setCST(impIcms.getCstIcms());
                icms20.setOrig(origemMercadoria);
                icms20.setPRedBC(FormatValor.getInstance().formatarValor(impIcms.getTaxaReducaoBcIcms()));
                icms20.setModBC(String.valueOf(impIcms.getModalidadeBcIcms()));
                icms20.setVBC(FormatValor.getInstance().formatarValor(impIcms.getBaseCalculoIcms()));
                icms20.setPICMS(FormatValor.getInstance().formatarValor(impIcms.getAliquotaIcms()));
                icms20.setVICMS(FormatValor.getInstance().formatarValor(impIcms.getValorIcms()));
            }
            if (cst.equals("30")) {
                TNFe.InfNFe.Det.Imposto.ICMS.ICMS30 icms30 = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS30();
                icms.setICMS30(icms30);

                icms30.setCST(impIcms.getCstIcms());
                icms30.setOrig(origemMercadoria);
                icms30.setModBCST(String.valueOf(impIcms.getModalidadeBcIcmsSt()));
                icms30.setPMVAST(FormatValor.getInstance().formatarValor(impIcms.getPercentualMvaIcmsSt()));
                icms30.setPRedBCST(FormatValor.getInstance().formatarValor(impIcms.getPercentualReducaoBcIcmsSt()));
                icms30.setVBCST(FormatValor.getInstance().formatarValor(impIcms.getValorBaseCalculoIcmsSt()));
                icms30.setPICMSST(FormatValor.getInstance().formatarValor(impIcms.getAliquotaIcmsSt()));
                icms30.setVICMSST(FormatValor.getInstance().formatarValor(impIcms.getValorIcmsSt()));
            }
            if (cst.equals("40") || cst.equals("41") || cst.equals("50")) {
                TNFe.InfNFe.Det.Imposto.ICMS.ICMS40 icms40 = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS40();
                icms.setICMS40(icms40);

                impIcms.setMotivoDesoneracaoIcms(9);

                icms40.setCST(impIcms.getCstIcms());
                icms40.setOrig(origemMercadoria);
                if (cst.equals("40")) {
                    icms40.setVICMSDeson(
                            FormatValor.getInstance().formatarValor(impIcms.getValorIcmsDesonerado()));
                    icms40.setMotDesICMS(String.valueOf(impIcms.getMotivoDesoneracaoIcms()));
                }
                if (impIcms.getMotivoDesoneracaoIcms() == null) {
                    throw new Exception("Para o CST " + cst + " é preciso informar um motivo para desoneração");
                }

            }
            if (cst.equals("51")) {
                TNFe.InfNFe.Det.Imposto.ICMS.ICMS51 icms51 = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS51();
                icms.setICMS51(icms51);
                icms51.setCST(impIcms.getCstIcms());
                icms51.setOrig(origemMercadoria);

            }
            if (cst.equals("60")) {
                TNFe.InfNFe.Det.Imposto.ICMS.ICMS60 icms60 = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS60();
                icms.setICMS60(icms60);

                icms60.setCST(cst);
                icms60.setOrig(origemMercadoria);
                icms60.setVBCSTRet(FormatValor.getInstance().formatarValor(impIcms.getValorBcIcmsStRetido()));
                icms60.setVICMSSTRet(FormatValor.getInstance().formatarValor(impIcms.getValorIcmsStRetido()));
            }
            if (cst.equals("70")) {
                TNFe.InfNFe.Det.Imposto.ICMS.ICMS70 icms70 = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS70();
                icms.setICMS70(icms70);

                icms70.setCST(impIcms.getCstIcms());
                icms70.setOrig(origemMercadoria);
                icms70.setModBC(String.valueOf(impIcms.getModalidadeBcIcms()));
                icms70.setPRedBC(FormatValor.getInstance().formatarValor(impIcms.getTaxaReducaoBcIcms()));
                icms70.setVBC(FormatValor.getInstance().formatarValor(impIcms.getBaseCalculoIcms()));
                icms70.setPICMS(FormatValor.getInstance().formatarValor(impIcms.getAliquotaIcms()));
                icms70.setVICMS(FormatValor.getInstance().formatarValor(impIcms.getValorIcms()));
                icms70.setModBCST(String.valueOf(impIcms.getModalidadeBcIcmsSt()));
                icms70.setPMVAST(FormatValor.getInstance().formatarValor(impIcms.getPercentualMvaIcmsSt()));
                icms70.setPRedBCST(FormatValor.getInstance().formatarValor(impIcms.getPercentualReducaoBcIcmsSt()));
                icms70.setVBCST(FormatValor.getInstance().formatarValor(impIcms.getValorBaseCalculoIcmsSt()));
                icms70.setPICMSST(FormatValor.getInstance().formatarValor(impIcms.getAliquotaIcmsSt()));
                icms70.setVICMSST(FormatValor.getInstance().formatarValor(impIcms.getValorIcmsSt()));
            }
            if (cst.equals("90")) {
                TNFe.InfNFe.Det.Imposto.ICMS.ICMS90 icms90 = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS90();
                icms.setICMS90(icms90);

                icms90.setCST(impIcms.getCstIcms());
                icms90.setOrig(origemMercadoria);
                icms90.setModBC(String.valueOf(impIcms.getModalidadeBcIcms()));
                icms90.setPRedBC(FormatValor.getInstance().formatarValor(impIcms.getTaxaReducaoBcIcms()));
                icms90.setVBC(FormatValor.getInstance().formatarValor(impIcms.getBaseCalculoIcms()));
                icms90.setPICMS(FormatValor.getInstance().formatarValor(impIcms.getAliquotaIcms()));
                icms90.setVICMS(FormatValor.getInstance().formatarValor(impIcms.getValorIcms()));
                icms90.setModBCST(String.valueOf(impIcms.getModalidadeBcIcmsSt()));
                icms90.setPMVAST(FormatValor.getInstance().formatarValor(impIcms.getPercentualMvaIcmsSt()));
                icms90.setPRedBCST(FormatValor.getInstance().formatarValor(impIcms.getPercentualReducaoBcIcmsSt()));
                icms90.setVBCST(FormatValor.getInstance().formatarValor(impIcms.getValorBaseCalculoIcmsSt()));
                icms90.setPICMSST(FormatValor.getInstance().formatarValor(impIcms.getAliquotaIcmsSt()));
                icms90.setVICMSST(FormatValor.getInstance().formatarValor(impIcms.getValorIcmsSt()));
            }
        }

        return icms;
    }

    private ISSQN getISSQN(NfeDetalheImpostoIssqn iss) throws ParseException {
        TNFe.InfNFe.Det.Imposto.ISSQN issqn = new TNFe.InfNFe.Det.Imposto.ISSQN();
        MaskFormatter formatter = new MaskFormatter("##.##");
        formatter.setValueContainsLiteralCharacters(false);
        String codServico = iss.getItemListaServicos().toString();
        codServico = codServico.length() == 3 ? "0" + codServico : codServico;
        codServico = formatter.valueToString(codServico);
        issqn.setVBC(FormatValor.getInstance().formatarValor(iss.getBaseCalculoIssqn()));
        issqn.setVAliq(FormatValor.getInstance().formatarValor(iss.getAliquotaIssqn()));
        issqn.setVISSQN(FormatValor.getInstance().formatarValor(iss.getValorIssqn()));
        issqn.setCMunFG(iss.getMunicipioIncidencia().toString());
        issqn.setCListServ(codServico);
        issqn.setIndIncentivo(iss.getIndicadorIncentivoFiscal().toString());
        issqn.setIndISS(iss.getIndicadorExigibilidadeIss().toString());

        return issqn;
    }


    private InfNFe.Cobr getCobr(NfeFatura fatura, Set<NfeDuplicata> listaDuplicata) throws ParseException {
        TNFe.InfNFe.Cobr cobr = null;
        if (fatura != null && fatura.getNumero() != null) {
            if (!fatura.getNumero().equals("")) {
                cobr = new TNFe.InfNFe.Cobr();
                TNFe.InfNFe.Cobr.Fat fat = new TNFe.InfNFe.Cobr.Fat();
                cobr.setFat(fat);
                fat.setNFat(fatura.getNumero());
                fat.setVOrig(FormatValor.getInstance().formatarValor(fatura.getValorOriginal()));
                fat.setVDesc(FormatValor.getInstance().formatarValor((fatura.getValorDesconto())));
                fat.setVLiq(FormatValor.getInstance().formatarValor((fatura.getValorLiquido())));
            }
        }

        if (listaDuplicata != null) {
            for (NfeDuplicata n : listaDuplicata) {
                if (cobr == null) {
                    cobr = new TNFe.InfNFe.Cobr();

                }

                TNFe.InfNFe.Cobr.Dup dup = new TNFe.InfNFe.Cobr.Dup();
                dup.setNDup(nfeCabecalho.getNumero() + "/" + n.getNumero().trim());
                dup.setDVenc(FormatValor.getInstance().formatarDataEUA(n.getDataVencimento()));
                dup.setVDup(FormatValor.getInstance().formatarValor(n.getValor()));

                cobr.getDup().add(dup);
            }
        }
        return cobr;
    }


    private InfAdic getInfAdic() {
        InfAdic infAdic = new TNFe.InfNFe.InfAdic();
        infAdic.setInfAdFisco(nfeCabecalho.getInformacoesAddFisco() == null ? null
                : nfeCabecalho.getInformacoesAddFisco().equals("") ? null : nfeCabecalho.getInformacoesAddFisco());
        infAdic.setInfCpl(nfeCabecalho.getInformacoesAddContribuinte() == null ? null
                : nfeCabecalho.getInformacoesAddContribuinte().equals("") ? null
                : nfeCabecalho.getInformacoesAddContribuinte().trim());
        return infAdic;
    }


    private Total getTotais(NfeCabecalho nfeCabecalho) throws ParseException {

        TNFe.InfNFe.Total total = new TNFe.InfNFe.Total();

        TNFe.InfNFe.Total.ICMSTot icmsTot = new TNFe.InfNFe.Total.ICMSTot();
        total.setICMSTot(icmsTot);

        icmsTot.setVBC(FormatValor.getInstance().formatarValor(nfeCabecalho.getBaseCalculoIcms()));
        icmsTot.setVICMS(FormatValor.getInstance().formatarValor(nfeCabecalho.getValorIcms()));
        icmsTot.setVBCST(FormatValor.getInstance().formatarValor(nfeCabecalho.getBaseCalculoIcmsSt()));
        icmsTot.setVST(FormatValor.getInstance().formatarValor(nfeCabecalho.getValorIcmsSt()));
        icmsTot.setVProd(FormatValor.getInstance().formatarValor(nfeCabecalho.getValorTotalProdutos()));
        icmsTot.setVFrete(FormatValor.getInstance().formatarValor(nfeCabecalho.getValorFrete()));
        icmsTot.setVSeg(FormatValor.getInstance().formatarValor(nfeCabecalho.getValorSeguro()));
        icmsTot.setVDesc(FormatValor.getInstance().formatarValor(nfeCabecalho.getValorDesconto()));
        icmsTot.setVII(FormatValor.getInstance().formatarValor(nfeCabecalho.getValorImpostoImportacao()));
        icmsTot.setVIPI(FormatValor.getInstance().formatarValor(nfeCabecalho.getValorIpi()));
        icmsTot.setVPIS(FormatValor.getInstance().formatarValor(nfeCabecalho.getValorPis()));
        icmsTot.setVCOFINS(FormatValor.getInstance().formatarValor(nfeCabecalho.getValorCofins()));
        icmsTot.setVOutro(FormatValor.getInstance().formatarValor(nfeCabecalho.getValorDespesasAcessorias()));
        icmsTot.setVNF(FormatValor.getInstance().formatarValor(nfeCabecalho.getValorTotal()));
        icmsTot.setVICMSDeson(FormatValor.getInstance().formatarValor(nfeCabecalho.getValorIcmsDesonerado()));
        icmsTot.setVTotTrib(FormatValor.getInstance().formatarValor(valorTotalTributos));

        if (nfeCabecalho.getValorServicos().compareTo(BigDecimal.ZERO) > 0) {
            TNFe.InfNFe.Total.ISSQNtot issqnTot = new TNFe.InfNFe.Total.ISSQNtot();
            total.setISSQNtot(issqnTot);

            issqnTot.setVServ(FormatValor.getInstance().formatarValor(nfeCabecalho.getValorServicos()));
            if (nfeCabecalho.getBaseCalculoIssqn().compareTo(BigDecimal.ZERO) > 0) {
                total.getISSQNtot().setVBC(FormatValor.getInstance().formatarValor(nfeCabecalho.getBaseCalculoIssqn()));
                issqnTot.setVISS(FormatValor.getInstance().formatarValor(nfeCabecalho.getValorIssqn()));
            }

            if (nfeCabecalho.getValorPisIssqn().compareTo(BigDecimal.ZERO) > 0) {
                issqnTot.setVPIS(FormatValor.getInstance().formatarValor(nfeCabecalho.getValorPisIssqn()));
            }
            if (nfeCabecalho.getValorCofinsIssqn().compareTo(BigDecimal.ZERO) > 0) {
                issqnTot.setVCOFINS(FormatValor.getInstance().formatarValor(nfeCabecalho.getValorCofinsIssqn()));
            }
            issqnTot.setDCompet(FormatValor.getInstance().formatarData(new Date()));
        }

        if (nfeCabecalho.getBaseCalculoPrevidencia().compareTo(BigDecimal.ZERO) > 0) {
            TNFe.InfNFe.Total.RetTrib issRet = new TNFe.InfNFe.Total.RetTrib();
            total.setRetTrib(issRet);
            issRet.setVBCRetPrev(FormatValor.getInstance().formatarValor(nfeCabecalho.getBaseCalculoPrevidencia()));
            issRet.setVRetPrev(FormatValor.getInstance().formatarValor(nfeCabecalho.getValorRetidoPrevidencia()));
        }

        boolean valorRetido = false;

        if (nfeCabecalho.getValorRetidoPis().compareTo(BigDecimal.ZERO) > 0) {
            valorRetido = true;
        } else if (nfeCabecalho.getValorRetidoCofins().compareTo(BigDecimal.ZERO) > 0) {
            valorRetido = true;
        } else if (nfeCabecalho.getValorRetidoCofins().compareTo(BigDecimal.ZERO) > 0) {
            valorRetido = true;
        } else if (nfeCabecalho.getValorRetidoCsll().compareTo(BigDecimal.ZERO) > 0) {
            valorRetido = true;
        } else if (nfeCabecalho.getBaseCalculoIrrf().compareTo(BigDecimal.ZERO) > 0) {
            valorRetido = true;
        } else if (nfeCabecalho.getBaseCalculoPrevidencia().compareTo(BigDecimal.ZERO) > 0) {
            valorRetido = true;
        } else if (nfeCabecalho.getValorRetidoPrevidencia().compareTo(BigDecimal.ZERO) > 0) {
            valorRetido = true;
        }

        if (valorRetido) {
            TNFe.InfNFe.Total.RetTrib retTrib = new TNFe.InfNFe.Total.RetTrib();
            total.setRetTrib(retTrib);
            retTrib.setVRetPIS(FormatValor.getInstance().formatarValor(nfeCabecalho.getValorRetidoPis()));
            retTrib.setVRetCOFINS(FormatValor.getInstance().formatarValor(nfeCabecalho.getValorRetidoCofins()));
            retTrib.setVRetCSLL(FormatValor.getInstance().formatarValor(nfeCabecalho.getValorRetidoCsll()));
            retTrib.setVBCIRRF(FormatValor.getInstance().formatarValor(nfeCabecalho.getBaseCalculoIrrf()));
            retTrib.setVIRRF(FormatValor.getInstance().formatarValor(nfeCabecalho.getValorRetidoIrrf()));
            retTrib.setVBCRetPrev(FormatValor.getInstance().formatarValor(nfeCabecalho.getBaseCalculoPrevidencia()));
            retTrib.setVRetPrev(FormatValor.getInstance().formatarValor(nfeCabecalho.getValorRetidoPrevidencia()));
        }

        return total;
    }


}