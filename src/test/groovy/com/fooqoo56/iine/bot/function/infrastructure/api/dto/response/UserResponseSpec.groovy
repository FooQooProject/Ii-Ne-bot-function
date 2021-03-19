package com.fooqoo56.iine.bot.function.infrastructure.api.dto.response


import spock.lang.Specification
import spock.lang.Unroll

class UserResponseSpec extends Specification {

    @Unroll
    final "isFollow - #caseName"() {
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

        where:
        caseName | following     || expected
        "true"   | Boolean.TRUE  || true
        "false"  | Boolean.FALSE || false
        "null"   | null          || false
    }

    @Unroll
    final "isDefaultProfile - #caseName"() {
        given:
        final sut = UserResponse.builder()
                .id("11348282")
                .followersCount(28605561)
                .friendsCount(270)
                .listedCount(90405)
                .favouritesCount(2960)
                .statusesCount(50713)
                .following(false)
                .defaultProfileFlag(defaultProfileFlag)
                .defaultProfileImageFlag(false)
                .build()

        when:
        final actual = sut.isDefaultProfile()

        then:
        actual == expected

        where:
        caseName | defaultProfileFlag || expected
        "true"   | Boolean.TRUE       || true
        "false"  | Boolean.FALSE      || false
        "null"   | null               || false
    }

    @Unroll
    final "isDefaultProfileImage - #caseName"() {
        given:
        final sut = UserResponse.builder()
                .id("11348282")
                .followersCount(28605561)
                .friendsCount(270)
                .listedCount(90405)
                .favouritesCount(2960)
                .statusesCount(50713)
                .following(false)
                .defaultProfileFlag(false)
                .defaultProfileImageFlag(defaultProfileImageFlag)
                .build()

        when:
        final actual = sut.isDefaultProfileImage()

        then:
        actual == expected

        where:
        caseName | defaultProfileImageFlag || expected
        "true"   | Boolean.TRUE            || true
        "false"  | Boolean.FALSE           || false
        "null"   | null                    || false
    }

    final "NullCheck付きgetter"() {
        given:
        final sut = UserResponse.builder()
                .id("11348282")
                .followersCount(28605561)
                .friendsCount(270)
                .listedCount(90405)
                .favouritesCount(2960)
                .statusesCount(50713)
                .following(false)
                .defaultProfileFlag(false)
                .defaultProfileImageFlag(false)
                .build()

        expect:
        verifyAll {
            sut.getIdWithNullCheck() == "11348282"
            sut.getFriendsCountWithNullCheck() == 270
            sut.getFollowersCountWithNullCheck() == 28605561
            sut.getListedCountWithNullCheck() == 90405
            sut.getFavouritesCountWithNullCheck() == 2960
            sut.getStatusesCountWithNullCheck() == 50713
        }
    }

    final "getIdWithNullCheck 例外"() {
        given:
        final sut = UserResponse.builder()
                .id(null)
                .followersCount(28605561)
                .friendsCount(270)
                .listedCount(90405)
                .favouritesCount(2960)
                .statusesCount(50713)
                .following(false)
                .defaultProfileFlag(false)
                .defaultProfileImageFlag(false)
                .build()

        when:
        sut.getIdWithNullCheck()

        then:
        final exception = thrown(NullPointerException)
        exception.getMessage() == "フィールドがnullです: UserResponse.id"
    }

    final "getFriendsCountWithNullCheck 例外"() {
        given:
        final sut = UserResponse.builder()
                .id("11348282")
                .followersCount(28605561)
                .friendsCount(null)
                .listedCount(90405)
                .favouritesCount(2960)
                .statusesCount(50713)
                .following(false)
                .defaultProfileFlag(false)
                .defaultProfileImageFlag(false)
                .build()

        when:
        sut.getFriendsCountWithNullCheck()

        then:
        final exception = thrown(NullPointerException)
        exception.getMessage() == "フィールドがnullです: UserResponse.friendsCount"
    }

    final "getFollowersCountWithNullCheck 例外"() {
        given:
        final sut = UserResponse.builder()
                .id("11348282")
                .followersCount(null)
                .friendsCount(270)
                .listedCount(90405)
                .favouritesCount(2960)
                .statusesCount(50713)
                .following(false)
                .defaultProfileFlag(false)
                .defaultProfileImageFlag(false)
                .build()

        when:
        sut.getFollowersCountWithNullCheck()

        then:
        final exception = thrown(NullPointerException)
        exception.getMessage() == "フィールドがnullです: UserResponse.followersCount"
    }

    final "getListedCountWithNullCheck 例外"() {
        given:
        final sut = UserResponse.builder()
                .id("11348282")
                .followersCount(28605561)
                .friendsCount(270)
                .listedCount(null)
                .favouritesCount(2960)
                .statusesCount(50713)
                .following(false)
                .defaultProfileFlag(false)
                .defaultProfileImageFlag(false)
                .build()

        when:
        sut.getListedCountWithNullCheck()

        then:
        final exception = thrown(NullPointerException)
        exception.getMessage() == "フィールドがnullです: UserResponse.listedCount"
    }

    final "getFavouritesCountWithNullCheck 例外"() {
        given:
        final sut = UserResponse.builder()
                .id("11348282")
                .followersCount(28605561)
                .friendsCount(270)
                .listedCount(90405)
                .favouritesCount(null)
                .statusesCount(50713)
                .following(false)
                .defaultProfileFlag(false)
                .defaultProfileImageFlag(false)
                .build()

        when:
        sut.getFavouritesCountWithNullCheck()

        then:
        final exception = thrown(NullPointerException)
        exception.getMessage() == "フィールドがnullです: UserResponse.favouritesCount"
    }

    final "getStatusesCountWithNullCheck 例外"() {
        given:
        final sut = UserResponse.builder()
                .id("11348282")
                .followersCount(28605561)
                .friendsCount(270)
                .listedCount(90405)
                .favouritesCount(2960)
                .statusesCount(null)
                .following(false)
                .defaultProfileFlag(false)
                .defaultProfileImageFlag(false)
                .build()

        when:
        sut.getStatusesCountWithNullCheck()

        then:
        final exception = thrown(NullPointerException)
        exception.getMessage() == "フィールドがnullです: UserResponse.statusesCount"
    }
}
