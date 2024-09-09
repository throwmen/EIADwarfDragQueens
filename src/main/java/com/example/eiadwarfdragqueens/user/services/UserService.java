package com.example.eiadwarfdragqueens.user.services;

import com.example.eiadwarfdragqueens.user.dao.UserRepository;
import com.example.eiadwarfdragqueens.user.modelEntity.User;
import com.example.eiadwarfdragqueens.user.modelEntity.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userDetail = repository.findByEmail(username);

        return userDetail.map(user ->
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        getAuthorities(user)
                )).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()));
    }

    public User addUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repository.save(user);
    }

    public void deleteUser(String typeId, Long id) {
        UserId userId = new UserId(typeId, id);
        if (repository.existsById(userId)) {
            repository.deleteById(userId);
        } else {
            throw new UsernameNotFoundException("User not found with id: " + id);
        }
    }

    public Optional<User> findUserById(String typeId, Long id) {
        UserId userId = new UserId(typeId, id);
        return repository.findById(userId);
    }

    public List<User> findAllUsers() {
        return repository.findAll();
    }

}
