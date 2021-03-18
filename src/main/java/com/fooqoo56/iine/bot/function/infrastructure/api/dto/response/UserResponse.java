package com.fooqoo56.iine.bot.function.infrastructure.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.lang.NonNull;

/**
 * ツイッター検索APIのレスポンスに含まれるユーザデータ
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class UserResponse implements Serializable {

    private static final long serialVersionUID = -2037788819579328427L;

    /**
     * ユーザID
     */
    @JsonProperty("id_str")
    private String id;

    /**
     * フォロワー数
     */
    @JsonProperty("followers_count")
    private Integer followersCount;

    /**
     * フォロー数
     */
    @JsonProperty("friends_count")
    private Integer friendsCount;

    /**
     * リストに登録された数
     */
    @JsonProperty("listed_count")
    private Integer listedCount;

    /**
     * いいねしてる数
     */
    @JsonProperty("favourites_count")
    private Integer favouritesCount;

    /**
     * ツイートしてる数
     */
    @JsonProperty("statuses_count")
    private Integer statusesCount;

    /**
     * フォロー済の場合、true
     */
    @JsonProperty("following")
    private Boolean following;

    /**
     * デフォルトのプロフィールのままの場合、true
     */
    @JsonProperty("default_profile")
    private Boolean defaultProfileFlag;

    /**
     * デフォルトのプロフィール画像のままの場合、true
     */
    @JsonProperty("default_profile_image")
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
