package com.chronos.modelo.enuns;

/**
 * Created by john on 04/10/17.
 */
public enum EstatusPersistencia {
    ERRO("Erro"), SUCESSO("Sucesso"),
    OBJETO_REFERENCIADO("Esso objeto não pode ser apagado por possuir referências ao mesmo.");

    private String name;

    EstatusPersistencia(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
