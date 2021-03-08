package com.fooqoo56.iine.bot.function.application.service;

import com.fooqoo56.iine.bot.function.application.sharedservice.TwitterSharedService;
import com.fooqoo56.iine.bot.function.domain.model.Qualification;
import com.fooqoo56.iine.bot.function.domain.model.Tweet;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.request.TweetRequest;
import com.fooqoo56.iine.bot.function.presentation.function.dto.TweetQualification;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * いいえを実施するサービスクラス
 */
@Service
@RequiredArgsConstructor
public class FavoriteService {

    private static final boolean SENSITIVE_QUALIFICATION = false;
    private static final boolean QUOTE_QUALIFICATION = false;
    private static final boolean REPLY_QUALIFICATION = false;
    private static final int MIN_STATUS_QUALIFICATION = 100;
    private static final boolean FOLLOWER_QUALIFICATION = false;

    private final TwitterSharedService twitterSharedService;

    /**
     * ツイート実行
     *
     * @param tweetQualification ツイート条件
     * @return ツイートのいいねに成功した場合、trueを返す
     */
    @NonNull
    public Mono<Boolean> favoriteTweet(final TweetQualification tweetQualification) {

        final Qualification qualification = buildQualification(tweetQualification);

        final TweetRequest request = TweetRequest.buildTweetRequest(qualification);

        return twitterSharedService.findTweet(request)
                .filter(tweet -> isQualifiedTweet(tweet, qualification))
                .collectList()
                .thenReturn(Boolean.FALSE);
    }

    /**
     * ツイート要件を作成する
     *
     * @param tweetQualification Pub/Subから入力された要件
     * @return 要件ドメイン
     */
    @NonNull
    private Qualification buildQualification(final TweetQualification tweetQualification) {
        return Qualification.builder()
                .query(tweetQualification.getQuery())
                .minRetweetCount(tweetQualification.getRetweetCount())
                .minFavoriteCount(tweetQualification.getFavoriteCount())
                .minFollowersCount(tweetQualification.getFollowersCount())
                .minFriendsCount(tweetQualification.getFriendsCount())
                .sensitive(SENSITIVE_QUALIFICATION)
                .quote(QUOTE_QUALIFICATION)
                .reply(REPLY_QUALIFICATION)
                .minStatusesCount(MIN_STATUS_QUALIFICATION)
                .follow(FOLLOWER_QUALIFICATION)
                .build();
    }

    /**
     * ツイートが要件に合致しているか
     *
     * @param tweet         ツイート
     * @param qualification 要件
     * @return ツイートが要件に合致している場合、trueを返す
     */
    private boolean isQualifiedTweet(final Tweet tweet,
                                     final Qualification qualification) {
        // ツイートにいいね要件のクエリが含まれているか
        final boolean isContainQuery = tweet.getText().contains(qualification.getQuery());

        // センシティブ要件
        final boolean isQualifiedSensitive = qualification.isSensitive() == tweet.isSensitive();

        // 引用要件
        final boolean isQualifiedQuote = qualification.isQuote() == tweet.isQuote();

        // リプライ要件
        final boolean isQualifiedReply = qualification.isReply() == tweet.isReply();

        // フォローユーザ要件
        final boolean isQualifiedFollow = qualification.isFollow() == tweet.getUser().isFollow();

        // リツイート数要件
        final boolean isQualifiedRetweetCount =
                isGraterThanEqualQualification(tweet.getRetweetCount(),
                        qualification.getMinRetweetCount());

        // いいね数要件
        final boolean isQualifiedFavoriteCount =
                isGraterThanEqualQualification(tweet.getFavoriteCount(),
                        qualification.getMinFavoriteCount());

        // フォロワー数要件
        final boolean isQualifiedFollowersCount =
                isGraterThanEqualQualification(tweet.getUser().getFollowersCount(),
                        qualification.getMinFollowersCount());

        // ツイート数要件
        final boolean isQualifiedStatusesCount =
                isGraterThanEqualQualification(tweet.getUser().getStatusesCount(),
                        qualification.getMinStatusesCount());

        // 全ての要件の論理積を返す
        return BooleanUtils.and(new boolean[] {
                isContainQuery,
                isQualifiedSensitive,
                isQualifiedReply,
                isQualifiedFollow,
                isQualifiedQuote,
                isQualifiedRetweetCount,
                isQualifiedFavoriteCount,
                isQualifiedFollowersCount,
                isQualifiedStatusesCount});
    }

    /**
     * 要件より値が低いかどうか
     *
     * @param target        比較対象
     * @param qualification 要件
     * @return 要件より値が低い場合、trueを返す
     */
    private boolean isGraterThanEqualQualification(final Integer target,
                                                   final Integer qualification) {
        // left > right -> +1, left < right -> -1, left == right => 0
        return NumberUtils.compare(target, qualification) >= 0;
    }
}
