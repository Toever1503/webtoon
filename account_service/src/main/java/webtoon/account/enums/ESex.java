package webtoon.account.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ESex {

    Male("Male"),
    Female("Female"),
    Other("Other");

    private final String value;
}
