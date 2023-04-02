package webtoon.domains.tag.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import webtoon.domains.tag.entity.TagEntity;
import webtoon.domains.tag.entity.enums.ETagType;

public interface ITagService {
	
	TagEntity saveTag(TagEntity input);

	List<TagEntity> saveTagRelation(Long objectId, List<Long> tagIds, ETagType tagType);
	
	void deleteTagByIds(List<Long> ids);

	Page<TagEntity> filterTag(String s, Pageable page);
}
