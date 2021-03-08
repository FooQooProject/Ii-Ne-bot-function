package com.fooqoo56.iine.bot.function.infrastructure.api.config;

import java.io.Serializable;
import java.time.Duration;
import lombok.Data;

@Data
public class ApiSetting implements Serializable {

    private static final long serialVersionUID = 1497326555757182956L;

    private String baseUrl;

    private Duration connectTimeout;

    private Duration readTimeout;

    private String apikey;

    private String apiSecret;

    private String accessToken;

    private String accessTokenSecret;

    private Integer maxInMemorySize;
}
