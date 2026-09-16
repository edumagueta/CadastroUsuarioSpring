package com.projeto.CadastroUsuario.controller;

import com.projeto.CadastroUsuario.model.Usuario;
import com.projeto.CadastroUsuario.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> incluirUsuario(@RequestBody Usuario usuario) {
        try {
            return ResponseEntity.ok(usuarioService.salvarUsuario(usuario));
        } catch (IllegalArgumentException error) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error.getMessage());
        }
    }

    @GetMapping("/customer")
    public ResponseEntity<?> buscarUsuarioPorNome(@RequestParam String nome){
        Optional<Usuario> usuario = usuarioService.buscarUsuarioPorNome(nome);
        if (usuario.isPresent()){
            return ResponseEntity.ok(usuario.get());
        } return ResponseEntity.status(404).body("Usuario não encontrado");
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
        } return ResponseEntity.status(404).body("Cliente não encontrado");
    }
}