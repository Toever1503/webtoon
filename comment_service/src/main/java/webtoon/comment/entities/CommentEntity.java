package webtoon.comment.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import webtoon.comment.enums.ECommentType;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tbl_comment")
@Getter
@Setter
@Builder
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "comment_type")
    @Enumerated(EnumType.STRING)
    private ECommentType commentType;

    @Column(name = "object_id")
    private Long objectId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    @CreatedDate
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at")
    @LastModifiedDate
    private Date modifiedAt;

    @JoinColumn(name = "created_by")
    @CreatedBy
    private Long createdBy;

    @JoinColumn(name = "modified_by")
    @LastModifiedBy
    private Long modifiedBy;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private CommentEntity parentComment;

    @OneToMany
    private List<CommentEntity> childComments;
}
