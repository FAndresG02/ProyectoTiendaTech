import { ApplicationConfig, importProvidersFrom, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideAnimations } from '@angular/platform-browser/animations';

import { routes } from './app.routes';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { tokenInterceptor } from './core/guards/interceptors/token-interceptor';
import { NgxUiLoaderConfig, NgxUiLoaderModule, SPINNER } from 'ngx-ui-loader';

const loaderConfig: NgxUiLoaderConfig = {
  fgsType: SPINNER.ballSpinClockwise,
  fgsSize: 60,
  fgsColor: '#ff6347',
  pbColor: '#ff6347',
  overlayColor: 'rgba(0,0,0,0.5)',
};

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideAnimations(),
    //Configura el proveedor de HttpClient para usar el interceptor de token en todas las solicitudes HTTP
    provideHttpClient(
      //withInterceptors es una función que permite agregar interceptores a las solicitudes HTTP, 
      //en este caso se agrega el tokenInterceptorInterceptor para incluir el token de autenticación 
      //en las cabeceras de las solicitudes y manejar errores de autenticación
      withInterceptors([tokenInterceptor])
    ),
    provideRouter(routes),
    importProvidersFrom(NgxUiLoaderModule.forRoot(loaderConfig)),
    // MenuItems
  ]
};
