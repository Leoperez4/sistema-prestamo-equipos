import { Component, inject } from '@angular/core';
import { ApiService } from '../api.service';

/**
 * Historico de reservas del usuario seleccionado.
 *
 * No recibe datos por parametro: lee directamente la senal del servicio,
 * asi que se repinta solo cuando otro componente crea o devuelve una reserva.
 */
@Component({
  selector: 'app-mis-reservas',
  templateUrl: './mis-reservas.html',
})
export class MisReservas {
  protected readonly api = inject(ApiService);

  async devolver(reservaId: number): Promise<void> {
    await this.api.devolver(reservaId);
  }
}
