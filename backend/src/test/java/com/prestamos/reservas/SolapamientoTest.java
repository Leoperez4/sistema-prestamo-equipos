package com.prestamos.reservas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de la regla de solapamiento de fechas.
 *
 * La reserva existente es siempre del 10 al 15 de marzo.
 * Cada test comprueba una posicion distinta de la reserva nueva
 * respecto a esa.
 */
class SolapamientoTest {

    private static final LocalDate INICIO_EXISTENTE = fecha(10);
    private static final LocalDate FIN_EXISTENTE = fecha(15);

    private static LocalDate fecha(int dia) {
        return LocalDate.of(2026, 3, dia);
    }

    private static boolean chocaConLaExistente(int inicio, int fin) {
        return Solapamiento.seSolapan(
                fecha(inicio), fecha(fin),
                INICIO_EXISTENTE, FIN_EXISTENTE);
    }

    // ---------- Casos que NO deben solapar ----------

    @Test
    @DisplayName("Una reserva que termina antes de que empiece la existente no solapa")
    void reservaAnterior() {
        assertFalse(chocaConLaExistente(1, 5));
    }

    @Test
    @DisplayName("Una reserva que empieza despues de que termine la existente no solapa")
    void reservaPosterior() {
        assertFalse(chocaConLaExistente(20, 25));
    }

    @Test
    @DisplayName("Empezar justo el dia siguiente al fin de la existente no solapa")
    void diaSiguienteAlFin() {
        assertFalse(chocaConLaExistente(16, 20));
    }

    // ---------- Casos que SI deben solapar ----------

    @Test
    @DisplayName("Un rango identico solapa")
    void rangoIdentico() {
        assertTrue(chocaConLaExistente(10, 15));
    }

    @Test
    @DisplayName("Empezar dentro y terminar fuera solapa")
    void empiezaDentroTerminaFuera() {
        assertTrue(chocaConLaExistente(14, 18));
    }

    @Test
    @DisplayName("Empezar fuera y terminar dentro solapa")
    void empiezaFueraTerminaDentro() {
        assertTrue(chocaConLaExistente(5, 12));
    }

    @Test
    @DisplayName("Una reserva contenida dentro de la existente solapa")
    void contenida() {
        assertTrue(chocaConLaExistente(11, 14));
    }

    @Test
    @DisplayName("Una reserva que envuelve por completo a la existente solapa")
    void envolvente() {
        assertTrue(chocaConLaExistente(5, 20));
    }

    @Test
    @DisplayName("Una reserva de un solo dia dentro del rango solapa")
    void unSoloDia() {
        assertTrue(chocaConLaExistente(12, 12));
    }

    // ---------- El caso limite, decision de negocio ----------

    @Test
    @DisplayName("Empezar el mismo dia en que termina la existente SI solapa: "
            + "el dia de devolucion cuenta como ocupado")
    void bordesQueSeTocan() {
        assertTrue(chocaConLaExistente(15, 20));
    }
}