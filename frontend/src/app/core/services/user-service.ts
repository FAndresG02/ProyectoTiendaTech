import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { UserSignup, UserSignupResponse } from '../../interface/user/user-signup';
import { Observable } from 'rxjs';

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
    signup(data: UserSignup): Observable<UserSignupResponse> {
        return this.http.post<UserSignupResponse>(`${this.url}/signup`, data, {
            headers: { 'Content-Type': 'application/json' }
        });
    }


}
