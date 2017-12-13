package com.chronos.controll.tributacao;

import com.chronos.calc.enuns.Crt;
import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.*;
import com.chronos.repository.Repository;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.Mensagem;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by john on 08/11/17.
 */
@Named
@ViewScoped
public class TributacaoControll extends AbstractControll<TributOperacaoFiscal> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<TributPisCodApuracao> pisRepository;
    @Inject
    private Repository<TributCofinsCodApuracao> cofinsRepository;
    @Inject
    private Repository<TributIss> issRepository;
    @Inject
    private Repository<TributIpiDipi> ipiRepository;
    @Inject
    private Repository<TributIcmsUf> icmsRepository;
    @Inject
    private Repository<TributGrupoTributario> grupoRepository;


    private TributPisCodApuracao pis;
    private TributCofinsCodApuracao cofins;
    private TributIpiDipi ipi;
    private TributIss iss;
    private TributIcmsUf tributIcmsUf;
    private TributIcmsUf tributIcmsUfSelecionado;
    private List<TributIcmsUf> listTributIcmsUf;
    private boolean controlaPisCofins;
    private boolean controlaIpi;
    private boolean controlaIss;
    private boolean controlaIcms;
    private boolean simplesNascional;


    @Override
    public ERPLazyDataModel<TributOperacaoFiscal> getDataModel() {
        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(TributOperacaoFiscal.class);
            dataModel.setDao(dao);
        }
        dataModel.setAtributos(new Object[]{"descricao", "cfop", "obrigacaoFiscal", "destacaIpi", "destacaPisCofins", "calculoIssqn"});
        return dataModel;
    }

    @Override
    public void doEdit() {
        super.doEdit();
        controlaIpi = getObjeto().getDestacaIpi();
        controlaPisCofins = getObjeto().getDestacaPisCofins();
        controlaIss = getObjeto().getCalculoIssqn();
        controlaIcms = getObjeto().getObrigacaoFiscal();
        if (empresa.getCrt() == null) {
            Mensagem.addInfoMessage("CRT para " + empresa.getRazaoSocial() + " não informado");
            setTelaGrid(true);
            return;
        }
        Crt crt = Crt.valueOfCodigo(Integer.valueOf(empresa.getCrt()));
        simplesNascional = crt == Crt.SimplesNaciona;
        if (getObjeto().getObrigacaoFiscal()) {
            listTributIcmsUf = Optional
                    .ofNullable(icmsRepository.getEntitys(TributIcmsUf.class, "tributOperacaoFiscal.id", getObjeto().getId(), null, new Object[]{"tributGrupoTributario"}))
                    .orElse(new ArrayList<>());

            if (crt == Crt.SimplesNaciona) {
                listTributIcmsUf.stream()
                        .forEach(icms -> {
                            if (icms.getCsosnB() == null) {
                                Mensagem.addInfoMessage("ICMS para " + icms.getUfDestino() + " sem CSOSN informado");
                            }

                        });
            } else {
                listTributIcmsUf.stream()
                        .forEach(icms -> {
                            if (icms.getCstB() == null) {
                                Mensagem.addInfoMessage("ICMS para " + icms.getUfDestino() + " sem CST informado");
                            }

                        });
            }
        } else {
            listTributIcmsUf = new ArrayList<>();
        }
        if (getObjeto().getCalculoIssqn()) {
            iss = Optional.ofNullable(issRepository.get(TributIss.class, "tributOperacaoFiscal.id", getObjeto().getId())).orElse(instanciarIssqn());

        } else {
            instanciarIssqn();
        }
        if (getObjeto().getDestacaIpi()) {
            ipi = Optional.ofNullable(ipiRepository.get(TributIpiDipi.class, "tributOperacaoFiscal.id", getObjeto().getId())).orElse(instanciarIpi());

        } else {
            instanciarIpi();
        }
        if (getObjeto().getDestacaPisCofins()) {
            pis = Optional.ofNullable(pisRepository.get(TributPisCodApuracao.class, "tributOperacaoFiscal.id", getObjeto().getId())).orElse(instanciarPis());
            cofins = Optional.ofNullable(cofinsRepository.get(TributCofinsCodApuracao.class, "tributOperacaoFiscal.id", getObjeto().getId())).orElse(instanciarCofins());

        } else {
            instanciarPis();
            instanciarCofins();
        }


    }

    @Transactional
    @Override
    public void salvar() {
        try {
            validarTributacao();

            if (getObjeto().getCalculoIssqn()) {
                iss = issRepository.atualizar(iss);
            }
            if (getObjeto().getDestacaPisCofins()) {
                pis = pisRepository.atualizar(pis);
                cofins = cofinsRepository.atualizar(cofins);
            }
            if (getObjeto().getDestacaIpi()) {
                ipi = ipiRepository.atualizar(ipi);
            }
            listTributIcmsUf.stream().forEach(icms -> {
                icms = icmsRepository.atualizar(icms);
            });
            Mensagem.addInfoMessage("Tributação salva com sucesso");
            setTelaGrid(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }

    }

    public TributCofinsCodApuracao instanciarCofins() {


        cofins = new TributCofinsCodApuracao();
        cofins.setTributOperacaoFiscal(getObjeto());
        cofins.setAliquotaPorcento(BigDecimal.ZERO);
        cofins.setAliquotaUnidade(BigDecimal.ZERO);
        cofins.setPorcentoBaseCalculo(BigDecimal.ZERO);
        cofins.setValorPautaFiscal(BigDecimal.ZERO);
        cofins.setValorPautaFiscal(BigDecimal.ZERO);
        cofins.setValorPrecoMaximo(BigDecimal.ZERO);

        return cofins;
    }

    private TributPisCodApuracao instanciarPis() {
        pis = new TributPisCodApuracao();
        pis.setTributOperacaoFiscal(getObjeto());
        pis.setAliquotaPorcento(BigDecimal.ZERO);
        pis.setAliquotaUnidade(BigDecimal.ZERO);
        pis.setPorcentoBaseCalculo(BigDecimal.ZERO);
        pis.setValorPautaFiscal(BigDecimal.ZERO);
        pis.setValorPrecoMaximo(BigDecimal.ZERO);

        return pis;
    }

    public TributIpiDipi instanciarIpi() {

        ipi = new TributIpiDipi();
        ipi.setTributOperacaoFiscal(getObjeto());
        ipi.setAliquotaPorcento(BigDecimal.ZERO);
        ipi.setAliquotaUnidade(BigDecimal.ZERO);
        ipi.setPorcentoBaseCalculo(BigDecimal.ZERO);
        ipi.setValorPautaFiscal(BigDecimal.ZERO);
        ipi.setValorPrecoMaximo(BigDecimal.ZERO);

        return ipi;
    }

    public TributIss instanciarIssqn() {

        iss = new TributIss();
        iss.setTributOperacaoFiscal(getObjeto());
        iss.setAliquotaPorcento(BigDecimal.ZERO);
        iss.setAliquotaUnidade(BigDecimal.ZERO);
        iss.setItemListaServico(0);
        iss.setModalidadeBaseCalculo('0');
        iss.setPorcentoBaseCalculo(BigDecimal.ZERO);
        iss.setValorPautaFiscal(BigDecimal.ZERO);
        iss.setValorPrecoMaximo(BigDecimal.ZERO);

        return iss;
    }

    public void incluirTributIcmsUf() {
        tributIcmsUf = new TributIcmsUf();
        tributIcmsUf.setTributOperacaoFiscal(getObjeto());
        tributIcmsUf.setAliquota(BigDecimal.ZERO);
        tributIcmsUf.setAliquotaIcmsSt(BigDecimal.ZERO);
        tributIcmsUf.setAliquotaInterestadualSt(BigDecimal.ZERO);
        tributIcmsUf.setAliquotaInternaSt(BigDecimal.ZERO);
        tributIcmsUf.setMva(BigDecimal.ZERO);
        tributIcmsUf.setPorcentoBc(BigDecimal.ZERO);
        tributIcmsUf.setPorcentoBcSt(BigDecimal.ZERO);
        tributIcmsUf.setValorPauta(BigDecimal.ZERO);
        tributIcmsUf.setValorPautaSt(BigDecimal.ZERO);
        tributIcmsUf.setValorPrecoMaximo(BigDecimal.ZERO);
        tributIcmsUf.setValorPrecoMaximoSt(BigDecimal.ZERO);
        tributIcmsUf.setModalidadeBc("3");
        tributIcmsUf.setModalidadeBcSt("4");
        setActiveTabIndex(1);
    }

    public void alterarTributIcmsUf() {
        tributIcmsUf = icmsRepository.get(tributIcmsUfSelecionado.getId(), TributIcmsUf.class);
    }

    public void salvarTributIcmsUf() {
        try {
            validarTributacaoICMS();
            if (tributIcmsUf.getId() == null) {
                listTributIcmsUf.add(tributIcmsUf);
            }
            //  tributIcmsUf = icmsRepository.atualizar(tributIcmsUf);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().validationFailed();

            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }

    }


    public void excluirTributIcmsUf() {
        if (tributIcmsUfSelecionado.getId() == null) {
            listTributIcmsUf.remove(tributIcmsUfSelecionado);
        } else {
            listTributIcmsUf.remove(tributIcmsUfSelecionado);
            icmsRepository.excluir(tributIcmsUfSelecionado, tributIcmsUfSelecionado.getId());
        }
        Mensagem.addInfoMessage("Registro excluído com sucesso!");
    }

    public List<TributGrupoTributario> getListaGrupoTributario(String nome) {
        List<TributGrupoTributario> listaTributGrupoTributario = new ArrayList<>();
        try {
            listaTributGrupoTributario = grupoRepository.getEntitys(TributGrupoTributario.class, "descricao", nome, new Object[]{"descricao"});
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTributGrupoTributario;
    }


    private void validarTributacao() throws Exception {
        if (getObjeto().getObrigacaoFiscal() && listTributIcmsUf.isEmpty()) {
            throw new Exception("É preciso definir uma tributação de ICMS");
        }
        if (getObjeto().getDestacaIpi()
                && (ipi.getAliquotaPorcento() == null || ipi.getAliquotaPorcento().signum() <= 0)
                && (ipi.getCstIpi().equals("00")
                || ipi.getCstIpi().equals("49")
                || ipi.getCstIpi().equals("50")
                || ipi.getCstIpi().equals("99"))) {
            throw new Exception("Aliquota de IPI Invalida");
        }

        if (getObjeto().getDestacaPisCofins()
                && (pis.getAliquotaPorcento() == null || pis.getAliquotaPorcento().signum() <= 0)
                && (pis.getCstPis().equals("01")
                || pis.getCstPis().equals("02"))) {
            throw new Exception("Aliquota de PIS Invalida");
        }

        if (getObjeto().getDestacaPisCofins()
                && (cofins.getAliquotaPorcento() == null || cofins.getAliquotaPorcento().signum() <= 0)
                && (cofins.getCstCofins().equals("01")
                || cofins.getCstCofins().equals("02"))) {
            throw new Exception("Aliquota de COFINS Invalida");
        }


    }

    private void validarTributacaoICMS() throws Exception {
        if (listTributIcmsUf.stream()
                .filter(icms -> icms.getTributGrupoTributario().getId() == tributIcmsUf.getTributGrupoTributario().getId() && icms.getUfDestino().equals(tributIcmsUf.getUfDestino()))
                .findAny().isPresent()) {
            throw new Exception("Grupo tributário já definido para essa UF ");
        }

        if (tributIcmsUf.getCsosnB() != null) {
            switch (tributIcmsUf.getCsosnB()) {
                case "101":
                    if (tributIcmsUf.getCreditoIcms() == null || tributIcmsUf.getCreditoIcms().signum() <= 0) {
                        throw new Exception("Aliquota de credito de ICMS Invalida");
                    }
                    break;
                case "201":
                    if (tributIcmsUf.getAliquotaIcmsSt() == null || tributIcmsUf.getAliquotaIcmsSt().signum() <= 0) {
                        throw new Exception("Aliquota de ICMS ST Invalida");
                    }
                    if (tributIcmsUf.getMva() == null) {
                        throw new Exception("Aliquota de MVA Invalida");
                    }
                    if (tributIcmsUf.getPorcentoBcSt() == null || tributIcmsUf.getPorcentoBcSt().signum() <= 0) {
                        throw new Exception("Percentual de redução da base de Calculo de ICMS ST invalida");
                    }
                    if (tributIcmsUf.getCreditoIcms() == null || tributIcmsUf.getCreditoIcms().signum() <= 0) {
                        throw new Exception("Aliquota de credito de ICMS Invalida");
                    }
                    break;
                case "202":
                    if (tributIcmsUf.getAliquotaIcmsSt() == null || tributIcmsUf.getAliquotaIcmsSt().signum() <= 0) {
                        throw new Exception("Aliquota de ICMS ST Invalida");
                    }
                    if (tributIcmsUf.getMva() == null) {
                        throw new Exception("Aliquota de MVA Invalida");
                    }
                    if (tributIcmsUf.getPorcentoBcSt() == null || tributIcmsUf.getPorcentoBcSt().signum() <= 0) {
                        throw new Exception("Percentual de redução da base de Calculo de ICMS ST invalida");
                    }

                case "900":
                    validarCsosn900Cst90();
                    break;
            }
        } else {
            switch (tributIcmsUf.getCstB()) {
                case "00":
                    if (tributIcmsUf.getAliquota() == null || tributIcmsUf.getAliquota().signum() <= 0) {
                        throw new Exception("Aliquota de ICMS Invalida");
                    }
                    break;
                case "10":
                    if (tributIcmsUf.getAliquota() == null || tributIcmsUf.getAliquota().signum() <= 0) {
                        throw new Exception("Aliquota de ICMS Invalida");
                    }
                    if (tributIcmsUf.getMva() == null) {
                        throw new Exception("Aliquota de MVA Invalida");
                    }
                    if (tributIcmsUf.getPorcentoBcSt() == null || tributIcmsUf.getPorcentoBcSt().signum() <= 0) {
                        throw new Exception("Percentual de redução da base de Calculo de ICMS ST invalida");
                    }
                    if (tributIcmsUf.getAliquotaIcmsSt() == null || tributIcmsUf.getAliquotaIcmsSt().signum() <= 0) {
                        throw new Exception("Aliquota de ICMS ST Invalida");
                    }

                    break;
                case "20":

                    if (tributIcmsUf.getAliquota() == null || tributIcmsUf.getAliquota().signum() <= 0) {
                        throw new Exception("Aliquota de ICMS Invalida");
                    }
                    if (tributIcmsUf.getPorcentoBc() == null || tributIcmsUf.getPorcentoBc().signum() <= 0) {
                        throw new Exception("Percentual de redução da base de Calculo de ICMS  invalida");
                    }
                    break;
                case "30":
                    if (tributIcmsUf.getAliquotaIcmsSt() == null || tributIcmsUf.getAliquotaIcmsSt().signum() <= 0) {
                        throw new Exception("Aliquota de ICMS ST Invalida");
                    }
                    if (tributIcmsUf.getMva() == null) {
                        throw new Exception("Aliquota de MVA Invalida");
                    }
                    if (tributIcmsUf.getPorcentoBcSt() == null || tributIcmsUf.getPorcentoBcSt().signum() <= 0) {
                        throw new Exception("Percentual de redução da base de Calculo de ICMS ST invalida");
                    }
                    break;

                case "70":
                    if (tributIcmsUf.getAliquota() == null || tributIcmsUf.getAliquota().signum() <= 0) {
                        throw new Exception("Aliquota de ICMS Invalida");
                    }
                    if (tributIcmsUf.getPorcentoBc() == null || tributIcmsUf.getPorcentoBc().signum() <= 0) {
                        throw new Exception("Percentual de redução da base de Calculo de ICMS  invalida");
                    }
                    if (tributIcmsUf.getMva() == null) {
                        throw new Exception("Aliquota de MVA Invalida");
                    }
                    if (tributIcmsUf.getAliquotaIcmsSt() == null || tributIcmsUf.getAliquotaIcmsSt().signum() <= 0) {
                        throw new Exception("Aliquota de ICMS ST Invalida");
                    }
                    if (tributIcmsUf.getPorcentoBcSt() == null || tributIcmsUf.getPorcentoBcSt().signum() <= 0) {
                        throw new Exception("Percentual de redução da base de Calculo de ICMS ST invalida");
                    }
                    break;

                case "90":
                    validarCsosn900Cst90();

                    break;
            }
        }
    }

    private void validarCsosn900Cst90() throws Exception {
        if (tributIcmsUf.getAliquota() != null && tributIcmsUf.getAliquotaIcmsSt() != null) {
            if (tributIcmsUf.getAliquota() == null || tributIcmsUf.getAliquota().signum() <= 0) {
                throw new Exception("Aliquota de ICMS Invalida");
            }
            if (tributIcmsUf.getPorcentoBc() == null || tributIcmsUf.getPorcentoBc().signum() <= 0) {
                throw new Exception("Percentual de redução da base de Calculo de ICMS  invalida");
            }
            if (tributIcmsUf.getAliquotaIcmsSt() == null || tributIcmsUf.getAliquotaIcmsSt().signum() <= 0) {
                throw new Exception("Aliquota de ICMS ST Invalida");
            }
            if (tributIcmsUf.getMva() == null) {
                throw new Exception("Aliquota de MVA Invalida");
            }
            if (tributIcmsUf.getPorcentoBcSt() == null || tributIcmsUf.getPorcentoBcSt().signum() <= 0) {
                throw new Exception("Percentual de redução da base de Calculo de ICMS ST invalida");
            }
        } else if (tributIcmsUf.getAliquota() == null || tributIcmsUf.getAliquota().signum() <= 0) {
            if (tributIcmsUf.getAliquota() == null || tributIcmsUf.getAliquota().signum() <= 0) {
                throw new Exception("Aliquota de ICMS Invalida");
            }
            if (tributIcmsUf.getPorcentoBc() == null || tributIcmsUf.getPorcentoBc().signum() <= 0) {
                throw new Exception("Percentual de redução da base de Calculo de ICMS  invalida");
            }
        }
    }

    @Override
    protected Class<TributOperacaoFiscal> getClazz() {
        return TributOperacaoFiscal.class;
    }


    @Override
    protected String getFuncaoBase() {
        return "TRIBUTACAO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public TributCofinsCodApuracao getCofins() {
        return cofins;
    }

    public void setCofins(TributCofinsCodApuracao cofins) {
        this.cofins = cofins;
    }

    public TributPisCodApuracao getPis() {
        return pis;
    }

    public void setPis(TributPisCodApuracao pis) {
        this.pis = pis;
    }

    public TributIpiDipi getIpi() {
        return ipi;
    }

    public void setIpi(TributIpiDipi ipi) {
        this.ipi = ipi;
    }

    public TributIss getIss() {
        return iss;
    }

    public void setIss(TributIss iss) {
        this.iss = iss;
    }


    public boolean isControlaPisCofins() {
        return controlaPisCofins;
    }

    public void setControlaPisCofins(boolean controlaPisCofins) {
        this.controlaPisCofins = controlaPisCofins;
    }

    public boolean isControlaIpi() {
        return controlaIpi;
    }

    public void setControlaIpi(boolean controlaIpi) {
        this.controlaIpi = controlaIpi;
    }

    public boolean isControlaIss() {
        return controlaIss;
    }

    public void setControlaIss(boolean controlaIss) {
        this.controlaIss = controlaIss;
    }

    public TributIcmsUf getTributIcmsUf() {
        return tributIcmsUf;
    }

    public void setTributIcmsUf(TributIcmsUf tributIcmsUf) {
        this.tributIcmsUf = tributIcmsUf;
    }

    public TributIcmsUf getTributIcmsUfSelecionado() {
        return tributIcmsUfSelecionado;
    }

    public void setTributIcmsUfSelecionado(TributIcmsUf tributIcmsUfSelecionado) {
        this.tributIcmsUfSelecionado = tributIcmsUfSelecionado;
    }

    public boolean isControlaIcms() {
        return controlaIcms;
    }

    public void setControlaIcms(boolean controlaIcms) {
        this.controlaIcms = controlaIcms;
    }

    public List<TributIcmsUf> getListTributIcmsUf() {
        return listTributIcmsUf;
    }

    public void setListTributIcmsUf(List<TributIcmsUf> listTributIcmsUf) {
        this.listTributIcmsUf = listTributIcmsUf;
    }

    public boolean isSimplesNascional() {
        return simplesNascional;
    }

    public void setSimplesNascional(boolean simplesNascional) {
        this.simplesNascional = simplesNascional;
    }
}
