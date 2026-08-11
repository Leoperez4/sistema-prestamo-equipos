package com.prestamos.equipos;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EquipoService {

    private final EquipoRepository repository;

    public EquipoService(EquipoRepository repository) {
        this.repository = repository;
    }

    public List<Equipo> listar() {
        return repository.listar();
    }
}