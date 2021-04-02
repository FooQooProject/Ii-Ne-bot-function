package com.fooqoo56.iine.bot.function.application.sharedservice;

import com.fooqoo56.iine.bot.function.domain.model.Tweet;
import com.fooqoo56.iine.bot.function.domain.model.TwitterUser;
import com.fooqoo56.iine.bot.function.domain.model.User;
import com.fooqoo56.iine.bot.function.domain.repository.api.TwitterRepository;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.request.TweetRequest;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.response.TweetListResponse;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.response.TweetResponse;
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

    /**
     * ツイートを検索する
     *
     * @param request 要件
     * @return TweetのFlux
     */
    @NonNull
    public Flux<Tweet> findTweet(final TweetRequest request, final TwitterUser twitterUser) {
        return twitterRepository.findTweet(request)
                .map(TweetListResponse::getStatuses)
                .flatMapMany(Flux::fromIterable)
                .map(this::buildTweet);
    }

    /**
     * ツイートをいいねする
     *
     * @param id ツイートID
     * @return TweetのOptional
     */
    @NonNull
    public Mono<Optional<Tweet>> favoriteTweet(final String id, final TwitterUser twitterUser) {
        return twitterRepository.favoriteTweet(id, twitterUser)
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
    public Flux<Tweet> lookUpTweet(final List<String> idList, final TwitterUser twitterUser) {
        return twitterRepository.lookupTweet(idList)
                .map(this::buildTweet);
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
