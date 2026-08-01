import { Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogContent, MatDialogActions } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MessageDialogData } from '../../../../interface/MessageDialogData';

@Component({
  selector: 'app-message',
  imports: [MatDialogContent, MatDialogActions, MatIconModule],
  templateUrl: './message.html',
  styleUrl: './message.scss',
})
export class Message {
  // Referencia al diálogo, para poder cerrarlo desde el template o el .ts
  dialogRef = inject(MatDialogRef<Message>);
  // Data inyectada al abrir el diálogo (título, mensaje, etc.)
  data = inject<MessageDialogData>(MAT_DIALOG_DATA);
}
