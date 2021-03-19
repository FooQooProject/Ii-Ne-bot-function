package com.fooqoo56.iine.bot.function.domain.model;

import java.io.Serializable;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.lang.NonNull;

/**
 * ツイート
 */
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Tweet implements Serializable {

    private static final long serialVersionUID = -5203574052011105305L;

    /**
     * ツイートID
     */
    @NonNull
    private final String id;

    /**
     * ツイート本文
     */
    @NonNull
    private final String text;

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
     * ユーザ
     */
    @NonNull
    private final User user;

    /**
     * いいね済の場合、true
     */
    private final boolean favorite;

    /**
     * リツイート済の場合、true
     */
    private final boolean retweet;

    /**
     * センシティブ指定の場合、true
     */
    private final boolean sensitive;

    /**
     * 引用ツイートの場合、true
     */
    private final boolean quote;

    /**
     * リプライツイートの場合、true
     */
    private final boolean reply;

    /**
     * いいねが未実施かどうか
     *
     * @return いいねが未実施の場合、trueを返す
     */
    public boolean isNotFavorite() {
        return !favorite;
    }
}
