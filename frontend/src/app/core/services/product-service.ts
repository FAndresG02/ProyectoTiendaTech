import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  url = environment.API_URL + '/product';

  constructor(private http: HttpClient) {}

  getProducts() {
    return this.http.get(`${this.url}/getProducts`);
  }
}