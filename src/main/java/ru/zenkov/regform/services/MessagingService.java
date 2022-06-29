package ru.zenkov.regform.services;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.zenkov.regform.models.Message;
import ru.zenkov.regform.models.User;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class MessagingService {

    private Message send(String username) {
        return new Message(username);
    }

    /**
     * По сути та самая внешная система, которая выносит решения по контретному пользователю
     *
     * @param message сообщение, отправляемое во внешнюю систему
     * @return Тело ответа
     */
    private Message receive(Message message) throws TimeoutException {
        if (shouldThrowTimeout()) {
            sleep();

            throw new TimeoutException("Timeout!");
        }

        if (shouldSleep()) {
            sleep();
        }
        message.setRegStatusDecision(makeDecision());
        return message;
    }

    /**
     * Делаем запрос во внешнюю систему по конкрутному пользователю
     *
     * @param username логин пользователя
     * @return Тело ответа
     */
    public Message doRequest(String username) throws TimeoutException {
        final Message message = send(username);

        return receive(message);
    }

    @SneakyThrows
    private static void sleep() {
        Thread.sleep(TimeUnit.MINUTES.toMillis(1));
    }

    private static boolean shouldSleep() {
        return new Random().nextInt(10) == 1;
    }

    private static boolean shouldThrowTimeout() {
        return new Random().nextInt(10) == 1;
    }

    /**
     * С какой-то вероятностью отказываем пользователю в регистрации
     *
     * @return принятое решение, true - одобрено, false - отказ в регистрации
     */
    private static boolean makeDecision() {
        return new Random().nextInt(10) > 4;
    }
}
