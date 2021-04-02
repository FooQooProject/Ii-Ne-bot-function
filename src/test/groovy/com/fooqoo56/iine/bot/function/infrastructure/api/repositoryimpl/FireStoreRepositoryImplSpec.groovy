package com.fooqoo56.iine.bot.function.infrastructure.api.repositoryimpl


import com.fooqoo56.iine.bot.function.infrastructure.api.config.ApiSetting
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.response.UdbResponse
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
import java.time.Clock

/**
 * TwitterRepositoryのテスト
 */
class FireStoreRepositoryImplSpec extends Specification {

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
                .user(UdbResponse.TwitterUserResponse.builder()
                        .userId("userId")
                        .accessToken("accessToken")
                        .accessTokenSecret("accessTokenSecret")
                        .build())
                .build()

        when:
        final actual = sut.getTwitterUser("id").block()

        then:
        actual == expectedResults
    }
}
