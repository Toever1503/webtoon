package webtoon.account.dtos;

import lombok.Builder;
import lombok.Getter;
import webtoon.account.entities.RoleEntity;
import webtoon.account.enums.EAccountType;
import webtoon.account.enums.ESex;
import webtoon.account.enums.EStatus;
import webtoon.account.entities.AuthorityEntity;
import webtoon.account.entities.UserEntity;

import java.util.Date;
import java.util.Set;

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
    private EAccountType accountType;
    private Date createdAt;
    private Date modifiedAt;

    private Set<AuthorityEntity> authorities;

    private RoleEntity role;

    public static UserDto toDto(UserEntity entity) {
        if(entity == null) return  null;
        return UserDto.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .sex(entity.getSex())
                .status(entity.getStatus())
                .hasBlocked(entity.getHasBlocked())
                .accountType(entity.getAccountType())
                .createdAt(entity.getCreatedAt())
                .modifiedAt(entity.getModifiedAt())
                .authorities(entity.getAuthorities())
                .role(entity.getRole())
                .build();
    }
}
