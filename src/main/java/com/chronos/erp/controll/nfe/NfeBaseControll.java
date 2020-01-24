package com.chronos.erp.controll.nfe;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.modelo.enuns.StatusTransmissao;
import com.chronos.erp.modelo.view.PessoaCliente;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.service.comercial.NfeService;
import com.chronos.erp.util.jsf.Mensagem;
import com.chronos.transmissor.exception.EmissorException;
import com.chronos.transmissor.infra.enuns.LocalDestino;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import org.primefaces.event.SelectEvent;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NfeBaseControll extends AbstractControll<NfeCabecalho> implements Serializable {


    @Inject
    protected NfeService service;

    protected PessoaCliente pessoaCliente;
    protected String justificativa;
    @Inject
    private Repository<PessoaCliente> pessoaClienteRepository;
    private boolean duplicidade;
    @Inject
    private Repository<TributOperacaoFiscal> operacaoFiscalRepository;

    public void transmitirNfe(boolean iniciarConfiguracao, boolean baixaEstoque) {
        try {

            if (iniciarConfiguracao) {
                service.instanciarConfNfe(empresa, ModeloDocumento.NFE);
            }

            StatusTransmissao status = service.transmitirNFe(getObjeto(), baixaEstoque);
            if (status == StatusTransmissao.AUTORIZADA) {

                Mensagem.addInfoMessage("NFe transmitida com sucesso");
            } else {
                duplicidade = status == StatusTransmissao.DUPLICIDADE;
            }


        } catch (Exception ex) {
            if (ex instanceof ChronosException || ex instanceof EmissorException) {
                if (ex.getMessage().contains("Read timed out")) {
                    getObjeto().setStatusNota(StatusTransmissao.ENVIADA.getCodigo());
                    dao.atualizar(getObjeto());
                } else {
                    Mensagem.addErrorMessage("", ex);
                }
            } else {
                throw new RuntimeException("", ex);
            }

        }
    }

    public void cancelaNfe(boolean iniciarConfiguracao) {
        try {
            if (iniciarConfiguracao) {
                service.instanciarConfNfe(empresa, ModeloDocumento.NFE);
            }
            getObjeto().setJustificativaCancelamento(justificativa);

            boolean estoque = isTemAcesso("ESTOQUE");
            boolean cancelado = service.cancelarNFe(getObjeto(), estoque);
            if (cancelado) {
                Mensagem.addInfoMessage("NFe cancelada com sucesso");
            }

        } catch (Exception ex) {
            if (ex instanceof ChronosException || ex instanceof EmissorException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro ao cancelar NFe ", ex);
            }

        }

    }

    public void cartaCorrecao(boolean iniciarConfiguracao) {
        try {
            if (iniciarConfiguracao) {
                service.instanciarConfNfe(empresa, ModeloDocumento.NFE);
            }
            service.cartaCorrecao(getObjeto(), justificativa);
        } catch (Exception ex) {
            if (ex instanceof ChronosException || ex instanceof EmissorException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro ao gerar a carta de correção", ex);
            }

        }
    }


    public void danfe() {


        try {

            service.danfe(getObjeto());

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro ao gerar o danfe", ex);
            }
        }
    }


    public List<PessoaCliente> getListaPessoaCliente(String nome) {
        List<PessoaCliente> listaPessoaCliente = new ArrayList<>();
        try {
            listaPessoaCliente = pessoaClienteRepository.getEntitys(PessoaCliente.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaPessoaCliente;
    }

    public List<TributOperacaoFiscal> getListaTributOperacaoFiscal(String descricao) {
        List<TributOperacaoFiscal> listaTributOperacaoFiscal = new ArrayList<>();

        try {
            listaTributOperacaoFiscal = operacaoFiscalRepository.getEntitys(TributOperacaoFiscal.class, "descricao", descricao, new Object[]{"descricao", "descricaoNaNf", "cfop", "obrigacaoFiscal", "destacaIpi", "destacaPisCofins", "calculoIssqn", "classificacaoContabilConta"});
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTributOperacaoFiscal;
    }

    public void selecionaPessoaCliente(SelectEvent event) {
        PessoaCliente pessoaCliente = (PessoaCliente) event.getObject();
        definirDestinatario(pessoaCliente);
    }

    protected void definirDestinatario(PessoaCliente pessoaCliente) {
        try {

            Cliente cliente = new Cliente();
            cliente.setId(pessoaCliente.getId());
            getObjeto().setCliente(cliente);

            getObjeto().getDestinatario().setCpfCnpj(pessoaCliente.getCpfCnpj());
            getObjeto().getDestinatario().setNome(pessoaCliente.getNome());
            getObjeto().getDestinatario().setLogradouro(pessoaCliente.getLogradouro());
            getObjeto().getDestinatario().setComplemento(pessoaCliente.getComplemento());
            getObjeto().getDestinatario().setNumero(pessoaCliente.getNumero());
            getObjeto().getDestinatario().setBairro(pessoaCliente.getBairro());
            getObjeto().getDestinatario().setNomeMunicipio(pessoaCliente.getCidade());
            getObjeto().getDestinatario().setCodigoMunicipio(pessoaCliente.getMunicipioIbge());
            getObjeto().getDestinatario().setUf(pessoaCliente.getUf());
            getObjeto().getDestinatario().setCep(pessoaCliente.getCep());
            getObjeto().getDestinatario().setTelefone(pessoaCliente.getFone());
            getObjeto().getDestinatario().setInscricaoEstadual(pessoaCliente.getRgIe());
            getObjeto().getDestinatario().setEmail(pessoaCliente.getEmail());
            getObjeto().getDestinatario().setCodigoPais(1058);
            getObjeto().getDestinatario().setNomePais("Brazil");


            getObjeto().setLocalDestino(LocalDestino.getByUf(empresa.buscarEnderecoPrincipal().getUf(), pessoaCliente.getUf()));
            service.definirIndicadorIe(getObjeto().getDestinatario(), getObjeto().getModeloDocumento());

        } catch (ChronosException e) {
            if (e instanceof ChronosException) {
                Mensagem.addErrorMessage("", e);
            } else {
                throw new RuntimeException("Erro definir destinatário", e);
            }
        }


    }

    protected void instanciarImpostos(NfeDetalhe item) {
        item.setNfeDetalheImpostoIssqn(new NfeDetalheImpostoIssqn());
        item.getNfeDetalheImpostoIssqn().setNfeDetalhe(item);
        item.setNfeDetalheImpostoPis(new NfeDetalheImpostoPis());
        item.getNfeDetalheImpostoPis().setNfeDetalhe(item);
        item.setNfeDetalheImpostoCofins(new NfeDetalheImpostoCofins());
        item.getNfeDetalheImpostoCofins().setNfeDetalhe(item);
        item.setNfeDetalheImpostoIcms(new NfeDetalheImpostoIcms());
        item.getNfeDetalheImpostoIcms().setNfeDetalhe(item);
        item.setNfeDetalheImpostoIpi(new NfeDetalheImpostoIpi());
        item.getNfeDetalheImpostoIpi().setNfeDetalhe(item);
        item.setNfeDetalheImpostoIi(new NfeDetalheImpostoIi());
        item.getNfeDetalheImpostoIi().setNfeDetalhe(item);
    }


    public void limparJustificativa() {
        justificativa = "";
    }

    @Override
    protected Class<NfeCabecalho> getClazz() {
        return NfeCabecalho.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "NFE";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public PessoaCliente getPessoaCliente() {
        return pessoaCliente;
    }

    public void setPessoaCliente(PessoaCliente pessoaCliente) {
        this.pessoaCliente = pessoaCliente;
    }
}
