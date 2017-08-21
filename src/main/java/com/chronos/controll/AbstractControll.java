/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll;

import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.Usuario;
import com.chronos.modelo.entidades.enuns.Estados;
import com.chronos.repository.Repository;
import com.chronos.security.UsuarioLogado;
import com.chronos.security.UsuarioSistema;
import com.chronos.service.AbstractService;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;

/**
 *
 * @author john
 * @param <T>
 */
public abstract class AbstractControll<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private T objetoSelecionado;
    private T objeto;
    protected ERPLazyDataModel<T> dataModel;
    private boolean telaGrid = true;
    @Inject
    protected Repository<T> dao;

    private String titulo;
    private int activeTabIndex;
    protected Usuario usuario;
    protected Empresa empresa;
    protected Object[] atributos;
    protected Object[] joinFetch;

    private Map<String, String> simNao;
    private Map<String, String> naoSim;
    private Map<String, String> tipoPessoa;
    private Map<String, String> sexo;
    private Map<String, String> tipoSangue;
    private Map<String, String> racaCor;
    private Map<String, String> crt;
    private Map<String, String> tipoRegimeEmpresa;
    private Map<String, Integer> tipoTelefone;
    private Map<String, String> tipoSindicato;
    private Map<String, String> produtoClasse;
    private Map<String, String> tipoItemSped;
    private Map<String, String> produtoTipo;
    private Map<String, String> produtoIat;
    private Map<String, String> produtoIppt;
    private Map<String, String> clienteIndicadorPreco;
    private Map<String, String> clienteTipoFrete;
    private Map<String, String> clienteFormaDesconto;
    private Map<String, String> fornecedorLocalizacao;
    private Map<String, String> colaboradorFormaPagamento;
    private Map<String, String> talonarioChequeStatus;
    private Map<String, String> chequeStatus;
    private Map<String, String> tipoContaCaixa;
    private Map<String, String> tipoFeriado;
    private Map<String, String> feriadoAbrangencia;

    //Financeiro
    private Map<String, String> tipoNaturazaFinanceira;
    private Map<String, String> layoutRemessa;
    private Map<String, String> especieCobranca;
    private Map<String, String> tipoBaixa;

    //Estoque
    private Map<String, String> requisicaoInternaSituacao;
    private Map<String, String> tipoReajuste;

    //NFe
    private Map<String, Integer> origemMercadoriaNfe;
    private Map<String, String> codigoModeloNfe;
    private Map<String, Integer> localDestinoNfe;
    private Map<String, Integer> consumidorOperacaoNfe;
    private Map<String, String> consumidorPresencaNfe;
    private Map<String, Integer> tipoOperacaoNfe;
    private Map<String, Integer> tipoEmissaoNfe;
    private Map<String, Integer> finalidadeEmissaoNfe;
    private Map<String, Integer> formatoImpressaoDanfeNfe;
    private Map<String, Integer> modalidadeFreteNfe;
    private Map<String, Integer> statusNfe;

    protected abstract Class<T> getClazz();

    protected abstract String getFuncaoBase();

    @PostConstruct
    public void init() {
        dataModel = new ERPLazyDataModel<>();
        dataModel.setClazz(getClazz());
        dataModel.setDao(dao);
        usuario = getUsuarioLogado();
        empresa = getEmpresaUsuario();
        atributos = new Object[]{"nome"};

        //Cadastros
        simNao = new LinkedHashMap<>();
        simNao.put("Sim", "S");
        simNao.put("Não", "N");

        naoSim = new LinkedHashMap<>();
        naoSim.put("Não", "N");
        naoSim.put("Sim", "S");


        tipoPessoa = new LinkedHashMap<>();
        tipoPessoa.put("Fisica", "F");
        tipoPessoa.put("Jurídica", "J");

        sexo = new LinkedHashMap<>();
        sexo.put("Masculino", "M");
        sexo.put("Feminino", "F");

        tipoSangue = new LinkedHashMap<>();
        tipoSangue.put("A+", "A+");
        tipoSangue.put("B+", "B+");
        tipoSangue.put("O+", "O+");
        tipoSangue.put("AB+", "AB+");
        tipoSangue.put("A-", "A-");
        tipoSangue.put("B-", "B-");
        tipoSangue.put("AB-", "AB-");
        tipoSangue.put("O-", "O-");

        racaCor = new LinkedHashMap<>();
        racaCor.put("Branco", "B");
        racaCor.put("Negro", "N");
        racaCor.put("Pardo", "P");
        racaCor.put("Indio", "I");

        tipoSindicato = new LinkedHashMap<>();
        tipoSindicato.put("Patronal", "P");
        tipoSindicato.put("Empregados", "E");

        produtoClasse = new LinkedHashMap<>();
        produtoClasse.put("A", "A");
        produtoClasse.put("B", "B");
        produtoClasse.put("C", "C");

        produtoTipo = new LinkedHashMap<>();
        produtoTipo.put("Venda", "V");
        produtoTipo.put("Composição", "C");
        produtoTipo.put("Produção", "P");
        produtoTipo.put("Insumo", "I");
        produtoTipo.put("Uso Proprio", "U");

        produtoIat = new LinkedHashMap<>();
        produtoIat.put("Arredondamento", "A");
        produtoIat.put("Truncamento", "T");

        produtoIppt = new LinkedHashMap<>();
        produtoIppt.put("Próprio", "P");
        produtoIppt.put("Terceiro", "T");

        tipoItemSped = new LinkedHashMap<>();
        tipoItemSped.put("Mercadoria para Revenda", "00");
        tipoItemSped.put("Matéria-Prima", "01");
        tipoItemSped.put("Embalagem", "02");
        tipoItemSped.put("Produto em Processo", "03");
        tipoItemSped.put("Produto Acabado", "04");
        tipoItemSped.put("Subproduto", "05");
        tipoItemSped.put("Produto Intermediário", "06");
        tipoItemSped.put("Material de Uso e Consumo", "07");
        tipoItemSped.put("Ativo Imobilizado", "08");
        tipoItemSped.put("Serviços", "09");
        tipoItemSped.put("Outros Insumos", "10");
        tipoItemSped.put("Outras", "99");

        clienteIndicadorPreco = new LinkedHashMap<>();
        clienteIndicadorPreco.put("Tabela", "T");
        clienteIndicadorPreco.put("Último Pedido", "P");

        clienteTipoFrete = new LinkedHashMap<>();
        clienteTipoFrete.put("Emitente", "E");
        clienteTipoFrete.put("Destinatario", "D");
        clienteTipoFrete.put("Sem frete", "S");
        clienteTipoFrete.put("Terceiros", "T");

        clienteFormaDesconto = new LinkedHashMap<>();
        clienteFormaDesconto.put("Produto", "P");
        clienteFormaDesconto.put("Fim do Pedido", "F");

        crt = new LinkedHashMap<>();
        crt.put("1 - Simples Nacional", "1");
        crt.put("2 - Simples Nac - Excesso", "2");
        crt.put("3 - Regime Normal", "3");

        tipoRegimeEmpresa = new LinkedHashMap<>();
        tipoRegimeEmpresa.put("Lucro Real", "1");
        tipoRegimeEmpresa.put("Lucro Presumido", "2");
        tipoRegimeEmpresa.put("Simples Nacional", "3");

        tipoTelefone = new LinkedHashMap<>();
        tipoTelefone.put("Residencial", 0);
        tipoTelefone.put("Comercial", 1);
        tipoTelefone.put("Celular", 2);
        tipoTelefone.put("Outro", 3);

        fornecedorLocalizacao = new LinkedHashMap<>();
        fornecedorLocalizacao.put("Nacional", "N");
        fornecedorLocalizacao.put("Exterior", "E");

        colaboradorFormaPagamento = new LinkedHashMap<>();
        colaboradorFormaPagamento.put("Dinheiro", "1");
        colaboradorFormaPagamento.put("Cheque", "2");
        colaboradorFormaPagamento.put("Conta", "3");

        talonarioChequeStatus = new LinkedHashMap<>();
        talonarioChequeStatus.put("Normal", "N");
        talonarioChequeStatus.put("Cancelado", "C");
        talonarioChequeStatus.put("Extraviado", "E");
        talonarioChequeStatus.put("Utilizado", "U");

        chequeStatus = new LinkedHashMap<>();
        chequeStatus.put("Em Ser", "E");
        chequeStatus.put("Baixado", "B");
        chequeStatus.put("Utilizado", "U");
        chequeStatus.put("Compensado", "C");
        chequeStatus.put("Cancelado", "N");

        tipoContaCaixa = new LinkedHashMap<>();
        tipoContaCaixa.put("Corrente", "C");
        tipoContaCaixa.put("Poupança", "P");
        tipoContaCaixa.put("Investimento", "I");
        tipoContaCaixa.put("Caixa Interno", "X");

        tipoFeriado = new LinkedHashMap<>();
        tipoFeriado.put("Fixo", "F");
        tipoFeriado.put("Móvel", "M");

        feriadoAbrangencia = new LinkedHashMap<>();
        feriadoAbrangencia.put("Federal", "F");
        feriadoAbrangencia.put("Estadual", "E");
        feriadoAbrangencia.put("Municipal", "M");

        //Estoque
        requisicaoInternaSituacao = new LinkedHashMap<>();
        requisicaoInternaSituacao.put("Aberta", "A");
        requisicaoInternaSituacao.put("Deferida", "D");
        requisicaoInternaSituacao.put("Indeferida", "I");

        tipoReajuste = new LinkedHashMap<>();
        tipoReajuste.put("Aumentar", "A");
        tipoReajuste.put("Diminuir", "D");

        //NFe
        codigoModeloNfe = new LinkedHashMap<>();
        codigoModeloNfe.put("Nota Fiscal Eletrônica - NFe", "55");

        localDestinoNfe = new LinkedHashMap<>();
        localDestinoNfe.put("Operação Interna", 1);
        localDestinoNfe.put("Operação Interestadual", 2);
        localDestinoNfe.put("Operação com Exterior", 3);

        consumidorOperacaoNfe = new LinkedHashMap<>();
        consumidorOperacaoNfe.put("Normal", 0);
        consumidorOperacaoNfe.put("Consumidor Final", 1);

        consumidorPresencaNfe = new LinkedHashMap<>();
        consumidorPresencaNfe.put("Operação Presencial", "1");
        consumidorPresencaNfe.put("Operação não Presencial - Internet", "2");
        consumidorPresencaNfe.put("Operação não Presencial - Teleatendimento", "3");
        consumidorPresencaNfe.put("Operação não Presencial - Outros", "9");
        consumidorPresencaNfe.put("Não se aplica", "0");

        tipoOperacaoNfe = new LinkedHashMap<>();
        tipoOperacaoNfe.put("Entrada", 0);
        tipoOperacaoNfe.put("Saída", 1);

        tipoEmissaoNfe = new LinkedHashMap<>();
        tipoEmissaoNfe.put("Normal", 1);
        tipoEmissaoNfe.put("Contigência", 2);
        tipoEmissaoNfe.put("Contingência SCAN", 3);
        tipoEmissaoNfe.put("Contingência DPEC", 4);
        tipoEmissaoNfe.put("Contingência FS-DA", 5);

        finalidadeEmissaoNfe = new LinkedHashMap<>();
        finalidadeEmissaoNfe.put("Normal", 1);
        finalidadeEmissaoNfe.put("Complementar", 2);
        finalidadeEmissaoNfe.put("Ajuste", 3);

        formatoImpressaoDanfeNfe = new LinkedHashMap<>();
        formatoImpressaoDanfeNfe.put("Retrato", 1);
        formatoImpressaoDanfeNfe.put("Paisagem", 1);

        modalidadeFreteNfe = new LinkedHashMap<>();
        modalidadeFreteNfe.put("Conta Emitente", 0);
        modalidadeFreteNfe.put("Conta Destinatário", 1);
        modalidadeFreteNfe.put("Conta Terceiros", 2);
        modalidadeFreteNfe.put("Sem Frete", 9);

        statusNfe = new LinkedHashMap<>();
        statusNfe.put("Em Edição", 0);
        statusNfe.put("Salva", 1);
        statusNfe.put("Validada", 2);
        statusNfe.put("Assinada", 3);
        statusNfe.put("Enviada", 4);
        statusNfe.put("Autorizada", 5);
        statusNfe.put("Cancelada", 6);


        tipoNaturazaFinanceira = new LinkedHashMap<>();
        tipoNaturazaFinanceira.put("Receita", "R");
        tipoNaturazaFinanceira.put("Despesa", "D");

        layoutRemessa = new LinkedHashMap<>();
        layoutRemessa.put("240", "240");
        layoutRemessa.put("400", "400");

        especieCobranca = new LinkedHashMap<>();
        especieCobranca.put("Duplicata Mercantil", "DM");
        especieCobranca.put("Duplicata de Serviços", "DS");
        especieCobranca.put("Recibo", "RC");
        especieCobranca.put("Nota Promissória", "NP");

        tipoBaixa = new LinkedHashMap<>();
        tipoBaixa.put("Total", "T");
        tipoBaixa.put("Parcial", "P");


    }

    public void voltar() {
        titulo = "Consultar";
        telaGrid = true;
    }
    
    public List<Estados> getEstado() {
        List<Estados> estados = new ArrayList<>();
        if (estados.isEmpty()) {
            estados = new LinkedList<>();
            estados.addAll(Arrays.asList(Estados.values()));
        }
        return estados;
    }

    public String keyFromValue(HashMap map, Object value) {
        for (Object o : map.keySet()) {
            if (map.get(o).equals(value)) {
                return String.valueOf(o);
            }
        }
        return null;
    }

    public void onTabChange(final TabChangeEvent event) {
        TabView tv = (TabView) event.getComponent();
        this.activeTabIndex = tv.getActiveIndex();
    }

    public String doIndex() {
        return "/privado/index?faces-redirect=true";
    }

    public void doCreate() {
        try {
            objeto = (T) getClazz().newInstance();
            telaGrid = false;
            titulo = "Cadastrar";
            activeTabIndex = 0;
        } catch (InstantiationException | IllegalAccessException e) {
            Mensagem.addErrorMessage("Ocorreu um erro ao iniciar a inclusão do registro!", e);
        }

    }

    public void doEdit() {
        objeto = objetoSelecionado;
        titulo = "Alteração";
        telaGrid = false;
        activeTabIndex = 0;
    }

    public void salvar() {
        salvar(null);

    }

    public void salvar(String mensagem) {
        try {
            objeto = dao.atualizar (objeto);
            Mensagem.addInfoMessage(mensagem != null ? mensagem : "Registro salvo com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao salvar o registro!", e);
        }
    }

    public void remover() {
        try {
            Integer idObjeto = null;
            if (objetoSelecionado != null) {
                idObjeto = (Integer) getClazz().getMethod("getId").invoke(objetoSelecionado);
            }
            if (objetoSelecionado == null || idObjeto == null) {
                Mensagem.addWarnMessage("Nenhum registro selecionado!");
            } else {
                dao.excluir(objetoSelecionado, idObjeto);
                Mensagem.addInfoMessage("Registro excluído com sucesso!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao excluir o registro!", e);
        }
    }

    @Produces
    @UsuarioLogado
    public Usuario getUsuarioLogado() {
        UsuarioSistema user = null;

        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();

        if (usuario == null && auth != null && auth.getPrincipal() != null) {
            user = (UsuarioSistema) auth.getPrincipal();
            usuario = user.getUsuario();
        }

        return usuario;
    }

    public Empresa getEmpresaUsuario() {
        empresa = null;

        getUsuarioLogado().getColaborador().getPessoa().getListaEmpresa().stream().forEach((e) -> {
            empresa = e;
        });
        return empresa;
    }

    public boolean podeConsultar() {
        // return false;
        boolean teste = FacesUtil.isUserInRole(getFuncaoBase() + "_CONSULTA")
                || FacesUtil.isUserInRole("ADMIN");
        return teste;
    }

    public boolean podeInserir() {
        return FacesUtil.isUserInRole(getFuncaoBase() + "_INSERE") || FacesUtil.isUserInRole("ADMIN");
    }

    public boolean podeAlterar() {
        return FacesUtil.isUserInRole(getFuncaoBase() + "_ALTERA") || FacesUtil.isUserInRole("ADMIN");
    }

    public boolean podeExcluir() {
        return FacesUtil.isUserInRole(getFuncaoBase() + "_EXCLUI") || FacesUtil.isUserInRole("ADMIN");
    }

    public T getObjetoSelecionado() {
        return objetoSelecionado;
    }

    public void setObjetoSelecionado(T objetoSelecionado) {
        this.objetoSelecionado = objetoSelecionado;
    }

    public T getObjeto() {
        return objeto;
    }

    public void setObjeto(T objeto) {
        this.objeto = objeto;
    }

    public ERPLazyDataModel<T> getDataModel() {
        return dataModel;
    }

    public void setDataModel(ERPLazyDataModel<T> dataModel) {
        this.dataModel = dataModel;
    }

    public boolean isTelaGrid() {
        return telaGrid;
    }

    public void setTelaGrid(boolean telaGrid) {
        this.telaGrid = telaGrid;
    }

    public Repository<T> getDao() {
        return dao;
    }

    public void setDao(Repository<T> dao) {
        this.dao = dao;
    }

    public String getTitulo() {
        return StringUtils.isEmpty(titulo) ? "Consultar" : titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getActiveTabIndex() {
        return activeTabIndex;
    }

    public void setActiveTabIndex(int activeTabIndex) {
        this.activeTabIndex = activeTabIndex;
    }

    public Map<String, String> getSimNao() {
        return simNao;
    }

    public Map<String, String> getTipoSindicato() {
        return tipoSindicato;
    }

    public Map<String, String> getProdutoClasse() {
        return produtoClasse;
    }

    public Map<String, String> getTipoItemSped() {
        return tipoItemSped;
    }

    public Map<String, String> getProdutoTipo() {
        return produtoTipo;
    }

    public Map<String, String> getProdutoIat() {
        return produtoIat;
    }

    public Map<String, String> getProdutoIppt() {
        return produtoIppt;
    }

    public Map<String, String> getClienteIndicadorPreco() {
        return clienteIndicadorPreco;
    }

    public Map<String, String> getClienteTipoFrete() {
        return clienteTipoFrete;
    }

    public Map<String, String> getClienteFormaDesconto() {
        return clienteFormaDesconto;
    }

    public Map<String, String> getSexo() {
        return sexo;
    }

    public Map<String, String> getTipoSangue() {
        return tipoSangue;
    }

    public Map<String, String> getRacaCor() {
        return racaCor;
    }

    public Map<String, String> getCrt() {
        return crt;
    }

    public Map<String, String> getTipoRegimeEmpresa() {
        return tipoRegimeEmpresa;
    }

    public Map<String, Integer> getTipoTelefone() {
        return tipoTelefone;
    }

    public Map<String, String> getTipoPessoa() {
        return tipoPessoa;
    }

    public Map<String, String> getFornecedorLocalizacao() {
        return fornecedorLocalizacao;
    }

    public Map<String, String> getColaboradorFormaPagamento() {
        return colaboradorFormaPagamento;
    }

    public void setColaboradorFormaPagamento(Map<String, String> colaboradorFormaPagamento) {
        this.colaboradorFormaPagamento = colaboradorFormaPagamento;
    }

    public Map<String, String> getTalonarioChequeStatus() {
        return talonarioChequeStatus;
    }

    public Map<String, String> getChequeStatus() {
        return chequeStatus;
    }

    public Map<String, String> getTipoContaCaixa() {
        return tipoContaCaixa;
    }

    public Map<String, String> getTipoFeriado() {
        return tipoFeriado;
    }

    public Map<String, String> getFeriadoAbrangencia() {
        return feriadoAbrangencia;
    }

    public Map<String, String> getNaoSim() {
        return naoSim;
    }

    public Map<String, String> getRequisicaoInternaSituacao() {
        return requisicaoInternaSituacao;
    }

    public Map<String, String> getTipoReajuste() {
        return tipoReajuste;
    }

    public Map<String, Integer> getOrigemMercadoriaNfe() {
        return origemMercadoriaNfe;
    }

    public Map<String, String> getCodigoModeloNfe() {
        return codigoModeloNfe;
    }

    public Map<String, Integer> getLocalDestinoNfe() {
        return localDestinoNfe;
    }

    public Map<String, Integer> getConsumidorOperacaoNfe() {
        return consumidorOperacaoNfe;
    }

    public Map<String, String> getConsumidorPresencaNfe() {
        return consumidorPresencaNfe;
    }

    public Map<String, Integer> getTipoOperacaoNfe() {
        return tipoOperacaoNfe;
    }

    public Map<String, Integer> getTipoEmissaoNfe() {
        return tipoEmissaoNfe;
    }

    public Map<String, Integer> getFinalidadeEmissaoNfe() {
        return finalidadeEmissaoNfe;
    }

    public Map<String, Integer> getFormatoImpressaoDanfeNfe() {
        return formatoImpressaoDanfeNfe;
    }

    public Map<String, Integer> getModalidadeFreteNfe() {
        return modalidadeFreteNfe;
    }

    public Map<String, Integer> getStatusNfe() {
        return statusNfe;
    }

    public Map<String, String> getTipoNaturazaFinanceira() {
        return tipoNaturazaFinanceira;
    }

    public Map<String, String> getLayoutRemessa() {
        return layoutRemessa;
    }

    public Map<String, String> getEspecieCobranca() {
        return especieCobranca;
    }

    public Map<String, String> getTipoBaixa() {
        return tipoBaixa;
    }
}
