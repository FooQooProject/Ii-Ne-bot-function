package com.fooqoo56.iine.bot.function.presentation.function.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.NonNull;

/**
 * ツイート条件クラス
 */
@Getter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class TweetQualification implements Serializable {

    public static final String PARAM_QUERY = "query";
    public static final String PARAM_RETWEET_COUNT = "retweetCount";
    public static final String PARAM_FAVORITE_COUNT = "favoriteCount";
    public static final String PARAM_FOLLOWERS_COUNT = "followersCount";
    public static final String PARAM_FRIENDS_COUNT = "friendsCount";
    private static final long serialVersionUID = 756620699363145836L;
    /**
     * キーワード
     */
    @NonNull
    private final String query;

    /**
     * リツイート数
     */
    @NonNull
    private final Integer retweetCount;

    /**
     * いいね数
     */
    @NonNull
    private final Integer favoriteCount;

    /**
     * フォロワー数
     */
    @NonNull
    private final Integer followersCount;

    /**
     * フォロー数
     */
    @NonNull
    private final Integer friendsCount;

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
    public TweetQualification(
            @NonNull @JsonProperty(PARAM_QUERY) final String query,
            @NonNull @JsonProperty(PARAM_RETWEET_COUNT) final Integer retweetCount,
            @NonNull @JsonProperty(PARAM_FAVORITE_COUNT) final Integer favoriteCount,
            @NonNull @JsonProperty(PARAM_FOLLOWERS_COUNT) final Integer followersCount,
            @NonNull @JsonProperty(PARAM_FRIENDS_COUNT) final Integer friendsCount
    ) {
        this.query = query;
        this.retweetCount = retweetCount;
        this.favoriteCount = favoriteCount;
        this.followersCount = followersCount;
        this.friendsCount = friendsCount;
    }
}
