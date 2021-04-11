package com.fooqoo56.iine.bot.function.domain.model;

import static java.nio.charset.StandardCharsets.UTF_8;


import java.io.Serializable;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.lang.NonNull;
import org.springframework.web.util.UriUtils;

@Getter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
@Builder
public class UserOauth implements Serializable {

    private static final long serialVersionUID = 6298021157481576949L;

    @NonNull
    private final String oauthTimestamp;

    @NonNull
    private final String oauthSignatureMethod;

    @NonNull
    private final String oauthVersion;

    @NonNull
    private final String oauthNonce;

    @NonNull
    private final String oauthConsumerKey;

    @NonNull
    private final String oauthToken;

    @NonNull
    private final String oauthSignature;

    /**
     * Replaces any character not specifically unreserved to an equivalent percent sequence.
     *
     * @param text the string to encode
     * @return and encoded string
     */
    @NonNull
    private static String encodeUriComponent(final String text) {
        return UriUtils.encode(text, UTF_8);
    }

    /**
     * AuthorizationHeaderの取得
     *
     * @param id ツイートID
     * @return AuthorizationHeader
     */
    @NonNull
    public String getOauthAuthorizationHeader(final String id) {
        return String.format("OAuth %s, %s, %s, %s, %s, %s, %s, %s",
                getOauthKV("id", id),
                getOauthKV("oauth_timestamp", oauthTimestamp),
                getOauthKV("oauth_signature_method", oauthSignatureMethod),
                getOauthKV("oauth_version", oauthVersion),
                getOauthKV("oauth_nonce", oauthNonce),
                getOauthKV("oauth_consumer_key", oauthConsumerKey),
                getOauthKV("oauth_token", oauthToken),
                getOauthKV("oauth_signature", oauthSignature));
    }

    /**
     * OauthのパラメータをKey-Value形式で取得
     *
     * @param key   パラメータキー
     * @param value パラメータバリュー
     * @return Oauthパラメータ(KeyValue形式)
     */
    @NonNull
    private String getOauthKV(final String key, final String value) {
        return String.format("%s=\"%s\"", encodeUriComponent(key), encodeUriComponent(value));
    }
}
