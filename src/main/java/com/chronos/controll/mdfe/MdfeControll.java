package com.chronos.controll.mdfe;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.dto.DocFiscalDto;
import com.chronos.exception.EmissorException;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.StatusTransmissao;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.comercial.MdfeService;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by john on 16/06/18.
 */
@Named
@ViewScoped
public class MdfeControll extends AbstractControll<MdfeCabecalho> implements Serializable {


    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<Municipio> municipioRepository;
    @Inject
    private Repository<Veiculo> veiculoRepository;

    private String uf = "AL";
    private Municipio municipioInicio;
    private Municipio municipioFim;

    private MdfePercurso mdfePercurso;
    private MdfePercurso mdfePercursoSelecionado;

    private MdfeInformacaoSeguro mdfeInformacaoSeguro;
    private MdfeInformacaoSeguro mdfeInformacaoSeguroSelecionado;

    private MdfeLacre mdfeLacre;
    private MdfeLacre mdfeLacreSelecionado;

    private MdfeRodoviarioPedagio mdfeRodoviarioPedagio;
    private MdfeRodoviarioPedagio mdfeRodoviarioPedagioSelecionado;

    private MdfeInformacaoNfe mdfeInformacaoNfe;
    private MdfeInformacaoNfe mdfeInformacaoNfeSelecionado;

    private MdfeInformacaoCte mdfeInformacaoCteSelecionado;

    private List<MdfeInformacaoNfe> listaMdfeInformacaoNfe;
    private List<MdfeInformacaoCte> listaMdfeInformacaoCte;

    private MdfeRodoviarioMotorista mdfeRodoviarioMotorista;
    private MdfeRodoviarioMotorista mdfeRodoviarioMotoristaSelecionado;

    private MdfeRodoviarioVeiculo mdfeRodoviarioVeiculo;
    private MdfeRodoviarioVeiculo mdfeRodoviarioVeiculoSelecionado;

    private Veiculo veiculo;


    private DocFiscalDto docFiscal;

    private boolean duplicidade;

    @Inject
    private MdfeService service;

    private boolean dadosSalvos = true;

    @PostConstruct
    @Override
    public void init() {
        super.init();
        docFiscal = new DocFiscalDto("", 0, 55);
        municipioFim = new Municipio();
        municipioInicio = new Municipio();
    }


    //<editor-fold defaultstate="collapsed" desc="procedimentos crud">


    @Override
    public ERPLazyDataModel<MdfeCabecalho> getDataModel() {

        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setDao(dao);
            dataModel.setClazz(MdfeCabecalho.class);

        }

        dataModel.setAtributos(new Object[]{"numeroMdfe", "dataHoraEmissao", "chaveAcesso", "digitoVerificador", "valorCarga", "statusMdfe"});

