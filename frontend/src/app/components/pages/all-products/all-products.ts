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
import { MatSelectionListChange } from '@angular/material/list';
import { ActivatedRoute, RouterLink } from '@angular/router';

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

  // Variable de referencia al elemento grid de productos para el carrusel
  @ViewChild('productsGrid', { static: false }) productsGrid!: ElementRef<HTMLElement>;
  // Variable de referencia al elemento select para el ordenamiento
  @ViewChild('sortSelect', { static: false }) sortSelect!: ElementRef<HTMLSelectElement>;

  products: GetProduct[] = [];
  allProducts: GetProduct[] = [];
  featuredProducts: GetProduct[] = [];
  responseMessage: any;
  categories: GetCategory[] = [];

  currentSort: string = 'default';
  searchTerm: string = '';

  constructor(
    private cdr: ChangeDetectorRef,
    private productService: ProductService,
    private ngxService: NgxUiLoaderService,
    private snackbarService: SnackbarService,
    private categoryService: CategoryService,
    private route: ActivatedRoute,
  ) { }


  ngOnInit(): void {
    this.ngxService.start();
    this.loadCategories();
    this.route.queryParams.subscribe(params => {
      this.searchTerm = params['search'] || '';
      this.loadProducts();
    });
  }

  loadProducts() {
    this.productService.getProducts().subscribe((response: GetProduct[]) => {
      // Filtra los productos activos (status === true) y los asigna a la variable products
      this.products = response.filter(product => product.status === true);

      // Productos destacados para el carrusel (no afectado por filtros)
      this.featuredProducts = this.products.filter(product =>
        product.featured &&
        product.status
      );

      // Si hay un término de búsqueda, filtra los productos por nombre, de lo contrario, 
      // asigna todos los productos activos a allProducts
      if (this.searchTerm) {
        this.searchProducts(this.searchTerm);
      } else {
        this.ngxService.stop();
        this.allProducts = [...this.products];
        this.cdr.markForCheck();
      }
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

  // Método para buscar productos por nombre
  searchProducts(name: string) {
    this.productService.getProductByName(name).subscribe({
      next: (response: GetProduct[]) => {
        this.ngxService.stop();
        this.allProducts = response.filter(product => product.status === true);
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
      this.allProducts = [...this.products];
      this.cdr.markForCheck();
      return;
    }

    this.ngxService.start();

    // Filtra los productos por la categoría seleccionada
    this.productService.getProductByCategory(option.value).subscribe({
      next: (response: GetProduct[]) => {
        this.ngxService.stop();
        // Filtra los productos para mostrar solo aquellos activos
        this.allProducts = response.filter(product =>
          product.status === true
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
    let list = [...this.allProducts];

    if (this.currentSort === 'price-asc') {
      list.sort((a, b) => a.price - b.price);
    } else if (this.currentSort === 'price-desc') {
      list.sort((a, b) => b.price - a.price);
    } else if (this.currentSort === 'discount') {
      list.sort((a, b) => b.discountPercentage - a.discountPercentage);
    }

    this.allProducts = list;
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
