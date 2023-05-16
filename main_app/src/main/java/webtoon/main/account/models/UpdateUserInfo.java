package webtoon.main.account.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.main.account.enums.ESex;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateUserInfo {
    private String password;
    private String phone;
    private ESex sex;

}
