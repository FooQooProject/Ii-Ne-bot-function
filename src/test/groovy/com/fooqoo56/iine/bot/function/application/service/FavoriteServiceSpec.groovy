package com.fooqoo56.iine.bot.function.application.service

import com.fooqoo56.iine.bot.function.application.sharedservice.TwitterSharedService
import com.fooqoo56.iine.bot.function.domain.model.Tweet
import com.fooqoo56.iine.bot.function.domain.model.User
import com.fooqoo56.iine.bot.function.presentation.function.dto.TweetQualification
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import spock.lang.Specification

/**
 * FavoriteServiceのテスト
 */
class FavoriteServiceSpec extends Specification {

    private FavoriteService sut
    private TwitterSharedService twitterSharedService

    final setup() {
        twitterSharedService = Mock(TwitterSharedService)
        sut = new FavoriteService(twitterSharedService)
    }

    final "いいねを実行する"() {
        given:
        // mockを作成する
        twitterSharedService.findTweet(*_) >> getMockTweetFlux()
        twitterSharedService.lookUpTweet(*_) >> Flux.fromIterable(getMockTweet())
        twitterSharedService.favoriteTweet(*_) >> Mono.just(Optional.of(Mock(Tweet)))

        // 処理が複雑なprivateメソッドのみmockを作成する

        // 引数を作成する
        final qualification = TweetQualification.builder()
                .query("http")
                .retweetCount(0)
                .favoriteCount(0)
                .followersCount(0)
                .friendsCount(0)
                .build()

        // 期待値を作成する
        final expected = Boolean.TRUE

        when:
        final actual = sut.favoriteQualifiedTweet(qualification).block()

        then:
        actual == expected
    }

    /**
     * ツイートのFluxのモックを取得する
     *
     * @return ツイートのFlux
     */
    private static final getMockTweetFlux() {
        return Flux.just(
                Tweet.builder()
                        .id("967824267948773377")
                        .text("From pilot to astronaut, Robert H. Lawrence was the first African-American to be selected as an astronaut by any na… https://t.co/FjPEWnh804")
                        .user(User.builder()
                                .id("11348282")
                                .followersCount(28605561)
                                .friendsCount(270)
                                .listedCount(90405)
                                .favouritesCount(2960)
                                .statusesCount(50713)
                                .follow(false)
                                .defaultProfile(false)
                                .defaultProfileImage(false)
                                .build())
                        .retweetCount(988)
                        .favoriteCount(3875)
                        .retweet(false)
                        .sensitive(false)
                        .quote(false)
                        .reply(false)
                        .favorite(false)
                        .build(),
                Tweet.builder()
                        .id("967844427480911872")
                        .text("A magnetic power struggle of galactic proportions - new research highlights the role of the Sun's magnetic landscap… https://t.co/29dZgga54m")
                        .user(User.builder()
                                .id("11348282")
                                .followersCount(28605561)
                                .friendsCount(270)
                                .listedCount(90405)
                                .favouritesCount(2960)
                                .statusesCount(50713)
                                .follow(false)
                                .defaultProfile(false)
                                .defaultProfileImage(false)
                                .build())
                        .retweetCount(2654)
                        .favoriteCount(7962)
                        .retweet(false)
                        .sensitive(false)
                        .quote(false)
                        .reply(false)
                        .favorite(false)
                        .build()
        )
    }

    private final getMockTweet() {
        return [
                Mock(Tweet) {
                    isNotFavorite() >> true
                    getId() >> "id"

                },
                Mock(Tweet) {
                    isNotFavorite() >> true
                    getId() >> "id"

                }
        ]
    }
}
