package com.fooqoo56.iine.bot.function.infrastructure.api.config;

import java.io.Serializable;
import java.time.Duration;
import lombok.Data;

@Data
public class ApiSetting implements Serializable {

    private static final long serialVersionUID = 1497326555757182956L;

    /**
     * URL
     */
    private String baseUrl;

    /**
     * 接続タイムアウト
     */
    private Duration connectTimeout;

    /**
     * 読み込みタイムアウト
     */
    private Duration readTimeout;

    /**
     * API Key
     */
    private String apikey;

    /**
     * API Secret
     */
    private String apiSecret;

    /**
     * Access Token
     */
    private String accessToken;

    /**
     * Access Token Secret
     */
    private String accessTokenSecret;

    /**
     * 最大メモリサイズ
     */
    private Integer maxInMemorySize;
}
