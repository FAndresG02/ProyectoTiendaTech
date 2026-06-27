import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { MatDialog } from '@angular/material/dialog';
import { Router, RouterLink } from '@angular/router';
import { CurrencyPipe } from '@angular/common';
import { ProductService } from '../../../core/services/product-service';
import { SnackbarService } from '../../../core/services/snackbar-service';
import { MATERIAL_IMPORTS } from '../../../shared/material.imports';

@Component({
  selector: 'app-main-page',
  imports: [
    ...MATERIAL_IMPORTS,
    CurrencyPipe,
    RouterLink,
  ],
  templateUrl: './main-page.html',
  styleUrl: './main-page.scss',
})
export class MainPage implements OnInit {

  dataProducts: any[] = [];
  responseMessage: any;

  constructor(
    private cdr: ChangeDetectorRef,
    //Inyecta los servicios definos en CategoryService
    private productService: ProductService,
    //Permite mostrar el sping de carga
    private ngxService: NgxUiLoaderService,
    //Permite controlar el diaologo del mesnaje
    private dialog: MatDialog,
    //Permite mostrar mensajes al usuario
    private snackbarService: SnackbarService,
    //Permite al usuario redirigir a otras paginas 
    private router: Router
  ) { }

  ngOnInit(): void {
    //Inicia el sping de carga
    this.ngxService.start();
    //carga el metodo para extraer los productos de la base de datos
    this.obtenerProductos();
  }

  obtenerProductos() {

    this.productService.getProducts().subscribe((response: any) => {

      this.ngxService.stop();
      this.dataProducts = response;
      //Marca el componente para revisión en el próximo ciclo 
      this.cdr.markForCheck();
      console.log(response);

    }, (error: any) => {
      this.ngxService.stop();
      console.log(error?.message);
      if (error.error?.message) {
        this.responseMessage = error.error?.message;
      } else {
        this.responseMessage = 'Error al cargar los productos';
      }
    }

    )
  }






}
