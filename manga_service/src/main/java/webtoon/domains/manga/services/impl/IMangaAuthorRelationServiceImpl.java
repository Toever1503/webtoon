package webtoon.domains.manga.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import webtoon.domains.manga.dtos.MangaAuthorRelationDto;
import webtoon.domains.manga.entities.MangaAuthorRelationEntity;
import webtoon.domains.manga.models.MangaAuthorRelationModel;
import webtoon.domains.manga.repositories.IMangaAuthorRelationRepository;
import webtoon.domains.manga.services.IMangaAuthorRelationService;

@Service
@Transactional
public class IMangaAuthorRelationServiceImpl implements IMangaAuthorRelationService {

	@Autowired
	private IMangaAuthorRelationRepository authorRelationRepository;

	@Override
	public MangaAuthorRelationDto add(MangaAuthorRelationModel model) {

		MangaAuthorRelationEntity authorRelationEntity = MangaAuthorRelationEntity.builder()
				.authorId(model.getAuthorId()).mangaId(model.getMangaId()).authorType(model.getAuthorType()).build();
		this.authorRelationRepository.saveAndFlush(authorRelationEntity);

		return MangaAuthorRelationDto.builder().authorId(authorRelationEntity.getAuthorId())
				.mangaId(authorRelationEntity.getMangaId()).authorType(authorRelationEntity.getAuthorType()).build();
	}

	@Override
	public MangaAuthorRelationDto update(MangaAuthorRelationModel model) {
		MangaAuthorRelationEntity authorRelationEntity = this.getById(model.getId());
		authorRelationEntity.setAuthorId(model.getAuthorId());
		authorRelationEntity.setMangaId(model.getMangaId());
		authorRelationEntity.setAuthorType(model.getAuthorType());
		authorRelationRepository.saveAndFlush(authorRelationEntity);
		return MangaAuthorRelationDto.builder().authorId(authorRelationEntity.getAuthorId())
				.mangaId(authorRelationEntity.getMangaId()).authorType(authorRelationEntity.getAuthorType()).build();

	}

	@Override
	public boolean deleteById(Long id) {
		try {
			this.authorRelationRepository.findById(id);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
	@Override
	public Page<MangaAuthorRelationDto> filter(Pageable pageable,Specification<MangaAuthorRelationEntity> specs){
		return authorRelationRepository.findAll(specs, pageable).map(MangaAuthorRelationDto::toDto);
	}

	public MangaAuthorRelationEntity getById(Long id) {
		return this.authorRelationRepository.findById(id).orElseThrow(() -> new RuntimeException("22"));
	}
}
