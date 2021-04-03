package com.fooqoo56.iine.bot.function.presentation.function;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooqoo56.iine.bot.function.application.service.FavoriteService;
import com.fooqoo56.iine.bot.function.domain.model.Tweet;
import com.fooqoo56.iine.bot.function.exception.NotSuccessMappingException;
import com.fooqoo56.iine.bot.function.presentation.function.dto.PubSubMessage;
import com.fooqoo56.iine.bot.function.presentation.function.dto.TweetQualification;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FunctionSubscriber {

    private final FavoriteService favoriteService;
    private final ObjectMapper objectMapper;

    /**
     * pubsubでメッセージを受け取る関数
     *
     * @return PubSubMessageを引数に持つ関数
     */
    @Bean
    @NonNull
    public Consumer<PubSubMessage> pubSubFunction() {
        return this::favoriteTweetFunction;
    }

    /**
     * ツイートをいいねする関数
     *
     * @param message Pub/Subからpublishされたメッセージ
     * @return ツイートのいいねに成功した場合、trueを返す
     */
    @NonNull
    private Boolean favoriteTweetFunction(final PubSubMessage message) {
        // pub/subのメッセージをログ出力する
        log.info(message.getLog());

        final TweetQualification tweetQualification = mapTweetCondition(getDecodedMessage(message));
        // ツイートのいいね実行 & 同期処理
        final Optional<Tweet> tweetOptional =
                favoriteService.favoriteQualifiedTweet(tweetQualification).block();

        // NullCheck or いいねが失敗した場合、例外を発生させる
        if (Objects.isNull(tweetOptional) || tweetOptional.isEmpty()) {
            log.error("Failed liking the tweet. messageId: {}", message.getMessageId());
            return Boolean.FALSE;
        }

        // ツイートがnon nullの場合、ログ出力する
        tweetOptional.ifPresent(
                tweet -> log.info("Finished liking the tweet. tweetId: {}", tweet.getId()));

        return Boolean.TRUE;
    }

    /**
     * pub/subのdataをツイート条件クラスにマッピングする
     *
     * @param data pub/subのdata
     * @return ツイート条件クラス
     * @throws NotSuccessMappingException マッピングに失敗した場合の例外
     */
    @NonNull
    private TweetQualification mapTweetCondition(final String data)
            throws NotSuccessMappingException {
        try {
            return objectMapper.readValue(data, TweetQualification.class);
        } catch (final Exception exception) {
            throw new NotSuccessMappingException(exception);
        }
    }

    /**
     * メッセージをデコードする
     *
     * @param message pubsubメッセージ
     * @return デコードされたdataパラメータ
     */
    @NonNull
    private String getDecodedMessage(final PubSubMessage message) {
        return new String(Base64.getDecoder().decode(message.getData()),
                StandardCharsets.UTF_8);
    }
}
