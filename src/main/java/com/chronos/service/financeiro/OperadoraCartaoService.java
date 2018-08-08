package com.chronos.service.financeiro;

import com.chronos.modelo.entidades.OperadoraCartaoTaxa;
import com.chronos.service.ChronosException;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

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
            if (count > 0) {
                throw new ChronosException("Já foram definido taxa com esse intervalo inicial");
            }
            count = taxas.stream().filter(t -> t.getIntervaloFinal().equals(taxa.getIntervaloFinal())).count();

            if (count > 0) {
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

}
