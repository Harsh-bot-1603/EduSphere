package com.edusphere.edusphere.service;

import com.edusphere.edusphere.dto.request.AuthenticationRequest;
import com.edusphere.edusphere.dto.request.RegisterRequest;
import com.edusphere.edusphere.dto.response.AuthenticationResponse;
import com.edusphere.edusphere.entity.Role;
import com.edusphere.edusphere.entity.User;
import com.edusphere.edusphere.exception.UserAlreadyExistsException;
import com.edusphere.edusphere.repository.RoleRepository;
import com.edusphere.edusphere.repository.UserRepository;
import com.edusphere.edusphere.security.JwtService;
import com.edusphere.enums.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse login(AuthenticationRequest request){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtService.generateToken(userDetails);
        return new AuthenticationResponse(jwt);
    }
    public AuthenticationResponse register(RegisterRequest request){
        if(userRepository.existsByEmail(request.getUsername())){
            throw new UserAlreadyExistsException("Username Already Exists");
        }

        Role studentRole = roleRepository.
                findByName(RoleType.STUDENT)
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        User user = User.builder()
                .name(request.getName())
                .email(request.getUsername())
                .password(
                        passwordEncoder.encode(request.getPassword())
                )
                .role(studentRole)
                .build();
        User savedUser = userRepository.save(user);
        String jwt = jwtService.generateToken(savedUser);
        return new AuthenticationResponse(jwt);
    }
}
