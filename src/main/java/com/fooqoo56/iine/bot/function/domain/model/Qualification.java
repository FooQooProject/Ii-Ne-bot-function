package com.fooqoo56.iine.bot.function.domain.model;

import java.io.Serializable;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * いいね要件
 */
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@Builder
public class Qualification implements Serializable {

    private static final long serialVersionUID = 3867629517005845693L;

    /**
     * キーワード
     */
    @NonNull
    private final String query;

    /**
     * リツイート数
     */
    @NonNull
    private final Integer minRetweetCount;

    /**
     * いいね数
     */
    @NonNull
    private final Integer minFavoriteCount;

    /**
     * フォロワー数
     */
    @NonNull
    private final Integer minFollowersCount;

    /**
     * フォロー数
     */
    @NonNull
    private final Integer minFriendsCount;

    /**
     * センシティブツイートであるか
     */
    private final boolean sensitive;

    /**
     * 引用ツイートであるか
     */
    private final boolean quote;

    /**
     * リプライツイートであるか
     */
    private final boolean reply;

    /**
     * ユーザのツイート数
     */
    @NonNull
    private final Integer minStatusesCount;

    /**
     * ツイート投稿者をフォローしているか
     */
    private final boolean follow;
}
