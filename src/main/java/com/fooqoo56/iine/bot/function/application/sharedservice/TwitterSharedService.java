package com.fooqoo56.iine.bot.function.application.sharedservice;

import com.fooqoo56.iine.bot.function.domain.model.Tweet;
import com.fooqoo56.iine.bot.function.domain.model.User;
import com.fooqoo56.iine.bot.function.domain.repository.api.TwitterRepository;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.request.TweetRequest;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.response.TweetListResponse;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.response.TweetResponse;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * TwitterAPIの共通サービス層
 */
@Service
@RequiredArgsConstructor
public class TwitterSharedService {

    private final TwitterRepository twitterRepository;

    /**
     * ツイートを検索する
     *
     * @param request 要件
     * @return TweetResponseのFlux
     */
    @NonNull
    public Flux<Tweet> findTweet(final TweetRequest request) {

        return twitterRepository.findTweet(request)
                .map(TweetListResponse::getStatuses)
                .flatMapMany(Flux::fromIterable)
                .map(this::buildTweet);
    }

    /**
     * ツイートドメインを作成する
     *
     * @param tweetResponse 検索APIのレスポンス
     * @return ツイートドメイン
     */
    private Tweet buildTweet(final TweetResponse tweetResponse) {
        return Tweet.builder()
                .id(tweetResponse.getId())
                .text(tweetResponse.getText())
                .retweetCount(tweetResponse.getRetweetCount())
                .favoriteCount(tweetResponse.getFavoriteCount())
                .favorite(tweetResponse.getFavoriteFlag())
                .retweet(tweetResponse.getRetweetFlag())
                .sensitive(tweetResponse.getSensitiveFlag())
                .quote(tweetResponse.getQuoteFlag())
                .reply(tweetResponse.isReply())
                .user(buildUser(tweetResponse.getUser()))
                .build();
    }

    /**
     * ユーザドメインを作成する
     *
     * @param userResponse ユーザレスポンス
     * @return ユーザドメイン
     */
    private User buildUser(final UserResponse userResponse) {
        return User.builder()
                .id(userResponse.getId())
                .followersCount(userResponse.getFollowersCount())
                .friendsCount(userResponse.getFriendsCount())
                .listedCount(userResponse.getListedCount())
                .favouritesCount(userResponse.getFavouritesCount())
                .friendsCount(userResponse.getFriendsCount())
                .statusesCount(userResponse.getStatusesCount())
                .follow(userResponse.isFollow())
                .defaultProfile(userResponse.getDefaultProfileFlag())
                .defaultProfileImage(userResponse.getDefaultProfileImageFlag())
                .build();
    }
}
