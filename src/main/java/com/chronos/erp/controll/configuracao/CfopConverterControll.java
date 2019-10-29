package com.chronos.erp.controll.configuracao;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.data.DataDomain;
import com.chronos.erp.dto.MapIntegerDTO;
import com.chronos.erp.modelo.entidades.ConverterCfop;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class CfopConverterControll extends AbstractControll<ConverterCfop> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, String> tipo;


    private List<MapIntegerDTO> cfops;


    private MapIntegerDTO cfopOrigem;
    private MapIntegerDTO cfopDestino;


    @Override
    public void doCreate() {
        super.doCreate();
        popularMap();
        cfopDestino = null;
        cfopOrigem = null;
    }

    @Override
    public void doEdit() {
        super.doEdit();
        popularMap();
    }

    @Override
    public void salvar() {
        getObjeto().setCfopDestino(cfopDestino.getId());
        getObjeto().setCfopOrigem(cfopOrigem.getId());
        super.salvar();
    }

    private void popularMap() {
        tipo = new LinkedHashMap<>();
        tipo.put("Entrada", "E");
        tipo.put("Saída", "S");

//        cfops = new LinkedList<>(DataDomain.getCFOP()
//                .entrySet()
//                .stream()
//                .sorted(Comparator.comparing(e -> e.getValue()))
//                .map(c-> new MapIntegerDTO(c.getKey(),c.getValue())).collect(Collectors.toList())) ;

        cfops = DataDomain.getCFOP();
    }

    public List<MapIntegerDTO> getListCfop(String nome) {
        String str = "0" + nome.replaceAll("\\D", "");
        int cfop = Integer.valueOf(str);

        if (cfop > 0) {
            return DataDomain.getCFOP().stream().filter(c -> c.getId() == cfop).collect(Collectors.toList());
        } else {
            return DataDomain.getCFOP().stream().filter(c -> c.getDescricao().contains(nome)).collect(Collectors.toList());
        }
    }

    @Override
    protected Class<ConverterCfop> getClazz() {
        return ConverterCfop.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "CONVERTER_CFOP";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public Map<String, String> getTipo() {
        return tipo;
    }

    public List<MapIntegerDTO> getCfops() {
        return cfops;
    }

    public MapIntegerDTO getCfopOrigem() {
        return cfopOrigem;
    }

    public void setCfopOrigem(MapIntegerDTO cfopOrigem) {
        this.cfopOrigem = cfopOrigem;
    }

    public MapIntegerDTO getCfopDestino() {
        return cfopDestino;
    }

    public void setCfopDestino(MapIntegerDTO cfopDestino) {
        this.cfopDestino = cfopDestino;
    }


}
