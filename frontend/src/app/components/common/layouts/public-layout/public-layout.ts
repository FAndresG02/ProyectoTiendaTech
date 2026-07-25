import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { COMMON_IMPORTS } from '../../../../shared/common.imports';
import { MATERIAL_IMPORTS } from '../../../../shared/material.imports';
import { UserService } from '../../../../core/services/user-service';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Signup } from '../../dialog/signup/signup';

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
    private router: Router,
    //Inyección de MatDialog para abrir diálogos modales
    private dialog: MatDialog,
    //Inyección de UserService para verificar el token del usuario al cargar la página de inicio
    private userService: UserService,
    //Inyección de Router para redirigir al usuario a otras páginas si es necesario
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

}
