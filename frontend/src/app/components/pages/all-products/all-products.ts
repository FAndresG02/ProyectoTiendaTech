import { ChangeDetectorRef, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { MATERIAL_IMPORTS } from '../../../shared/material.imports';
import { COMMON_IMPORTS } from '../../../shared/common.imports';
import { GetProduct } from '../../../interface/product/get-product';
import { GetCategory } from '../../../interface/category/get-category';
import { ProductService } from '../../../core/services/product-service';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { SnackbarService } from '../../../core/services/snackbar-service';
import { CategoryService } from '../../../core/services/category-service';
import { GlobalConstants } from '../../../shared/global-constants';
import { MatListOption } from '@angular/material/list';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-all-products',
  imports: [
    ...MATERIAL_IMPORTS,
    ...COMMON_IMPORTS,
    RouterLink,
  ],
  templateUrl: './all-products.html',
  styleUrl: './all-products.scss',
})
export class AllProducts implements OnInit {

  @ViewChild('productsGrid', { static: false }) productsGrid!: ElementRef<HTMLElement>;

  products: GetProduct[] = [];
  allProducts: GetProduct[] = [];
  responseMessage: any;
  categories: GetCategory[] = [];

  selectedCategory = 'Todos';

  constructor(
    private cdr: ChangeDetectorRef,
    private productService: ProductService,
    private ngxService: NgxUiLoaderService,
    private snackbarService: SnackbarService,
    private categoryService: CategoryService
  ) { }


  ngOnInit(): void {
    this.ngxService.start();
    this.loadProducts();
    this.loadCategories();
  }

  scrollCarousel(direction: number) {
    const grid = this.productsGrid?.nativeElement;
    if (grid) {
      const scrollAmount = grid.clientWidth * 0.8;
      grid.scrollBy({ left: direction * scrollAmount, behavior: 'smooth' });
    }
  }

  loadProducts() {
    this.productService.getProducts().subscribe((response: GetProduct[]) => {
      this.ngxService.stop();

      // Filtra los productos activos (status === true) y los asigna a la variable products
      this.products = response.filter(product => product.status === true);
      //asigna todos los productos a la variable allProducts
      this.allProducts = [...this.products];

      this.cdr.markForCheck();
    }, (error: any) => {
      this.ngxService.stop();

      if (error.error?.message) {
        this.responseMessage = error.error?.message;
      } else {
        this.responseMessage = GlobalConstants.genericErrorMessage;
      }

      this.snackbarService.openSnackBar(this.responseMessage, GlobalConstants.error);
    });
  }

  loadCategories() {

    this.categoryService.getCategories().subscribe((response: GetCategory[]) => {

      this.ngxService.stop();
      this.categories = response;
      this.cdr.markForCheck();

    }, (error: any) => {
      this.ngxService.stop();

      if (error.error?.message) {
        this.responseMessage = error.error?.message;
      } else {
        this.responseMessage = GlobalConstants.genericErrorMessage;
      }

      this.snackbarService.openSnackBar(this.responseMessage, GlobalConstants.error);
    });
  }

  filterByCategory(selected: MatListOption[]) {
    // Extrae los IDs de las categorías seleccionadas
    const selectedIds = selected.map(option => option.value as number);

    // Verifica si no hay ninguna categoría seleccionada
    if (selectedIds.length === 0) {
      // Restaura todos los productos (copia del mismo array)
      this.allProducts = [...this.products];
    } else {
      // Filtra y muestra solo los productos cuya categoría esté seleccionada
      this.allProducts = this.products.filter(product =>
        selectedIds.includes(product.categoryId)
      );
    }
  }

  sortBy(event: any) {
    // Obtiene el valor seleccionado del evento de cambio
    const val = event.target.value;

    // Copiamos la lista para no modificar la original directamente
    let list = [...this.allProducts];

    if (val === 'price-asc') {
      list.sort((a, b) => {
        // orden ascendente por precio
        return a.price - b.price;
      });
    }

    if (val === 'price-desc') {
      list.sort((a, b) => {
        // orden descendente por precio
        return b.price - a.price;
      });
    }

    if (val === 'discount') {
      list.sort((a, b) => {
        // mayor descuento primero
        return b.discountPercentage - a.discountPercentage;
      });
    }
    this.allProducts = list;
  }
}
