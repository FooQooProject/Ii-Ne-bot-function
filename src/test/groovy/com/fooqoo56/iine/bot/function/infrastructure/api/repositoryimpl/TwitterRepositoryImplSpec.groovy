package com.fooqoo56.iine.bot.function.infrastructure.api.repositoryimpl

import com.fooqoo56.iine.bot.function.infrastructure.api.dto.request.TweetRequest
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.response.*
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import spock.lang.Specification

class TwitterRepositoryImplSpec extends Specification {

    private MockWebServer mockWebServer
    private WebClient webClient

    final setup() {
        mockWebServer = new MockWebServer()
        mockWebServer.start()
        webClient = WebClient.create(mockWebServer.url("/").toString())
    }

    final cleanup() {
        mockWebServer.shutdown()
    }

    final "getBearerToken"() {
        given:
        // テスト対象クラスのインスタンス作成
        final sut = new TwitterRepositoryImpl(webClient, webClient)

        // mockサーバを作成する
        final mockResponse = new FileReader("src/test/resources/oauth2.json").text
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(mockResponse))

        // 期待値を作成する
        final expectedResults = Oauth2Response.builder()
                .tokenType("token_type")
                .accessToken("access_token")
                .build()

        when:
        final actual = sut.getBearerToken().block()

        then:
        actual == expectedResults
    }

    final "findTweet"() {
        given:
        // テスト対象クラスのインスタンス作成 - メソッドのモック化
        final sut = Spy(TwitterRepositoryImpl, constructorArgs: [webClient, webClient]) {
            getBearerToken() >> Mono.just(Oauth2Response.builder()
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
        final expectedResults = getExpectedTweetApiResponse()

        // 引数を作成する
        final request = TweetRequest.builder()
                .query("query")
                .maxId("0")
                .build()

        when:
        final actual = sut.findTweet(request).block()

        then:
        actual == expectedResults
    }

    /**
     * ツイート検索APIの期待値を取得する
     *
     * @return ツイート検索APIのレスポンス(固定値)
     */
    private final getExpectedTweetApiResponse() {
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
}
