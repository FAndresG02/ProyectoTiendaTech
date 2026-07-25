import { Component, OnInit } from '@angular/core';
import { COMMON_IMPORTS } from '../../../../shared/common.imports';
import { MATERIAL_IMPORTS } from '../../../../shared/material.imports';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../../../core/services/user-service';
import { MatDialogRef } from '@angular/material/dialog';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { SnackbarService } from '../../../../core/services/snackbar-service';
import { UserSignup, UserSignupResponse } from '../../../../interface/user/user-signup';
import { GlobalConstants } from '../../../../shared/global-constants';

@Component({
  selector: 'app-signup',
  imports: [
    ...MATERIAL_IMPORTS,
    ...COMMON_IMPORTS,
  ],
  templateUrl: './signup.html',
  styleUrl: './signup.scss',
})
export class Signup implements OnInit {

  password: boolean = true;
  confirmPassword: boolean = true;

  // Variable para el formulario de registro
  // aqui se almacenan los datos ingresados por el usuario antes de enviarlos al backend
  signupForm!: FormGroup;

  responseMessage: any;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private userService: UserService,
    private snackbarService: SnackbarService,
    private ngxService: NgxUiLoaderService,
    public dialogRef: MatDialogRef<Signup>
  ) { }

  ngOnInit(): void {

    // Inicialización del formulario con validaciones
    this.signupForm = this.formBuilder.group({

      // Nombre (solo letras y números)
      name: [null, [
        Validators.required,
        Validators.pattern(GlobalConstants.nameRegex)
      ]],

      // Email (MEJOR usar Validators.email)
      email: [null, [
        Validators.required,
        Validators.email
      ]],

      // Número de contacto (10 dígitos)
      contactNumber: [null, [
        Validators.required,
        Validators.pattern(GlobalConstants.contactNumberRegex)
      ]],

      // Contraseña
      password: [null, [Validators.required]],

      // Confirmación de contraseña
      confirmPassword: [null, [Validators.required]]
    });
  }

  // Función para validar si las contraseñas coinciden
  validateSubmit() {
    // Obtiene los valores de los campos de contraseña y 
    // confirmación de contraseña del formulario
    let password = this.signupForm.get('password')?.value;
    let confirmPassword = this.signupForm.get('confirmPassword')?.value;

    // Compara las contraseñas y devuelve true si no coinciden, false si coinciden
    if (password !== confirmPassword) {
      return true;
    } else {
      return false;
    }
  }

  // Función para manejar el envío del formulario de registro
  handleSubmit() {
    // Inicia el loader de la interfaz de usuario
    this.ngxService.start();

    if (this.signupForm.invalid || this.validateSubmit()) {
      // Si el formulario es inválido o las contraseñas no coinciden,
      // detiene el loader y muestra un mensaje de error
      this.ngxService.stop();
      this.snackbarService.openSnackBar('Por favor, complete todos los campos correctamente.', 'Cerrar');
      return;
    }

    const formValue = this.signupForm.value;

    const formData: UserSignup = {
      name: formValue.name,
      email: formValue.email,
      contactNumber: formValue.contactNumber,
      password: formValue.password
    };

    this.userService.signup(formData).subscribe((response: UserSignupResponse) => {

      // Detener el spinner de carga
      this.ngxService.stop();
      // Cerrar el diálogo después de mostrar el mensaje
      this.dialogRef.close();
      // Obtener el mensaje de respuesta del backend
      this.responseMessage = response?.message;
      // Mostrar el mensaje de éxito al usuario
      console.log(this.responseMessage);
      this.snackbarService.openSnackBar(this.responseMessage, "success");
      // Redirigir al usuario a la página de inicio de sesión después del registro exitoso
      this.router.navigate(['/']);
    },      // Error
      (error) => {
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
        this.snackbarService.openSnackBar(this.responseMessage, "error");
      });

  }
}
