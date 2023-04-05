package webtoon.account.dtos;

import lombok.Builder;
import lombok.Getter;
import webtoon.account.entities.UserEntity;
import webtoon.account.enums.ESex;
import webtoon.account.enums.EStatus;

import java.util.Date;

@Getter
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private ESex sex;
    private EStatus status;
    private Boolean hasBlocked;
    private Integer numberOfFailedSignIn;
    private Date createdAt;
    private Date modifiedAt;

    public static UserDto toDto(UserEntity entity) {
        return UserDto.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .sex(entity.getSex())
                .status(entity.getStatus())
                .hasBlocked(entity.getHasBlocked())
                .numberOfFailedSignIn(entity.getNumberOfFailedSignIn())
                .createdAt(entity.getCreatedAt())
                .modifiedAt(entity.getModifiedAt())
                .build();
    }
}
