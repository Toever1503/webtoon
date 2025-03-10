package webtoon.storage.domain.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import webtoon.storage.domain.dtos.FileDto;
import webtoon.storage.domain.entities.FileEntity;
import webtoon.storage.domain.mappers.FileMapper;
import webtoon.storage.domain.repositories.IFileRepositoty;
import webtoon.storage.domain.utils.FileUploadProvider;

@Service
@Transactional
public class FileServiceImpl implements IFileService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final IFileRepositoty fileRepository;

	private final FileUploadProvider fileUploadProvider;
	private final FileMapper fileMapper;

	public FileServiceImpl(IFileRepositoty fileRepository, FileUploadProvider fileUploadProvider,
			FileMapper fileMapper) {
		super();
		this.fileRepository = fileRepository;
		this.fileUploadProvider = fileUploadProvider;
		this.fileMapper = fileMapper;
		logger.info("File service created.");
	}

	@Override
	public FileDto uploadFile(MultipartFile file, String folder) {
		// TODO Auto-generated method stub
		FileEntity entity;
		if (folder != null)
			entity = this.fileUploadProvider.uploadFile(file, folder);
		entity = this.fileUploadProvider.uploadFile(file);
		this.fileRepository.save(entity);
		return fileMapper.toDto(entity);
	}

	@Override
	public List<FileDto> uploadBulkFile(List<MultipartFile> files, String folder) {
		// TODO Auto-generated method stub
		return files.stream().map(f -> this.uploadFile(f, folder)).collect(Collectors.toList());
	}

	@Override
	public void deleteFile(List<Long> ids) {
		// TODO Auto-generated method stub
		List<FileEntity> fileEntities = this.fileRepository.findAllById(ids);
		List<String> fileUrls = fileEntities.stream().map(f -> f.getUrl()).collect(Collectors.toList());
		try {
			this.fileRepository.deleteAll(fileEntities);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		fileUrls.forEach(url -> {
			this.fileUploadProvider.deleteFile(url);
		});
	}

	@Override
	public Page<FileDto> filterFile(Pageable pageable, Specification<FileEntity> specs) {
		// TODO Auto-generated method stub
		return this.fileMapper.toDtoPage(this.fileRepository.findAll(specs, pageable));
	}

}
