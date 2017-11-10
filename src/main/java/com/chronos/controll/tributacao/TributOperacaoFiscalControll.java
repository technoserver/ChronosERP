package com.chronos.controll.tributacao;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.*;
import com.chronos.repository.Repository;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by john on 16/08/17.
 */
@Named
@ViewScoped
public class TributOperacaoFiscalControll extends AbstractControll<TributOperacaoFiscal> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<Cfop> cfops;
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

    private Cfop cfop;

    private Map<String, Boolean> controla;

    @PostConstruct
    @Override
    public void init() {
        super.init();

        controla = new LinkedHashMap();
        controla.put("Não", false);
        controla.put("Sim", true);
    }

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setEmpresa(empresa);
    }

    @Transactional
    @Override
    public void salvar() {

        try {
            if (getObjeto().getId() != null) {
                verificarTributacao();
            }

            super.salvar();

        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }
    }

    private void verificarTributacao() {
        if (!getObjeto().getDestacaIpi()) {
            ipiRepository.excluir(TributIpiDipi.class, "tributOperacaoFiscal.id", getObjeto().getId());

        }
        if (!getObjeto().getDestacaPisCofins()) {
            pisRepository.excluir(TributPisCodApuracao.class, "tributOperacaoFiscal.id", getObjeto().getId());

        }
        if (!getObjeto().getDestacaPisCofins()) {
            cofinsRepository.excluir(TributCofinsCodApuracao.class, "tributOperacaoFiscal.id", getObjeto().getId());
        }
        if (!getObjeto().getCalculoInss()) {
            issRepository.excluir(TributIss.class, "tributOperacaoFiscal.id", getObjeto().getId());
        }
        if (!getObjeto().getObrigacaoFiscal()) {
            icmsRepository.excluir(TributIcmsUf.class, "tributOperacaoFiscal.id", getObjeto().getId());
        }
    }

    public void selecionaCFOP(SelectEvent event) {
        Cfop cfopSelecionado = (Cfop) event.getObject();
        getObjeto().setCfop(cfopSelecionado.getCfop());

        this.cfop = null;
    }

    public List<Cfop> getListaCfop(String nome) {
        List<Cfop> listaCfop = new ArrayList<>();
        try {
            listaCfop = cfops.getEntitys(Cfop.class, "descricao", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaCfop;
    }

    @Override
    protected Class<TributOperacaoFiscal> getClazz() {
        return TributOperacaoFiscal.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "TRIBUT_OPERACAO_FISCAL";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public Cfop getCfop() {
        return cfop;
    }

    public void setCfop(Cfop cfop) {
        this.cfop = cfop;
    }

    public Map<String, Boolean> getControla() {
        return controla;
    }

    public void setControla(Map<String, Boolean> controla) {
        this.controla = controla;
    }
}
