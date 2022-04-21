package com.example.easymrp.service.auth;

import com.example.easymrp.exception.duplicate.DuplicateEntryException;
import com.example.easymrp.model.auth.User;
import com.example.easymrp.repository.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public User createNew(User user) {
        if (userRepository.existsByUsernameIgnoreCase(user.getUsername()))
            throw new DuplicateEntryException();

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(roleService.getOrCreate("ROLE_USER", false, Set.of("READ_PRIVILEGE"))));
        return userRepository.save(user);
    }

    public User createNewAdmin(User user) {
        if (userRepository.existsByUsernameIgnoreCase(user.getUsername()))
            throw new DuplicateEntryException();

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(roleService.getOrCreate("ROLE_ADMIN", false, Set.of("READ_PRIVILEGE", "WRITE_PRIVILEGE"))));
        return userRepository.save(user);
    }

    public boolean isExist(String username) {
        return userRepository.existsByUsernameIgnoreCase(username);
    }


}
