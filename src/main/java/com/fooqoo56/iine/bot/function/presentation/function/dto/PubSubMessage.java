package com.fooqoo56.iine.bot.function.presentation.function.dto;

import java.io.Serializable;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.lang.NonNull;

/**
 * Google Cloud PubsubからPublishされるメッセージ
 */
@Getter
@ToString
@RequiredArgsConstructor
@Builder
public class PubSubMessage implements Serializable {

    private static final long serialVersionUID = -4523165913549401939L;

    /**
     * pub/subのペイロード
     */
    @NonNull
    private final String data;

    /**
     * attributes
     */
    @NonNull
    private final Map<String, String> attributes;

    /**
     * メッセージID
     */
    @NonNull
    private final String messageId;

    /**
     * publishされた時刻
     */
    @NonNull
    private final String publishTime;

    /**
     * 入力内容をログ化する
     *
     * @return ログの文字列
     */
    @NonNull
    public String getLog() {
        return String.format("メッセージID: %s, attributes: %s, publishTime: %s, data: %s", messageId,
                attributes, publishTime, data);
    }
}
