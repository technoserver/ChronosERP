package com.chronos.erp.service.configuracao;


import com.chronos.erp.modelo.entidades.EmailConfiguracao;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.outjected.email.api.ContentDisposition;
import com.outjected.email.api.MailMessage;
import com.outjected.email.api.SessionConfig;
import com.outjected.email.impl.MailMessageImpl;
import com.outjected.email.impl.SimpleMailConfig;
import com.outjected.email.impl.attachments.BaseAttachment;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmailService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<EmailConfiguracao> repository;

    public void enviar(int idempresa, List<File> anexos, String email, String assunto, String texto) throws Exception {
        MailMessage message = novaMensagem(idempresa);

        message.to(email)
                .subject(assunto)
                .addAttachments(criarAnexos(anexos).stream().collect(Collectors.toSet()))
                //.bodyHtml(new VelocityTemplate(getClass().getResourceAsStream("/emails/pedido.template")))
                .bodyText(texto)
                .send();

    }

    private SessionConfig getMailConfig(int idempresa) throws ChronosException {

        EmailConfiguracao email = repository.get(EmailConfiguracao.class, "empresa.id", idempresa);

        if (email == null) {
            throw new ChronosException("Email não configurado");
        }

        SimpleMailConfig config = new SimpleMailConfig();
        config.setServerHost(email.getServidorSmtp());
        config.setServerPort(email.getPorta());
        config.setEnableSsl(email.getAutenticaSsl().equals("S"));
        config.setAuth(email.getUsaTls().equals("S"));
        config.setUsername(email.getUsuario());
        config.setPassword(email.getSenha());

        return config;
    }

    private List<BaseAttachment> criarAnexos(List<File> arquivos) throws IOException {

        List<BaseAttachment> anexos = new ArrayList<>();

        for (File file : arquivos) {
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            byte[] fileContent = Files.readAllBytes(file.toPath());
            BaseAttachment anexo = new BaseAttachment(file.getName(), mimeType, ContentDisposition.INLINE, fileContent);
            anexos.add(anexo);
        }

        return anexos;
    }


    private MailMessage novaMensagem(int idempresa) throws ChronosException {
        return new MailMessageImpl(getMailConfig(idempresa));
    }

}
