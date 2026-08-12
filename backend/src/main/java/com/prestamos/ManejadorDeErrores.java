package com.prestamos;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;


@RestControllerAdvice
public class ManejadorDeErrores {

    /** Errores de negocio lanzados a proposito desde los servicios. */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> deNegocio(ResponseStatusException ex) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("estado", ex.getStatusCode().value());
        cuerpo.put("mensaje", ex.getReason());
        return ResponseEntity.status(ex.getStatusCode()).body(cuerpo);
    }

    /** Errores de validacion del @Valid: reune todos los campos invalidos. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> deValidacion(MethodArgumentNotValidException ex) {
        String detalle = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("estado", 400);
        cuerpo.put("mensaje", detalle.isBlank() ? "Datos invalidos" : detalle);
        return ResponseEntity.badRequest().body(cuerpo);
    }
}
