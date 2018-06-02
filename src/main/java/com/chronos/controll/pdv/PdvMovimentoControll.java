package com.chronos.controll.pdv;

import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.PdvCaixa;
import com.chronos.modelo.entidades.PdvMovimento;
import com.chronos.modelo.entidades.PdvOperador;
import com.chronos.modelo.entidades.PdvTurno;
import com.chronos.repository.Repository;
import com.chronos.service.financeiro.MovimentoService;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.jsf.Mensagem;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

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
    private MovimentoService service;

    private ERPLazyDataModel<PdvMovimento> dataModel;

    private PdvMovimento movimento;
    private PdvOperador operador;
    private PdvCaixa caixa;
    private List<PdvCaixa> caixas;
    private PdvTurno turno;
    private List<PdvTurno> turnos;

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


    public PdvMovimentoControll() {
    }

    @PostConstruct
    private void init(){
        telaGrid = true;
        movimento = service.getMovimento();
    }

    public ERPLazyDataModel<PdvMovimento> getDataModel() {

        if(dataModel == null){
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(PdvMovimento.class);
            dataModel.setDao(repository);
        }
        Object[] atributos;
        atributos = new Object[]{"idGerenteSupervisor","dataAbertura","horaAbertura","dataFechamento","horaFechamento","totalSuprimento","totalSangria","totalVenda","totalDesconto","totalAcrescimo","totalFinal","totalRecebido","totalTroco","totalCancelado","statusMovimento","empresa.id"};
        dataModel.setAtributos(atributos);
        return dataModel;
    }


    public void verificarMovimento(){
        movimento = FacesUtil.getMovimento();
        if(movimento==null){

        }else{

        }
    }

    public void iniciarMovimento(){
        telaGrid = false;
        movimento = new PdvMovimento();
        telaAbertura = true;
        telaFechamento = false;
    }

    public void iniciarFechamento(){
        telaAbertura = false;
        telaFechamento = true;
        telaGrid = false;
    }

    public void confimarMovimento() {

        try {
            service.iniciarMovimento(suprimentos);
            telaGrid = true;
            Mensagem.addInfoMessage("Caixa aberto com sucesso");
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }
    }

    public void confirmarFechamento(){
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

    public boolean isTemMovimento(){
        return movimento!=null;
    }
}
