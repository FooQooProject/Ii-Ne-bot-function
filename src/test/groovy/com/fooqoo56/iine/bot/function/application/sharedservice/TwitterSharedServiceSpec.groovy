package com.fooqoo56.iine.bot.function.application.sharedservice

import com.fooqoo56.iine.bot.function.domain.model.Tweet
import com.fooqoo56.iine.bot.function.domain.model.TwitterUser
import com.fooqoo56.iine.bot.function.domain.model.User
import com.fooqoo56.iine.bot.function.domain.repository.api.TwitterRepository
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.request.TweetRequest
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.response.SearchMetaDataResponse
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.response.TweetListResponse
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.response.TweetResponse
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.response.UserResponse
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import spock.lang.Specification
import spock.lang.Unroll

/**
 * TwitterSharedServiceのテスト
 */
class TwitterSharedServiceSpec extends Specification {

    private TwitterRepository twitterRepository
    private TwitterSharedService sut

    final setup() {
        twitterRepository = Mock(TwitterRepository)
        sut = new TwitterSharedService(twitterRepository)
    }

    final "findTweet"() {
        given:
        final request = Mock(TweetRequest)

        final expected = getTweetList()

        // mock作成
        twitterRepository.findTweet(*_) >> getFindTweetMock()

        when:
        final actual = sut.findTweet(request, Mock(TwitterUser)).collectList().block()

        then:
        actual == expected
    }

    final "favoriteTweet"() {
        given:
        final expected = Optional.of(Tweet.builder()
                .id("id")
                .text("text")
                .user(User.builder()
                        .id("userId")
                        .followersCount(0)
                        .friendsCount(0)
                        .listedCount(0)
                        .favouritesCount(0)
                        .statusesCount(0)
                        .follow(false)
                        .defaultProfile(true)
                        .defaultProfileImage(true)
                        .build())
                .retweetCount(0)
                .favoriteCount(0)
                .retweet(true)
                .sensitive(true)
                .quote(true)
                .reply(true)
                .favorite(true)
                .build())

        // mock作成
        twitterRepository.favoriteTweet(*_) >> Mono.just(
                TweetResponse.builder()
                        .id("id")
                        .text("text")
                        .user(UserResponse.builder()
                                .id("userId")
                                .followersCount(0)
                                .friendsCount(0)
                                .listedCount(0)
                                .favouritesCount(0)
                                .statusesCount(0)
                                .following(Boolean.FALSE)
                                .defaultProfileFlag(Boolean.TRUE)
                                .defaultProfileImageFlag(Boolean.TRUE)
                                .build())
                        .retweetCount(0)
                        .favoriteCount(0)
                        .favoriteFlag(Boolean.TRUE)
                        .retweetFlag(Boolean.TRUE)
                        .sensitiveFlag(Boolean.TRUE)
                        .quoteFlag(Boolean.TRUE)
                        .inReplyToStatusId("reply")
                        .build())

        when:
        final actual = sut.favoriteTweet("id", Mock(TwitterUser)).block()

        then:
        actual == expected
    }

    final "favoriteTweet - 例外"() {
        given:
        // mock作成
        twitterRepository.favoriteTweet(*_) >> Mono.error(new WebClientResponseException(500, "", null, null, null))

        when:
        final actual = sut.favoriteTweet("id", Mock(TwitterUser)).block()

        then:
        actual == Optional.empty()
    }

    final "lookUpTweet"() {
        given:
        final request = List.of("id1", "id2")

        final expected = getTweetList()

        // mock作成
        twitterRepository.lookupTweet(*_) >> getLookUpTweetMock()

        when:
        final actual = sut.lookUpTweet(request, Mock(TwitterUser)).collectList().block()

        then:
        actual == expected
    }

