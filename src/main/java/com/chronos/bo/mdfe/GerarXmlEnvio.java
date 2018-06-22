package com.chronos.bo.mdfe;

import br.inf.portalfiscal.mdfe.schema_300.consMDFeNaoEnc.TConsMDFeNaoEnc;
import br.inf.portalfiscal.mdfe.schema_300.consSitMDFe.TConsSitMDFe;
import br.inf.portalfiscal.mdfe.schema_300.enviMDFe.TEndeEmi;
import br.inf.portalfiscal.mdfe.schema_300.enviMDFe.TEnviMDFe;
import br.inf.portalfiscal.mdfe.schema_300.enviMDFe.TMDFe;
import br.inf.portalfiscal.mdfe.schema_300.enviMDFe.TUf;
import br.inf.portalfiscal.mdfe.schema_300.evCancMDFe.EvCancMDFe;
import br.inf.portalfiscal.mdfe.schema_300.evEncMDFe.EvEncMDFe;
import br.inf.portalfiscal.mdfe.schema_300.evIncCondutorMDF.EvIncCondutorMDFe;
import br.inf.portalfiscal.mdfe.schema_300.mdfeModalRodoviario.Rodo;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.TipoEmitente;
import com.chronos.transmissor.exception.EmissorException;
import com.chronos.transmissor.mdfe.Mdfe;
import com.chronos.transmissor.util.ObjetoUtil;
import com.chronos.util.FormatValor;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.*;

/**
 * Created by john on 19/06/18.
 */
public class GerarXmlEnvio {

    public TEnviMDFe gerarMdfe(MdfeCabecalho mdfeCab) throws Exception {

        TMDFe mdfe = new TMDFe();
        TMDFe.InfMDFe infMdfe = new TMDFe.InfMDFe();
        infMdfe.setId("MDFe" + mdfeCab.getChaveAcessoCompleta());
        infMdfe.setVersao("3.00");

        infMdfe.setEmit(getEmit(mdfeCab.getMdfeEmitente()));
        infMdfe.setIde(getIde(mdfeCab));
        infMdfe.setInfAdic(getInfAdic(mdfeCab));
        infMdfe.setInfDoc(getDoc(mdfeCab.getListaMdfeMunicipioDescarregamento(),
                TipoEmitente.valueOfCodigo(mdfeCab.getTipoEmitente())));
        infMdfe.setInfModal(getModal(mdfeCab));
        infMdfe.getSeg().addAll(getSeguro(mdfeCab.getListaMdfeInformacaoSeguro()));
        infMdfe.setTot(getTotal(mdfeCab));

        TEnviMDFe enviMdfe = new TEnviMDFe();

        mdfe.setInfMDFe(infMdfe);

        enviMdfe.setIdLote("1");
        enviMdfe.setMDFe(mdfe);
        enviMdfe.setVersao("3.00");

        enviMdfe = Mdfe.montaMDFe(enviMdfe, false);

        return enviMdfe;
    }

    public br.inf.portalfiscal.mdfe.schema_300.evCancMDFe.TEvento cancelamento(String chave, String cnpj,
                                                                               String ambiente, String uf, String protocolo, String justificativa) throws EmissorException, Exception {

        String id = "ID110111" + chave + "01";

        br.inf.portalfiscal.mdfe.schema_300.evCancMDFe.TEvento enviEvento = new br.inf.portalfiscal.mdfe.schema_300.evCancMDFe.TEvento();
        enviEvento.setVersao("3.00");

        br.inf.portalfiscal.mdfe.schema_300.evCancMDFe.TEvento.InfEvento infoEvento = new br.inf.portalfiscal.mdfe.schema_300.evCancMDFe.TEvento.InfEvento();
        infoEvento.setId(id);
        infoEvento.setCOrgao(uf);
        infoEvento.setTpAmb(ambiente);
        infoEvento.setCNPJ(cnpj);
        infoEvento.setChMDFe(chave);
        infoEvento.setDhEvento(FormatValor.getInstance().formatarDataNota((new Date())));
        infoEvento.setTpEvento("110111");
        infoEvento.setNSeqEvento("1");

        br.inf.portalfiscal.mdfe.schema_300.evCancMDFe.EvCancMDFe eventoCancela = new EvCancMDFe();
        eventoCancela.setDescEvento("Cancelamento");
        eventoCancela.setNProt(protocolo);
        eventoCancela.setXJust(justificativa.trim());

        br.inf.portalfiscal.mdfe.schema_300.evCancMDFe.TEvento.InfEvento.DetEvento detEvento = new br.inf.portalfiscal.mdfe.schema_300.evCancMDFe.TEvento.InfEvento.DetEvento();
        detEvento.setVersaoEvento("3.00");
        detEvento.setAny(ObjetoUtil.objectToElement(eventoCancela,
                br.inf.portalfiscal.mdfe.schema_300.evCancMDFe.EvCancMDFe.class, "evCancMDFe"));

        infoEvento.setDetEvento(detEvento);

        enviEvento.setInfEvento(infoEvento);
        return enviEvento;

    }

