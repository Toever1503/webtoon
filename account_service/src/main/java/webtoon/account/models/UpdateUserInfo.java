package webtoon.account.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import webtoon.account.enums.ESex;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateUserInfo {
    private String password;
    private String phone;
    private ESex sex;

}
