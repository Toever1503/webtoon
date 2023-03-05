package webtoon.account.models;

import lombok.Builder;
import lombok.Getter;
import webtoon.account.enums.EnumSex;

@Getter
@Builder
public class UserModel {
    private final String username;
    private final String fullName;
    private final String email;
    private final EnumSex sex;
}
