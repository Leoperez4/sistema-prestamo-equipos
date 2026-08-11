package com.prestamos.equipos;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class EquipoRepository {

    /**
     * Convierte una fila del resultado SQL en un objeto Equipo.
     * Aqui se traduce snake_case (base de datos) a camelCase (Java).
     */
    private static final RowMapper<Equipo> MAPPER = (rs, filaNum) -> new Equipo(
            rs.getLong("id"),
            rs.getString("nombre"),
            rs.getString("tipo"),
            rs.getString("numero_serie"),
            rs.getString("estado")
    );

    private final JdbcTemplate jdbc;

    public EquipoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Equipo> listar() {
        return jdbc.query("""
                SELECT id, nombre, tipo, numero_serie, estado
                  FROM equipos
                 ORDER BY tipo, nombre
                """, MAPPER);
    }
}