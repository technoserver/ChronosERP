package com.chronos.service.comercial;

import br.inf.portalfiscal.nfe.schema_4.procNFe.TNFe;
import com.chronos.dto.ImpostoDTO;
import com.chronos.modelo.entidades.ConverterCst;
import com.chronos.modelo.entidades.NfeDetalheImpostoIcms;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class DefinirCstService implements Serializable {

    private static final long serialVersionUID = 1L;

    public NfeDetalheImpostoIcms definirIcms(TNFe.InfNFe.Det.Imposto.ICMS icms, List<ConverterCst> csts, String crt) {
        NfeDetalheImpostoIcms detalheImpostoIcms;
        ImpostoDTO imposto = new ImpostoDTO();

        if (icms.getICMS00() != null) {
            imposto.setCst(icms.getICMS00().getCST());

            imposto.setOrigem(icms.getICMS00().getOrig());
            imposto.setModalidadeBaseCalculo(icms.getICMS00().getModBC());
            imposto.setBaseCalculoIcms(icms.getICMS00().getVBC());
            imposto.setAliquotaIcms(icms.getICMS00().getPICMS());
            imposto.setValorIcms(icms.getICMS00().getVICMS());

            imposto.setCst(definirCst(csts, imposto, crt));
        }

        if (icms.getICMS10() != null) {
            imposto.setCst(icms.getICMS10().getCST());

            imposto.setOrigem(icms.getICMS10().getOrig());

            imposto.setModalidadeBaseCalculo(icms.getICMS10().getModBC());
            imposto.setBaseCalculoIcms(icms.getICMS10().getVBC());
            imposto.setBaseCalculoIcms(icms.getICMS10().getVBC());
            imposto.setValorIcms(icms.getICMS10().getVICMS());

            imposto.setModalidadeBaseCalculoST(icms.getICMS10().getModBCST());
            imposto.setMva(icms.getICMS10().getPMVAST());
            imposto.setReducaoBaseCalculoIcmsST(icms.getICMS10().getPRedBCST());
            imposto.setBaseCalculoIcmsST(icms.getICMS10().getVBCST());
            imposto.setAliquotaIcmsST(icms.getICMS10().getPICMSST());
            imposto.setValorIcmsST(icms.getICMS10().getVICMSST());

            imposto.setCst(definirCst(csts, imposto, crt));

        }

        if (icms.getICMS20() != null) {
            imposto.setCst(icms.getICMS20().getCST());

            imposto.setOrigem(icms.getICMS20().getOrig());
            imposto.setModalidadeBaseCalculo(icms.getICMS20().getModBC());
            imposto.setBaseCalculoIcms(icms.getICMS20().getVBC());
            imposto.setAliquotaIcms(icms.getICMS20().getPICMS());
            imposto.setValorIcms(icms.getICMS20().getVICMS());
            imposto.setReducaoBaseCalculoIcms(icms.getICMS20().getPRedBC());

            imposto.setCst(definirCst(csts, imposto, crt));

        }

        if (icms.getICMS30() != null) {
            imposto.setCst(icms.getICMS30().getCST());

            imposto.setOrigem(icms.getICMS30().getOrig());

            imposto.setModalidadeBaseCalculoST(icms.getICMS30().getModBCST());
            imposto.setMva(icms.getICMS30().getPMVAST());
            imposto.setReducaoBaseCalculoIcmsST(icms.getICMS30().getPRedBCST());
            imposto.setBaseCalculoIcmsST(icms.getICMS30().getVBCST());
            imposto.setAliquotaIcmsST(icms.getICMS30().getPICMSST());
            imposto.setValorIcmsST(icms.getICMS30().getVICMSST());

            imposto.setCst(definirCst(csts, imposto, crt));
        }

        if (icms.getICMS40() != null) {
            imposto.setCst(icms.getICMS40().getCST());

            imposto.setOrigem(icms.getICMS40().getOrig());

            imposto.setCst(definirCst(csts, imposto, crt));
        }
        if (icms.getICMS51() != null) {
            imposto.setCst(icms.getICMS51().getCST());

            imposto.setOrigem(icms.getICMS51().getOrig());

            imposto.setCst(definirCst(csts, imposto, crt));
        }

        if (icms.getICMS60() != null) {
            imposto.setCst(icms.getICMS60().getCST());
            imposto.setOrigem(icms.getICMS60().getOrig());

            imposto.setCst(definirCst(csts, imposto, crt));

        }

        if (icms.getICMS70() != null) {
            imposto.setCst(icms.getICMS70().getCST());

            imposto.setOrigem(icms.getICMS70().getOrig());
            imposto.setModalidadeBaseCalculo(icms.getICMS70().getModBC());
            imposto.setReducaoBaseCalculoIcms(icms.getICMS70().getPRedBC());
            imposto.setBaseCalculoIcms(icms.getICMS70().getVBC());
            imposto.setAliquotaIcms(icms.getICMS70().getPICMS());
            imposto.setValorIcms(icms.getICMS70().getVICMS());

            imposto.setModalidadeBaseCalculoST(icms.getICMS70().getModBCST());
            imposto.setMva(icms.getICMS70().getPMVAST());
            imposto.setReducaoBaseCalculoIcmsST(icms.getICMS70().getPRedBCST());
            imposto.setBaseCalculoIcmsST(icms.getICMS70().getVBCST());
            imposto.setAliquotaIcmsST(icms.getICMS70().getPICMSST());
            imposto.setValorIcmsST(icms.getICMS70().getVICMSST());

            imposto.setCst(definirCst(csts, imposto, crt));
        }

        if (icms.getICMS90() != null) {
            imposto.setCst(icms.getICMS90().getCST());

            imposto.setOrigem(icms.getICMS90().getOrig());
            imposto.setModalidadeBaseCalculo(icms.getICMS90().getModBC());
            imposto.setReducaoBaseCalculoIcms(icms.getICMS90().getPRedBC());
            imposto.setBaseCalculoIcms(icms.getICMS90().getVBC());
            imposto.setAliquotaIcms(icms.getICMS90().getPICMS());
            imposto.setValorIcms(icms.getICMS90().getVICMS());

            imposto.setModalidadeBaseCalculoST(icms.getICMS90().getModBCST());
            imposto.setMva(icms.getICMS90().getPMVAST());
            imposto.setReducaoBaseCalculoIcmsST(icms.getICMS90().getPRedBCST());
            imposto.setBaseCalculoIcmsST(icms.getICMS90().getVBCST());
            imposto.setAliquotaIcmsST(icms.getICMS90().getPICMSST());
            imposto.setValorIcmsST(icms.getICMS90().getVICMSST());

            imposto.setCst(definirCst(csts, imposto, crt));

        }

        if (icms.getICMSSN101() != null) {
            imposto.setCst(icms.getICMSSN101().getCSOSN());
            imposto.setOrigem(icms.getICMSSN101().getOrig());
            imposto.setAliquotaCreditoIcms(icms.getICMSSN101().getPCredSN());
            imposto.setValorCreditoIcms(icms.getICMSSN101().getVCredICMSSN());

            imposto.setCst(definirCst(csts, imposto, crt));

        }
        if (icms.getICMSSN102() != null) {
            imposto.setCst(icms.getICMSSN101().getCSOSN());

            imposto.setOrigem(icms.getICMSSN102().getOrig());

            imposto.setCst(definirCst(csts, imposto, crt));
        }
        if (icms.getICMSSN201() != null) {
            imposto.setCst(icms.getICMSSN201().getCSOSN());

            imposto.setOrigem(icms.getICMSSN201().getOrig());

            imposto.setModalidadeBaseCalculoST(icms.getICMSSN201().getModBCST());
            imposto.setMva(icms.getICMSSN201().getPMVAST());
            imposto.setReducaoBaseCalculoIcmsST(icms.getICMSSN201().getPRedBCST());
            imposto.setBaseCalculoIcmsST(icms.getICMSSN201().getVBCST());
            imposto.setAliquotaIcmsST(icms.getICMSSN201().getPICMSST());
            imposto.setValorIcmsST(icms.getICMSSN201().getVICMSST());

            imposto.setAliquotaCreditoIcms(icms.getICMSSN201().getPCredSN());
            imposto.setValorCreditoIcms(icms.getICMSSN201().getVCredICMSSN());

            imposto.setCst(definirCst(csts, imposto, crt));

        }
        if (icms.getICMSSN202() != null) {
            imposto.setCst(icms.getICMSSN202().getCSOSN());

            imposto.setOrigem(icms.getICMSSN202().getOrig());

            imposto.setModalidadeBaseCalculoST(icms.getICMSSN202().getModBCST());
            imposto.setMva(icms.getICMSSN202().getPMVAST());
            imposto.setReducaoBaseCalculoIcmsST(icms.getICMSSN202().getPRedBCST());
            imposto.setBaseCalculoIcmsST(icms.getICMSSN202().getVBCST());
            imposto.setAliquotaIcmsST(icms.getICMSSN202().getPICMSST());
            imposto.setValorIcmsST(icms.getICMSSN202().getVICMSST());

            imposto.setCst(definirCst(csts, imposto, crt));
        }
        if (icms.getICMSSN500() != null) {
            imposto.setCst(icms.getICMSSN500().getCSOSN());

            imposto.setOrigem(icms.getICMSSN500().getOrig());

            imposto.setCst(definirCst(csts, imposto, crt));

        }
        if (icms.getICMSSN900() != null) {
            imposto.setCst(icms.getICMSSN900().getCSOSN());

            imposto.setOrigem(icms.getICMSSN900().getOrig());

            imposto.setModalidadeBaseCalculo(icms.getICMSSN900().getModBC());
            imposto.setReducaoBaseCalculoIcms(icms.getICMSSN900().getPRedBC());
            imposto.setBaseCalculoIcms(icms.getICMSSN900().getVBC());
            imposto.setAliquotaIcms(icms.getICMSSN900().getPICMS());
            imposto.setValorIcms(icms.getICMSSN900().getVICMS());

            imposto.setModalidadeBaseCalculoST(icms.getICMSSN900().getModBCST());
            imposto.setMva(icms.getICMSSN900().getPMVAST());
            imposto.setReducaoBaseCalculoIcmsST(icms.getICMSSN900().getPRedBCST());
            imposto.setBaseCalculoIcmsST(icms.getICMSSN900().getVBCST());
            imposto.setAliquotaIcmsST(icms.getICMSSN900().getPICMSST());
            imposto.setValorIcmsST(icms.getICMSSN900().getVICMSST());

            imposto.setAliquotaCreditoIcms(icms.getICMSSN900().getPCredSN());
            imposto.setValorCreditoIcms(icms.getICMSSN900().getVCredICMSSN());

            imposto.setCst(definirCst(csts, imposto, crt));
        }
        detalheImpostoIcms = definirNfeDetalheIcms(imposto.getCst(), crt, imposto);
        return detalheImpostoIcms;
    }

    public NfeDetalheImpostoIcms definirNfeDetalheIcms(String cst, String crt, ImpostoDTO imposto) {
        NfeDetalheImpostoIcms icms = new NfeDetalheImpostoIcms();

        icms.setOrigemMercadoria(Integer.valueOf(imposto.getOrigem()));
        if (!crt.equals("1")) {
            icms.setCstIcms(cst);
        } else {
            icms.setCsosn(cst);
        }

        switch (cst) {
            case "00":
                icms.setModalidadeBcIcms(Integer.valueOf(imposto.getModalidadeBaseCalculo()));
                icms.setAliquotaIcms(imposto.getAliquotaIcms());
                icms.setBaseCalculoIcms(imposto.getBaseCalculoIcms());
                icms.setValorIcms(imposto.getValorIcms());
                break;

            case "10":
                icms.setModalidadeBcIcms(Integer.valueOf(imposto.getModalidadeBaseCalculo()));
                icms.setAliquotaIcms(imposto.getAliquotaIcms());
                icms.setBaseCalculoIcms(imposto.getBaseCalculoIcms());
                icms.setValorIcms(imposto.getValorIcms());

                icms.setModalidadeBcIcmsSt(Integer.valueOf(imposto.getModalidadeBaseCalculoST()));
                icms.setPercentualMvaIcmsSt(imposto.getMva());
                icms.setPercentualReducaoBcIcmsSt(imposto.getReducaoBaseCalculoIcmsST());
                icms.setBaseCalculoIcms(imposto.getBaseCalculoIcms());
                icms.setAliquotaIcmsSt(imposto.getAliquotaIcmsST());
                icms.setValorIcmsSt(imposto.getValorIcmsST());

                break;

            case "20":
                icms.setModalidadeBcIcms(Integer.valueOf(imposto.getModalidadeBaseCalculo()));

                icms.setTaxaReducaoBcIcms(imposto.getReducaoBaseCalculoIcms());

                icms.setAliquotaIcms(imposto.getAliquotaIcms());
                icms.setBaseCalculoIcms(imposto.getBaseCalculoIcms());
                icms.setValorIcms(imposto.getValorIcms());

                break;
            case "30":
                icms.setModalidadeBcIcmsSt(Integer.valueOf(imposto.getModalidadeBaseCalculoST()));

                icms.setPercentualMvaIcmsSt(imposto.getMva());
                icms.setPercentualReducaoBcIcmsSt(imposto.getReducaoBaseCalculoIcmsST());
                icms.setBaseCalculoIcms(imposto.getBaseCalculoIcms());
                icms.setAliquotaIcmsSt(imposto.getAliquotaIcmsST());
                icms.setValorIcmsSt(imposto.getValorIcmsST());

                icms.setValorIcmsDesonerado(BigDecimal.ZERO);

                break;
            case "40":
                icms.setValorIcmsDesonerado(BigDecimal.ZERO);

                break;
            case "41":
                icms.setValorIcmsDesonerado(BigDecimal.ZERO);
                break;

            case "51":
                icms.setModalidadeBcIcms(Integer.valueOf(imposto.getModalidadeBaseCalculo()));

                icms.setTaxaReducaoBcIcms(BigDecimal.ZERO);

                icms.setAliquotaIcms(BigDecimal.ZERO);
                icms.setBaseCalculoIcms(BigDecimal.ZERO);
                icms.setValorIcms(BigDecimal.ZERO);

                icms.setPercentualDiferimento(BigDecimal.ZERO);
                icms.setValorIcmsDiferido(BigDecimal.ZERO);

                break;

            case "60":
                icms.setModalidadeBcIcms(Integer.valueOf(imposto.getModalidadeBaseCalculo()));
                icms.setValorBcIcmsStRetido(BigDecimal.ZERO);
                icms.setValorIcmsStRetido(BigDecimal.ZERO);
                break;
            case "70":

                icms.setModalidadeBcIcms(Integer.valueOf(imposto.getModalidadeBaseCalculo()));
                icms.setTaxaReducaoBcIcms(imposto.getReducaoBaseCalculoIcms());
                icms.setAliquotaIcms(imposto.getAliquotaIcms());
                icms.setBaseCalculoIcms(imposto.getBaseCalculoIcms());
                icms.setValorIcms(imposto.getValorIcms());

                icms.setModalidadeBcIcmsSt(Integer.valueOf(imposto.getModalidadeBaseCalculoST()));

                icms.setPercentualMvaIcmsSt(imposto.getMva());
                icms.setPercentualReducaoBcIcmsSt(imposto.getReducaoBaseCalculoIcmsST());
                icms.setBaseCalculoIcms(imposto.getBaseCalculoIcms());
                icms.setAliquotaIcmsSt(imposto.getAliquotaIcmsST());
                icms.setValorIcmsSt(imposto.getValorIcmsST());

                break;
            case "90":

                icms.setModalidadeBcIcms(Integer.valueOf(imposto.getModalidadeBaseCalculo()));
                icms.setTaxaReducaoBcIcms(imposto.getReducaoBaseCalculoIcms());
                icms.setAliquotaIcms(imposto.getAliquotaIcms());
                icms.setBaseCalculoIcms(imposto.getBaseCalculoIcms());
                icms.setValorIcms(imposto.getValorIcms());

                icms.setModalidadeBcIcmsSt(Integer.valueOf(imposto.getModalidadeBaseCalculoST()));

                icms.setPercentualMvaIcmsSt(imposto.getMva());
                icms.setPercentualReducaoBcIcmsSt(imposto.getReducaoBaseCalculoIcmsST());
                icms.setBaseCalculoIcms(imposto.getBaseCalculoIcms());
                icms.setAliquotaIcmsSt(imposto.getAliquotaIcmsST());
                icms.setValorIcmsSt(imposto.getValorIcmsST());

                break;
            case "101":

                icms.setAliquotaCreditoIcmsSn(imposto.getAliquotaCreditoIcms());
                icms.setValorCreditoIcmsSn(imposto.getValorCreditoIcms());

                break;
            case "102":

                break;
            case "201":

                icms.setModalidadeBcIcmsSt(Integer.valueOf(imposto.getModalidadeBaseCalculoST()));

                icms.setPercentualMvaIcmsSt(imposto.getMva());
                icms.setPercentualReducaoBcIcmsSt(imposto.getReducaoBaseCalculoIcmsST());
                icms.setBaseCalculoIcms(imposto.getBaseCalculoIcms());
                icms.setAliquotaIcmsSt(imposto.getAliquotaIcmsST());
                icms.setValorIcmsSt(imposto.getValorIcmsST());

                icms.setAliquotaCreditoIcmsSn(imposto.getAliquotaCreditoIcms());
                icms.setValorCreditoIcmsSn(imposto.getValorCreditoIcms());

                break;
            case "202":

                icms.setModalidadeBcIcmsSt(Integer.valueOf(imposto.getModalidadeBaseCalculoST()));

                icms.setPercentualMvaIcmsSt(imposto.getMva());
                icms.setPercentualReducaoBcIcmsSt(imposto.getReducaoBaseCalculoIcmsST());
                icms.setBaseCalculoIcms(imposto.getBaseCalculoIcms());
                icms.setAliquotaIcmsSt(imposto.getAliquotaIcmsST());
                icms.setValorIcmsSt(imposto.getValorIcmsST());

                break;
            case "500":

                icms.setValorBcIcmsStRetido(BigDecimal.ZERO);
                icms.setValorIcmsStRetido(BigDecimal.ZERO);
                break;
            case "900":

                if (imposto.getModalidadeBaseCalculo() != null) {
                    icms.setModalidadeBcIcms(Integer.valueOf(imposto.getModalidadeBaseCalculo()));
                    icms.setTaxaReducaoBcIcms(imposto.getReducaoBaseCalculoIcms());
                    icms.setAliquotaIcms(imposto.getAliquotaIcms());
                    icms.setBaseCalculoIcms(imposto.getBaseCalculoIcms());
                    icms.setValorIcms(imposto.getValorIcms());
                }
                if (imposto.getModalidadeBaseCalculoST() != null) {
                    icms.setModalidadeBcIcmsSt(Integer.valueOf(imposto.getModalidadeBaseCalculoST()));

                    icms.setPercentualMvaIcmsSt(imposto.getMva());
                    icms.setPercentualReducaoBcIcmsSt(imposto.getReducaoBaseCalculoIcmsST());
                    icms.setBaseCalculoIcms(imposto.getBaseCalculoIcms());
                    icms.setAliquotaIcmsSt(imposto.getAliquotaIcmsST());
                    icms.setValorIcmsSt(imposto.getValorIcmsST());
                }

                icms.setAliquotaCreditoIcmsSn(imposto.getAliquotaCreditoIcms());
                icms.setValorCreditoIcmsSn(imposto.getValorCreditoIcms());

                break;

        }

        return icms;

    }

    public String definirCst(List<ConverterCst> csts, ImpostoDTO imposto, String crt) {
        String cst = imposto.getCst();
        ConverterCst converter = null;
        for (ConverterCst item : csts) {
            if (item.getCstOrigem().equals(imposto.getCst())) {
                converter = item;
                break;
            }
        }
        if (converter != null) {
            cst = converter.getCstDestino();
        } else {

            switch (cst) {
                case "00":
                    cst = crt.equals("1") ? "101" : "00";
                    break;
                case "10":
                    cst = crt.equals("1") ? "201" : "10";
                    break;
                case "20":
                    cst = crt.equals("1") ? "101" : "20";
                    break;
                case "30":
                    cst = crt.equals("1") ? "203" : "30";
                    break;
                case "41":
                    cst = crt.equals("1") ? "300" : "41";
                    break;
                case "40":
                    cst = crt.equals("1") ? "400" : "40";
                    break;
                case "50":
                    cst = crt.equals("1") ? "400" : "50";
                    break;
                case "51":
                    cst = crt.equals("1") ? "400" : "51";
                    break;
                case "60":
                    cst = crt.equals("1") ? "500" : "60";
                    break;
                case "70":
                    cst = crt.equals("1") ? "201" : "70";
                    break;
                case "101":
                    cst = crt.equals("1") ? "101" : "00";
                    break;
                case "201":
                    cst = crt.equals("1") ? "201" : "10";
                    break;
                case "203":
                    cst = crt.equals("1") ? "203" : "30";
                    break;
                case "300":
                    cst = crt.equals("1") ? "300" : "41";
                    break;
                case "400":
                    cst = crt.equals("1") ? "400" : "40";
                    break;
                case "500":
                    cst = crt.equals("1") ? "500" : "60";
                    break;

                default:
                    cst = crt.equals("1") ? "900" : "90";
                    break;
            }

        }

        return cst;
    }


}
