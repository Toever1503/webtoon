package webtoon.account.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ChangePasswordModel {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
