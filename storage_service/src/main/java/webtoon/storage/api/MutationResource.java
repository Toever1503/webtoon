package webtoon.storage.api;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("mutations")
public class MutationResource {

	public MutationResource() {
		super();
		// TODO Auto-generated constructor stub
		System.out.println("hello world");
	}

	@PostMapping("upload")
	public String uploadFile(@RequestPart MultipartFile file, @RequestParam(required = false) String folder) {
		return "uploaded";
	}

	@PostMapping("bulk-upload")
	public String uploadBulkFile(@RequestPart MultipartFile files, @RequestParam(required = false) String folder) {
		return "uploaded";
	}
	
	
	@DeleteMapping("delete-files")
	public void deleteFile() {
		
	}
}
