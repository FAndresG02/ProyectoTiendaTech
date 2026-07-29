import { Component, inject, OnInit } from '@angular/core';
import { ProductService } from '../../../../core/services/product-service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../../../core/services/user-service';
import { MatDialogRef } from '@angular/material/dialog';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { SnackbarService } from '../../../../core/services/snackbar-service';
import { UserLogin } from '../../../../interface/user/user-login';
import { GlobalConstants } from '../../../../shared/global-constants';
import { MATERIAL_IMPORTS } from '../../../../shared/material.imports';
import { COMMON_IMPORTS } from '../../../../shared/common.imports';
import { AuthService } from '../../../../core/services/auth-service';

@Component({
  selector: 'app-login',
  imports: [
    ...MATERIAL_IMPORTS,
    ...COMMON_IMPORTS
  ],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login implements OnInit {

  // Controla si la contraseña se muestra u oculta
  password: boolean = true;
  // Variable para el formulario de registro
  // aqui se almacenan los datos ingresados por el usuario antes de enviarlos al backend
  loginForm!: FormGroup;
  // Mensaje de respuesta del backend
  responseMessage: any;

  private authService = inject(AuthService);

  constructor(
    // Se usa para 
    private formBuilder: FormBuilder,
    // Inyección del router para redirigir al usuario después del login
    private router: Router,
    // Inyección del servicio de usuario para manejar la lógica de login
    private userService: UserService,
    // Inyección del servicio de diálogo para cerrar el diálogo después del login
    public dialogRef: MatDialogRef<Login>,
    // Inyección del servicio de ngx-ui-loader para mostrar un spinner de carga
    private ngxService: NgxUiLoaderService,
    // Inyección del servicio de snackbar para mostrar mensajes al usuario
    private snackbarService: SnackbarService
  ) { }

  ngOnInit(): void {
    // Inicialización del formulario con validaciones
    this.loginForm = this.formBuilder.group({
      // Email (MEJOR usar Validators.email)
      email: [null, [
        Validators.required,
        Validators.email
      ]],
      // Contraseña (MEJOR usar Validators.minLength)
      password: [null, [
        Validators.required
      ]]

    });
  }

  handleSubmit() {

    this.ngxService.start();

    const formData: UserLogin = {
      email: this.loginForm.value.email,
      password: this.loginForm.value.password
    };


    //Usa la data para registrase lo demas son validaciones 
    this.userService.login(formData).subscribe((response: any) => {

      // Detener el spinner de carga
      this.ngxService.stop();
      // Guardar el authService
      this.authService.login(response?.token);
      // Redirigir al usuario al inicio después del login exitoso
      this.router.navigate(['/']);
      // Obtener el mensaje de respuesta del backend
      this.responseMessage = response?.message;
      console.log(this.responseMessage);
      this.snackbarService.openSnackBar(this.responseMessage, "Sesion iniciada exitosamente");
      // Cerrar el diálogo después de mostrar el mensaje
      // el dialogo hace referencia a este componente
      this.dialogRef.close();


    }, (error) => {
      // Detener el spinner de carga
      this.ngxService.stop();
      // Obtener el mensaje de error del backend
      if (error.error?.message) {
        // Mostrar el mensaje de error al usuario
        this.responseMessage = error.error?.message;
      } else {
        // Mostrar un mensaje de error genérico al usuario
        this.responseMessage = GlobalConstants.genericErrorMessage;
      }
      // Mostrar el mensaje de error al usuario
      this.snackbarService.openSnackBar(this.responseMessage, GlobalConstants.error);
    }
    );



  }





}
