package com.pacifique.todoapp.config.utils.events;

import com.pacifique.todoapp.model.User;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1905122041950251207L;

    private User user;
    private String verifyUrl;

    public RegistrationCompleteEvent(User user, String verifyUrl) {
        super(user);
        this.user = user;
        this.verifyUrl = verifyUrl;
    }
}
