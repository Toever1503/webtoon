package webtoon.domains.tag.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_tag_relation", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "object_id", "tag_id", "tag_type" }) })
public class TagEntityRelation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "object_id", nullable = false)
	private Long objectID;

	@ManyToOne
	@JoinColumn(name = "tag_id", nullable = false)
	private TagEntity tag;

	@Column(name = "tag_type", nullable = false)
	private String tagType;

}