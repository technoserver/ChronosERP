package com.chronos.erp.bo.financeiro;

import com.chronos.erp.modelo.entidades.FinExtratoContaBanco;
import net.sf.ofx4j.domain.data.MessageSetType;
import net.sf.ofx4j.domain.data.ResponseEnvelope;
import net.sf.ofx4j.domain.data.ResponseMessageSet;
import net.sf.ofx4j.domain.data.banking.BankStatementResponseTransaction;
import net.sf.ofx4j.domain.data.banking.BankingResponseMessageSet;
import net.sf.ofx4j.domain.data.common.Transaction;
import net.sf.ofx4j.io.AggregateUnmarshaller;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 16/03/18.
 */
public class ImportaOFX {

    public List<FinExtratoContaBanco> importaArquivoOFX(File arquivo) {
        try {
            AggregateUnmarshaller a = new AggregateUnmarshaller(ResponseEnvelope.class);
            ResponseEnvelope re = (ResponseEnvelope) a.unmarshal(new FileInputStream(arquivo));

            //define o tipo de mensagem
            MessageSetType type = MessageSetType.banking;
            ResponseMessageSet message = re.getMessageSet(type);

            if (message != null) {
                //busca a lista de contas no arquivo
                FinExtratoContaBanco extrato;
                List<FinExtratoContaBanco> listaExtrato = new ArrayList<>();
                List bank = ((BankingResponseMessageSet) message).getStatementResponses();
                for (int i = 0; i < bank.size(); i++) {
                    //objeto que contem os dados das contas
                    BankStatementResponseTransaction conta = (BankStatementResponseTransaction) bank.get(i);

                    //busca os dados das transacoes
                    List transacoes = conta.getMessage().getTransactionList().getTransactions();
                    for (int j = 0; j < transacoes.size(); j++) {
                        Transaction transaction = (Transaction) transacoes.get(j);

                        extrato = new FinExtratoContaBanco();
                        extrato.setDataMovimento(transaction.getDatePosted());
                        extrato.setDataBalancete(transaction.getDatePosted());
                        extrato.setDocumento(transaction.getCheckNumber());
                        extrato.setHistorico(transaction.getMemo());
                        extrato.setValor(transaction.getBigDecimalAmount());

                        listaExtrato.add(extrato);
                    }
                }
                return listaExtrato;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
