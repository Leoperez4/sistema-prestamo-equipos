package com.prestamos.reservas;

import java.time.LocalDate;

/**
 * Regla de negocio central del sistema, aislada y sin dependencias.
 * Al no tocar base de datos ni Spring, se puede probar directamente.
 */
public final class Solapamiento {

    private Solapamiento() {
        // clase de utilidad: no se instancia
    }

    /**
     * Dos rangos de fechas se solapan si cada uno empieza antes o el mismo
     * dia en que el otro termina.
     *
     * Los rangos son CERRADOS en ambos extremos: el dia de devolucion
     * cuenta como ocupado, igual que la restriccion de PostgreSQL,
     * que usa daterange(inicio, fin, '[]').
     */
    public static boolean seSolapan(
            LocalDate aInicio, LocalDate aFin,
            LocalDate bInicio, LocalDate bFin) {

        return !aInicio.isAfter(bFin) && !bInicio.isAfter(aFin);
    }
}