import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { ProductService } from '../../../core/services/product-service';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { MatDialog } from '@angular/material/dialog';
import { SnackbarService } from '../../../core/services/snackbar-service';
import { Router } from '@angular/router';
import { CurrencyPipe } from '@angular/common';

@Component({
  selector: 'app-main-page',
  imports: [MatButtonModule, MatCardModule, MatIconModule, MatTableModule, CurrencyPipe],
  templateUrl: './main-page.html',
  styleUrl: './main-page.scss',
})
export class MainPage implements OnInit {

  dataSource: any[] = [];
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
      this.dataSource = response;
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
