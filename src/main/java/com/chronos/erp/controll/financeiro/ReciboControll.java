package com.chronos.erp.controll.financeiro;

import com.chronos.erp.modelo.entidades.Cliente;
import com.chronos.erp.modelo.entidades.Empresa;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.util.CurrencyWriter;
import com.chronos.erp.util.jsf.FacesUtil;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

@Named
@ViewScoped
public class ReciboControll implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<Cliente> clienteRepository;

    @NotNull
    private BigDecimal valor;
    private Cliente cliente;
    private String pagador;
    @NotNull
    private String referente;
    @NotNull
    private String nome;
    private String cpfCnpj;
    @NotNull
    private Date data = new Date();
    private String dataFormatada;
    private String local;
    private Empresa empresa;

    private boolean telaRecibo;


    @PostConstruct
    public void init() {
        iniciarValores();
    }


    public void confirmar() {
        gerarData();
        telaRecibo = false;
    }

    public void voltar() {
        telaRecibo = true;
    }

    public List<Cliente> getListaCliente(String nome) {
        List<Cliente> listaCliente = new ArrayList<>();
        try {
            listaCliente = clienteRepository.getEntitys(Cliente.class, "pessoa.nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaCliente;
    }


    private void iniciarValores() {
        data = new Date();
        cliente = null;
        pagador = "";
        valor = BigDecimal.ZERO;
        referente = "";
        local = "";
        cpfCnpj = "";
        telaRecibo = true;
        empresa = FacesUtil.getEmpresaUsuario();
    }

    private void gerarData() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        dataFormatada = cal.get(Calendar.DAY_OF_MONTH) + " de ";
        dataFormatada += cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + " de ";
        dataFormatada += cal.get(Calendar.YEAR);
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getReferente() {
        return referente;
    }

    public void setReferente(String referente) {
        this.referente = referente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getPagador() {
        return pagador;
    }

    public void setPagador(String pagador) {
        this.pagador = pagador;
    }

    public boolean isTelaRecibo() {
        return telaRecibo;
    }

    public void setTelaRecibo(boolean telaRecibo) {
        this.telaRecibo = telaRecibo;
    }

    public String getPagadorInformado() {
        return cliente != null ? cliente.getPessoa().getNome() : pagador;
    }

    public String getDataFormatada() {
        return dataFormatada;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public String getValorPorExtenso() {
        return CurrencyWriter.getInstance().write(Optional.ofNullable(valor).orElse(BigDecimal.ZERO));
    }
}
