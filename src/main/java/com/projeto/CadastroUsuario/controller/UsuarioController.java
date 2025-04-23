package com.projeto.CadastroUsuario.controller;

import com.projeto.CadastroUsuario.model.Usuario;
import com.projeto.CadastroUsuario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public Usuario incluirUsuario(@RequestBody Usuario usuario){
        return usuarioService.salvarUsuario(usuario);
    }

    @GetMapping("/customer")
    public ResponseEntity<?> buscarUsuarioPorNome(@RequestParam String nome){
        Optional<Usuario> usuario = usuarioService.buscarUsuarioPorNome(nome);
        if (usuario.isPresent()){
            return ResponseEntity.ok(usuario.get());
        } else return ResponseEntity.status(404).body("Usuario não encontrado");
    }

    @GetMapping
    public List<Usuario> listarUsuarios(){
        return usuarioService.listarUsuarios();
    }

    @DeleteMapping
    public ResponseEntity<?> deletarUsuario(@RequestParam String nome){
        boolean deleted = usuarioService.deletarUsuarioPorNome(nome);
        if (deleted){
            return ResponseEntity.ok("Usuario removido com sucesso");
        } else return ResponseEntity.status(404).body("Cliente não encontrado");
    }
}