package webtoon.storage.domain.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import webtoon.storage.domain.entities.FileEntity;
import webtoon.storage.domain.dtos.FileDto;

@Component
public class FileMapper {

	public FileDto toDto(FileEntity fileEntity) {
		return FileDto.builder().id(fileEntity.getId()).fileName(fileEntity.getFileName()).fileType(fileEntity.getFileType())
				.url(fileEntity.getUrl()).title(fileEntity.getTitle()).alt(fileEntity.getAlt())
				.createdAt(fileEntity.getCreatedAt()).updatedAt(fileEntity.getUpdatedAt())
//				.createdBy(null)
				.build();
	}

	public List<FileDto> toDtoList(List<FileEntity> fileEntities) {
		return fileEntities.stream().map(this::toDto).collect(Collectors.toList());
	}

	public Page<FileDto> toDtoPage(Page<FileEntity> filePage) {
		return filePage.map(this::toDto);
	}
}
