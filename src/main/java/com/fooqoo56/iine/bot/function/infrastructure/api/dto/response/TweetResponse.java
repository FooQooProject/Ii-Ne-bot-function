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
import lombok.ToString;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * ツイッター検索APIのレスポンスに含まれるツイートデータ
 */
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class TweetResponse implements Serializable {

    private static final long serialVersionUID = 944487153568367654L;

    @JsonProperty("id_str")
    @NonNull
    private String id;

    @JsonProperty("text")
    @NonNull
    private String text;

    @JsonProperty("user")
    @NonNull
    private UserResponse user;

    @JsonProperty("retweet_count")
    @NonNull
    private Integer retweetCount;

    @JsonProperty("favorite_count")
    @NonNull
    private Integer favoriteCount;

    @JsonProperty("favorited")
    @NonNull
    private Boolean favoriteFlag;

    @JsonProperty("retweeted")
    @NonNull
    private Boolean retweetFlag;

    @JsonProperty("possibly_sensitive")
    @NonNull
    private Boolean sensitiveFlag;

    @JsonProperty("is_quote_status")
    @NonNull
    private Boolean quoteFlag;

    @JsonProperty("in_reply_to_status_id_str")
    @Nullable
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
