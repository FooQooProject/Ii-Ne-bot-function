package com.fooqoo56.iine.bot.function.infrastructure.api.dto.request;

import com.fooqoo56.iine.bot.function.domain.model.Qualification;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.constant.Lang;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.constant.ResultType;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * ツイート検索APIのリクエストパラメータ
 */
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@Builder
public class TweetRequest implements Serializable {

    private static final long serialVersionUID = -9099926804237935939L;

    private static final String DEFAULT_MAX_ID = "-1";

    @NonNull
    private final String query;

    @NonNull
    private final Lang lang = Lang.JA;

    @NonNull
    private final ResultType resultType = ResultType.RECENT;

    @NonNull
    private final Integer count = 100;

    @NonNull
    private final Boolean includeEntitiesFlag = false;

    @NonNull
    private final LocalDate until = LocalDate.now();

    @NonNull
    private final String maxId;

    /**
     * payloadをAPIクエリへ変換.
     *
     * @param qualification PayLoad
     * @return APIクエリ
     */
    @NonNull
    public static TweetRequest buildTweetRequest(final Qualification qualification) {
        return TweetRequest
                .builder()
                .query(addFilterRetweet(qualification.getQuery()))
                .maxId(DEFAULT_MAX_ID)
                .build();
    }

    /**
     * payloadをAPIクエリへ変換.
     *
     * @param qualification PayLoad
     * @param nextMaxId     nextMaxId
     * @return APIクエリ
     */
    @NonNull
    public static TweetRequest buildTweetRequest(final Qualification qualification,
                                                 final String nextMaxId) {
        return TweetRequest
                .builder()
                .query(addFilterRetweet(qualification.getQuery()))
                .maxId(nextMaxId)
                .build();
    }

    /**
     * クエリにリツイートを除くフィルタを追加する.
     *
     * @param query クエリ
     * @return フィルタ付きクエリ
     */
    @NonNull
    private static String addFilterRetweet(final String query) {
        return query + " -filter:retweets";
    }

    /**
     * クエリをMap型に変換.
     *
     * @return Map
     */
    @NonNull
    public MultiValueMap<String, String> getQueryMap() {
        final MultiValueMap<String, String> queries = new LinkedMultiValueMap<>();

        queries.add("q", query);
        queries.add("lang", lang.getCountry());
        queries.add("result_type", resultType.getName());
        queries.add("count", count.toString());
        queries.add("include_entities", includeEntitiesFlag.toString());
        queries.add("until", until.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        queries.add("max_id", maxId);

        return queries;
    }
}
