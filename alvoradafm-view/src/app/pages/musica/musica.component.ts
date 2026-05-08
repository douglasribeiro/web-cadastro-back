import { MusicaService } from './../../services/music.service';
import { Component, inject, signal, PLATFORM_ID, OnInit } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';

@Component({
  selector: 'app-musica',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './musica.component.html',
  styleUrl: './musica.component.css',
})
export class MusicaComponent implements OnInit {
  private platformId = inject(PLATFORM_ID);
  private fb = inject(FormBuilder);
  private readonly musicaService = inject(MusicaService);

  // Signals para a UI
  public musicaForm: FormGroup;
  public capaPreview = signal<string | null>(null);
  public carregando = signal<boolean>(false);
  public isBrowser = signal<boolean>(false);

  // Instância da biblioteca carregada dinamicamente
  private musicMetadata: any;

  constructor() //private musicaService: MusicaService
  {
    this.isBrowser.set(isPlatformBrowser(this.platformId));

    this.musicaForm = this.fb.group({
      nome: ['', Validators.required],
      interprete: ['', Validators.required],
      album: [''],
      gravadora: [''],
      lancamento: [''],
      compositor: [''],
      intervalo: [0, Validators.min(1)],
      duracaoSegundos: [0n],
      introducao: [0n],
      genero: ['', Validators.required],
      caminhoArquivo: [''],
      ArquivoUrl: [''],
      Capa:[''],
    });
  }

  async ngOnInit() {
    // IMPORTANTE: Configura o ambiente apenas no Navegador (evita erro de window/buffer no SSR)
    if (this.isBrowser()) {
      try {
        // 1. Injeta o Buffer globalmente antes de carregar a lib de música
        const { Buffer } = await import('buffer');
        (window as any).Buffer = Buffer;
        (window as any).global = window;

        // 2. Carrega a biblioteca de metadados dinamicamente
        this.musicMetadata = await import('music-metadata-browser');
        console.log('Ambiente de áudio pronto.');
      } catch (err) {
        console.error('Erro ao carregar polyfills de áudio:', err);
      }
    }
  }

  async onArquivoSelecionado(event: Event) {
    if (!this.isBrowser() || !this.musicMetadata) return;

    const input = event.target as HTMLInputElement;
    if (!input.files?.length) return;

    const arquivo = input.files[0];
    this.carregando.set(true);

    try {
      // Extração profunda usando a lib carregada dinamicamente
      const metadata = await this.musicMetadata.parseBlob(arquivo);
      const { common, format } = metadata;

      // Atualiza o formulário (convertendo para os tipos da sua interface)
      this.musicaForm.patchValue({
        nome: common.title || arquivo.name.replace(/\.[^/.]+$/, ''),
        interprete: common.artist || 'Desconhecido',
        album: common.album || '',
        gravadora: common.label?.[0] || '',
        lancamento: common.year?.toString() || '',
        compositor: common.composer?.[0] || '',
        genero: common.genre?.[0] || '',
        duracaoSegundos: format.duration
          ? BigInt(Math.round(format.duration))
          : 0n,
        caminhoArquivo: arquivo.name,
        ArquivoUrl: URL.createObjectURL(arquivo),
        Capa: this.capaPreview,
      });

      // Tratamento da Capa (Picture)
      if (common.picture && common.picture.length > 0) {
        const pic = common.picture[0];
        // Usa o Buffer global que injetamos no ngOnInit
        const uint8Array = new Uint8Array(
          (window as any).Buffer.from(pic.data),
        );
        const blob = new Blob([uint8Array], { type: pic.format });

        if (this.capaPreview()) URL.revokeObjectURL(this.capaPreview()!);
        this.capaPreview.set(URL.createObjectURL(blob));
      }
    } catch (erro) {
      console.error('Erro ao processar arquivo:', erro);
    } finally {
      this.carregando.set(false);
    }
  }

  formatarDuracao(segundos: any): string {
    const s = Number(segundos || 0);
    return `${Math.floor(s / 60)}:${(s % 60).toString().padStart(2, '0')}`;
  }

  salvar() {
    console.log('Dados finais:', this.musicaForm.value);
    if (this.musicaForm.valid) {
      const dadosForm = this.musicaForm.value;
      console.log('registro para service ', this.musicaForm);
      // Certifique-se de que o nome aqui é o mesmo que você injetou lá em cima
      this.musicaService.salvarMusica(dadosForm).subscribe({
        next: (res) => {
          console.log('Salvo com sucesso!', res);
          alert('Música salva!');
        },
        error: (err) => {
          console.error('Erro ao salvar:', err);
        },
      });
    }
  }
}
