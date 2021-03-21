package com.fooqoo56.iine.bot.function.infrastructure.api.repositoryimpl

import com.fooqoo56.iine.bot.function.domain.model.TwitterUser
import com.fooqoo56.iine.bot.function.infrastructure.api.config.ApiSetting
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.constant.Lang
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.constant.ResultType
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.request.TweetRequest
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.response.*
import com.fooqoo56.iine.bot.function.infrastructure.api.util.OauthAuthorizationHeaderBuilder
import com.google.api.client.auth.oauth.OAuthHmacSigner
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import spock.lang.Specification

import java.security.SecureRandom
import java.time.*

/**
 * TwitterRepositoryのテスト
 */
class TwitterRepositoryImplSpec extends Specification {

    private MockWebServer mockWebServer
    private WebClient webClient
    private ApiSetting apiSetting
    private Clock clock
    private SecureRandom secureRandom
    private OAuthHmacSigner signer

    final setup() {
        mockWebServer = new MockWebServer()
        mockWebServer.start()
        webClient = WebClient.create(mockWebServer.url("/").toString())

        apiSetting = new ApiSetting()
        apiSetting.setBaseUrl("url")
        apiSetting.setConnectTimeout(Duration.ofMillis(1000))
        apiSetting.setReadTimeout(Duration.ofMillis(1000))
        apiSetting.setMaxInMemorySize(16777216)

        clock = Clock.fixed(ZonedDateTime.of(
                2021,
                01,
                01,
                12,
                12,
                12,
                0, ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault())

        secureRandom = Mock(SecureRandom) {
            nextBytes() >> "12345"
        }

        signer = new OAuthHmacSigner()
    }

    final cleanup() {
        mockWebServer.shutdown()
    }

    final "getBearerToken"() {
        given:
        // テスト対象クラスのインスタンス作成
        final sut = new TwitterRepositoryImpl(apiSetting, webClient, webClient, webClient, webClient, clock, secureRandom, signer)

        // mockサーバを作成する
        final mockResponse = new FileReader("src/test/resources/oauth2.json").text
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.OK.value())
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(mockResponse))

        // 引数を作成する
        final twitterUser = TwitterUser.builder()
                .apiKey("apiKey")
                .apiSecret("apiSecret")
                .accessToken("accessToken")
                .accessTokenSecret("accessTokenSecret")
                .build()

        // 期待値を作成する
        final expectedResults = Oauth2Response.builder()
                .tokenType("token_type")
                .accessToken("access_token")
                .build()

        when:
        final actual = sut.getBearerToken(twitterUser).block()

