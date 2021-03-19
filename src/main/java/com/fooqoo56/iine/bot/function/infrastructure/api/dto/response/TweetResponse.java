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
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.lang.NonNull;

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

    /**
     * センシティブ指定であるか
     *
     * @return センシティブ指定の場合、trueを返す
     */
    public boolean isSensitive() {
        return BooleanUtils.isTrue(sensitiveFlag);
    }

    /**
     * いいね済であるか
     *
     * @return いいね済の場合、trueを返す
     */
    public boolean isFavorite() {
        return BooleanUtils.isTrue(favoriteFlag);
    }

    /**
     * リツイート済であるか
     *
     * @return リツイート済の場合、trueを返す
     */
    public boolean isRetweet() {
        return BooleanUtils.isTrue(retweetFlag);
    }

    /**
     * 引用ツイートであるか
     *
     * @return リツイート済の場合、trueを返す
     */
    public boolean isQuote() {
        return BooleanUtils.isTrue(quoteFlag);
    }

    /**
     * idの取得(NullCheck実施)
     *
     * @return ツイートID
     */
    @NonNull
    public String getIdWithNullCheck() {
        return Objects.requireNonNull(id, "フィールドがnullです: TweetResponse.id");
    }

    /**
     * textの取得(NullCheck実施)
     *
     * @return ツイート本文
     */
    @NonNull
    public String getTextWithNullCheck() {
        return Objects.requireNonNull(text, "フィールドがnullです: TweetResponse.text");
    }

    /**
     * retweetCountの取得(NullCheck実施)
     *
     * @return リツイート数
     */
    @NonNull
    public Integer getRetweetCountWithNullCheck() {
        return Objects.requireNonNull(retweetCount, "フィールドがnullです: TweetResponse.retweetCount");
    }

    /**
     * favoriteCountの取得(NullCheck実施)
     *
     * @return いいね数
     */
    @NonNull
    public Integer getFavoriteCountWithNullCheck() {
        return Objects.requireNonNull(favoriteCount, "フィールドがnullです: TweetResponse.favoriteCount");
    }

    /**
     * userの取得(NullCheck実施)
     *
     * @return ユーザ
     */
    @NonNull
    public UserResponse getUserWithNullCheck() {
        return Objects.requireNonNull(user, "フィールドがnullです: TweetResponse.user");
    }
}
