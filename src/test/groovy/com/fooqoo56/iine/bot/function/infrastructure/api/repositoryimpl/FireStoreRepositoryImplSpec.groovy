package com.fooqoo56.iine.bot.function.infrastructure.api.repositoryimpl


import com.fooqoo56.iine.bot.function.infrastructure.api.dto.response.UdbResponse
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import spock.lang.Specification

/**
 * TwitterRepositoryのテスト
 */
class FireStoreRepositoryImplSpec extends Specification {

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
        final sut = new FireStoreRepositoryImpl(webClient, webClient)

        // mockサーバを作成する
        final mockResponse = new FileReader("src/test/resources/googleAuth.txt").text
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.OK.value())
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN)
                        .setBody(mockResponse))

        // 期待値を作成する
        final expectedResults = "BEARER_TOKEN"

        when:
        final actual = sut.getBearerToken().block()

        then:
        actual == expectedResults
    }

    final "getTwitterUser"() {
        given:
        // テスト対象クラスのインスタンス作成 - メソッドのモック化
        final sut = (FireStoreRepositoryImpl) Spy(FireStoreRepositoryImpl, constructorArgs: [webClient, webClient]) {
            getBearerToken() >> Mono.just("BEARER_TOKEN")
        }

        // mockサーバを作成する
        final mockResponse = new FileReader("src/test/resources/udb.json").text
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(mockResponse))

        // 期待値を作成する
        final expectedResults = UdbResponse.builder()
                .oauth(
                        UdbResponse.OauthUserResponse.builder()
                                .oauthTimestamp("1618066055")
                                .oauthSignatureMethod("HMAC-SHA1")
                                .oauthVersion("1.0")
                                .oauthNonce("da39a3ee5e6b4b0d3255bfef95601890afd80709")
                                .oauthConsumerKey("consumerKey")
                                .oauthToken("accessToken")
                                .oauthSignature("signature")
                                .build()
                )
                .build()

        when:
        final actual = sut.getTwitterUser("userId", "tweetId").block()

        then:
        actual == expectedResults
    }
}
