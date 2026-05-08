import { isPlatformBrowser } from '@angular/common';
import { Component, ElementRef, Inject, OnInit, PLATFORM_ID, ViewChild } from '@angular/core';

@Component({
  selector: 'app-plat-list',
  imports: [],
  templateUrl: './plat-list.component.html',
  styleUrl: './plat-list.component.css'
})
export class PlatListComponent implements OnInit {
  @ViewChild('canvas') canvas!: ElementRef<HTMLCanvasElement>;

  // Configurações
  public isPlaylistOpen = false; // Controle do painel
  public playlist: string[] = []; // Nomes dos arquivos no Java
  public currentIndex = 0;
  public crossfadeSeconds = 5;
  public isPlaying = false;
  public progress = 0;
  public songs: any[] = [];

  private audioCtx?: AudioContext;
  private analyser?: AnalyserNode;
  private currentSource?: AudioBufferSourceNode;
  private currentGain?: GainNode;

  private duration = 0;
  private startTime = 0;
  private nextTrackTimer: any;

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {}

  private async initAudio() {
    if (isPlatformBrowser(this.platformId) && !this.audioCtx) {
      this.audioCtx = new AudioContext();
      this.analyser = this.audioCtx.createAnalyser();
      this.analyser.connect(this.audioCtx.destination);
    }
  }

  async ngOnInit() {
    const resp = await fetch('http://localhost:8080/api/songs');
    this.songs = await resp.json();
    this.songs.forEach((element) => {
      this.playlist.push(element.filename);
    });
  }

  togglePlaylist() {
    this.isPlaylistOpen = !this.isPlaylistOpen;
  }

  getAlbumArt(song: any) {
    if (!song.albumArt) return 'assets/default-cover.png';
    return `data:image/jpeg;base64,${song.albumArt}`;
  }

  async playPlaylist(index: number) {
    await this.initAudio();
    this.currentIndex = index;
    await this.startTrack(
      this.playlist[this.currentIndex],
      this.crossfadeSeconds,
    );
  }

  private async startTrack(filename: string, fadeTime: number) {
    if (!this.audioCtx) return;
    clearTimeout(this.nextTrackTimer);

    const oldSource = this.currentSource;
    const oldGain = this.currentGain;

    // Carregamento
    const response = await fetch(`http://localhost:8080/api/audio/${filename}`);
    const buffer = await this.audioCtx.decodeAudioData(
      await response.arrayBuffer(),
    );

    // Novo Canal
    const newSource = this.audioCtx.createBufferSource();
    const newGain = this.audioCtx.createGain();
    newSource.buffer = buffer;
    newSource.connect(newGain).connect(this.analyser!);

    const now = this.audioCtx.currentTime;

    // CROSSFADE: Fade Out da antiga / Fade In da nova
    if (oldGain) {
      oldGain.gain.setValueAtTime(oldGain.gain.value, now);
      oldGain.gain.linearRampToValueAtTime(0.001, now + fadeTime);
      setTimeout(() => oldSource?.stop(), fadeTime * 1000);
    }

    newGain.gain.setValueAtTime(0, now);
    newGain.gain.linearRampToValueAtTime(1, now + fadeTime);

    newSource.start(0);

    this.currentSource = newSource;
    this.currentGain = newGain;
    this.duration = buffer.duration;
    this.startTime = now;
    this.isPlaying = true;

    this.initVisualizer();
    this.updateProgress();

    // Agenda próxima música automaticamente
    const timeUntilNext = this.duration - fadeTime;
    this.nextTrackTimer = setTimeout(
      () => this.nextWithTransition(),
      timeUntilNext * 1000,
    );
  }

  nextWithTransition() {
    this.currentIndex = (this.currentIndex + 1) % this.playlist.length;
    this.startTrack(this.playlist[this.currentIndex], this.crossfadeSeconds);
  }

  updateProgress() {
    if (!this.isPlaying || !this.audioCtx) return;
    const elapsed = this.audioCtx.currentTime - this.startTime;
    this.progress = (elapsed / this.duration) * 100;
    if (this.progress < 100) requestAnimationFrame(() => this.updateProgress());
  }

  initVisualizer() {
    if (!this.canvas || !this.analyser) return;
    const ctx = this.canvas.nativeElement.getContext('2d')!;
    const dataArray = new Uint8Array(this.analyser.frequencyBinCount);

    const draw = () => {
      if (!this.isPlaying) return;
      requestAnimationFrame(draw);
      this.analyser!.getByteFrequencyData(dataArray);

      ctx.fillStyle = '#111';
      ctx.fillRect(0, 0, 600, 150);

      dataArray.forEach((v, i) => {
        ctx.fillStyle = '#1db954';
        ctx.fillRect(i * 3, 150 - v / 2, 2, v / 2);
      });
    };
    draw();
  }

  restart() {
    this.startTrack(this.playlist[this.currentIndex], 0); // Reinício seco
  }

  stop() {
    clearTimeout(this.nextTrackTimer);
    this.currentSource?.stop();
    this.isPlaying = false;
    this.progress = 0;
  }
}
