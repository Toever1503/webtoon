package webtoon.domains.manga.enums;

import javax.persistence.Column;

public enum EMangaStatus {
	PUBLISHED("published"),DRAFT("draft");
	@Column(name = "value")
	 private String value;

	private EMangaStatus(String value) {
		this.value = value;
	}
	
}
