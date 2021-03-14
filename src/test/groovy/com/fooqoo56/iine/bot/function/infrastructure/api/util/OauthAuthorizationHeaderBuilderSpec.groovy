package com.fooqoo56.iine.bot.function.infrastructure.api.util


import com.google.api.client.auth.oauth.OAuthHmacSigner
import spock.lang.Specification

import java.security.SecureRandom
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class OauthAuthorizationHeaderBuilderSpec extends Specification {

    private Clock clock
    private SecureRandom secureRandom
    private OAuthHmacSigner signer

    final setup() {
        clock = Clock.fixed(ZonedDateTime.of(
                2021,
                01,
                01,
                12,
                12,
                12,
                0, ZoneId.of("UTC")).toInstant(), ZoneId.of("UTC"))

        secureRandom = Mock(SecureRandom) {
            nextBytes(*_) >> null
        }

        signer = new OAuthHmacSigner()
    }

    final "getOauthHeader"() {
        given:
        final sut = OauthAuthorizationHeaderBuilder.builder()
                .consumerKey("consumerKey")
                .consumerSecret("consumerSecret")
                .method("POST")
                .queryParameters(Map.of("id", "12345"))
                .tokenSecret("tokenSecret")
                .url("https://api.twitter.com/1.1/favorites/create.json?id=12345")
                .accessToken("accessToken")
                .tokenSecret("tokenSecret")
                .instant(Instant.now(clock))
                .secureRandom(secureRandom)
                .signer(signer)
                .build()

        final expected = "OAuth id=\"12345\", oauth_timestamp=\"1609470732\", oauth_signature_method=\"HMAC-SHA1\", oauth_version=\"1.0\", oauth_nonce=\"0000000000000000000000000000000000000000000000000000000000000000\", oauth_consumer_key=\"consumerKey\", oauth_token=\"accessToken\", oauth_signature=\"aoXWDkwHePXNA%2BtA4SDH4%2BWfIs4%3D\""

        // OAuthHmacSignerがモック化できないため、例外の検証はskipする
        when:
        final actual = sut.getOauthHeader()

        then:
        actual == expected
    }

    final "encodeUriComponent"() {
        given:
        final sut = OauthAuthorizationHeaderBuilder.builder()
                .consumerKey("consumerKey")
                .consumerSecret("consumerSecret")
                .method("POST")
                .queryParameters(Map.of("id", "12345"))
                .tokenSecret("tokenSecret")
                .url("https://api.twitter.com/1.1/favorites/create.json?id=12345")
                .accessToken("accessToken")
                .tokenSecret("tokenSecret")
                .instant(Instant.now(clock))
                .secureRandom(secureRandom)
                .signer(signer)
                .build()

        final expect = "https%3A%2F%2Fapi.twitter.com%2F1.1%2Ffavorites%2Fcreate.json%3Fid%3D12345"

        expect:
        sut.encodeUriComponent("https://api.twitter.com/1.1/favorites/create.json?id=12345") == expect
    }

    final "setOauthHmacSigner"() {
        given:
        final sut = OauthAuthorizationHeaderBuilder.builder()
                .consumerKey("consumerKey")
                .consumerSecret("consumerSecret")
                .method("POST")
                .queryParameters(Map.of("id", "12345"))
                .tokenSecret("tokenSecret")
                .url("https://api.twitter.com/1.1/favorites/create.json?id=12345")
                .accessToken("accessToken")
                .tokenSecret("tokenSecret")
                .instant(Instant.now(clock))
                .secureRandom(secureRandom)
                .signer(signer)
                .build()

        when:
        sut.setOauthHmacSigner()

        then:
        verifyAll {
            sut.getSigner().tokenSharedSecret == sut.getTokenSecret()
            sut.getSigner().clientSharedSecret == sut.getConsumerSecret()
        }
    }

    final "generateSignature"() {
        given:
        final sut = OauthAuthorizationHeaderBuilder.builder()
                .consumerKey("consumerKey")
                .consumerSecret("consumerSecret")
                .method("POST")
                .queryParameters(Map.of("id", "12345"))
                .tokenSecret("tokenSecret")
                .url("https://api.twitter.com/1.1/favorites/create.json?id=12345")
                .accessToken("accessToken")
                .tokenSecret("tokenSecret")
                .instant(Instant.now(clock))
                .secureRandom(secureRandom)
                .signer(signer)
                .build()

        when:
        final actual = sut.generateSignature("message")

        // OAuthHmacSignerがモック化できないため、例外の検証はskipする
        then:
        actual == "rMpNUCg16Hv3t8HsCzkMS0r9tfw="
    }

    final "generateSecretToken"() {
        given:
        final sut = OauthAuthorizationHeaderBuilder.builder()
                .consumerKey("consumerKey")
                .consumerSecret("consumerSecret")
                .method("POST")
                .queryParameters(Map.of("id", "12345"))
                .tokenSecret("tokenSecret")
                .url("https://api.twitter.com/1.1/favorites/create.json?id=12345")
                .accessToken("accessToken")
                .tokenSecret("tokenSecret")
                .instant(Instant.now(clock))
                .secureRandom(secureRandom)
                .signer(signer)
                .build()

        expect:
        sut.generateSecretToken() == "0000000000000000000000000000000000000000000000000000000000000000"
    }
}
