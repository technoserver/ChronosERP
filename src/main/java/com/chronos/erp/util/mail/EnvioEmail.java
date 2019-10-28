package com.chronos.erp.util.mail;

import com.chronos.erp.dto.ConfEmail;
import com.chronos.erp.modelo.entidades.NfeCabecalho;
import com.chronos.erp.util.ArquivoUtil;
import com.outjected.email.api.*;
import com.outjected.email.impl.MailMessageImpl;
import com.outjected.email.impl.SimpleMailConfig;
import org.apache.commons.mail.MultiPartEmail;
import org.springframework.util.StringUtils;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import java.io.File;


/**
 * Created by john on 11/12/17.
 */
@Stateless
public class EnvioEmail {

    public SessionConfig getMailConfig(ConfEmail confEmail) {

        SimpleMailConfig config = new SimpleMailConfig();
        config.setServerHost(confEmail.getHost());
        config.setServerPort(confEmail.getPorta());
        config.setEnableSsl(confEmail.isSsl());
        config.setEnableTls(confEmail.isTsl());
        config.setUsername(confEmail.getUsuario());
        config.setPassword(confEmail.getSenha());
        config.setAuth(true);


        return config;
    }


    public void enviarNfeEmail(ConfEmail confEmail, NfeCabecalho nfe) {
        MailMessage message = new MailMessageImpl(getMailConfig(confEmail));
        String htmlBody = "<html><body><b>Hello</b> World!</body></html>";
        //  .bodyHtml(new VelocityTemplate(this.getClass().getResourceAsStream("/email/nfe.template")))

        String pastaXml = ArquivoUtil.getInstance().getPastaXmlNfeProcessada(nfe.getEmpresa().getCnpj());
        String arquivoPdf = pastaXml + System.getProperty("file.separator") + nfe.getNomePdf();
        String caminhoXml = pastaXml + System.getProperty("file.separator") + nfe.getNomeXml();
        String assunto = StringUtils.isEmpty(confEmail.getAssunto()) ? "NFe " + nfe.getNumero() : confEmail.getAssunto() + " " + nfe.getNumero();
        EmailMessage email = new MailMessageImpl(getMailConfig(confEmail))
                .to(nfe.getDestinatario().getEmail())
                .subject(assunto)
                .bodyHtmlTextAlt("", confEmail.getAssunto()).importance(MessagePriority.HIGH)
                .addAttachment(ContentDisposition.ATTACHMENT, new File(caminhoXml))
                .addAttachment(ContentDisposition.ATTACHMENT, new File(arquivoPdf))
                .send();
//        message.to(nfe.getDestinatario().getEmail())
//                .subject("NFe " + nfe.getNumero())
//                .bodyHtml(new VelocityTemplate(this.getClass().getResourceAsStream("/email/nfe.template")))
//                .importance(MessagePriority.HIGH)
//                .put("nfe", nfe)
//                .put("locale", new Locale("pt", "BR"))
//                .contentType(ContentType.ALTERNATIVE)
//                .send();

    }

    @Asynchronous
    public void enviarEmail(ConfEmail confEmail, String assunto, String msg, String emaiEnvio) {
        MailMessage message = new MailMessageImpl(getMailConfig(confEmail));
        message.to(emaiEnvio)
                .subject(assunto)
                .bodyText(msg)
                .importance(MessagePriority.HIGH)
                .contentType(ContentType.ALTERNATIVE)
                .send();
    }

    public void enviarEmail(ConfEmail confEmail) {
        MultiPartEmail email = new MultiPartEmail();
        email.setHostName(confEmail.getHost());
//        email.setFrom(nfeConfiguracao.getEmpresa().getEmail());
//        email.addTo(getObjeto().getDestinatario().getEmail());
//        email.setSubject(nfeConfiguracao.getEmailAssunto());
//        email.setMsg(nfeConfiguracao.getEmailTexto());
    }
}
