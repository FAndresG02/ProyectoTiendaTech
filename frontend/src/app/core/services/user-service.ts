import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { UserSignup } from '../../interface/user/user-signup';
import { Observable } from 'rxjs';
import { UserLogin } from '../../interface/user/user-login';

@Injectable({
    providedIn: 'root'
})
export class UserService {

    url = environment.API_URL + '/users';

    constructor(
        private http: HttpClient
    ) { }

    //Metodo para registrar un nuevo usuario
    //headers: es necesario para que el backend pueda interpretar correctamente el cuerpo de la solicitud como JSON
    signup(data: UserSignup): Observable<UserSignup> {
        return this.http.post<UserSignup>(`${this.url}/signup`, data, {
            headers: { 'Content-Type': 'application/json' }
        });
    }

    login(data: UserLogin): Observable<UserLogin> {
        return this.http.post<UserLogin>(`${this.url}/login`, data, {
            headers: { 'Content-Type': 'application/json' }
        });
    }




}
