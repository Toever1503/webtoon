package webtoon.services.impl;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Query;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import webtoon.dtos.MangaDto;
import webtoon.entities.MangaEntity;
import webtoon.models.MangaFilterModel;
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
				.alternativeTitle(model.getAlternativeTitle())
				.concerpt(model.getConcerpt())
				.description(model.getDescription())
				.mangaName(model.getMangaName())
				.featuredImage(model.getFeaturedImage())
//				.status(null)
//				.mangaSts(null)
				.commentCount(model.getCommentCount())
//				.mangaType(null)
				.rating(model.getRating())
				.created_at(model.getCreated_at())
				.modifed_at(model.getModifed_at())
				.build();
		 this.mangaRepository.saveAndFlush(mangaEntity);
		return MangaDto.builder()
				.title(mangaEntity.getTitle())
				.alternativeTitle(mangaEntity.getAlternativeTitle())
				.concerpt(mangaEntity.getConcerpt())
				.description(mangaEntity.getDescription())
				.mangaName(mangaEntity.getMangaName())
				.featuredImage(mangaEntity.getFeaturedImage())
//				.status(null)
//				.mangaSts(null)
				.commentCount(mangaEntity.getCommentCount())
//				.mangaType(null)
				.rating(mangaEntity.getRating())
				.view_Count(mangaEntity.getView_Count())
				.created_at(mangaEntity.getCreated_at())
				.modifed_at(mangaEntity.getModifed_at())
				.build();
		
	}
	
	@Override
	public MangaDto update(MangaModel model) {
		
		MangaEntity mangaEntity = this.getById(model.getId());
		
		mangaEntity.setTitle(model.getTitle());
		mangaEntity.setAlternativeTitle(model.getAlternativeTitle());
		mangaEntity.setConcerpt(model.getConcerpt());
		mangaEntity.setDescription(model.getDescription());
		mangaEntity.setMangaName(model.getMangaName());
		mangaEntity.setFeaturedImage(model.getFeaturedImage());
		mangaEntity.setCommentCount(model.getCommentCount());
		mangaEntity.setCreated_at(model.getCreated_at());
		mangaEntity.setModifed_at(model.getModifed_at());
		mangaEntity.setRating(model.getRating());
		mangaEntity.setView_Count(model.getView_Count());
		mangaRepository.saveAndFlush(mangaEntity);
		return MangaDto.builder()
				.title(mangaEntity.getTitle())
				.alternativeTitle(mangaEntity.getAlternativeTitle())
				.concerpt(mangaEntity.getConcerpt())
				.description(mangaEntity.getDescription())
				.mangaName(mangaEntity.getMangaName())
				.featuredImage(mangaEntity.getFeaturedImage())
//				.status(null)
//				.mangaSts(null)
				.commentCount(mangaEntity.getCommentCount())
//				.mangaType(null)
				.rating(mangaEntity.getRating())
				.view_Count(mangaEntity.getView_Count())
				.created_at(mangaEntity.getCreated_at())
				.modifed_at(mangaEntity.getModifed_at())
				.build();
				
	}
	
	public MangaEntity getById(Long id) {
		return this.mangaRepository.findById(id).orElseThrow();
	}
	
	@Override
	public boolean deleteById(Long id) {
		try {
			this.mangaRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	@Override
	public Page<MangaDto> filter(Pageable pageable,Specification<MangaEntity> specs){
		return mangaRepository.findAll(specs, pageable).map(MangaDto::toDto);
	}
}
