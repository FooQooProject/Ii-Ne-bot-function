package com.fooqoo56.iine.bot.function.exception;

public abstract class IiNeBotException extends RuntimeException {

    private static final long serialVersionUID = -155442821866451585L;

    public IiNeBotException(final String message) {
        super(message);
    }

    public IiNeBotException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
