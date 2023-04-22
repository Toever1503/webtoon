package webtoon.account.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.account.entities.UserEntity;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserMetadataDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phone;

    public static UserMetadataDto toDto(UserEntity entity) {
        if (entity == null) return null;
        return UserMetadataDto.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .build();
    }
}
