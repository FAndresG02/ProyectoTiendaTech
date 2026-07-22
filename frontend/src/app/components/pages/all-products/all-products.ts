import { ChangeDetectorRef, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
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
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';

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
export class AllProducts implements OnInit, OnDestroy {

  @ViewChild('productsGrid', { static: false }) productsGrid!: ElementRef<HTMLElement>;

  products: GetProduct[] = [];
  allProducts: GetProduct[] = [];
  featuredProducts: GetProduct[] = [];
  responseMessage: any;
  categories: GetCategory[] = [];

  searchTerm: string = '';
  selectedCategoryIds: number[] = [];
  currentSort: string = 'default';
  private querySub!: Subscription;

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
    this.loadProducts();
    this.loadCategories();

    this.querySub = this.route.queryParamMap.subscribe(params => {
      const newSearch = params.get('search') || '';
      if (this.searchTerm !== newSearch || this.products.length === 0) {
        this.searchTerm = newSearch;
      }
      if (this.products.length > 0) {
        this.applyFilters();
        this.cdr.markForCheck();
      }
    });
  }

  ngOnDestroy(): void {
    this.querySub?.unsubscribe();
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

      // Productos destacados para el carrusel (no afectado por filtros)
      this.featuredProducts = this.products.filter(product =>
        product.featured &&
        product.status
      );

      this.applyFilters();
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
    this.selectedCategoryIds = selected.map(option => option.value as number);
    this.applyFilters();
  }

  sortBy(event: any) {
    this.currentSort = event.target.value;
    this.applyFilters();
  }

  private applyFilters() {
    let result = [...this.products];

    if (this.searchTerm) {
      const term = this.searchTerm.toLowerCase();
      result = result.filter(p => p.name.toLowerCase().includes(term));
    }

    if (this.selectedCategoryIds.length > 0) {
      result = result.filter(p => this.selectedCategoryIds.includes(p.categoryId));
    }

    if (this.currentSort === 'price-asc') {
      result.sort((a, b) => a.price - b.price);
    } else if (this.currentSort === 'price-desc') {
      result.sort((a, b) => b.price - a.price);
    } else if (this.currentSort === 'discount') {
      result.sort((a, b) => b.discountPercentage - a.discountPercentage);
    }

    this.allProducts = result;
  }
}
