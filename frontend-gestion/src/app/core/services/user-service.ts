import { Injectable } from '@angular/core';
import { environment } from '../../environments/environmets';
import { HttpClient } from '@angular/common/http';

@Injectable({
    providedIn: 'root',
})
export class UserService {

    url = environment.API_URL;

    constructor(private http: HttpClient) { }

    //Metodo para registrar un nuevo usuario
    signup(data: any) {
        return this.http.post(`${this.url}/users/signup`, data, { headers: { 'Content-Type': 'application/json' } });
    }

    //Metodo para iniciar sesión
    login(data: any) {
        return this.http.post(`${this.url}/users/login`, data, { headers: { 'Content-Type': 'application/json' } });
    }

    //Metodo para obtener los usuarios registrados
    getUsers() {
        return this.http.get(`${this.url}/users/getUsers`, { headers: { 'Content-Type': 'application/json' } });
    }

    //Metodo para actualizar el status de un usuario para habilitarlo o deshabilitarlo
    updateStatus(data: any) {
        return this.http.patch(`${this.url}/users/update-status`, data, { headers: { 'Content-Type': 'application/json' } });
    }

    //Metodo para cambiar la contraseña
    changePassword(data: any) {
        return this.http.patch(`${this.url}/users/changePassword`, data, { headers: { 'Content-Type': 'application/json' } });
    }

    //Metodo para eliminar un usuario
    deleteUser(id: number) {
        return this.http.delete(`${this.url}/users/deleteUser/${id}`, { headers: { 'Content-Type': 'application/json' } });
    }

    //Metodo para cambiar el rol de un usuario a admin
    updateRole(data: any) {
        return this.http.patch(`${this.url}/users/update-role`, data, { headers: { 'Content-Type': 'application/json' } });
    }

    //Metodo para actualizar los datos de un usuario
    updateUser(data: any) {
        return this.http.patch(`${this.url}/users/update-user`, data, { headers: { 'Content-Type': 'application/json' } });
    }

    //Metodo para verificar si el token es válido
    checkToken() {
        return this.http.get(`${this.url}/users/checkToken`, { headers: { 'Content-Type': 'application/json' } });
    }

}
