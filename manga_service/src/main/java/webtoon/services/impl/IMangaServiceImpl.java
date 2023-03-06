package webtoon.services.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import webtoon.dtos.MangaDto;
import webtoon.entities.MangaEntity;
import webtoon.models.MangaModel;
import webtoon.repositories.IMangaRepository;
import webtoon.services.IMangaService;
@Service
@Transactional
public class IMangaServiceImpl implements IMangaService {
	@Autowired
	private IMangaRepository mangaRepository;
	
	@Override
	public MangaDto add(MangaModel model) {
		MangaEntity mangaEntity = MangaEntity.builder()
				.title(model.getTitle())
				.build();
		 this.mangaRepository.saveAndFlush(mangaEntity);
		return MangaDto.builder()
				.title(mangaEntity.getTitle())
				.build();
		
	}
}
