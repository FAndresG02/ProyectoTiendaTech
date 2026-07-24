import { Component, OnInit } from '@angular/core';
import { COMMON_IMPORTS } from '../../../../shared/common.imports';
import { MATERIAL_IMPORTS } from '../../../../shared/material.imports';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../../../core/services/user-service';
import { MatDialogRef } from '@angular/material/dialog';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { SnackbarService } from '../../../../core/services/snackbar-service';

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
  }

  // Función para validar si las contraseñas coinciden
  validateSubmit(){
    // Obtiene los valores de los campos de contraseña y 
    // confirmación de contraseña del formulario
    let password = this.signupForm.get('password')?.value;
    let confirmPassword = this.signupForm.get('confirmPassword')?.value;
    
    // Compara las contraseñas y devuelve true si no coinciden, false si coinciden
    if(password !== confirmPassword){
      return true;
    } else {
      return false;
    }
  }

  // Función para manejar el envío del formulario de registro
  handleSubmit() {
    // Inicia el loader de la interfaz de usuario
    this.ngxService.start();

    if(this.signupForm.invalid || this.validateSubmit()){
      // Si el formulario es inválido o las contraseñas no coinciden,
      // detiene el loader y muestra un mensaje de error
      this.ngxService.stop();
      this.snackbarService.openSnackBar('Por favor, complete todos los campos correctamente.', 'Cerrar');
      return;
    }

}
