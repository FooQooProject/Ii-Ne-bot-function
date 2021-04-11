package com.fooqoo56.iine.bot.function.infrastructure.api.dto.response

import spock.lang.Specification

class UdbResponseSpec extends Specification {

    final "getOauthWithNullCheck"() {
        given:
        final sut = UdbResponse.builder()
                .oauth(
                        UdbResponse.OauthUserResponse.builder()
                                .oauthTimestamp("12345")
                                .oauthSignatureMethod("HMAC-SHA1")
                                .oauthVersion("1.0")
                                .oauthNonce("oauthNonce")
                                .oauthConsumerKey("consumerKey")
                                .oauthToken("accessToken")
                                .oauthSignature("signature")
                                .build()
                )
                .build()

        final expect = UdbResponse.OauthUserResponse.builder()
                .oauthTimestamp("12345")
                .oauthSignatureMethod("HMAC-SHA1")
                .oauthVersion("1.0")
                .oauthNonce("oauthNonce")
                .oauthConsumerKey("consumerKey")
                .oauthToken("accessToken")
                .oauthSignature("signature")
                .build()

        expect:
        sut.getOauthWithNullCheck() == expect
    }

    final "getOauthWithNullCheck 例外"() {
        given:
        final sut = UdbResponse.builder().build()

        when:
        sut.getOauthWithNullCheck()

        then:
        final exception = thrown(NullPointerException)
        exception.getMessage() == "フィールドがnullです: UdbResponse.oauth"
    }

    final "oauth NullCheck付きgetter"() {
        given:
        final sut = UdbResponse.OauthUserResponse.builder()
                .oauthTimestamp("12345")
                .oauthSignatureMethod("HMAC-SHA1")
                .oauthVersion("1.0")
                .oauthNonce("oauthNonce")
                .oauthConsumerKey("consumerKey")
                .oauthToken("accessToken")
                .oauthSignature("signature")
                .build()

        expect:
        verifyAll {
            sut.getOauthTimestampWithNullCheck() == "12345"
            sut.getOauthSignatureMethodWithNullCheck() == "HMAC-SHA1"
            sut.getOauthVersionWithNullCheck() == "1.0"
            sut.getOauthNonceWithNullCheck() == "oauthNonce"
            sut.getOauthConsumerKeyWithNullCheck() == "consumerKey"
            sut.getOauthTokenWithNullCheck() == "accessToken"
            sut.getOauthSignature() == "signature"
        }
    }

    final "getOauthTimestampWithNullCheck 例外"() {
        given:
        final sut = UdbResponse.OauthUserResponse.builder()
                .oauthTimestamp(null)
                .oauthSignatureMethod("HMAC-SHA1")
                .oauthVersion("1.0")
                .oauthNonce("oauthNonce")
                .oauthConsumerKey("consumerKey")
                .oauthToken("accessToken")
                .oauthSignature("signature")
                .build()

        when:
        sut.getOauthTimestampWithNullCheck()

        then:
        final exception = thrown(NullPointerException)
        exception.getMessage() == "フィールドがnullです: OauthUserResponse.oauthTimestamp"
    }

    final "getOauthSignatureMethodWithNullCheck 例外"() {
        given:
        final sut = UdbResponse.OauthUserResponse.builder()
                .oauthTimestamp("12345")
                .oauthSignatureMethod(null)
                .oauthVersion("1.0")
                .oauthNonce("oauthNonce")
                .oauthConsumerKey("consumerKey")
                .oauthToken("accessToken")
                .oauthSignature("signature")
                .build()

        when:
        sut.getOauthSignatureMethodWithNullCheck()

        then:
        final exception = thrown(NullPointerException)
        exception.getMessage() == "フィールドがnullです: OauthUserResponse.oauthSignatureMethod"
    }

    final "getOauthVersionWithNullCheck 例外"() {
        given:
        final sut = UdbResponse.OauthUserResponse.builder()
                .oauthTimestamp("12345")
                .oauthSignatureMethod("HMAC-SHA1")
                .oauthVersion(null)
                .oauthNonce("oauthNonce")
                .oauthConsumerKey("consumerKey")
                .oauthToken("accessToken")
                .oauthSignature("signature")
                .build()

        when:
        sut.getOauthVersionWithNullCheck()

        then:
        final exception = thrown(NullPointerException)
        exception.getMessage() == "フィールドがnullです: OauthUserResponse.oauthVersion"
    }

    final "getOauthNonceWithNullCheck 例外"() {
        given:
        final sut = UdbResponse.OauthUserResponse.builder()
                .oauthTimestamp("12345")
                .oauthSignatureMethod("HMAC-SHA1")
                .oauthVersion("1.0")
                .oauthNonce(null)
                .oauthConsumerKey("consumerKey")
                .oauthToken("accessToken")
                .oauthSignature("signature")
                .build()

        when:
        sut.getOauthNonceWithNullCheck()

        then:
        final exception = thrown(NullPointerException)
        exception.getMessage() == "フィールドがnullです: OauthUserResponse.oauthNonce"
    }

    final "getOauthConsumerKeyWithNullCheck 例外"() {
        given:
        final sut = UdbResponse.OauthUserResponse.builder()
                .oauthTimestamp("12345")
                .oauthSignatureMethod("HMAC-SHA1")
                .oauthVersion("1.0")
                .oauthNonce("oauthNonce")
                .oauthConsumerKey(null)
                .oauthToken("accessToken")
                .oauthSignature("signature")
                .build()

        when:
        sut.getOauthConsumerKeyWithNullCheck()

        then:
        final exception = thrown(NullPointerException)
        exception.getMessage() == "フィールドがnullです: OauthUserResponse.oauthConsumerKey"
    }

    final "getOauthTokenWithNullCheck 例外"() {
        given:
        final sut = UdbResponse.OauthUserResponse.builder()
                .oauthTimestamp("12345")
                .oauthSignatureMethod("HMAC-SHA1")
                .oauthVersion("1.0")
                .oauthNonce("oauthNonce")
                .oauthConsumerKey("consumerKey")
                .oauthToken(null)
                .oauthSignature("signature")
                .build()

        when:
        sut.getOauthTokenWithNullCheck()

        then:
        final exception = thrown(NullPointerException)
        exception.getMessage() == "フィールドがnullです: OauthUserResponse.oauthToken"
    }

    final "getOauthSignatureWithNullCheck 例外"() {
        given:
        final sut = UdbResponse.OauthUserResponse.builder()
                .oauthTimestamp("12345")
                .oauthSignatureMethod("HMAC-SHA1")
                .oauthVersion("1.0")
                .oauthNonce("oauthNonce")
                .oauthConsumerKey("consumerKey")
                .oauthToken("accessToken")
                .oauthSignature(null)
                .build()

        when:
        sut.getOauthSignatureWithNullCheck()

        then:
        final exception = thrown(NullPointerException)
        exception.getMessage() == "フィールドがnullです: OauthUserResponse.oauthSignature"
    }
}
