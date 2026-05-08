package com.br.developer.service;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.br.developer.dto.MusicaDto;
import com.br.developer.dto.MusicaRequest;
import com.br.developer.generics.BaseService;
import com.br.developer.model.Genero;
import com.br.developer.model.Gravadora;
import com.br.developer.model.Interprete;
import com.br.developer.model.Musica;
import com.br.developer.repository.GeneroRepository;
import com.br.developer.repository.GravadoraRepository;
import com.br.developer.repository.InterpreteRepository;
import com.br.developer.repository.MusicaRepository;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;

import jakarta.validation.Valid;

@Service
public class MusicaService extends BaseService<Musica, MusicaDto> {

	@Value("${app.upload.dir:D:/uploads/musicas}") // Define o caminho no application.properties
	private String diretorioDestino;
	private final MusicaRepository musicaRepository;
	private final InterpreteRepository interpreteRepository;
	private final GeneroRepository generoRepository;
	private final GravadoraRepository gravadoraRepository;
	private ModelMapper mapper;
	
	public MusicaService(
			InterpreteRepository interpreteRepository ,
			GeneroRepository generoRepository,
			GravadoraRepository gravadoraRepository,
			MusicaRepository musicaRepository,
			ModelMapper mapper) {
		super(Musica.class, MusicaDto.class);
		this.interpreteRepository = interpreteRepository;
		this.generoRepository = generoRepository;
		this.gravadoraRepository = gravadoraRepository;
		this.musicaRepository = musicaRepository;
		this.mapper = mapper;
	}
	
	public MusicaDto processarESalvar(MultipartFile arquivo) throws Exception {
	    Path pathDiretorio = Paths.get(diretorioDestino).toAbsolutePath().normalize();
	    Files.createDirectories(pathDiretorio);

	    String nomeArquivo = System.currentTimeMillis() + "_" + arquivo.getOriginalFilename();
	    Path caminhoCompleto = pathDiretorio.resolve(nomeArquivo);

	    Files.copy(arquivo.getInputStream(), caminhoCompleto, StandardCopyOption.REPLACE_EXISTING);

	    Mp3File mp3file = new Mp3File(caminhoCompleto.toFile());
	    MusicaDto dto = new MusicaDto();
	    
	    dto.setCaminhoArquivo(caminhoCompleto.toString());
	    dto.setArquivoUrl("/media/" + nomeArquivo);

	    if (mp3file.hasId3v2Tag()) {
	        ID3v2 id3v2Tag = mp3file.getId3v2Tag();
	        dto.setNome(id3v2Tag.getTitle());
	        dto.setArtista(id3v2Tag.getArtist());
	        dto.setInterprete(returnInterprete(id3v2Tag.getArtist()));
	        dto.setAlbum(id3v2Tag.getAlbum());
	        dto.setAlbumArt(id3v2Tag.getAlbumImage());
	        dto.setCompositor(id3v2Tag.getComposer());
	        dto.setGravadora(returnGravadora(id3v2Tag.getPublisher()));
	        dto.setLancamento(id3v2Tag.getYear());
	        dto.setGenero(returnGenero(id3v2Tag.getGenreDescription()));
	    }
	    dto.setDuracaoSegundos(mp3file.getLengthInSeconds());

	    // 5. Salvar metadados no banco
	    return this.salvar(dto);
	}

	public MusicaDto createMusic(@Valid MusicaRequest request) {
		MusicaDto dto = new MusicaDto();
		dto.setId(request.getId());
		dto.setInterprete(returnInterprete(request.getInterprete()));
		dto.setGenero(returnGenero(request.getGenero()));
		dto.setGravadora(returnGravadora(request.getGravadora()));
		dto.setIntervalo(request.getIntervalo());
		dto.setNome(request.getNome());
		dto.setAlbum(request.getAlbum());
		dto.setLancamento(request.getLancamento());
		dto.setCompositor(request.getCompositor());
		dto.setDuracaoSegundos(request.getDuracaoSegundos());
		dto.setIntroducao(request.getIntroducao());
		dto.setCaminhoArquivo(request.getCaminhoArquivo());
		dto.setUsuario(request.getUsuario());

		return mapper.map(salvar(dto), MusicaDto.class);
	}
	
	private Interprete returnInterprete(String nome) {
		Interprete interprete = interpreteRepository.findByNome(nome)
		    .orElseGet(() -> {
		        Interprete n = new Interprete();
		        n.setNome(nome);
		        return interpreteRepository.save(n);
		    });
		return interprete;
	}
	
	private Genero returnGenero(String nome) {
		Genero genero = generoRepository.findByNome(nome)
			.orElseGet(() -> {
				Genero novo = new Genero();
				novo.setNome(nome);
				return generoRepository.save(novo);
			});
		return genero;		
	}
	
	private Gravadora returnGravadora(String nome) {
		Gravadora gravadora = gravadoraRepository.findByNome(nome)
			.orElseGet(() -> {
				Gravadora novo = new Gravadora();
				novo.setNome(nome);
				return gravadoraRepository.save(novo);
			});
		return gravadora;
	}
}
