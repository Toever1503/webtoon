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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    @CreatedDate
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at")
    @LastModifiedDate
    private Date modifiedAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    @CreatedBy
    private UserEntity createdBy;

    @ManyToOne
    @JoinColumn(name = "modified_by")
    @LastModifiedBy
    private UserEntity modifiedBy;
}
