package com.fooqoo56.iine.bot.function.infrastructure.api.dto.response


import spock.lang.Specification
import spock.lang.Unroll

class TweetResponseSpec extends Specification {

    @Unroll
    final "isReply - #caseName"() {
        given:
        final sut = TweetResponse.builder()
                .id("967824267948773377")
                .text("From pilot to astronaut, Robert H. Lawrence was the first African-American to be selected as an astronaut by any na… https://t.co/FjPEWnh804")
                .user(UserResponse.builder()
                        .id("11348282")
                        .followersCount(28605561)
                        .friendsCount(270)
                        .listedCount(90405)
                        .favouritesCount(2960)
                        .statusesCount(50713)
                        .following(null)
                        .defaultProfileFlag(false)
                        .defaultProfileImageFlag(false)
                        .build())
                .retweetCount(988)
                .favoriteCount(3875)
                .retweetFlag(false)
                .sensitiveFlag(false)
                .quoteFlag(false)
                .inReplyToStatusId(inReplyToStatusId)
                .favoriteFlag(false)
                .build()

        when:
        final actual = sut.isReply()

        then:
        actual == expected

        // enumのみ検証する
        where:
        caseName | inReplyToStatusId || expected
        "idあり"   | "id"              || true
        "null"   | null              || false
    }
}
