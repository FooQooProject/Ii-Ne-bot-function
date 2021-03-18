package com.fooqoo56.iine.bot.function.infrastructure.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ツイッター検索APIのレスポンスに含まれるツイートデータ
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class TweetResponse implements Serializable {

    private static final long serialVersionUID = 944487153568367654L;

    /**
     * ツイートID
     */
    @JsonProperty("id_str")
    private String id;

    /**
     * ツイート本文
     */
    @JsonProperty("text")
    private String text;

    /**
     * ユーザ
     */
    @JsonProperty("user")
    private UserResponse user;

    /**
     * リツイート数
     */
    @JsonProperty("retweet_count")
    private Integer retweetCount;

    /**
     * いいね数
     */
    @JsonProperty("favorite_count")
    private Integer favoriteCount;

    /**
     * いいね済みの場合、true
     */
    @JsonProperty("favorited")
    private Boolean favoriteFlag;

    /**
     * リツイート済みの場合、true
     */
    @JsonProperty("retweeted")
    private Boolean retweetFlag;

    /**
     * センシティブ指定の場合、true
     */
    @JsonProperty("possibly_sensitive")
    private Boolean sensitiveFlag;

    /**
     * 引用ツイートの場合、true
     */
    @JsonProperty("is_quote_status")
    private Boolean quoteFlag;

    /**
     * リプライツイートの場合、true
     */
    @JsonProperty("in_reply_to_status_id_str")
    private String inReplyToStatusId;

    /**
     * リプライであるか
     *
     * @return リプライの場合、trueを返す
     */
    public boolean isReply() {
        return Objects.nonNull(inReplyToStatusId);
    }
}
