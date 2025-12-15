package com.ordep.syncotable.security;

import com.ordep.syncotable.model.User;
import com.ordep.syncotable.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

        private final UserRepository userRepository;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = userRepository.findWithRolesByEmail(username)
                                .orElseThrow(() -> new UsernameNotFoundException(
                                                "User not found with email: " + username));

                Set<GrantedAuthority> authorities = user.getRoles() == null ? Set.of()
                                : user.getRoles().stream()
                                                .map(role -> new SimpleGrantedAuthority(role.getName()))
                                                .collect(Collectors.toSet());

                return org.springframework.security.core.userdetails.User
                                .builder()
                                .username(user.getEmail())
                                .password(user.getPassword())
                                .authorities(authorities)
                                .disabled(!user.isActivated())
                                .build();
        }

}
