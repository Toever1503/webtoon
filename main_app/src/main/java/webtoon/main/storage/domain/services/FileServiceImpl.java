package webtoon.main.storage.domain.services;

import java.io.IOException;
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

import webtoon.main.storage.domain.repositories.IFileRepositoty;
import webtoon.main.utils.ASCIIConverter;
import webtoon.main.storage.domain.dtos.FileDto;
import webtoon.main.storage.domain.entities.FileEntity;
import webtoon.main.storage.domain.mappers.FileMapper;
import webtoon.main.storage.domain.utils.FileUploadProvider;

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
		else entity = this.fileUploadProvider.uploadFile(file);
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

	@Override
	public List<FileDto> uploadImageByZipFile(List<MultipartFile> files, String folder) throws IOException {
		// TODO Auto-generated method stub

		List<FileEntity> fileEntities = files.stream().map(f -> {
			System.out.println("zipEntry.getName(): " + f.getName());
			String fName = ASCIIConverter.removeAccent(f.getOriginalFilename());
			FileEntity fileEntity = null;
			try {
				fileEntity = this.fileUploadProvider.uploadFile(f.getBytes(), folder, fName);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			fileEntity.setFileName(f.getOriginalFilename());
			fileEntity.setAlt(ASCIIConverter.removeAccent(f.getOriginalFilename()));
			fileEntity.setTitle(ASCIIConverter.removeAccent(f.getOriginalFilename()));
			fileEntity.setFileType(f.getContentType());
			return fileEntity;
		}).collect(Collectors.toList());

		this.fileRepository.saveAll(fileEntities);
		return this.fileMapper.toDtoList(fileEntities);
	}

}