    public String statusServico(String ambiente, boolean validar) throws Exception {
        br.inf.portalfiscal.mdfe.schema_300.consStatServMDFe.TConsStatServ consStatServ = new br.inf.portalfiscal.mdfe.schema_300.consStatServMDFe.TConsStatServ();

        consStatServ.setTpAmb(ambiente);
        consStatServ.setVersao("3.00");
        consStatServ.setXServ("STATUS");

        br.inf.portalfiscal.mdfe.schema_300.consStatServMDFe.TRetConsStatServ retorno = Mdfe.statusServico(consStatServ,
                validar);
        String status = "";
        status += "Status:" + retorno.getCStat() + "\n";
        status += "Motivo:" + retorno.getXMotivo() + "\n";
        status += "Data:" + retorno.getDhRecbto() + "\n";

        return status;
    }

    public String situacaoMdfe(String ambiente, String chave) throws EmissorException, Exception {
        br.inf.portalfiscal.mdfe.schema_300.consSitMDFe.TConsSitMDFe consSitMdfe = new TConsSitMDFe();

        consSitMdfe.setVersao("3.00");
        consSitMdfe.setTpAmb(ambiente);
        consSitMdfe.setXServ("CONSULTAR");
        consSitMdfe.setChMDFe(chave);

        br.inf.portalfiscal.mdfe.schema_300.consSitMDFe.TRetConsSitMDFe retorno = Mdfe.consultarXml(consSitMdfe, false);

        String result = "";
        result += "Status:" + retorno.getCStat() + "\n";
        result += "Motivo:" + retorno.getXMotivo();

        return result;
    }

    public String consultarNaoEcenrrados(String ambiente, String cnpj) throws Exception {
        br.inf.portalfiscal.mdfe.schema_300.consMDFeNaoEnc.TConsMDFeNaoEnc consMDFeNaoEnc = new TConsMDFeNaoEnc();

        consMDFeNaoEnc.setCNPJ(cnpj);
        consMDFeNaoEnc.setTpAmb(ambiente);
        consMDFeNaoEnc.setVersao("3.00");
        consMDFeNaoEnc.setXServ("CONSULTAR NÃO ENCERRADOS");

        br.inf.portalfiscal.mdfe.schema_300.consMDFeNaoEnc.TRetConsMDFeNaoEnc retorno = Mdfe
                .consultarNaoEncerrado(consMDFeNaoEnc, false);

        String result = "";
        result += "Status:" + retorno.getCStat() + "\n";
        result += "Motivo:" + retorno.getXMotivo() + "\n";
        result += "UF:" + retorno.getCUF() + "\n";

        result = retorno.getInfMDFe().stream()
                .map((inf) -> "Chave :" + inf.getChMDFe() + " Número Protocolo " + inf.getNProt() + "\n")
                .reduce(result, String::concat);

        return result;
    }