    @Unroll
    final "buildTweet - #caseName"() {
        given:
        final tweetResponse = TweetResponse.builder()
                .id("id")
                .text("text")
                .user(UserResponse.builder()
                        .id("userId")
                        .followersCount(0)
                        .friendsCount(0)
                        .listedCount(0)
                        .favouritesCount(0)
                        .statusesCount(0)
                        .following(Boolean.FALSE)
                        .defaultProfileFlag(Boolean.TRUE)
                        .defaultProfileImageFlag(Boolean.TRUE)
                        .build())
                .retweetCount(0)
                .favoriteCount(0)
                .favoriteFlag(Boolean.TRUE)
                .retweetFlag(Boolean.TRUE)
                .sensitiveFlag(sensitiveFlag)
                .quoteFlag(Boolean.TRUE)
                .inReplyToStatusId(inReplyToStatusId)
                .build()

        final expected = Tweet.builder()
                .id("id")
                .text("text")
                .user(User.builder()
                        .id("userId")
                        .followersCount(0)
                        .friendsCount(0)
                        .listedCount(0)
                        .favouritesCount(0)
                        .statusesCount(0)
                        .follow(false)
                        .defaultProfile(true)
                        .defaultProfileImage(true)
                        .build())
                .retweetCount(0)
                .favoriteCount(0)
                .retweet(true)
                .sensitive(expectedSensitive)
                .quote(true)
                .reply(expectedReply)
                .favorite(true)
                .build()

        when:
        final actual = sut.buildTweet(tweetResponse)

        then:
        actual == expected

        where:
        caseName                   | sensitiveFlag | inReplyToStatusId || expectedSensitive | expectedReply
        "nullなし"                   | Boolean.TRUE  | "reply"           || true              | true
        "sensitiveFlag - null"     | null          | "reply"           || false             | true
        "inReplyToStatusId - null" | Boolean.TRUE  | null              || true              | false
    }

    @Unroll
    final "buildUser - #caseName"() {
        given:
        final userResponse = UserResponse.builder()
                .id("userId")
                .followersCount(0)
                .friendsCount(0)
                .listedCount(0)
                .favouritesCount(0)
                .statusesCount(0)
                .following(following)
                .defaultProfileFlag(Boolean.TRUE)
                .defaultProfileImageFlag(Boolean.TRUE)
                .build()

        final expected = User.builder()
                .id("userId")
                .followersCount(0)
                .friendsCount(0)
                .listedCount(0)
                .favouritesCount(0)
                .statusesCount(0)
                .follow(expectedFollowing)
                .defaultProfile(true)
                .defaultProfileImage(true)
                .build()

        when:
        final actual = sut.buildUser(userResponse)

        then:
        actual == expected

        where:
        caseName           | following    || expectedFollowing
        "nullなし"           | Boolean.TRUE || true
        "following - null" | null         || false
    }

    /**
     * ツイートのリストの取得
     *
     * @return ツイートのリスト
     */
    private final getTweetList() {
        return [
                Tweet.builder()
                        .id("id")
                        .text("text")
                        .user(User.builder()
                                .id("userId")
                                .followersCount(0)
                                .friendsCount(0)
                                .listedCount(0)
                                .favouritesCount(0)
                                .statusesCount(0)
                                .follow(false)
                                .defaultProfile(true)
                                .defaultProfileImage(true)
                                .build())
                        .retweetCount(0)
                        .favoriteCount(0)
                        .retweet(true)
                        .sensitive(true)
                        .quote(true)
                        .reply(true)
                        .favorite(true)
                        .build(),
                Tweet.builder()
                        .id("id2")
                        .text("text2")
                        .user(User.builder()
                                .id("userId2")
                                .followersCount(10)
                                .friendsCount(10)
                                .listedCount(10)
                                .favouritesCount(10)
                                .statusesCount(10)
                                .follow(true)
                                .defaultProfile(false)
                                .defaultProfileImage(false)
                                .build())
                        .retweetCount(10)
                        .favoriteCount(10)
                        .retweet(false)
                        .sensitive(false)
                        .quote(false)
                        .reply(false)
                        .favorite(false)
                        .build()
        ]
    }

