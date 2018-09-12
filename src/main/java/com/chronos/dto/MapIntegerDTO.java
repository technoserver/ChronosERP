package com.chronos.dto;

import java.io.Serializable;

public class MapIntegerDTO implements Serializable {

    private static final long serialVersionUID = 2L;

    private String descricao;
    private Integer id;

    public MapIntegerDTO() {
    }

    public MapIntegerDTO(String descricao, Integer id) {
        this.descricao = descricao;
        this.id = id;
    }


    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return id + " - " + descricao;
    }
}
