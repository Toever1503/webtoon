package webtoon.storage.domain.dtos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileDto {
	
	private String id;
	private String fileName;
	private String type;
	private String url;
	private String alt;
	private String title;
	
	@JsonFormat(pattern="HH:mm:ss")
	private Date createdAt;
	
	@JsonFormat(pattern="HH:mm:ss")
	private Date updatedAt;

}
