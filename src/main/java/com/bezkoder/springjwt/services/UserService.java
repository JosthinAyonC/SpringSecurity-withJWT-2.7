package com.bezkoder.springjwt.services;

import java.util.List;
import java.util.Optional;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.SignupRequest;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.repository.RoleRepository;
import com.bezkoder.springjwt.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository usuarioRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;

    public User insertar(User user) {
        return usuarioRepository.save(user);
    }

    public User actualizar(Long id, User user) {
        Optional<User> optionalUsuario = usuarioRepository.findById(id);

        if (optionalUsuario.isEmpty()) {
            return null;
        }
        User usuarioEditado = optionalUsuario.get();
        copiarCamposNoNulos(user, usuarioEditado);
        return usuarioRepository.save(usuarioEditado);
    }

    public List<User> listarTodos() {
        return usuarioRepository.findByEstado();
    }

    public User listarById(Long id) {
        return usuarioRepository.findById(id).get();
    }

    public List<User> eliminar(Long id) {
        usuarioRepository.deleteById(id);
        return usuarioRepository.findByEstado();
    }

    private void copiarCamposNoNulos(User fuente, User destino) {
        if (fuente.getFirstname() != null) {
            destino.setFirstname(fuente.getFirstname());
        }
        if (fuente.getLastname() != null) {
            destino.setLastname(fuente.getLastname());
        }
        if (fuente.getEmail() != null) {
            destino.setEmail(fuente.getEmail());
        }
        if (fuente.getPassword() != null) {
            destino.setPassword(fuente.getPassword());
        }
        // if (fuente.getRoles() != null) {
        // destino.setRoles(fuente.getRoles());
        // }
        if (fuente.getStatus() != null) {
            destino.setStatus(fuente.getStatus());
        }
    }

    public ResponseEntity<?> registrar(@Valid SignupRequest signUpRequest) {
        
        if (usuarioRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (usuarioRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getFirstname(),
                signUpRequest.getLastname(),
                signUpRequest.getStatus());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        usuarioRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
    
}
