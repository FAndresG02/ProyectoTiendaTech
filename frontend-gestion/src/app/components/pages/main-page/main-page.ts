import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatIcon } from "@angular/material/icon";
import { UserService } from '../../../core/services/user-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-main-page',
  imports: [MatIcon],
  templateUrl: './main-page.html',
  styleUrl: './main-page.scss',
})
export class MainPage {
  //MatDialog para abrir el diálogo de registro cuando el usuario haga clic en el botón de registro en la página de inicio  
  constructor(
    //Inyección de MatDialog para abrir diálogos modales
    private dialog: MatDialog,
    //Inyección de UserService para verificar el token del usuario al cargar la página de inicio
    private userService: UserService,
    //Inyección de Router para redirigir al usuario a otras páginas si es necesario
    private router: Router
  ) { }

  ngOnInit(): void {
    // Verificar el token del usuario al cargar la página de inicio y redirigir al dashboard si el token es válido
    this.userService.checkToken().subscribe((response: any) => {
      this.router.navigate(['/']);
    }, (error) => {
      // Si el token no es válido, no hacer nada y permitir que el usuario vea la página de inicio
      console.log('Token no válido o no presente' + error);
    });
  }




}
