package com.ordep.syncotable.service.user;

import com.ordep.syncotable.dto.user.UserDto;
import com.ordep.syncotable.mapper.UserMapper;
import com.ordep.syncotable.model.Role;
import com.ordep.syncotable.model.User;
import com.ordep.syncotable.repository.RoleRepository;
import com.ordep.syncotable.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(UserDto dto) {

        User user = userMapper.mapToEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        Role role = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new EntityNotFoundException("Role não encontrado"));
        user.setRoles(Set.of(role));
        user.setActivated(true);
        userRepository.save(user);

    }

    @Override
    public User findUserByEmail(String email) {

        return userRepository.findByEmail(email).orElse(null);

    }

    @Override
    public List<UserDto> findAllUsers() {

        return userRepository.findAll().stream().map(userMapper::mapToDto).toList();

    }

}