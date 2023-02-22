package webtoon.storage.api.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import webtoon.storage.domain.repositories.IFileRepositoty;
import webtoon.storage.domain.utils.FileUploadProvider;
import webtoon.storage.infras.jpa.PageableBean;

@RestController
@RequestMapping("mutations")
public class MutationResource {

	@Autowired
	@Lazy
	private PageableBean pageableBean;

	@Autowired
	private FileUploadProvider fileUploadProvider;

	public MutationResource() {
		super();
		// TODO Auto-generated constructor stub
		System.out.println("hello world + ");
	}

	@GetMapping
	public Object test() {
		return pageableBean.getPageable().toString();
	}

	@PostMapping("upload")
	public String uploadFile(@RequestPart MultipartFile file, @RequestParam(required = false) String folder) {
		return fileUploadProvider.uploadFile(file);
	}

	@PostMapping("bulk-upload")
	public String uploadBulkFile(@RequestPart MultipartFile files, @RequestParam(required = false) String folder) {
		return "uploaded";
	}

	@DeleteMapping("delete-files")
	public void deleteFile() {

	}
}
