package com.omar.shop.data;


import com.omar.shop.model.Role;
import com.omar.shop.model.User;
import com.omar.shop.repository.RoleRepository;
import com.omar.shop.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.util.Set;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${password}")
    private String secretPassword;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_USER");
        createDefaultUserIfNotExists();
        createDefaultRolesIfNotExists(defaultRoles);
        createDefaultAdminIfNotExists();
    }

    private void createDefaultUserIfNotExists() {
        Role userRole = roleRepository.findByName("ROLE_USER");

        for (int i = 1; i <= 5; i++) {
            String defaultUserName = "user" + i + "@gmail.com";
            if (userRepository.existsByEmail(defaultUserName)) {
                continue;
            }
            User user = new User();
            user.setFirstName("The User");
            user.setLastName("User" + i);
            user.setEmail(defaultUserName);
            user.setPassword(passwordEncoder.encode(secretPassword));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);

        }
    }

    private void createDefaultAdminIfNotExists() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        for (int i = 1; i <= 2; i++) {
            String defaultUserName = "admin" + i + "@gmail.com";
            if (userRepository.existsByEmail(defaultUserName)) {
                continue;
            }
            User user = new User();
            user.setFirstName("The admin");
            user.setLastName("Admin" + i);
            user.setEmail(defaultUserName);
            user.setPassword(passwordEncoder.encode(secretPassword));
            user.setRoles(Set.of(adminRole));
            userRepository.save(user);

        }
    }

    private void createDefaultRolesIfNotExists(Set<String> roles) {
        roles.stream()
                .filter(role -> !roleRepository.existsByName(role))
                .map(Role::new)
                .forEach(roleRepository::save);
    }

}
