package com.fooqoo56.iine.bot.function.exception;

/**
 * OAuth2.0の暗号化に失敗した場合の例外
 */
public class NotSuccessGetOauthHmacSignerException extends IiNeBotException {

    private static final long serialVersionUID = -3183622401588457560L;

    /**
     * メッセージ付きコンストラクタ
     *
     * @param message メッセージ
     */
    public NotSuccessGetOauthHmacSignerException(final String message) {
        super(message);
    }
}
