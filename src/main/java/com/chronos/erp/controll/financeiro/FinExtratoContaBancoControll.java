package com.chronos.erp.controll.financeiro;

import com.chronos.erp.bo.financeiro.ImportaOFX;
import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.util.jsf.Mensagem;
import org.apache.commons.io.FileUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by john on 14/08/17.
 */
@Named
@ViewScoped
public class FinExtratoContaBancoControll extends AbstractControll<ContaCaixa> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<FinExtratoContaBanco> extratos;
    @Inject
    private Repository<FinParcelaPagamento> pagamentos;
    @Inject
    private Repository<FinParcelaRecebimento> recebimentos;
    @Inject
    private Repository<FinChequeEmitido> cheques;

    private List<FinExtratoContaBanco> extratoContaBanco;
    private Date periodo;

    @Override
    public void voltar() {
        super.voltar();
        extratoContaBanco = new ArrayList<>();
        periodo = null;
    }

    public void buscaDados() {
        try {
            if (mesAno() == null) {
                Mensagem.addInfoMessage("Necessário informar um período válido!");

            } else {
                List<Filtro> filtros = new ArrayList<>();
                filtros.add(new Filtro(Filtro.AND, "contaCaixa", Filtro.IGUAL, getObjeto()));
                filtros.add(new Filtro(Filtro.AND, "mesAno", Filtro.IGUAL, mesAno()));

                extratoContaBanco = extratos.getEntitys(FinExtratoContaBanco.class, filtros);
                if (extratoContaBanco == null || extratoContaBanco.isEmpty()) {
                    Mensagem.addInfoMessage("Nenhum registro encontrado para o período informado.");

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Erro ao buscar os dados!", e);

        }
    }

    public void conciliaCheques() {
        try {
            if (extratoContaBanco.isEmpty()) {
                throw new Exception("Nenhum lançamento para conciliar!");
            }

            for (FinExtratoContaBanco e : extratoContaBanco) {
                if (e.getHistorico().contains("Cheque")) {
                    List<Filtro> filtros = new ArrayList<>();
                    filtros.add(new Filtro(Filtro.AND, "cheque.numero", Filtro.IGUAL, Integer.valueOf(e.getDocumento())));
                    filtros.add(new Filtro(Filtro.AND, "cheque.talonarioCheque.contaCaixa", Filtro.IGUAL, getObjeto()));

                    FinChequeEmitido chequeEmitido = cheques.get(FinChequeEmitido.class, filtros);

                    if (chequeEmitido != null) {
                        e.setConciliado("S");
                        if (chequeEmitido.getValor().compareTo(e.getValor().negate()) != 0) {
                            e.setObservacao("VALOR DO CHEQUE NO EXTRATO DIFERE DO VALOR ARMAZENADO NO BANCO DE DADOS - CHEQUE NAO CONCILIADO");
                            e.setConciliado("N");
                        }
                    } else {
                        e.setConciliado("N");
                    }
                }
            }
            Mensagem.addInfoMessage("Conciliação realizada!");
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Erro ao conciliar cheque!", e);
        }
    }

    public void salvaExtrato() {
        try {
            if (mesAno() == null) {
                throw new Exception("Período inválido!");
            }

            if (extratoContaBanco.isEmpty()) {
                throw new Exception("Nenhum registro para salvar!");
            }

            for (FinExtratoContaBanco e : extratoContaBanco) {
                e.setMesAno(mesAno());
                e.setMes(mesAno().substring(0, 2));
                e.setAno(mesAno().substring(3, 7));

                extratos.atualizar(e);
            }

            Mensagem.addInfoMessage("Extrato Salvo com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();

            Mensagem.addErrorMessage("Erro ao salvar o extrato!", e);
        }
    }

    public void conciliaLancamentos() {
        try {
            if (extratoContaBanco.isEmpty()) {
                throw new Exception("Nenhum lançamento para conciliar!");
            }

            for (FinExtratoContaBanco e : extratoContaBanco) {
                if (!e.getHistorico().contains("Cheque")) {
                    List<Filtro> filtros = new ArrayList<>();
                    filtros.add(new Filtro(Filtro.AND, "contaCaixa", Filtro.IGUAL, getObjeto()));

                    if (e.getValor().compareTo(BigDecimal.ZERO) < 0) {
                        filtros.add(new Filtro(Filtro.AND, "dataPagamento", Filtro.IGUAL, e.getDataMovimento()));
                        filtros.add(new Filtro(Filtro.AND, "valorPago", Filtro.IGUAL, e.getValor().negate()));
                        if (pagamentos.getEntitys(FinParcelaPagamento.class, filtros).isEmpty()) {
                            e.setConciliado("N");
                        } else {
                            e.setConciliado("S");
                        }
                    } else {
                        filtros.add(new Filtro(Filtro.AND, "dataRecebimento", Filtro.IGUAL, e.getDataMovimento()));
                        filtros.add(new Filtro(Filtro.AND, "valorRecebido", Filtro.IGUAL, e.getValor()));
                        if (recebimentos.getEntitys(FinParcelaRecebimento.class, filtros).isEmpty()) {
                            e.setConciliado("N");
                        } else {
                            e.setConciliado("S");
                        }
                    }
                }
            }

            Mensagem.addInfoMessage("Conciliação realizada!");
        } catch (Exception e) {
            e.printStackTrace();

            Mensagem.addErrorMessage("Erro ao conciliar lançamentos!", e);
        }
    }

    public String getTotais() {
        if (extratoContaBanco != null) {
            BigDecimal creditos = BigDecimal.ZERO;
            BigDecimal debitos = BigDecimal.ZERO;
            BigDecimal saldo = BigDecimal.ZERO;
            for (FinExtratoContaBanco e : extratoContaBanco) {
                if (e.getValor().compareTo(BigDecimal.ZERO) == -1) {
                    debitos = debitos.add(e.getValor());
                } else {
                    creditos = creditos.add(e.getValor());
                }
            }
            saldo = creditos.add(debitos);

            DecimalFormat decimalFormat = new DecimalFormat("#,###,##0.00");
            String texto = "|   Créditos: R$ " + decimalFormat.format(creditos);
            texto += "|   Débitos: R$ " + decimalFormat.format(debitos);
            texto += "|   Saldo: R$ " + decimalFormat.format(saldo) + "   |";
            return texto;
        }
        return "";
    }

    public void importaOFX(FileUploadEvent event) {
        try {
            UploadedFile arquivo = event.getFile();
            File file = File.createTempFile("extrato", ".ofx");
            FileUtils.copyInputStreamToFile(arquivo.getInputstream(), file);

            ImportaOFX importa = new ImportaOFX();
            extratoContaBanco = importa.importaArquivoOFX(file);
            //seta os dados do extrato
            for (FinExtratoContaBanco e : extratoContaBanco) {
                e.setContaCaixa(getObjeto());
            }

            Mensagem.addInfoMessage("Extrato importado com sucesso.!");
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Erro ao importar o extrato.!", e);

        }
    }

    private String mesAno() {
        try {
            if (periodo == null) {
                return null;
            }
            Calendar dataValida = Calendar.getInstance();
            dataValida.setLenient(false);
            dataValida.setTime(periodo);

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
            return dateFormat.format(dataValida.getTime());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected Class<ContaCaixa> getClazz() {
        return ContaCaixa.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "EXTRATO_CONTA_BANCO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public List<FinExtratoContaBanco> getExtratoContaBanco() {
        return extratoContaBanco;
    }

    public void setExtratoContaBanco(List<FinExtratoContaBanco> extratoContaBanco) {
        this.extratoContaBanco = extratoContaBanco;
    }

    public Date getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Date periodo) {
        this.periodo = periodo;
    }
}
