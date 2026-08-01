import { Injectable, signal } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    // signal que refleja si hay sesión iniciada, inicializado leyendo el token actual
    // Se utiliza !! para convertir el valor a booleano, true si hay token, false si no
    isLoggedIn = signal<boolean>(!!localStorage.getItem('token'));

    // Método para iniciar sesión almacena el token en el localStorage y actualiza el signal
    login(token: string): void {
        localStorage.setItem('token', token);
        this.isLoggedIn.set(true);
    }

    // Método para cerrar sesión limpia el token del localStorage y actualiza el signal
    logout(): void {
        localStorage.clear();
        this.isLoggedIn.set(false);
    }
}
