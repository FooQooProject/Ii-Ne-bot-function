package com.fooqoo56.iine.bot.function.config;

import com.google.api.client.auth.oauth.OAuthHmacSigner;
import java.security.SecureRandom;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OAuth1.0に必要なインスタンスのbean定義
 */
@Configuration
public class AuthenticationConfig {

    /**
     * signerのbean定義
     *
     * @return OAuthHmacSigner
     */
    @Bean
    public OAuthHmacSigner signer() {
        return new OAuthHmacSigner();
    }

    /**
     * secureRandomのbean定義
     *
     * @return SecureRandom
     */
    @Bean
    public SecureRandom secureRandom() {
        return new SecureRandom();
    }
}
