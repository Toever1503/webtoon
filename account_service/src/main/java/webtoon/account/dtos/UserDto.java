package webtoon.account.dtos;

import lombok.Builder;
import lombok.Getter;
import webtoon.account.entities.AuthorityEntity;
import webtoon.account.entities.UserEntity;
import webtoon.account.enums.EAccountType;
import webtoon.account.enums.ESex;
import webtoon.account.enums.EStatus;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private ESex sex;
    private EStatus status;
    private Boolean hasBlocked;
    private Integer numberOfFailedSignIn;
    private EAccountType accountType;
    private Date createdAt;
    private Date modifiedAt;

    private Set<AuthorityEntity> authorities;

    public static UserDto toDto(UserEntity entity) {
        return UserDto.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .sex(entity.getSex())
                .status(entity.getStatus())
                .hasBlocked(entity.getHasBlocked())
                .numberOfFailedSignIn(entity.getNumberOfFailedSignIn())
                .accountType(entity.getAccountType())
                .createdAt(entity.getCreatedAt())
                .modifiedAt(entity.getModifiedAt())
                .authorities(entity.getAuthorities())
                .build();
    }
}
