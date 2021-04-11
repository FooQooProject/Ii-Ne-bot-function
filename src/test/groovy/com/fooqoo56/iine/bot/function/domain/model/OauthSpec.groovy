package com.fooqoo56.iine.bot.function.domain.model

import spock.lang.Specification

class OauthSpec extends Specification {

    final "getOauthAuthorizationHeader"() {
        given:
        final sut = Oauth.builder()
                .oauthTimestamp("12345")
                .oauthSignatureMethod("HMAC-SHA1")
                .oauthVersion("1.0")
                .oauthNonce("oauthNonce")
                .oauthConsumerKey("consumerKey")
                .oauthToken("accessToken")
                .oauthSignature("signature")
                .build()

        final expect = "OAuth id=\"id\", oauth_timestamp=\"12345\", oauth_signature_method=\"HMAC-SHA1\", oauth_version=\"1.0\", oauth_nonce=\"oauthNonce\", oauth_consumer_key=\"consumerKey\", oauth_token=\"accessToken\", oauth_signature=\"signature\""

        expect:
        sut.getOauthAuthorizationHeader("id") == expect
    }

    final "encodeUriComponent"() {
        given:
        final sut = Oauth.builder()
                .oauthTimestamp("12345")
                .oauthSignatureMethod("HMAC-SHA1")
                .oauthVersion("1.0")
                .oauthNonce("oauthNonce")
                .oauthConsumerKey("consumerKey")
                .oauthToken("accessToken")
                .oauthSignature("signature")
                .build()

        final expect = "https%3A%2F%2Fapi.twitter.com%2F1.1%2Ffavorites%2Fcreate.json%3Fid%3D12345"

        expect:
        sut.encodeUriComponent("https://api.twitter.com/1.1/favorites/create.json?id=12345") == expect
    }

    final "getOauthKeyValue"() {
        given:
        final sut = Oauth.builder()
                .oauthTimestamp("12345")
                .oauthSignatureMethod("HMAC-SHA1")
                .oauthVersion("1.0")
                .oauthNonce("oauthNonce")
                .oauthConsumerKey("consumerKey")
                .oauthToken("accessToken")
                .oauthSignature("signature")
                .build()

        final expect = "key=\"value\""

        expect:
        sut.getOauthKeyValue("key", "value") == expect
    }
}
