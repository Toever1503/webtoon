package webtoon.domains.manga.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import webtoon.domains.manga.dtos.MangaAuthorDto;
import webtoon.domains.manga.entities.MangaGenreEntity;
import webtoon.domains.manga.models.MangaAuthorModel;
import webtoon.domains.manga.repositories.IMangaAuthorRepository;
import webtoon.domains.manga.entities.MangaAuthorEntity;
import webtoon.domains.manga.services.IMangaAuthorService;
import webtoon.utils.ASCIIConverter;
import webtoon.utils.exception.CustomHandleException;

import java.util.List;

@Service
@Transactional
public class IMangaAuthorServiceImpl implements IMangaAuthorService {

    @Autowired
    private IMangaAuthorRepository mangaAuthorRepository;

    @Override
    public MangaAuthorEntity add(MangaAuthorModel model) {
        MangaAuthorEntity authorEntity = this.getById(model.getId());
        authorEntity.setName(model.getName());
        if (model.getSlug() != null)
            authorEntity.setSlug(ASCIIConverter.removeAccent(model.getSlug()));
        else
            authorEntity.setSlug(ASCIIConverter.removeAccent(model.getName()));

        if (mangaAuthorRepository.findByName(model.getName()).isPresent())
            throw new CustomHandleException(111);
        if (mangaAuthorRepository.findBySlug(authorEntity.getSlug()).isPresent())
            throw new CustomHandleException(112);
        this.mangaAuthorRepository.saveAndFlush(authorEntity);
        return authorEntity;
    }

    @Override
    public MangaAuthorEntity update(MangaAuthorModel model) {
        MangaAuthorEntity authorEntity = this.getById(model.getId());
        authorEntity.setName(model.getName());
        if (model.getSlug() != null)
            authorEntity.setSlug(ASCIIConverter.removeAccent(model.getSlug()));
        else
            authorEntity.setSlug(ASCIIConverter.removeAccent(model.getName()));

        MangaAuthorEntity checkGenre = mangaAuthorRepository.findByName(model.getName()).orElse(null);
        if (checkGenre != null && (checkGenre.getId() != authorEntity.getId()))
            throw new CustomHandleException(111);
        checkGenre = mangaAuthorRepository.findBySlug(model.getSlug()).orElse(null);
        if (checkGenre != null && (checkGenre.getId() != authorEntity.getId()))
            throw new CustomHandleException(112);
        mangaAuthorRepository.saveAndFlush(authorEntity);
        return authorEntity;
    }

    @Override
    public MangaAuthorEntity getById(Long id) {
        return this.mangaAuthorRepository.findById(id).orElseThrow(() -> new RuntimeException("22"));
    }

    @Override
    public Page<MangaAuthorDto> filter(Pageable pageable, Specification<MangaAuthorEntity> specs) {
        return mangaAuthorRepository.findAll(specs, pageable).map(MangaAuthorDto::toDto);
    }

    @Override
    public Page<MangaAuthorEntity> filterAuthor(Specification spec, Pageable page) {
        return this.mangaAuthorRepository.findAll(spec, page);
    }

    @Override
    public void deleteAuthorByIds(List<Long> ids) {
        try {
            this.mangaAuthorRepository.deleteAllById(ids);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("delete author failed");
        }
    }

    @Override
    public List<MangaAuthorEntity> getAll() {
        return this.mangaAuthorRepository.findAll();
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            this.mangaAuthorRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }

}
