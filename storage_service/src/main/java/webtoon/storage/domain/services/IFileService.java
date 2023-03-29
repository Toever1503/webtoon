package webtoon.storage.domain.services;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import webtoon.storage.domain.dtos.FileDto;
import webtoon.storage.domain.entities.FileEntity;

public interface IFileService {
	FileDto uploadFile(MultipartFile file, String folder);
	
	List<FileDto> uploadBulkFile(List<MultipartFile> files, String folder);
	
	Page<FileDto> filterFile(Pageable pageable, Specification<FileEntity> specs);
	
	void deleteFile(List<Long> ids);


	List<FileDto> uploadImageByZipFile(List<MultipartFile> files, String folder)  throws IOException;
}
