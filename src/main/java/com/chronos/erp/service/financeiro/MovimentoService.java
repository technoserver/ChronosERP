package com.chronos.erp.service.financeiro;

import com.chronos.erp.dto.UsuarioDTO;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.util.ArquivoUtil;
import com.chronos.erp.util.Biblioteca;
import com.chronos.erp.util.FormatValor;
import com.chronos.erp.util.jpa.Transactional;
import com.chronos.erp.util.jsf.FacesUtil;
import com.chronos.erp.util.jsf.Mensagem;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.*;

import static java.nio.file.FileSystems.getDefault;

/**
 * Created by john on 19/01/18.
 */
public class MovimentoService implements Serializable {


    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<PdvMovimento> repository;
    @Inject
    private Repository<PdvSuprimento> pdvSuprimentoRepository;
    @Inject
    private Repository<PdvSangria> pdvSangriaRepository;


    @Inject
    private ExternalContext context;

    private PdvMovimento movimento;
    private StringBuilder linhasRelatorio;
    private String nomeImpressaoMovimento;
    private PdvConfiguracao conf;


    @PostConstruct
    private void init() {
        movimento = FacesUtil.getMovimento();
    }

    @Transactional
    public void iniciarMovimento(Empresa empresa, BigDecimal valorSuprimento, PdvTurno turno, PdvCaixa caixa) throws Exception {


        UsuarioDTO user = FacesUtil.getUsuarioSessao();

        if (user.getOperador() == null) {
            throw new ChronosException("É preciso que o usuário esteja definido como operador de caixa");
        }

        PdvMovimento movimentoAberto = pesquisarMovimento(user.getOperador().getId());

        if (movimentoAberto != null) {
            throw new ChronosException("Já existe movimento aberto para esse operador");
        }

        List<Filtro> filtros = new ArrayList<>();
        filtros.add(new Filtro("statusMovimento", "A"));
        filtros.add(new Filtro("pdvCaixa.id", caixa.getId()));
        boolean existeCaixaAberto = repository.existeRegisro(PdvMovimento.class, filtros);

        if (existeCaixaAberto) {
            throw new ChronosException("Esse caixa já se encontra em  aberto");
        }

        movimento = new PdvMovimento();
        movimento.setEmpresa(empresa);


        movimento.setPdvCaixa(caixa);
        movimento.setPdvOperador(new PdvOperador(user.getOperador().getId()));
        movimento.setIdGerenteSupervisor(user.getOperador().getId());
        movimento.setPdvTurno(turno);
        movimento.setDataAbertura(new Date());
        movimento.setHoraAbertura(FormatValor.getInstance().formatarHora(new Date()));
        movimento.setTotalSuprimento(valorSuprimento);

        atualizar();

        addSuprimento(valorSuprimento, "Saldo inicial de caixa");
        imprimeAbertura();
    }

    @Transactional
    public void realizarFechamento() throws ParseException {
        movimento = FacesUtil.getMovimento();
        movimento.setDataFechamento(new Date());
        movimento.setStatusMovimento("F");
        movimento.setHoraFechamento(FormatValor.getInstance().formatarHora(new Date()));
        repository.atualizarNamedQuery("PdvMovimento.Fechamento", new Date(), FormatValor.getInstance().formatarHora(new Date()), movimento.getId());

        FacesUtil.setMovimento(null);
    }


    @Transactional
    private void addSuprimento(BigDecimal valor, String obs) {
        PdvSuprimento suprimento = new PdvSuprimento();
        suprimento.setPdvMovimento(movimento);
        suprimento.setDataSuprimento(new Date());
        suprimento.setObservacao(obs);
        suprimento.setValor(valor);

        pdvSuprimentoRepository.salvar(suprimento);
    }

    @Transactional
    private void addSangria(BigDecimal valor, String obs) {
        PdvSangria sangria = new PdvSangria();
        sangria.setPdvMovimento(movimento);
        sangria.setDataSangria(new Date());
        sangria.setObservacao(obs);
        sangria.setValor(valor);

        pdvSangriaRepository.salvar(sangria);
    }

    @Transactional
    public void lancarMovimento(int tipo, BigDecimal valor, String obs) {
        if (tipo == 1) {
            addSuprimento(valor, obs);
            lancaSuprimento(valor);
        } else {
            addSangria(valor, obs);
            lancaSangria(valor);
        }
    }


    public void lancaVenda(BigDecimal valor) {
        movimento = FacesUtil.getMovimento();
        movimento.setTotalVenda(Biblioteca.soma(movimento.getTotalVenda(), valor));
        atualizar();
    }

    public void lancaVenda(BigDecimal valorVenda, BigDecimal desconto, BigDecimal troco) {
        movimento = FacesUtil.getMovimento();
        movimento.setTotalVenda(Biblioteca.soma(movimento.getTotalVenda(), valorVenda));
        movimento.setTotalDesconto(Biblioteca.soma(movimento.getTotalDesconto(), Optional.ofNullable(desconto).orElse(BigDecimal.ZERO)));
        movimento.setTotalTroco(Biblioteca.soma(movimento.getTotalDesconto(), Optional.ofNullable(troco).orElse(BigDecimal.ZERO)));
        atualizar();
    }

