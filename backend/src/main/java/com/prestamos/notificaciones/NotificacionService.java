package com.prestamos.notificaciones;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * Envia la notificacion de reserva creada.
 *
 * En local el correo va por SMTP al puerto 1025, donde escucha MailHog
 * (contenedor de Docker). MailHog no reenvia nada a internet: captura el
 * mensaje y lo muestra en su bandeja web en http://localhost:8025
 *
 * Nunca falla hacia afuera: si el servidor de correo esta caido se registra
 * el error y la reserva sigue siendo valida. Una notificacion no entregada
 * no puede invalidar una operacion de negocio ya confirmada.
 */
@Service
public class NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionService.class);

    private final MailSender mailSender;
    private final String remitente;

    public NotificacionService(MailSender mailSender,
                               @Value("${app.mail.remitente}") String remitente) {
        this.mailSender = mailSender;
        this.remitente = remitente;
    }

    public void reservaCreada(String destinatario, String nombreUsuario,
                              String nombreEquipo, Object fechaInicio, Object fechaFin) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setFrom(remitente);
            mensaje.setTo(destinatario);
            mensaje.setSubject("Reserva confirmada: " + nombreEquipo);
            mensaje.setText("""
                    Hola %s,

                    Tu solicitud de prestamo ha sido registrada correctamente.

                      Equipo: %s
                      Desde:  %s
                      Hasta:  %s
                      Estado: PENDIENTE de aprobacion

                    Recibiras un aviso cuando sea aprobada.

                    Sistema de Prestamo de Equipos
                    """.formatted(nombreUsuario, nombreEquipo, fechaInicio, fechaFin));

            mailSender.send(mensaje);
            log.info("Notificacion enviada a {} por la reserva de {}", destinatario, nombreEquipo);

        } catch (Exception e) {
            log.error("No se pudo enviar la notificacion a {}: {}", destinatario, e.getMessage());
        }
    }
}
