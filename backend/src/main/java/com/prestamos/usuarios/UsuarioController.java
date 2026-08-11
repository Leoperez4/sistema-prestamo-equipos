package com.prestamos.usuarios;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Expone los usuarios de prueba para que el frontend pueda ofrecer
 * un selector de "estoy usando la aplicacion como...".
 *
 * El reto no pide autenticacion, asi que no se implemento. En un
 * sistema real el usuario saldria del token de sesion, nunca de un
 * parametro enviado por el cliente.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioRepository repository;

    public UsuarioController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Usuario> listar() {
        return repository.listar();
    }
}
