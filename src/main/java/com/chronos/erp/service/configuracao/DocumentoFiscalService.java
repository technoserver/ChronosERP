package com.chronos.erp.service.configuracao;

import com.chronos.erp.modelo.entidades.Empresa;
import com.chronos.erp.modelo.entidades.NfeCabecalho;
import com.chronos.erp.modelo.entidades.NotaFiscalTipo;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.util.Biblioteca;
import com.chronos.erp.util.FormatValor;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;

public class DocumentoFiscalService implements Serializable {

    private static final long serialVersionUID = 1L;

    private Repository<NotaFiscalTipo> repository;
    private List<Filtro> filtros;

    @Inject
    public DocumentoFiscalService(Repository<NotaFiscalTipo> repository) {
        this.repository = repository;
        filtros = new LinkedList<>();
    }

    public NotaFiscalTipo gerarNumeracao(NfeCabecalho nfe) throws Exception {

        Integer numero;
        String serie;
        NotaFiscalTipo notaFiscalTipo;
        ModeloDocumento modelo = nfe.getModeloDocumento();
        notaFiscalTipo = getNotaFicalTipo(modelo, nfe.getSerie(), nfe.getEmpresa());

        if (notaFiscalTipo == null) {
            throw new ChronosException("Numero fiscal não encontrado");
        }

        if (StringUtils.isEmpty(nfe.getNumero())) {
            numero = notaFiscalTipo.proximoNumero();
            serie = notaFiscalTipo.getSerie();
        } else {
            numero = Integer.valueOf(nfe.getNumero());
            serie = nfe.getSerie();
        }

        nfe.setNumero(FormatValor.getInstance().formatarNumeroDocFiscalToString(numero));
        nfe.setCodigoNumerico(gerarCodigoNFe());
        nfe.setSerie(serie);
        nfe.setChaveAcesso("" + nfe.getEmpresa().getCodigoIbgeUf()
                + FormatValor.getInstance().formatarAno(new Date())
                + FormatValor.getInstance().formatarMes(new Date())
                + nfe.getEmpresa().getCnpj()
                + nfe.getCodigoModelo()
                + nfe.getSerie()
                + nfe.getNumero()
                + "1"
                + nfe.getCodigoNumerico());
        nfe.setDigitoChaveAcesso(Biblioteca.modulo11(nfe.getChaveAcesso()).toString());
        return notaFiscalTipo;
    }

    public void atualizarNumeroNfe(NotaFiscalTipo notaFiscalTipo, int numero) {
        filtros.clear();
        filtros.add(new Filtro("id", notaFiscalTipo.getId()));
        Map<String, Object> atributos = new HashMap<>();
        atributos.put("ultimo_numero", numero);
        repository.updateNativo(NotaFiscalTipo.class, filtros, atributos);
    }

    public void atualizarNumeroNfe(NfeCabecalho nfe) {
        int numero = Integer.parseInt(nfe.getNumero());
        repository.atualizarNamedQuery("NotaFiscalTipo.UpdateNumeroModelo", numero, nfe.getCodigoModelo()
                , nfe.getSerie(), nfe.getEmpresa().getId());
    }


    public NotaFiscalTipo getNotaFicalTipo(ModeloDocumento modelo, String serie, Empresa empresa) throws ChronosException {

        if (modelo == null) {
            String msg = modelo == null ? "Modelo não definido" : "Serie não definida";
            throw new ChronosException("Configuração de " + msg);
        }

        filtros.clear();
        filtros.add(new Filtro(Filtro.AND, "empresa.id", Filtro.IGUAL, empresa.getId()));
        filtros.add(new Filtro(Filtro.AND, "notaFiscalModelo.codigo", Filtro.IGUAL, modelo.getCodigo().toString()));

        if (!StringUtils.isEmpty(serie)) {
            filtros.add(new Filtro(Filtro.AND, "serie", Filtro.IGUAL, serie));
        }

        Object[] atributos = new String[]{"serie", "ultimoNumero", "notaFiscalModelo.id"};
        NotaFiscalTipo notaFiscalTipo = repository.get(NotaFiscalTipo.class, filtros, atributos);
        if (notaFiscalTipo == null) {
            throw new ChronosException("Configuração de numero fiscal para o modelo :" + modelo + " não definida");
        }

        return notaFiscalTipo;
    }

    public String gerarCodigoNFe() {
        String codigo = "";

        List<String> codigosInvalido = Arrays.asList(
                new String[]{
                        "00000000", "11111111", "22222222", "33333333", "44444444",
                        "55555555", "66666666", "77777777", "88888888", "99999999",
                        "12345678", "23456789", "34567890", "45678901", "56789012",
                        "67890123", "78901234", "89012345", "90123456", "01234567"
                });

        Random random = new Random();


        do {
            int result = random.nextInt(99999998 - 10000000 + 1) + 10000000;
            codigo = String.valueOf(result);
        } while (codigosInvalido.contains(codigo));


        return codigo;
    }
}
