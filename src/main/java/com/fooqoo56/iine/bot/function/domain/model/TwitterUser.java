package com.fooqoo56.iine.bot.function.domain.model;

import java.io.Serializable;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.lang.NonNull;

@Getter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
@Builder
public class TwitterUser implements Serializable {

    private static final long serialVersionUID = 6298021157481576949L;

    @NonNull
    private final String apiKey;

    @NonNull
    private final String apiSecret;

    @NonNull
    private final String accessToken;

    @NonNull
    private final String accessTokenSecret;
}
