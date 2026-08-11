package com.prestamos.equipos;

/**
 * Representa una fila de la tabla "equipos".
 * Es un record: una clase inmutable que solo transporta datos.
 */
public record Equipo(
        Long id,
        String nombre,
        String tipo,
        String numeroSerie,
        String estado
) {
}