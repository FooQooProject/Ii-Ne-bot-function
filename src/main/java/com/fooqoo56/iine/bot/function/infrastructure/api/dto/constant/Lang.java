package com.fooqoo56.iine.bot.function.infrastructure.api.dto.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * 言語指定クエリ
 */
@Getter
@RequiredArgsConstructor
public enum Lang {

    JA("ja"),
    EN("en");

    /**
     * クエリパラメータ値
     */
    @NonNull
    private final String country;
}
