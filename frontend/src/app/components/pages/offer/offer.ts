import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MATERIAL_IMPORTS } from '../../../shared/material.imports';
import { ProductService } from '../../../core/services/product-service';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { GlobalConstants } from '../../../shared/global-constants';
import { SnackbarService } from '../../../core/services/snackbar-service';
import { CurrencyPipe } from '@angular/common';
import { GetProduct } from '../../../interface/product/get-product';
import { COMMON_IMPORTS } from '../../../shared/shared';
import { CategoryService } from '../../../core/services/category-service';
import { GetCategory } from '../../../interface/category/get-category';
import { MatListOption } from '@angular/material/list';

@Component({
  selector: 'app-offer',
  imports: [
    ...MATERIAL_IMPORTS,
    ...COMMON_IMPORTS,
    CurrencyPipe,
  ],
  templateUrl: './offer.html',
  styleUrl: './offer.scss',
})
export class Offer implements OnInit {

  products: GetProduct[] = [];
  filteredProducts: GetProduct[] = [];
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

  loadProducts() {
    this.productService.getProducts().subscribe((response: GetProduct[]) => {
      this.ngxService.stop();

      //asigna los productos con descuento a la variable products
      this.products = response.filter(product =>
        product.discountPercentage > 0
      );

      // Inicializa filteredProducts y le asiga los productos con descuento
      this.filteredProducts = [...this.products];

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
    const selectedIds = selected.map(option => option.value as number);

    if (selectedIds.length === 0) {
      // Si no hay nada seleccionado, muestra todos
      this.filteredProducts = [...this.products];
    } else {
      this.filteredProducts = this.products.filter(product =>
        selectedIds.includes(product.categoryId)
      );
    }
  }


  sortBy(event: any) {
    // Obtiene el valor seleccionado del evento de cambio
    const val = event.target.value;

    // Copiamos la lista para no modificar la original directamente
    let list = [...this.filteredProducts];

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
    this.filteredProducts = list;
  }

}
