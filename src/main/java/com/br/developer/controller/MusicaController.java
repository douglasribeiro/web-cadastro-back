package com.br.developer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.br.developer.dto.MusicaDto;
import com.br.developer.dto.MusicaRequest;
import com.br.developer.generics.ApiResponse;
import com.br.developer.generics.BaseController;
import com.br.developer.service.AuditoriaService;
import com.br.developer.service.MusicaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/musica")
@RequiredArgsConstructor
public class MusicaController extends BaseController<MusicaDto, MusicaService> {
	
	private Logger logger = LoggerFactory.getLogger(MusicaController.class);
	private final AuditoriaService auditoria;
	
	@PostMapping("/upload")
	public ResponseEntity<ApiResponse<MusicaDto>> upload(@RequestParam("file") MultipartFile file) {
		try {
			MusicaDto salva = service.processarESalvar(file);
			return ResponseEntity.ok(ApiResponse.success(salva, "Música cadastrada via MP3!"));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping("/personal")
	//@Auditavel(operacao = "SALVAR_MUSICA") 
	public ResponseEntity<?> create(@Valid @RequestBody MusicaRequest request, @AuthenticationPrincipal Jwt jwt) {
	    String username = jwt.getClaim("preferred_username"); 
	    logger.info("Usuário {} salvando registro.", username);
	    request.setUsuario(username);
	    MusicaDto salva = service.createMusic(request);

	    return ResponseEntity.ok(ApiResponse.success(salva, "Salvar personalizado para " + username));
	
		//logger.info("Chegada ao serviço que salva registro.");
			//MusicaDto salva = service.createMusic(request);
		
			//return ResponseEntity.ok(ApiResponse.success(salva, "Salvar personalizado."));		
	}

	@Override
	//@Auditavel(operacao = "ALTERAR_MUSICA")
	public ResponseEntity<ApiResponse<MusicaDto>>  update(@PathVariable Long id, @RequestBody MusicaDto dto) {
		logger.info("Serviço alternativo que altera registro.");
		MusicaRequest musicaRequest = MusicaRequest.builder()
				.id(id)
				.album(dto.getAlbum())
				.ArquivoUrl(dto.getArquivoUrl())
				.caminhoArquivo(dto.getCaminhoArquivo())
				.compositor(dto.getCompositor())
				.duracaoSegundos(dto.getDuracaoSegundos())
				.genero(dto.getGenero().getNome())
				.gravadora(dto.getGravadora().getNome())
				.interprete(dto.getInterprete().getNome())
				.intervalo(dto.getIntervalo())
				.introducao(dto.getIntroducao())
				.lancamento(dto.getLancamento())
				.nome(dto.getNome())
				.build();
		MusicaDto salva = service.createMusic(musicaRequest);
		
		return ResponseEntity.ok(ApiResponse.success(salva, "Salvar personalizado."));
	}
	
	@GetMapping("/audit")
	public void quemAlterou() {
		auditoria.mostrarQuemAlterou(97L);
	}
	
}
