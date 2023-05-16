package webtoon.main.account.models;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class UpdateUserModel {

    private final Long id;

    private final String username;

    private final String fullName;

    private final String password;

    private final String rePassword;

    private final String email;

    private final MultipartFile image;

    private final String sex;

}
