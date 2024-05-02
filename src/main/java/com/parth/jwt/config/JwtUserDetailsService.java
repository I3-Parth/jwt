package com.parth.jwt.config;

import com.parth.jwt.model.UserEntity;
import com.parth.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserByUsername(username);
        System.out.println("User Data: "+userEntity);
        if(userEntity != null){
            Collection<String> mappedAuthorities = Arrays.asList(userEntity.getRole().toString().split(","));
            return new User(username, passwordEncoder().encode(userEntity.getPassword()),mappedAuthorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        }
        else {
            throw new UsernameNotFoundException("User not found with username: "+username);
        }
    }
}
