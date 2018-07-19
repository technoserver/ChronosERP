package com.chronos.bo.cadastro;

import com.chronos.modelo.entidades.Produto;
import org.apache.commons.lang3.StringUtils;

public class ItemToledo {

    private Produto produto;

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
        if (this.produto.getDescricao().length() > 50) {
            novaDescricao = this.produto.getNome().substring(0, 49);
            return completarComEspacoDireita(50, novaDescricao);
        } else {
            return completarComEspacoDireita(50, this.produto.getNome());
        }
    }

    private String montarPreco() {
        String precoConvertido = this.produto.getValorVenda().toString().replace(".", "");
        String novoPreco;
        if (precoConvertido.length() > 6) {
            novoPreco = precoConvertido.substring(0, 5);
            return completarComZeroDireita(6, novoPreco);
        } else {
            return completarComZeroDireita(6, precoConvertido);
        }
    }

    public String montarItem() {
        String linha = "010" + montarCodigo() + montarPreco() + "000" + montarDescricao() + completarComZeroDireita(88, "") + "\r\n";
        return linha;

    }
}
