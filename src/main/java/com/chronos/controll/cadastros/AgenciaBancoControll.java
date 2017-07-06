/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.AgenciaBanco;
import com.chronos.modelo.entidades.Banco;
import com.chronos.modelo.entidades.Municipio;
import com.chronos.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author john
 */
@Named
@ViewScoped
public class AgenciaBancoControll extends AbstractControll<AgenciaBanco> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<Banco> bancos;
    private Repository<Municipio> municipios;

    public List<Banco> getAllBanco(String nome) {
        List<Banco> listaBancos = new LinkedList<>();
        try {
            listaBancos = bancos.getEntitys(Banco.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaBancos;
    }

    public List<String> getListaMunicipios(String nome) {
        List<String> listMunicipios = new LinkedList<>();
        try {
            listMunicipios = municipios.getEntitysToQuery(String.class, "select m.nome from Municipio  m where m.uf.sigla = ?1 and LOWER(m.nome) like ?2", getObjeto().getUf(), "%" + nome.toLowerCase() + "%");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return listMunicipios;
    }

    @Override
    public Class<AgenciaBanco> getClazz() {
        return AgenciaBanco.class;
    }

    @Override
    public String getFuncaoBase() {
        return "AGENCIA_BANCO";
    }

}
