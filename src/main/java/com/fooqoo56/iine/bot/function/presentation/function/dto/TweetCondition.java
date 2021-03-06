package com.fooqoo56.iine.bot.function.presentation.function.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

/**
 * ツイート条件クラス
 */
@Getter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class TweetCondition implements Serializable {

    private static final long serialVersionUID = 756620699363145836L;

    public static final String PARAM_QUERY = "query";
    public static final String PARAM_RETWEET_COUNT = "retweetCount";
    public static final String PARAM_FAVORITE_COUNT = "favoriteCount";
    public static final String PARAM_FOLLOWERS_COUNT = "followersCount";
    public static final String PARAM_FRIENDS_COUNT = "friendsCount";

    /**
     * キーワード
     */
    @NonNull
    private final String query;

    /**
     * リツイート数
     */
    @NonNull
    private final Long retweetCount;

    /**
     * いいね数
     */
    @NonNull
    private final Long favoriteCount;

    /**
     * フォロワー数
     */
    @NonNull
    private final Long followersCount;

    /**
     * フォロー数
     */
    @NonNull
    private final Long friendsCount;

    /**
     * Json生成
     *
     * @param query          キーワード
     * @param retweetCount   リツイート数
     * @param favoriteCount  いいね数
     * @param followersCount フォロワー数
     * @param friendsCount   フォロー数
     */
    @JsonCreator
    public TweetCondition(
            @NonNull @JsonProperty(PARAM_QUERY) final String query,
            @NonNull @JsonProperty(PARAM_RETWEET_COUNT) final Long retweetCount,
            @NonNull @JsonProperty(PARAM_FAVORITE_COUNT) final Long favoriteCount,
            @NonNull @JsonProperty(PARAM_FOLLOWERS_COUNT) final Long followersCount,
            @NonNull @JsonProperty(PARAM_FRIENDS_COUNT) final Long friendsCount
    ) {
        this.query = StringUtils.defaultString(query);
        this.retweetCount = retweetCount;
        this.favoriteCount = favoriteCount;
        this.followersCount = followersCount;
        this.friendsCount = friendsCount;
    }
}
