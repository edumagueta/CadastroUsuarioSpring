package com.projeto.CadastroUsuario.controller;

import com.projeto.CadastroUsuario.dto.UsuarioRequest;
import com.projeto.CadastroUsuario.dto.UsuarioResponse;
import com.projeto.CadastroUsuario.model.Usuario;
import com.projeto.CadastroUsuario.service.UsuarioService;
import com.projeto.CadastroUsuario.service.EmailDuplicadoException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/customers")
@Tag(name = "Usuários", description = "Cadastro e consulta de usuários")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @Operation(summary = "Cadastrar usuário", description = "Cadastra os dados básicos e solicita o enriquecimento assíncrono via Kafka.")
    public ResponseEntity<?> incluirUsuario(@RequestBody UsuarioRequest request) {
        try {
            Usuario usuarioSalvo = usuarioService.salvarUsuario(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponse.fromEntity(usuarioSalvo));

        } catch (EmailDuplicadoException error) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("mensagem", error.getMessage()));
        } catch (IllegalArgumentException error) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", error.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "Consultar usuários", description = "Filtra por email ou lista as contas por documento ou nome, ignorando maiúsculas e minúsculas no nome.")
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
            List<Usuario> usuarios = usuarioService.buscarPorDocumento(documento);
            return usuarios.isEmpty()
                    ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado")
                    : ResponseEntity.ok(usuarios.stream().map(UsuarioResponse::fromEntity).toList());
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
    @Operation(summary = "Excluir usuário", description = "Remove um usuário identificado pelo email.")
    public ResponseEntity<?> deletarUsuarioPorEmail(@RequestParam String email) {

        boolean deleted = usuarioService.deletarUsuarioPorEmail(email);

        if (deleted) {
            return ResponseEntity.ok("Usuário removido com sucesso");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
    }
}
