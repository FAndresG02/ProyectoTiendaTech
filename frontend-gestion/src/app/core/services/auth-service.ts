import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})

//El objetivo de este servicio es verificar si el usuario se encuentra autenticado comprobando la existencia de 
//un token JWT almacenado en el navegador. Si el token está presente, considera que el usuario ha iniciado 
//sesión y permite continuar con la navegación; en caso contrario, redirige al usuario a la página de inicio 
//de sesión y devuelve un valor que indica que no está autenticado. Este servicio es utilizado como una 
//validación básica de autenticación antes de permitir el acceso a funcionalidades protegidas de la aplicación.
export class AuthService {

  //Inyección de Router para redirigir a los usuarios no autenticados
  constructor(private router: Router){}

  //Metodo para verificar si el usuario está autenticado comprobando la existencia de un token en el almacenamiento local
  public isAuthenticated(): boolean{

    //Obtiene el token del almacenamiento local
    const token = localStorage.getItem('token');

    //Si el token no existe
    if(!token){
      //Redirige al usuario a la página de inicio de sesión y retorna false
      this.router.navigate(['/'])
      return false;
    }else{
      //Si el token existe, retorna true, indicando que el usuario está autenticado
      return true;
    }
  }
}
