package com.fooqoo56.iine.bot.function.domain.model

import spock.lang.Specification
import spock.lang.Unroll

class TweetSpec extends Specification {

    @Unroll
    final "isNotFavorite - #caseName"() {
        given:
        final sut = Tweet.builder()
                .id("id")
                .text("text")
                .user(Mock(User))
                .retweetCount(988)
                .favoriteCount(3875)
                .retweet(false)
                .sensitive(false)
                .quote(false)
                .reply(false)
                .favorite(favorite)
                .build()

        expect:
        expected == sut.isNotFavorite()

        where:
        caseName           | favorite || expected
        "favorite - true"  | true     || false
        "favorite - false" | false    || true
    }
}
