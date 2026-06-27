import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { GetProduct } from '../../interface/product/get-product';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  url = environment.API_URL + '/product';

  constructor(private http: HttpClient) { }

  addProduct(data: any) {
    return this.http.post(`${this.url}/addProduct`, data,
      { headers: { 'Content-Type': 'application/json' } });
  }

  getProducts() {
    return this.http.get<GetProduct[]>(`${this.url}/getProducts`);
  }

  getProductByCategory(id: number) {
    return this.http.get(`${this.url}/getProductByCategory/${id}`);

  }

  getProductById(id: number) {
    return this.http.get(`${this.url}/getProductById/${id}`);

  }

  updateProduct(id: number, data: any) {
    return this.http.put(
      `${this.url}/updateProduct/${id}`,
      data
    );
  }

  updatePictureProduct(id: number, file: File) {
    const formData = new FormData();
    formData.append('picture', file);

    return this.http.patch(
      `${this.url}/updatePictureProduct/${id}`,
      formData
    );
  }

  updateStatusProduct(data: any) {
    return this.http.patch(
      `${this.url}/updateStatusProduct`,
      data,
      { headers: { 'Content-Type': 'application/json' } }
    );
  }

  updateDiscount(data: any) {
    return this.http.patch(
      `${this.url}/updateDiscount`,
      data,
      { headers: { 'Content-Type': 'application/json' } }
    );
  }

  updateFeatured(data: any) {
    return this.http.patch(
      `${this.url}/updateFeatured`,
      data,
      { headers: { 'Content-Type': 'application/json' } }
    );
  }

  deleteProduct(id: number) {
    return this.http.delete(
      `${this.url}/deleteProduct/${id}`
    );
  }

  deletePictureProduct(id: number) {
    return this.http.delete(
      `${this.url}/deletePictureProduct/${id}`
    );
  }




}