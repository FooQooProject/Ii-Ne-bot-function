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
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * ツイッター検索APIのレスポンスに含まれるユーザデータ
 */
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class UserResponse implements Serializable {

    private static final Long serialVersionUID = -2037788819579328427L;

    @JsonProperty("id_str")
    @NonNull
    private String id;

    @JsonProperty("followers_count")
    @NonNull
    private Integer followersCount;

    @JsonProperty("friends_count")
    @NonNull
    private Integer friendsCount;

    @JsonProperty("listed_count")
    @NonNull
    private Integer listedCount;

    @JsonProperty("favourites_count")
    @NonNull
    private Integer favouritesCount;

    @JsonProperty("statuses_count")
    @NonNull
    private Integer statusesCount;

    @JsonProperty("following")
    @Nullable
    private Boolean following;

    @JsonProperty("default_profile")
    @NonNull
    private Boolean defaultProfileFlag;

    @JsonProperty("default_profile_image")
    @NonNull
    private Boolean defaultProfileImageFlag;

    /**
     * ユーザをフォローしているか
     *
     * @return ユーザをフォローしている場合、trueを返す
     */
    @NonNull
    public boolean isFollow() {
        return BooleanUtils.isTrue(getFollowing());
    }
}
