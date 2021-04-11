package com.fooqoo56.iine.bot.function.domain.repository.api;

import com.fooqoo56.iine.bot.function.infrastructure.api.dto.response.UdbResponse;
import reactor.core.publisher.Mono;

/**
 * FireStoreに接続するAPI
 */
public interface FireStoreRepository {

    /**
     * ツイッターユーザを取得する
     *
     * @param userId  ユーザID
     * @param tweetId ツイートID
     * @return UdbResponse
     */
    Mono<UdbResponse> getTwitterUser(final String userId, final String tweetId);
}
