import { ConfigComponent } from './pages/config/config.component';
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTabsModule } from '@angular/material/tabs';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip'; // Importante para o menu retrátil

import { MusicaComponent } from './pages/musica/musica.component';
import { ComerciaisComponent } from './pages/comerciais/comerciais.component';
import { PlatListComponent } from './pages/plat-list/plat-list.component';

interface Tab {
  label: string;
  component: any;
  content?: string;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    MatSidenavModule,
    MatToolbarModule,
    MatTabsModule,
    MatListModule,
    MatIconModule,
    MatButtonModule,
    MatTooltipModule
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  isMenuCollapsed = false;

  modulos = [
    { label: 'Configuração', icon: 'group', component: ConfigComponent },
    { label: 'Musica', icon: 'queue_music', component: MusicaComponent},
    { label: 'Comerciais', icon: 'attach_money', component: ComerciaisComponent},
    //{ label: 'Play List', icon: 'playlist_play', component: PlatListComponent}
  ];

  tabsAbertas: Tab[] = [];
  abaAtiva?: Tab;

  toggleMenu() {
    this.isMenuCollapsed = !this.isMenuCollapsed;
  }

  selecionarModulo(modulo: any) {
    const aberta = this.tabsAbertas.find(t => t.label === modulo.label);
    if (!aberta) {
      const novaTab = { label: modulo.label, component: modulo.component };
      this.tabsAbertas.push(novaTab);
      this.abaAtiva = novaTab;
    } else {
      this.abaAtiva = aberta;
    }
  }

  fecharAba(index: number, event: MouseEvent) {
    event.stopPropagation();
    const removida = this.tabsAbertas.splice(index, 1)[0];
    if (this.abaAtiva === removida) {
      this.abaAtiva = this.tabsAbertas[this.tabsAbertas.length - 1];
    }
  }
}
