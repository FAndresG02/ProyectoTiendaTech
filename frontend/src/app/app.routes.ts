import { Component } from '@angular/core';
import { Routes } from '@angular/router';
import { Offer } from './components/pages/offer/offer';
import { PublicLayout } from './components/layouts/public-layout/public-layout';
import { MainPage } from './components/pages/main-page/main-page';

export const routes: Routes = [

    {
    path: '',
    component: PublicLayout,
    children: [
      { path: '', component: MainPage}, // Ruta principal
      { path: 'ofertas', component: Offer },
    ]
  },

    // Ruta comodín para manejar rutas no encontradas (fallback)
    { path: '**', component: MainPage },
];
