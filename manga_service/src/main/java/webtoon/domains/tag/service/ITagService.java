package webtoon.domains.tag.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import webtoon.domains.tag.entity.TagEntity;

public interface ITagService {
	
	TagEntity saveTag(TagEntity input);
	
	void deleteTagByIds(List<Long> ids);

	Page<TagEntity> filterTag(String s, Pageable page);
}
