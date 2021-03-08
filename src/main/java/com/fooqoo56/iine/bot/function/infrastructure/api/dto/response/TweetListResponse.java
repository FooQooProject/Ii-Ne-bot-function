package com.fooqoo56.iine.bot.function.infrastructure.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.NonNull;

/**
 * ツイッター検索APIのレスポンス
 */
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class TweetListResponse implements Serializable {

    private static final long serialVersionUID = 2367379756102455611L;

    private static final String DEFAULT_NEXT_MAX_ID = "-1";

    /**
     * ツイートのリスト
     */
    @JsonProperty("statuses")
    @NonNull
    private List<TweetResponse> statuses;

    /**
     * ツイッター検索APIのレスポンスに含まれるメタデータ
     */
    @JsonProperty("search_metadata")
    @NonNull
    private SearchMetaDataResponse searchMetaDataResponse;
}
