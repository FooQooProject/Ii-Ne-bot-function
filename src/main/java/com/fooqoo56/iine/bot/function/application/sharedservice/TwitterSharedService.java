package com.fooqoo56.iine.bot.function.application.sharedservice;

import com.fooqoo56.iine.bot.function.domain.model.Oauth;
import com.fooqoo56.iine.bot.function.domain.model.Tweet;
import com.fooqoo56.iine.bot.function.domain.model.User;
import com.fooqoo56.iine.bot.function.domain.repository.api.FireStoreRepository;
import com.fooqoo56.iine.bot.function.domain.repository.api.TwitterRepository;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.request.TweetRequest;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.response.TweetListResponse;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.response.TweetResponse;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.response.UdbResponse;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.response.UserResponse;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * TwitterAPIの共通サービス層
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TwitterSharedService {

    private final TwitterRepository twitterRepository;
    private final FireStoreRepository fireStoreRepository;

    /**
     * ツイートを検索する
     *
     * @param request 要件
     * @return TweetのFlux
     */
    @NonNull
    public Flux<Tweet> findTweet(final TweetRequest request) {
        return twitterRepository.findTweet(request)
                .map(TweetListResponse::getStatuses)
                .flatMapMany(Flux::fromIterable)
                .map(this::buildTweet);
    }

    /**
     * ツイートをいいねする
     *
     * @param tweetId ツイートID
     * @param userId  ユーザID
     * @return TweetのOptional
     */
    @NonNull
    public Mono<Optional<Tweet>> favoriteTweet(final String tweetId, final String userId) {
        return getTwitterOauth(userId, tweetId)
                .flatMap(user -> twitterRepository.favoriteTweet(tweetId, user))
                .map(this::buildTweet)
                .map(Optional::of)
                .onErrorResume(WebClientResponseException.class,
                        exception -> {
                            log.error(exception.toString());
                            return Mono.just(Optional.empty());
                        });
    }

    /**
     * ツイートのID指定で取得する
     *
     * @param idList IDのリスト
     * @return TweetのFlux
     */
    @NonNull
    public Flux<Tweet> lookUpTweet(final List<String> idList) {
        return twitterRepository.lookupTweet(idList)
                .map(this::buildTweet);
    }

    /**
     * ツイッターユーザ取得
     *
     * @param userId  userId
     * @param tweetId tweetId
     * @return ツイッターユーザ
     */
    @NonNull
    private Mono<Oauth> getTwitterOauth(final String userId, final String tweetId) {
        return fireStoreRepository.getTwitterUser(userId, tweetId)
                .map(this::buildUserOauth);
    }

    /**
     * UserOauthを作成する
     *
     * @param udbResponse udbのAPIレスポンス
     * @return 認証ドメイン
     */
    private Oauth buildUserOauth(final UdbResponse udbResponse) {
        final UdbResponse.OauthUserResponse oauthUserResponse = udbResponse.getOauthWithNullCheck();

        return Oauth.builder()
                .oauthTimestamp(oauthUserResponse.getOauthTimestampWithNullCheck())
                .oauthSignatureMethod(oauthUserResponse.getOauthSignatureMethodWithNullCheck())
                .oauthVersion(oauthUserResponse.getOauthVersionWithNullCheck())
                .oauthNonce(oauthUserResponse.getOauthNonceWithNullCheck())
                .oauthConsumerKey(oauthUserResponse.getOauthConsumerKeyWithNullCheck())
                .oauthToken(oauthUserResponse.getOauthTokenWithNullCheck())
                .oauthSignature(oauthUserResponse.getOauthSignatureWithNullCheck())
                .build();
    }

    /**
     * ツイートドメインを作成する
     *
     * @param tweetResponse 検索APIのレスポンス
     * @return ツイートドメイン
     */
    @NonNull
    private Tweet buildTweet(final TweetResponse tweetResponse) {
        return Tweet.builder()
                .id(tweetResponse.getIdWithNullCheck())
                .text(tweetResponse.getTextWithNullCheck())
                .retweetCount(tweetResponse.getRetweetCountWithNullCheck())
                .favoriteCount(tweetResponse.getFavoriteCountWithNullCheck())
                .favorite(tweetResponse.isFavorite())
                .retweet(tweetResponse.isRetweet())
                .sensitive(tweetResponse.isSensitive())
                .quote(tweetResponse.isQuote())
                .reply(tweetResponse.isReply())
                .user(buildUser(tweetResponse.getUserWithNullCheck()))
                .build();
    }

    /**
     * ユーザドメインを作成する
     *
     * @param userResponse ユーザレスポンス
     * @return ユーザドメイン
     */
    @NonNull
    private User buildUser(final UserResponse userResponse) {
        return User.builder()
                .id(userResponse.getIdWithNullCheck())
                .followersCount(userResponse.getFollowersCountWithNullCheck())
                .friendsCount(userResponse.getFriendsCountWithNullCheck())
                .listedCount(userResponse.getListedCountWithNullCheck())
                .favouritesCount(userResponse.getFavouritesCountWithNullCheck())
                .statusesCount(userResponse.getStatusesCountWithNullCheck())
                .follow(userResponse.isFollow())
                .defaultProfile(userResponse.isDefaultProfile())
                .defaultProfileImage(userResponse.isDefaultProfileImage())
                .build();
    }
}
