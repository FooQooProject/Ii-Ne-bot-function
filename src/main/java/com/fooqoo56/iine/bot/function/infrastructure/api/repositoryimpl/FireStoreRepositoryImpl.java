package com.fooqoo56.iine.bot.function.infrastructure.api.repositoryimpl;

import com.fooqoo56.iine.bot.function.domain.repository.api.FireStoreRepository;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.response.UdbResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
@Profile("prod")
public class FireStoreRepositoryImpl implements FireStoreRepository {

    private static final String BEARER_PREFIX = "Bearer ";

    private final WebClient bearerTokenGoogleClient;
    private final WebClient udbClient;

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<UdbResponse> getTwitterUser(final String userId, final String tweetId) {
        return getBearerToken()
                .flatMap(token -> udbClient
                        .get()
                        .uri(uriBuilder -> uriBuilder
                                .path("{id}")
                                .queryParam("tweetId", tweetId)
                                .build(userId))
                        .header(HttpHeaders.AUTHORIZATION,
                                BEARER_PREFIX + token)
                        .retrieve()
                        .bodyToMono(UdbResponse.class));
    }

    /**
     * BearerToken取得
     *
     * @return bearerToken
     */
    protected Mono<String> getBearerToken() {
        return bearerTokenGoogleClient
                .get()
                .retrieve()
                .bodyToMono(String.class);
    }
}
