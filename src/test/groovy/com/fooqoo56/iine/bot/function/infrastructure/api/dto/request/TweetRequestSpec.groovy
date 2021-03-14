package com.fooqoo56.iine.bot.function.infrastructure.api.dto.request

import com.fooqoo56.iine.bot.function.infrastructure.api.dto.constant.Lang
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.constant.ResultType
import org.springframework.util.LinkedMultiValueMap
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate

class TweetRequestSpec extends Specification {

    @Unroll
    final "getQueryMap #caseName"() {
        given:
        final sut = TweetRequest.builder()
                .query("spring boot")
                .lang(lang)
                .resultType(resultType)
                .maxId("-1")
                .count(100)
                .includeEntitiesFlag(Boolean.FALSE)
                .until(LocalDate.of(2021, 01, 01))
                .build()

        final LinkedMultiValueMap expected = [
                q               : ["spring boot -filter:retweets"],
                lang            : [expectedLang],
                result_type     : [expectedResultType],
                count           : ["100"],
                include_entities: ["false"],
                until           : ["2021-01-01"],
                max_id          : ["-1"]
        ]

        when:
        final actual = sut.getQueryMap()

        then:
        actual == expected

        // enumのみ検証する
        where:
        caseName                        | lang    | resultType         || expectedLang | expectedResultType
        "lang: ja, resultType: recent"  | Lang.JA | ResultType.RECENT  || "ja"         | "recent"
        "lang: en, resultType: recent"  | Lang.EN | ResultType.RECENT  || "en"         | "recent"
        "lang: ja, resultType: mixed"   | Lang.JA | ResultType.MIXED   || "ja"         | "mixed"
        "lang: en, resultType: mixed"   | Lang.EN | ResultType.MIXED   || "en"         | "mixed"
        "lang: ja, resultType: popular" | Lang.JA | ResultType.POPULAR || "ja"         | "popular"
        "lang: en, resultType: popular" | Lang.EN | ResultType.POPULAR || "en"         | "popular"
    }

    final "getFormattedUntil"() {
        given:
        final sut = TweetRequest.builder()
                .query("spring boot")
                .lang(Lang.JA)
                .resultType(ResultType.RECENT)
                .maxId("-1")
                .count(100)
                .includeEntitiesFlag(Boolean.FALSE)
                .until(LocalDate.of(2021, 01, 01))
                .build()

        expect:
        sut.getFormattedUntil() == "2021-01-01"
    }

    final "getQueryWithFilter"() {
        given:
        final sut = TweetRequest.builder()
                .query("spring boot")
                .lang(Lang.JA)
                .resultType(ResultType.RECENT)
                .maxId("-1")
                .count(100)
                .includeEntitiesFlag(Boolean.FALSE)
                .until(LocalDate.of(2021, 01, 01))
                .build()

        expect:
        sut.getQueryWithFilter() == "spring boot -filter:retweets"
    }
}
