package webtoon.domains.manga.entities;

import lombok.*;

import javax.persistence.*;
@Entity
@Table(name = "tbl_manga_rating")
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Builder
public class MangaRatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "manga_id")
    private Long mangaId;

    @Transient
    private String createdBy;

    @Column(name = "rate")
    private Float rate;
}