    public br.inf.portalfiscal.mdfe.schema_300.evEncMDFe.TEvento encerrar(String chave, String cnpj, String ambiente, String codIbgeUf, String protocolo,
                                                                          String codIbgeUfEnc, String codIbgeMunEnc) throws EmissorException, Exception {
        br.inf.portalfiscal.mdfe.schema_300.evEncMDFe.TEvento enviEvento = new br.inf.portalfiscal.mdfe.schema_300.evEncMDFe.TEvento();
        br.inf.portalfiscal.mdfe.schema_300.evEncMDFe.TEvento.InfEvento infoEvento = new br.inf.portalfiscal.mdfe.schema_300.evEncMDFe.TEvento.InfEvento();

        String id = "ID110112" + chave + "01";

        infoEvento.setId(id);
        infoEvento.setId(id);
        infoEvento.setCOrgao(codIbgeUf);
        infoEvento.setTpAmb(ambiente);
        infoEvento.setCNPJ(cnpj);
        infoEvento.setChMDFe(chave);
        infoEvento.setDhEvento(FormatValor.getInstance().formatarDataNota(new Date()));
        infoEvento.setTpEvento("110112");
        infoEvento.setNSeqEvento("1");

        br.inf.portalfiscal.mdfe.schema_300.evEncMDFe.EvEncMDFe encerrar = new EvEncMDFe();

        encerrar.setCMun(codIbgeMunEnc);
        encerrar.setCUF(codIbgeUfEnc);
        encerrar.setDescEvento("Encerramento");
        encerrar.setDtEnc(FormatValor.getInstance().formatarDataEUA(new Date()));
        encerrar.setNProt(protocolo);

        br.inf.portalfiscal.mdfe.schema_300.evEncMDFe.TEvento.InfEvento.DetEvento detEvento = new br.inf.portalfiscal.mdfe.schema_300.evEncMDFe.TEvento.InfEvento.DetEvento();

        detEvento.setVersaoEvento("3.00");

        detEvento.setAny(ObjetoUtil.objectToElement(encerrar,
                br.inf.portalfiscal.mdfe.schema_300.evEncMDFe.EvEncMDFe.class, "evEncMDFe"));

        infoEvento.setDetEvento(detEvento);
        enviEvento.setVersao("3.00");
        enviEvento.setInfEvento(infoEvento);

        br.inf.portalfiscal.mdfe.schema_300.evEncMDFe.TRetEvento retorno = Mdfe.encerrar(enviEvento, false);

        String result = "";

        result += "Status:" + retorno.getInfEvento().getCStat() + "\n";
        result += "Motivo:" + retorno.getInfEvento().getXMotivo() + "\n";
        result += "Data:" + retorno.getInfEvento().getDhRegEvento() + "\n";

        br.inf.portalfiscal.mdfe.schema_300.evEncMDFe.TProcEvento procEvento = new br.inf.portalfiscal.mdfe.schema_300.evEncMDFe.TProcEvento();

        procEvento.setVersao("3.00");
        procEvento.setEventoMDFe(enviEvento);
        procEvento.setRetEventoMDFe(retorno);

        return enviEvento;

    }


    public br.inf.portalfiscal.mdfe.schema_300.evIncCondutorMDF.TEvento incluirCondutor(String chave, String ambiente, String cnpj, String codUfIbge, String nome, String cpf, Integer seqEvento)
            throws EmissorException, Exception {
        String id;
        if (seqEvento < 10) {
            id = "ID110114" + chave + "0" + String.valueOf(seqEvento);
        } else {
            id = "ID110114" + chave + String.valueOf(seqEvento);
        }

        br.inf.portalfiscal.mdfe.schema_300.evIncCondutorMDF.TEvento enviEvento = new br.inf.portalfiscal.mdfe.schema_300.evIncCondutorMDF.TEvento();
        enviEvento.setVersao("3.00");
        br.inf.portalfiscal.mdfe.schema_300.evIncCondutorMDF.TEvento.InfEvento infoEvento = new br.inf.portalfiscal.mdfe.schema_300.evIncCondutorMDF.TEvento.InfEvento();

        infoEvento.setId(id);

        infoEvento.setCOrgao(codUfIbge);
        infoEvento.setTpAmb(ambiente);
        infoEvento.setCNPJ(cnpj);
        infoEvento.setChMDFe(chave);
        infoEvento.setDhEvento(FormatValor.getInstance().formatarDataNota(new Date()));

        infoEvento.setTpEvento("110114");
        infoEvento.setNSeqEvento(String.valueOf(seqEvento));

        br.inf.portalfiscal.mdfe.schema_300.evIncCondutorMDF.EvIncCondutorMDFe eventoIncluirCondutor = new EvIncCondutorMDFe();

        eventoIncluirCondutor.setCondutor(getCondutor(nome, cpf));
        eventoIncluirCondutor.setDescEvento("Inclusao Condutor");

        br.inf.portalfiscal.mdfe.schema_300.evIncCondutorMDF.TEvento.InfEvento.DetEvento detEvento = new br.inf.portalfiscal.mdfe.schema_300.evIncCondutorMDF.TEvento.InfEvento.DetEvento();
        detEvento.setVersaoEvento("3.00");
        detEvento.setAny(ObjetoUtil.objectToElement(eventoIncluirCondutor,
                br.inf.portalfiscal.mdfe.schema_300.evIncCondutorMDF.EvIncCondutorMDFe.class, "evIncCondutorMDFe"));

        infoEvento.setDetEvento(detEvento);
        enviEvento.setInfEvento(infoEvento);
        return enviEvento;
    }

