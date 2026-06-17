import { Component } from '@angular/core';
import { Routes } from '@angular/router';
import { Home } from './components/pages/home/home';
import { MainLayout } from './components/common/layouts/main-layout/main-layout';

export const routes: Routes = [
    {path: '', component: Home},

    {
        path: 'tech',
        component: MainLayout,

        children: [

        ]
    },

    // Ruta comodín para manejar rutas no encontradas (fallback)
    { path: '**', component: Home },
];
