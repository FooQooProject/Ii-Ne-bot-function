package com.fooqoo56.iine.bot.function.infrastructure.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Udbのレスポンス
 */
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class UdbResponse implements Serializable {

    private static final long serialVersionUID = -1872421997598192120L;

    @JsonProperty("user")
    private TwitterUserResponse user;

    @Getter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    @Builder
    public static class TwitterUserResponse implements Serializable {

        private static final long serialVersionUID = 4834470782480293668L;

        @JsonProperty("userId")
        private String userId;

        @JsonProperty("accessToken")
        private String accessToken;

        @JsonProperty("accessTokenSecret")
        private String accessTokenSecret;
    }


}
