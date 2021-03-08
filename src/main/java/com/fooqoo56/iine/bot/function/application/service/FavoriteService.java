package com.fooqoo56.iine.bot.function.application.service;

import com.fooqoo56.iine.bot.function.application.sharedservice.TwitterService;
import com.fooqoo56.iine.bot.function.domain.model.Qualification;
import com.fooqoo56.iine.bot.function.domain.model.Tweet;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.request.TweetRequest;
import com.fooqoo56.iine.bot.function.presentation.function.dto.TweetQualification;
import lombok.RequiredArgsConstructor;
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

    private final TwitterService twitterService;

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

        return twitterService.findTweet(request)
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
        if (!tweet.getText().contains(qualification.getQuery())) {
            return false;
        }

        // センシティブ要件
        else if (qualification.isSensitive() != tweet.isSensitive()) {
            return false;
        }

        // 引用要件
        else if (qualification.isQuote() != tweet.isQuote()) {
            return false;
        }

        // リプライ要件
        else if (qualification.isReply() != tweet.isReply()) {
            return false;
        }

        // フォローユーザ要件
        else if (qualification.isFollow() != tweet.getUser().isFollow()) {
            return false;
        }

        // リツイート数要件
        else if (isLessThanQualification(tweet.getRetweetCount(),
                qualification.getMinRetweetCount())) {
            return false;
        }

        // いいね数要件
        else if (isLessThanQualification(tweet.getFavoriteCount(),
                qualification.getMinFavoriteCount())) {
            return false;
        }

        // フォロワー数要件
        else if (isLessThanQualification(tweet.getUser().getFollowersCount(),
                qualification.getMinFollowersCount())) {
            return false;
        }

        // ツイート数要件
        else {
            return !isLessThanQualification(tweet.getUser().getStatusesCount(),
                    qualification.getMinStatusesCount());
        }
    }

    /**
     * 要件より値が低いかどうか
     *
     * @param target        比較対象
     * @param qualification 要件
     * @return 要件より値が低い場合、trueを返す
     */
    private boolean isLessThanQualification(final Integer target,
                                            final Integer qualification) {
        // left > right -> +1, left < right -> -1, left == right => 0
        return NumberUtils.compare(target, qualification) < 0;
    }
}
