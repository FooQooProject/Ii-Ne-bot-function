package com.fooqoo56.iine.bot.function.application.service;

import static com.fooqoo56.iine.bot.function.infrastructure.api.dto.request.TweetRequest.DEFAULT_MAX_ID;
import static com.fooqoo56.iine.bot.function.infrastructure.api.dto.request.TweetRequest.MAX_COUNT;


import com.fooqoo56.iine.bot.function.application.sharedservice.TwitterSharedService;
import com.fooqoo56.iine.bot.function.domain.model.Qualification;
import com.fooqoo56.iine.bot.function.domain.model.Tweet;
import com.fooqoo56.iine.bot.function.exception.NotFoundQualifiedTweetException;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.constant.Lang;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.constant.ResultType;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.request.TweetRequest;
import com.fooqoo56.iine.bot.function.presentation.function.dto.TweetQualification;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

/**
 * いいねを実施するサービスクラス
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteService {

    private static final boolean SENSITIVE_QUALIFICATION = false;
    private static final boolean QUOTE_QUALIFICATION = false;
    private static final boolean REPLY_QUALIFICATION = false;
    private static final int MIN_STATUS_QUALIFICATION = 100;
    private static final boolean FOLLOWER_QUALIFICATION = false;

    private static final int NUM_OF_TOP_TWEET = 30;

    private final TwitterSharedService twitterSharedService;
    private final Clock clock;

    /**
     * 要件の合致したツイートのいいね
     *
     * @param tweetQualification ツイート条件
     * @return ツイートのいいねに成功した場合、trueを返す
     */
    @NonNull
    public Mono<Optional<Tweet>> favoriteQualifiedTweet(
            final TweetQualification tweetQualification) {

        final Qualification qualification = buildQualification(tweetQualification);

        final TweetRequest request = buildTweetRequest(qualification);

        return twitterSharedService.findTweet(request)
                // いいね要件に合致したツイートのみフィルタリングする
                .filter(tweet -> isQualifiedTweet(tweet, qualification))
                .collectList()
                .log("collectList")
                // いいね数の降順ソート実施する
                .map(this::sortTweetOrderByFavoritesCountDesc)
                // ソート後のリストの先頭を取得する
                .map(this::getTopIdList)
                // ID指定でツイートを取得する -> いいね未実施ツイートのみフィルタリング -> IDのリストに変換
                .flatMap(this::filterNonFavoritedTweet)
                // 条件にあったツイートの件数をログ出力
                .doOnNext(idList -> log
                        .info("要件と合致したツイートの件数: {}/{}", idList.size(), NUM_OF_TOP_TWEET))
                // 先頭一件を取得する
                .map(this::getTopId)
                // ツイートのいいねを実行する
                .flatMap(twitterSharedService::favoriteTweet)
                // 条件に合致したツイートが存在しない場合に、ログ出力して、falseを返す
                .onErrorResume(NotFoundQualifiedTweetException.class,
                        exception -> {
                            log.error(exception.toString());
                            return Mono.just(Optional.empty());
                        });
    }

    /**
     * ツイッター検索APIのリクエストを生成
     *
     * @param qualification ツイート要件
     * @return ツイッター検索APIのリクエスト
     */
    @NonNull
    private TweetRequest buildTweetRequest(final Qualification qualification) {
        return TweetRequest
                .builder()
                .query(qualification.getQuery())
                .lang(Lang.JA)
                .resultType(ResultType.RECENT)
                .count(MAX_COUNT)
                .includeEntitiesFlag(Boolean.FALSE)
                .maxId(DEFAULT_MAX_ID)
                .until(LocalDate.now(clock))
                .build();
    }

    /**
     * ID指定でツイートを取得する -> いいね未実施ツイートのみフィルタリング -> IDのリストに変換
     *
     * @param idList ツイートIDのリスト
     * @return いいね未実施ツイートをフィルタしたIDのリスト
     */
    @NonNull
    private Mono<List<String>> filterNonFavoritedTweet(final List<String> idList) {
        return Mono.just(idList)
                .flatMapMany(twitterSharedService::lookUpTweet)
                .filter(Tweet::isNotFavorite)
                .map(Tweet::getId)
                .collectList();
    }

    /**
     * ツイートのソート(いいね降順)
     *
     * @param tweetList ツイートのリスト
     * @return ソート済ツイートのリスト
     */
    @NonNull
    private List<String> sortTweetOrderByFavoritesCountDesc(final List<Tweet> tweetList) {
        return tweetList.stream().sorted(Comparator
                .comparing((Tweet tweet) -> tweet.getUser().getFavouritesCount()).reversed())
                .map(Tweet::getId).collect(Collectors.toList());
    }

    /**
     * ツイートIDのリストの先頭N件を取得する
     *
     * @param idList ツイートIDのリスト
     * @return 先頭のツイート
     * @throws NotFoundQualifiedTweetException 条件に合致したツイートが存在しない場合の例外
     */
    @NonNull
    private List<String> getTopIdList(final List<String> idList)
            throws NotFoundQualifiedTweetException {
        final List<String> idListTop =
                idList.stream().limit(NUM_OF_TOP_TWEET).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(idListTop)) {
            throw new NotFoundQualifiedTweetException("条件に合致したツイートが存在しません");
        }

        return idListTop;
    }

    /**
     * ツイートIDのリストの先頭一件を取得する
     *
     * @param idList ツイートIDのリスト
     * @return 先頭のツイート
     * @throws NotFoundQualifiedTweetException 条件に合致したツイートが存在しない場合の例外
     */
    @NonNull
    private String getTopId(final List<String> idList)
            throws NotFoundQualifiedTweetException {
        return idList.stream().findFirst().orElseThrow(
                () -> new NotFoundQualifiedTweetException("条件に合致したツイートが存在しません"));
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

        // フォロー数要件
        final boolean isQualifiedFriendsCount =
                isGraterThanEqualQualification(tweet.getUser().getFriendsCount(),
                        qualification.getMinFriendsCount());

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
                isQualifiedFriendsCount,
                isQualifiedStatusesCount});
    }

    /**
     * 要件より値以上かどうか
     *
     * @param target        比較対象
     * @param qualification 要件
     * @return 要件より値以上の場合、trueを返す
     */
    private boolean isGraterThanEqualQualification(final Integer target,
                                                   final Integer qualification) {
        // left > right -> +1, left < right -> -1, left == right => 0
        return NumberUtils.compare(target, qualification) >= 0;
    }
}
