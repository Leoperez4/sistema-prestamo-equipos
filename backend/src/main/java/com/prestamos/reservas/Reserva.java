package com.prestamos.reservas;

import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * Una reserva, con el nombre y tipo del equipo ya resueltos
 * para que el frontend no tenga que cruzarlos por su cuenta.
 */
public record Reserva(
        Long id,
        Long usuarioId,
        Long equipoId,
        String equipoNombre,
        String equipoTipo,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        String estado,
        OffsetDateTime creadaEn
) {
}