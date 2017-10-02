package com.chronos.service.comercial;

import com.chronos.bo.nfe.NfeTransmissao;
import com.chronos.bo.nfe.NfeUtil;
import com.chronos.infra.enuns.ModeloDocumento;
import com.chronos.modelo.entidades.*;
import com.chronos.repository.EstoqueRepository;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.*;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.jsf.Mensagem;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by john on 26/09/17.
 */
public class NfeService implements Serializable {

    private static final long serialVersionUID = 1L;

    private Empresa empresa;

    @Inject
    private Repository<NotaFiscalTipo> tiposNotaFiscal;
    @Inject
    private Repository<NfeNumeroInutilizado> numeros;
    @Inject
    private Repository<NotaFiscalModelo> modelos;
    @Inject
    private Repository<NfeConfiguracao> configuracoesNfe;
    @Inject
    private Repository<NfceConfiguracao> configuracoesNfce;

    @Inject
    private EstoqueRepository produtos;

    @Inject
    private ExternalContext context;


    @PostConstruct
    private void init() {
        empresa = FacesUtil.getEmpresaUsuario();
        context = FacesContext.getCurrentInstance().getExternalContext();
    }

    public void dadosPadroes(NfeCabecalho nfe, ModeloDocumento modelo, Empresa empresa) throws Exception {
        NfeUtil nfeUtil = new NfeUtil();

        nfe = nfeUtil.dadosPadroes(nfe, modelo, empresa);
        setarConfiguracoesNFe(nfe, modelo);
    }

    public void setarConfiguracoesNFe(NfeCabecalho nfe, ModeloDocumento modelo) throws Exception {
        List<Filtro> filtros = new LinkedList<>();
        filtros.add(new Filtro(Filtro.AND, "empresa.id", Filtro.IGUAL, empresa.getId()));


        if (modelo == ModeloDocumento.NFE) {
            NfeConfiguracao configuracao = configuracoesNfe.get(NfeConfiguracao.class, filtros);

            if (configuracao == null) {
                throw new Exception("Configurações da NF-e  não definidas");
            }
            nfe.setAmbiente(configuracao.getWebserviceAmbiente());
            nfe.setProcessoEmissao(configuracao.getProcessoEmissao());
            nfe.setVersaoProcessoEmissao(configuracao.getVersaoProcessoEmissao());

            nfe.setInformacoesAddContribuinte(configuracao.getObservacaoPadrao());


        } else {
            NfceConfiguracao configuracao = configuracoesNfce.get(NfceConfiguracao.class, filtros);
            if (configuracao == null) {
                throw new Exception("Configurações da NFC-e  não definidas");
            }

            nfe.setAmbiente(configuracao.getWebserviceAmbiente());
            nfe.setProcessoEmissao(configuracao.getProcessoEmissao());
            nfe.setVersaoProcessoEmissao(configuracao.getVersaoProcessoEmissao());
            nfe.setInformacoesAddContribuinte(configuracao.getObservacaoPadrao());


        }

    }

    public String inutilizarNFe(NfeConfiguracao configuracao, String modelo, Integer serie, Integer numInicial, Integer numFinal, String justificativa) throws Exception {
        NotaFiscalTipo notaFiscalTipo = getNotaFicalTipoByModelo(modelo);
        if (notaFiscalTipo == null) {
            throw new Exception("Não foi informando numeração para o modelo " + modelo);
        }
        String schemas = org.springframework.util.StringUtils.isEmpty(configuracao.getCaminhoSchemas()) ? context.getRealPath(Constantes.DIRETORIO_SCHEMA_NFE) : configuracao.getCaminhoSchemas();
        NfeTransmissao transmissao = new NfeTransmissao(empresa);
        configuracao.setCaminhoSchemas(schemas);
        br.inf.portalfiscal.nfe.schema.retinutnfe.TRetInutNFe.InfInut infRetorno = transmissao.inutilizarNFe(configuracao, modelo, serie, numInicial, numFinal, justificativa);
        String resultado = "";
        switch (infRetorno.getCStat()) {
            case "102":
                for (int i = numInicial; i <= numFinal; i++) {
                    NfeNumeroInutilizado numInutilizado = new NfeNumeroInutilizado();
                    numInutilizado.setDataInutilizacao(new Date());
                    numInutilizado.setEmpresa(empresa);
                    numInutilizado.setNumero(i);
                    numInutilizado.setSerie(String.valueOf(serie));
                    numeros.salvar(numInutilizado);
                }

                if (numFinal > notaFiscalTipo.getUltimoNumero()) {

                    atualizarNumeroNfe(notaFiscalTipo, numFinal);
                }
                resultado = infRetorno.getXMotivo();
                break;
            case "241":
                resultado = infRetorno.getXMotivo();
                break;
            case "215":
                String xml = XmlUtil.objectToXml(infRetorno);
                String erroValidacao = ValidarNFe.validaXml(xml, "inutilizacao");
                Mensagem.addErrorMessage(erroValidacao);
                break;
            default:
                resultado = infRetorno.getXMotivo();
                break;
        }

        return resultado;
    }

