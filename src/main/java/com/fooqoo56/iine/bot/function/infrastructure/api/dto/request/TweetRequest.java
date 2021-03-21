package com.fooqoo56.iine.bot.function.infrastructure.api.dto.request;

import com.fooqoo56.iine.bot.function.infrastructure.api.dto.constant.Lang;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.constant.ResultType;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.lang.NonNull;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * ツイート検索APIのリクエストパラメータ
 */
@Getter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
@Builder
public class TweetRequest implements Serializable {

    public static final String DEFAULT_MAX_ID = "-1";
    public static final int MAX_COUNT = 100;
    private static final long serialVersionUID = -9099926804237935939L;
    /**
     * 検索クエリ
     */
    @NonNull
    private final String query;

    /**
     * 言語
     */
    @NonNull
    private final Lang lang;

    /**
     * 検索結果の設定
     */
    @NonNull
    private final ResultType resultType;

    /**
     * 最大数
     */
    @NonNull
    private final Integer count;

    /**
     * entityを含める場合、true
     */
    @NonNull
    private final Boolean includeEntitiesFlag;

    /**
     * 現在時刻
     */
    @NonNull
    private final LocalDate until;

    /**
     * max_idの指定
     */
    @NonNull
    private final String maxId;

    /**
     * クエリをMap型に変換.
     *
     * @return Map
     */
    @NonNull
    public MultiValueMap<String, String> getQueryMap() {
        final MultiValueMap<String, String> queries = new LinkedMultiValueMap<>();

        queries.add("q", getQueryWithFilter());
        queries.add("lang", lang.getCountry());
        queries.add("result_type", resultType.getName());
        queries.add("count", count.toString());
        queries.add("include_entities", includeEntitiesFlag.toString());
        queries.add("until", getFormattedUntil());
        queries.add("max_id", maxId);

        return queries;
    }

    /**
     * 「yyyy-MM-dd」でフォーマットしたuntilの値
     *
     * @return フォーマットされたuntil
     */
    private String getFormattedUntil() {
        return until.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * フィルタ付きのクエリを取得する
     *
     * @return フィルタ付きのクエリを取得する
     */
    private String getQueryWithFilter() {
        return query + " -filter:retweets";
    }
}
