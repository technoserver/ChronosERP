package com.chronos.erp.service.gerencial;

import com.chronos.erp.dto.UsuarioDTO;
import com.chronos.erp.modelo.entidades.Auditoria;
import com.chronos.erp.modelo.entidades.Usuario;
import com.chronos.erp.modelo.enuns.AcaoLog;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.util.FormatValor;
import com.chronos.erp.util.jsf.FacesUtil;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by john on 16/10/17.
 */
public class AuditoriaService implements Serializable {

    private Auditoria log;
    @Inject
    private Repository<Auditoria> repository;


    public void recebimentoParcela(String cliente, int parcela, String numDoc, BigDecimal valor, Usuario user) {
        String conteudo = "Recebido do cliente : " + cliente + " referente a parcela " + parcela + " com numero de documento " + numDoc + " o valor de :" + FormatValor.getInstance().formatoDecimal("V", valor.doubleValue());
        gerarLog(AcaoLog.BAIXA_PARCELA, conteudo, "Recebimento de parcela");
    }

    public void cancelarVenda(UsuarioDTO usuario, int idvenda, String motivo) {
        String conteudo = "cancelado a venda com id :" + idvenda + " por " + usuario.getLogin() + " pelo seguinte :\n" + motivo;
        gerarLog(AcaoLog.CANCELAR, conteudo, "CANCELAR VENDA");
    }

    public void cancelarNFe(UsuarioDTO usuario, String chave, String motivo, Usuario user) {
        String conteudo = "cancelado a NFe com chave :" + chave + " por " + user.getLogin() + " pelo seguinte :\n" + motivo;
        gerarLog(AcaoLog.CANCELAR, conteudo, "CANCELAR NFE");
    }

    public void gerarLog(AcaoLog acao, String conteudo, String janela) {
        try {
            UsuarioDTO usuario = FacesUtil.getUsuarioSessao();
            Date agora = new Date();
            log = new Auditoria();
            log.setAcao(acao.getNome());
            log.setConteudo(conteudo);
            log.setDataRegistro(agora);
            log.setHoraRegistro(new SimpleDateFormat("hh:mm:ss").format(agora));
            log.setJanelaController(janela);
            log.setUsuario(new Usuario(usuario.getId()));
            repository.salvar(log);
        } catch (Exception ex) {

        }
    }
}
