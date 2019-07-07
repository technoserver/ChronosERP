package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.EmpresaEndereco;
import com.chronos.modelo.entidades.Municipio;
import com.chronos.repository.Repository;
import com.chronos.service.cadastros.MunicipioService;
import com.chronos.util.ArquivoUtil;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

/**
 * Created by john on 24/09/17.
 */
@Named
@ViewScoped
public class EmpresaControll extends AbstractControll<Empresa> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<Municipio> municipios;
    @Inject
    private MunicipioService municipioService;

    private EmpresaEndereco endereco;
    private EmpresaEndereco EnderecoSelecionado;

    private Municipio cidade;
    private List<Municipio> cidades;

    private String logo;

    private Map<String, String> tipo;

    private Empresa matriz;




    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setDataCadastro(new Date());
        getObjeto().setTipoControleEstoque("D");

        endereco = new EmpresaEndereco();

        endereco.setPrincipal("S");
        endereco.setEmpresa(getObjeto());
        getObjeto().setListaEndereco(new HashSet<>());
        getObjeto().getListaEndereco().add(endereco);
        instanciarObjetos();
    }

    @Override
    public void doEdit() {
        super.doEdit();

        endereco = getObjeto().buscarEnderecoPrincipal();
        cidade = new Municipio(0, endereco.getCidade(), endereco.getMunicipioIbge());
        logo = getObjeto().getImagemLogotipo();

        instanciarObjetos();
    }

    @Override
    public void salvar() {
        String cep = endereco.getCep() != null ? endereco.getCep().replaceAll("\\D", "") : "";
        String fone = endereco.getFone() != null ? endereco.getFone().replaceAll("\\D", "") : "";

        endereco.setCep(cep);
        endereco.setFone(fone);

        if (getObjeto().getTipo().equals("F")) {
            getObjeto().setIdempresa(matriz.getIdempresa());
        }
        super.salvar();
        if (empresa.equals(getObjeto())) {
            FacesUtil.setEmpresaUsuario(getObjeto());
        }
    }

    public void atualizarCodIbge() {
        endereco.setMunicipioIbge(cidade.getCodigoIbge());
        endereco.setCidade(cidade.getNome());
        getObjeto().setCodigoIbgeUf(Integer.valueOf(cidade.getCodigoIbge().toString().substring(0, 2)));
        getObjeto().setCodigoIbgeCidade(cidade.getCodigoIbge());

    }

    public void instanciaCidade() {
        cidade = new Municipio();
    }

    public List<Municipio> getMunicipios(String nome) {
        cidades = new LinkedList<>();
        cidades = municipioService.getMunicipios(nome, endereco.getUf());
        return cidades;
    }

    public void uploadImagem(FileUploadEvent event) {
        try {

            UploadedFile arquivo = event.getFile();

            String extensao = arquivo.getFileName().substring(arquivo.getFileName().lastIndexOf("."));

            logo = ArquivoUtil.getInstance().getLogoEmpresa(getObjeto().getCnpj(), extensao);
            getObjeto().setImagemLogotipo(logo);
            Files.copy(arquivo.getInputstream(), Paths.get(new File(logo).toURI()), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            Mensagem.addErrorMessage("Erro ao realizar o upload", e);
            e.printStackTrace();
        }
    }

    public List<Empresa> getListaEmpresa(String nome) {
        List<Empresa> listaEmpresa = new ArrayList<>();
        try {
            listaEmpresa = dao.getEntitys(Empresa.class, "razaoSocial", nome, new Object[]{"razaoSocial"});
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaEmpresa;
    }

    public boolean isPodeCadastrarEmpresa(){
        return FacesUtil.isUserInRole("SOFTHOUSE");
    }

    private void instanciarObjetos() {
        tipo = new LinkedHashMap<>();
        tipo.put("Filial", "F");
        tipo.put("Matriz", "M");

        if (getObjeto().getIdempresa() != null) {
            matriz = dao.get(Empresa.class, "id", getObjeto().getIdempresa(), new Object[]{"razaoSocial"});
        }
    }

    @Override
    protected Class<Empresa> getClazz() {
        return Empresa.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "EMPRESA";
    }

    @Override
    protected boolean auditar() {
        return false;
    }


    public EmpresaEndereco getEndereco() {
        return endereco;
    }

    public void setEndereco(EmpresaEndereco endereco) {
        this.endereco = endereco;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Municipio getCidade() {
        return cidade;
    }

    public void setCidade(Municipio cidade) {
        this.cidade = cidade;
    }

    public Map<String, String> getTipo() {
        return tipo;
    }

    public void setTipo(Map<String, String> tipo) {
        this.tipo = tipo;
    }

    public Empresa getMatriz() {
        return matriz;
    }

    public void setMatriz(Empresa matriz) {
        this.matriz = matriz;
    }
}
