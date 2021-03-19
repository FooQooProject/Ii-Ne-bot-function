package com.fooqoo56.iine.bot.function.infrastructure.api.dto.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.lang.NonNull;

/**
 * 検索結果の設定クエリ
 */
@RequiredArgsConstructor
@ToString
@Getter
public enum ResultType {

    MIXED("mixed"),
    RECENT("recent"),
    POPULAR("popular");

    /**
     * クエリパラメータの値
     */
    @NonNull
    private final String name;
}
