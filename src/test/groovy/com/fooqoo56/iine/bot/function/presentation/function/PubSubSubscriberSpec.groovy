package com.fooqoo56.iine.bot.function.presentation.function

import com.fasterxml.jackson.databind.ObjectMapper
import com.fooqoo56.iine.bot.function.application.service.FavoriteService
import com.fooqoo56.iine.bot.function.domain.model.Tweet
import com.fooqoo56.iine.bot.function.exception.NotSuccessMappingException
import com.fooqoo56.iine.bot.function.presentation.function.dto.PubSubMessage
import com.fooqoo56.iine.bot.function.presentation.function.dto.TweetQualification
import reactor.core.publisher.Mono
import spock.lang.Specification
import spock.lang.Unroll

/**
 * PubSubSubscriberのテスト
 */
class PubSubSubscriberSpec extends Specification {

    private FunctionSubscriber sut
    private FavoriteService favoriteService

    final setup() {
        favoriteService = Mock(FavoriteService)

        sut = new FunctionSubscriber(favoriteService, new ObjectMapper())
    }

    final "favoriteTweetFunction"() {
        given:
        // 引数を生成する
        final message = PubSubMessage.builder()
                .data("eyJ1c2VySWQiOiAiamZRRDZJNkpHd3ZkUzVReWpSbWciLCAicXVlcnkiOiAiTmV4dC5qcyIsICJyZXR3ZWV0Q291bnQiOiAwLCAiZmF2b3JpdGVDb3VudCI6IDMsICJmb2xsb3dlcnNDb3VudCI6IDEwLCAiZnJpZW5kc0NvdW50IjogMTB9Cg==")
                .attributes(Map.of("key", "value"))
                .messageId("id")
                .publishTime("publishTime")
                .build()

        favoriteService.favoriteQualifiedTweet(*_) >> Mono.just(Optional.of(Mock(Tweet)))

        when:
        final actual = sut.favoriteTweetFunction(message)

        then:
        actual == Boolean.TRUE
    }

    @Unroll
    final "favoriteTweetFunction - #caseName"() {
        given:
        // 引数を生成する
        final message = Mock(PubSubMessage) {
            getData() >> "eyJxdWVyeSI6ICJOZXh0LmpzIiwgInJldHdlZXRDb3VudCI6IDAsICJmYXZvcml0ZUNvdW50IjogMywgImZvbGxvd2Vyc0NvdW50IjogMTAsICJmcmllbmRzQ291bnQiOiAxMH0K"
        }

        favoriteService.favoriteQualifiedTweet(*_) >> favoriteTweetResponse

        when:
        final actual = sut.favoriteTweetFunction(message)

        then:
        actual == expected

        where:
        caseName           | favoriteTweetResponse       || expected
        "レスポンスが空のOptional" | Mono.just(Optional.empty()) || Boolean.FALSE
        "レスポンスがnull"       | Mono.empty()                || Boolean.FALSE
    }

    final "mapTweetCondition"() {
        given:
        // 引数を作成
        final data = "{\"userId\": \"userId\", \"query\": \"Next.js\", \"retweetCount\": 0, \"favoriteCount\": 3, \"followersCount\": 10, \"friendsCount\": 10}"

        // 期待値を生成
        final expected = TweetQualification.builder()
                .userId("userId")
                .query("Next.js")
                .retweetCount(0)
                .favoriteCount(3)
                .followersCount(10)
                .friendsCount(10)
                .build()
        when:
        final actual = sut.mapTweetCondition(data)

        then:
        actual == expected
    }

    final "mapTweetCondition - 例外"() {
        given:
        // 引数を作成
        final data = "xxxxx"

        when:
        sut.mapTweetCondition(data)

        then:
        thrown(NotSuccessMappingException)
    }

    final "getDecodedMessage"() {
        given:
        // 引数を生成する
        final message = Mock(PubSubMessage) {
            getData() >> "eyJxdWVyeSI6ICJOZXh0LmpzIiwgInJldHdlZXRDb3VudCI6IDAsICJmYXZvcml0ZUNvdW50IjogMywgImZvbGxvd2Vyc0NvdW50IjogMTAsICJmcmllbmRzQ291bnQiOiAxMH0K"
        }

        // 期待値を生成する
        final expected = "{\"query\": \"Next.js\", \"retweetCount\": 0, \"favoriteCount\": 3, \"followersCount\": 10, \"friendsCount\": 10}\n"

        when:
        final actual = sut.getDecodedMessage(message)

        then:
        actual == expected
    }
}
