package com.fooqoo56.iine.bot.function.domain.model;

import java.io.Serializable;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * ユーザ
 */
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@Builder
public class User implements Serializable {

    private static final long serialVersionUID = 6732265885878037807L;

    /**
     * ユーザID
     */
    @NonNull
    private final String id;

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
     * リストに登録された数
     */
    @NonNull
    private final Integer listedCount;

    /**
     * いいねしている数
     */
    @NonNull
    private final Integer favouritesCount;

    /**
     * ツイートしてる数
     */
    @NonNull
    private final Integer statusesCount;

    /**
     * フォロー済の場合、true
     */
    private final boolean follow;

    /**
     * デフォルトのプロフィールのままの場合、true
     */
    private final boolean defaultProfile;

    /**
     * デフォルトのプロフィール画像のままの場合、true
     */
    private final boolean defaultProfileImage;
}
