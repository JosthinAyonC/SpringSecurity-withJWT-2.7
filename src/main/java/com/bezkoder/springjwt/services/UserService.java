package com.bezkoder.springjwt.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
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
        user.setPassword(encoder.encode(user.getPassword()));
        return usuarioRepository.save(user);
    }

    public User actualizar(User user) {
        Optional<User> optionalUsuario = usuarioRepository.findById(user.getId());
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

    //en la ruta /api/auth/signup va a guardar nuevo usuario
    public ResponseEntity<?> registrar(@Valid User signUpRequest) {
        
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
        if (signUpRequest.getUsername() == null || signUpRequest.getEmail() == null || 
        signUpRequest.getLastname() == null || signUpRequest.getFirstname() == null || 
        signUpRequest.getPassword() == null) {
            
            return ResponseEntity
            .badRequest()
            .body(new MessageResponse("Error: Campos vacios!"));
        }
        
        Set<Role> strRoles = signUpRequest.getRoles();
        signUpRequest.setRoles(strRoles);
        signUpRequest.setStatus("A");
        signUpRequest.setPassword(encoder.encode(signUpRequest.getPassword()));
        
        usuarioRepository.save(signUpRequest);
        
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    //Metodo para copiar campos no nulos
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
        if (fuente.getRoles() != null) {
        destino.setRoles(fuente.getRoles());
        }
        if (fuente.getStatus() != null) {
            destino.setStatus(fuente.getStatus());
        }
    }

}
