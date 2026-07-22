import { ChangeDetectorRef, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
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
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-offer',
  imports: [
    ...MATERIAL_IMPORTS,
    ...COMMON_IMPORTS,
    CurrencyPipe,
    RouterLink,
  ],
  templateUrl: './offer.html',
  styleUrl: './offer.scss',
})
export class Offer implements OnInit {

  @ViewChild('productsGrid', { static: false }) productsGrid!: ElementRef<HTMLElement>;

  products: GetProduct[] = [];
  filteredProducts: GetProduct[] = [];
  featuredProducts: GetProduct[] = [];
  responseMessage: any;
  categories: GetCategory[] = [];

  selectedCategory = 'Todos';
  currentSort: string = 'default';

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

      //asigna los productos con descuento a la variable products
      this.products = response.filter(product =>
        product.discountPercentage > 0 && product.status === true
      );

      // Inicializa filteredProducts y le asiga los productos con descuento
      this.filteredProducts = [...this.products];

      // Productos destacados para el carrusel (no afectado por filtros)
      this.featuredProducts = this.products.filter(product =>
        product.featured &&
        product.status &&
        product.discountPercentage > 0
      );

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
      this.filteredProducts = [...this.products];
    } else {
      this.filteredProducts = this.products.filter(product =>
        selectedIds.includes(product.categoryId)
      );
    }

    if (this.currentSort !== 'default') {
      this.applySort();
    }
  }

  sortBy(event: any) {
    this.currentSort = event.target.value;
    this.applySort();
  }

  private applySort() {
    let list = [...this.filteredProducts];

    if (this.currentSort === 'price-asc') {
      list.sort((a, b) => a.price - b.price);
    } else if (this.currentSort === 'price-desc') {
      list.sort((a, b) => b.price - a.price);
    } else if (this.currentSort === 'discount') {
      list.sort((a, b) => b.discountPercentage - a.discountPercentage);
    }

    this.filteredProducts = list;
  }

}
