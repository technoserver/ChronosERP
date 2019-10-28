package com.chronos.erp.controll.compras;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.CompraCotacao;
import com.chronos.erp.modelo.entidades.CompraCotacaoDetalhe;
import com.chronos.erp.modelo.entidades.CompraFornecedorCotacao;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.util.jsf.FacesUtil;
import com.chronos.erp.util.jsf.Mensagem;
import org.apache.commons.io.FileUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.UploadedFile;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by john on 16/08/17.
 */
@Named
@ViewScoped
public class CompraConfirmaCotacaoControll extends AbstractControll<CompraCotacao> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<CompraFornecedorCotacao> compraFornecedores;
    private CompraFornecedorCotacao compraFornecedorCotacao;
    private CompraFornecedorCotacao compraFornecedorCotacaoSelecionado;


    @Override
    public void salvar() {
        try {
            CompraCotacao cc = dao.getEntityJoinFetch(getObjeto().getId(), CompraCotacao.class);
            setObjeto(cc);

            BigDecimal valorTotalItens;
            boolean fornecedorContemValor = false;
            for (CompraFornecedorCotacao f : getObjeto().getListaCompraFornecedorCotacao()) {
                if (f.getValorSubtotal() != null && f.getTotal() != null) {
                    f = compraFornecedores.getEntityJoinFetch(f.getId(), CompraFornecedorCotacao.class);
                    valorTotalItens = BigDecimal.ZERO;
                    for (CompraCotacaoDetalhe d : f.getListaCompraCotacaoDetalhe()) {
                        valorTotalItens = valorTotalItens.add(d.getValorTotal());
                    }
                    if (f.getTotal().compareTo(valorTotalItens) != 0) {
                        throw new Exception("Valor Total dos Itens do fornecedor '" + f.getFornecedor().getPessoa().getNome() + "' não corresponde ao total informado!\nValor Calculado: " + valorTotalItens);
                    }
                    fornecedorContemValor = true;
                }
            }

            if (!fornecedorContemValor) {
                throw new Exception("Nenhum fornecedor informou valores de cotação");
            }

            getObjeto().setSituacao("C");

            dao.atualizar(getObjeto());

            voltar();
            Mensagem.addInfoMessage("Cotação confirmada com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao salvar o registro!", e);

        }
    }

    public void selecionarFornecedor() {
        try {
            compraFornecedorCotacao = compraFornecedores.getEntityJoinFetch(compraFornecedorCotacaoSelecionado.getId(), CompraFornecedorCotacao.class);
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao buscar os dados do fornecedor!", e);

        }
    }

    public void salvarDadosFornecedor(RowEditEvent event) {
        try {
            if (event != null) {
                compraFornecedorCotacao = (CompraFornecedorCotacao) event.getObject();
            }
            compraFornecedores.atualizar(compraFornecedorCotacao);
            Mensagem.addInfoMessage("Dados do fornecedor salvo com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao salvar o registro!", e);

        }
    }

    public void alteraItemFornecedor(RowEditEvent event) {
        salvarDadosFornecedor(null);
    }

    public void exportaCsvFornecedor() {
        try {
            final int BUFFER = 2048;
            BufferedInputStream origin = null;
            File arquivoZip = File.createTempFile("fornecedores", ".zip");
            arquivoZip.deleteOnExit();
            FileOutputStream dest = new FileOutputStream(arquivoZip);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            byte data[] = new byte[BUFFER];
            String nomeArquivo;
            String linhasArquivo;
            for (CompraFornecedorCotacao f : getObjeto().getListaCompraFornecedorCotacao()) {
                nomeArquivo = "Fornecedor_" + f.getCompraCotacao().getId() + "_" + f.getId();
                linhasArquivo = "Id Produto;Nome Produto;Quantidade;Valor Unitário;Valor Subtotal;Taxa Desconto;Valor Desconto;Valor Total" + System.getProperty("line.separator");
                f = compraFornecedores.getEntityJoinFetch(f.getId(), CompraFornecedorCotacao.class);
                for (CompraCotacaoDetalhe d : f.getListaCompraCotacaoDetalhe()) {
                    linhasArquivo += d.getProduto().getId() + ";" + d.getProduto().getNome() + ";" + d.getQuantidade() + ";" + "0;0;0;0;0" + System.getProperty("line.separator");
                }
                File arquivoCSV = File.createTempFile(nomeArquivo, ".csv");
                arquivoCSV.deleteOnExit();

                PrintStream printStream = new PrintStream(arquivoCSV);
                printStream.print(linhasArquivo);
                printStream.close();

                //adiciona ao arquivo compactado
                FileInputStream fi = new FileInputStream(arquivoCSV);
                origin = new BufferedInputStream(fi, 20);
                ZipEntry entry = new ZipEntry(nomeArquivo + ".csv");
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.close();

            FacesUtil.downloadArquivo(arquivoZip, "fornecedores.zip", true);
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Erro na exportação do arquivo.!", e);

        }
    }

    public void importaCsvFornecedor(FileUploadEvent event) {
        try {
            if (compraFornecedorCotacaoSelecionado == null) {
                throw new Exception("Nenhum fornecedor selecionado!");
            }

            UploadedFile arquivo = event.getFile();

            compraFornecedorCotacao = compraFornecedores.getEntityJoinFetch(compraFornecedorCotacaoSelecionado.getId(), CompraFornecedorCotacao.class);
            File file = File.createTempFile("file", ".tmp");
            FileUtils.copyInputStreamToFile(arquivo.getInputstream(), file);
            List<String> lines = FileUtils.readLines(file);

            int idProduto;
            BigDecimal valorSubtotal = BigDecimal.ZERO;
            BigDecimal taxaDesconto = BigDecimal.ZERO;
            BigDecimal valorDesconto = BigDecimal.ZERO;
            BigDecimal valorTotal = BigDecimal.ZERO;
            String linhaArquivo[];
            List<String> linhaErro = new ArrayList<>();
            for (int i = 0; i < lines.size(); i++) {
                if (i != 0) {
                    linhaArquivo = lines.get(i).split(";");
                    for (CompraCotacaoDetalhe d : compraFornecedorCotacao.getListaCompraCotacaoDetalhe()) {
                        try {
                            idProduto = Integer.valueOf(linhaArquivo[0]);
                            if (d.getProduto().getId() == idProduto) {
                                d.setValorUnitario(BigDecimal.valueOf(Double.valueOf(linhaArquivo[3])));
                                d.setValorSubtotal(BigDecimal.valueOf(Double.valueOf(linhaArquivo[4])));
                                valorSubtotal = valorSubtotal.add(d.getValorSubtotal());
                                d.setTaxaDesconto(BigDecimal.valueOf(Double.valueOf(linhaArquivo[5])));
                                taxaDesconto = taxaDesconto.add(d.getTaxaDesconto());
                                d.setValorDesconto(BigDecimal.valueOf(Double.valueOf(linhaArquivo[6])));
                                valorDesconto = valorDesconto.add(d.getValorDesconto());
                                d.setValorTotal(BigDecimal.valueOf(Double.valueOf(linhaArquivo[7])));
                                valorTotal = valorTotal.add(d.getValorTotal());

                                break;
                            }
                        } catch (Exception e) {
                            linhaErro.add("Linha " + (i + 1) + ": " + lines.get(i) + "\n" + "Erro: " + e.getMessage() + "\n");
                        }
                    }
                }
            }
            compraFornecedorCotacao.setValorSubtotal(valorSubtotal);
            compraFornecedorCotacao.setTaxaDesconto(taxaDesconto);
            compraFornecedorCotacao.setValorDesconto(valorDesconto);
            compraFornecedorCotacao.setTotal(valorTotal);

            compraFornecedores.atualizar(compraFornecedorCotacao);

            String mensagem = "";
            if (!linhaErro.isEmpty()) {
                for (int i = 0; i < linhaErro.size(); i++) {
                    mensagem += linhaErro.get(i);
                }
                throw new Exception(mensagem);
            }
            Mensagem.addInfoMessage("Arquivo importado com sucesso.!");

        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Erro na importação do arquivo.!", e);

        }

    }

    @Override
    protected Class<CompraCotacao> getClazz() {
        return CompraCotacao.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "COMPRA_CONFIRMA_COTACAO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public CompraFornecedorCotacao getCompraFornecedorCotacaoSelecionado() {
        return compraFornecedorCotacaoSelecionado;
    }

    public void setCompraFornecedorCotacaoSelecionado(CompraFornecedorCotacao compraFornecedorCotacaoSelecionado) {
        this.compraFornecedorCotacaoSelecionado = compraFornecedorCotacaoSelecionado;
    }

    public CompraFornecedorCotacao getCompraFornecedorCotacao() {
        return compraFornecedorCotacao;
    }

    public void setCompraFornecedorCotacao(CompraFornecedorCotacao compraFornecedorCotacao) {
        this.compraFornecedorCotacao = compraFornecedorCotacao;
    }
}
