package webtoon.main.domains.post.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_category")
public class CategoryEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "category_name", nullable = false)
	private String categoryName;

	@Column(name = "slug", nullable = false, unique = true)
	private String slug;

}
