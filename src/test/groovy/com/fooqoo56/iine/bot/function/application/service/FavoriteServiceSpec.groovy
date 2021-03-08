package com.fooqoo56.iine.bot.function.application.service

import com.fooqoo56.iine.bot.function.application.sharedservice.TwitterSharedService
import com.fooqoo56.iine.bot.function.domain.model.Tweet
import com.fooqoo56.iine.bot.function.domain.model.User
import com.fooqoo56.iine.bot.function.presentation.function.dto.TweetQualification
import reactor.core.publisher.Flux
import spock.lang.Specification

class FavoriteServiceSpec extends Specification {

    private FavoriteService favoriteService
    private TwitterSharedService twitterService = Mock(TwitterSharedService)

    final setup() {
        favoriteService = new FavoriteService(twitterService)
    }

    final "いいねを実行する"() {
        given:
        // mockを作成する
        twitterService.findTweet(*_) >> getMockTweetFlux()

        // 引数を作成する
        final qualification = TweetQualification.builder()
                .query("Next.js")
                .retweetCount(0)
                .favoriteCount(3)
                .followersCount(10)
                .friendsCount(10)
                .build()

        // 期待値を作成する
        final expected = Boolean.FALSE

        when:
        final actual = favoriteService.favoriteTweet(qualification).block()

        then:
        actual == expected
    }

    private final getMockTweetFlux() {
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
}