    private EvIncCondutorMDFe.Condutor getCondutor(String nome, String cpf) {
        EvIncCondutorMDFe.Condutor condutor = new EvIncCondutorMDFe.Condutor();

        condutor.setCPF(cpf);
        condutor.setXNome(nome);

        return condutor;
    }


    private TMDFe.InfMDFe.Emit getEmit(MdfeEmitente emitente) {
        TMDFe.InfMDFe.Emit emit = new TMDFe.InfMDFe.Emit();
        emit.setCNPJ(emitente.getCnpj());
        emit.setEnderEmit(getEnderEmit(emitente));
        emit.setIE(emitente.getIe());
        emit.setXFant(emitente.getFantasia());
        emit.setXNome(emitente.getNome());

        return emit;
    }

    private TEndeEmi getEnderEmit(MdfeEmitente emitente) {
        TEndeEmi enderEmit = new TEndeEmi();

        enderEmit.setXLgr(emitente.getLogradouro());
        enderEmit.setNro(emitente.getNumero());
        enderEmit.setXCpl(StringUtils.isEmpty(emitente.getComplemento()) ? null : emitente.getComplemento());
        enderEmit.setXBairro(emitente.getBairro());
        enderEmit.setCMun(String.valueOf(emitente.getCodigoMunicipio()));
        enderEmit.setXMun(emitente.getNomeMunicipio());
        enderEmit.setCEP(emitente.getCep());

        enderEmit.setUF(TUf.valueOf(emitente.getUf()));

        return enderEmit;
    }

    private TMDFe.InfMDFe.Ide getIde(MdfeCabecalho mdfe) throws ParseException {
        TMDFe.InfMDFe.Ide ide = new TMDFe.InfMDFe.Ide();

        ide.setCDV(String.valueOf(mdfe.getDigitoVerificador()));
        ide.setCMDF(mdfe.getCodigoNumerico());
        ide.setCUF(String.valueOf(mdfe.getUf()));
        ide.setCUF(mdfe.getEmpresa().getCodigoIbgeUf().toString());
        ide.setDhEmi(FormatValor.getInstance().formatarDataNota(new Date()));
        ide.setMod("58");
        ide.setModal(String.valueOf(mdfe.getModal()));
        ide.setNMDF(String.valueOf(Integer.parseInt(mdfe.getNumeroMdfe())));
        ide.setProcEmi(String.valueOf(mdfe.getProcessoEmissao()));
        ide.setSerie(String.valueOf(Integer.parseInt(mdfe.getSerie())));
        ide.setTpAmb(String.valueOf(mdfe.getTipoAmbiente()));
        ide.setTpEmis(String.valueOf(mdfe.getTipoEmissao()));
        ide.setTpEmit(String.valueOf(mdfe.getTipoEmitente()));
        if (mdfe.getTipoEmitente() == 1) {
            ide.setTpTransp(String.valueOf(mdfe.getTipoTransportadora()));
        }
        ide.setUFFim(TUf.valueOf(String.valueOf(mdfe.getUfFim())));
        ide.setUFIni(TUf.valueOf(String.valueOf(mdfe.getUfInicio())));
        ide.setVerProc("1.00");
        ide.setDhIniViagem(FormatValor.getInstance().formatarDataNota(new Date()));
        ide.getInfMunCarrega().addAll(addMunCarregamento(mdfe.getListaMdfeMunicipioCarregamento()));
        ide.getInfPercurso().addAll(addPercurso(mdfe.getListaMdfePercurso()));
        return ide;
    }


