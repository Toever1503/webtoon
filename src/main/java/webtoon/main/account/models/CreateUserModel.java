package webtoon.main.account.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webtoon.main.account.enums.ESex;

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

    private ESex sex;

}
