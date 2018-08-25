package com.chronos.bo.cadastro;

import com.chronos.modelo.entidades.Produto;
import com.chronos.service.ChronosException;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Optional;

public class ItemFilizola {

    private Produto produto;


    /**
     * codigo do produto com tamanho 6 + P de Pesado, descrição do produto com tamanho 22, mais preço do produto com tamanho 7  000 que representa a validade
     *
     * @return
     * @throws ChronosException
     */
    public String montarItem() throws ChronosException {
        String linha = montarCodigo() + "P" + montarDescricao() + montarPreco() + "000";
        return linha;

    }

    public void setProduto(Produto produto) {
        this.produto = produto;
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

    private String completarComZeroDireita(int tamanho, String valor) {
        String retorno = "";
        retorno = StringUtils.leftPad(valor, tamanho, "0");
        return retorno;
    }

    private String montarDescricao() {
        String novaDescricao;
        if (this.produto.getNome().length() > 22) {
            novaDescricao = this.produto.getNome().substring(0, 21);
            return completarComEspacoDireita(22, novaDescricao);
        } else {
            return completarComEspacoDireita(22, this.produto.getNome());
        }
    }

    private String completarComEspacoDireita(int tamanho, String valor) {
        String retorno = null;
        retorno = StringUtils.rightPad(valor, tamanho, " ");
        return retorno;
    }

    private String montarPreco() throws ChronosException {
        BigDecimal valor = Optional.ofNullable(this.produto.getValorVenda()).orElse(BigDecimal.ZERO);
        if (valor.compareTo(BigDecimal.valueOf(99999.99)) > 0) {
            throw new ChronosException("o Valor do produto " + produto.getNome() + " não pode ser superior a R$ 99.999,99");
        }

        int pre = (int) valor.doubleValue();
        double pos = (valor.doubleValue() - pre) * 100;
        String precoConvertido = "" + pre + (int) pos;

        String novoPreco;
        if (precoConvertido.length() > 7) {
            novoPreco = precoConvertido.substring(0, 6);
            return completarComZeroDireita(7, novoPreco);
        } else {
            return completarComZeroDireita(7, precoConvertido);
        }

    }
}
