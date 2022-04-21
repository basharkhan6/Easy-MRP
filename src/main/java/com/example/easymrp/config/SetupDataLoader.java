package com.example.easymrp.config;

import com.example.easymrp.exception.duplicate.DuplicateEntryException;
import com.example.easymrp.model.auth.User;
import com.example.easymrp.service.auth.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;
    private final UserService userService;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup)
            return;

        try {
            userService.createNew(new User("user", "test@1"));
            userService.createNewAdmin(new User("admin", "test@2"));
        } catch (DuplicateEntryException exception) {
            System.out.println("Already setup");
        }
        alreadySetup = true;
    }

}
