package com.fooqoo56.iine.bot.function

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class IiNeBotFunctionApplicationSpec extends Specification {

    final "main"() {
        expect:
        IiNeBotFunctionApplication.main()
    }
}