    private void atualizarNumeroNfe(NotaFiscalTipo notaFiscalTipo, int numero) {
        List<Filtro> filtros = new LinkedList<>();
        filtros.add(new Filtro("id", notaFiscalTipo.getId()));
        Map<String, Object> atributos = new HashMap<>();
        atributos.put("ultimo_numero", numero);
        tiposNotaFiscal.updateNativo(NotaFiscalTipo.class, filtros, atributos);
    }

    public NotaFiscalTipo getNotaFicalTipoByModelo(String modelo) throws Exception {
        List<Filtro> filtros = new LinkedList<>();
        filtros.add(new Filtro(Filtro.AND, "empresa.id", Filtro.IGUAL, empresa.getId()));
        filtros.add(new Filtro(Filtro.AND, "notaFiscalModelo.codigo", Filtro.IGUAL, modelo));
        String camposTipoNF[] = new String[]{"serie", "ultimoNumero", "notaFiscalModelo.id"};
        NotaFiscalTipo notaFiscalTipo = tiposNotaFiscal.get(NotaFiscalTipo.class, filtros, camposTipoNF);

        return notaFiscalTipo;
    }

    private NotaFiscalTipo getNotaFicalTipo(String modelo) throws Exception {
        List<Filtro> filtros = new LinkedList<>();
        filtros.add(new Filtro(Filtro.AND, "empresa.id", Filtro.IGUAL, empresa.getId()));
        filtros.add(new Filtro(Filtro.AND, "notaFiscalModelo.codigo", Filtro.IGUAL, modelo));
        Object[] atributos = new String[]{"serie", "ultimoNumero", "notaFiscalModelo.id"};
        NotaFiscalTipo notaFiscalTipo = tiposNotaFiscal.get(NotaFiscalTipo.class, filtros, atributos);
        if (notaFiscalTipo == null) {
            notaFiscalTipo = new NotaFiscalTipo();
            notaFiscalTipo.setEmpresa(empresa);
            notaFiscalTipo.setSerie("001");
            notaFiscalTipo.setUltimoNumero(1);
            NotaFiscalModelo codigo = new NotaFiscalModelo(modelo.equals("55") ? 30 : 34);

            notaFiscalTipo.setNotaFiscalModelo(codigo);
            notaFiscalTipo = tiposNotaFiscal.atualizar(notaFiscalTipo);
        } else {
            notaFiscalTipo.setUltimoNumero(notaFiscalTipo.proximoNumero());
            notaFiscalTipo.setEmpresa(empresa);
            atualizarNumeroNfe(notaFiscalTipo, notaFiscalTipo.proximoNumero());
        }
        return notaFiscalTipo;
    }

    public void gerarNumeracao(NfeCabecalho nfe) throws Exception {

        Integer numero;
        String serie;
        if (nfe.getNumero() == null || nfe.getSerie() == null) {
            NotaFiscalTipo notaFiscalTipo = getNotaFicalTipo(nfe.getCodigoModelo());
            numero = notaFiscalTipo.getUltimoNumero();
            serie = notaFiscalTipo.getSerie();

        } else {
            numero = Integer.valueOf(nfe.getNumero());
            serie = nfe.getSerie();
        }

        nfe.setNumero(FormatValor.getInstance().formatarNumeroDocFiscalToString(numero));
        nfe.setCodigoNumerico(FormatValor.getInstance().formatarCodigoNumeroDocFiscalToString(numero));
        nfe.setSerie(serie);
        nfe.setChaveAcesso("" + nfe.getEmpresa().getCodigoIbgeUf()
                + FormatValor.getInstance().formatarAno(nfe.getDataHoraEmissao())
                + FormatValor.getInstance().formatarMes(nfe.getDataHoraEmissao())
                + nfe.getEmpresa().getCnpj()
                + nfe.getCodigoModelo()
                + nfe.getSerie()
                + nfe.getNumero()
                + "1"
                + nfe.getCodigoNumerico());
        nfe.setDigitoChaveAcesso(Biblioteca.modulo11(nfe.getChaveAcesso()).toString());
    }


    public void validar(NfeCabecalho nfe) throws Exception {
        if (nfe.getListaNfeDetalhe().isEmpty()) {
            throw new Exception("Não foi informado nenhum produto");
        }
        BigDecimal valorDucplicata = nfe.getListaDuplicata().stream()
                .map(NfeDuplicata::getValor)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        ;
        if (valorDucplicata.signum() == 1 && valorDucplicata.compareTo(nfe.getValorTotal()) != 0) {
            throw new Exception("Soma dos valores da duplicata Invalida");
        }
    }


