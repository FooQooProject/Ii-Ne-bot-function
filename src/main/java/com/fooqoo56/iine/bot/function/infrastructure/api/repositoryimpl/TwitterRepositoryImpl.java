package com.fooqoo56.iine.bot.function.infrastructure.api.repositoryimpl;

import com.fooqoo56.iine.bot.function.domain.repository.api.TwitterRepository;
import com.fooqoo56.iine.bot.function.exception.NotSuccessGetOauthHmacSignerException;
import com.fooqoo56.iine.bot.function.infrastructure.api.config.ApiSetting;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.request.TweetRequest;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.response.Oauth2Response;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.response.TweetListResponse;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.response.TweetResponse;
import com.fooqoo56.iine.bot.function.infrastructure.api.util.OauthAuthorizationHeaderBuilder;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * ツイッターAPIのRepository実装クラス
 */
@Repository
@RequiredArgsConstructor
public class TwitterRepositoryImpl implements TwitterRepository {

    private static final String BEARER_PREFIX = "Bearer ";

    private final ApiSetting twitterFavoriteApiSetting;

    private final WebClient twitterSearchClient;
    private final WebClient twitterFavoriteClient;
    private final WebClient twitterLookupClient;
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
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public Mono<TweetResponse> favoriteTweet(final String id)
            throws NotSuccessGetOauthHmacSignerException {
        return twitterFavoriteClient
                .post()
                .uri(uriBuilder -> uriBuilder.queryParam("id", id).build())
                .header(HttpHeaders.AUTHORIZATION, getOauth2Header(id))
                .retrieve()
                .bodyToMono(TweetResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public Flux<TweetResponse> lookupTweet(final List<String> ids) {
        return getBearerToken()
                .map(Oauth2Response::getAccessToken)
                .flatMapMany(
                        accessToken -> twitterLookupClient
                                .get()
                                .uri(uriBuilder -> uriBuilder
                                        .queryParam("id", String.join(",", ids))
                                        .build())
                                .header(HttpHeaders.AUTHORIZATION,
                                        BEARER_PREFIX + accessToken)
                                .retrieve()
                                .bodyToFlux(TweetResponse.class));
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

    /**
     * Oauth2.0ヘッダ取得
     *
     * @param id ツイートID
     * @return Oauth2ヘッダ
     */
    @NonNull
    private String getOauth2Header(final String id) throws NotSuccessGetOauthHmacSignerException {
        return OauthAuthorizationHeaderBuilder
                .builder()
                .method(HttpMethod.POST.name().toUpperCase())
                .url(twitterFavoriteApiSetting.getBaseUrl())
                .consumerSecret(twitterFavoriteApiSetting.getApiSecret())
                .tokenSecret(twitterFavoriteApiSetting.getAccessTokenSecret())
                .accessToken(twitterFavoriteApiSetting.getAccessToken())
                .consumerKey(twitterFavoriteApiSetting.getApikey())
                .queryParameters(Map.of("id", id))
                .build().getOauthHeader();
    }
}
