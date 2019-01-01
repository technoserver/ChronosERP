package com.chronos.controll.gerencial;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.RestricaoSistema;
import com.chronos.modelo.entidades.Usuario;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class RestricaoSistemaControll extends AbstractControll<RestricaoSistema> implements Serializable {

    @Inject
    private Repository<Usuario> usuarioRepository;

    @Override
    public void salvar() {

        try {

            RestricaoSistema restricao = dao.get(RestricaoSistema.class, "usuario.id", getObjeto().getUsuario().getId());

            if (restricao != null && !getObjeto().equals(restricao)) {
                throw new ChronosException("Já foram definidas retrições para esse usuário");
            }
            super.salvar();

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro ao salvar as retrições", ex);
            }
        }


    }

    public List<Usuario> getListUsuario(String nome) {
        List<Usuario> list = new ArrayList<>();
        try {

            list = usuarioRepository.getEntitys(Usuario.class, "login", nome, new Object[]{"login"});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }


    @Override
    protected Class<RestricaoSistema> getClazz() {
        return RestricaoSistema.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "RESTRICAO_SISTEMA";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
