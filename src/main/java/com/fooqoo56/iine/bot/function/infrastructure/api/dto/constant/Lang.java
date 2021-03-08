package com.fooqoo56.iine.bot.function.infrastructure.api.dto.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

@Getter
@RequiredArgsConstructor
public enum Lang {

    JA("ja"),
    EN("en");

    @NonNull
    private final String country;
}