        return dataModel;
    }

    @Override
    public void doCreate() {
        mdfePercurso = new MdfePercurso();

        listaMdfeInformacaoCte = new ArrayList<>();
        listaMdfeInformacaoNfe = new ArrayList<>();
        super.doCreate();
        getObjeto().setEmpresa(empresa);
        getObjeto().setMdfeRodoviario(new MdfeRodoviario());
        getObjeto().getMdfeRodoviario().setMdfeCabecalho(getObjeto());
    }

    @Override
    public void doEdit() {
        MdfeCabecalho mdfe = dao.getJoinFetch(getObjetoSelecionado().getId(), MdfeCabecalho.class);
        setObjeto(mdfe);
        setTelaGrid(false);
    }

    @Override
    public void salvar() {

        try {
            MdfeCabecalho mdfeCabecalho = service.salvar(getObjeto(), listaMdfeInformacaoCte, listaMdfeInformacaoNfe);
            setObjeto(mdfeCabecalho);
        } catch (Exception ex) {
            dadosSalvos = false;
            ex.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao salvar o registro", ex);
        }
    }

    // </editor-fold>


    //<editor-fold defaultstate="collapsed" desc="procedimentos tranmissao">

    public void transmitir() {
        try {
            if (dadosSalvos) {

                boolean estoque = isTemAcesso("ESTOQUE");
                StatusTransmissao status = service.enviarMdfe(getObjeto());
                if (status == StatusTransmissao.AUTORIZADA) {

                    Mensagem.addInfoMessage("NFe transmitida com sucesso");
                } else {
                    duplicidade = status == StatusTransmissao.DUPLICIDADE;
                }

            } else {
                Mensagem.addInfoMessage("Antes de enviar a NF-e é necessário salvar as informações!");
            }
        } catch (EmissorException ex) {
            if (ex.getMessage().contains("Read timed out")) {
                try {
                    getObjeto().setStatusMdfe(StatusTransmissao.ENVIADA.getCodigo());
                    dao.atualizar(getObjeto());
                } catch (Exception ex1) {

                }
            }
            ex.printStackTrace();
            Mensagem.addErrorMessage("Erro ao transmitir\n", ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("Erro ao transmitir\n", ex);
        }
    }

    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="procedimentos crud veiculo">


    public void incluirVeiculo() {
        mdfeRodoviarioVeiculo = new MdfeRodoviarioVeiculo();
        mdfeRodoviarioVeiculoSelecionado = null;
        dadosSalvos = false;
    }

    public void alterarVeiculo() {
        mdfeRodoviarioVeiculo = new MdfeRodoviarioVeiculo();
        mdfeRodoviarioVeiculo = mdfeRodoviarioVeiculoSelecionado;
        dadosSalvos = false;
    }

    public void salvarVeiculo() {

        if (!getObjeto().getMdfeRodoviario().getListaMdfeRodoviarioVeiculo().contains(mdfeRodoviarioVeiculo)) {
            getObjeto().getMdfeRodoviario().getListaMdfeRodoviarioVeiculo().add(mdfeRodoviarioVeiculo);

            Mensagem.addInfoMessage("Veiculo Adicionado com Sucesso!");

        } else {
            Mensagem.addInfoMessage("Registro já foi adicionado!");
        }

    }

    public void excluirVeiculo() {
        getObjeto().getMdfeRodoviario().getListaMdfeRodoviarioVeiculo().remove(mdfeRodoviarioVeiculoSelecionado);
        Mensagem.addInfoMessage("Registro excluído com sucesso!");
        dadosSalvos = false;
    }

    public List<Veiculo> getListVeiculos(String nome) {
        List<Veiculo> list = new ArrayList<>();
        try {
            list = veiculoRepository.getEntitys(Veiculo.class, "placa", nome);
        } catch (Exception ex) {

        }
        return list;
    }

    public void selecionarVeiculo(SelectEvent event) {

        Veiculo veiculo = (Veiculo) event.getObject();
        mdfeRodoviarioVeiculo = service.definirMdfeVeiculo(veiculo);
        mdfeRodoviarioVeiculo.setMdfeRodoviario(getObjeto().getMdfeRodoviario());

    }

    // </editor-fold>


    //<editor-fold defaultstate="collapsed" desc="procedimentos crud condutor">

    public void incluirCondutor() {
        mdfeRodoviarioMotorista = new MdfeRodoviarioMotorista();
        mdfeRodoviarioMotorista.setMdfeRodoviario(getObjeto().getMdfeRodoviario());
        mdfeRodoviarioMotoristaSelecionado = null;
        dadosSalvos = false;
    }

    public void alterarCondutor() {
        mdfeRodoviarioMotorista = new MdfeRodoviarioMotorista();
        mdfeRodoviarioMotorista = mdfeRodoviarioMotoristaSelecionado;
        dadosSalvos = false;
    }

    public void salvarCondutor() {

        if (!getObjeto().getMdfeRodoviario().getListaMdfeRodoviarioMotorista().contains(mdfeRodoviarioMotorista) || mdfeRodoviarioMotoristaSelecionado != null) {
            mdfeRodoviarioMotorista.setCpf(mdfeRodoviarioMotorista.getCpf().replaceAll("\\D", ""));
            getObjeto().getMdfeRodoviario().getListaMdfeRodoviarioMotorista().add(mdfeRodoviarioMotorista);
            if (mdfeRodoviarioMotoristaSelecionado == null) {
                Mensagem.addInfoMessage("Adicionado com Sucesso!");
            } else {
                Mensagem.addInfoMessage("Alterado com Sucesso!");
            }
        } else {
            Mensagem.addInfoMessage("Registro já foi adicionado!");
        }

    }

    public void excluirCondutor() {
        getObjeto().getMdfeRodoviario().getListaMdfeRodoviarioMotorista().remove(mdfeRodoviarioMotoristaSelecionado);
        Mensagem.addInfoMessage("Registro excluído com sucesso!");
        dadosSalvos = false;
    }

    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="procedimentos crud seguro">

    public void incluirSeguro() {
        mdfeInformacaoSeguro = new MdfeInformacaoSeguro();
        mdfeInformacaoSeguro.setMdfeCabecalho(getObjeto());
    }

    public void alterarSeguro() {
        mdfeInformacaoSeguro = new MdfeInformacaoSeguro();
        mdfeInformacaoSeguro = mdfeInformacaoSeguroSelecionado;

    }

    public void excluirSeguro() {
        getObjeto().getListaMdfeInformacaoSeguro().remove(mdfeInformacaoSeguroSelecionado);
        Mensagem.addInfoMessage("Registro excluído com sucesso!");

    }

    public void salvarSeguro() {
        if (mdfeInformacaoSeguro.getId() == null) {
            getObjeto().getListaMdfeInformacaoSeguro().add(mdfeInformacaoSeguro);
            Mensagem.addInfoMessage("Seguro adcionado com Sucesso!");

        }
    }


    // </editor-fold>


    //<editor-fold defaultstate="collapsed" desc="procedimentos com doc">

    public void incluirDoc(int modelo) {
        docFiscal = new DocFiscalDto("", 0, modelo);
        dadosSalvos = false;
    }


    public void salvarDoc() {
        service.addDocFiscal(getObjeto(), docFiscal, listaMdfeInformacaoNfe, listaMdfeInformacaoCte);
        dadosSalvos = false;
    }

    public void alterarDoc() {

        dadosSalvos = false;
    }


    public void excluirNfe() {
        listaMdfeInformacaoNfe.remove(mdfeInformacaoNfeSelecionado);
        getObjeto().setQuantidadeTotalNfe(listaMdfeInformacaoNfe.size());
        getObjeto().setQuantidadeTotalCte(0);
        Mensagem.addInfoMessage("Registro excluído com sucesso!");
        dadosSalvos = false;
    }

    public void excluirCte() {
        listaMdfeInformacaoCte.remove(mdfeInformacaoCteSelecionado);
        getObjeto().setQuantidadeTotalCte(listaMdfeInformacaoCte.size());
        getObjeto().setQuantidadeTotalNfe(0);
        Mensagem.addInfoMessage("Registro excluído com sucesso!");
        dadosSalvos = false;
    }


    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="procedimentos com percurso">


    public void salvarPercurso() {

        mdfePercursoSelecionado = null;
        mdfePercurso.setMdfeCabecalho(getObjeto());
        dadosSalvos = false;
        boolean existe = getObjeto().getListaMdfePercurso().stream()
                .filter(p -> p.getUfPercurso().equals(mdfePercurso.getUfPercurso()))
                .findAny()
                .isPresent();
        if (!existe) {
            getObjeto().getListaMdfePercurso().add(mdfePercurso);
            Mensagem.addInfoMessage("Adicionado com Sucesso!");
            mdfePercurso = new MdfePercurso();
        } else {
            Mensagem.addErrorMessage("Percurso já informado!");
        }


    }


    public void excluirPercurso() {
        getObjeto().getListaMdfePercurso().remove(mdfePercursoSelecionado);
        Mensagem.addInfoMessage("Registro excluído com sucesso!");
        dadosSalvos = false;
    }

    // </editor-fold>


    //<editor-fold defaultstate="collapsed" desc="procedimentos com pedagio">

    public void incluirPedagio() {
        mdfeRodoviarioPedagio = new MdfeRodoviarioPedagio();
        mdfeRodoviarioPedagio.setMdfeRodoviario(getObjeto().getMdfeRodoviario());
        mdfeRodoviarioPedagioSelecionado = null;
        dadosSalvos = false;
    }

    public void alterarPedagio() {
        mdfeRodoviarioPedagio = new MdfeRodoviarioPedagio();
        mdfeRodoviarioPedagio = mdfeRodoviarioPedagioSelecionado;
        dadosSalvos = false;
    }

    public void salvarPedagio() {

        if (!getObjeto().getMdfeRodoviario().getListaMdfeRodoviarioPedagio().contains(mdfeRodoviarioPedagio) || mdfeRodoviarioPedagioSelecionado != null) {
            if (mdfeRodoviarioPedagio.getId() == null) {
                getObjeto().getMdfeRodoviario().getListaMdfeRodoviarioPedagio().add(mdfeRodoviarioPedagio);
                if (mdfeRodoviarioPedagioSelecionado == null) {
                    Mensagem.addInfoMessage("Adicionado com Sucesso!");
                } else {
                    Mensagem.addInfoMessage("Alterado com Sucesso!");
                }
            }
        } else {
            Mensagem.addInfoMessage("Registro já foi adicionado!");
        }

    }

    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="procedimentos com lacres">

    public void incluirLacre() {
        mdfeLacre = new MdfeLacre();
        mdfeLacre.setMdfeCabecalho(getObjeto());
        mdfeLacreSelecionado = null;
        dadosSalvos = false;
    }

    public void alterarLacre() {
        mdfeLacre = new MdfeLacre();
        mdfeLacre = mdfeLacreSelecionado;
        dadosSalvos = false;
    }

    public void salvarLacre() {

        if (!getObjeto().getListaMdfeLacre().contains(mdfeLacre) || mdfeLacreSelecionado != null) {
            if (mdfeLacre.getId() == null) {
                getObjeto().getListaMdfeLacre().add(mdfeLacre);
                if (mdfeLacreSelecionado == null) {
                    Mensagem.addInfoMessage("Adicionado com Sucesso!");
                } else {
                    Mensagem.addInfoMessage("Alterado com Sucesso!");
                }
            }
            dadosSalvos = false;
        } else {
            Mensagem.addInfoMessage("Registro já foi adicionado!");
        }
    }

    public void excluirLacre() {
        getObjeto().getListaMdfeLacre().remove(mdfeLacreSelecionado);
        Mensagem.addInfoMessage("Registro excluído com sucesso!");
        dadosSalvos = false;
    }

    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="procedimentos com seleção de municipio">
    public void definirUfInicio() {
        municipioInicio = new Municipio();
        uf = getObjeto().getUfInicio();
        getObjeto().setListaMdfeMunicipioCarregamento(new HashSet<>());
    }

    public void definirUfFim() {
        municipioFim = new Municipio();
        uf = getObjeto().getUfFim();
        getObjeto().setListaMdfeMunicipioDescarregamento(new HashSet<>());
    }

    public void selecionarMunicipio(SelectEvent event) {
        Municipio mun = (Municipio) event.getObject();
        String id = event.getComponent().getId();
        service.definirMunicipio(getObjeto(), mun, id);
    }

    public List<Municipio> getListaMunicipio(String nome) {
        List<Municipio> municipios = new ArrayList<>();
        try {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro(Filtro.AND, "uf.sigla", Filtro.IGUAL, uf));
            filtros.add(new Filtro(Filtro.AND, "nome", Filtro.LIKE, nome));
            municipios = municipioRepository.getEntitys(Municipio.class, filtros);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return municipios;
    }

    // </editor-fold>


    @Override
    protected Class<MdfeCabecalho> getClazz() {
        return MdfeCabecalho.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "MDFE";
    }

    @Override
    protected boolean auditar() {
        return false;
    }


    public Municipio getMunicipioInicio() {
        return municipioInicio;
    }

    public void setMunicipioInicio(Municipio municipioInicio) {
        this.municipioInicio = municipioInicio;
    }

    public Municipio getMunicipioFim() {
        return municipioFim;
    }

    public void setMunicipioFim(Municipio municipioFim) {
        this.municipioFim = municipioFim;
    }

    public MdfePercurso getMdfePercurso() {
        return mdfePercurso;
    }

    public void setMdfePercurso(MdfePercurso mdfePercurso) {
        this.mdfePercurso = mdfePercurso;
    }

    public MdfePercurso getMdfePercursoSelecionado() {
        return mdfePercursoSelecionado;
    }

    public void setMdfePercursoSelecionado(MdfePercurso mdfePercursoSelecionado) {
        this.mdfePercursoSelecionado = mdfePercursoSelecionado;
    }

    public MdfeInformacaoSeguro getMdfeInformacaoSeguro() {
        return mdfeInformacaoSeguro;
    }

    public void setMdfeInformacaoSeguro(MdfeInformacaoSeguro mdfeInformacaoSeguro) {
        this.mdfeInformacaoSeguro = mdfeInformacaoSeguro;
    }

    public MdfeInformacaoSeguro getMdfeInformacaoSeguroSelecionado() {
        return mdfeInformacaoSeguroSelecionado;
    }

    public void setMdfeInformacaoSeguroSelecionado(MdfeInformacaoSeguro mdfeInformacaoSeguroSelecionado) {
        this.mdfeInformacaoSeguroSelecionado = mdfeInformacaoSeguroSelecionado;
    }

    public MdfeLacre getMdfeLacre() {
        return mdfeLacre;
    }

    public void setMdfeLacre(MdfeLacre mdfeLacre) {
        this.mdfeLacre = mdfeLacre;
    }

    public MdfeLacre getMdfeLacreSelecionado() {
        return mdfeLacreSelecionado;
    }

    public void setMdfeLacreSelecionado(MdfeLacre mdfeLacreSelecionado) {
        this.mdfeLacreSelecionado = mdfeLacreSelecionado;
    }

    public MdfeRodoviarioPedagio getMdfeRodoviarioPedagio() {
        return mdfeRodoviarioPedagio;
    }

    public void setMdfeRodoviarioPedagio(MdfeRodoviarioPedagio mdfeRodoviarioPedagio) {
        this.mdfeRodoviarioPedagio = mdfeRodoviarioPedagio;
    }

    public MdfeRodoviarioPedagio getMdfeRodoviarioPedagioSelecionado() {
        return mdfeRodoviarioPedagioSelecionado;
    }

    public void setMdfeRodoviarioPedagioSelecionado(MdfeRodoviarioPedagio mdfeRodoviarioPedagioSelecionado) {
        this.mdfeRodoviarioPedagioSelecionado = mdfeRodoviarioPedagioSelecionado;
    }

    public List<MdfeInformacaoNfe> getListaMdfeInformacaoNfe() {
        return listaMdfeInformacaoNfe;
    }

    public void setListaMdfeInformacaoNfe(List<MdfeInformacaoNfe> listaMdfeInformacaoNfe) {
        this.listaMdfeInformacaoNfe = listaMdfeInformacaoNfe;
    }

    public List<MdfeInformacaoCte> getListaMdfeInformacaoCte() {
        return listaMdfeInformacaoCte;
    }

    public void setListaMdfeInformacaoCte(List<MdfeInformacaoCte> listaMdfeInformacaoCte) {
        this.listaMdfeInformacaoCte = listaMdfeInformacaoCte;
    }

    public MdfeInformacaoNfe getMdfeInformacaoNfe() {
        return mdfeInformacaoNfe;
    }

    public void setMdfeInformacaoNfe(MdfeInformacaoNfe mdfeInformacaoNfe) {
        this.mdfeInformacaoNfe = mdfeInformacaoNfe;
    }

    public MdfeInformacaoNfe getMdfeInformacaoNfeSelecionado() {
        return mdfeInformacaoNfeSelecionado;
    }

    public void setMdfeInformacaoNfeSelecionado(MdfeInformacaoNfe mdfeInformacaoNfeSelecionado) {
        this.mdfeInformacaoNfeSelecionado = mdfeInformacaoNfeSelecionado;
    }

    public MdfeInformacaoCte getMdfeInformacaoCteSelecionado() {
        return mdfeInformacaoCteSelecionado;
    }

    public void setMdfeInformacaoCteSelecionado(MdfeInformacaoCte mdfeInformacaoCteSelecionado) {
        this.mdfeInformacaoCteSelecionado = mdfeInformacaoCteSelecionado;
    }

    public DocFiscalDto getDocFiscal() {
        return docFiscal;
    }

    public void setDocFiscal(DocFiscalDto docFiscal) {
        this.docFiscal = docFiscal;
    }

    public MdfeRodoviarioMotorista getMdfeRodoviarioMotorista() {
        return mdfeRodoviarioMotorista;
    }

    public void setMdfeRodoviarioMotorista(MdfeRodoviarioMotorista mdfeRodoviarioMotorista) {
        this.mdfeRodoviarioMotorista = mdfeRodoviarioMotorista;
    }

    public MdfeRodoviarioMotorista getMdfeRodoviarioMotoristaSelecionado() {
        return mdfeRodoviarioMotoristaSelecionado;
    }

    public void setMdfeRodoviarioMotoristaSelecionado(MdfeRodoviarioMotorista mdfeRodoviarioMotoristaSelecionado) {
        this.mdfeRodoviarioMotoristaSelecionado = mdfeRodoviarioMotoristaSelecionado;
    }

    public MdfeRodoviarioVeiculo getMdfeRodoviarioVeiculo() {
        return mdfeRodoviarioVeiculo;
    }

    public void setMdfeRodoviarioVeiculo(MdfeRodoviarioVeiculo mdfeRodoviarioVeiculo) {
        this.mdfeRodoviarioVeiculo = mdfeRodoviarioVeiculo;
    }

    public MdfeRodoviarioVeiculo getMdfeRodoviarioVeiculoSelecionado() {
        return mdfeRodoviarioVeiculoSelecionado;
    }

    public void setMdfeRodoviarioVeiculoSelecionado(MdfeRodoviarioVeiculo mdfeRodoviarioVeiculoSelecionado) {
        this.mdfeRodoviarioVeiculoSelecionado = mdfeRodoviarioVeiculoSelecionado;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }
}
