import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

// Este interceptor de token se encarga de agregar el token de autenticación a las solicitudes HTTP salientes 
// y manejar los errores de autenticación.
//El objetivo de este interceptor es centralizar el manejo de la autenticación en todas las peticiones HTTP de la 
//aplicación. Para ello, agrega automáticamente el token JWT almacenado en el navegador a la cabecera Authorization 
//de cada solicitud enviada al backend y, además, detecta respuestas de error relacionadas con la autenticación 
//(401 Unauthorized y 403 Forbidden). Cuando ocurre alguno de estos errores, elimina la información de la 
//sesión del usuario y lo redirige al inicio de sesión, evitando que cada componente tenga que implementar 
//esta lógica de forma individual y garantizando un manejo uniforme de la seguridad en toda la aplicación.
export const tokenInterceptor: HttpInterceptorFn = (req, next) => {

  //Así se inyectan servicios en interceptoras funcionales
  //Inyecta el servicio Router para redirigir a los usuarios no autenticados a la página de inicio de sesión
  const router = inject(Router);
  //Obtiene el token del almacenamiento local para incluirlo en las cabeceras de las solicitudes HTTP
  const token = localStorage.getItem('token');

  //Si el token existe, se clona la solicitud original y se agrega el token en la cabecera Authorization
  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
  }


  //Se envía la solicitud HTTP modificada (con el token en las cabeceras si existe) 
  //y se maneja cualquier error que pueda ocurrir durante la solicitud
  return next(req).pipe(
    //Maneja los errores HTTP, específicamente los errores 401 (no autorizado) y 403 (prohibido)
    catchError((error) => {
      //Verifica si el error es una instancia de HttpErrorResponse para asegurarse de que se trata de un error HTTP
      if (error instanceof HttpErrorResponse) {

        console.log('Error en la solicitud HTTP:', error.url);

        //Si el error es un 401 o 403, se limpia el almacenamiento local y se redirige al usuario a la página de inicio de sesión
        if (error.status === 401 || error.status === 403) {
          if (router.url !== '/') {
            localStorage.clear();
            router.navigate(['/']);
          }
        }
      }
      //Retorna el error para que pueda ser manejado por otros interceptores o componentes que consumen la API
      return throwError(() => error);
    })
  );
};
