package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.Cheque;
import com.chronos.modelo.entidades.FinChequeEmitido;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import org.apache.commons.lang3.StringUtils;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by john on 14/08/17.
 */
@Named
@ViewScoped
public class FinChequeEmitidoControll extends AbstractControll<FinChequeEmitido> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<Cheque> chequeRepository;

    @Override
    public void doCreate() {
        super.doCreate();

        getObjeto().setDataEmissao(new Date());

    }

    public List<Cheque> getListaCheque(String descricao) {
        List<Cheque> cheques = new ArrayList<>();

        try {
            List<Filtro> filtros = new ArrayList<>();
            int numero = StringUtils.isNumeric(descricao) && descricao.length() <= 9 ? Integer.parseInt(descricao) : 0;
            filtros.add(new Filtro("numero", Filtro.IGUAL, numero));
            cheques = StringUtils.isEmpty(descricao)
                    ? chequeRepository.getEntitys(Cheque.class, new Object[]{"numero"})
                    : chequeRepository.getEntitys(Cheque.class, filtros, new Object[]{"numero"});
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return cheques;
    }

    @Override
    protected Class<FinChequeEmitido> getClazz() {
        return FinChequeEmitido.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "CHEQUE_RECEBIDO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
