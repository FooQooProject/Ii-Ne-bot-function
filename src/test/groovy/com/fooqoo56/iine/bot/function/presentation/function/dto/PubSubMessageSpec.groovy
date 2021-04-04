package com.fooqoo56.iine.bot.function.presentation.function.dto

import spock.lang.Specification

class PubSubMessageSpec extends Specification {

    final "getLog"() {
        given:
        final sut = PubSubMessage.builder()
                .data("data")
                .messageId("messageId")
                .publishTime("publishTime")
                .attributes(Map.of("key", "value"))
                .build()

        final expect = "messageID: messageId"

        expect:
        sut.getLog() == expect
    }

    final "getter"() {
        given:
        final sut = PubSubMessage.builder()
                .data("data")
                .messageId("messageId")
                .publishTime("publishTime")
                .attributes(Map.of("key", "value"))
                .build()

        expect:
        verifyAll {
            sut.getData() == "data"
            sut.getMessageId() == "messageId"
            sut.getPublishTime() == "publishTime"
            sut.getAttributes() == Map.of("key", "value")
        }
    }
}
