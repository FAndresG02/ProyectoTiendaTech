import { Component } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { COMMON_IMPORTS } from '../../../../shared/common.imports';
import { MATERIAL_IMPORTS } from '../../../../shared/material.imports';

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
export class PublicLayout {

  constructor(private router: Router) {}

  search(term: string): void {
    if (term.trim()) {
      this.router.navigate(['/allProducts'], { queryParams: { search: term.trim() } });
    }
  }

}
