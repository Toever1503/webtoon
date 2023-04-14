package webtoon.storage.api.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import webtoon.storage.domain.dtos.FileDto;
import webtoon.storage.domain.entities.FileEntity;
import webtoon.storage.domain.services.IFileService;
import webtoon.storage.infras.jpa.PageableBean;

@RestController
@RequestMapping("queries")
public class QueryResource {

	@Autowired
	@Lazy
	private PageableBean pageableBean;

	@Autowired
	private IFileService fileService;

	@GetMapping
	public Page<FileDto> filterFile(@RequestParam(required = false) String q) {
		Specification<FileEntity> spec = null;
		if (q != null) {
			final String search = "%" + q + "%";
			spec = Specification.where((root, query, cb) -> cb.or(cb.like(root.get("fileName"), search),
					cb.like(root.get("fileType"), search), cb.like(root.get("title"), search),
					cb.like(root.get("alt"), search)));
		}
		return this.fileService.filterFile(this.pageableBean.getPageable(), spec);
	}
}
