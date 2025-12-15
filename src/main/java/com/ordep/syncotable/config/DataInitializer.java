package com.ordep.syncotable.config;

import com.ordep.syncotable.model.*;
import com.ordep.syncotable.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

        private final RoleRepository roleRepo;
        private final UserRepository userRepo;
        private final PasswordEncoder passwordEncoder;
        private final CardRepository cardRepo;
        private final CardColumnRepository columnRepo;
        private final CardRowRepository rowRepo;

        @Override
        public void run(String... args) throws Exception {
                if (userRepo.count() == 0) {
                        Role adminRole = roleRepo.save(Role.builder().name("ROLE_ADMIN").build());
                        Role userRole = roleRepo.save(Role.builder().name("ROLE_USER").build());

                        User userAdm = User.builder()
                                        .activated(true)
                                        .email("admin@gmail.com")
                                        .password(passwordEncoder.encode("123"))
                                        .username("admin")
                                        .build();
                        userAdm.setRoles(Set.of(adminRole));
                        userRepo.save(userAdm);

                        User user = User.builder()
                                        .activated(true)
                                        .email("user@gmail.com")
                                        .password(passwordEncoder.encode("123"))
                                        .username("user")
                                        .build();
                        user.setRoles(Set.of(userRole));
                        userRepo.save(user);

                        Card c1 = cardRepo.save(Card.builder()
                                        .title("Funcionários")
                                        .description("Demo")
                                        .createdBy(userAdm)
                                        .build());
                        Card c2 = cardRepo.save(Card.builder()
                                        .title("Produtos")
                                        .description("Demo")
                                        .createdBy(userAdm)
                                        .build());

                        CardColumn nome = columnRepo.save(CardColumn.builder()
                                        .card(c1)
                                        .key("nome")
                                        .label("Nome")
                                        .type("TEXT")
                                        .orderIndex(1)
                                        .required(true)
                                        .visible(true)
                                        .editable(true)
                                        .build());
                        CardColumn idade = columnRepo.save(CardColumn.builder()
                                        .card(c1)
                                        .key("idade")
                                        .label("Idade")
                                        .type("NUMBER")
                                        .orderIndex(2)
                                        .required(false)
                                        .visible(true)
                                        .editable(true)
                                        .build());

                        CardRow r1 = new CardRow();
                        r1.setCard(c1);
                        r1.setValuesJson(new HashMap<>(Map.of("nome", "João", "idade", 30)));
                        r1.setCreatedBy(userAdm);
                        r1.setStatus("ACTIVE");
                        rowRepo.save(r1);

                        CardRow r2 = new CardRow();
                        r2.setCard(c1);
                        r2.setValuesJson(new HashMap<>(Map.of("nome", "Maria", "idade", 25)));
                        r2.setCreatedBy(userAdm);
                        r2.setStatus("ACTIVE");
                        rowRepo.save(r2);
                }

        }

}
