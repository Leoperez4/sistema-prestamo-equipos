package com.prestamos.reservas;

import com.prestamos.equipos.Equipo;
import com.prestamos.equipos.EquipoRepository;
import com.prestamos.notificaciones.NotificacionService;
import com.prestamos.usuarios.Usuario;
import com.prestamos.usuarios.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final EquipoRepository equipoRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacionService notificacionService;

    public ReservaService(ReservaRepository reservaRepository,
                          EquipoRepository equipoRepository,
                          UsuarioRepository usuarioRepository,
                          NotificacionService notificacionService) {
        this.reservaRepository = reservaRepository;
        this.equipoRepository = equipoRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacionService = notificacionService;
    }

    /**
     * @Transactional abre una transaccion al entrar y hace COMMIT al salir
     * (o ROLLBACK si se lanza una excepcion). Es lo que mantiene vivo el
     * bloqueo FOR UPDATE durante todo el metodo.
     */
    @Transactional
    public Reserva crear(CrearReservaRequest peticion) {

        // 1. Validacion que no necesita la base de datos
        if (peticion.fechaFin().isBefore(peticion.fechaInicio())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La fecha de fin no puede ser anterior a la de inicio");
        }

        // 2. CAPA 1: bloqueo pesimista.
        Equipo equipo = equipoRepository.bloquearPorId(peticion.equipoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No existe el equipo " + peticion.equipoId()));

        if ("MANTENIMIENTO".equals(equipo.estado())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El equipo \"" + equipo.nombre() + "\" esta en mantenimiento");
        }

        // 3. Comprobacion explicita del solapamiento, usando la funcion pura
        //    que esta cubierta por los tests unitarios.
        List<Reserva> vigentes =
                reservaRepository.listarVigentesPorEquipo(peticion.equipoId());

        Optional<Reserva> conflicto = vigentes.stream()
                .filter(existente -> Solapamiento.seSolapan(
                        peticion.fechaInicio(), peticion.fechaFin(),
                        existente.fechaInicio(), existente.fechaFin()))
                .findFirst();

        if (conflicto.isPresent()) {
            Reserva ocupada = conflicto.get();
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El equipo ya esta reservado del " + ocupada.fechaInicio()
                            + " al " + ocupada.fechaFin());
        }

        // 4. CAPA 2: la red de seguridad. Si dos transacciones llegaran hasta
        //    aqui a la vez, la restriccion EXCLUDE rechaza la segunda y Spring
        //    la convierte en DataIntegrityViolationException.
        try {
            Long id = reservaRepository.insertar(
                    peticion.usuarioId(),
                    peticion.equipoId(),
                    peticion.fechaInicio(),
                    peticion.fechaFin());

            Reserva creada = reservaRepository.buscarPorId(id).orElseThrow();

            usuarioRepository.buscarPorId(peticion.usuarioId()).ifPresent(
                    (Usuario usuario) -> notificacionService.reservaCreada(
                            usuario.email(),
                            usuario.nombre(),
                            equipo.nombre(),
                            creada.fechaInicio(),
                            creada.fechaFin()));

            return creada;

        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Otra reserva para ese equipo y esas fechas se registro primero");
        }
        // 6. Al salir del metodo Spring hace COMMIT y libera el bloqueo.
    }

    @Transactional(readOnly = true)
    public List<Reserva> listarPorUsuario(Long usuarioId) {
        return reservaRepository.listarPorUsuario(usuarioId);
    }

    @Transactional
    public void devolver(Long id) {
        int filasAfectadas = reservaRepository.marcarDevuelta(id);
        if (filasAfectadas == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No existe una reserva activa con id " + id);
        }
    }
}
