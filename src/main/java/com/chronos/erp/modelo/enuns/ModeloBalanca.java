package com.chronos.erp.modelo.enuns;

public enum ModeloBalanca {

    NENHUM("Nenhum"),
    FILIZOLA("Filizola"),
    TOLEDO("Toledo"),
    TOLEDO2180("Toledo 2180"),
    URANO("Urano"),
    LUCASTEC("LucasTEC"),
    MAGNA("MAGNA"),
    DIGITRON("DGITRON"),
    MAGELLAN("Magellan");

    private String descricao;


    ModeloBalanca(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
