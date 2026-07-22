import { Component, ElementRef, ViewChild } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
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
export class PublicLayout {

  @ViewChild('searchInput') searchInput!: ElementRef<HTMLInputElement>;

  constructor(private router: Router) {}

  onSearch() {
    const term = this.searchInput.nativeElement.value.trim();
    this.router.navigate(['/allProducts'], {
      queryParams: term ? { search: term } : undefined,
    });
  }

}
