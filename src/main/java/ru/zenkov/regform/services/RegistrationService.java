package ru.zenkov.regform.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.zenkov.regform.models.Message;
import ru.zenkov.regform.models.User;

import java.util.concurrent.TimeoutException;

@Service
public class RegistrationService {

    private final MessagingService messaging;
    private final MailSenderService mailSender;

    public RegistrationService(MessagingService messaging, MailSenderService mailSender) {
        this.messaging = messaging;
        this.mailSender = mailSender;
    }

    public boolean registrationUser(User user) throws TimeoutException {
        String GREETING = "Приветсвуем";
        String SUBJECT = "Статус регистрации";
        String POSITIVE_ANSWER = "Ваш аккаунт успешно зарегистрирован";
        String NEGATIVE_ANSWER = "к сожадению Вам отказано в регистрации";

        Message regAnswer = messaging.doRequest(user.getUsername());
        String mes = String.format("%s, %s, %s", GREETING, regAnswer.getUsername(),
                regAnswer.isRegStatusDecision() ? POSITIVE_ANSWER : NEGATIVE_ANSWER);
        mailSender.send(user.getEmail(), SUBJECT, mes);
        return regAnswer.isRegStatusDecision();

    }
}
