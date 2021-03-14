package com.fooqoo56.iine.bot.function.exception;

/**
 * 要件にあったツイートが取得できない場合の例外
 */
public class NotFoundQualifiedTweetException extends IiNeBotException {

    private static final long serialVersionUID = 7767978493455326860L;

    /**
     * メッセージ付きコンストラクタ
     *
     * @param message メッセージ
     */
    public NotFoundQualifiedTweetException(final String message) {
        super(message);
    }
}
