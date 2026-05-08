import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MusicaService } from '../../../services/music.service';
import { Musica } from '../../../services/model/musica';

@Component({
  selector: 'app-musica-lista',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './musica-lista.component.html',
  styleUrls: ['./musica-lista.component.css']
})
export class MusicaListaComponent implements OnInit {
  private musicaService = inject(MusicaService);

  // Usamos um Signal para armazenar a lista
  public musicas = signal<Musica[]>([]);
  public carregando = signal<boolean>(true);

  ngOnInit(): void {
    this.carregarMusicas();
  }

  carregarMusicas(): void {
    this.musicaService.listarTodas().subscribe({
      next: (dados) => {
        this.musicas.set(dados);
        this.carregando.set(false);
      },
      error: (err) => {
        console.error('Erro ao listar músicas:', err);
        this.carregando.set(false);
      }
    });
  }

  // Função auxiliar para mostrar a duração formatada
  formatarDuracao(segundos: any): string {
    const s = Number(segundos || 0);
    return `${Math.floor(s / 60)}:${(s % 60).toString().padStart(2, '0')}`;
  }
}
