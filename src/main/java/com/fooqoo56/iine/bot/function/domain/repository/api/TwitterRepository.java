package com.fooqoo56.iine.bot.function.domain.repository.api;

import com.fooqoo56.iine.bot.function.domain.model.Oauth;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.request.TweetRequest;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.response.TweetListResponse;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.response.TweetResponse;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * ツイッターAPI
 */
public interface TwitterRepository {

    /**
     * ツイートの検索
     *
     * @param request TweetRequest
     * @return TwitterFollowerResponse
     */
    Mono<TweetListResponse> findTweet(final TweetRequest request);

    /**
     * ツイートのいいね
     *
     * @param id        ツイートID
     * @param userOauth Twitterユーザ
     * @return いいねAPIのレスポンス
     */
    Mono<TweetResponse> favoriteTweet(final String id, final Oauth userOauth);

    /**
     * ID指定のツイート取得
     *
     * @param ids ツイートID
     * @return 取得APIのレスポンス
     */
    Flux<TweetResponse> lookupTweet(final List<String> ids);
}
