package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.ContaPessoa;
import com.chronos.modelo.entidades.MovimentoContaPessoa;
import com.chronos.modelo.entidades.Pessoa;
import com.chronos.modelo.enuns.TipoLancamento;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.financeiro.ContaPessoaService;
import com.chronos.util.jsf.Mensagem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.DecimalMin;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by john on 01/02/18.
 */
@Named
@ViewScoped
public class CreditoPessoaControll extends AbstractControll<ContaPessoa> implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(CreditoPessoaControll.class);

    @Inject
    private Repository<Pessoa> pessoaRepository;

    @Inject
    private Repository<MovimentoContaPessoa> movimentoRepository;

    private ERPLazyDataModel<MovimentoContaPessoa> dataModelMovimento;

    @DecimalMin(value = "0.01", message = "deve ser maior que R$ 0,01")
    private BigDecimal valor;

    private MovimentoContaPessoa movimento;
    private List<MovimentoContaPessoa> movimentos;


    @Inject
    private ContaPessoaService service;

    @Override
    public ERPLazyDataModel<ContaPessoa> getDataModel() {
        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setDao(dao);
            dataModel.setClazz(ContaPessoa.class);
        }
        dataModel.setAtributos(new Object[]{"pessoa.nome", "saldo", "classificacaoContabilConta"});
        return dataModel;
    }

    public ERPLazyDataModel<MovimentoContaPessoa> getDataModelMovimento() {
        if (dataModelMovimento == null) {
            dataModelMovimento = new ERPLazyDataModel<>();
            dataModelMovimento.setDao(movimentoRepository);
            dataModelMovimento.setClazz(MovimentoContaPessoa.class);


        }
        dataModelMovimento.getFiltros().clear();
        dataModelMovimento.addFiltro("contaPessoa.id", getObjeto().getId(), Filtro.IGUAL);
        return dataModelMovimento;
    }

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setSaldo(BigDecimal.ZERO);
    }

    @Override
    public void doEdit() {
        super.doEdit();
        valor = BigDecimal.ZERO;
        ContaPessoa conta = dao.get(getObjeto().getId(),ContaPessoa.class);
        setObjeto(conta);

    }

    public void lancaMovimento() {
        try {
            service.lancaMovimento(getObjeto(), valor, TipoLancamento.CREDITO, "0", "0");
            valor = BigDecimal.ZERO;
            Mensagem.addInfoMessage("Lancamento de credito realizado com sucesso");
        } catch (Exception ex) {
            logger.error("erro ao lançar movimento de credito ", ex);
            throw new RuntimeException("erro ao lançar movimento de credito\n " + ex.getMessage());
        }
    }

    public void estornarValor() {
        try {
            service.lancaMovimento(getObjeto(), valor, TipoLancamento.CREDITO, "0", "0");
            Mensagem.addInfoMessage("Estorno de credito realizado com sucesso");
        } catch (Exception ex) {
            logger.error("erro ao estornar movimento de credito ", ex);
            throw new RuntimeException("erro ao estornar movimento de credito\n " + ex.getMessage());
        }
    }

    public List<Pessoa> getListaPessoa(String nome) {
        List<Pessoa> listaPessoa = new ArrayList<>();
        try {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("cliente", "S"));
            filtros.add(new Filtro("nome", Filtro.LIKE, nome));
            listaPessoa = pessoaRepository.getEntitys(Pessoa.class, filtros, new Object[]{"nome"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPessoa;
    }

    @Override
    protected Class<ContaPessoa> getClazz() {
        return ContaPessoa.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "CREDITO_CLIENTE";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public TipoLancamento[] getTipo() {
        return TipoLancamento.values();
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}