    private List<TMDFe.InfMDFe.Ide.InfMunCarrega> addMunCarregamento(Set<MdfeMunicipioCarregamento> carregamentos) {
        List<TMDFe.InfMDFe.Ide.InfMunCarrega> municipios = new ArrayList<>();

        carregamentos.stream().forEach((carregamento) -> {
            TMDFe.InfMDFe.Ide.InfMunCarrega munCarrega = new TMDFe.InfMDFe.Ide.InfMunCarrega();
            munCarrega.setCMunCarrega(carregamento.getCodigoMunicipio());
            munCarrega.setXMunCarrega(carregamento.getNomeMunicipio());
            municipios.add(munCarrega);
        });

        return municipios;
    }

    private Collection<? extends TMDFe.InfMDFe.Ide.InfPercurso> addPercurso(Set<MdfePercurso> listaMdfePercurso) {
        List<TMDFe.InfMDFe.Ide.InfPercurso> percursos = new ArrayList<>();
        listaMdfePercurso.stream().forEach(p -> {
            TMDFe.InfMDFe.Ide.InfPercurso percurso = new TMDFe.InfMDFe.Ide.InfPercurso();
            percurso.setUFPer(TUf.valueOf(p.getUfPercurso()));
            percursos.add(percurso);
        });

        return percursos;
    }

    private TMDFe.InfMDFe.InfAdic getInfAdic(MdfeCabecalho mdfe) {

        TMDFe.InfMDFe.InfAdic info = new TMDFe.InfMDFe.InfAdic();
        info.setInfAdFisco(StringUtils.isEmpty(mdfe.getInformacoesAddFisco()) ? null : mdfe.getInformacoesAddFisco());
        info.setInfCpl(StringUtils.isEmpty(mdfe.getInformacoesAddContribuinte()) ? null : mdfe.getInformacoesAddContribuinte());
        return info;
    }

    private TMDFe.InfMDFe.InfDoc getDoc(Set<MdfeMunicipioDescarregamento> descarregamentos, TipoEmitente tipoEmitente) {
        TMDFe.InfMDFe.InfDoc doc = new TMDFe.InfMDFe.InfDoc();
        doc.getInfMunDescarga().addAll(addMunDescarga(descarregamentos, tipoEmitente));
        return doc;
    }

    private List<TMDFe.InfMDFe.InfDoc.InfMunDescarga> addMunDescarga(Set<MdfeMunicipioDescarregamento> descarregamentos,
                                                                     TipoEmitente tipoEmitente) {
        List<TMDFe.InfMDFe.InfDoc.InfMunDescarga> listMunDesc = new ArrayList<>();

        descarregamentos.stream().forEach((d) -> {
            TMDFe.InfMDFe.InfDoc.InfMunDescarga munDesc = new TMDFe.InfMDFe.InfDoc.InfMunDescarga();
            munDesc.setCMunDescarga(d.getCodigoMunicipio());
            munDesc.setXMunDescarga(d.getNomeMunicipio());

            if (tipoEmitente.equals(TipoEmitente.CARGA_PROPRIA)) {
                d.getListaMdfeInformacaoNfe().forEach(n -> {
                    munDesc.getInfNFe().add(addInfNfe(n));
                });
            } else {
                d.getListaMdfeInformacaoCte().stream().forEach((c) -> {
                    munDesc.getInfCTe().add(addInfCte(c));

                });
            }

            listMunDesc.add(munDesc);
        });

        return listMunDesc;
    }

    private TMDFe.InfMDFe.InfDoc.InfMunDescarga.InfNFe addInfNfe(MdfeInformacaoNfe infoNfe) {
        TMDFe.InfMDFe.InfDoc.InfMunDescarga.InfNFe infNfe = new TMDFe.InfMDFe.InfDoc.InfMunDescarga.InfNFe();

        infNfe.setChNFe(infoNfe.getChaveNfe());
        if (infoNfe.getIndicadorReentrega() == 1) {
            infNfe.setIndReentrega(String.valueOf(infoNfe.getIndicadorReentrega()));
        }
        infNfe.setSegCodBarra(infoNfe.getSegundoCodigoBarras());

        return infNfe;
    }

