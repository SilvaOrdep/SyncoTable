package com.ordep.syncotable.config;

import com.ordep.syncotable.model.Role;
import com.ordep.syncotable.model.User;
import com.ordep.syncotable.repository.RoleRepository;
import com.ordep.syncotable.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            User user = User.builder()
                    .activated(true)
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("123"))
                    .username("admin")
                    .build();
            user.setRoles(Set.of(adminRole));
            userRepository.save(user);
        }

        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }

    }

}
