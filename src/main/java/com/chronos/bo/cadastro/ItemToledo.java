package com.chronos.bo.cadastro;

import com.chronos.modelo.entidades.Produto;
import com.chronos.service.ChronosException;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Optional;

public class ItemToledo {

    private Produto produto;
    private DecimalFormatSymbols simboloDecimal = DecimalFormatSymbols.getInstance();
    private DecimalFormat formatoValor = new DecimalFormat("0000.##", simboloDecimal);

    public ItemToledo() {
        simboloDecimal.setDecimalSeparator('.');
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    private String completarComZeroDireita(int tamanho, String valor) {
        String retorno = "";
        retorno = StringUtils.leftPad(valor, tamanho, "0");
        return retorno;
    }

    private String completarComEspacoDireita(int tamanho, String valor) {
        String retorno = null;
        retorno = StringUtils.rightPad(valor, tamanho, " ");
        return retorno;
    }

    private String montarCodigo() {
        String novoCodigo;
        if (this.produto.getCodigoBalanca().toString().length() > 6) {
            novoCodigo = this.produto.getCodigoBalanca().toString().substring(0, 5);
            return completarComZeroDireita(6, novoCodigo);
        } else {
            return completarComZeroDireita(6, this.produto.getCodigoBalanca().toString());
        }
    }

    private String montarDescricao() {
        String novaDescricao;
        if (this.produto.getNome().length() > 50) {
            novaDescricao = this.produto.getNome().substring(0, 49);
            return completarComEspacoDireita(50, novaDescricao);
        } else {
            return completarComEspacoDireita(50, this.produto.getNome());
        }
    }

    private String montarPreco() throws ChronosException {
        BigDecimal valor = Optional.ofNullable(this.produto.getValorVenda()).orElse(BigDecimal.ZERO);
        if (valor.compareTo(BigDecimal.valueOf(9999.99)) > 0) {
            throw new ChronosException("o Valor do produto " + produto.getNome() + " não pode ser superior a R$ 9.999,99");
        }

        int pre = (int) valor.doubleValue();
        double pos = ((valor.subtract(BigDecimal.valueOf(pre))).multiply(BigDecimal.valueOf(100))).doubleValue();
        String precoConvertido = "" + pre + completarComZeroDireita(2, ((int) pos) + "");


        String novoPreco;
        if (precoConvertido.length() > 6) {
            novoPreco = precoConvertido.substring(0, 5);
            return completarComZeroDireita(6, novoPreco);
        } else {
            return completarComZeroDireita(6, precoConvertido);
        }

    }

    public String format(double value) {
        int pre = (int) value;
        double pos = (value - pre) * 100;
        String result = "" + pre + (int) pos;
        if (result.length() <= 6) {

            while (result.length() < 6) {
                result = "0" + result;
            }
            return result;
        } else {
            return result.substring(0, 6);
        }
    }

    public String montarItem() throws ChronosException {
        String linha = "010" + montarCodigo() + montarPreco() + "000" + montarDescricao() + completarComZeroDireita(88, "");
        return linha;

    }

    public String montarItem2() throws ChronosException {
        String linha = "010" + montarCodigo() + montarPreco() + "000" + montarDescricao() + completarComZeroDireita(88, "") + "\r\n";
        return linha;

    }
}
