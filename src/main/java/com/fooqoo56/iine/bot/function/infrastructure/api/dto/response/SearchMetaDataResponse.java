package com.fooqoo56.iine.bot.function.infrastructure.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ツイッター検索APIのレスポンスに含まれるメタデータ
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SearchMetaDataResponse implements Serializable {

    private static final long serialVersionUID = 47033691799811374L;

    /**
     * 次のカーソルを取得するクエリパラメータ
     */
    @JsonProperty("next_results")
    private String nextResults;
}