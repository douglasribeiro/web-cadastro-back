import { Routes } from '@angular/router';
import { MusicaListaComponent } from './pages/musica/musica-lista/musica-lista.component';
import { MusicaComponent } from './pages/musica/musica.component';

export const routes: Routes = [
  { path: 'musicas', component: MusicaListaComponent },
  { path: 'musicas/novo', component: MusicaComponent },
  { path: 'musicas/editar/:id', component: MusicaComponent },
  { path: '', redirectTo: 'musicas', pathMatch: 'full' } // Redireciona para lista ao abrir
];
