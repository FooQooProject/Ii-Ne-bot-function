package com.fooqoo56.iine.bot.function.exception;

/**
 * IiNeBotの独自例外
 */
public abstract class IiNeBotException extends RuntimeException {

    private static final long serialVersionUID = -155442821866451585L;

    /**
     * メッセージ付きコンストラクタ
     *
     * @param message メッセージ
     */
    public IiNeBotException(final String message) {
        super(message);
    }

    /**
     * メッセージと例外付きコンストラクタ
     *
     * @param message   メッセージ
     * @param throwable 例外
     */
    public IiNeBotException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