    public NfeDetalhe definirTributacao(NfeDetalhe item, TributOperacaoFiscal operacaoFiscal, NfeDestinatario destinatario) throws Exception {
        NfeUtil nfeUtil = new NfeUtil();

        item = nfeUtil.defineTributacao(item, empresa, operacaoFiscal, destinatario);
        return item;
    }

    public NfeCabecalho atualizarTotais(NfeCabecalho nfe) throws Exception {

        NfeUtil nFeUtil = new NfeUtil();
        return nFeUtil.calcularTotalNFe(nfe);
    }

    public List<Produto> getListaProduto(String descricao) throws Exception {
        List<Produto> listaProduto;
        List<Filtro> filtros = new ArrayList<>();
        if (org.apache.commons.lang3.StringUtils.isNumeric(descricao)) {
            filtros.add(new Filtro(Filtro.AND, "id", Filtro.IGUAL, descricao));
            // listaProduto = produtoDao.getEntitys(Produto.class, filtros);
        } else {
            filtros.add(new Filtro(Filtro.AND, "nome", Filtro.LIKE, descricao.trim()));
            //  listaProduto = produtoDao.getEntitys(Produto.class, filtros);
        }

        listaProduto = produtos.getProdutoEmpresa(descricao, empresa);
        return listaProduto;
    }

    public void gerarDuplicatas(NfeCabecalho nfe, VendaCondicoesPagamento condicoesPagamento, Date primeiroVencimento, int intervaloParcelas, int qtdParcelas) throws Exception {

        if (nfe.getValorTotal() == null || nfe.getValorTotal().signum() == 0) {
            throw new Exception("O valor total da NFe deve ser maior que 0");
        }
        if (condicoesPagamento == null && (primeiroVencimento == null || intervaloParcelas == 0 || qtdParcelas == 0)) {
            throw new Exception("Se a condição de pagament não for informada é preciso informa o primeiro vencimento,intervalo de parcelas e a quantidade.");
        }
        if (nfe.getListaDuplicata() == null) {
            nfe.setListaDuplicata(new HashSet<>());
        }
        nfe.getListaDuplicata().clear();
        BigDecimal residuo = BigDecimal.ZERO;
        BigDecimal somaParcelas = BigDecimal.ZERO;
        BigDecimal valorParcela = BigDecimal.ZERO;
        if (condicoesPagamento != null) {
            int number = 0;
            for (VendaCondicoesParcelas parcelas : condicoesPagamento.getParcelas()) {
                NfeDuplicata duplicata = new NfeDuplicata();
                duplicata.setNfeCabecalho(nfe);
                valorParcela = Biblioteca.calcTaxa(nfe.getValorTotal(), parcelas.getTaxa());
                duplicata.setDataVencimento(Biblioteca.addDay(new Date(), parcelas.getDias()));
                duplicata.setValor(valorParcela);
                somaParcelas = somaParcelas.add(valorParcela);
                if (number == (condicoesPagamento.getParcelas().size() - 1)) {
                    residuo = nfe.getValorTotal().subtract(somaParcelas);
                    valorParcela = valorParcela.add(residuo);
                    duplicata.setValor(valorParcela);
                }
                duplicata.setNumero(String.format("%3s", String.valueOf(number++ + 1)));
                nfe.getListaDuplicata().add(duplicata);
            }
        } else {
            Calendar firstVencimento = Calendar.getInstance();
            firstVencimento.setTime(primeiroVencimento);
            valorParcela = nfe.getValorTotal().divide(BigDecimal.valueOf(qtdParcelas), RoundingMode.HALF_DOWN);

            for (int i = 0; i < qtdParcelas; i++) {
                NfeDuplicata duplicata = new NfeDuplicata();
                duplicata.setNfeCabecalho(nfe);
                duplicata.setNumero(String.format("%3s", String.valueOf(i + 1)));
                if (i > 0) {
                    firstVencimento.add(Calendar.DAY_OF_MONTH, intervaloParcelas);
                }
                duplicata.setDataVencimento(firstVencimento.getTime());
                duplicata.setValor(valorParcela);

                somaParcelas = somaParcelas.add(valorParcela);
                if (i == (qtdParcelas - 1)) {
                    residuo = nfe.getValorTotal().subtract(somaParcelas);
                    valorParcela = valorParcela.add(residuo);
                    duplicata.setValor(valorParcela);
                }
                nfe.getListaDuplicata().add(duplicata);
            }
        }


    }
}
