import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { GetCategory } from '../../interface/category/get-category';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
    url = environment.API_URL + '/category';

    constructor(private http: HttpClient) { }

    getCategories() {
        return this.http.get<GetCategory[]>(`${this.url}/getCategories`);
    }
}
