package br.com.logique.starter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private MessageSource messageSource;

    @Autowired
    public MessageService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String get(String chave) {
        return get(chave, null);
    }

    public String get(String chave, Object[] param) {
        return messageSource.getMessage(chave, param, LocaleContextHolder.getLocale());
    }
}
