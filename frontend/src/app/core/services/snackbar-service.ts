import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

export type SnackbarType = 'error' | 'success' | 'warning' | 'info';

@Injectable({
  providedIn: 'root',
})
export class SnackbarService {

  constructor(private snackbar: MatSnackBar) { }

  openSnackBar(message: string, action: string) {
    if (action === 'error') {
      this.snackbar.open(message, 'Cerrar', {
        horizontalPosition: 'center',
        verticalPosition: 'top',
        duration: 3000,
        panelClass: ['black-snackbar']
      });
    } else {
      this.snackbar.open(message, 'Cerrar', {
        horizontalPosition: 'center',
        verticalPosition: 'top',
        duration: 3000,
        panelClass: ['green-snackbar']
      });
    }
  }
}