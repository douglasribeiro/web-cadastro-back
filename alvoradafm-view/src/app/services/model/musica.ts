export interface Musica {

  nome: string;
  interprete: string;
	album: string;
	gravadora: string;
	lancamento: string;
	compositor: string;
	intervalo: number;
	duracaoSegundos: bigint;
	introducao: bigint;
	genero: string;
	caminhoArquivo: string;
	ArquivoUrl: string;
  capa: Blob;
}
