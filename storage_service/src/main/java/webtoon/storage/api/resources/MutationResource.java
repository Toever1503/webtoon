package webtoon.storage.api.resources;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import webtoon.storage.domain.dtos.FileDto;
import webtoon.storage.domain.services.IFileService;

@RestController
@RequestMapping("mutations")
public class MutationResource {

	
	@Autowired
	private IFileService fileService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public MutationResource() {
		super();
		// TODO Auto-generated constructor stub
		logger.info("MutationResource created.");
	}

	@PostMapping("upload")
	public FileDto uploadFile(@RequestPart MultipartFile file,
			@RequestParam(required = false) String folder) {
		return fileService.uploadFile(file, folder);
	}

	@PostMapping("bulk-upload")
	public List<FileDto> uploadBulkFile(@RequestPart List<MultipartFile> files,
			@RequestParam(required = false) String folder) {
		return fileService.uploadBulkFile(files, folder);
	}

	@DeleteMapping("delete-files")
	public void deleteFile(@RequestParam List<Long> ids) {
		this.fileService.deleteFile(ids);
	}
}
