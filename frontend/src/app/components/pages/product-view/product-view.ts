import { Component, OnInit } from '@angular/core';
import { MATERIAL_IMPORTS } from '../../../shared/material.imports';
import { COMMON_IMPORTS } from '../../../shared/common.imports';

@Component({
  selector: 'app-product-view',
  imports: [
    ...MATERIAL_IMPORTS,
    ...COMMON_IMPORTS
  ],
  templateUrl: './product-view.html',
  styleUrl: './product-view.scss',
})
export class ProductView {


  constructor() { }



}
