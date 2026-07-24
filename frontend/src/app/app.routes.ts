import { Component } from '@angular/core';
import { Routes } from '@angular/router';
import { Offer } from './components/pages/offer/offer';
import { MainPage } from './components/pages/main-page/main-page';
import { AllProducts } from './components/pages/all-products/all-products';
import { ProductView } from './components/pages/product-view/product-view';
import { PublicLayout } from './components/common/layouts/public-layout/public-layout';

export const routes: Routes = [

  {
    path: '',
    component: PublicLayout,
    children: [
      { path: '', component: MainPage }, // Ruta principal
      { path: 'offer', component: Offer },
      { path: 'allProducts', component: AllProducts },
      { path: 'productView/:id', component: ProductView },
    ]
  },

  // Ruta comodín para manejar rutas no encontradas (fallback)
  { path: '**', component: MainPage },
];
