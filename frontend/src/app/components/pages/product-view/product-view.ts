import { ChangeDetectorRef, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { MATERIAL_IMPORTS } from '../../../shared/material.imports';
import { COMMON_IMPORTS } from '../../../shared/common.imports';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../../core/services/product-service';
import { GetProductById } from '../../../interface/product/get-productById';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { SnackbarService } from '../../../core/services/snackbar-service';
import { GlobalConstants } from '../../../shared/global-constants';
import { GetProduct } from '../../../interface/product/get-product';

@Component({
  selector: 'app-product-view',
  imports: [
    ...MATERIAL_IMPORTS,
    ...COMMON_IMPORTS
  ],
  templateUrl: './product-view.html',
  styleUrl: './product-view.scss',
})
export class ProductView implements OnInit {

  @ViewChild('productsGrid', { static: false }) productsGrid!: ElementRef<HTMLElement>;
  productId!: number;
  product?: GetProductById;
  allProducts?: GetProduct[];
  responseMessage: any;
  currentSlide = 0;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private ngxService: NgxUiLoaderService,
    private snackbarService: SnackbarService,
    private cdr: ChangeDetectorRef
  ) {
  }

  ngOnInit(): void {

    // Carga productos generales una sola vez al entrar a la página.
    this.loadProducts();
    // Escucha cambios del id para actualizar el producto mostrado sin destruir el componente.
    this.route.paramMap.subscribe(params => {
      this.productId = Number(params.get('id'));
      this.currentSlide = 0;
      this.getProductById(this.productId);
    });
  }

  getProductById(id: number) {

    this.ngxService.start();

    this.productService.getProductById(id).subscribe((response: GetProductById) => {
      this.ngxService.stop();
      this.product = response;
      this.cdr.detectChanges();
    }
      , (error: any) => {
        this.ngxService.stop();

        if (error.error?.message) {
          this.responseMessage = error.error?.message;
        } else {
          this.responseMessage = GlobalConstants.genericErrorMessage;
        }

        this.snackbarService.openSnackBar(this.responseMessage, GlobalConstants.error);
      });

  }

  loadProducts() {
    this.productService.getProducts().subscribe((response: GetProduct[]) => {
      this.ngxService.stop();
      this.allProducts = response;
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

  // Cambia al slide anterior del carrusel de imágenes.
  prevSlide(): void {
    // Verifica que el producto exista y que tenga imágenes disponibles.
    if (this.product?.images.length) {
      // Si está en la primera imagen vuelve a la última, caso contrario retrocede una posición.
      this.currentSlide = this.currentSlide === 0
        ? this.product.images.length - 1
        : this.currentSlide - 1;
    }
  }

  // Cambia al siguiente slide del carrusel de imágenes.
  nextSlide(): void {
    // Verifica que el producto exista y que tenga imágenes disponibles.
    if (this.product?.images.length) {
      // Si está en la última imagen vuelve a la primera, caso contrario avanza una posición.
      this.currentSlide = this.currentSlide === this.product.images.length - 1
        ? 0
        : this.currentSlide + 1;
    }
  }

  // Genera el arreglo de estrellas según la calificación promedio.
  getStars(rating: number): string[] {

    const stars: string[] = [];

    for (let i = 1; i <= 5; i++) {
      if (i <= rating) {
        // Estrella completa
        stars.push('star');
      } else if (i - 0.5 <= rating) {
        // Media estrella
        stars.push('star_half');
      } else {
        // Estrella vacía
        stars.push('star_border');
      }
    }

    return stars;
  }

  // Calcula el precio final con formato de dos decimales.
  calculateDiscountPrice(price: number, discountPercentage: number): string {

    const finalPrice = price - (price * discountPercentage / 100);

    return finalPrice.toFixed(2);
  }

  // Obtiene las iniciales del nombre del usuario para mostrar en el avatar.
  getInitials(name: string): string {

    if (!name) {
      return '';
    }

    const names = name.split(' ');

    if (names.length > 1) {
      return names[0].charAt(0) + names[1].charAt(0);
    }

    return names[0].substring(0, 2).toUpperCase();
  }

  scrollCarousel(direction: number) {
    const grid = this.productsGrid?.nativeElement;
    if (grid) {
      const scrollAmount = grid.clientWidth * 0.8;
      grid.scrollBy({ left: direction * scrollAmount, behavior: 'smooth' });
    }
  }
}
