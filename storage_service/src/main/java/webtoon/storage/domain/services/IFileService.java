package webtoon.storage.domain.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import webtoon.storage.domain.dtos.FileDto;

public interface IFileService {
	FileDto uploadFile(MultipartFile file, String folder);

	List<FileDto> uploadBulkFile(List<MultipartFile> files, String folder);
}
