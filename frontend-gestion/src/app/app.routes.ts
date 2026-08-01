import { Routes } from '@angular/router';
import { AdminLayout } from './components/common/layouts/admin-layout/admin-layout';
import { MainPage } from './components/pages/main-page/main-page';

export const routes: Routes = [
    {
        path: '',
        component: AdminLayout,
        children: [
            { path: '', component: MainPage },
        ]
    },

    // Ruta comodín para manejar rutas no encontradas (fallback)
    { path: '**', component: MainPage },
];