    private TMDFe.InfMDFe.InfDoc.InfMunDescarga.InfCTe addInfCte(MdfeInformacaoCte infoCte) {
        TMDFe.InfMDFe.InfDoc.InfMunDescarga.InfCTe infCte = new TMDFe.InfMDFe.InfDoc.InfMunDescarga.InfCTe();

        infCte.setChCTe(infoCte.getChaveCte());
        if (infoCte.getIndicadorReentrega() == 1) {
            infCte.setIndReentrega(String.valueOf(infoCte.getIndicadorReentrega()));
        }
        infCte.setSegCodBarra(infoCte.getSegundoCodigoBarras());

        return infCte;
    }

    private TMDFe.InfMDFe.InfModal getModal(MdfeCabecalho mdfe) throws EmissorException {
        MdfeRodoviario mdfeRodoviario = mdfe.getMdfeRodoviario();
        TMDFe.InfMDFe.InfModal modal = new TMDFe.InfMDFe.InfModal();

        Rodo rodo = new Rodo();
        Rodo.InfANTT infANTT = new Rodo.InfANTT();
        infANTT.setRNTRC(mdfe.getMdfeRodoviario().getRntrc());
        rodo.setInfANTT(infANTT);
        if (!mdfeRodoviario.getListaMdfeRodoviarioPedagio().isEmpty()) {
            Rodo.InfANTT.ValePed valepadagio = new Rodo.InfANTT.ValePed();
            valepadagio.getDisp().addAll(addValePedagio(mdfeRodoviario.getListaMdfeRodoviarioPedagio()));
            rodo.getInfANTT().setValePed(valepadagio);
        }
        if (mdfe.getListaMdfeLacre().size() > 0) {
            rodo.getLacRodo().addAll(addLacres(mdfe.getListaMdfeLacre()));
        }
        if (!mdfeRodoviario.getListaMdfeRodoviarioVeiculo().isEmpty()) {
            rodo.setVeicTracao(
                    getVeicTracao(mdfeRodoviario.getListaMdfeRodoviarioVeiculo().iterator().next(), mdfeRodoviario.getListaMdfeRodoviarioMotorista()));
        }

        modal.setAny(ObjetoUtil.objectToElement(rodo, Rodo.class, "rodo"));
        modal.setVersaoModal("3.00");

        return modal;
    }

    private Rodo.VeicTracao getVeicTracao(MdfeRodoviarioVeiculo veiculo, Set<MdfeRodoviarioMotorista> condutores) {
        Rodo.VeicTracao veic = new Rodo.VeicTracao();

        veic.setCInt(StringUtils.isEmpty(veiculo.getCodigoInterno()) ? null : veiculo.getCodigoInterno());

        veic.setPlaca(veiculo.getPlaca());
        if (veiculo.getCapacidadeKg() != null) {
            veic.setCapKG(String.valueOf(veiculo.getCapacidadeKg()));
        }
        if (veiculo.getCapacidadeM3() != null) {
            veic.setCapM3(String.valueOf(veiculo.getCapacidadeM3()));
        }
        if (veiculo.getRenavam() != null) {
            veic.setRENAVAM(veiculo.getRenavam());
        }

        veic.setTara(String.valueOf(veiculo.getTara()));

        veic.setTpCar(veiculo.getTipoCarroceria());
        veic.setTpRod(veiculo.getTipoRodado());
        veic.setUF(br.inf.portalfiscal.mdfe.schema_300.mdfeModalRodoviario.TUf.valueOf(veiculo.getUfLicenciamento()));
        veic.getCondutor().addAll(addCondutor(condutores));

        return veic;
    }

