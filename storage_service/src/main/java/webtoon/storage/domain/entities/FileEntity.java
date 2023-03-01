package webtoon.storage.domain.entities;

import java.util.Date;
import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "tbl_files")
public class FileEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "file_name", nullable = false, length = 300)
	private String fileName;

	@Column(name = "file_type", nullable = false, length = 20)
	private String fileType;

	@Column(name = "url", nullable = false, length = 255)
	private String url;

	@Column(name = "title", length = 255)
	private String title;

	@Column(name = "alt", length = 255)
	private String alt;

	@Column(name = "created_by", nullable = false)
	private Long createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false)
	@CreationTimestamp
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", nullable = false)
	@CreationTimestamp
	private Date updatedAt;
}
