package webtoon.account.dtos;

import lombok.Builder;
import lombok.Getter;
import webtoon.account.entities.UserEntity;
import webtoon.account.enums.ESex;

import java.util.Date;

@Getter
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private ESex sex;
    private Boolean status;
    private Boolean hasBlocked;
    private Integer numberOfFailedSignIn;
    private Date createdAt;
    private Date modifiedAt;

    public static UserDto toDto (UserEntity entity){
        return null;
    }
}
