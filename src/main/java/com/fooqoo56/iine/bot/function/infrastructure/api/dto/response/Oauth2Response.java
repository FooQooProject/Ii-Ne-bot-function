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
 * Oauth2.0のリクエストボディ
 */
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class Oauth2Response implements Serializable {

    private static final long serialVersionUID = 670574284955441461L;

    /**
     * Token Type
     */
    @JsonProperty("token_type")
    private String tokenType;

    /**
     * Access Token
     */
    @JsonProperty("access_token")
    private String accessToken;
}
