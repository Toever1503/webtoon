package webtoon.domains.manga.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import webtoon.domains.manga.dtos.MangaVolumeDto;
import webtoon.domains.manga.entities.MangaVolumeEntity;
import webtoon.domains.manga.models.MangaVolumeFilterInput;
import webtoon.domains.manga.models.MangaVolumeModel;
import webtoon.domains.manga.repositories.IMangaChapterRepository;
import webtoon.domains.manga.repositories.IMangaVolumeRepository;
import webtoon.domains.manga.entities.MangaVolumeEntity_;
import webtoon.domains.manga.services.IMangaService;
import webtoon.domains.manga.services.IMangaVolumeService;

import java.util.List;

import java.util.ArrayList;

@Service
@Transactional
public class IMangaVolumeServiceImpl implements IMangaVolumeService {

    @Autowired
    private IMangaVolumeRepository mangaVolumeRepository;

    @Autowired
    private IMangaService mangaService;

    @Autowired
    private IMangaChapterRepository mangaChapterRepository;

    @Override
    public MangaVolumeDto add(MangaVolumeModel model) {
        MangaVolumeEntity entity = MangaVolumeEntity.builder()
                .manga(mangaService.getById(model.getMangaId()))
                .name(model.getName()).build();
        MangaVolumeDto lastVolume = this.getLastVolIndex(model.getMangaId());
        if (lastVolume == null)
            entity.setVolumeIndex(0);
        else
            entity.setVolumeIndex(lastVolume.getVolumeIndex() + 1);
        mangaVolumeRepository.saveAndFlush(entity);
        return MangaVolumeDto.builder().id(entity.getId()).mangaId(entity.getManga().getId()).name(entity.getName())
                .volumeIndex(entity.getVolumeIndex()).build();
    }

    @Override
    public MangaVolumeDto update(MangaVolumeModel model) {
        MangaVolumeEntity entity = this.getById(model.getId());
        entity.setName(model.getName());
        mangaVolumeRepository.saveAndFlush(entity);
        return MangaVolumeDto.builder().id(entity.getId()).mangaId(entity.getManga().getId()).name(entity.getName())
                .volumeIndex(entity.getVolumeIndex()).build();
    }


    @Override
    public boolean deleteById(Long id) {
        try {
            MangaVolumeEntity entity = this.getById(id);
            this.mangaVolumeRepository.reindexVolumeAfterIndex(entity.getVolumeIndex());
            mangaVolumeRepository.deleteById(id);
            // task: need reindex volume
            return true;
        } catch (Exception e) {
            return false;
            // TODO: handle exception
        }
    }

    @Override
    public Page<MangaVolumeDto> filter(Pageable pageable, Specification<MangaVolumeEntity> specs) {
        return mangaVolumeRepository.findAll(specs, pageable).map(MangaVolumeDto::toDto);
    }

    @Override
    public MangaVolumeEntity getById(Long id) {
        return this.mangaVolumeRepository.findById(id).orElseThrow(() -> new RuntimeException("22"));
    }

    @Override
    public MangaVolumeDto findById(Long id) {
        return MangaVolumeDto.toDto(this.getById(id));
    }
    @Override
    public List<MangaVolumeEntity> findAll() {
        return mangaVolumeRepository.findAll();
    }

    @Override
    public Page<MangaVolumeDto> filterVolume(Pageable pageable, MangaVolumeFilterInput input) {
        if (input.getQ() != null)
            input.setQ("%" + input.getQ() + "%");

        List<Specification> specs = new ArrayList<>();

        if (input.getQ() != null) {
            specs.add((root, query, cb) -> cb.like(root.get(MangaVolumeEntity_.NAME), input.getQ()));
        }
        if (input.getMangaId() != null) {
            specs.add((root, query, cb) -> cb.equal(root.get(MangaVolumeEntity_.MANGA).get("id"), input.getMangaId()));
        }
        if (input.getVolumeIndex() != null) {
            specs.add((root, query, cb) -> cb.equal(root.get(MangaVolumeEntity_.VOLUME_INDEX), input.getVolumeIndex()));
        }
        Specification<MangaVolumeEntity> finalSpec = null;
        for (Specification spec : specs) {
            if (finalSpec == null) {
                finalSpec = spec;
            } else {
                finalSpec = finalSpec.and(spec);
            }
        }
        return this.mangaVolumeRepository.findAll(finalSpec, pageable).map(MangaVolumeDto::toDto);
    }

    @Override
    public MangaVolumeDto getLastVolIndex(Long mangaId) {
        MangaVolumeEntity entity = this.mangaVolumeRepository.getLastIndex(mangaId).orElse(null);
        if (entity == null) return null;
        return MangaVolumeDto.toDto(entity);
    }

	@Override
	public Page<MangaVolumeEntity> filterBy(String s, Pageable page){
		return this.mangaVolumeRepository.findAll((root, query, cb) -> {
			return cb.like(root.get(MangaVolumeEntity_.NAME),"%" + s + "%");
		},page);
	}

    @Override
    public List<MangaVolumeEntity> findByManga(Long manga){

        return mangaVolumeRepository.findByMangaId(manga);
    }

    @Override
    public MangaVolumeEntity finByMangaId(Long mangaId){
        return mangaVolumeRepository.finByMangaId(mangaId);
    }

}
