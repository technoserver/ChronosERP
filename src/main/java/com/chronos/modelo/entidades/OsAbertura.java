package com.chronos.modelo.entidades;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

@Entity
@Table(name = "OS_ABERTURA")
@DynamicUpdate
public class OsAbertura implements Serializable {

    private static final long serialVersionUID = 2L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "ID_NFE_CABECALHO")
    private Integer idnfeCabecalho;
    @Column(name = "NUMERO")
    private String numero;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_INICIO")
    private Date dataInicio;
    @Column(name = "HORA_INICIO")
    private String horaInicio;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_PREVISAO")
    private Date dataPrevisao;
    @Column(name = "HORA_PREVISAO")
    private String horaPrevisao;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_FIM")
    private Date dataFim;
    @Column(name = "HORA_FIM")
    private String horaFim;
    @Column(name = "NOME_CONTATO")
    private String nomeContato;
    @Column(name = "FONE_CONTATO")
    private String foneContato;
    @Column(name = "OBSERVACAO_CLIENTE")
    private String observacaoCliente;
    @Column(name = "OBSERVACAO_ABERTURA")
    private String observacaoAbertura;
    @Column(name = "VALOR_TOTAL_PRODUTOS")
    private BigDecimal valorTotalProduto;
    @Column(name = "VALOR_TOTAL_SERVICOS")
    private BigDecimal valorTotalServico;
    @DecimalMin(value = "0.01", message = "O valor  do desconto deve ser maior que R$0,01")
    @DecimalMax(value = "9999999.99", message = "O valor  deve ser menor que R$9.999.999,99")
    @Column(name = "VALOR_TOTAL_DESCONTO")
    private BigDecimal valorTotalDesconto;
    @DecimalMax(value = "9999999.99", message = "O valor  deve ser menor que R$9.999.999,99")
    @Column(name = "VALOR_TOTAL")
    private BigDecimal valorTotal;
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private Cliente cliente;
    @JoinColumn(name = "ID_COLABORADOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private Colaborador colaborador;
    @JoinColumn(name = "ID_OS_STATUS", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private OsStatus osStatus;
    @JoinColumn(name = "ID_VENDA_CONDICOES_PAGAMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private VendaCondicoesPagamento condicoesPagamento;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Empresa empresa;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "osAbertura", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OsEvolucao> listaOsEvolucao;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "osAbertura", cascade = CascadeType.ALL, orphanRemoval = true)     
    private Set<OsProdutoServico> listaOsProdutoServico;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "osAbertura", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OsAberturaEquipamento> listaOsAberturaEquipamento;

    public OsAbertura() {
    }

    public OsAbertura(Integer id, String numero, Date dataInicio, Date dataPrevisao, Date dataFim, BigDecimal valorTotal, int idcliente, String nome, Integer idstatus, String osStatus, Integer idnfeCabecalho) {
        this.id = id;
        this.numero = numero;
        this.dataInicio = dataInicio;
        this.dataPrevisao = dataPrevisao;
        this.dataFim = dataFim;
        this.cliente = new Cliente(idcliente, nome);
        this.osStatus = new OsStatus(idstatus, osStatus);
        this.idnfeCabecalho = idnfeCabecalho;
        this.valorTotal = valorTotal;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdnfeCabecalho() {
        return idnfeCabecalho;
    }

    public void setIdnfeCabecalho(Integer idnfeCabecalho) {
        this.idnfeCabecalho = idnfeCabecalho;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getDataPrevisao() {
        return dataPrevisao;
    }

    public void setDataPrevisao(Date dataPrevisao) {
        this.dataPrevisao = dataPrevisao;
    }

    public String getHoraPrevisao() {
        return horaPrevisao;
    }

    public void setHoraPrevisao(String horaPrevisao) {
        this.horaPrevisao = horaPrevisao;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    public String getNomeContato() {
        return nomeContato;
    }

    public void setNomeContato(String nomeContato) {
        this.nomeContato = nomeContato;
    }

    public String getFoneContato() {
        return foneContato;
    }

    public void setFoneContato(String foneContato) {
        this.foneContato = foneContato;
    }

    public String getObservacaoCliente() {
        return observacaoCliente;
    }

    public void setObservacaoCliente(String observacaoCliente) {
        this.observacaoCliente = observacaoCliente;
    }

    public String getObservacaoAbertura() {
        return observacaoAbertura;
    }

    public void setObservacaoAbertura(String observacaoAbertura) {
        this.observacaoAbertura = observacaoAbertura;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }

    public OsStatus getOsStatus() {
        return osStatus;
    }

    public void setOsStatus(OsStatus osStatus) {
        this.osStatus = osStatus;
    }

  
    public Set<OsEvolucao> getListaOsEvolucao() {
        return listaOsEvolucao;
    }

    public void setListaOsEvolucao(Set<OsEvolucao> listaOsEvolucao) {
        this.listaOsEvolucao = listaOsEvolucao;
    }

    public Set<OsProdutoServico> getListaOsProdutoServico() {
        return Optional.ofNullable(listaOsProdutoServico).orElse(new HashSet<>());
    }

    public void setListaOsProdutoServico(Set<OsProdutoServico> listaOsProdutoServico) {
        this.listaOsProdutoServico = listaOsProdutoServico;
    }

    public Set<OsAberturaEquipamento> getListaOsAberturaEquipamento() {
        return listaOsAberturaEquipamento;
    }

    public void setListaOsAberturaEquipamento(Set<OsAberturaEquipamento> listaOsAberturaEquipamento) {
        this.listaOsAberturaEquipamento = listaOsAberturaEquipamento;
    }


    public BigDecimal getValorTotalProduto() {
        return valorTotalProduto;
    }

    public void setValorTotalProduto(BigDecimal valorTotalProduto) {
        this.valorTotalProduto = valorTotalProduto;
    }

    public BigDecimal getValorTotalServico() {
        return valorTotalServico;
    }

    public void setValorTotalServico(BigDecimal valorTotalServico) {
        this.valorTotalServico = valorTotalServico;
    }

    public BigDecimal getValorTotalDesconto() {
        return valorTotalDesconto;
    }

    public void setValorTotalDesconto(BigDecimal valorTotalDesconto) {
        this.valorTotalDesconto = valorTotalDesconto;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public VendaCondicoesPagamento getCondicoesPagamento() {
        return condicoesPagamento;
    }

    public void setCondicoesPagamento(VendaCondicoesPagamento condicoesPagamento) {
        this.condicoesPagamento = condicoesPagamento;
    }


    private String formatarValor(BigDecimal valor) {
        DecimalFormatSymbols simboloDecimal = DecimalFormatSymbols.getInstance();
        simboloDecimal.setDecimalSeparator('.');
        DecimalFormat formatar = new DecimalFormat("0.00", simboloDecimal);

        return formatar.format(Optional.ofNullable(valor).orElse(BigDecimal.ZERO));
    }

    public BigDecimal calcularValorTotal() {
        BigDecimal valor = getListaOsProdutoServico()
                .stream()
                .map(OsProdutoServico::getValorTotal)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        return valor;
    }

    public BigDecimal calcularValorProduto() {
        BigDecimal valor = getListaOsProdutoServico()
                .stream()
                .filter((p) -> p.getTipo() != null && p.getTipo() == 0)
                .map(OsProdutoServico::getValorTotal)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        return valor;
    }

    public BigDecimal calcularValorServico() {
        BigDecimal valor = getListaOsProdutoServico()
                .stream()
                .filter((p) -> p.getTipo() == 1)
                .map(OsProdutoServico::getValorTotal)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        return valor;
    }

    public BigDecimal calcularTotalDesconto() {
        BigDecimal desconto = getListaOsProdutoServico().stream()
                .map(OsProdutoServico::getValorDesconto)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        valorTotalDesconto = desconto.signum() > 0 ? desconto : valorTotalDesconto;
        return desconto;
    }


    public void calcularValores() {
        this.valorTotalProduto = calcularValorProduto();
        this.valorTotalServico = calcularValorServico();
        this.valorTotal = calcularValorTotal().subtract(calcularTotalDesconto());
    }

    public boolean isNovo() {
        return this.id == null;
    }

    public String valorTotalFormatado() {
        return formatarValor(calcularValorTotal());
    }

    public String valorProdutoFormatado() {
        return formatarValor(calcularValorProduto());
    }

    public String valorServicoFormatado() {
        return formatarValor(calcularValorServico());
    }

    public boolean isFaturado() {
        return this.osStatus.getId() != null && this.osStatus.getId() == 5;
    }

    public boolean isEmitido() {
        return this.osStatus.getId() != null && this.osStatus.getId() == 6;
    }

    public boolean isEmitidoOrFaturado() {
        boolean teste = this.osStatus.getId() == 6 || this.osStatus.getId() == 5;
        return teste;
    }

    public boolean isCancelado() {
        return this.osStatus.getId() != null && this.osStatus.getId() == 7;
    }

    public boolean isPodeExcluir() {
        return (this.osStatus.getId() != 6 && this.osStatus.getId() != 5 && this.osStatus.getId() != 7);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OsAbertura other = (OsAbertura) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "OsAbertura{" + "id=" + id + '}';
    }
    
    

}
