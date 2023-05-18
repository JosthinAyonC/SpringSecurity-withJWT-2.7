package com.bezkoder.springjwt.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.services.UserService;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserService usuarioService;

    @GetMapping
    public List<User> listar(){
        return usuarioService.listarTodos();
    }
    
    @GetMapping("/{id}")
    public User getUsuarioById(@PathVariable("id") Long id ){
        return usuarioService.listarById(id);
    } 
    
    @PostMapping
    public User insertar(@RequestBody User usuarioBody){
        return usuarioService.insertar(usuarioBody);
    }
    
    @PutMapping("/editar/{id}")
    public User actualizar(@PathVariable Long id, @RequestBody User usuarioBody){
        usuarioBody.setId(id);
        return usuarioService.actualizar(id, usuarioBody);
    }

    @PutMapping("/eliminar/{id}")
    public List<User> eliminar(@PathVariable Long id){
        return usuarioService.eliminar(id);
    } 
}
