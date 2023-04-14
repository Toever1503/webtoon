package webtoon.domains.post.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.domains.post.input.EPostStatus;
import webtoon.domains.tag.entity.TagEntityRelation;

import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "tbl_post")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "excerpt", nullable = false, unique = true)
    private String excerpt;

    @Column(name = "content", columnDefinition = "LONGTEXT", nullable = false)
    private String content;

    @Column(name = "post_name", length = 300, nullable = false)
    private String postName;

    @Column(name = "featured_image", length = 500, nullable = false)
    private String featuredImage;

    @Column(name = "comment_count", nullable = false)
    private Integer commentCount;

    @Column(name = "view_count", nullable = false)
    private Integer viewCount;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EPostStatus status;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(nullable = false,name = "created_date")
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(nullable = false,name = "modified_date")
    private Date modifiedDate;

    @ManyToOne
    private CategoryEntity category;

//    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
//    @JoinColumn(name = "object_id")
//    @Where(clause = "tag_type =  '" + TagRelationTypeConstant.POST + "'")
    @Transient
    private List<TagEntityRelation> tagRelations;

    @Override
    public String toString() {
        return "PostsEntity{} to string";
    }

}
