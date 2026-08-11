import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideHttpClient } from '@angular/common/http';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    // Habilita HttpClient en toda la aplicacion. Sin esto,
    // inject(HttpClient) falla al arrancar.
    provideHttpClient(),
  ],
};
