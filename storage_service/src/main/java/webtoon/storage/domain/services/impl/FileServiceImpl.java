package webtoon.storage.domain.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import webtoon.storage.domain.dtos.FileDto;
import webtoon.storage.domain.entities.FileEntity;
import webtoon.storage.domain.repositories.IFileRepositoty;
import webtoon.storage.domain.services.IFileService;
import webtoon.storage.domain.utils.FileUploadProvider;

@Component
public class FileServiceImpl implements IFileService {

	private final FileUploadProvider uploadProvider;
	private final IFileRepositoty fileRepositoty;

	public FileServiceImpl(FileUploadProvider uploadProvider, IFileRepositoty fileRepositoty) {
		super();
		this.uploadProvider = uploadProvider;
		this.fileRepositoty = fileRepositoty;
	}

	@Override
	public FileDto uploadFile(MultipartFile file, String folder) {
		// TODO Auto-generated method stub

		FileEntity fileEntity = new FileEntity();
		if (folder == null)
			fileEntity.setUrl(this.uploadProvider.uploadFile(file));
		else
			fileEntity.setUrl(this.uploadProvider.uploadFile(file, folder));
		fileEntity.setFileName(file.getOriginalFilename());
		fileEntity.setFileType(file.getContentType());
		this.fileRepositoty.save(file);
		return null;
	}

	@Override
	public List<FileDto> uploadBulkFile(List<MultipartFile> files, String folder) {
		// TODO Auto-generated method stub
		return files.stream().map(file -> uploadFile(file)).collect(Collectors.toList());
	}

}
