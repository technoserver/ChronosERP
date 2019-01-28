
package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.Contador;
import com.chronos.modelo.entidades.Municipio;
import com.chronos.modelo.entidades.Uf;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author john
 */
@Named
@ViewScoped
public class ContadorControll extends AbstractControll<Contador> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<Municipio> municipioRepository;

    private Municipio municipio;



    @Override
    public void doEdit() {
        super.doEdit();
        instanciarMunicipio();
    }

    @Override
    public void salvar() {

        try {

            Contador contador = dao.get(Contador.class, "inscricaoCrc", getObjeto().getInscricaoCrc());

            if (contador != null && !contador.equals(getObjeto())) {
                throw new ChronosException("CRC já informado");
            }

            super.salvar();
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {

            } else {
                throw new RuntimeException("", ex);
            }
        }
    }

    public List<Municipio> getMunicipios(String nome) {
        List<Municipio> cidades = new ArrayList<>();
        try {

            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("uf.sigla", getObjeto().getUf()));
            filtros.add(new Filtro("nome", Filtro.LIKE, nome));
            atributos = new Object[]{"nome", "codigoIbge"};
            cidades = municipioRepository.getEntitys(Municipio.class, filtros, atributos);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cidades;
    }


    public void atualizarCodIbge() {
        municipio = new Municipio();
        getObjeto().setMunicipioIbge(municipio.getCodigoIbge());
        getObjeto().setCidade(municipio.getNome());

    }

    public void instanciarMunicipio() {
        municipio = new Municipio();
        municipio.setNome(getObjeto().getCidade());
        municipio.setUf(new Uf(getObjeto().getUf()));
    }


    @Override
    protected Class<Contador> getClazz() {
        return Contador.class;
    }


    @Override
    protected String getFuncaoBase() {
        return "CONTADOR";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }
}