    private List<Rodo.InfANTT.ValePed.Disp> addValePedagio(Set<MdfeRodoviarioPedagio> pedagios) {
        List<Rodo.InfANTT.ValePed.Disp> ped = new ArrayList<>();
        pedagios.stream().forEach((d) -> {
            Rodo.InfANTT.ValePed.Disp pedagio = new Rodo.InfANTT.ValePed.Disp();
            pedagio.setCNPJForn(d.getCnpjFornecedor().trim());
            pedagio.setVValePed(FormatValor.getInstance().formatarValor(d.getValor()));
            if (d.getCnpjCpfResponsavel().length() == 14) {
                pedagio.setCNPJPg(d.getCnpjCpfResponsavel().trim());
            } else {
                pedagio.setCPFPg(d.getCnpjCpfResponsavel().trim());
            }

            pedagio.setNCompra(d.getNumeroComprovante());
            ped.add(pedagio);
        });

        return ped;

    }

    private List<Rodo.LacRodo> addLacres(Set<MdfeLacre> lacres) {
        List<Rodo.LacRodo> lac = new ArrayList<>();
        lacres.stream().forEach((d) -> {
            Rodo.LacRodo lacre = new Rodo.LacRodo();
            lacre.setNLacre(d.getNumeroLacre().trim());
            lac.add(lacre);
        });

        return lac;

    }

    private List<Rodo.VeicTracao.Condutor> addCondutor(Set<MdfeRodoviarioMotorista> condutores) {
        List<Rodo.VeicTracao.Condutor> cond = new ArrayList<>();
        condutores.stream().forEach((d) -> {
            Rodo.VeicTracao.Condutor codutor = new Rodo.VeicTracao.Condutor();
            codutor.setCPF(d.getCpf().trim());
            codutor.setXNome(d.getNome().trim());
            cond.add(codutor);
        });

        return cond;

    }

    private TMDFe.InfMDFe.Tot getTotal(MdfeCabecalho mdfe) {
        TMDFe.InfMDFe.Tot tot = new TMDFe.InfMDFe.Tot();

        tot.setCUnid(mdfe.getCodigoUnidadeMedida());
        tot.setQCarga(FormatValor.getInstance().formatarQuantidade(mdfe.getPesoBrutoCarga()));
        if (mdfe.getQuantidadeTotalNfe() > 0) {
            tot.setQNFe(String.valueOf(mdfe.getQuantidadeTotalNfe()));
        }
        if (mdfe.getQuantidadeTotalCte() > 0) {
            tot.setQCTe(String.valueOf(mdfe.getQuantidadeTotalCte()));
        }

        if (mdfe.getQuantidadeTotalMdfe() > 0) {
            tot.setQMDFe(String.valueOf(mdfe.getQuantidadeTotalMdfe()));
        }
        tot.setVCarga(FormatValor.getInstance().formatarValor(mdfe.getValorCarga()));

        return tot;
    }

    private List<TMDFe.InfMDFe.Seg> getSeguro(Set<MdfeInformacaoSeguro> seguros) {
        List<TMDFe.InfMDFe.Seg> seg = new ArrayList<>();
        seguros.stream().forEach((d) -> {
            TMDFe.InfMDFe.Seg seguro = new TMDFe.InfMDFe.Seg();
            if (d.getCnpjSeguradora() != null && !d.getCnpjSeguradora().isEmpty()) {
                TMDFe.InfMDFe.Seg.InfSeg infSeg = new TMDFe.InfMDFe.Seg.InfSeg();
                infSeg.setCNPJ(d.getCnpjSeguradora());
                infSeg.setXSeg(d.getSeguradora());
                seguro.setInfSeg(infSeg);
            }
            if (d.getCnpjCpf() != null && !d.getCnpjCpf().isEmpty()) {
                TMDFe.InfMDFe.Seg.InfResp infResp = new TMDFe.InfMDFe.Seg.InfResp();
                if (d.getCnpjCpf().length() == 14) {
                    infResp.setCNPJ(d.getCnpjCpf());
                } else {
                    infResp.setCPF(d.getCnpjCpf());
                }
                infResp.setRespSeg(String.valueOf(d.getResponsavel()));

                seguro.setInfResp(infResp);
            }

            if (d.getApolice() != null && !d.getApolice().isEmpty()) {
                seguro.setNApol(d.getApolice());
            }

            if (d.getAverbacao() != null && !d.getAverbacao().isEmpty()) {
                seguro.getNAver().add(d.getAverbacao());
            }

            seg.add(seguro);
        });

        return seg;
    }

}
