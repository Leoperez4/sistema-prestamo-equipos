package com.prestamos.usuarios;

/** Una fila de la tabla "usuarios". */
public record Usuario(
        Long id,
        String nombre,
        String email
) {
}
