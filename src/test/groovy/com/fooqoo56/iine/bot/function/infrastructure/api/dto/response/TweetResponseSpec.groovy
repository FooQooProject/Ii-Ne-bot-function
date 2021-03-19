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

    @Unroll
    final "isSensitive - #caseName"() {
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
                .sensitiveFlag(sensitiveFlag)
                .quoteFlag(false)
                .inReplyToStatusId("inReplyToStatusId")
                .favoriteFlag(false)
                .build()

        when:
        final actual = sut.isSensitive()

        then:
        actual == expected

        // enumのみ検証する
        where:
        caseName | sensitiveFlag || expected
        "true"   | Boolean.TRUE  || true
        "false"  | Boolean.FALSE || false
        "null"   | null          || false
    }

    @Unroll
    final "isFavorite #caseName"() {
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
                .inReplyToStatusId("inReplyToStatusId")
                .favoriteFlag(favoriteFlag)
                .build()

        when:
        final actual = sut.isFavorite()

        then:
        actual == expected

        // enumのみ検証する
        where:
        caseName | favoriteFlag  || expected
        "true"   | Boolean.TRUE  || true
        "false"  | Boolean.FALSE || false
        "null"   | null          || false
    }

    @Unroll
    final "isRetweet #caseName"() {
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
                .retweetFlag(retweetFlag)
                .sensitiveFlag(false)
                .quoteFlag(false)
                .inReplyToStatusId("inReplyToStatusId")
                .favoriteFlag(false)
                .build()

        when:
        final actual = sut.isRetweet()

        then:
        actual == expected

        // enumのみ検証する
        where:
        caseName | retweetFlag   || expected
        "true"   | Boolean.TRUE  || true
        "false"  | Boolean.FALSE || false
        "null"   | null          || false
    }

    @Unroll
    final "isQuote #caseName"() {
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
                .quoteFlag(quoteFlag)
                .inReplyToStatusId("inReplyToStatusId")
                .favoriteFlag(false)
                .build()

        when:
        final actual = sut.isQuote()

        then:
        actual == expected

        // enumのみ検証する
        where:
        caseName | quoteFlag     || expected
        "true"   | Boolean.TRUE  || true
        "false"  | Boolean.FALSE || false
        "null"   | null          || false
    }

    final "NullCheck付きgetter"() {
        given:
        final expectedUserResponse = UserResponse.builder()
                .id("11348282")
                .followersCount(28605561)
                .friendsCount(270)
                .listedCount(90405)
                .favouritesCount(2960)
                .statusesCount(50713)
                .following(null)
                .defaultProfileFlag(false)
                .defaultProfileImageFlag(false)
                .build()

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
                .inReplyToStatusId("inReplyToStatusId")
                .favoriteFlag(false)
                .build()

        expect:
        verifyAll {
            sut.getIdWithNullCheck() == "967824267948773377"
            sut.getTextWithNullCheck() == "From pilot to astronaut, Robert H. Lawrence was the first African-American to be selected as an astronaut by any na… https://t.co/FjPEWnh804"
            sut.getRetweetCountWithNullCheck() == 988
            sut.getFavoriteCountWithNullCheck() == 3875
            sut.getUserWithNullCheck() == expectedUserResponse
        }
    }

    final "getIdWithNullCheck 例外"() {
        given:

        final sut = TweetResponse.builder()
                .id(null)
                .text("From pilot to astronaut, Robert H. Lawrence was the first African-American to be selected as an astronaut by any na… https://t.co/FjPEWnh804")
                .user(Mock(UserResponse))
                .retweetCount(988)
                .favoriteCount(3875)
                .retweetFlag(false)
                .sensitiveFlag(false)
                .quoteFlag(false)
                .inReplyToStatusId("inReplyToStatusId")
                .favoriteFlag(false)
                .build()

        when:
        sut.getIdWithNullCheck()

        then:
        final exception = thrown(NullPointerException)
        exception.getMessage() == "フィールドがnullです: TweetResponse.id"
    }

    final "getTextWithNullCheck 例外"() {
        given:

        final sut = TweetResponse.builder()
                .id("967824267948773377")
                .text(null)
                .user(Mock(UserResponse))
                .retweetCount(988)
                .favoriteCount(3875)
                .retweetFlag(false)
                .sensitiveFlag(false)
                .quoteFlag(false)
                .inReplyToStatusId("inReplyToStatusId")
                .favoriteFlag(false)
                .build()

        when:
        sut.getTextWithNullCheck()

        then:
        final exception = thrown(NullPointerException)
        exception.getMessage() == "フィールドがnullです: TweetResponse.text"
    }

    final "getRetweetCountWithNullCheck 例外"() {
        given:

        final sut = TweetResponse.builder()
                .id("967824267948773377")
                .text("From pilot to astronaut, Robert H. Lawrence was the first African-American to be selected as an astronaut by any na… https://t.co/FjPEWnh804")
                .user(Mock(UserResponse))
                .retweetCount(null)
                .favoriteCount(3875)
                .retweetFlag(false)
                .sensitiveFlag(false)
                .quoteFlag(false)
                .inReplyToStatusId("inReplyToStatusId")
                .favoriteFlag(false)
                .build()

        when:
        sut.getRetweetCountWithNullCheck()

        then:
        final exception = thrown(NullPointerException)
        exception.getMessage() == "フィールドがnullです: TweetResponse.retweetCount"
    }

    final "getFavoriteCountWithNullCheck 例外"() {
        given:

        final sut = TweetResponse.builder()
                .id("967824267948773377")
                .text("From pilot to astronaut, Robert H. Lawrence was the first African-American to be selected as an astronaut by any na… https://t.co/FjPEWnh804")
                .user(Mock(UserResponse))
                .retweetCount(988)
                .favoriteCount(null)
                .retweetFlag(false)
                .sensitiveFlag(false)
                .quoteFlag(false)
                .inReplyToStatusId("inReplyToStatusId")
                .favoriteFlag(false)
                .build()

        when:
        sut.getFavoriteCountWithNullCheck()

        then:
        final exception = thrown(NullPointerException)
        exception.getMessage() == "フィールドがnullです: TweetResponse.favoriteCount"
    }

    final "getUserWithNullCheck 例外"() {
        given:

        final sut = TweetResponse.builder()
                .id("967824267948773377")
                .text("From pilot to astronaut, Robert H. Lawrence was the first African-American to be selected as an astronaut by any na… https://t.co/FjPEWnh804")
                .user(null)
                .retweetCount(988)
                .favoriteCount(3875)
                .retweetFlag(false)
                .sensitiveFlag(false)
                .quoteFlag(false)
                .inReplyToStatusId("inReplyToStatusId")
                .favoriteFlag(false)
                .build()

        when:
        sut.getUserWithNullCheck()

        then:
        final exception = thrown(NullPointerException)
        exception.getMessage() == "フィールドがnullです: TweetResponse.user"
    }
}
