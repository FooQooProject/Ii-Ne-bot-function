package com.fooqoo56.iine.bot.function.domain.repository.api;

import com.fooqoo56.iine.bot.function.domain.model.TwitterUser;
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
    Mono<TweetListResponse> findTweet(final TweetRequest request, final TwitterUser twitterUser);

    /**
     * ツイートのいいね
     *
     * @param id ツイートID
     * @return いいねAPIのレスポンス
     */
    Mono<TweetResponse> favoriteTweet(final String id, final TwitterUser twitterUser);

    /**
     * ID指定のツイート取得
     *
     * @param ids ツイートID
     * @return 取得APIのレスポンス
     */
    Flux<TweetResponse> lookupTweet(final List<String> ids, final TwitterUser twitterUser);
}
