import { Component, inject, OnInit, signal } from '@angular/core';
import { ApiService, Equipo } from './api.service';
import { MisReservas } from './mis-reservas/mis-reservas';

@Component({
  selector: 'app-root',
  imports: [MisReservas],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit {
  /** El servicio es publico porque la plantilla lee sus senales. */
  protected readonly api = inject(ApiService);

  protected readonly equipoSeleccionado = signal<Equipo | null>(null);
  protected readonly fechaInicio = signal('');
  protected readonly fechaFin = signal('');
  protected readonly error = signal('');
  protected readonly exito = signal('');
  protected readonly enviando = signal(false);

  /** ngOnInit se ejecuta una vez, cuando el componente ya existe. */
  async ngOnInit(): Promise<void> {
    await this.api.cargarUsuarios();
    await this.api.cargarEquipos();
    await this.api.cargarReservas();
  }

  async cambiarUsuario(id: string): Promise<void> {
    this.api.usuarioActualId.set(Number(id));
    this.exito.set('');
    await this.api.cargarReservas();
  }

  abrirFormulario(equipo: Equipo): void {
    this.equipoSeleccionado.set(equipo);
    this.error.set('');
    this.exito.set('');
  }

  cerrarFormulario(): void {
    this.equipoSeleccionado.set(null);
    this.fechaInicio.set('');
    this.fechaFin.set('');
    this.error.set('');
  }

  async reservar(): Promise<void> {
    const equipo = this.equipoSeleccionado();
    if (!equipo) {
      return;
    }
    if (!this.fechaInicio() || !this.fechaFin()) {
      this.error.set('Indica la fecha de inicio y la de fin.');
      return;
    }

    this.enviando.set(true);
    this.error.set('');

    try {
      await this.api.crearReserva(equipo.id, this.fechaInicio(), this.fechaFin());
      this.exito.set(`Reserva creada para "${equipo.nombre}". Se envio el correo de confirmacion.`);
      this.cerrarFormulario();
    } catch (e: unknown) {
      // El backend responde { estado, mensaje } gracias a ManejadorDeErrores
      const respuesta = e as { error?: { mensaje?: string } };
      this.error.set(respuesta.error?.mensaje ?? 'No se pudo crear la reserva.');
    } finally {
      this.enviando.set(false);
    }
  }
}