        then:
        actual == expectedResults
    }

    final "buildOauthAuthorizationHeaderBuilder"() {
        given:
        // テスト対象クラスのインスタンス作成
        final sut = new TwitterRepositoryImpl(apiSetting, webClient, webClient, webClient, webClient, clock, secureRandom, signer)

        // 期待値を作成する
        final expectedResults = OauthAuthorizationHeaderBuilder.builder()
                .method("POST")
                .url("url")
                .consumerSecret("apiSecret")
                .tokenSecret("accessTokenSecret")
                .queryParameters(Map.of("id", "id"))
                .consumerKey("apiKey")
                .accessToken("accessToken")
                .instant(Instant.now(clock))
                .secureRandom(secureRandom)
                .signer(signer)
                .build()

        // 引数を作成する
        final twitterUser = TwitterUser.builder()
                .apiKey("apiKey")
                .apiSecret("apiSecret")
                .accessToken("accessToken")
                .accessTokenSecret("accessTokenSecret")
                .build()

        when:
        final actual = sut.buildOauthAuthorizationHeaderBuilder(twitterUser, "id",)

        then:
        actual == expectedResults
    }

    final "findTweet"() {
        given:
        // テスト対象クラスのインスタンス作成 - メソッドのモック化
        final sut = (TwitterRepositoryImpl) Spy(TwitterRepositoryImpl, constructorArgs: [apiSetting, webClient, webClient, webClient, webClient, clock, secureRandom, signer]) {
            getBearerToken(*_) >> Mono.just(Oauth2Response.builder()
                    .tokenType("token_type")
                    .accessToken("access_token")
                    .build())
        }

        // mockサーバを作成する
        final mockResponse = new FileReader("src/test/resources/searchTweet.json").text
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(mockResponse))

        // 期待値を作成する
        final expectedResults = getExpectedTweetSearchApiResponse()

        // 引数を作成する
        final request = TweetRequest.builder()
                .query("query")
                .maxId("0")
                .lang(Lang.JA)
                .resultType(ResultType.RECENT)
                .includeEntitiesFlag(Boolean.FALSE)
                .count(100)
                .until(LocalDate.of(2021, 1, 1))
                .build()

        final twitterUser = TwitterUser.builder()
                .apiKey("apiKey")
                .apiSecret("apiSecret")
                .accessToken("accessToken")
                .accessTokenSecret("accessTokenSecret")
                .build()

        when:
        final actual = sut.findTweet(request, twitterUser).block()

        then:
        actual == expectedResults
    }

    final "favoriteTweet"() {
        given:
        // テスト対象クラスのインスタンス作成 - メソッドのモック化
        final sut = (TwitterRepositoryImpl) Spy(TwitterRepositoryImpl, constructorArgs: [apiSetting, webClient, webClient, webClient, webClient, clock, secureRandom, signer]) {
            buildOauthAuthorizationHeaderBuilder() >> Mock(OauthAuthorizationHeaderBuilder) {
                getOauthHeader() >> "oauth2header"
            }
        }

        // mockサーバを作成する
        final mockResponse = new FileReader("src/test/resources/favoriteTweet.json").text
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.OK.value())
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(mockResponse))

        // 期待値を作成する
        final expectedResults = getExpectedTweetFavoriteApiResponse()

        // 引数を作成する
        final request = "query"

        final twitterUser = TwitterUser.builder()
                .apiKey("apiKey")
                .apiSecret("apiSecret")
                .accessToken("accessToken")
                .accessTokenSecret("accessTokenSecret")
                .build()

        when:
        final actual = sut.favoriteTweet(request, twitterUser).block()

        then:
        actual == expectedResults
    }

    final "lookupTweet"() {
        given:
        // テスト対象クラスのインスタンス作成 - メソッドのモック化
        final sut = (TwitterRepositoryImpl) Spy(TwitterRepositoryImpl, constructorArgs: [apiSetting, webClient, webClient, webClient, webClient, clock, secureRandom, signer]) {
            getBearerToken(*_) >> Mono.just(Oauth2Response.builder()
                    .tokenType("token_type")
                    .accessToken("access_token")
                    .build())
        }

        // mockサーバを作成する
        final mockResponse = new FileReader("src/test/resources/lookupTweet.json").text
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.OK.value())
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(mockResponse))

        // 期待値を作成する
        final expectedResults = getExpectedTweetLookupApiResponse()

        // 引数を作成する
        final request = ["xxxxx", "yyyyy"]

        final twitterUser = TwitterUser.builder()
                .apiKey("apiKey")
                .apiSecret("apiSecret")
                .accessToken("accessToken")
                .accessTokenSecret("accessTokenSecret")
                .build()

        when:
        final actual = sut.lookupTweet(request, twitterUser).collectList().block()

        then:
        actual == expectedResults
    }

    /**
     * ツイート検索APIの期待値を取得する
     *
     * @return ツイート検索APIのレスポンス(固定値)
     */
    private static final getExpectedTweetSearchApiResponse() {
        return TweetListResponse.builder()
                .statuses([
                        TweetResponse.builder()
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
                                .inReplyToStatusId(null)
                                .favoriteFlag(false)
                                .build(),
                        TweetResponse.builder()
                                .id("967844427480911872")
                                .text("A magnetic power struggle of galactic proportions - new research highlights the role of the Sun's magnetic landscap… https://t.co/29dZgga54m")
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
                                .retweetCount(2654)
                                .favoriteCount(7962)
                                .retweetFlag(false)
                                .sensitiveFlag(false)
                                .quoteFlag(false)
                                .inReplyToStatusId(null)
                                .favoriteFlag(false)
                                .build()])
                .searchMetaDataResponse(new SearchMetaDataResponse("?max_id=967574182522482687&q=nasa&include_entities=1&result_type=popular"))
                .build()
    }

    /**
     * ツイートいいねAPIの期待値を取得する
     *
     * @return TweetResponse
     */
    private static final getExpectedTweetFavoriteApiResponse() {
        return TweetResponse.builder()
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
                .inReplyToStatusId(null)
                .favoriteFlag(false)
                .build()
    }

    /**
     * ツイート取得APIの期待値を取得する
     *
     * @return TweetResponseのList
     */
    private static final getExpectedTweetLookupApiResponse() {
        return [
                TweetResponse.builder()
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
                        .inReplyToStatusId(null)
                        .favoriteFlag(false)
                        .build(),
                TweetResponse.builder()
                        .id("967844427480911872")
                        .text("A magnetic power struggle of galactic proportions - new research highlights the role of the Sun's magnetic landscap… https://t.co/29dZgga54m")
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
                        .retweetCount(2654)
                        .favoriteCount(7962)
                        .retweetFlag(false)
                        .sensitiveFlag(false)
                        .quoteFlag(false)
                        .inReplyToStatusId(null)
                        .favoriteFlag(false)
                        .build()]
    }
}
