
package com.chronos.modelo.entidades;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "CONTA_CAIXA")
public class ContaCaixa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CODIGO")
    private String codigo;
    @Column(name = "DIGITO")
    private String digito;
    @Column(name = "NOME")
    @NotNull
    @NotEmpty
    private String nome;
    @Column(name = "DESCRICAO")
    private String descricao;
    @Column(name = "TIPO")
    @NotNull
    @NotEmpty
    private String tipo;
    @Column(name = "LIMITE_CREDITO")
    @DecimalMax(value = "9999999.99", message = "O limite de credito deve ser menor que R$9.999.999,99")
    private BigDecimal limiteCredito;
    @Column(name = "CLASSIFICACAO_CONTABIL_CONTA")
    private String classificacaoContabilConta;
    @Column(name = "TAXA_MULTA")
    @DecimalMax(value = "100.0", message = "O percentual de multa deve ser menor que 100")
    private BigDecimal taxaMulta;
    @Column(name = "TAXA_JURO")
    @DecimalMax(value = "100.0", message = "O percentual de juros deve ser menor que 100")
    private BigDecimal taxaJuro;
    @Column(name = "DESCONTO_MAXIMO_PERMITIDO")
    @DecimalMax(value = "100.0", message = "O desconto maximo deve ser menor que 100")
    private BigDecimal descontoMaximoPermitido;
    @Column(name = "LIMITE_COBRANCA_JURO")
    @DecimalMax(value = "100.0", message = "O limite de cobrança deve ser menor que 100")
    private Integer limiteCobrancaJuro;
    @JoinColumn(name = "ID_AGENCIA_BANCO", referencedColumnName = "ID")
    @ManyToOne   
    private AgenciaBanco agenciaBanco;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private Empresa empresa;
  

    public ContaCaixa() {
    }

    public ContaCaixa(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }
    
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDigito() {
        return digito;
    }

    public void setDigito(String digito) {
        this.digito = digito;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getLimiteCredito() {
        return limiteCredito;
    }

    public void setLimiteCredito(BigDecimal limiteCredito) {
        this.limiteCredito = limiteCredito;
    }

    public AgenciaBanco getAgenciaBanco() {
        return agenciaBanco;
    }

    public void setAgenciaBanco(AgenciaBanco agenciaBanco) {
        this.agenciaBanco = agenciaBanco;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getClassificacaoContabilConta() {
        return classificacaoContabilConta;
    }

    public void setClassificacaoContabilConta(String classificacaoContabilConta) {
        this.classificacaoContabilConta = classificacaoContabilConta;
    }

    public BigDecimal getTaxaMulta() {
        return taxaMulta;
    }

    public void setTaxaMulta(BigDecimal taxaMulta) {
        this.taxaMulta = taxaMulta;
    }

    public BigDecimal getTaxaJuro() {
        return taxaJuro;
    }

    public void setTaxaJuro(BigDecimal taxaJuro) {
        this.taxaJuro = taxaJuro;
    }

    public BigDecimal getDescontoMaximoPermitido() {
        return descontoMaximoPermitido;
    }

    public void setDescontoMaximoPermitido(BigDecimal descontoMaximoPermitido) {
        this.descontoMaximoPermitido = descontoMaximoPermitido;
    }

    public Integer getLimiteCobrancaJuro() {
        return limiteCobrancaJuro;
    }

    public void setLimiteCobrancaJuro(Integer limiteCobrancaJuro) {
        this.limiteCobrancaJuro = limiteCobrancaJuro;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.id);
        hash = 11 * hash + Objects.hashCode(this.codigo);
        hash = 11 * hash + Objects.hashCode(this.digito);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ContaCaixa other = (ContaCaixa) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.codigo, other.codigo)) {
            return false;
        }
        if (!Objects.equals(this.digito, other.digito)) {
            return false;
        }
        return true;
    }

    
    
    @Override
    public String toString() {
        return nome;
    }

}
