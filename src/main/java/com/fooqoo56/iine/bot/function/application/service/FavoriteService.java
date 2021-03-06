package com.fooqoo56.iine.bot.function.application.service;

import com.fooqoo56.iine.bot.function.presentation.function.dto.TweetCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * いいえを実施するサービスクラス
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FavoriteService {

    /**
     * ツイート実行
     *
     * @param tweetCondition ツイート条件
     * @return ツイートのいいねに成功した場合、trueを返す
     */
    public Mono<Boolean> favoriteTweet(final TweetCondition tweetCondition) {
        return Mono.just(Boolean.FALSE);
    }
}
