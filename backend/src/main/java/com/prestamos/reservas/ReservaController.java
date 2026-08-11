package com.prestamos.reservas;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService service;

    public ReservaController(ReservaService service) {
        this.service = service;
    }

    /** POST /api/reservas */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reserva crear(@Valid @RequestBody CrearReservaRequest peticion) {
        return service.crear(peticion);
    }

    /** GET /api/reservas?usuarioId=1 */
    @GetMapping
    public List<Reserva> listar(@RequestParam Long usuarioId) {
        return service.listarPorUsuario(usuarioId);
    }

    /** PATCH /api/reservas/5/devolver */
    @PatchMapping("/{id}/devolver")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void devolver(@PathVariable Long id) {
        service.devolver(id);
    }
}
