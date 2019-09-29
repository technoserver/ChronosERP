package com.chronos.service.comercial;

import com.chronos.bo.nfe.VendaToNFe;
import com.chronos.dto.ConfiguracaoEmissorDTO;
import com.chronos.dto.ProdutoVendaDTO;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.AcaoLog;
import com.chronos.modelo.enuns.Modulo;
import com.chronos.modelo.enuns.StatusTransmissao;
import com.chronos.repository.EstoqueRepository;
import com.chronos.repository.Repository;
import com.chronos.service.AbstractService;
import com.chronos.service.ChronosException;
import com.chronos.service.financeiro.FinLancamentoReceberService;
import com.chronos.service.gerencial.AuditoriaService;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import com.chronos.util.Biblioteca;
import com.chronos.util.Constantes;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.Mensagem;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by john on 13/12/17.
 */
public class OsService extends AbstractService<OsAbertura> {

    @Inject
    private Repository<OsAbertura> repository;
    @Inject
    private Repository<NfeCabecalho> nfeRepository;
    @Inject
    private EstoqueRepository estoqueRepositoy;

    private Set<OsProdutoServico> itens;
    @Inject
    private NfeService nfeService;
    @Inject
    private FinLancamentoReceberService finLancamentoReceberService;

    @Inject
    private Repository<VendaCondicoesParcelas> parcelasRepository;

    @Inject
    private ComissaoService comissaoService;

    @Inject
    private AuditoriaService auditoriaService;


    public OsAbertura salvar(OsAbertura os) throws ChronosException {


        if (os.getCondicoesPagamento().getVistaPrazo().equals("1") && os.getCliente().getSituacaoForCli().getBloquear().equals("S")) {
            throw new ChronosException("Cliente com restrinções de bloqueio");
        }

        if (os.isNovo()) {
            repository.salvar(os);
            os.setNumero("OS" + new DecimalFormat("0000000").format(os.getId()));
            os = repository.atualizar(os);
        } else {
            os = repository.atualizar(os);
        }
        return os;
    }


    public OsAbertura salvarItem(OsAbertura os, OsProdutoServico item) throws ChronosException {
        itens = os.getListaOsProdutoServico();
        Optional<OsProdutoServico> itemOptional = buscarItem(item.getProduto());
        BigDecimal quantidade = item.getQuantidade();

        if (itemOptional.isPresent()) {
            item = itemOptional.get();
            item.setQuantidade(quantidade);
        } else {
            item.setQuantidade(quantidade);
            itens.add(item);
        }

        item.setTipo(item.getProduto().getServico() != null && item.getProduto().getServico().equals("S") ? 1 : 0);
        os.calcularValores();
        return salvar(os);
    }

    @Transactional
    public void transmitirNFe(OsAbertura os, ModeloDocumento modelo, boolean atualizarEstoque) throws Exception {


        NfeCabecalho nfe;
        List<VendaCondicoesParcelas> parcelas = parcelasRepository.getEntitys(VendaCondicoesParcelas.class, "vendaCondicoesPagamento.id", os.getCondicoesPagamento().getId());
        os.getCondicoesPagamento().setParcelas(parcelas);
        VendaToNFe vendaNfe = new VendaToNFe(modelo, os);
        nfe = vendaNfe.gerarNfe();
        nfe.setOs(os);
        ConfiguracaoEmissorDTO configuracaoEmissorDTO = nfeService.instanciarConfNfe(nfe.getEmpresa(), nfe.getModeloDocumento(), true);
        nfe.setSerie(configuracaoEmissorDTO.getSerie());
        nfe.setAmbiente(configuracaoEmissorDTO.getWebserviceAmbiente());
        nfe.setCsc(configuracaoEmissorDTO.getCsc());
        Integer statusOs = os.getStatus();
        StatusTransmissao status = nfeService.transmitirNFe(nfe, atualizarEstoque);

        if (status == StatusTransmissao.AUTORIZADA) {
            Mensagem.addInfoMessage("OS Faturada com sucesso");
            if (!status.equals(12)) {
                BigDecimal comissao = gerarComissoes(os);
                repository.atualizar(os);
            }
        }


    }

    @Transactional
    public void encerrar(OsAbertura os) throws ChronosException {
        os.setStatus(12);

        List<ProdutoVendaDTO> produtos = new ArrayList<>();
        os.getListaOsProdutoServico()
                .stream()
                .filter(p -> p.getProduto().getServico().equalsIgnoreCase("N"))
                .forEach(p -> {
                    produtos.add(new ProdutoVendaDTO(p.getId(), p.getQuantidade()));
                });

        estoqueRepositoy.atualizaEstoqueVerificado(os.getEmpresa().getId(), produtos);
        finLancamentoReceberService.gerarLancamento(os.getId(), os.getValorTotal(), os.getCliente(),
                os.getCondicoesPagamento(), Modulo.VENDA.getCodigo(), Constantes.FIN.NATUREZA_VENDA, os.getEmpresa());
        os = repository.saveAndFlush(os);

        auditoriaService.gerarLog(AcaoLog.ENCERRAR_OS, "Encerrado OS " + os.getNumero(), "OS");

        BigDecimal comissao = gerarComissoes(os);

        os.setValorComissao(comissao);

        repository.atualizar(os);

    }

