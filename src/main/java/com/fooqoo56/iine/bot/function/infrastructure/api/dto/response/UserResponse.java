package com.fooqoo56.iine.bot.function.infrastructure.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Objects;
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

    /**
     * デフォルトのプロフィールかどうか
     *
     * @return デフォルトのプロフィールの場合、trueを返す
     */
    @NonNull
    public boolean isDefaultProfile() {
        return BooleanUtils.isTrue(getDefaultProfileFlag());
    }

    /**
     * デフォルトのプロフィール画像かどうか
     *
     * @return デフォルトのプロフィール画像の場合、trueを返す
     */
    @NonNull
    public boolean isDefaultProfileImage() {
        return BooleanUtils.isTrue(getDefaultProfileImageFlag());
    }

    /**
     * ユーザID取得(NullCheck実施)
     *
     * @return ユーザID
     */
    @NonNull
    public String getIdWithNullCheck() {
        return Objects.requireNonNull(id, "フィールドがnullです: UserResponse.id");
    }

    /**
     * いいね数取得(NullCheck実施)
     *
     * @return いいね数
     */
    @NonNull
    public Integer getFriendsCountWithNullCheck() {
        return Objects.requireNonNull(friendsCount, "フィールドがnullです: UserResponse.friendsCount");
    }

    /**
     * フォロワー数の取得(NullCheck実施)
     *
     * @return フォロワー数
     */
    @NonNull
    public Integer getFollowersCountWithNullCheck() {
        return Objects.requireNonNull(followersCount, "フィールドがnullです: UserResponse.followersCount");
    }

    /**
     * リストに登録された数の取得(NullCheck実施)
     *
     * @return リストに登録された数
     */
    @NonNull
    public Integer getListedCountWithNullCheck() {
        return Objects.requireNonNull(listedCount, "フィールドがnullです: UserResponse.listedCount");
    }

    /**
     * いいね数の取得(NullCheck実施)
     *
     * @return いいね数
     */
    @NonNull
    public Integer getFavouritesCountWithNullCheck() {
        return Objects
                .requireNonNull(favouritesCount, "フィールドがnullです: UserResponse.favouritesCount");
    }

    /**
     * ツイート数の取得(NullCheck実施)
     *
     * @return ツイート数
     */
    @NonNull
    public Integer getStatusesCountWithNullCheck() {
        return Objects.requireNonNull(statusesCount, "フィールドがnullです: UserResponse.statusesCount");
    }
}
