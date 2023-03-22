package webtoon.account.models;

import lombok.*;
import webtoon.account.enums.EnumSex;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserModel {
    private String username;

    private String fullName;

    private String email;

    private String password;

    private String rePassword;

    private EnumSex sex;

}
