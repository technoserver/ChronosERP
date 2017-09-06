package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.FinDocumentoOrigem;
import com.chronos.modelo.entidades.FinPagamentoFixo;
import com.chronos.modelo.entidades.Fornecedor;
import com.chronos.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 15/08/17.
 */
@Named
@ViewScoped
public class FinPagamentoFixoControll  extends AbstractControll<FinPagamentoFixo> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<Fornecedor> fornecedores;
    @Inject
    private Repository<FinDocumentoOrigem> documentos;



    public List<Fornecedor> getListaFornecedor(String nome) {
        List<Fornecedor> listaFornecedor = new ArrayList<>();
        try {
            listaFornecedor = fornecedores.getEntitys(Fornecedor.class, "pessoa.nome", nome);
        } catch (Exception e) {
             e.printStackTrace();
        }
        return listaFornecedor;
    }

    public List<FinDocumentoOrigem> getListaFinDocumentoOrigem(String nome) {
        List<FinDocumentoOrigem> listaFinDocumentoOrigem = new ArrayList<>();
        try {
            listaFinDocumentoOrigem = documentos.getEntitys(FinDocumentoOrigem.class, "descricao", nome);
        } catch (Exception e) {
             e.printStackTrace();
        }
        return listaFinDocumentoOrigem;
    }

     /*TODO EXERCICIO: IMPLEMENTE A GERAÇÃO DOS LANÇAMENTOS COM BASE NAS INFORMAÇÕES DOS PAGAMENTOS FIXOS
    *  TODO 01 - UMA JANELA COM UM BOTÃO PARA GERAÇÃO
    *  TODO 02 - SEMPRE QUE ENTRAR NO SISTEMA FINANCEIRO, UMA ROTINA REALIZA A VERIFICAÇÃO/GERAÇÃO
    */

    @Override
    protected Class<FinPagamentoFixo> getClazz() {
        return FinPagamentoFixo.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "FIN_PAGAMENTO_FIXO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
