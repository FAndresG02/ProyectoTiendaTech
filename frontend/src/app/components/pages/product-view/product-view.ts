import { Component, OnInit } from '@angular/core';
import { MATERIAL_IMPORTS } from '../../../shared/material.imports';
import { COMMON_IMPORTS } from '../../../shared/common.imports';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-product-view',
  imports: [
    ...MATERIAL_IMPORTS,
    ...COMMON_IMPORTS
  ],
  templateUrl: './product-view.html',
  styleUrl: './product-view.scss',
})
export class ProductView implements OnInit {

  productId!: number;

  constructor(
    private route: ActivatedRoute
  ) { 
  }

  ngOnInit(): void {
    this.productId = Number(this.route.snapshot.paramMap.get('id'));
  }

}
