import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { firstValueFrom } from 'rxjs';


export interface Usuario {
  id: number;
  nombre: string;
  email: string;
}

export interface Equipo {
  id: number;
  nombre: string;
  tipo: string;
  numeroSerie: string;
  estado: 'DISPONIBLE' | 'EN_PRESTAMO' | 'MANTENIMIENTO';
}

export interface Reserva {
  id: number;
  usuarioId: number;
  equipoId: number;
  equipoNombre: string;
  equipoTipo: string;
  fechaInicio: string;
  fechaFin: string;
  estado: 'PENDIENTE' | 'APROBADA' | 'DEVUELTA';
  creadaEn: string;
}

const API = 'http://localhost:8080/api';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly http = inject(HttpClient);

  readonly usuarios = signal<Usuario[]>([]);
  readonly equipos = signal<Equipo[]>([]);
  readonly reservas = signal<Reserva[]>([]);
  readonly usuarioActualId = signal<number | null>(null);

  async cargarUsuarios(): Promise<void> {
    const lista = await firstValueFrom(this.http.get<Usuario[]>(`${API}/usuarios`));
    this.usuarios.set(lista);
    if (this.usuarioActualId() === null && lista.length > 0) {
      this.usuarioActualId.set(lista[0].id);
    }
  }

  async cargarEquipos(): Promise<void> {
    this.equipos.set(await firstValueFrom(this.http.get<Equipo[]>(`${API}/equipos`)));
  }

  async cargarReservas(): Promise<void> {
    const id = this.usuarioActualId();
    if (id === null) {
      return;
    }
    this.reservas.set(
      await firstValueFrom(this.http.get<Reserva[]>(`${API}/reservas?usuarioId=${id}`)),
    );
  }

  /** Lanza el error HTTP si el backend responde 400/404/409. */
  async crearReserva(equipoId: number, fechaInicio: string, fechaFin: string): Promise<void> {
    await firstValueFrom(
      this.http.post<Reserva>(`${API}/reservas`, {
        usuarioId: this.usuarioActualId(),
        equipoId,
        fechaInicio,
        fechaFin,
      }),
    );
    await this.cargarReservas();
  }

  async devolver(reservaId: number): Promise<void> {
    await firstValueFrom(this.http.patch(`${API}/reservas/${reservaId}/devolver`, {}));
    await this.cargarReservas();
  }
}
