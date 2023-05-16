package webtoon.main.account.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoginResponseDto {
    private String token;
    private long validTimeIn;

    private List<String> auths;
}
