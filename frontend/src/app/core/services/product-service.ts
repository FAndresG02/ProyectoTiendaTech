import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { GetProduct } from '../../interface/product/get-product';
import { GetProductById } from '../../interface/product/get-productById';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  url = environment.API_URL + '/product';

  constructor(private http: HttpClient) { }


  getProducts() {
    return this.http.get<GetProduct[]>(`${this.url}/getProducts`);
  }


  // Obtiene la información completa de un producto mediante su identificador.
  getProductById(id: number): Observable<GetProductById> {
    return this.http.get<GetProductById>(`${this.url}/getProductById/${id}`);
  }

}