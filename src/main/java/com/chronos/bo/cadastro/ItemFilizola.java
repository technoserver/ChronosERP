package com.chronos.bo.cadastro;

import com.chronos.modelo.entidades.Produto;
import com.chronos.modelo.entidades.TabelaNutricionalCabecalho;
import com.chronos.modelo.entidades.TabelaNutricionalDetalhe;
import com.chronos.modelo.enuns.Nutrientes;
import com.chronos.service.ChronosException;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Optional;

public class ItemFilizola {

    private Produto produto;
    private TabelaNutricionalCabecalho tabelaNutricional;
    private String identificador;


    public ItemFilizola(Produto produto) {
        this.produto = produto;
        this.identificador = produto.getBalanca().getIdentificador();
        this.tabelaNutricional = produto.getTabelaNutricional();
    }

    /**
     * codigo do produto com tamanho 6 + P de Pesado, descrição do produto com tamanho 22, mais preço do produto com tamanho 7  000 que representa a validade
     *
     * @return
     * @throws ChronosException
     */
    public String montarItem() throws ChronosException {
        String linha =
                montarCodigo()
                        + montaIdentificador()
                        + montarDescricao()
                        + montarPreco()
                        + montarValidade()
                        + incluirSeparadorNutricional()
                        + montaDescricaoNutricional()
                        + montarValorNutricional(Nutrientes.VALOR_CALORICO)
                        + montarVD(Nutrientes.VALOR_CALORICO)
                        + montarValorNutricional(Nutrientes.PROTEINA)
                        + montarVD(Nutrientes.PROTEINA)
                        + montarValorNutricional(Nutrientes.GORDURA_TOTAIS)
                        + montarVD(Nutrientes.GORDURA_TOTAIS)
                        + montarValorNutricional(Nutrientes.GORDURA_SATURADA)
                        + montarVD(Nutrientes.GORDURA_SATURADA)
                        + montarValorNutricional(Nutrientes.GORDURA_TRANS)
                        + montarVD(Nutrientes.GORDURA_TRANS)
                        + montarValorNutricional(Nutrientes.FIBRA_ALIMENTAR)
                        + montarVD(Nutrientes.FIBRA_ALIMENTAR)
                        + montarValorNutricional(Nutrientes.CALCIO)
                        + montarVD(Nutrientes.CALCIO)
                        + montarValorNutricional(Nutrientes.FERRO)
                        + montarVD(Nutrientes.FERRO)
                        + montarValorNutricional(Nutrientes.SODIO)
                        + montarVD(Nutrientes.SODIO);
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
        double pos = ((valor.subtract(BigDecimal.valueOf(pre))).multiply(BigDecimal.valueOf(100))).doubleValue();
        String precoConvertido = "" + pre + completarComZeroDireita(2, ((int) pos) + "");

        String novoPreco;

        if (precoConvertido.length() > 7) {
            novoPreco = precoConvertido.substring(0, 6);
            return completarComZeroDireita(7, novoPreco);
        } else {
            return completarComZeroDireita(7, precoConvertido);
        }


    }

    private String montaValor(BigDecimal valor, boolean exibirPos) {
        int pre = (int) valor.doubleValue();
        double pos = ((valor.subtract(BigDecimal.valueOf(pre))).multiply(BigDecimal.valueOf(100))).doubleValue();
        StringBuilder str = new StringBuilder();
        str.append(pre);
        if (exibirPos) {
            str.append(pos);
        }
        return str.toString();
    }

    private String montaIdentificador() throws ChronosException {
        if (!(identificador.equals("Q") || identificador.equals("P"))) {
            throw new ChronosException("Layout para balança Filizola não suporte esse formato");
        }

        return identificador.equals("Q") ? "U" : "P";
    }

    private String montarValidade() {
        String validade = produto.getDiasValidade() == null ? "0" : produto.getDiasValidade().toString();
        return StringUtils.leftPad(validade, 3, "0");
    }

    private String incluirSeparadorNutricional() {
        return tabelaNutricional != null ? "@" : "";
    }

    private String montaDescricaoNutricional() {
        String novaDescricao;

        if (this.tabelaNutricional == null) {
            return "";
        }

        if (this.tabelaNutricional.getNome().length() > 35) {
            novaDescricao = this.tabelaNutricional.getNome().substring(0, 34);
            return completarComEspacoDireita(35, novaDescricao);
        } else {
            return completarComEspacoDireita(35, this.tabelaNutricional.getNome());
        }
    }

    private String montarValorNutricional(Nutrientes nutriente) {
        if (this.tabelaNutricional == null) {
            return "";
        } else {
            BigDecimal valorCalorico = this.tabelaNutricional.getNutrientes()
                    .stream()
                    .filter(n -> n.getNutriente().getId() == nutriente.ordinal())
                    .findFirst()
                    .map(TabelaNutricionalDetalhe::getQuantidade)

                    .orElse(BigDecimal.ZERO);
            String strValorCalorico = StringUtils.leftPad(montaValor(valorCalorico, false), 5, "0");
            return strValorCalorico;
        }
    }

    private String montarVD(Nutrientes nutriente) {
        if (this.tabelaNutricional == null) {
            return "";
        } else {
            BigDecimal valorCalorico = this.tabelaNutricional.getNutrientes()
                    .stream()
                    .filter(n -> n.getNutriente().getId() == nutriente.ordinal())
                    .findFirst()
                    .map(TabelaNutricionalDetalhe::getVd)

                    .orElse(BigDecimal.ZERO);
            String strValorCalorico = StringUtils.leftPad(montaValor(valorCalorico, false), 4, "0");
            return strValorCalorico;
        }
    }


}
