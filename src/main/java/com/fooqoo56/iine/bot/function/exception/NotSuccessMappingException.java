package com.fooqoo56.iine.bot.function.exception;

/**
 * pub/subメッセージからオブジェクトへのマッピングした場合の例外
 */
public class NotSuccessMappingException extends IiNeBotException {

    private static final long serialVersionUID = -164643728314322740L;

    /**
     * コンストラクタ
     *
     * @param throwable 例外
     */
    public NotSuccessMappingException(final Throwable throwable) {
        super("pub/subメッセージからオブジェクトへのマッピングに失敗しました:", throwable);
    }
}
