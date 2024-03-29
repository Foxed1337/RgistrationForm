package ru.zenkov.regform.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.zenkov.regform.models.Message;
import ru.zenkov.regform.models.User;

import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

@Service
public class RegistrationService {

    private final MessagingService messaging;
    private final MailSenderService mailSender;

    private final Logger log = Logger.getLogger(this.getClass().getName());

    public RegistrationService(MessagingService messaging, MailSenderService mailSender) {
        this.messaging = messaging;
        this.mailSender = mailSender;
    }

    /**
     * Обращается в messaging для принятия решения о регистрации по конртеному пользователю.
     * Если messaging вернул ответ, то отправляем на почту через mailSender письмо пользователю о результате регистрации.
     *
     * @param user регестрируемый пользователь
     * @return true - пользователь зарегестрирован, false - пользователь не зарегестрирован
     */
    public boolean registrationUser(User user) throws TimeoutException {
        String GREETING = "Приветсвуем";
        String SUBJECT = "Статус регистрации";
        String POSITIVE_ANSWER = "Ваш аккаунт успешно зарегистрирован";
        String NEGATIVE_ANSWER = "к сожадению Вам отказано в регистрации";

        Message regAnswer = messaging.doRequest(user.getUsername());
        String mes = String.format("%s, %s, %s", GREETING, regAnswer.getUsername(),
                regAnswer.isRegStatusDecision() ? POSITIVE_ANSWER : NEGATIVE_ANSWER);
        mailSender.send(user.getEmail(), SUBJECT, mes);
        log.info(String.format("Статус регистрации пользователя %s: %s", regAnswer.getUsername(), regAnswer.isRegStatusDecision()));
        log.info(String.format("Сообщение, отправленное пользователю %s: %s", user.getUsername(), mes));

        return regAnswer.isRegStatusDecision();
    }
}