    /**
     * ツイート取得APIのレスポンスを取得する
     *
     * @return ツイート取得APIのレスポンス
     */
    private final getLookUpTweetMock() {
        return Flux.just(
                TweetResponse.builder()
                        .id("id")
                        .text("text")
                        .user(UserResponse.builder()
                                .id("userId")
                                .followersCount(0)
                                .friendsCount(0)
                                .listedCount(0)
                                .favouritesCount(0)
                                .statusesCount(0)
                                .following(Boolean.FALSE)
                                .defaultProfileFlag(Boolean.TRUE)
                                .defaultProfileImageFlag(Boolean.TRUE)
                                .build())
                        .retweetCount(0)
                        .favoriteCount(0)
                        .favoriteFlag(Boolean.TRUE)
                        .retweetFlag(Boolean.TRUE)
                        .sensitiveFlag(Boolean.TRUE)
                        .quoteFlag(Boolean.TRUE)
                        .inReplyToStatusId("reply")
                        .build(),
                TweetResponse.builder()
                        .id("id2")
                        .text("text2")
                        .user(UserResponse.builder()
                                .id("userId2")
                                .followersCount(10)
                                .friendsCount(10)
                                .listedCount(10)
                                .favouritesCount(10)
                                .statusesCount(10)
                                .following(Boolean.TRUE)
                                .defaultProfileFlag(Boolean.FALSE)
                                .defaultProfileImageFlag(Boolean.FALSE)
                                .build())
                        .retweetCount(10)
                        .favoriteCount(10)
                        .favoriteFlag(Boolean.FALSE)
                        .retweetFlag(Boolean.FALSE)
                        .sensitiveFlag(Boolean.FALSE)
                        .quoteFlag(Boolean.FALSE)
                        .inReplyToStatusId(null)
                        .build()
        )
    }

    /**
     * Twitter検索APIのFluxを取得する
     *
     * @return ツイート検索APIのレスポンス
     */
    private final getFindTweetMock() {
        return Mono.just(
                TweetListResponse.builder()
                        .statuses([
                                TweetResponse.builder()
                                        .id("id")
                                        .text("text")
                                        .user(UserResponse.builder()
                                                .id("userId")
                                                .followersCount(0)
                                                .friendsCount(0)
                                                .listedCount(0)
                                                .favouritesCount(0)
                                                .statusesCount(0)
                                                .following(Boolean.FALSE)
                                                .defaultProfileFlag(Boolean.TRUE)
                                                .defaultProfileImageFlag(Boolean.TRUE)
                                                .build())
                                        .retweetCount(0)
                                        .favoriteCount(0)
                                        .favoriteFlag(Boolean.TRUE)
                                        .retweetFlag(Boolean.TRUE)
                                        .sensitiveFlag(Boolean.TRUE)
                                        .quoteFlag(Boolean.TRUE)
                                        .inReplyToStatusId("reply")
                                        .build(),
                                TweetResponse.builder()
                                        .id("id2")
                                        .text("text2")
                                        .user(UserResponse.builder()
                                                .id("userId2")
                                                .followersCount(10)
                                                .friendsCount(10)
                                                .listedCount(10)
                                                .favouritesCount(10)
                                                .statusesCount(10)
                                                .following(Boolean.TRUE)
                                                .defaultProfileFlag(Boolean.FALSE)
                                                .defaultProfileImageFlag(Boolean.FALSE)
                                                .build())
                                        .retweetCount(10)
                                        .favoriteCount(10)
                                        .favoriteFlag(Boolean.FALSE)
                                        .retweetFlag(Boolean.FALSE)
                                        .sensitiveFlag(Boolean.FALSE)
                                        .quoteFlag(Boolean.FALSE)
                                        .inReplyToStatusId(null)
                                        .build(),
                        ])
                        .searchMetaDataResponse(Mock(SearchMetaDataResponse))
                        .build()
        )
    }
}
