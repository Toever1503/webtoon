package webtoon.domains.manga.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import webtoon.domains.manga.dtos.MangaGenreDto;
import webtoon.domains.manga.entities.MangaGenreEntity;
import webtoon.domains.manga.models.MangaGenreModel;
import webtoon.domains.manga.repositories.IMangaGenreRepository;
import webtoon.domains.manga.services.IMangaGenreService;
import webtoon.main.utils.ASCIIConverter;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class IMangaGenreServiceImpl implements IMangaGenreService {

    @Autowired
    private IMangaGenreRepository genreRepository;

    @Override
    public MangaGenreEntity add(MangaGenreModel model) {
        MangaGenreEntity entity = MangaGenreEntity.builder().name(model.getName()).slug(model.getSlug()).build();
        if (model.getSlug() != null)
            entity.setSlug(ASCIIConverter.removeAccent(model.getSlug()));
        else
            entity.setSlug(ASCIIConverter.removeAccent(model.getName()));
        genreRepository.saveAndFlush(entity);
        return entity;
    }

    @Override
    public MangaGenreEntity update(MangaGenreModel model) {
        MangaGenreEntity entity = this.getById(model.getId());
        entity.setName(model.getName());
        entity.setSlug(model.getSlug());
        genreRepository.saveAndFlush(entity);
        return entity;
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            genreRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }

    @Override
    public Page<MangaGenreDto> filter(Pageable pageable, Specification<MangaGenreEntity> specs) {
        return genreRepository.findAll(specs, pageable).map(MangaGenreDto::toDto);
    }

    @Override
    public Page<MangaGenreEntity> filterGenre(Specification spec, Pageable page) {
        return this.genreRepository.findAll(spec, page);
    }

    @Override
    public void deleteGenreByIds(List<Long> ids) {
        try {
            this.genreRepository.deleteAllById(ids);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("delete genre failed");
        }
    }

    public MangaGenreEntity getById(Long id) {
        return this.genreRepository.findById(id).orElseThrow(() -> new RuntimeException("22"));
    }

    @Override
    public List<MangaGenreEntity> findAllGenre(){
        return this.genreRepository.findAll()
                .stream().map(mangaGenreEntity -> {
                    return MangaGenreEntity.builder()
                            .id(mangaGenreEntity.getId())
                            .name(mangaGenreEntity.getName())
                            .slug(mangaGenreEntity.getSlug())
                            .mangaCount(mangaGenreEntity.getMangaCount())
                            .build();
                }).collect(Collectors.toList());
    }

}
