package com.fooqoo56.iine.bot.function.infrastructure.api.config;

import io.netty.channel.ChannelOption;
import io.netty.resolver.DefaultAddressResolverGroup;
import java.time.Duration;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

    /**
     * connector.
     */
    private static final BiFunction<Duration, Duration, ReactorClientHttpConnector> CONNECTOR =
            (connectTimeout, readTimeout) -> new ReactorClientHttpConnector(HttpClient.create()
                    .responseTimeout(readTimeout)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout.toMillisPart())
                    .resolver(DefaultAddressResolverGroup.INSTANCE));

    /**
     * strategy.
     */
    private static final Function<Integer, ExchangeStrategies> STRATEGY =
            (maxInMemorySize) -> ExchangeStrategies.builder()
                    .codecs(configurer -> configurer
                            .defaultCodecs()
                            .maxInMemorySize(maxInMemorySize))
                    .build();

    /**
     * ツイート検索APIの設定.
     *
     * @return TwitterSetting
     */
    @Bean
    @ConfigurationProperties(prefix = "extension.api.twitter.search")
    @NonNull
    public ApiSetting twitterSearchApiSetting() {
        return new ApiSetting();
    }

    /**
     * OauthAPIの設定.
     *
     * @return TwitterSetting
     */
    @Bean
    @ConfigurationProperties(prefix = "extension.api.twitter.oauth")
    public ApiSetting twitterOauthApiSetting() {
        return new ApiSetting();
    }

    /**
     * 検索APIクライアント.
     *
     * @return WebClient
     */
    @Bean
    @NonNull
    public WebClient twitterSearchClient(
            @Qualifier(value = "twitterSearchApiSetting") final ApiSetting apiSetting) {

        final ReactorClientHttpConnector connector = CONNECTOR
                .apply(apiSetting.getConnectTimeout(), apiSetting.getReadTimeout());

        return WebClient.builder()
                .baseUrl(apiSetting.getBaseUrl())
                .exchangeStrategies(STRATEGY.apply(apiSetting.getMaxInMemorySize()))
                .clientConnector(connector)
                .build();
    }

    /**
     * Bearer APIのWebClient取得
     *
     * @param apiSetting API設定
     * @return WebClient
     */
    @Bean
    @NonNull
    public WebClient bearerTokenTwitterClient(
            @Qualifier(value = "twitterOauthApiSetting") final ApiSetting apiSetting) {

        final ReactorClientHttpConnector connector = CONNECTOR
                .apply(apiSetting.getConnectTimeout(), apiSetting.getReadTimeout());

        return WebClient.builder()
                .baseUrl(apiSetting.getBaseUrl())
                .clientConnector(connector)
                .filter(ExchangeFilterFunctions
                        .basicAuthentication(apiSetting.getApikey(), apiSetting.getApiSecret()))
                .build();
    }
}
