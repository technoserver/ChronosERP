package com.chronos.service.fiscal;

import com.chronos.infra.efdicms.SpedFiscalIcms;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.entidades.view.*;
import com.chronos.repository.EcfNotaFiscalCabecalhoRepository;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.repository.ViewSpedC425Repository;
import com.chronos.util.Biblioteca;
import com.chronos.util.jsf.FacesUtil;

import javax.inject.Inject;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by john on 28/09/17.
 */


public class SpedIcmsIpiService implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<NfeCabecalho> nfes;
    @Inject
    private Repository<EcfNotaFiscalCabecalho> nf2Repository;
    @Inject
    private Repository<TributOperacaoFiscal> operacaoFiscalRepository;
    @Inject
    private Repository<Contador> contadores;
    @Inject
    private Repository<ProdutoAlteracaoItem> alteracaoItemRepository;
    @Inject
    private Repository<ViewSpedC190Id> viewC190Repository;
    @Inject
    private Repository<ViewSpedC370Id> viewC370Repository;
    @Inject
    private Repository<ViewSpedC390Id> viewC390Repository;
    @Inject
    private Repository<ViewSpedC300Id> viewC300Repository;
    @Inject
    private EcfNotaFiscalCabecalhoRepository ecfNotaFiscalCabecalhoRepository;
    @Inject
    private Repository<ViewSpedC321Id> viewC321Repository;
    @Inject
    private Repository<EcfImpressora> ecfImpressoraRepository;
    @Inject
    private Repository<EcfR02> ecfR02Repository;
    @Inject
    private Repository<EcfR03> ecfR03Repository;
    @Inject
    private ViewSpedC425Repository viewC425Repository;
    @Inject
    private Repository<EcfVendaCabecalho> ecfVendaCabecalhoRepository;
    @Inject
    private Repository<EcfVendaDetalhe> ecfVendaDetalheRepository;
    @Inject
    private Repository<Produto> produtos;
    @Inject
    private Repository<ViewSpedC490Id> viewC490Repository;

    private Date dataInicio;
    private Date dataFim;
    private String versao;
    private String finalidadeArquivo;
    private String perfil;
    private Integer inventario;
    private Integer idContador;
    private StringBuilder arquivo;
    private Empresa empresa;
    private EmpresaEndereco enderecoPrincipal;
    private SpedFiscalIcms sped;


    public File geraArquivo(String versao, String finalidadeArquivo, String perfil, Integer inventario, Date dataInicial, Date dataFinal, Integer idContador) throws Exception {
        this.dataInicio = dataInicial;
        this.dataFim = dataFinal;
        this.versao = versao;
        this.finalidadeArquivo = finalidadeArquivo;
        this.perfil = perfil;
        this.inventario = inventario;
        this.idContador = idContador;
        arquivo = new StringBuilder();


        empresa = FacesUtil.getEmpresaUsuario();
        empresa = (Empresa) Biblioteca.nullToEmpty(empresa, false);
        EmpresaEndereco enderecoPrincipal = new EmpresaEndereco();
        for (EmpresaEndereco e : empresa.getListaEndereco()) {
            if (e.getPrincipal().equals("S")) {
                enderecoPrincipal = e;
            }
        }
        enderecoPrincipal = (EmpresaEndereco) Biblioteca.nullToEmpty(enderecoPrincipal, false);

        geraBloco0();
        geraBlocoC();
        // BLOCO D: DOCUMENTOS FISCAIS II - SERVIÇOS (ICMS).
        // Bloco de registros dos dados relativos à emissão ou ao recebimento de
        // documentos fiscais que acobertam as prestações de serviços de
        // comunicação, transporte intermunicipal e interestadual.
        // Implementado a critério do Participante do T2Ti ERP
        geraBlocoE();
        // BLOCO G – CONTROLE DO CRÉDITO DE ICMS DO ATIVO PERMANENTE CIAP
        // TODO implementa controle de patrimonio
        if (inventario > 0) {
            geraBlocoH();
        }
        geraBloco1();


        File file = File.createTempFile("spedfiscal", ".txt");
        file.deleteOnExit();


        return file;
    }

    /**
     * BLOCO 0: ABERTURA, IDENTIFICAÇÃO E REFERÊNCIAS
     */
    private void geraBloco0() {


        //TODO REGISTRO 0015: DADOS DO CONTRIBUINTE SUBSTITUTO

        List<Filtro> filtros = new ArrayList<>();
        filtros.add(new Filtro(Filtro.AND, "dataHoraEmissao", Filtro.MAIOR_OU_IGUAL, dataInicio));
        filtros.add(new Filtro(Filtro.AND, "dataHoraEmissao", Filtro.MENOR_OU_IGUAL, dataFim));
        List<NfeCabecalho> listaNfeCabecalho = nfes.getEntitys(NfeCabecalho.class, filtros);

        List<UnidadeProduto> listaUnidadeProduto = new ArrayList<>();

        List<TributOperacaoFiscal> listaOperacaoFiscal = operacaoFiscalRepository.getEntitys(TributOperacaoFiscal.class);

        Contador contador = contadores.get(idContador, Contador.class);
    }

    private void geraBlocoC() {
    }

    private void geraBlocoE() {
    }

    private void geraBlocoH() {
    }

    private void geraBloco1() {
    }
}
