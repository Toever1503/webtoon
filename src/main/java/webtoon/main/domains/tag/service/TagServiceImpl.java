package webtoon.main.domains.tag.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import webtoon.main.domains.manga.entities.MangaGenreEntity;
import webtoon.main.domains.tag.entity.ITagRelationRepository;
import webtoon.main.domains.tag.entity.ITagRepository;
import webtoon.main.domains.tag.entity.TagEntity;
import webtoon.main.domains.tag.entity.TagEntityRelation;
import webtoon.main.domains.tag.entity.*;
import webtoon.main.domains.tag.entity.enums.ETagType;
import webtoon.main.utils.ASCIIConverter;
import webtoon.main.utils.exception.CustomHandleException;

@Service
public class TagServiceImpl implements ITagService {

    private final ITagRepository tagRepository;

    private final ITagRelationRepository tagRelationRepository;

    public TagServiceImpl(ITagRepository tagRepository, ITagRelationRepository tagRelationRepository) {
        super();
        this.tagRepository = tagRepository;
        this.tagRelationRepository = tagRelationRepository;
    }

    @Override
    public TagEntity saveTag(TagEntity input) {
        // TODO Auto-generated method stub
        if (input.getSlug() != null)
            input.setSlug(ASCIIConverter.removeAccent(input.getSlug()));
        else
            input.setSlug(ASCIIConverter.removeAccent(input.getTagName()));
        return this.tagRepository.saveAndFlush(input);
    }

    @Override
    public List<TagEntity> saveTagRelation(Long objectId, List<Long> tagIds, ETagType tagType) {
        this.tagRelationRepository.deleteAllByObjectIDAndTagType(objectId, tagType.name());

        List<TagEntity> tagEntities = this.tagRepository.findAllById(tagIds);
        List<TagEntityRelation> tagEntityRelations = tagEntities.stream().map(t -> TagEntityRelation.builder()
                .objectID(objectId)
                .tag(t)
                .tagType(tagType.name()).build()).collect(Collectors.toList());
        this.tagRelationRepository.saveAllAndFlush(tagEntityRelations);
        return tagEntities;
    }

    @Override
    public List<TagEntity> findAllByObjectIdAndType(Long objectId, ETagType tagType) {
        return this.tagRelationRepository.findAllByObjectIDAndTagType(objectId, tagType.name())
                .stream().map(TagEntityRelation::getTag).collect(Collectors.toList());
    }

    @Override
    public void deleteTagByIds(List<Long> ids) {
        // TODO Auto-generated method stub
        this.tagRepository.deleteAllById(ids);
    }

    @Override
    public Page<TagEntity> filterTag(String s, Pageable page) {
        Specification spec = null;
//        if(s)
        // TODO Auto-generated method stub
        return this.tagRepository.findAll((root, query, cb) -> {
            return cb.like(root.get(TagEntity_.TAG_NAME), "%" + s + "%");
        }, page);
    }

    @Override
    public TagEntity getById(Long id) {
        return this.tagRepository.findById(id).orElseThrow(() -> new CustomHandleException(1));
    }

    @Override
    public List<TagEntity> findAll() {
        return this.tagRepository.findAll();
    }

    @Override
    public List<TagEntity> findAll(Pageable pageable) {
        return this.tagRepository.findAll(pageable).getContent();
    }

}
