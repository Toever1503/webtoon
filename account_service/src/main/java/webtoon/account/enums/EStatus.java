package webtoon.account.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EStatus {

    NOT_ENABLED("Not Enabled"),
    ENABLED("Enabled"),
    NOT_ACTIVE("Not Active");

    private final String value;
}
