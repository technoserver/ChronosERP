package com.chronos.erp.controll.nfe;

import com.chronos.calc.CalcTributacao;
import com.chronos.calc.TributaNFe;
import com.chronos.calc.dto.ITributavel;
import com.chronos.calc.dto.Imposto;
import com.chronos.calc.dto.TributosProduto;
import com.chronos.calc.enuns.Crt;
import com.chronos.calc.enuns.TipoOperacao;
import com.chronos.calc.enuns.TipoPessoa;
import com.chronos.calc.resultados.IResultadoCalculoIbpt;
import com.chronos.erp.modelo.entidades.*;

import java.math.BigDecimal;

/**
 * Created by john on 29/09/17.
 */
public class NfeCalculoControll {

    private TributaNFe tributacao;
    private Empresa empresa;
    private NfeDestinatario destinatario;
    private TributosProduto produto;

    public NfeCalculoControll() {

    }

    public NfeCalculoControll(TributosProduto produto) {
        this.produto = produto;
    }

    public NfeDetalhe calcularTributacao(NfeDetalhe item, Empresa empresa, NfeDestinatario destinatario) throws Exception {
        Imposto imposto;
        this.destinatario = destinatario;
        this.empresa = empresa;

        tributacao = new TributaNFe(produto);
        if (empresa.getCrt() == null) {
            throw new Exception("CRT da empresa não definido");
        }
        Crt crt = Crt.valueOfCodigo(Integer.valueOf(empresa.getCrt()));
        TipoOperacao tipoOperacao = isOperacaoInterestadual();
        TipoPessoa tipoPessoa = destinatario == null || destinatario.getCpfCnpj() == null || destinatario.getCpfCnpj().length() == 11 ? TipoPessoa.Fisica : TipoPessoa.Juridica;
        imposto = tributacao.tributarNfe(produto, crt, tipoOperacao, tipoPessoa);

        if (produto.isServico()) {
            item.getNfeDetalheImpostoIssqn().setBaseCalculoIssqn(imposto.getIssqn().getBaseCalculo());
            item.getNfeDetalheImpostoIssqn().setAliquotaIssqn(imposto.getIssqn().getPercentualIss());
            item.getNfeDetalheImpostoIssqn().setValorIssqn(imposto.getIssqn().getValor());
            item.getNfeDetalheImpostoIssqn().setValorRetencaoIss(BigDecimal.ZERO);
        } else {
            // Valores ICMS
            item.getNfeDetalheImpostoIcms().setAliquotaIcms(imposto.getIcms().getPercentualIcms());
            item.getNfeDetalheImpostoIcms().setBaseCalculoIcms(imposto.getIcms().getValorBcIcms());
            item.getNfeDetalheImpostoIcms().setValorIcms(imposto.getIcms().getValorIcms());
            item.getNfeDetalheImpostoIcms().setTaxaReducaoBcIcms(imposto.getIcms().getPercentualReducao());

            //TODO  verificar como sera calculado o ICMS desonerado
            // valor icmsDesonerado
            item.getNfeDetalheImpostoIcms().setValorIcmsDesonerado(imposto.getIcms().getValorIcms());
            // valores de icms st
            item.getNfeDetalheImpostoIcms().setValorBaseCalculoIcmsSt(imposto.getIcms().getValorBaseCalcST());
            item.getNfeDetalheImpostoIcms().setValorIcmsSt(imposto.getIcms().getValorIcmsST());
            item.getNfeDetalheImpostoIcms().setPercentualReducaoBcIcmsSt(imposto.getIcms().getPercentualReducaoST());

            // credito de icmssn
            item.getNfeDetalheImpostoIcms().setValorCreditoIcmsSn(imposto.getIcms().getValorCredito());

            if (item.getNfeDetalheImpostoIpi() != null && imposto.getIpi() != null) {
                item.getNfeDetalheImpostoIpi().setValorBaseCalculoIpi(imposto.getIpi().getValorBcIpi());
                item.getNfeDetalheImpostoIpi().setValorIpi(imposto.getIpi().getValorIpi());
            }

        }

        // Valores PIS
        if (item.getNfeDetalheImpostoPis() != null) {

            item.getNfeDetalheImpostoPis().setValorBaseCalculoPis(imposto.getPis().getBaseCalculo());
            item.getNfeDetalheImpostoPis().setValorPis(imposto.getPis().getValor());
        }
        // Valores COFINS
        if (item.getNfeDetalheImpostoCofins() != null) {

            item.getNfeDetalheImpostoCofins().setBaseCalculoCofins(imposto.getCofins().getBaseCalculo());
            item.getNfeDetalheImpostoCofins().setValorCofins(imposto.getCofins().getValor());
        }

        item.setValorTotalTributos(imposto.getValorTotalTributos());
        item.setImpostoEstadual(imposto.getTributacaoEstadual());
        item.setImpostoFederal(imposto.getTributacaoFederal());
        item.setImpostoMunicipal(imposto.getTributacaoMunicipal());
        return item;
    }

    public NfeDetalhe calculoIbpt(NfeDetalhe item, Ibpt ibpt) {
        ITributavel tributos = new ITributavel();

        //Valores produto
        tributos.setValorProduto(item.getValorUnitarioComercial());
        tributos.setDesconto(item.getValorDesconto());
        tributos.setFrete(item.getValorFrete());
        tributos.setOutrasDespesas(item.getValorOutrasDespesas());
        tributos.setQuantidadeProduto(item.getQuantidadeComercial());
        tributos.setSeguro(item.getValorSeguro());

        tributos.setPercentualFederal(ibpt.getNacionalFederal());
        tributos.setPercentualFederalImportados(ibpt.getImportadosFederal());
        tributos.setPercentualEstadual(ibpt.getEstadual());
        tributos.setPercentualMunicipal(ibpt.getMunicipal());

        CalcTributacao calcular = new CalcTributacao(tributos);

        IResultadoCalculoIbpt result = calcular.calculaIbpt(tributos);

        item.setValorTotalTributos(result.getValorTotalTributos());
        item.setImpostoEstadual(result.getTributacaoEstadual());
        item.setImpostoFederal(result.getTributacaoFederal());
        item.setImpostoMunicipal(result.getTributacaoMunicipal());

        return item;
    }

    private TipoOperacao isOperacaoInterestadual() {
        TipoOperacao tipoOperacao = TipoOperacao.OperacaoInterestadual;
        for (EmpresaEndereco end : empresa.getListaEndereco()) {
            if (destinatario == null || destinatario.getUf() == null) {
                tipoOperacao = TipoOperacao.OperacaoInterna;
            } else if (end.getPrincipal().equals("S") && !end.getUf().equals(destinatario.getUf())) {
                tipoOperacao = TipoOperacao.OperacaoInterna;
            }
        }
        return tipoOperacao;
    }
}
