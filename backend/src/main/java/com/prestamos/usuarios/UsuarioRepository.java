package com.prestamos.usuarios;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioRepository {

    private static final RowMapper<Usuario> MAPPER = (rs, fila) -> new Usuario(
            rs.getLong("id"),
            rs.getString("nombre"),
            rs.getString("email")
    );

    private final JdbcTemplate jdbc;

    public UsuarioRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Usuario> listar() {
        return jdbc.query("SELECT id, nombre, email FROM usuarios ORDER BY nombre", MAPPER);
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return jdbc.query("SELECT id, nombre, email FROM usuarios WHERE id = ?", MAPPER, id)
                .stream()
                .findFirst();
    }
}
