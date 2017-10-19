package com.chronos.service.gerencial;

import com.chronos.modelo.entidades.Auditoria;
import com.chronos.modelo.entidades.Usuario;
import com.chronos.modelo.entidades.enuns.AcaoLog;
import com.chronos.repository.Repository;
import com.chronos.util.FormatValor;

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
        gerarLog(AcaoLog.BAIXA_PARCELA, conteudo, "Recebimento de parcela", user);
    }

    public void cancelarVenda(int idvenda, String motivo, Usuario user) {
        String conteudo = "cancelado a venda com id :" + idvenda + " por " + user.getLogin() + " pelo seguinte :\n" + motivo;
        gerarLog(AcaoLog.CANCELAR, conteudo, "CANCELAR VENDA", user);
    }

    public void cancelarNFe(String chave, String motivo, Usuario user) {
        String conteudo = "cancelado a NFe com chave :" + chave + " por " + user.getLogin() + " pelo seguinte :\n" + motivo;
        gerarLog(AcaoLog.CANCELAR, conteudo, "CANCELAR NFE", user);
    }

    public void gerarLog(AcaoLog acao, String conteudo, String janela, Usuario user) {
        try {
            Date agora = new Date();
            log = new Auditoria();
            log.setAcao(acao.getNome());
            log.setConteudo(conteudo);
            log.setDataRegistro(agora);
            log.setHoraRegistro(new SimpleDateFormat("hh:mm:ss").format(agora));
            log.setJanelaController(janela);
            log.setUsuario(user);
            repository.salvar(log);
        } catch (Exception ex) {

        }
    }
}
