import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

export type SnackbarType = 'error' | 'success' | 'warning' | 'info';

@Injectable({
  providedIn: 'root',
})
export class SnackbarService {

  private readonly panelClassMap: Record<SnackbarType, string> = {
    error:   'snackbar-error',
    success: 'snackbar-success',
    warning: 'snackbar-warning',
    info:    'snackbar-info',
  };

  constructor(private snackbar: MatSnackBar) {}

  open(message: string, type: SnackbarType = 'success') {
    this.snackbar.open(message, 'Cerrar', {
      horizontalPosition: 'center',
      verticalPosition: 'top',
      duration: 3000,
      panelClass: [this.panelClassMap[type]],
    });
  }
}