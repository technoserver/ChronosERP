package com.chronos.erp.service.configuracao;

import com.chronos.erp.dto.ChaveAcessoDTO;
import com.chronos.erp.modelo.entidades.Empresa;
import com.chronos.erp.modelo.entidades.MdfeCabecalho;
import com.chronos.erp.modelo.entidades.NotaFiscalTipo;
import com.chronos.erp.modelo.enuns.StatusTransmissao;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.util.Biblioteca;
import com.chronos.erp.util.FormatValor;
import com.chronos.erp.util.jsf.FacesUtil;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;

/**
 * Created by john on 20/06/18.
 */
public class NotaFiscalService implements Serializable {

    private static final long serialVersionUID = 1L;

    private Empresa empresa;
    @Inject
    private Repository<NotaFiscalTipo> repository;

    @PostConstruct
    private void init() {
        empresa = FacesUtil.getEmpresaUsuario();

    }

    public String definirChaveAcessoNfe() {

        return "";
    }

    public MdfeCabecalho definirChaveAcessoMdfe(MdfeCabecalho mdfe) throws Exception {


        Integer numero;
        String serie;
        if (mdfe.getStatusTransmissao() == StatusTransmissao.DUPLICIDADE) {
            atualizarNumero(Integer.valueOf(mdfe.getNumeroMdfe()), mdfe.getSerie(), mdfe.getEmpresa().getId(), mdfe.getModelo());

            numero = Integer.valueOf(mdfe.getNumeroMdfe()) + 1;
            serie = mdfe.getSerie();
        } else {
            if (mdfe.getNumeroMdfe() == null) {
                NotaFiscalTipo notaFicalTipo = getNotaFicalTipo(ModeloDocumento.getByCodigo(Integer.valueOf(mdfe.getModelo())));
                numero = notaFicalTipo.getUltimoNumero() + 1;
                serie = notaFicalTipo.getSerie();
            } else {
                numero = Integer.valueOf(mdfe.getNumeroMdfe());
                serie = mdfe.getSerie();
            }

        }

        Random random = new Random();

        mdfe.setSerie(serie);
        mdfe.setNumeroMdfe(FormatValor.getInstance().formatarNumeroDocFiscalToString(numero));
        mdfe.setCodigoNumerico(String.valueOf(random.nextInt(99999999)));
        String str = mdfe.getCodigoNumerico();

        if (str.length() > 8) {
            mdfe.setCodigoNumerico(StringUtils.right(str, 8));
        }
        String chave = gerarChaveAcesso(new ChaveAcessoDTO(mdfe));
        mdfe.setChaveAcesso(chave);
        mdfe.setDigitoVerificador(Biblioteca.modulo11(chave));
        return mdfe;
    }


    public NotaFiscalTipo getNotaFicalTipo(ModeloDocumento modelo) throws Exception {

        List<Filtro> filtros = new LinkedList<>();
        filtros.add(new Filtro(Filtro.AND, "empresa.id", Filtro.IGUAL, empresa.getId()));
        filtros.add(new Filtro(Filtro.AND, "notaFiscalModelo.codigo", Filtro.IGUAL, modelo.getCodigo().toString()));
        Object[] atributos = new String[]{"serie", "ultimoNumero", "notaFiscalModelo.id"};
        NotaFiscalTipo notaFiscalTipo = repository.get(NotaFiscalTipo.class, filtros, atributos);
        if (notaFiscalTipo == null) {
            throw new Exception("Configuração de numero fiscal para o modelo :" + modelo + " não definida");
        }

        notaFiscalTipo.setUltimoNumero(notaFiscalTipo.proximoNumero());

        return notaFiscalTipo;
    }

    public void atualizarNumero(int numero, String serie, Integer idempresa, String modelo) {
        List<Filtro> filtros = new ArrayList<>();
        filtros.add(new Filtro("notaFiscalModelo.codigo", modelo));
        filtros.add(new Filtro("serie", serie));
        filtros.add(new Filtro("empresa.id", idempresa));
        Map<String, Object> atributos = new HashMap<>();
        atributos.put("ultimoNumero", numero);
        //repository.atualizar(NotaFiscalTipo.class,filtros,atributos);
        repository.atualizarNamedQuery("NotaFiscalTipo.UpdateNumeroModelo", numero, modelo, serie, idempresa);
    }

    private String gerarChaveAcesso(ChaveAcessoDTO chaveAcesso) {
        String chave;
        chave = chaveAcesso.getCodigoUF()
                + FormatValor.getInstance().formatarAno(new Date())
                + FormatValor.getInstance().formatarMes(new Date())
                + chaveAcesso.getCnpj()
                + chaveAcesso.getModelo()
                + chaveAcesso.getSerie()
                + chaveAcesso.getNumero()
                + "1"
                + chaveAcesso.getCodigoNumerico();

        return chave;
    }
}