    public void lancaSangria(BigDecimal valor) {
        movimento = FacesUtil.getMovimento();
        movimento.setTotalSangria(Biblioteca.soma(movimento.getTotalSangria(), valor));
        atualizar();
    }

    public void lancaSuprimento(BigDecimal valor) {
        movimento = FacesUtil.getMovimento();
        movimento.setTotalSuprimento(Biblioteca.soma(movimento.getTotalSuprimento(), valor));
        atualizar();
    }

    public void lancaAcrescimo(BigDecimal valor) {
        movimento = FacesUtil.getMovimento();
        movimento.setTotalAcrescimo(Biblioteca.soma(movimento.getTotalAcrescimo(), valor));
        atualizar();
    }

    public void lancaRecebimento(BigDecimal valor) {
        movimento = FacesUtil.getMovimento();
        movimento.setTotalRecebido(Biblioteca.soma(movimento.getTotalRecebido(), valor));
        atualizar();
    }

    public void lancaTroco(BigDecimal valor) {
        movimento = FacesUtil.getMovimento();
        movimento.setTotalTroco(Biblioteca.soma(movimento.getTotalTroco(), valor));
        atualizar();
    }

    @Transactional
    public void atualizar() {
        this.movimento.calcularTotalFinal();
        this.movimento = repository.atualizar(movimento);
        FacesUtil.setMovimento(movimento);
    }


    public PdvMovimento verificarMovimento(Empresa empresa) throws ChronosException {


        if (movimento == null) {

            UsuarioDTO user = FacesUtil.getUsuarioSessao();

            if (user.getOperador() == null) {
                return null;
            }

            movimento = pesquisarMovimento(user.getOperador().getId());

            FacesUtil.setMovimento(movimento);

        }

        return movimento;
    }

    private PdvMovimento pesquisarMovimento(Integer idoperador) {
        List<Filtro> filtros = new ArrayList<>();
        filtros.add(new Filtro("statusMovimento", "A"));
        filtros.add(new Filtro("pdvOperador.id", idoperador));
        Object[] atributos;
        atributos = new Object[]{"idGerenteSupervisor", "dataAbertura", "horaAbertura", "dataFechamento",
                "horaFechamento", "totalSuprimento", "totalSangria", "totalVenda", "totalDesconto", "totalAcrescimo",
                "totalFinal", "totalRecebido", "totalTroco", "totalCancelado", "statusMovimento", "empresa.id",
                "pdvCaixa.id", "pdvCaixa.codigo", "pdvOperador.id", "pdvTurno.id"};
        return repository.get(PdvMovimento.class, filtros, atributos);
    }


    private void imprimeAbertura() {
        try {
            linhasRelatorio = new StringBuilder();
            linhasRelatorio.setLength(0);
            append(Biblioteca.repete("=", 48));
            append("               ABERTURA DE CAIXA ");
            append("");
            append("DATA DE ABERTURA: " + FormatValor.getInstance().formatarData(movimento.getDataAbertura()));
            append("HORA DE ABERTURA: " + movimento.getHoraAbertura());
            append("Caixa principal  OPERADOR: padrão");
            append("MOVIMENTO: " + movimento.getId());
            append(Biblioteca.repete("=", 48));
            append("");
            append("SUPRIMENTO...: " + FormatValor.getInstance().formatoDecimal("V", movimento.getTotalSuprimento().doubleValue()));
            append("");
            append("");
            append("");
            append(" ________________________________________ ");
            append("             VISTO DO CAIXA ");
            append("");
            append("");
            append("");
            append(" ________________________________________ ");
            append("           VISTO DO SUPERVISOR ");

            Map map = new HashMap();
            File fileTemp = new File(context.getRealPath("/") + System.getProperty("file.separator") + "temp");
            if (!fileTemp.exists()) {
                fileTemp.mkdir();
            }
            map.put("CONTEUDO", linhasRelatorio.toString());
            String caminhoJasper = "/relatorios/nfce/relatorioMovimento.jasper";
            InputStream inputStream = this.getClass().getResourceAsStream(caminhoJasper);
            JasperPrint jp = JasperFillManager.fillReport(inputStream, map, new JREmptyDataSource());
            byte[] pdfFile = JasperExportManager.exportReportToPdf(jp);
            Path localPdf = getDefault().getPath(fileTemp.getPath());
            nomeImpressaoMovimento = "movimento.pdf";
            ArquivoUtil.getInstance().escrever(pdfFile, nomeImpressaoMovimento, localPdf.toString());
            PrimeFaces.current().ajax().addCallbackParam("movimentoIniciado", true);

        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao imprimir o relatório de abertura de movimento.\n.", ex);

        }
    }

    private void append(String texto) {
        linhasRelatorio.append(texto + "\n");
    }


    public PdvMovimento getMovimento() {
        return movimento;
    }

}
