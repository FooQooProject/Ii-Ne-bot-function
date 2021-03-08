package com.fooqoo56.iine.bot.function.domain.model;

import java.io.Serializable;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * ツイート
 */
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@Builder
public class Tweet implements Serializable {

    private static final long serialVersionUID = -5203574052011105305L;

    @NonNull
    private final String id;

    @NonNull
    private final String text;

    @NonNull
    private final Integer retweetCount;

    @NonNull
    private final Integer favoriteCount;

    @NonNull
    private final User user;

    private final boolean favorite;

    private final boolean retweet;

    private final boolean sensitive;

    private final boolean quote;

    private final boolean reply;
}
