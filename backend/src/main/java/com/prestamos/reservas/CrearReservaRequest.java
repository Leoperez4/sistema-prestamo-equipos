package com.prestamos.reservas;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

/**
 * Datos que llegan en el cuerpo del POST /api/reservas.
 * Es distinto de Reserva: esto es lo que ENTRA, aquello es lo que SALE.
 * El cliente no puede mandar id, estado ni fecha de creacion:
 * esos los decide el servidor.
 */
public record CrearReservaRequest(
        @NotNull @Positive Long usuarioId,
        @NotNull @Positive Long equipoId,
        @NotNull LocalDate fechaInicio,
        @NotNull LocalDate fechaFin
) {
}
