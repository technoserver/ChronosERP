package com.chronos.controll.fiscal;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.FiscalApuracaoIcms;
import com.chronos.modelo.view.ViewSpedC190Id;
import com.chronos.modelo.view.ViewSpedC390Id;
import com.chronos.modelo.view.ViewSpedC490Id;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.Biblioteca;
import com.chronos.util.jsf.Mensagem;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by john on 09/10/17.
 */
@Named
@ViewScoped
public class FiscalApuracaoIcmsControll extends AbstractControll<FiscalApuracaoIcms> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<ViewSpedC190Id> viewSpedC190IdRepository;
    @Inject
    private Repository<ViewSpedC390Id> viewSpedC390IdRepository;
    @Inject
    private Repository<ViewSpedC490Id> viewSpedC490IdRepository;

    private String periodo;

    @Override
    protected Class<FiscalApuracaoIcms> getClazz() {
        return FiscalApuracaoIcms.class;
    }

    @PostConstruct
    @Override
    public void init() {
        setObjeto(new FiscalApuracaoIcms());
        super.init();
    }


    public void carregaDados() {
        try {

            String periodoAnterior = "";
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, Integer.valueOf(periodo.substring(0, 2)));
            calendar.set(Calendar.YEAR, Integer.valueOf(periodo.substring(3, 7)));

            Date dataInicio = Biblioteca.getDataInicial(calendar.getTime());
            Date dataFim;
            BigDecimal valorTotalDebitos = BigDecimal.ZERO;
            BigDecimal valorTotalCreditos = BigDecimal.ZERO;
            BigDecimal valorSaldoApurado;
            try {
                periodoAnterior = Biblioteca.periodoAnterior(periodo);

                dataFim = Biblioteca.ultimoDiaMes(dataInicio);
            } catch (Exception e) {
                throw new Exception("Período inválido.");
            }

            FiscalApuracaoIcms apuracaoIcmsAnterior = dao.get(FiscalApuracaoIcms.class, "competencia", periodoAnterior);

            setObjeto(dao.get(FiscalApuracaoIcms.class, "competencia", periodo));

            // Se não existe registro para o período atual, cria objeto
            if (getObjeto() == null) {
                setObjeto(new FiscalApuracaoIcms());
                getObjeto().setEmpresa(empresa);
                getObjeto().setCompetencia(periodo);
            }

            // Se existir o período anterior, pega o saldo credor
            if (apuracaoIcmsAnterior != null) {
                getObjeto().setValorSaldoCredorAnterior(apuracaoIcmsAnterior.getValorSaldoCredorAnterior());
            }

            /*
            REGISTRO E110 - Campo 02 - VL_TOT_DEBITOS'

            Campo 02 - Validação: o valor informado deve corresponder ao somatório de todos os documentos fiscais de saída que
            geram débito de ICMS. Deste somatório, estão excluídos os documentos extemporâneos (COD_SIT com valor igual 01),
            os documentos complementares extemporâneos (COD_SIT com valor igual 07) e os documentos fiscais com CFOP 5605.
            Devem ser incluídos os documentos fiscais com CFOP igual a 1605.
            O valor neste campo deve ser igual à soma dos VL_ICMS de todos os registros C190, C320, C390, C490, C590, C690,
            C790, D190, D300, D390, D410, D590, D690, D696, com as datas dos campos DT_DOC (C300, C405, C600, D300,
            D355, D400, D600) ou DT_E_S (C100, C500) ou DT_DOC_FIN (C700, D695) ou DT_A_P (D100, D500) dentro do
            período informado no registro E100.
             */

            // REGISTRO C190: REGISTRO ANALÍTICO DO DOCUMENTO (CÓDIGO 01, 1B, 04 ,55 e 65).
            List<Filtro> filtros = new ArrayList<>();

            filtros.add(new Filtro(Filtro.AND, "viewSpedC190.dataEmissao", Filtro.BETWEEN, new Object[]{dataInicio, dataFim}));


            String sql = "SELECT o FROM com.chronos.modelo.view.ViewSpedC190Id o  WHERE 1 = 1 AND o.viewSpedC190.dataEmissao  BETWEEN ?1 AND  :?2";

            List<ViewSpedC190Id> listaNfeAnalitico = viewSpedC190IdRepository.getEntitys(ViewSpedC190Id.class, filtros);

            for (ViewSpedC190Id item : listaNfeAnalitico) {


                valorTotalDebitos = valorTotalDebitos.add(item.getViewSpedC190().getSomaValorIcms());

            }


            // REGISTRO C390: REGISTRO ANALÍTICO DAS NOTAS FISCAIS DE VENDA A CONSUMIDOR (CÓDIGO 02)
            filtros = new ArrayList<>();
            filtros.add(new Filtro(Filtro.AND, "viewC390.dataEmissao", Filtro.MAIOR_OU_IGUAL, dataInicio));
            filtros.add(new Filtro(Filtro.AND, "viewC390.dataEmissao", Filtro.MENOR_OU_IGUAL, dataFim));
            List<ViewSpedC390Id> listaC390 = viewSpedC390IdRepository.getEntitys(ViewSpedC390Id.class, filtros);
            for (int i = 0; i < listaC390.size(); i++) {
                valorTotalDebitos = valorTotalDebitos.add(listaC390.get(i).getViewC390().getSomaIcms());
            }

            // REGISTRO C490: REGISTRO ANALÍTICO DO MOVIMENTO DIÁRIO (CÓDIGO 02, 2D e 60).
            filtros = new ArrayList<>();
            filtros.add(new Filtro(Filtro.AND, "viewC490.dataVenda", Filtro.MAIOR_OU_IGUAL, dataInicio));
            filtros.add(new Filtro(Filtro.AND, "viewC490.dataVenda", Filtro.MENOR_OU_IGUAL, dataFim));
            List<ViewSpedC490Id> listaC490 = viewSpedC490IdRepository.getEntitys(ViewSpedC490Id.class, filtros);
            for (int i = 0; i < listaC490.size(); i++) {
                valorTotalDebitos = valorTotalDebitos.add(listaC490.get(i).getViewC490().getSomaIcms());
            }

            getObjeto().setValorTotalDebito(valorTotalDebitos);

            /*
            Campo 06 - Validação: o valor informado deve corresponder ao somatório de todos os documentos fiscais de entrada que
            geram crédito de ICMS. O valor neste campo deve ser igual à soma dos VL_ICMS de todos os registros C190, C590, D190
            e D590. Deste somatório, estão excluídos os documentos fiscais com CFOP 1605 e incluídos os documentos fiscais com
            CFOP 5605. Os documentos fiscais devem ser somados conforme o período informado no registro E100 e a data informada
            no campo DT_E_S (C100, C500) ou campo DT_A_P (D100, D500), exceto se COD_SIT do documento for igual a 01
            (extemporâneo) ou igual a 07 (NF Complementar extemporânea), cujo valor será somado no primeiro período de apuração
            informado no registro E100.
             */

            // REGISTRO C190: REGISTRO ANALÍTICO DO DOCUMENTO (CÓDIGO 01, 1B, 04 ,55 e 65).
            for (int i = 0; i < listaNfeAnalitico.size(); i++) {
                valorTotalCreditos = valorTotalCreditos.add(listaNfeAnalitico.get(i).getViewSpedC190().getSomaValorIcms());
            }

            // REGISTRO C390: REGISTRO ANALÍTICO DAS NOTAS FISCAIS DE VENDA A CONSUMIDOR (CÓDIGO 02)
            for (int i = 0; i < listaC390.size(); i++) {
                valorTotalCreditos = valorTotalCreditos.add(listaC390.get(i).getViewC390().getSomaIcms());
            }

            // REGISTRO C490: REGISTRO ANALÍTICO DO MOVIMENTO DIÁRIO (CÓDIGO 02, 2D e 60).
            for (int i = 0; i < listaC490.size(); i++) {
                valorTotalCreditos = valorTotalCreditos.add(listaC490.get(i).getViewC490().getSomaIcms());
            }

            getObjeto().setValorTotalCredito(valorTotalCreditos);

            /*
            Campo 11 - Validação: o valor informado deve ser preenchido com base na expressão: soma do total de débitos
            (VL_TOT_DEBITOS) com total de ajustes (VL_AJ_DEBITOS +VL_TOT_AJ_DEBITOS) com total de estorno de crédito
            (VL_ESTORNOS_CRED) menos a soma do total de créditos (VL_TOT_CREDITOS) com total de ajuste de créditos
            (VL_,AJ_CREDITOS + VL_TOT_AJ_CREDITOS) com total de estorno de débito (VL_ESTORNOS_DEB) com saldo
            credor do período anterior (VL_SLD_CREDOR_ANT). Se o valor da expressão for maior ou igual a 0 (zero), então este
            valor deve ser informado neste campo e o campo 14 (VL_SLD_CREDOR_TRANSPORTAR) deve ser igual a 0 (zero).
            Se o valor da expressão for menor que 0 (zero), então este campo deve ser preenchido com 0 (zero) e o valor absoluto
            da expressão deve ser informado no campo VL_SLD_CREDOR_TRANSPORTAR.
             */

            valorSaldoApurado = valorTotalDebitos.subtract(valorTotalCreditos);
            if (getObjeto().getValorSaldoCredorAnterior() != null) {
                valorSaldoApurado = valorSaldoApurado.add(getObjeto().getValorSaldoCredorAnterior());
            }

            if (valorSaldoApurado.compareTo(BigDecimal.ZERO) >= 0) {
                getObjeto().setValorSaldoApurado(valorSaldoApurado);
                getObjeto().setValorSaldoCredorTransp(BigDecimal.ZERO);
            } else {
                getObjeto().setValorSaldoApurado(BigDecimal.ZERO);
                getObjeto().setValorSaldoCredorTransp(valorSaldoApurado.negate());
            }

            /*
            Campo 13  Validação: o valor informado deve corresponder à diferença entre o campo VL_SLD_APURADO e o campo
            VL_TOT_DED. Se o resultado dessa operação for negativo, informe o valor zero neste campo, e o valor absoluto
            correspondente no campo VL_SLD_CREDOR_TRANSPORTAR. Verificar se a legislação da UF permite que dedução
            seja maior que o saldo devedor.
             */

            if (valorSaldoApurado.compareTo(BigDecimal.ZERO) >= 0) {
                getObjeto().setValorIcmsRecolher(valorSaldoApurado);
            } else {
                getObjeto().setValorIcmsRecolher(BigDecimal.ZERO);
            }

            salvar("Apuração realizada.");
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Erro ao carregar os dados!", e);

        }
    }

    @Override
    protected String getFuncaoBase() {
        return "APURACAO_ICMS";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }
}
