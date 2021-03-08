package com.fooqoo56.iine.bot.function.infrastructure.api.repositoryimpl;

import com.fooqoo56.iine.bot.function.domain.repository.api.TwitterRepository;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.request.TweetRequest;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.response.Oauth2Response;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.response.TweetListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class TwitterRepositoryImpl implements TwitterRepository {

    private static final String BEARER_PREFIX = "Bearer ";

    private final WebClient twitterSearchClient;
    private final WebClient bearerTokenTwitterClient;

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public Mono<TweetListResponse> findTweet(final TweetRequest request) {
        return getBearerToken()
                .map(Oauth2Response::getAccessToken)
                .flatMap(
                        accessToken -> twitterSearchClient
                                .get()
                                .uri(uriBuilder -> uriBuilder.queryParams(request.getQueryMap())
                                        .build())
                                .header(HttpHeaders.AUTHORIZATION,
                                        BEARER_PREFIX + accessToken)
                                .retrieve()
                                .bodyToMono(TweetListResponse.class));
    }

    /**
     * トークン取得
     *
     * @return Oauth2Response
     */
    @NonNull
    protected Mono<Oauth2Response> getBearerToken() {

        final MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        return bearerTokenTwitterClient
                .post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Oauth2Response.class);
    }
}
