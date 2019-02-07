package com.chronos.bo.nfe;

import com.chronos.calc.dto.TributosProduto;
import com.chronos.calc.enuns.Csosn;
import com.chronos.calc.enuns.Cst;
import com.chronos.calc.enuns.CstIpi;
import com.chronos.calc.enuns.CstPisCofins;
import com.chronos.controll.nfe.NfeCalculoControll;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.view.*;
import com.chronos.repository.*;
import com.chronos.service.ChronosException;
import com.chronos.transmissor.infra.enuns.FormatoImpressaoDanfe;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import com.chronos.util.cdi.ManualCDILookup;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by john on 29/09/17.
 */
public class NfeUtil extends ManualCDILookup implements Serializable {


    private IcmsRepository icmsRepository;
    private IcmsCustomRepository icmsCustomRepository;
    private IpiRepository ipiRepository;
    private IbptRepository ibptRepository;
    private PisRepository pisRepository;
    private CofinsRepository cofinsRepository;
    private IssRepository issRepository;

    @Inject
    private Repository<NotaFiscalTipo> tiposNotaFiscal;







    public NfeCabecalho calcularTotalNFe(NfeCabecalho nfe) throws Exception {
        ibptRepository = getFacadeWithJNDI(IbptRepository.class);
        boolean servico;
        //valores comuns
        BigDecimal totalProdutos = BigDecimal.ZERO;
        BigDecimal valorFrete = BigDecimal.ZERO;
        BigDecimal valorSeguro = BigDecimal.ZERO;
        BigDecimal valorOutrasDespesas = BigDecimal.ZERO;
        BigDecimal desconto = BigDecimal.ZERO;
        BigDecimal valorTotalTributos = BigDecimal.ZERO;
        // valores ISSQN
        BigDecimal totalServicos = BigDecimal.ZERO;
        BigDecimal baseCalculoIssqn = BigDecimal.ZERO;
        BigDecimal valorIssqn = BigDecimal.ZERO;
        BigDecimal valorPisIssqn = BigDecimal.ZERO;
        BigDecimal valorCofinsIssqn = BigDecimal.ZERO;
        BigDecimal baseCalculoPrevidencia = BigDecimal.ZERO;
        BigDecimal valorRetidoPrevidencia = BigDecimal.ZERO;
        // valores vendas
        BigDecimal baseCalculoIcms = BigDecimal.ZERO;
        BigDecimal valorIcms = BigDecimal.ZERO;
        BigDecimal baseCalculoIcmsSt = BigDecimal.ZERO;
        BigDecimal valorIcmsSt = BigDecimal.ZERO;
        BigDecimal valorIcmsDesonerado = BigDecimal.ZERO;
        BigDecimal valorIpi = BigDecimal.ZERO;
        BigDecimal valorPis = BigDecimal.ZERO;
        BigDecimal valorCofins = BigDecimal.ZERO;
        BigDecimal valorNotaFiscal = BigDecimal.ZERO;

        BigDecimal impostoFederal = BigDecimal.ZERO;
        BigDecimal impostoEstadual = BigDecimal.ZERO;
        BigDecimal impostoMunicipal = BigDecimal.ZERO;


        BigDecimal impFederal = BigDecimal.ZERO;
        BigDecimal impEstadual = BigDecimal.ZERO;
        BigDecimal impMunicipal = BigDecimal.ZERO;


        NfeCalculoControll calculo = new NfeCalculoControll();
        for (NfeDetalhe item : nfe.getListaNfeDetalhe()) {
            servico = !StringUtils.isEmpty(item.getProduto().getServico()) && item.getProduto().getServico().equals("S");
            valorFrete = valorFrete.add(item.getValorFrete());
            valorSeguro = valorSeguro.add(item.getValorSeguro());
            valorOutrasDespesas = valorOutrasDespesas.add(item.getValorOutrasDespesas());
            desconto = desconto.add(item.getValorDesconto());
            item.calcularValorTotalProduto();
            if (servico) {
                totalServicos = totalServicos.add(item.getValorBrutoProduto());
                baseCalculoIssqn = baseCalculoIssqn.add(item.getNfeDetalheImpostoIssqn().getBaseCalculoIssqn());
                valorIssqn = valorIssqn.add(item.getNfeDetalheImpostoIssqn().getValorIssqn());
                valorPisIssqn = item.getNfeDetalheImpostoPis() == null ? BigDecimal.ZERO : valorPisIssqn.add(item.getNfeDetalheImpostoPis().getValorPis());
                valorCofinsIssqn = item.getNfeDetalheImpostoCofins() == null ? BigDecimal.ZERO : valorCofinsIssqn.add(item.getNfeDetalheImpostoCofins().getValorCofins());

            } else {
                totalProdutos = totalProdutos.add(item.getValorBrutoProduto());
                baseCalculoIcms = baseCalculoIcms.add(item.getNfeDetalheImpostoIcms().getBaseCalculoIcms());
                valorIcms = valorIcms.add(item.getNfeDetalheImpostoIcms().getValorIcms());
                baseCalculoIcmsSt = baseCalculoIcmsSt.add(item.getNfeDetalheImpostoIcms().getValorBaseCalculoIcmsSt());
                valorIcmsSt = valorIcmsSt.add(item.getNfeDetalheImpostoIcms().getValorIcmsSt());
                valorIpi = item.getNfeDetalheImpostoIpi() == null ? BigDecimal.ZERO : valorIpi.add(item.getNfeDetalheImpostoIpi().getValorIpi());
                valorPis = item.getNfeDetalheImpostoPis() == null ? BigDecimal.ZERO : valorPis.add(item.getNfeDetalheImpostoPis().getValorPis());
                valorCofins = item.getNfeDetalheImpostoCofins() == null ? BigDecimal.ZERO : valorCofins.add(item.getNfeDetalheImpostoCofins().getValorCofins());
            }


            String ncm = servico ? item.getProduto().getCodigoLst() : item.getNcm();
            List<Filtro> listaFiltro = new ArrayList<>();
            listaFiltro.add(new Filtro("AND", "ncm", Filtro.IGUAL, ncm));
            Ibpt tb = ibptRepository.get(Ibpt.class, listaFiltro);

            if (tb != null) {
                item = calculo.calculoIbpt(item, tb);

                impFederal = Optional.ofNullable(item.getImpostoFederal()).orElse(BigDecimal.ZERO);
                impEstadual = Optional.ofNullable(item.getImpostoEstadual()).orElse(BigDecimal.ZERO);
                impMunicipal = Optional.ofNullable(item.getImpostoMunicipal()).orElse(BigDecimal.ZERO);

                impostoFederal = impostoFederal.add(impFederal);
                impostoEstadual = impostoEstadual.add(impEstadual);
                impostoMunicipal = impostoMunicipal.add(impMunicipal);


            }

            item.setValorTotalTributosFederais(impFederal);
            item.setValorTotalTributosEstaduais(impEstadual);
            item.setValorTotalTributosMunicipais(impostoMunicipal);
            item.setValorTotalTributos(impFederal.add(impEstadual).add(impMunicipal));


        }
        valorNotaFiscal = valorNotaFiscal.add(valorIpi).add(valorIcmsSt).add(totalProdutos);
        nfe.setValorFrete(valorFrete);
        nfe.setValorDespesasAcessorias(valorOutrasDespesas);
        nfe.setValorSeguro(valorSeguro);
        nfe.setValorDesconto(desconto);

        nfe.setValorServicos(totalServicos);
        nfe.setBaseCalculoIssqn(baseCalculoIssqn);
        nfe.setValorIssqn(valorIssqn);
        nfe.setValorPisIssqn(valorPisIssqn);
        nfe.setValorCofinsIssqn(valorCofinsIssqn);

        nfe.setBaseCalculoPrevidencia(baseCalculoIssqn);
        nfe.setValorRetidoPrevidencia(valorRetidoPrevidencia);

        nfe.setValorIcmsDesonerado(valorIcmsDesonerado);

        nfe.setValorTotalProdutos(totalProdutos);
        nfe.setBaseCalculoIcms(baseCalculoIcms);
        nfe.setValorIcms(valorIcms);
        nfe.setBaseCalculoIcmsSt(baseCalculoIcmsSt);
        nfe.setValorIcmsSt(valorIcmsSt);
        nfe.setValorIpi(valorIpi);
        nfe.setValorPis(valorPis);
        nfe.setValorCofins(valorCofins);
        nfe.calcularValorTotal();
        String msg = "Trib. Aprox. Federal R$ " + new DecimalFormat("#,###,##0.00").format(impostoFederal)
                + " e R$ " + new DecimalFormat("#,###,##0.00").format(impostoEstadual) + " Estadual "
                + "e R$ " + new DecimalFormat("#,###,##0.00").format(impostoMunicipal) + " Municipal Fonte IBPT";

        valorTotalTributos = valorTotalTributos.add(impostoFederal).add(impostoEstadual).add(impostoMunicipal);
        nfe.setValorTotalTributosFederais(impostoFederal);
        nfe.setValorTotalTributosEstaduais(impostoEstadual);
        nfe.setValorTotalTributosMunicipais(impostoMunicipal);
        nfe.setValorTotalTributos(valorTotalTributos);
        nfe.setInformacoesAddContribuinte(msg);


        return nfe;
    }


