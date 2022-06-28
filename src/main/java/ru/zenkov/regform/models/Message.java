package ru.zenkov.regform.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Message {
    private String username;
    private boolean regStatusDecision;

    public Message(String username) {
        this.username = username;
    }
}
