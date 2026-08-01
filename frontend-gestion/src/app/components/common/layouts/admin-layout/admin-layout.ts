import { Component } from '@angular/core';
import { COMMON_IMPORTS } from '../../../../shared/common.imports';
import { MAC_ENTER } from '@angular/cdk/keycodes';
import { MATERIAL_IMPORTS } from '../../../../shared/material.imports';

@Component({
  selector: 'app-admin-layout',
  imports: [
    ...COMMON_IMPORTS,
    ...MATERIAL_IMPORTS
  ],
  templateUrl: './admin-layout.html',
  styleUrl: './admin-layout.scss',
})
export class AdminLayout {}