    public NfeEmitente getEmitente(Empresa empresa) {
        NfeEmitente emitente = new NfeEmitente();

        EmpresaEndereco endereco = empresa.buscarEnderecoPrincipal();

        emitente.setCpfCnpj(empresa.getCnpj());
        emitente.setInscricaoEstadual(empresa.getInscricaoEstadual());
        emitente.setNome(empresa.getRazaoSocial());
        emitente.setFantasia(empresa.getNomeFantasia());
        emitente.setLogradouro(endereco.getLogradouro());
        emitente.setNumero(endereco.getNumero());
        emitente.setComplemento(endereco.getComplemento());
        emitente.setBairro(endereco.getBairro());
        emitente.setCodigoMunicipio(endereco.getMunicipioIbge());
        emitente.setNomeMunicipio(endereco.getCidade());
        emitente.setUf(endereco.getUf());
        emitente.setCep(endereco.getCep());
        emitente.setCrt(empresa.getCrt());
        emitente.setCodigoPais(1058);
        emitente.setNomePais("BRASIL");
        emitente.setTelefone(endereco.getFone());
        emitente.setInscricaoEstadualSt(empresa.getInscricaoEstadualSt());
        emitente.setInscricaoMunicipal(empresa.getInscricaoMunicipal());
        emitente.setCnae(empresa.getCodigoCnaePrincipal());


        return emitente;
    }

