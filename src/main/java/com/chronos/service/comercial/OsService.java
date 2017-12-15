package com.chronos.service.comercial;

import com.chronos.bo.nfe.VendaToNFe;
import com.chronos.dto.ConfiguracaoEmissorDTO;
import com.chronos.infra.enuns.ModeloDocumento;
import com.chronos.modelo.entidades.NfeCabecalho;
import com.chronos.modelo.entidades.OsAbertura;
import com.chronos.modelo.entidades.OsProdutoServico;
import com.chronos.modelo.entidades.Produto;
import com.chronos.modelo.entidades.enuns.StatusTransmissao;
import com.chronos.repository.Repository;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.Mensagem;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.Set;

/**
 * Created by john on 13/12/17.
 */
public class OsService implements Serializable {

    @Inject
    private Repository<OsAbertura> repository;

    private Set<OsProdutoServico> itens;
    @Inject
    private NfeService nfeService;

    public OsAbertura salvar(OsAbertura os) throws Exception {
        if (os.isNovo()) {
            repository.salvar(os);
            os.setNumero("OS" + new DecimalFormat("0000000").format(os.getId()));
            os = repository.atualizar(os);
        } else {
            os = repository.atualizar(os);
        }
        return os;
    }


    public OsAbertura salvarItem(OsAbertura os, OsProdutoServico item) throws Exception {
        itens = os.getListaOsProdutoServico();
        Optional<OsProdutoServico> itemOptional = buscarItem(item.getProduto());
        BigDecimal quantidade = item.getQuantidade();

        if (itemOptional.isPresent()) {
            item = itemOptional.get();
            item.setQuantidade(item.getQuantidade().add(quantidade));
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

        ConfiguracaoEmissorDTO configuracao = nfeService.getConfEmisor(os.getEmpresa(), modelo);
        NfeCabecalho nfe;
        VendaToNFe vendaNfe = new VendaToNFe(modelo, configuracao, os);
        nfe = vendaNfe.gerarNfe();
        nfe.setCsc(configuracao.getCsc());
        nfe.setOs(os);

        StatusTransmissao status = nfeService.transmitirNFe(nfe, configuracao, atualizarEstoque);
        if (status == StatusTransmissao.AUTORIZADA) {
            String msg = modelo == ModeloDocumento.NFE ? "NFe transmitida com sucesso" : "NFCe transmitida com sucesso";
            Mensagem.addInfoMessage(msg);
        }


    }

    private Optional<OsProdutoServico> buscarItem(Produto produto) {
        return itens.stream().filter(i -> i.getProduto().equals(produto))
                .findAny();
    }


}
