package br.com.logique.starter.service;

import br.com.logique.starter.model.RespostaExecucao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;

@Slf4j
@Service
public class EmailService {

    private String remetente;

    private JavaMailSender sender;
    private MessageService message;
    private TemplateEngine template;

    @Autowired
    public EmailService(@Value("${spring.mail.username}") String remetente,
                        JavaMailSender sender, TemplateEngine template, MessageService message) {
        this.sender = sender;
        this.message = message;
        this.template = template;
        this.remetente = remetente;
    }

    public RespostaExecucao enviarEmailRedefinicaoSenha(String destinatario, String nome, String codigo) {

        Context context = new Context();
        context.setVariable("nome", nome);
        context.setVariable("codigo", codigo);
        context.setVariable("titulo", message.get("titulo.email.redefinicao-senha"));

        String content = template.process("email-reset-senha", context);

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(remetente);
            messageHelper.setTo(destinatario);
            messageHelper.setSubject(message.get("titulo.email.redefinicao-senha"));
            messageHelper.setText(content, true);
            addInlineBodyPart(messageHelper, "logo", "static/logo.png", MimeTypeUtils.IMAGE_PNG_VALUE);
        };

        try {
            sender.send(messagePreparator);

            log.info("E-mail enviado com sucesso.");
            return RespostaExecucao.ok();

        } catch (MailException e) {
            log.error("Não foi possível enviar e-mail de redefinição de senha.", e);
            return RespostaExecucao.erro(message.get("erro.email.envio"));
        }
    }

    private void addInlineBodyPart(MimeMessageHelper messageHelper, String cid, String filePath, String contentType) throws MessagingException {
        messageHelper.addInline(cid, () -> new ClassPathResource(filePath).getInputStream(), contentType);
    }
}
