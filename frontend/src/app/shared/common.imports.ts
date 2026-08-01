import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { RouterLink, RouterModule } from "@angular/router";

export const COMMON_IMPORTS = [
  CommonModule,
  ReactiveFormsModule,
  FormsModule,
  RouterModule,
  RouterLink,
] as const;