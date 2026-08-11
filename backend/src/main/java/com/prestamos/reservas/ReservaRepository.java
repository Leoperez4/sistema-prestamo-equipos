package com.prestamos.reservas;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ReservaRepository {

    /** Trozo comun de las consultas: une reservas con su equipo. */
    private static final String SELECT_BASE = """
            SELECT r.id, r.usuario_id, r.equipo_id,
                   e.nombre AS equipo_nombre,
                   e.tipo   AS equipo_tipo,
                   r.fecha_inicio, r.fecha_fin, r.estado, r.created_at
              FROM reservas r
              JOIN equipos e ON e.id = r.equipo_id
            """;

    private static final RowMapper<Reserva> MAPPER = (rs, fila) -> new Reserva(
            rs.getLong("id"),
            rs.getLong("usuario_id"),
            rs.getLong("equipo_id"),
            rs.getString("equipo_nombre"),
            rs.getString("equipo_tipo"),
            rs.getObject("fecha_inicio", LocalDate.class),
            rs.getObject("fecha_fin", LocalDate.class),
            rs.getString("estado"),
            rs.getObject("created_at", OffsetDateTime.class)
    );

    private final JdbcTemplate jdbc;

    public ReservaRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /** Reservas de un equipo que todavia bloquean fechas (no devueltas). */
    public List<Reserva> listarVigentesPorEquipo(Long equipoId) {
        return jdbc.query(SELECT_BASE + """
                 WHERE r.equipo_id = ?
                   AND r.estado <> 'DEVUELTA'
                """, MAPPER, equipoId);
    }

    public List<Reserva> listarPorUsuario(Long usuarioId) {
        return jdbc.query(SELECT_BASE + """
                 WHERE r.usuario_id = ?
                 ORDER BY r.created_at DESC
                """, MAPPER, usuarioId);
    }

    public Optional<Reserva> buscarPorId(Long id) {
        return jdbc.query(SELECT_BASE + " WHERE r.id = ?", MAPPER, id)
                .stream()
                .findFirst();
    }

    /** Inserta y devuelve el id generado por PostgreSQL. */
    public Long insertar(Long usuarioId, Long equipoId,
                         LocalDate fechaInicio, LocalDate fechaFin) {
        return jdbc.queryForObject("""
                INSERT INTO reservas (usuario_id, equipo_id, fecha_inicio, fecha_fin)
                VALUES (?, ?, ?, ?)
                RETURNING id
                """, Long.class, usuarioId, equipoId, fechaInicio, fechaFin);
    }

    /** Devuelve cuantas filas cambio: 0 si no existia o ya estaba devuelta. */
    public int marcarDevuelta(Long id) {
        return jdbc.update("""
                UPDATE reservas
                   SET estado = 'DEVUELTA'
                 WHERE id = ?
                   AND estado <> 'DEVUELTA'
                """, id);
    }
}