    @Transactional
    public void cancelarOs(OsAbertura os, boolean estoque, String justificativa) throws Exception {
        boolean cancelado = true;

        if (os.getStatus().equals(13)) {
            NfeCabecalho nfe = nfeRepository.get(os.getIdnfeCabecalho(), NfeCabecalho.class);
            nfe.setJustificativaCancelamento(justificativa);


            cancelado = nfeService.cancelarNFe(nfe, estoque);
            if (cancelado) {
                finLancamentoReceberService.excluirFinanceiro(os.getNumero(), Modulo.OS);
            }
        } else {
            finLancamentoReceberService.excluirFinanceiro(os.getNumero(), Modulo.OS);
        }

        if (estoque && cancelado) {
            for (OsProdutoServico item : os.getListaOsProdutoServico()) {
                if (item.getProduto().getServico().equals("N")) {
                    estoqueRepositoy.atualizaEstoqueEmpresaControle(os.getEmpresa().getId(), item.getProduto().getId(), item.getQuantidade());
                }

            }
        }
        os.setStatus(11);
        salvar(os);
        Mensagem.addInfoMessage("OS cancelada com sucesso");
    }

    public void gerarDanfe(OsAbertura os) throws Exception {
        NfeCabecalho nfe = nfeRepository.get(os.getIdnfeCabecalho(), NfeCabecalho.class);

        nfeService.danfe(nfe);
    }

    public void gerarOSDoOrcamento(VendaOrcamentoCabecalho orcamento, OsAbertura os) {


        for (VendaOrcamentoDetalhe d : orcamento.getListaVendaOrcamentoDetalhe()) {
            OsProdutoServico item = new OsProdutoServico();
            item.setOsAbertura(os);
            item.setProduto(d.getProduto());
            item.setQuantidade(d.getQuantidade());
            item.setTaxaDesconto(d.getTaxaDesconto());
            item.setValorDesconto(d.getValorDesconto());
            item.setValorSubtotal(d.getValorSubtotal());
            item.setValorTotal(d.getValorTotal());
            item.setValorUnitario(d.getValorUnitario());
            item.setTipo(0);
            os.getListaOsProdutoServico().add(item);
        }


        os.setCliente(orcamento.getCliente());
        os.setCondicoesPagamento(orcamento.getCondicoesPagamento());
        os.setVendedor(orcamento.getVendedor());
        os.setValorComissao(orcamento.getValorComissao());
        os.setValorTotal(orcamento.getValorTotal());
        os.setEmpresa(orcamento.getEmpresa());
        os.setVendaOrcamentoCabecalho(orcamento);
        os.getVendedor().setNome(os.getVendedor().getColaborador().getPessoa().getNome());

        os.calcularValores();


    }

    private Optional<OsProdutoServico> buscarItem(Produto produto) {
        return itens.stream().filter(i -> i.getProduto().equals(produto))
                .findAny();
    }

    private BigDecimal gerarComissoes(OsAbertura os) throws ChronosException {


        BigDecimal comissaoTecnico = BigDecimal.ZERO;

        BigDecimal comissaoVendedor = BigDecimal.ZERO;

        if (os.getTecnico().getComissao() != null && os.getTecnico().getComissao().signum() > 0
                && os.getValorTotalServico().signum() > 0) {
            comissaoTecnico = Biblioteca.calcularValorPercentual(os.getValorTotalServico(), os.getTecnico().getComissao());
            comissaoService.gerarComissao("A", "C", comissaoTecnico, os.getValorTotalServico(),
                    os.getId().toString(), os.getTecnico().getColaborador(), Modulo.OS);
        }

        if (os.getVendedor() != null && os.getVendedor().getComissao() != null && os.getVendedor().getComissao().signum() > 0
                && os.getValorTotalProduto().signum() > 0) {
            comissaoVendedor = Biblioteca.calcularValorPercentual(os.getValorTotalProduto(), os.getVendedor().getComissao());

            comissaoService.gerarComissao("A", "C", comissaoVendedor, os.getValorTotalProduto(),
                    os.getId().toString(), os.getVendedor().getColaborador(), Modulo.OS);
        }

        return Biblioteca.soma(comissaoTecnico, comissaoVendedor);
    }


    @Override
    protected Class<OsAbertura> getClazz() {
        return OsAbertura.class;
    }
}
