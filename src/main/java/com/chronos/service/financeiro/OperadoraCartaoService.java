package com.chronos.service.financeiro;

import com.chronos.modelo.entidades.OperadoraCartao;
import com.chronos.modelo.entidades.OperadoraCartaoTaxa;
import com.chronos.service.ChronosException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class OperadoraCartaoService implements Serializable {


    private static final long serialVersionUID = 1L;


    public List<OperadoraCartaoTaxa> validarIntevalo(List<OperadoraCartaoTaxa> taxas, OperadoraCartaoTaxa taxa) throws ChronosException {
        if (taxa.getIntervaloInicial() > taxa.getIntervaloFinal()) {
            throw new ChronosException("Intervalo inicial não pode ser maior que o intervalo final");
        }
        if (taxas.isEmpty()) {
            taxas.add(taxa);
        } else {
            long count = taxas.stream().filter(t -> t.getIntervaloInicial().equals(taxa.getIntervaloInicial())).count();
            if (count > 0 && taxaIgual(taxas, taxa)) {
                throw new ChronosException("Já foram definido taxa com esse intervalo inicial");
            }
            count = taxas.stream().filter(t -> t.getIntervaloFinal().equals(taxa.getIntervaloFinal())).count();

            if (count > 0 && taxaFinalIgual(taxas, taxa)) {
                throw new ChronosException("Já foram definido taxa com esse intervalo Final");
            }

            count = taxas.stream().filter(t -> taxa.getIntervaloInicial() > t.getIntervaloInicial() && taxa.getIntervaloFinal() < t.getIntervaloFinal()).count();

            if (count > 0) {
                throw new ChronosException("Existe conflito de intervalos");
            }
            taxas.add(taxa);


        }
        return taxas;
    }

    public OperadoraCartaoTaxa addTaxa(List<OperadoraCartaoTaxa> taxas) {


        OperadoraCartaoTaxa taxa = taxas.stream().max(Comparator.comparing(OperadoraCartaoTaxa::getIntervaloFinal)).get();
        taxa.setIntervaloInicial(taxa.getIntervaloFinal() + 1);
        taxa.setIntervaloFinal(taxa.getIntervaloFinal() + 2);

        return taxa;
    }

    public boolean taxaIgual(List<OperadoraCartaoTaxa> taxas, OperadoraCartaoTaxa taxa) {

        Optional<OperadoraCartaoTaxa> taxaOptional = taxas.stream()
                .filter(t -> t.getIntervaloInicial().equals(taxa.getIntervaloInicial()))
                .findFirst();

        return taxaOptional.isPresent() && !taxaOptional.get().equals(taxa);
    }

    public boolean taxaFinalIgual(List<OperadoraCartaoTaxa> taxas, OperadoraCartaoTaxa taxa) {

        Optional<OperadoraCartaoTaxa> taxaOptional = taxas.stream()
                .filter(t -> t.getIntervaloFinal().equals(taxa.getIntervaloFinal()))
                .findFirst();

        return taxaOptional.isPresent() && !taxaOptional.get().equals(taxa);
    }

    public int quantidadeMaximaParcelas(OperadoraCartao operadoraCartao) {
        if (operadoraCartao.getListaOperadoraCartaoTaxas() == null || operadoraCartao.getListaOperadoraCartaoTaxas().isEmpty()) {
            return 1;
        } else {
            OperadoraCartaoTaxa taxa = operadoraCartao.getListaOperadoraCartaoTaxas().stream().max(Comparator.comparing(OperadoraCartaoTaxa::getIntervaloFinal)).get();
            return taxa.getIntervaloFinal();
        }
    }

    public BigDecimal getTaxa(List<OperadoraCartaoTaxa> taxas, int qtdParcelas) {
        BigDecimal taxa = taxas
                .stream()
                .filter(t -> t.getIntervaloInicial() >= qtdParcelas || t.getIntervaloFinal() >= qtdParcelas)
                .min(Comparator.comparing(OperadoraCartaoTaxa::getIntervaloFinal)).get().getTaxaAdm();

//        List<OperadoraCartaoTaxa> collect = taxas.stream().filter(t -> t.getIntervaloInicial() >= qtdParcelas || t.getIntervaloFinal() >= qtdParcelas ).collect(Collectors.toList());
        return taxa;
    }

}
