package com.chronos.erp.controll.pdv;

import com.chronos.erp.controll.ERPLazyDataModel;
import com.chronos.erp.dto.MapDTO;
import com.chronos.erp.dto.UsuarioDTO;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.service.financeiro.MovimentoService;
import com.chronos.erp.util.jsf.FacesUtil;
import com.chronos.erp.util.jsf.Mensagem;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by john on 19/01/18.
 */
@Named
@ViewScoped
public class PdvMovimentoControll implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<PdvMovimento> repository;
    @Inject
    private Repository<PdvTurno> pdvTurnoRepository;
    @Inject
    private Repository<PdvCaixa> caixaRepository;
    @Inject
    private Repository<PdvOperador> operadorRepository;
    @Inject
    private MovimentoService service;

    @Inject
    private Repository<PdvSuprimento> pdvSuprimentoRepository;
    @Inject
    private Repository<PdvSangria> pdvSangriaRepository;


    private ERPLazyDataModel<PdvMovimento> dataModel;

    private PdvMovimento movimento;
    private PdvOperador operador;
    private PdvTurno turno;
    private PdvCaixa caixa;
    private List<PdvTurno> turnos;
    private List<PdvCaixa> Caixas;

    private BigDecimal suprimentos;
    private BigDecimal sangria;
    private BigDecimal acrescimo;

    private String userGerente;
    private String senhaGerente;
    private String userOperador;
    private String senhaOperador;

    private boolean telaGrid;
    private boolean telaAbertura;
    private boolean telaFechamento;
    private boolean temMovimento;
    private boolean temConfiguracao;

    private String status;
    private Empresa empresa;
    private PdvMovimento movimentoAberto;
    private Date dataInicial, dataFinal;
    private int idoperador;
    private int idcaixa;

    private Map<String, String> statusDomain;
    private Map<String, Integer> operadorDomain;
    private Map<String, Integer> caixaDomain;
    private Map<String, Integer> tiposLancamento;

    private UsuarioDTO usuario;

    private PdvMovimento movimentoDetalhado;
    private List<MapDTO> formasPagamento;
    private String obsLancamento;
    @NotNull
    @DecimalMin(value = "0.01", message = "O valor  do lançamento deve ser maior que R$0,01")
    private BigDecimal valorLancamento;
    private int tipoLancamento;


    public PdvMovimentoControll() {
    }

    @PostConstruct
    private void init() {
        telaGrid = true;

        try {
            empresa = FacesUtil.getEmpresaUsuario();
            movimentoAberto = FacesUtil.getMovimento();
            temConfiguracao = service.verificarConfPdv(empresa);
            usuario = FacesUtil.getUsuarioSessao();
            idoperador = usuario != null && usuario.getOperador() != null ? usuario.getOperador().getId() : idoperador;
            tiposLancamento = new HashMap<>();
            tiposLancamento.put("Suprimento", 1);
            tiposLancamento.put("Sangria", 2);
            if (temConfiguracao) {
                iniciarObjetos();
            } else {
                Mensagem.addInfoMessage("É preciso informar as configuracoes do PDV");
            }
            formasPagamento = new ArrayList<>();
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro ao buscar os movimentos");
            }

        }
    }

    public ERPLazyDataModel<PdvMovimento> getDataModel() {

        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(PdvMovimento.class);
            dataModel.setDao(repository);
        }
        Object[] atributos;
        atributos = new Object[]{"idGerenteSupervisor", "dataAbertura", "horaAbertura", "dataFechamento", "horaFechamento", "totalSuprimento", "totalSangria", "totalVenda", "totalDesconto", "totalAcrescimo", "totalFinal", "totalRecebido", "totalTroco", "totalCancelado", "statusMovimento", "empresa.id", "pdvCaixa.codigo"};
        dataModel.setAtributos(atributos);

        pesquisar();

        return dataModel;
    }

    public void pesquisar() {
        dataModel.getFiltros().clear();
        dataModel.getFiltros().add(new Filtro("empresa.id", empresa.getId()));
        if (!StringUtils.isEmpty(status)) {
            dataModel.getFiltros().add(new Filtro("statusMovimento", status));
        }

        if (dataInicial != null) {
            if (status.equals("F")) {
                dataModel.getFiltros().add(new Filtro("dataFechamento", Filtro.MAIOR_OU_IGUAL, dataInicial));
            } else {
                dataModel.getFiltros().add(new Filtro("dataAbertura", Filtro.MAIOR_OU_IGUAL, dataInicial));
            }
        }

        if (dataFinal != null) {
            if (status.equals("F")) {
                dataModel.getFiltros().add(new Filtro("dataFechamento", Filtro.MENOR_OU_IGUAL, dataFinal));
            } else {
                dataModel.getFiltros().add(new Filtro("dataAbertura", Filtro.MENOR_OU_IGUAL, dataFinal));
            }
        }

        if (idcaixa > 0) {
            dataModel.getFiltros().add(new Filtro("pdvCaixa.id", idcaixa));
        }

        if (usuario.getAdministrador().equals("N")) {
            int id = usuario.getOperador() != null ? usuario.getOperador().getId() : 0;
            dataModel.getFiltros().add(new Filtro("pdvOperador.id", id));
        } else {
            if (idoperador > 0) {
                dataModel.getFiltros().add(new Filtro("pdvOperador.id", idoperador));
            }
        }

    }

    public void detalheMovimentos() {
        List<Filtro> filtros = new ArrayList<>();

        filtros.add(new Filtro("empresa.id", empresa.getId()));
        if (!StringUtils.isEmpty(status)) {
            filtros.add(new Filtro("statusMovimento", Filtro.IGUAL, status));
        }

        if (dataInicial != null) {
            dataInicial = DateUtils.truncate(dataInicial, Calendar.DATE);
            if (status.equals("F")) {
                filtros.add(new Filtro("dataFechamento", Filtro.MAIOR_OU_IGUAL, dataInicial));
            } else {
                filtros.add(new Filtro("dataAbertura", Filtro.MAIOR_OU_IGUAL, dataInicial));
            }
        }

        if (dataFinal != null) {
            dataFinal = DateUtils.addSeconds(DateUtils.addMinutes(DateUtils.addHours(dataFinal, 23), 59), 59);
            if (status.equals("F")) {
                filtros.add(new Filtro("dataFechamento", Filtro.MENOR_OU_IGUAL, dataFinal));
            } else {
                filtros.add(new Filtro("dataAbertura", Filtro.MENOR_OU_IGUAL, dataFinal));
            }
        }

        if (idcaixa > 0) {
            filtros.add(new Filtro("pdvCaixa.id", idcaixa));
        }

        if (idoperador > 0) {
            filtros.add(new Filtro("pdvOperador.id", idoperador));
        }

        List<PdvMovimento> movimentos = repository.getEntitys(PdvMovimento.class, filtros);
        movimentoDetalhado = new PdvMovimento();
        int[] ids = new int[movimentos.size()];

        int i = 0;
        for (PdvMovimento m : movimentos) {
            movimentoDetalhado.setTotalRecebido(movimentoDetalhado.getTotalRecebido().add(m.getTotalRecebido()));
            movimentoDetalhado.setTotalVenda(movimentoDetalhado.getTotalVenda().add(m.getTotalVenda()));
            movimentoDetalhado.setTotalSangria(movimentoDetalhado.getTotalSangria().add(m.getTotalSangria()));
            movimentoDetalhado.setTotalDesconto(movimentoDetalhado.getTotalDesconto().add(m.getTotalDesconto()));
            movimentoDetalhado.setTotalSuprimento(movimentoDetalhado.getTotalSuprimento().add(m.getTotalSuprimento()));
            movimentoDetalhado.setTotalAcrescimo(movimentoDetalhado.getTotalAcrescimo().add(m.getTotalAcrescimo()));
            movimentoDetalhado.setTotalTroco(movimentoDetalhado.getTotalTroco().add(m.getTotalTroco()));
            movimentoDetalhado.setTotalCancelado(movimentoDetalhado.getTotalCancelado().add(m.getTotalCancelado()));
            movimentoDetalhado.setTotalFinal(movimentoDetalhado.getTotalFinal().add(m.getTotalFinal()));
            ids[i] = m.getId();
            i++;
        }
        if (ids.length > 0) {
            String jpql = "SELECT new com.chronos.erp.dto.MapDTO(t.descricao,sum(f.valor)) from PdvFormaPagamento f " +
                    "inner join f.pdvVendaCabecalho v " +
                    "inner join v.pdvMovimento m " +
                    "inner join f.tipoPagamento t " +
                    "where m.id in :ids " +
                    "group by t.descricao";
            formasPagamento = repository.executeQuery(MapDTO.class, jpql, ids);

            jpql = "SELECT new com.chronos.erp.dto.MapDTO('Dinheiro',sum(s.valor *-1)) from PdvSangria s " +
                    "inner join s.pdvMovimento m " +
                    "where m.id in :ids ";
            List<MapDTO> sangrias = repository.executeQuery(MapDTO.class, jpql, ids);

            jpql = "SELECT new com.chronos.erp.dto.MapDTO('Dinheiro',sum(s.valor)) from PdvSuprimento s " +
                    "inner join s.pdvMovimento m " +
                    "where m.id in :ids ";
            List<MapDTO> suprimentos = repository.executeQuery(MapDTO.class, jpql, ids);

            BigDecimal totalSangria = sangrias.stream().map(s -> Optional.ofNullable(s.getValor()).orElse(BigDecimal.ZERO)).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            BigDecimal totalSuprimentos = suprimentos.stream().map(s -> Optional.ofNullable(s.getValor()).orElse(BigDecimal.ZERO)).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);

            BigDecimal total = totalSuprimentos.add(totalSangria);

            Optional<MapDTO> dinheiro = formasPagamento.stream().filter(p -> p.getDescricao().equals("Dinheiro")).findFirst();

            if (dinheiro.isPresent()) {
                dinheiro.get().setValor(dinheiro.get().getValor().add(total));
            } else {
                formasPagamento.add(new MapDTO("Dinheiro", total));
            }

        }
    }


    public void iniciarMovimento() {
        telaGrid = false;
        movimento = new PdvMovimento();
        telaAbertura = true;
        telaFechamento = false;
    }

    public void iniciarFechamento() {
        telaAbertura = false;
        telaFechamento = true;
        telaGrid = false;
    }

    public void confimarMovimento() {

        try {
            service.iniciarMovimento(empresa, suprimentos, turno, caixa);
            telaGrid = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }
    }

    public void confirmarFechamento() {
        try {
            service.realizarFechamento();
            telaGrid = true;
            movimento = null;
            Mensagem.addInfoMessage("Caixa fechado com sucesso");
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }
    }

    public void lancarMovimento() {
        service.lancarMovimento(tipoLancamento, valorLancamento, obsLancamento);
    }


    private void iniciarObjetos() throws ChronosException {

        movimento = service.verificarMovimento(empresa);
        turnos = pdvTurnoRepository.getAll(PdvTurno.class);
        Caixas = caixaRepository.getEntitys(PdvCaixa.class, new Object[]{"codigo", "nome"});

        caixaDomain = new LinkedHashMap<>();
        caixaDomain.put("Todos", 0);
        caixaDomain.putAll(Caixas.stream()
                .collect(Collectors.toMap(PdvCaixa::getNome, PdvCaixa::getId)));

        operadorDomain = new LinkedHashMap<>();
        operadorDomain.put("Todos", 0);
        operadorDomain.putAll(operadorRepository.getEntitys(PdvOperador.class, new Object[]{"login"}).stream()
                .collect(Collectors.toMap(PdvOperador::getLogin, PdvOperador::getId)));

        empresa = FacesUtil.getEmpresaUsuario();
        statusDomain = new LinkedHashMap<>();
        statusDomain.put("Todos", null);
        statusDomain.put("Aberto", "A");
        statusDomain.put("Fechado", "F");
    }

    public PdvMovimento getMovimento() {
        return movimento;
    }

    public void setMovimento(PdvMovimento movimento) {
        this.movimento = movimento;
    }

    public BigDecimal getSuprimentos() {
        return suprimentos;
    }

    public void setSuprimentos(BigDecimal suprimentos) {
        this.suprimentos = suprimentos;
    }

    public BigDecimal getSangria() {
        return sangria;
    }

    public void setSangria(BigDecimal sangria) {
        this.sangria = sangria;
    }

    public BigDecimal getAcrescimo() {
        return acrescimo;
    }

    public void setAcrescimo(BigDecimal acrescimo) {
        this.acrescimo = acrescimo;
    }

    public PdvTurno getTurno() {
        return turno;
    }

    public void setTurno(PdvTurno turno) {
        this.turno = turno;
    }

    public List<PdvTurno> getTurnos() {
        return turnos;
    }

    public void setTurnos(List<PdvTurno> turnos) {
        this.turnos = turnos;
    }

    public PdvOperador getOperador() {
        return operador;
    }

    public void setOperador(PdvOperador operador) {
        this.operador = operador;
    }

    public String getUserGerente() {
        return userGerente;
    }

    public void setUserGerente(String userGerente) {
        this.userGerente = userGerente;
    }

    public String getSenhaGerente() {
        return senhaGerente;
    }

    public void setSenhaGerente(String senhaGerente) {
        this.senhaGerente = senhaGerente;
    }

    public String getUserOperador() {
        return userOperador;
    }

    public void setUserOperador(String userOperador) {
        this.userOperador = userOperador;
    }

    public String getSenhaOperador() {
        return senhaOperador;
    }

    public void setSenhaOperador(String senhaOperador) {
        this.senhaOperador = senhaOperador;
    }

    public boolean isTelaGrid() {
        return telaGrid;
    }

    public boolean isTelaAbertura() {
        return telaAbertura;
    }

    public boolean isTelaFechamento() {
        return telaFechamento;
    }

    public boolean isTemMovimento() {
        return movimento != null;
    }

    public boolean isTemConfiguracao() {
        return temConfiguracao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public int getIdoperador() {
        return idoperador;
    }

    public void setIdoperador(int idoperador) {
        this.idoperador = idoperador;
    }

    public Map<String, String> getStatusDomain() {
        return statusDomain;
    }

    public void setStatusDomain(Map<String, String> statusDomain) {
        this.statusDomain = statusDomain;
    }

    public Map<String, Integer> getOperadorDomain() {
        return operadorDomain;
    }

    public void setOperadorDomain(Map<String, Integer> operadorDomain) {
        this.operadorDomain = operadorDomain;
    }

    public Map<String, Integer> getCaixaDomain() {
        return caixaDomain;
    }

    public void setCaixaDomain(Map<String, Integer> caixaDomain) {
        this.caixaDomain = caixaDomain;
    }

    public int getIdcaixa() {
        return idcaixa;
    }

    public void setIdcaixa(int idcaixa) {
        this.idcaixa = idcaixa;
    }

    public PdvCaixa getCaixa() {
        return caixa;
    }

    public void setCaixa(PdvCaixa caixa) {
        this.caixa = caixa;
    }

    public List<PdvCaixa> getCaixas() {
        return Caixas;
    }

    public void setCaixas(List<PdvCaixa> caixas) {
        Caixas = caixas;
    }

    public PdvMovimento getMovimentoDetalhado() {
        return movimentoDetalhado;
    }

    public List<MapDTO> getFormasPagamento() {
        return formasPagamento;
    }

    public String getObsLancamento() {
        return obsLancamento;
    }

    public void setObsLancamento(String obsLancamento) {
        this.obsLancamento = obsLancamento;
    }

    public BigDecimal getValorLancamento() {
        return valorLancamento;
    }

    public void setValorLancamento(BigDecimal valorLancamento) {
        this.valorLancamento = valorLancamento;
    }

    public int getTipoLancamento() {
        return tipoLancamento;
    }

    public void setTipoLancamento(int tipoLancamento) {
        this.tipoLancamento = tipoLancamento;
    }

    public Boolean isExisteMovimento() {
        return movimentoAberto != null;
    }

    public Map<String, Integer> getTiposLancamento() {
        return tiposLancamento;
    }
}
