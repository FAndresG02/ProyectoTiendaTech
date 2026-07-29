import { Injectable, signal } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    // signal que refleja si hay sesión iniciada, inicializado leyendo el token actual
    isLoggedIn = signal<boolean>(!!localStorage.getItem('token'));

    login(token: string): void {
        localStorage.setItem('token', token);
        this.isLoggedIn.set(true);
    }

    logout(): void {
        localStorage.clear();
        this.isLoggedIn.set(false);
    }
}
