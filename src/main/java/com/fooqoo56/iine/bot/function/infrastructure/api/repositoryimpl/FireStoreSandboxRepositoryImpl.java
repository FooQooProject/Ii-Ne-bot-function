package com.fooqoo56.iine.bot.function.infrastructure.api.repositoryimpl;

import com.fooqoo56.iine.bot.function.domain.repository.api.FireStoreRepository;
import com.fooqoo56.iine.bot.function.infrastructure.api.dto.response.UdbResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
@Profile("local")
public class FireStoreSandboxRepositoryImpl implements FireStoreRepository {

    private final WebClient udbClient;

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<UdbResponse> getTwitterUser(final String id) {
        return udbClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("{id}")
                        .build(id))
                .retrieve()
                .bodyToMono(UdbResponse.class);

    }
}
