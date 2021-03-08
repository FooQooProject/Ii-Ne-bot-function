package com.fooqoo56.iine.bot.function.infrastructure.api.dto.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

@RequiredArgsConstructor
@Getter
public enum ResultType {

    MIXED("mixed"),
    RECENT("recent"),
    POPULAR("popular");

    @NonNull
    private final String name;
}
