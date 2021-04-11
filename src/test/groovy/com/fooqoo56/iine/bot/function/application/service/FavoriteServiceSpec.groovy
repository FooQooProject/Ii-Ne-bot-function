package com.fooqoo56.iine.bot.function.application.service

import com.fooqoo56.iine.bot.function.application.sharedservice.TwitterSharedService
import com.fooqoo56.iine.bot.function.domain.model.Qualification
import com.fooqoo56.iine.bot.function.domain.model.Tweet
import com.fooqoo56.iine.bot.function.domain.model.User
import com.fooqoo56.iine.bot.function.domain.repository.api.FireStoreRepository
import com.fooqoo56.iine.bot.function.exception.NotFoundQualifiedTweetException
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.constant.Lang
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.constant.ResultType
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.request.TweetRequest
import com.fooqoo56.iine.bot.function.presentation.function.dto.TweetQualification
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * FavoriteServiceのテスト
 */
class FavoriteServiceSpec extends Specification {

    private FavoriteService sut
    private TwitterSharedService twitterSharedService
    private FireStoreRepository fireStoreRepository

    final setup() {
        final clock = Clock.fixed(ZonedDateTime.of(
                2021,
                01,
                01,
                12,
                12,
                12,
                0, ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault())

        twitterSharedService = Mock(TwitterSharedService)
        sut = new FavoriteService(twitterSharedService, clock)
    }

    @Unroll
    final "いいねを実行する #caseName"() {
        given:
        // mockを作成する
        twitterSharedService.findTweet(*_) >> findApiFlux
        twitterSharedService.lookUpTweet(*_) >> Flux.fromIterable(lookupApiFlux)
        twitterSharedService.favoriteTweet(*_) >> Mono.just(favoriteApiMono)

        // 処理が複雑なprivateメソッドのみmockを作成する

        // 引数を作成する
        final qualification = TweetQualification.builder()
                .userId("userId")
                .query("http")
                .retweetCount(0)
                .favoriteCount(0)
                .followersCount(0)
                .friendsCount(0)
                .build()

        when:
        final actual = sut.favoriteQualifiedTweet(qualification).block()

        then:
        actual == expected

        where:
        caseName            | findApiFlux                    | lookupApiFlux              | favoriteApiMono        || expected
        "正常系"               | getMockTweetFlux()             | getMockTweet()             | getMockOptionalTweet() || getMockOptionalTweet()
        "全ていいね済みツイート"       | getMockTweetFlux()             | getMockNonFavoritedTweet() | getMockOptionalTweet() || Optional.empty()
        "検索がヒットしない"         | Flux.empty()                   | getMockTweet()             | getMockOptionalTweet() || Optional.empty()
        "検索結果で要件に合うツイートがない" | getMockQualifiedTweetFluxNon() | getMockTweet()             | getMockOptionalTweet() || Optional.empty()
        "いいねAPIのレスポンスが空"    | getMockTweetFlux()             | getMockTweet()             | Optional.empty()       || Optional.empty()
    }

    final "buildTweetRequest"() {
        given:
        // 引数を作成する
        final request = Mock(Qualification) {
            getQuery() >> "spring boot"
        }

        // 期待値を作成する
        final expected = TweetRequest.builder()
                .query("spring boot")
                .maxId("-1")
                .lang(Lang.JA)
                .resultType(ResultType.RECENT)
                .count(100)
                .includeEntitiesFlag(Boolean.FALSE)
                .until(LocalDate.of(2021, 1, 1))
                .build()

        when:
        final actual = sut.buildTweetRequest(request)

        then:
        actual == expected
    }

    final "filterNonFavoritedTweet"() {
        given:
        // mockを作成する
        twitterSharedService.lookUpTweet(*_) >> Flux.fromIterable(getMockTweet())

        // 期待値を作成する
        final expected = ["aaa", "bbb", "ccc"]

        when:
        final actual = sut.filterNonFavoritedTweet(["aaa", "bbb", "ccc", "ddd"]).block()

        then:
        actual == expected
    }

    final "sortTweetOrderByFavoritesCountDesc"() {
        given:
        final tweetList = getMockTweet()

        final expected = ["aaa", "ccc", "bbb", "ddd"]

        when:
        final actual = sut.sortTweetOrderByFavoritesCountDesc(tweetList)

        then:
        actual == expected
    }

    @Unroll
    final "getTopIdList #caseName"() {
        when:
        final actual = sut.getTopIdList(idList)

        then:
        actual == expectedIdList

        where:
        caseName | idList                  || expectedIdList
        "30個"    | ('a'..'z') + ('a'..'d') || ('a'..'z') + ('a'..'d')
        "31個"    | ('a'..'z') + ('a'..'e') || ('a'..'z') + ('a'..'d')
    }

    final "getTopIdList 例外"() {
        when:
        sut.getTopIdList([])

        then:
        final exception = thrown(NotFoundQualifiedTweetException)
        exception.getMessage() == "条件に合致したツイートが存在しません"
    }

    final "getTopId"() {
        expect:
        "aaa" == sut.getTopId(["aaa", "ccc", "bbb"])
    }

    final "getTopId 例外"() {
        when:
        sut.getTopId([])

        then:
        final exception = thrown(NotFoundQualifiedTweetException)
        exception.getMessage() == "条件に合致したツイートが存在しません"
    }

    final "buildQualification"() {
        given:
        // 引数を作成する
        final qualification = TweetQualification.builder()
                .query("http")
                .retweetCount(0)
                .favoriteCount(0)
                .followersCount(0)
                .friendsCount(0)
                .build()

        final expected = Qualification.builder()
                .query("http")
                .minRetweetCount(0)
                .minFavoriteCount(0)
                .minFollowersCount(0)
                .minFriendsCount(0)
                .sensitive(false)
                .quote(false)
                .reply(false)
                .minStatusesCount(100)
                .follow(false)
                .build()

        when:
        final actual = sut.buildQualification(qualification)

        then:
        actual == expected
    }

    @Unroll
    final "isQualifiedTweet - パターン1 - #caseName"() {
        given:
        final qualification = Qualification.builder()
                .query("spring boot")
                .minRetweetCount(10)
                .minFavoriteCount(10)
                .minFollowersCount(10)
                .minFriendsCount(10)
                .minStatusesCount(100)
                .sensitive(false)
                .quote(false)
                .reply(false)
                .follow(false)
                .build()

        final tweet = Mock(Tweet) {
            getText() >> text
            getUser() >> Mock(User) {
                getFollowersCount() >> followersCount
                getFriendsCount() >> friendsCount
                getStatusesCount() >> statusesCount
                isFollow() >> follow
            }
            getRetweetCount() >> retweetCount
            getFavoriteCount() >> favoriteCount
            isSensitive() >> sensitive
            isQuote() >> quote
            isReply() >> reply
        }

        when:
        final actual = sut.isQualifiedTweet(tweet, qualification)

        then:
        actual == expectedFlag

        // qualificationの値は固定
        where:
        caseName               | text                  | followersCount | friendsCount | statusesCount | retweetCount | favoriteCount | sensitive | quote | reply | follow || expectedFlag
        "要件全て満たす"              | "nodejs, spring boot" | 10             | 10           | 100           | 10           | 10            | false     | false | false | false  || true
        "text - 要件外"           | "python, javascript"  | 10             | 10           | 100           | 10           | 10            | false     | false | false | false  || false
        "followersCount - 要件外" | "nodejs, spring boot" | 9              | 10           | 100           | 10           | 10            | false     | false | false | false  || false
        "friendsCount - 要件外"   | "nodejs, spring boot" | 10             | 9            | 100           | 10           | 10            | false     | false | false | false  || false
        "statusesCount - 要件外"  | "nodejs, spring boot" | 10             | 10           | 99            | 10           | 10            | false     | false | false | false  || false
        "retweetCount - 要件外"   | "nodejs, spring boot" | 10             | 10           | 100           | 9            | 10            | false     | false | false | false  || false
        "favoriteCount - 要件外"  | "nodejs, spring boot" | 10             | 10           | 100           | 10           | 9             | false     | false | false | false  || false
        "sensitive - 要件外"      | "nodejs, spring boot" | 10             | 10           | 100           | 10           | 10            | true      | false | false | false  || false
        "quote - 要件外"          | "nodejs, spring boot" | 10             | 10           | 100           | 10           | 10            | false     | true  | false | false  || false
        "reply - 要件外"          | "nodejs, spring boot" | 10             | 10           | 100           | 10           | 10            | false     | false | true  | false  || false
        "follow - 要件外"         | "nodejs, spring boot" | 10             | 10           | 100           | 10           | 10            | false     | false | false | true   || false

    }

    @Unroll
    final "isQualifiedTweet - パターン2 - #caseName"() {
        given:
        final qualification = Qualification.builder()
                .query("spring boot")
                .minRetweetCount(100)
                .minFavoriteCount(100)
                .minFollowersCount(100)
                .minFriendsCount(100)
                .minStatusesCount(1000)
                .sensitive(true)
                .quote(true)
                .reply(true)
                .follow(true)
                .build()

        final tweet = Mock(Tweet) {
            getText() >> text
            getUser() >> Mock(User) {
                getFollowersCount() >> followersCount
                getFriendsCount() >> friendsCount
                getStatusesCount() >> statusesCount
                isFollow() >> follow
            }
            getRetweetCount() >> retweetCount
            getFavoriteCount() >> favoriteCount
            isSensitive() >> sensitive
            isQuote() >> quote
            isReply() >> reply
        }

        when:
        final actual = sut.isQualifiedTweet(tweet, qualification)

        then:
        actual == expectedFlag

        // qualificationの値は固定
        where:
        caseName               | text                  | followersCount | friendsCount | statusesCount | retweetCount | favoriteCount | sensitive | quote | reply | follow || expectedFlag
        "要件全て満たす"              | "nodejs, spring boot" | 100            | 100          | 1000          | 100          | 100           | true      | true  | true  | true   || true
        "text - 要件外"           | "python, javascript"  | 100            | 100          | 1000          | 100          | 100           | true      | true  | true  | true   || false
        "followersCount - 要件外" | "nodejs, spring boot" | 99             | 100          | 1000          | 100          | 100           | true      | true  | true  | true   || false
        "friendsCount - 要件外"   | "nodejs, spring boot" | 100            | 99           | 1000          | 100          | 100           | true      | true  | true  | true   || false
        "statusesCount - 要件外"  | "nodejs, spring boot" | 100            | 100          | 999           | 100          | 100           | true      | true  | true  | true   || false
        "retweetCount - 要件外"   | "nodejs, spring boot" | 100            | 100          | 1000          | 99           | 100           | true      | true  | true  | true   || false
        "favoriteCount - 要件外"  | "nodejs, spring boot" | 100            | 100          | 1000          | 100          | 99            | true      | true  | true  | true   || false
        "sensitive - 要件外"      | "nodejs, spring boot" | 100            | 100          | 1000          | 100          | 100           | false     | true  | true  | true   || false
        "quote - 要件外"          | "nodejs, spring boot" | 100            | 100          | 1000          | 100          | 100           | true      | false | true  | true   || false
        "reply - 要件外"          | "nodejs, spring boot" | 100            | 100          | 1000          | 100          | 100           | true      | true  | false | true   || false
    }

    @Unroll
    final "isGraterThanEqualQualification - #caseName"() {
        expect:
        expectedFlag == sut.isGraterThanEqualQualification(targetNumber, qualificationNumber)

        where:
        caseName         | targetNumber | qualificationNumber || expectedFlag
        "ツイート = 要件(10)"  | 10           | 10                  || true
        "ツイート > 要件(10)"  | 11           | 10                  || true
        "ツイート < 要件(10)"  | 9            | 10                  || false
        "ツイート = 要件(100)" | 100          | 100                 || true
        "ツイート > 要件(100)" | 101          | 100                 || true
        "ツイート < 要件(100)" | 99           | 100                 || false
    }

    /**
     * ツイートのOptionalを取得する
     *
     * @return ツイートのOptional
     */
    private static final getMockOptionalTweet() {
        return Optional.of(Tweet.builder()
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
                .build())
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
                        .build(),
                Tweet.builder()
                        .id("111144427480911872")
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

    /**
     * ツイートのFluxのモックを取得する
     *
     * @return ツイートのFlux
     */
    private static final getMockQualifiedTweetFluxNon() {
        return Flux.just(
                Tweet.builder()
                        .id("967824267948773377")
                        .text("hoge")
                        .user(User.builder()
                                .id("11348282")
                                .followersCount(0)
                                .friendsCount(0)
                                .listedCount(0)
                                .favouritesCount(0)
                                .statusesCount(0)
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
                        .build()
        )
    }

    /**
     * モックツイートのリストを取得する
     *
     * @return モックのリスト
     */
    private final getMockTweet() {
        return [
                Mock(Tweet) {
                    isNotFavorite() >> true
                    getId() >> "aaa"
                    getUser() >> Mock(User) {
                        getFavouritesCount() >> 10
                    }

                },
                Mock(Tweet) {
                    isNotFavorite() >> true
                    getId() >> "bbb"
                    getUser() >> Mock(User) {
                        getFavouritesCount() >> 8
                    }
                },
                Mock(Tweet) {
                    isNotFavorite() >> true
                    getId() >> "ccc"
                    getUser() >> Mock(User) {
                        getFavouritesCount() >> 9
                    }
                },
                Mock(Tweet) {
                    isNotFavorite() >> false
                    getId() >> "ddd"
                    getUser() >> Mock(User) {
                        getFavouritesCount() >> 7
                    }
                }
        ]
    }

    /**
     * 全ていいね済みのモックツイートのリストを取得する
     *
     * @return モックのリスト
     */
    private final getMockNonFavoritedTweet() {
        return [
                Mock(Tweet) {
                    isNotFavorite() >> false
                    getId() >> "aaa"
                    getUser() >> Mock(User) {
                        getFavouritesCount() >> 10
                    }

                },
                Mock(Tweet) {
                    isNotFavorite() >> false
                    getId() >> "bbb"
                    getUser() >> Mock(User) {
                        getFavouritesCount() >> 8
                    }
                },
                Mock(Tweet) {
                    isNotFavorite() >> false
                    getId() >> "ccc"
                    getUser() >> Mock(User) {
                        getFavouritesCount() >> 9
                    }
                },
                Mock(Tweet) {
                    isNotFavorite() >> false
                    getId() >> "ddd"
                    getUser() >> Mock(User) {
                        getFavouritesCount() >> 7
                    }
                }
        ]
    }
}
