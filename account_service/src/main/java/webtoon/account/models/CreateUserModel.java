package webtoon.account.models;

import lombok.Builder;
import lombok.Getter;
import webtoon.account.enums.EnumSex;

@Getter
@Builder
public class CreateUserModel {
    private final String username;

    private final String fullName;

    private final String password;

    private final String rePassword;

    private final String email;

    private final EnumSex sex;

}
