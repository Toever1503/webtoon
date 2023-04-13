package webtoon.account.inputs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.account.enums.ESex;
import webtoon.account.entities.UserEntity;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInput {
    private Long id;
    private String fullName;
    private String username;
    private String email;
    private String password;
    private String phone;
    private ESex sex;
    private List<Long> authorities;

    public static UserEntity toEntity(UserInput input){
        return UserEntity.builder()
                .id(input.getId())
                .fullName(input.getFullName())
                .username(input.getUsername())
                .email(input.getEmail())
                .sex(input.getSex())
                .phone(input.getPhone())
                .build();
    }
}
