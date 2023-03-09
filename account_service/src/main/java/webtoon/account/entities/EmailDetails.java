package webtoon.account.entities;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailDetails {
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;

}
