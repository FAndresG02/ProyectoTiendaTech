import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { COMMON_IMPORTS, MATERIAL_IMPORTS } from '../../../shared/shared';

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
export class PublicLayout {}
