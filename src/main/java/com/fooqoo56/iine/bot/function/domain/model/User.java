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

    @NonNull
    private final String id;

    @NonNull
    private final Integer followersCount;

    @NonNull
    private final Integer friendsCount;

    @NonNull
    private final Integer listedCount;

    @NonNull
    private final Integer favouritesCount;

    @NonNull
    private final Integer statusesCount;

    private final boolean follow;

    private final boolean defaultProfile;

    private final boolean defaultProfileImage;
}
