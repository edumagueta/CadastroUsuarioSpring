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
            Usuario usuarioSalvo = usuarioService.salvarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);

        } catch (IllegalArgumentException error) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> buscarUsuario(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String documento) {

        if (email != null) {
            try {
                return ResponseEntity.ok(usuarioService.buscarPorEmail(email));
            } catch (IllegalArgumentException error) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.getMessage());
            }
        }

        if (documento != null) {
            try {
                return ResponseEntity.ok(usuarioService.buscarPorDocumento(documento));
            } catch (IllegalArgumentException error) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.getMessage());
            }
        }

        if (nome != null) {
            List<Usuario> usuarios = usuarioService.buscarUsuariosPorNome(nome);

            if (usuarios.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
            }
            return ResponseEntity.ok(usuarios);
        }
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @DeleteMapping
    public ResponseEntity<?> deletarUsuarioPorEmail(
            @RequestParam String email) {

        boolean deleted = usuarioService.deletarUsuarioPorEmail(email);

        if (deleted) {
            return ResponseEntity.ok("Usuário removido com sucesso");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
    }
}