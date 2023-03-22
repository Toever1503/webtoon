package webtoon.domains.tag.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import webtoon.domains.tag.entity.ITagRepository;
import webtoon.domains.tag.entity.TagEntity;

@Service
public class TagServiceImpl implements ITagService {

	private final ITagRepository tagRepository;

	public TagServiceImpl(ITagRepository tagRepository) {
		super();
		this.tagRepository = tagRepository;
	}

	@Override
	public TagEntity saveTag(TagEntity input) {
		// TODO Auto-generated method stub
		return this.tagRepository.saveAndFlush(input);
	}

	@Override
	public void deleteTagByIds(List<Long> ids) {
		// TODO Auto-generated method stub
		this.tagRepository.deleteAllById(ids);
	}

	@Override
	public Page<TagEntity> filterTag(String s, Pageable page) {
		// TODO Auto-generated method stub
		return this.tagRepository.findAll((root, query, cb) -> {
			return cb.like(root.get("tagName"), "%" + s + "%");
		}, page);
	}

}
