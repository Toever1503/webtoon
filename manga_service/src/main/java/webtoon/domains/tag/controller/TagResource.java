package webtoon.domains.tag.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import webtoon.domains.tag.entity.TagEntity;
import webtoon.domains.tag.service.ITagService;

@RestController("api/tags")
public class TagResource {

	private final ITagService tagService;

	public TagResource(ITagService tagService) {
		super();
		this.tagService = tagService;
	}

	@GetMapping("filter")
	public Page<TagEntity> filter(@RequestParam String s, Pageable page) {
		return this.tagService.filterTag(s, page);
	}

	@PostMapping
	public TagEntity addNew(@RequestBody TagEntity input) {
		input.setId(null);
		return this.tagService.saveTag(input);
	}

	@PutMapping("{id}")
	public TagEntity update(@PathVariable Long id, @RequestBody TagEntity input) {
		input.setId(id);
		return this.tagService.saveTag(input);
	}

	@DeleteMapping("bulk-del")
	public void deleteBulk(@RequestParam List<Long> ids) {
		this.tagService.deleteTagByIds(ids);
	}
}
