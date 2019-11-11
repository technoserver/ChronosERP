package com.chronos.erp.controll.cadastros;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.ProdutoAtributo;
import com.chronos.erp.modelo.entidades.ProdutoAtributoDetalhe;
import com.chronos.erp.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.stream.IntStream;

@Named
@ViewScoped
public class ProdutoAtributoControll extends AbstractControll<ProdutoAtributo> implements Serializable {

    private static final long serialVersionUID = 1L;

    private ProdutoAtributoDetalhe atributoSelecionado;
    private ProdutoAtributoDetalhe atributo;
    private String valor;


    @Override
    public void doCreate() {
        super.doCreate();
        valor = "";
    }

    @Override
    public void doEdit() {
        super.doEdit();
        ProdutoAtributo atributo = dataModel.getRowData(getObjetoSelecionado().getId().toString());
        setObjeto(atributo);
    }

    @Override
    public void salvar() {

        if (getObjeto().getListaProdutoAtributoDetalhe().isEmpty()) {
            Mensagem.addErrorMessage("Valores não definido");
        } else {
            super.salvar();
        }

    }

    public void salvarAtributo() {

        String valores[] = valor.split(",");

        if (valor.isEmpty()) {
            Mensagem.addErrorMessage("Valor não informado");
        } else if (getObjeto().getListaProdutoAtributoDetalhe().stream().filter(a -> a.getNome().toLowerCase().contains(valor.toLowerCase())).count() > 0) {
            Mensagem.addErrorMessage("Valor já informado");
        } else {
            if (valores.length > 0) {
                for (String v : valores) {
                    atributo = new ProdutoAtributoDetalhe();
                    atributo.setProdutoAtributo(getObjeto());
                    atributo.setNome(v);
                    getObjeto().getListaProdutoAtributoDetalhe().add(atributo);
                }
            } else {

                atributo = new ProdutoAtributoDetalhe();
                atributo.setProdutoAtributo(getObjeto());
                atributo.setNome(valor);
                getObjeto().getListaProdutoAtributoDetalhe().add(atributo);

            }
            valor = "";

        }


    }

    public void removerAtributo() {
        int idx = IntStream.range(0, getObjeto().getListaProdutoAtributoDetalhe().size())
                .filter(i -> getObjeto().getListaProdutoAtributoDetalhe().get(i).getNome().equals(atributoSelecionado.getNome()))
                .findAny().getAsInt();
        getObjeto().getListaProdutoAtributoDetalhe().remove(idx);

    }

    @Override
    protected Class<ProdutoAtributo> getClazz() {
        return ProdutoAtributo.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "PRODUTO_ATRIBUTO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public ProdutoAtributoDetalhe getAtributoSelecionado() {
        return atributoSelecionado;
    }

    public void setAtributoSelecionado(ProdutoAtributoDetalhe atributoSelecionado) {
        this.atributoSelecionado = atributoSelecionado;
    }

    public ProdutoAtributoDetalhe getAtributo() {
        return atributo;
    }

    public void setAtributo(ProdutoAtributoDetalhe atributo) {
        this.atributo = atributo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
