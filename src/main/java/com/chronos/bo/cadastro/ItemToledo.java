package com.chronos.bo.cadastro;

import com.chronos.modelo.entidades.Produto;
import com.chronos.modelo.entidades.TabelaNutricionalCabecalho;
import com.chronos.modelo.entidades.TabelaNutricionalDetalhe;
import com.chronos.modelo.enuns.Nutrientes;
import com.chronos.service.ChronosException;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Optional;

public class ItemToledo {


    private Produto produto;
    private TabelaNutricionalCabecalho tabelaNutricional;
    private String identificador;


    public ItemToledo(Produto produto) {
        this.produto = produto;
        this.identificador = produto.getBalanca().getIdentificador();
        this.tabelaNutricional = produto.getTabelaNutricional();
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }


    public String montarItem() throws ChronosException {
        String linha = "01"
                + montaTipo()
                + montarCodigo()
                + montarPreco()
                + montarDiasValidade()
                + montarDescricao()
                + completarComZeroDireita(6, "")//RRRRRR
                + completarComZeroDireita(4, "")//FFFF
                + montaCodigoTabelaNutricional() //IIIIII
                + completarComZeroDireita(86, "");
        return linha;

    }

    public String montarValorNutricional() {
        String infNutri = "N"
                + montaCodigoTabelaNutricional()
                + "0"
                + montaValor(tabelaNutricional.getPorcao(), false)
                + montarUndiadePorcao()
                + montarParteInteiraMedidaCaseira()
                + montarParteDecimalMedidaCaseira()
                + montaMedidaCaseira()
                + montarValorNutricional(Nutrientes.VALOR_CALORICO, 4)
                + montarValorNutricional(Nutrientes.CARBOIDRATO, 4, true)
                + montarValorNutricional(Nutrientes.PROTEINA, 3, true)
                + montarValorNutricional(Nutrientes.GORDURA_TOTAIS, 3, true)
                + montarValorNutricional(Nutrientes.GORDURA_SATURADA, 3, true)
                + montarValorNutricional(Nutrientes.GORDURA_TRANS, 3, true)
                + montarValorNutricional(Nutrientes.FIBRA_ALIMENTAR, 3, true)
                + montarValorNutricional(Nutrientes.SODIO, 5, true);
        return infNutri;
    }

    public String montarItem2() throws ChronosException {
        String linha = "010" + montarCodigo() + montarPreco() + "000" + montarDescricao() + completarComZeroDireita(88, "") + "\r\n";
        return linha;

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

    private String montaTipo() {
        return identificador.equals("Q") ? "1" : "0";
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

    private String montaCodigoTabelaNutricional() {
        Integer codigo = tabelaNutricional == null ? 0 : tabelaNutricional.getId();
        return StringUtils.leftPad(codigo.toString(), 6, "0");
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

    private String montarDiasValidade() {
        Integer validade = produto.getDiasValidade() == null ? 0 : produto.getDiasValidade();
        return StringUtils.leftPad(validade.toString(), 3, "0");
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

    private String montarUndiadePorcao() {
        String un = tabelaNutricional == null || org.springframework.util.StringUtils.isEmpty(tabelaNutricional.getUnidade())
                ? "UN"
                : tabelaNutricional.getUnidade();
        switch (un) {
            case "GR":
                return "0";
            case "ML":
                return "1";
            default:
                return "2";
        }
    }


    private String montarParteInteiraMedidaCaseira() {
        Integer parteInteira = tabelaNutricional == null || tabelaNutricional.getParteInteiraMedidaCaseria() == null
                ? 0
                : tabelaNutricional.getParteInteiraMedidaCaseria();
        return StringUtils.leftPad(parteInteira.toString(), 2, "0");
    }

    private String montarParteDecimalMedidaCaseira() {
        Integer parteDecimal = tabelaNutricional == null || tabelaNutricional.getParteDecimalMedidaCaseria() == null
                ? 0
                : tabelaNutricional.getParteDecimalMedidaCaseria();
        return parteDecimal.toString();
    }

    private String montaMedidaCaseira() {
        return tabelaNutricional == null || org.springframework.util.StringUtils.isEmpty(tabelaNutricional.getMedidaCaseiraUtilizada())
                ? "00"
                : tabelaNutricional.getMedidaCaseiraUtilizada();
    }


    private String montarValorNutricional(Nutrientes nutriente, int tam) {
        return montarValorNutricional(nutriente, tam, false);
    }

    private String montarValorNutricional(Nutrientes nutriente, int tam, boolean exibirParteDecimal) {
        if (this.tabelaNutricional == null) {
            return "";
        } else {
            BigDecimal valorCalorico = this.tabelaNutricional.getNutrientes()
                    .stream()
                    .filter(n -> n.getNutriente().getId() == nutriente.ordinal())
                    .findFirst()
                    .map(TabelaNutricionalDetalhe::getQuantidade)

                    .orElse(BigDecimal.ZERO);
            String strValorCalorico = StringUtils.leftPad(montaValor(valorCalorico, exibirParteDecimal), tam, "0");
            return strValorCalorico;
        }
    }

    private String montaValor(BigDecimal valor, boolean exibirPos) {
        int pre = (int) valor.doubleValue();
        StringBuilder str = new StringBuilder();
        str.append(pre);
        if (exibirPos) {
            int pos = (int) valor.subtract(BigDecimal.valueOf(pre)).multiply(BigDecimal.valueOf(100)).doubleValue();
            pos = pos / 10;
            str.append(pos);
        }
        return str.toString();
    }


    private String format(double value) {
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


}
