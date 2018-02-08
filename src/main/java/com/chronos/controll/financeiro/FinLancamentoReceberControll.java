package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.*;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.Constantes;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by john on 15/08/17.
 */
@Named
@ViewScoped
public class FinLancamentoReceberControll extends AbstractControll<FinLancamentoReceber> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<FinDocumentoOrigem> documentos;
    @Inject
    private Repository<Cliente> clientes;
    @Inject
    private Repository<ContaCaixa> contas;
    @Inject
    private Repository<NaturezaFinanceira> naturezas;
    @Inject
    private Repository<FinStatusParcela> status;
    @Inject
    private Repository<FinParcelaReceber> parcelas;
    @Inject
    private Repository<FinConfiguracaoBoleto> configuracoes;
    @Inject
    private Repository<Pessoa> pessoas;

    private List<FinLancamentoReceber> lancamentosSelecionados;
    private List<FinLancamentoReceber> lancamentosFiltrados;

    private FinParcelaReceber finParcelaReceber;
    private FinParcelaReceber finParcelaReceberSelecionado;
    private FinLctoReceberNtFinanceira finLctoReceberNtFinanceira;
    private FinLctoReceberNtFinanceira finLctoReceberNtFinanceiraSelecionado;

    //atributos utilizados para geração das parcelas
    private ContaCaixa contaCaixa;
    private NaturezaFinanceira naturezaFinanceira;


    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setListaFinParcelaReceber(new ArrayList<>());
        getObjeto().setListaFinLctoReceberNtFinanceira(new HashSet<>());
        getObjeto().setDataLancamento(new Date());
        getObjeto().setPrimeiroVencimento(new Date());
        getObjeto().setEmpresa(empresa);

    }

    @Override
    public void doEdit() {
        super.doEdit();
        FinLancamentoReceber lancamento = dataModel.getRowData(getObjeto().getId().toString());
        setObjeto(lancamento);
    }

    @Override
    public void salvar() {
        getObjeto().setValorAReceber(getObjeto().getValorTotal());
        try {
            if (getObjeto().getId() == null) {
                gerarParcelas();
                geraNaturezaFinanceira();
            }
            super.salvar();
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao salvar o registro!", e);

        }
    }

    public void gerarParcelas() throws Exception {


        FinStatusParcela statusParcela = Constantes.FIN.STATUS_ABERTO;


        if (contaCaixa == null || contaCaixa.getId() == null) {
            throw new Exception("É necessário informar a conta caixa para previsão das parcelas.");
        }

        Date dataEmissão = new Date();
        Calendar primeiroVencimento = Calendar.getInstance();
        primeiroVencimento.setTime(getObjeto().getPrimeiroVencimento());
        BigDecimal valorParcela = getObjeto().getValorAReceber().divide(BigDecimal.valueOf(getObjeto().getQuantidadeParcela()), RoundingMode.HALF_DOWN);
        BigDecimal somaParcelas = BigDecimal.ZERO;
        BigDecimal residuo = BigDecimal.ZERO;
        String nossoNumero;
        DecimalFormat formatoNossoNumero4 = new DecimalFormat("0000");
        DecimalFormat formatoNossoNumero5 = new DecimalFormat("00000");
        SimpleDateFormat formatoNossoNumero6 = new SimpleDateFormat("D");
        Date dataAtual = new Date();
        for (int i = 0; i < getObjeto().getQuantidadeParcela(); i++) {
            FinParcelaReceber parcelaReceber = new FinParcelaReceber();
            parcelaReceber.setFinLancamentoReceber(getObjeto());
            parcelaReceber.setFinStatusParcela(statusParcela);
            parcelaReceber.setContaCaixa(contaCaixa);
            parcelaReceber.setNumeroParcela(i + 1);
            parcelaReceber.setDataEmissao(dataEmissão);
            if (i > 0) {
                primeiroVencimento.add(Calendar.DAY_OF_MONTH, getObjeto().getIntervaloEntreParcelas());
            }
            parcelaReceber.setDataVencimento(primeiroVencimento.getTime());
            parcelaReceber.setEmitiuBoleto("S");
            parcelaReceber.setTaxaJuro(contaCaixa.getTaxaJuro());
            parcelaReceber.setTaxaMulta(contaCaixa.getTaxaMulta());

            nossoNumero = formatoNossoNumero5.format(getObjeto().getCliente().getId());
            nossoNumero += formatoNossoNumero5.format(parcelaReceber.getContaCaixa().getId());
            nossoNumero += formatoNossoNumero4.format(parcelaReceber.getNumeroParcela());
            nossoNumero += formatoNossoNumero6.format(dataAtual);
            parcelaReceber.setBoletoNossoNumero(nossoNumero);

            parcelaReceber.setValor(valorParcela);

            somaParcelas = somaParcelas.add(valorParcela);
            if (i == (getObjeto().getQuantidadeParcela() - 1)) {
                residuo = getObjeto().getValorAReceber().subtract(somaParcelas);
                valorParcela = valorParcela.add(residuo);
                parcelaReceber.setValor(valorParcela);
            }
            getObjeto().getListaFinParcelaReceber().add(parcelaReceber);
        }
    }


    private void geraNaturezaFinanceira() {
        FinLctoReceberNtFinanceira finLctoReceberNaturezaFinancaeira = new FinLctoReceberNtFinanceira();
        finLctoReceberNaturezaFinancaeira.setFinLancamentoReceber(getObjeto());
        finLctoReceberNaturezaFinancaeira.setNaturezaFinanceira(naturezaFinanceira);
        finLctoReceberNaturezaFinancaeira.setDataInclusao(new Date());
        finLctoReceberNaturezaFinancaeira.setValor(getObjeto().getValorAReceber());

        getObjeto().getListaFinLctoReceberNtFinanceira().add(finLctoReceberNaturezaFinancaeira);
    }

    public void mesclarLancamentos() {
        try {
            if (lancamentosSelecionados.size() <= 1) {
                Mensagem.addWarnMessage("Necessário selecionar 2 ou mais lançamentos!");

            } else if (lancamentosSelecionados.size() > 1) {
                BigDecimal valorTotal = BigDecimal.ZERO;
                int quantidadeParcelas = 0;
                for (FinLancamentoReceber l : lancamentosSelecionados) {
                    if (l.getMescladoPara() != null) {
                        throw new Exception("Lançamento selecionado já mesclado: " + l.getId());
                    }
                    if (l.getValorTotal() != null) {
                        valorTotal = valorTotal.add(l.getValorTotal());
                    }
                    quantidadeParcelas += l.getQuantidadeParcela();
                }

                List<Filtro> filtros = new ArrayList<>();
                filtros.add(new Filtro(Filtro.AND, "finLancamentoReceber", " IN ", lancamentosSelecionados));

                List<FinParcelaReceber> listaParcelas = parcelas.getEntitys(FinParcelaReceber.class, filtros);

                FinLancamentoReceber lancamentoMesclado = new FinLancamentoReceber();
                lancamentoMesclado.setCliente(lancamentosSelecionados.get(0).getCliente());
                lancamentoMesclado.setFinDocumentoOrigem(lancamentosSelecionados.get(0).getFinDocumentoOrigem());
                lancamentoMesclado.setDataLancamento(lancamentosSelecionados.get(0).getDataLancamento());
                lancamentoMesclado.setIntervaloEntreParcelas(lancamentosSelecionados.get(0).getIntervaloEntreParcelas());
                lancamentoMesclado.setNumeroDocumento(lancamentosSelecionados.get(0).getNumeroDocumento());
                lancamentoMesclado.setPrimeiroVencimento(lancamentosSelecionados.get(0).getPrimeiroVencimento());
                lancamentoMesclado.setQuantidadeParcela(quantidadeParcelas);
                lancamentoMesclado.setValorAReceber(valorTotal);
                lancamentoMesclado.setValorTotal(valorTotal);

                for (FinParcelaReceber p : listaParcelas) {
                    p.setId(null);
                    p.setFinLancamentoReceber(lancamentoMesclado);
                    p.setListaFinParcelaRecebimento(null);
                }
                lancamentoMesclado.setListaFinParcelaReceber(new ArrayList<>());

                dao.salvar(lancamentoMesclado);

                for (FinLancamentoReceber l : lancamentosSelecionados) {
                    l.setMescladoPara(lancamentoMesclado.getId());
                    l = dao.atualizar(l);
                }
                Mensagem.addInfoMessage("Lançamentos Mesclados!");

            }
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao mesclar os lançamentos!", e);

        }
    }

    public void gerarRemessa() {
        try {
            File file = File.createTempFile("cnab240", ".txt");

            List<FinParcelaReceber> listaParcelasReceber = new ArrayList<>(getObjeto().getListaFinParcelaReceber());
            if (listaParcelasReceber.isEmpty()) {
                throw new Exception("É necessário gerar as parcelas antes de gerar a remessa.");
            }
            FinConfiguracaoBoleto configuracaoBoleto = configuracaoBoleto(listaParcelasReceber.get(0).getContaCaixa());


            PessoaEndereco pessoaEndereco = null;
            Pessoa pessoa = pessoas.getEntityJoinFetch(getObjeto().getCliente().getPessoa().getId(), Pessoa.class);
            for (PessoaEndereco e : pessoa.getListaPessoaEndereco()) {
                if (e.getPrincipal() != null && e.getPrincipal().equals("S")) {
                    pessoaEndereco = e;
                }
            }
            if (pessoaEndereco == null) {
                throw new Exception("Cliente sem endereço principal cadastrado.");
            }
            //TODO criar projeto para gerenciar os arquivos de remessa
            //  GerarArquivoRemessaBB gerarArquivoRemessaBB = new GerarArquivoRemessaBB();
            //  gerarArquivoRemessaBB.gerarArquivoRemessa(listaParcelasReceber, empresa, pessoaEndereco, configuracaoBoleto, file);

            FacesUtil.downloadArquivo(file, "cnab240.txt");
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao gerar o arquivo remessa!", e);

        }
    }

    private FinConfiguracaoBoleto configuracaoBoleto(ContaCaixa contaCaixa) throws Exception {
        List<FinConfiguracaoBoleto> listaConfiguracaoBoleto = configuracoes.getEntitys(FinConfiguracaoBoleto.class, "contaCaixa", contaCaixa);
        if (listaConfiguracaoBoleto.isEmpty()) {
            throw new Exception("Não existem configurações de boleto para a conta caixa.");
        }
        return listaConfiguracaoBoleto.get(0);
    }

    public void alterarFinParcelaReceber() {
        finParcelaReceber = finParcelaReceberSelecionado;
    }

    public void salvarFinParcelaReceber() {
        salvar("Registro salvo com sucesso!");
    }

    public void incluirFinLctoReceberNtFinanceira() {
        finLctoReceberNtFinanceira = new FinLctoReceberNtFinanceira();
        finLctoReceberNtFinanceira.setFinLancamentoReceber(getObjeto());
    }

    public void alterarFinLctoReceberNtFinanceira() {
        finLctoReceberNtFinanceira = finLctoReceberNtFinanceiraSelecionado;
    }

    public void salvarFinLctoReceberNtFinanceira() {
        if (finLctoReceberNtFinanceira.getId() == null) {
            getObjeto().getListaFinLctoReceberNtFinanceira().add(finLctoReceberNtFinanceira);
        }
        salvar("Registro salvo com sucesso!");
    }

    public void excluirFinLctoReceberNtFinanceira() {

        getObjeto().getListaFinLctoReceberNtFinanceira().remove(finLctoReceberNtFinanceiraSelecionado);
        salvar("Registro excluído com sucesso!");

    }

    public List<FinDocumentoOrigem> getListaFinDocumentoOrigem(String nome) {
        List<FinDocumentoOrigem> listaFinDocumentoOrigem = new ArrayList<>();
        try {
            listaFinDocumentoOrigem = documentos.getEntitys(FinDocumentoOrigem.class, "siglaDocumento", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaFinDocumentoOrigem;
    }

    public List<Cliente> getListaCliente(String nome) {
        List<Cliente> listaCliente = new ArrayList<>();
        try {
            listaCliente = clientes.getEntitys(Cliente.class, "pessoa.nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaCliente;
    }

    public List<ContaCaixa> getListaContaCaixa(String nome) {
        List<ContaCaixa> listaContaCaixa = new ArrayList<>();
        try {
            listaContaCaixa = contas.getEntitys(ContaCaixa.class, "descricao", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaContaCaixa;
    }

    public List<NaturezaFinanceira> getListaNaturezaFinanceira(String nome) {
        List<NaturezaFinanceira> listaNaturezaFinanceira = new ArrayList<>();
        try {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("apareceAReceber", "S"));
            filtros.add(new Filtro("descricao", Filtro.LIKE, nome));
            listaNaturezaFinanceira = naturezas.getEntitys(NaturezaFinanceira.class, filtros, new Object[]{"descricao"});
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaNaturezaFinanceira;
    }

    @Override
    protected Class<FinLancamentoReceber> getClazz() {
        return FinLancamentoReceber.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "LANCAMENTO_RECEBER";
    }

    @Override
    protected boolean auditar() {
        return false;
    }


    public FinParcelaReceber getFinParcelaReceber() {
        return finParcelaReceber;
    }

    public void setFinParcelaReceber(FinParcelaReceber finParcelaReceber) {
        this.finParcelaReceber = finParcelaReceber;
    }

    public FinParcelaReceber getFinParcelaReceberSelecionado() {
        return finParcelaReceberSelecionado;
    }

    public void setFinParcelaReceberSelecionado(FinParcelaReceber finParcelaReceberSelecionado) {
        this.finParcelaReceberSelecionado = finParcelaReceberSelecionado;
    }

    public FinLctoReceberNtFinanceira getFinLctoReceberNtFinanceira() {
        return finLctoReceberNtFinanceira;
    }

    public void setFinLctoReceberNtFinanceira(FinLctoReceberNtFinanceira finLctoReceberNtFinanceira) {
        this.finLctoReceberNtFinanceira = finLctoReceberNtFinanceira;
    }

    public FinLctoReceberNtFinanceira getFinLctoReceberNtFinanceiraSelecionado() {
        return finLctoReceberNtFinanceiraSelecionado;
    }

    public void setFinLctoReceberNtFinanceiraSelecionado(FinLctoReceberNtFinanceira finLctoReceberNtFinanceiraSelecionado) {
        this.finLctoReceberNtFinanceiraSelecionado = finLctoReceberNtFinanceiraSelecionado;
    }

    public ContaCaixa getContaCaixa() {
        return contaCaixa;
    }

    public void setContaCaixa(ContaCaixa contaCaixa) {
        this.contaCaixa = contaCaixa;
    }

    public NaturezaFinanceira getNaturezaFinanceira() {
        return naturezaFinanceira;
    }

    public void setNaturezaFinanceira(NaturezaFinanceira naturezaFinanceira) {
        this.naturezaFinanceira = naturezaFinanceira;
    }

    public List<FinLancamentoReceber> getLancamentosSelecionados() {
        return lancamentosSelecionados;
    }

    public void setLancamentosSelecionados(List<FinLancamentoReceber> lancamentosSelecionados) {
        this.lancamentosSelecionados = lancamentosSelecionados;
    }

    public List<FinLancamentoReceber> getLancamentosFiltrados() {
        return lancamentosFiltrados;
    }

    public void setLancamentosFiltrados(List<FinLancamentoReceber> lancamentosFiltrados) {
        this.lancamentosFiltrados = lancamentosFiltrados;
    }

}
