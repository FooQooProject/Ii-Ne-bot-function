package com.fooqoo56.iine.bot.function.infrastructure.api.util;

import static java.nio.charset.StandardCharsets.UTF_8;


import com.fooqoo56.iine.bot.function.exception.NotSuccessGetOauthHmacSignerException;
import com.google.api.client.auth.oauth.OAuthHmacSigner;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.springframework.lang.NonNull;
import org.springframework.web.util.UriUtils;

/**
 * OAuth2.0の認証ヘッダ作成クラス
 */
@Builder
@EqualsAndHashCode
public class OauthAuthorizationHeaderBuilder implements Serializable {

    private static final String AND_DELIMITER = "&";

    private static final long serialVersionUID = -8790302082606292722L;

    /**
     * Access Token Secret
     */
    @NonNull
    private final String consumerSecret;

    /**
     * HTTPメソッド
     */
    @NonNull
    private final String method;

    /**
     * クエリパラメータのマップ
     */
    @NonNull
    private final Map<String, String> queryParameters;

    /**
     * API Key Secret
     */
    @NonNull
    private final String tokenSecret;

    /**
     * アクセスするAPIのURL
     */
    @NonNull
    private final String url;

    /**
     * API Key
     */
    @NonNull
    private final String consumerKey;

    /**
     * Access Token
     */
    @NonNull
    private final String accessToken;

    /**
     * OauthHeaderの取得.
     *
     * @return OauthHeader
     */
    @NonNull
    public String getOauthHeader() throws NotSuccessGetOauthHmacSignerException {
        final Map<String, String> parameters = new LinkedHashMap<>(queryParameters);

        // Boiler plate parameters
        parameters.put("oauth_timestamp", String.valueOf(Instant.now().getEpochSecond()));
        parameters.put("oauth_signature_method", "HMAC-SHA1");
        parameters.put("oauth_version", "1.0");
        parameters.put("oauth_nonce", String.valueOf(Math.random() * 100000000));
        parameters.put("oauth_consumer_key", consumerKey);
        parameters.put("oauth_token", accessToken);

        // Build the parameter string after sorting the keys in lexicographic order per the OAuth v1 spec.
        final String parameterString = parameters.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> encodeUriComponent(e.getKey()) + "=" + encodeUriComponent(e.getValue()))
                .collect(Collectors.joining(AND_DELIMITER));

        // Build the signature base string
        final String signatureBaseString = method.toUpperCase()
                + AND_DELIMITER
                + encodeUriComponent(url)
                + AND_DELIMITER
                + encodeUriComponent(parameterString);

        // Sign the Signature Base String
        final String signature = generateSignature(signatureBaseString);

        // Add the signature to be included in the header
        parameters.put("oauth_signature", signature);

        // Build the authorization header value using the order in which the parameters were added
        return "OAuth " + parameters.entrySet().stream()
                .map(e -> encodeUriComponent(e.getKey()) + "=\""
                        + encodeUriComponent(e.getValue()) + "\"")
                .collect(Collectors.joining(", "));
    }

    /**
     * Replaces any character not specifically unreserved to an equivalent percent sequence.
     *
     * @param s the string to encode
     * @return and encoded string
     */
    @NonNull
    private String encodeUriComponent(final String s) {
        return UriUtils.encode(s, UTF_8);
    }

    /**
     * OauthHmacSignerの取得.
     *
     * @return OauthHmacSigner
     */
    @NonNull
    private OAuthHmacSigner getOauthHmacSigner() {
        final OAuthHmacSigner signer = new OAuthHmacSigner();
        signer.clientSharedSecret = consumerSecret;
        signer.tokenSharedSecret = tokenSecret;
        return signer;
    }

    /**
     * 鍵を生成する
     *
     * @param message メッセージ
     * @return 鍵
     * @throws NotSuccessGetOauthHmacSignerException 鍵の作成に失敗した場合の例外
     */
    @NonNull
    private String generateSignature(final String message)
            throws NotSuccessGetOauthHmacSignerException {
        final OAuthHmacSigner signer = getOauthHmacSigner();

        try {
            return signer.computeSignature(message);
        } catch (final GeneralSecurityException e) {
            throw new NotSuccessGetOauthHmacSignerException("OAuth2.0の認証鍵作成に失敗しました");
        }
    }
}
