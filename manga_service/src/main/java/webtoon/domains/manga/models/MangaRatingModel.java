package webtoon.domains.manga.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.account.entities.UserEntity;
import webtoon.domains.manga.entities.MangaEntity;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MangaRatingModel {
    private Long id;

    private Long mangaEntity;

    private UserEntity createdBy;

    private String  rate;

}
