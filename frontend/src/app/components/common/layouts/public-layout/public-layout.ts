import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { COMMON_IMPORTS } from '../../../../shared/common.imports';
import { MATERIAL_IMPORTS } from '../../../../shared/material.imports';
import { UserService } from '../../../../core/services/user-service';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Signup } from '../../dialog/signup/signup';
import { Login } from '../../dialog/login/login';
import { AuthService } from '../../../../core/services/auth-service';

@Component({
  selector: 'app-public-layout',
  imports: [
    ...COMMON_IMPORTS,
    ...MATERIAL_IMPORTS,
    RouterOutlet,
    RouterLink
  ],
  templateUrl: './public-layout.html',
  styleUrl: './public-layout.scss',
})
export class PublicLayout implements OnInit {


  constructor(
    //Inyección de AuthService para manejar la autenticación del usuario
    public authService: AuthService,
    //Inyección de Router para manejar la navegación entre páginas
    private router: Router,
    //Inyección de MatDialog para abrir diálogos modales
    private dialog: MatDialog,
    //Inyección de UserService para verificar el token del usuario al cargar la página de inicio
    private userService: UserService,
  ) { }

  ngOnInit(): void {
  }

  search(term: string): void {
    if (term.trim()) {
      this.router.navigate(['/allProducts'], { queryParams: { search: term.trim() } });
    }
  }

  // Lógica para manejar la acción de registro
  handleSignupAction() {
    // Aquí puedes abrir un diálogo de registro o redirigir a una página de registro
    const dialogConfig = new MatDialogConfig();
    // Configura el diálogo según tus necesidades
    dialogConfig.width = '400px';
    // dialogConfig.data = { /* datos que quieras pasar al diálogo */ };
    this.dialog.open(Signup, dialogConfig);
  }

  // Lógica para manejar la acción de inicio de sesion
  handleLoginAction() {
    // Aquí puedes abrir un diálogo de olvido de inicio de sesion
    const dialogConfig = new MatDialogConfig();
    // Configura el diálogo según tus necesidades
    dialogConfig.width = '400px';
    // dialogConfig.data = { /* datos que quieras pasar al diálogo */ };
    this.dialog.open(Login, dialogConfig);
  }

  handleLogout() {
    this.authService.logout();
    this.router.navigate(['/']);
  }

}
