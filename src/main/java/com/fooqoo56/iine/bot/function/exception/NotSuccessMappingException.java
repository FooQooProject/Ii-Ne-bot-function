package com.fooqoo56.iine.bot.function.exception;

public class NotSuccessMappingException extends IiNeBotException {

    private static final long serialVersionUID = -164643728314322740L;

    public NotSuccessMappingException(final Throwable throwable) {
        super("pubsubメッセージからオブジェクトへのマッピングに失敗しました:", throwable);
    }
}