    public NfeCabecalho definirDadosPadrao(ModeloDocumento modelo, Empresa empresa) {
        NfeCabecalho nfe = new NfeCabecalho();

        nfe.setFormatoImpressaoDanfe(modelo == ModeloDocumento.NFE ? FormatoImpressaoDanfe.DANFE_RETRATO.getCodigo() : FormatoImpressaoDanfe.DANFE_NFCE.getCodigo());
        nfe.setUfEmitente(empresa.getCodigoIbgeUf());
        nfe.setCodigoMunicipio(empresa.getCodigoIbgeCidade());
        nfe.setCodigoModelo(String.valueOf(modelo.getCodigo()));
        nfe.setEmpresa(empresa);

        nfe.getEmitente().setNfeCabecalho(nfe);

        nfe.getDestinatario().setNfeCabecalho(nfe);


        nfe.getFatura().setNfeCabecalho(nfe);


        return nfe;
    }

    public NfeDetalhe defineTributacao(NfeDetalhe item, TributOperacaoFiscal operacaoFiscal, NfeDestinatario destinatario) throws Exception {


        Empresa empresa = item.getNfeCabecalho().getEmpresa();
        issRepository = getFacadeWithJNDI(IssRepository.class);
        icmsCustomRepository = getFacadeWithJNDI(IcmsCustomRepository.class);
        icmsRepository = getFacadeWithJNDI(IcmsRepository.class);
        ipiRepository = getFacadeWithJNDI(IpiRepository.class);
        pisRepository = getFacadeWithJNDI(PisRepository.class);
        cofinsRepository = getFacadeWithJNDI(CofinsRepository.class);

        String ufEmpresa = empresa.buscarEnderecoPrincipal().getUf();

        TributosProduto tributos = new TributosProduto();
        List<Filtro> listaFiltro = new ArrayList<>();

        //Valores produto
        tributos.setValorProduto(item.getValorUnitarioComercial());
        tributos.setDesconto(item.getValorDesconto());
        tributos.setFrete(item.getValorFrete());
        tributos.setOutrasDespesas(item.getValorOutrasDespesas());
        tributos.setQuantidadeProduto(item.getQuantidadeComercial());
        tributos.setSeguro(item.getValorSeguro());
        boolean servico = item.getProduto().getServico() != null && item.getProduto().getServico().equals("S");
        tributos.setServico(servico);
        boolean operacaoInterna = destinatario == null || StringUtils.isEmpty(destinatario.getUf()) || empresa.buscarEnderecoPrincipal().getUf().equals(destinatario.getUf());


        if (item.getProduto().getTributIcmsCustomCab() != null) {
            listaFiltro.add(new Filtro(Filtro.AND, "id", Filtro.IGUAL, item.getProduto().getTributIcmsCustomCab().getId()));
            listaFiltro.add(new Filtro(Filtro.AND, "ufDestino", Filtro.IGUAL, destinatario.getUf() == null ? ufEmpresa : destinatario.getUf()));
            ViewTributacaoIcmsCustom icms = icmsCustomRepository.get(ViewTributacaoIcmsCustom.class, listaFiltro);
            if (icms != null) {
                if (icms.getCfop() == null) {
                    throw new ChronosException("Não existe CFOP definido na tributação de ICMS definida para os parâmetros informados. Operação não realizada.");
                }
                item.setNfeDetalheImpostoIcms(new NfeDetalheImpostoIcms());
                item.getNfeDetalheImpostoIcms().setNfeDetalhe(item);
                item.setCfop(icms.getCfop());
                item.getNfeDetalheImpostoIcms().setOrigemMercadoria(Integer.valueOf(icms.getOrigemMercadoria()));
                item.getNfeDetalheImpostoIcms().setCstIcms(icms.getCstB());
                item.getNfeDetalheImpostoIcms().setCsosn(icms.getCsosnB());
                item.getNfeDetalheImpostoIcms().setModalidadeBcIcms(Integer.valueOf(icms.getModalidadeBc()));
                item.getNfeDetalheImpostoIcms().setModalidadeBcIcmsSt(Integer.valueOf(icms.getModalidadeBcSt()));

                tributos.setCsosn(Csosn.valueOfCodigo(item.getNfeDetalheImpostoIcms().getCsosn()));
                tributos.setCst(Cst.valueOfCodigo(item.getNfeDetalheImpostoIcms().getCstIcms()));
                tributos.setPercentualReducao(icms.getPorcentoBc());
                tributos.setPercentualIcms(icms.getAliquota());
                tributos.setPercentualMva(icms.getMva());
                tributos.setPercentualReducaoSt(icms.getPorcentoBcSt());

                tributos.setPercentualIcmsSt(icms.getAliquotaIcmsSt());
                tributos.setPercentualCredito(BigDecimal.ZERO);

            } else {
                throw new ChronosException("Não existe tributação de ICMS definida para o produto : " + item.getNomeProduto() + ". Operação não realizada.");
            }
        } else {
            if (servico) {
                if (StringUtils.isEmpty(empresa.getInscricaoMunicipal())) {
                    throw new ChronosException("IM não definida.");
                }

                if (StringUtils.isEmpty(item.getProduto().getCodigoLst())) {
                    throw new ChronosException("Codigo LST do serviço não definido.");
                }
                item.setNfeDetalheImpostoIssqn(new NfeDetalheImpostoIssqn());
                item.getNfeDetalheImpostoIssqn().setNfeDetalhe(item);

                item.setCfop(operacaoInterna ? 5933 : 6933);

                listaFiltro.add(new Filtro("tributOperacaoFiscal.id", operacaoFiscal.getId()));


                // ISSQN
                // TributIss iss = operacaoFiscal.getListaIss().get(0);
                TributIss iss = issRepository.get(TributIss.class, listaFiltro);
                if (iss == null) {
                    throw new ChronosException("Não existe tributação de ISS definida para o " + item.getProduto().getNome() + " informados. Operação não realizada.");
                }
                if(StringUtils.isEmpty(item.getProduto().getCodigoLst().trim()) || item.getProduto().getCodigoLst().trim().isEmpty()){
                    throw new ChronosException("Não existe código de LST para o " + item.getProduto().getNome() + " informados. Operação não realizada.");
                }
                tributos.setPercentualIssqn(iss.getAliquotaPorcento());
                item.getNfeDetalheImpostoIssqn().setMunicipioIssqn(Integer.valueOf(empresa.getInscricaoMunicipal()));
                item.getNfeDetalheImpostoIssqn().setItemListaServicos(Integer.valueOf(item.getProduto().getCodigoLst().trim()));
                item.getNfeDetalheImpostoIssqn().setIndicadorExigibilidadeIss(iss.getIndicadorExigibilidade());
                item.getNfeDetalheImpostoIssqn().setIndicadorIncentivoFiscal(iss.getIndicadorIncentivoFiscal());
                item.getNfeDetalheImpostoIssqn().setMunicipioIncidencia(operacaoInterna ? empresa.buscarEnderecoPrincipal().getMunicipioIbge() : destinatario.getCodigoMunicipio());
                item.getNfeDetalheImpostoIssqn().setIndicadorIncentivoFiscal(iss.getIndicadorIncentivoFiscal());

            } else {

                if (operacaoFiscal == null) {
                    throw new ChronosException("Operação Fiscal não definida.Operação não realizada.");
                }

                listaFiltro.add(new Filtro("idTributOperacaoFiscal", operacaoFiscal.getId()));
                listaFiltro.add(new Filtro("idTributGrupoTributario", item.getProduto().getTributGrupoTributario().getId()));
                listaFiltro.add(new Filtro("ufDestino", "=", destinatario == null || destinatario.getUf() == null ? ufEmpresa : destinatario.getUf()));

                ViewTributacaoIcms icms = icmsRepository.get(ViewTributacaoIcms.class, listaFiltro);
                if (icms != null) {
                    if (icms.getCfop() == null) {
                        throw new ChronosException("Não existe CFOP definido na tributação de ICMS definida para os parâmetros informados. Operação não realizada.");
                    }
                    if (icms.getOrigemMercadoria() == null) {
                        throw new ChronosException("Origem da mercadoria não definida na tributação de ICMS definida para os parâmetros informados. Operação não realizada.");
                    }
                    if (icms.getModalidadeBc() == null) {
                        throw new ChronosException("Modalidade da base de calculo do ICMS não definida na tributação de ICMS definida para os parâmetros informados.\n Operação não realizada.");
                    }
                    if (icms.getModalidadeBcSt() == null) {
                        throw new ChronosException("Modalidade da base de calculo do ICMS ST não definida na tributação de ICMS definida para os parâmetros informados.\n Operação não realizada.");
                    }
                    item.setNfeDetalheImpostoIcms(new NfeDetalheImpostoIcms());
                    item.getNfeDetalheImpostoIcms().setNfeDetalhe(item);
                    item.setCfop(icms.getCfop());
                    item.getNfeDetalheImpostoIcms().setOrigemMercadoria(Integer.valueOf(icms.getOrigemMercadoria()));
                    item.getNfeDetalheImpostoIcms().setCstIcms(icms.getCstB());
                    item.getNfeDetalheImpostoIcms().setCsosn(icms.getCsosnB());

                    item.getNfeDetalheImpostoIcms().setAliquotaIcms(icms.getAliquota());

                    item.getNfeDetalheImpostoIcms().setModalidadeBcIcms(Integer.valueOf(icms.getModalidadeBc()));
                    item.getNfeDetalheImpostoIcms().setModalidadeBcIcmsSt(Integer.valueOf(icms.getModalidadeBcSt()));
                    item.getNfeDetalheImpostoIcms().setPercentualMvaIcmsSt(icms.getMva());
                    item.getNfeDetalheImpostoIcms().setAliquotaIcmsSt(icms.getAliquotaIcmsSt());

                    tributos.setCsosn(Csosn.valueOfCodigo(item.getNfeDetalheImpostoIcms().getCsosn()));
                    tributos.setCst(Cst.valueOfCodigo(item.getNfeDetalheImpostoIcms().getCstIcms()));
                    tributos.setPercentualReducao(icms.getPorcentoBc());
                    tributos.setPercentualIcms(icms.getAliquota());
                    tributos.setPercentualMva(icms.getMva());
                    tributos.setPercentualReducaoSt(icms.getPorcentoBcSt());

                    tributos.setPercentualIcmsSt(icms.getAliquotaIcmsSt());
                    tributos.setPercentualCredito(BigDecimal.ZERO);

                } else {
                    throw new ChronosException("Não existe tributação de ICMS definida para o produto : " + item.getNomeProduto() + ". Operação não realizada.");
                }
                if (operacaoFiscal.getDestacaIpi()) {
                    // IPI
                    listaFiltro.clear();
                    listaFiltro.add(new Filtro("idTributOperacaoFiscal", operacaoFiscal.getId()));


                    ViewTributacaoIpi ipi = ipiRepository.get(ViewTributacaoIpi.class, listaFiltro);
                    if (ipi != null) {
                        item.setNfeDetalheImpostoIpi(new NfeDetalheImpostoIpi());
                        item.getNfeDetalheImpostoIpi().setNfeDetalhe(item);
                        item.getNfeDetalheImpostoIpi().setCstIpi(ipi.getCstIpi());
                        item.getNfeDetalheImpostoIpi().setAliquotaIpi(ipi.getAliquotaPorcento());
                        tributos.setPercentualIpi(ipi.getAliquotaPorcento());
                        tributos.setCstIpi(CstIpi.valueOfCodigo(item.getNfeDetalheImpostoIpi().getCstIpi()));
                    } else if (empresa.getCrt().equals("2")) {
                        throw new ChronosException("Não existe tributação de IPI definida para os parâmetros informados. Operação não realizada.");
                    }
                }


            }
            if (operacaoFiscal.getDestacaPisCofins()) {
                // PIS
                listaFiltro.clear();

                listaFiltro.add(new Filtro("idTributOperacaoFiscal", operacaoFiscal.getId()));


                ViewTributacaoPis pis = pisRepository.get(ViewTributacaoPis.class, listaFiltro);

                if (pis != null) {
                    item.setNfeDetalheImpostoPis(new NfeDetalheImpostoPis());
                    item.getNfeDetalheImpostoPis().setNfeDetalhe(item);
                    item.getNfeDetalheImpostoPis().setCstPis(pis.getCstPis());
                    item.getNfeDetalheImpostoPis().setAliquotaPisPercentual(pis.getAliquotaPorcento());
                    item.getNfeDetalheImpostoPis().setAliquotaPisReais(pis.getAliquotaUnidade());
                    tributos.setCstPisCofins(CstPisCofins.valueOfCodigo(item.getNfeDetalheImpostoPis().getCstPis()));
                    tributos.setPercentualPis(pis.getAliquotaPorcento());
                    tributos.setPercentualPisReais(pis.getAliquotaUnidade());
                }

                // COFINS
                listaFiltro.clear();
                listaFiltro.add(new Filtro("idTributOperacaoFiscal", operacaoFiscal.getId()));


                ViewTributacaoCofins cofins = cofinsRepository.get(ViewTributacaoCofins.class, listaFiltro);
                if (cofins != null) {
                    item.setNfeDetalheImpostoCofins(new NfeDetalheImpostoCofins());
                    item.getNfeDetalheImpostoCofins().setNfeDetalhe(item);
                    item.getNfeDetalheImpostoCofins().setCstCofins(cofins.getCstCofins());
                    item.getNfeDetalheImpostoCofins().setAliquotaCofinsPercentual(cofins.getAliquotaPorcento());
                    item.getNfeDetalheImpostoCofins().setAliquotaCofinsReais(cofins.getAliquotaUnidade());
                    tributos.setPercentualCofins(cofins.getAliquotaPorcento());
                    tributos.setPercentualCofinsRais(cofins.getAliquotaUnidade());
                }
            }

        }


        NfeCalculoControll calculo = new NfeCalculoControll(tributos);
        item = calculo.calcularTributacao(item, empresa, destinatario);
        return item;
    }
}
