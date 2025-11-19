package com.ordep.syncotable.config;

import com.ordep.syncotable.model.Role;
import com.ordep.syncotable.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {

        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            Role admin = new Role();
            admin.setName("ROLE_ADMIN");
            roleRepository.save(admin);
        }

        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            Role user = new Role();
            user.setName("ROLE_USER");
            roleRepository.save(user);
        }

    }

}
