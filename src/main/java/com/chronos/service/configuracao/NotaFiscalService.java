package com.chronos.service.configuracao;

import com.chronos.dto.ChaveAcessoDTO;
import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.MdfeCabecalho;
import com.chronos.modelo.entidades.NotaFiscalTipo;
import com.chronos.modelo.enuns.StatusTransmissao;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import com.chronos.util.Biblioteca;
import com.chronos.util.FormatValor;
import com.chronos.util.jsf.FacesUtil;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
            atualizarNumero(Integer.valueOf(mdfe.getNumeroMdfe()) + 1, mdfe.getSerie(), mdfe.getEmpresa().getId(), mdfe.getModelo());
            mdfe.setNumeroMdfe(null);
            mdfe.setSerie(null);
        }

        if (mdfe.getNumeroMdfe() == null) {
            NotaFiscalTipo notaFicalTipo = getNotaFicalTipo(ModeloDocumento.getByCodigo(Integer.valueOf(mdfe.getModelo())));
            numero = notaFicalTipo.getUltimoNumero();
            serie = notaFicalTipo.getSerie();
        } else {
            numero = Integer.valueOf(mdfe.getNumeroMdfe());
            serie = mdfe.getSerie();
        }
        mdfe.setSerie(serie);
        mdfe.setNumeroMdfe(FormatValor.getInstance().formatarNumeroDocFiscalToString(numero));
        mdfe.setCodigoNumerico(FormatValor.getInstance().formatarCodigoNumeroDocFiscalToString(numero));
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

    public void atualizarNumero(int i, String serie, Integer id, String modelo) {

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
