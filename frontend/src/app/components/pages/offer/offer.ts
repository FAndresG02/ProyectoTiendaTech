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
import { RouterLink } from '@angular/router';
import { MatSelectionListChange } from '@angular/material/list';

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

  // Variable de referencia al elemento grid de productos para el carrusel
  @ViewChild('productsGrid', { static: false }) productsGrid!: ElementRef<HTMLElement>;
  // Variable de referencia al elemento select para el ordenamiento
  @ViewChild('sortSelect', { static: false }) sortSelect!: ElementRef<HTMLSelectElement>;

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


  loadProducts() {
    this.productService.getProducts().subscribe((response: GetProduct[]) => {
      this.ngxService.stop();

      //asigna los productos con descuento a la variable products
      this.products = response.filter(product =>
        product.discountPercentage > 0 && product.status === true
      );

      // Inicializa filteredProducts y le asiga los productos con descuento
      //esto con el objetivo de no modificar la lista original de productos y poder aplicar filtros 
      // y ordenamientos sobre filteredProducts
      this.filteredProducts = [...this.products];

      // Productos destacados para el carrusel (no afectado por filtros)
      //para mostrar solo los productos destacados con descuento y activos dentro del carrusel
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

  filterByCategory(event: MatSelectionListChange) {
    // Reinicia el ordenamiento a "default" y actualiza el valor del select de ordenamiento
    this.currentSort = 'default';

    // Reinicia el valor del select de ordenamiento a "default" si existe
    if (this.sortSelect) this.sortSelect.nativeElement.value = 'default';

    // Obtiene la opción seleccionada del evento de cambio de selección
    const option = event.options[0];

    // Si no hay opción seleccionada o la opción seleccionada es "Todos" (valor 0), se muestran todos los productos
    if (!option?.selected || option.value === 0) {
      this.filteredProducts = [...this.products];
      this.cdr.markForCheck();
      return;
    }

    this.ngxService.start();

    // Filtra los productos por la categoría seleccionada
    this.productService.getProductByCategory(option.value).subscribe({
      next: (response: GetProduct[]) => {
        this.ngxService.stop();
        // Filtra los productos para mostrar solo aquellos con descuento y activos
        this.filteredProducts = response.filter(product =>
          product.discountPercentage > 0 && product.status === true
        );
        // Aplica el ordenamiento actual después de filtrar
        this.applySort();
        this.cdr.markForCheck();
      },
      error: (error: any) => {
        this.ngxService.stop();

        if (error.error?.message) {
          this.responseMessage = error.error?.message;
        } else {
          this.responseMessage = GlobalConstants.genericErrorMessage;
        }

        this.snackbarService.openSnackBar(this.responseMessage, GlobalConstants.error);
      }
    });
  }


  // Método para manejar el cambio de ordenamiento
  sortBy(event: any) {
    // Actualiza el criterio de ordenamiento basado en la selección del usuario
    this.currentSort = event.target.value;
    // Aplica el ordenamiento a la lista de productos filtrados
    this.applySort();
  }

  // Método privado para aplicar el ordenamiento a la lista de productos filtrados
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

  // Método para desplazar el carrusel de productos hacia la izquierda o derecha
  scrollCarousel(direction: number) {
    const grid = this.productsGrid?.nativeElement;
    if (grid) {
      const scrollAmount = grid.clientWidth * 0.8;
      grid.scrollBy({ left: direction * scrollAmount, behavior: 'smooth' });
    }
  }

}
