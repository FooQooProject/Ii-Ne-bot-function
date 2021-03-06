package com.fooqoo56.iine.bot.function.exception;

/**
 * いいねに失敗した時の例外クラス
 */
public class NotSuccessFavoriteException extends IiNeBotException {

    private static final long serialVersionUID = 6635624716612736704L;

    /**
     * メッセージ付きコンストラクタ
     *
     * @param message 　メッセージ
     */
    public NotSuccessFavoriteException(final String message) {
        super(message);
    }
}
