import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Musica } from './model/musica';
import { first, map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MusicaService {
  private http = inject(HttpClient);
  private readonly API_URL = 'http://localhost:8080/api/musica';

  // Retorna a lista de músicas
  listarTodas(): Observable<Musica[]> {
    return this.http.get<Musica[]>(this.API_URL);
  }

  /**
   * Busca uma música pelo ID e converte campos numéricos para bigint
   */
  getMusica(id: string): Observable<Musica> {
    return this.http.get<Musica>(`${this.API_URL}/${id}`).pipe(
      map(dados => ({
        ...dados,
        // Conversão explícita necessária pois o JSON da API vem como number ou string
        duracaoSegundos: BigInt(dados.duracaoSegundos),
        introducao: BigInt(dados.introducao)
      }))
    );
  }

  /**
   * Salva uma nova música enviando para a API
   */
  salvarMusica(novaMusica: Musica): Observable<Musica> {
    // Ao enviar, convertemos o bigint para string para o JSON aceitar
    console.log(novaMusica);
    const payload = {
      ...novaMusica,
      duracaoSegundos: novaMusica.duracaoSegundos.toString(),
      introducao: novaMusica.introducao.toString()
    };

    return this.http.post<Musica>(this.API_URL+'/personal', payload);
  }
}
