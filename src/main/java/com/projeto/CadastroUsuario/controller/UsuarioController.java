package com.projeto.CadastroUsuario.controller;

import com.projeto.CadastroUsuario.dto.UsuarioRequest;
import com.projeto.CadastroUsuario.dto.UsuarioResponse;
import com.projeto.CadastroUsuario.model.Usuario;
import com.projeto.CadastroUsuario.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponse> incluirUsuario(@RequestBody UsuarioRequest request) {
        try {
            Usuario usuarioSalvo = usuarioService.salvarUsuario(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponse.fromEntity(usuarioSalvo));

        } catch (IllegalArgumentException error) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping
    public ResponseEntity<?> buscarUsuario(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String documento) {

        if (email != null) {
            try {
                Usuario usuario = usuarioService.buscarPorEmail(email);
                return ResponseEntity.ok(UsuarioResponse.fromEntity(usuario));
            } catch (IllegalArgumentException error) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.getMessage());
            }
        }

        if (documento != null) {
            try {
                Usuario usuario = usuarioService.buscarPorDocumento(documento);

                return ResponseEntity.ok(UsuarioResponse.fromEntity(usuario));
            } catch (IllegalArgumentException error) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.getMessage());
            }
        }

        if (nome != null) {
            List<Usuario> usuarios = usuarioService.buscarUsuariosPorNome(nome);

            if (usuarios.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
            }

            List<UsuarioResponse> response = usuarios.stream().map(UsuarioResponse::fromEntity).toList();

            return ResponseEntity.ok(response);
        }

        List<UsuarioResponse> response = usuarioService.listarUsuarios().stream()
                .map(UsuarioResponse::fromEntity).toList();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deletarUsuarioPorEmail(@RequestParam String email) {

        boolean deleted = usuarioService.deletarUsuarioPorEmail(email);

        if (deleted) {
            return ResponseEntity.ok("Usuário removido com sucesso");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
    }
}