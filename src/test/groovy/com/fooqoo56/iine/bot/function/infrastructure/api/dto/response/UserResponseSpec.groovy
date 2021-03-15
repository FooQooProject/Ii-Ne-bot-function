package com.fooqoo56.iine.bot.function.infrastructure.api.dto.response


import spock.lang.Specification
import spock.lang.Unroll

class UserResponseSpec extends Specification {

    @Unroll
    final "isFollow"() {
        given:
        final sut = UserResponse.builder()
                .id("11348282")
                .followersCount(28605561)
                .friendsCount(270)
                .listedCount(90405)
                .favouritesCount(2960)
                .statusesCount(50713)
                .following(following)
                .defaultProfileFlag(false)
                .defaultProfileImageFlag(false)
                .build()

        when:
        final actual = sut.isFollow()

        then:
        actual == expected

        // enumのみ検証する
        where:
        caseName | following     || expected
        "true"   | Boolean.TRUE  || true
        "false"  | Boolean.FALSE || false
        "null"   | null          || false
    }
}
